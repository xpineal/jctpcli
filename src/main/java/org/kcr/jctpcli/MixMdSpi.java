package org.kcr.jctpcli;

import org.kcr.jctpcli.env.EnvCtn;
import org.kcr.jctpcli.util.Output;
import org.kr.jctp.*;

public class MixMdSpi extends CThostFtdcMdSpi{
    private final CThostFtdcMdApi mdApi;
    private final CThostFtdcReqUserLoginField loginField;
    private final String[] instruments;

    //行情交易对象
    private final EnvCtn env;
    //debug mode
    boolean debugMode;

    MixMdSpi(EnvCtn env, CThostFtdcMdApi mdApi, String brokerID,
             String userID, String passwd, String[] instruments, boolean debugMode) {
        this.env = env;
        this.mdApi = mdApi;
        this.loginField = new CThostFtdcReqUserLoginField();
        loginField.setBrokerID(brokerID);
        loginField.setUserID(userID);
        loginField.setPassword(passwd);
        this.instruments = instruments;
        this.debugMode = debugMode;
    }

    @Override
    public void OnFrontConnected(){
        System.out.println("连接上行情服务器");
        mdApi.ReqUserLogin(loginField, 0);
    }

    @Override
    public void OnFrontDisconnected(int nReason) {
        System.out.printf("行情服务器连接断开,原因: %d\n", nReason);
    }

    @Override
    public void OnHeartBeatWarning(int nTimeLapse) {
        System.out.printf("行情服务器心跳告警，时间流逝:%d\n", nTimeLapse);
    }

    @Override
    public void OnRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin, CThostFtdcRspInfoField pRspInfo,
                               int nRequestID, boolean bIsLast) {
        if (Output.pResponse("行情服务器登录", pRspInfo)) {
            return;
        }
        if (pRspUserLogin == null) {
            System.out.println("行情服务期返回用户登录信息为空");
            return;
        }
        mdApi.SubscribeMarketData(instruments);
    }

    @Override
    public void OnRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        Output.pResponse("行情服务回包错误", pRspInfo, nRequestID, bIsLast);
    }

    @Override
    public void OnRtnDepthMarketData(CThostFtdcDepthMarketDataField pDepthMarketData) {
        if (debugMode) {
            Output.pDepthMarketData("行情", pDepthMarketData);
        }
        var instr = env.feedMarket(pDepthMarketData);
        if (debugMode) {
            System.out.println();
            System.out.printf("多:%d, 空:%d, 昨多:%d, 昨空:%d\n",
                    instr.CLong, instr.CShort, instr.CLongYsd, instr.CShortYsd);
        }
    }

}
