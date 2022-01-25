package org.kcr.jctpcli;

import org.jetbrains.annotations.NotNull;
import org.kcr.jctpcli.env.EnvCtn;
import org.kr.jctp.*;

public class MixTradeSpi extends CThostFtdcTraderSpi {

    public MixTradeSpi(TraderCall traderCall, EnvCtn env, String instrumentID, boolean debugMode) {
        this.traderCall = traderCall;
        this.env = env;
        this.instrumentID = instrumentID;
        this.debugMode = debugMode;
    }

    @Override
    public void OnFrontConnected(){
        System.out.println("连接上交易服务器");
        traderCall.authenticate();
    }

    @Override
    public void OnFrontDisconnected(int nReason) {
        System.out.printf("交易服务器连接断开,原因: %d\n", nReason);
    };

    @Override
    public void OnHeartBeatWarning(int nTimeLapse) {
        System.out.printf("交易服务器心跳告警，时间流逝: %d\n", nTimeLapse);
    }

    @Override
    public void OnRspAuthenticate(CThostFtdcRspAuthenticateField pRspAuthenticateField,
                                  CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast)
    {
        if (Output.pResponse("交易服务器认证", pRspInfo)) {
            return;
        }
        if (pRspAuthenticateField == null) {
            System.out.println("交易服务期返回用户认证信息为空");
            return;
        }
        if (debugMode) {
            Output.pRspAuth("交易认证", pRspAuthenticateField);
        }
        traderCall.login();
    }

    @Override
    public void OnRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin,
                               CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast)
    {
        if (Output.pResponse("交易服务器登录", pRspInfo)) {
            return;
        }
        if (pRspUserLogin == null) {
            System.out.println("交易服务期返回用户登录信息为空");
            return;
        }
        //设置登录后相关的字段
        setupEnvAfterLogin(pRspUserLogin);
        if (debugMode) {
            Output.pLoginInfo("交易登录", pRspUserLogin);
        }
        env.feedLogin();

//        var rt = traderCall.queryTradeAccount("");
//
//        if (debugMode) {
//            System.out.printf("查询账户请求:%d-%d\n", rt.requestID, rt.resultCode);
//        }
    }

    @Override
    public void OnRspQryInvestorPosition(CThostFtdcInvestorPositionField pInvestorPosition,
                                         CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (Output.pResponse("交易服务查询持仓", pRspInfo)) {
            return;
        }
        if (pInvestorPosition == null) {
            System.out.printf("交易服务查询持仓返回为空:%d-%b\n", nRequestID, bIsLast);
            env.feedEmptyPosition();
            return;
        }
        env.feedPosition(pInvestorPosition, bIsLast);
        if (debugMode) {
            Output.pInvestorPosition("交易", pInvestorPosition);
        }
    }

    @Override
    public void OnRspQryTradingAccount(CThostFtdcTradingAccountField pTradingAccount,
                                       CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (Output.pResponse("交易服务查询账号", pRspInfo)) {
            return;
        }
//        var rp = traderCall.queryInvestorPosition(instrumentID);
//        if (debugMode) {
//            System.out.printf("查询持仓请求:%d-%d\n", rp.requestID, rp.resultCode);
//        }
        if (pTradingAccount == null) {
            System.out.println("交易服务查询交易账号返回为空");
            return;
        }
        env.feedAccount(pTradingAccount);
        if (debugMode) {
            Output.pTradingAccount("交易", pTradingAccount);
        }
    }

    @Override
    public void OnRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        Output.pResponse("交易服务回包错误", pRspInfo, nRequestID, bIsLast);
    }

    @Override
    public void OnRtnOrder(CThostFtdcOrderField pOrder) {
        if (pOrder == null) {
            System.out.println("交易服务订单信息回包为空");
            return;
        }
        var status = pOrder.getOrderStatus();
        if (status == jctpConstants.THOST_FTDC_OST_Canceled) {
            //订单没有成交的回包
            env.feedOrderCancel(pOrder);
        }
        if (debugMode) {
            Output.pOrder("交易", pOrder);
        }
    }

    @Override
    public void OnRtnTrade(CThostFtdcTradeField pTrade) {
        if (pTrade == null) {
            System.out.println("交易服务交易信息回包为空");
            return;
        }
        env.feedTrade(pTrade);
        if (debugMode) {
            Output.pTrade("交易成交", pTrade);
        }
    }

    //设置登录后相关字段
    private void setupEnvAfterLogin(@NotNull CThostFtdcRspUserLoginField rsp) {
        long rf;

        var frontID = rsp.getFrontID();
        var sessionID = rsp.getSessionID();
        var maxOrderRef = rsp.getMaxOrderRef();

        if (maxOrderRef != null && (!maxOrderRef.strip().equals(""))) {
            rf = Long.parseLong(maxOrderRef);
        }else{
            rf = 0;
        }
        traderCall.setAtom(frontID, sessionID, rf);
    }

    private TraderCall traderCall;
    //行情交易对象
    private EnvCtn env;
    //debug mode
    boolean debugMode;
    //instrument id
    String instrumentID;
}
