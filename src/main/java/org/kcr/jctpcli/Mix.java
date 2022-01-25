package org.kcr.jctpcli;

import org.kcr.jctpcli.env.EnvCtn;
import org.kcr.jctpcli.env.Instr;
import org.kr.jctp.CThostFtdcMdApi;
import org.kr.jctp.CThostFtdcTraderApi;
import org.kr.jctp.THOST_TE_RESUME_TYPE;

public class Mix {
    public static void main(String []args) throws InterruptedException {
        var cnf = FJson.readCnf("./cnf.json");
        boolean debugMode = false;

        //md spi
        MixMdSpi mdSpi;

        cnf.refresh();
        if (args.length > 0) {
            if (args[0].equals("debug")) {
                debugMode = true;
            }
        }
        var instrList = cnf.getInstruments();
        if (instrList.length == 0) {
            System.out.println("没有相关的合约配置");
            return;
        }
        var instr = new Instr(instrList[0].getInstrumentID(), instrList[0].getExchangeID());

        //交易接口的封装
        var traderApi = CThostFtdcTraderApi.CreateFtdcTraderApi();
        var traderCall =  new TraderCall(traderApi,
                cnf.getBrokerID(), cnf.getAccountID(), cnf.getPassword(), cnf.getAppID(), cnf.getAuthCode());
        //交易环境
        var envCtn = new EnvCtn(traderCall, instr);
        var tradeSpi = new MixTradeSpi(traderCall, envCtn, instr.id, debugMode);
        traderApi.RegisterSpi(tradeSpi);
        traderApi.SubscribePrivateTopic(THOST_TE_RESUME_TYPE.THOST_TERT_QUICK);
        traderApi.SubscribePublicTopic(THOST_TE_RESUME_TYPE.THOST_TERT_QUICK);
        traderApi.RegisterFront(cnf.getTradeServer());
        traderApi.Init();

        while (true) {
            if (envCtn.isLogin()) {
                break;
            }
            Thread.sleep(300);
        }

        traderCall.queryTradeAccount("").outConsole("queryTradeAccount");
        while (true) {
            if (envCtn.isAccount()) {
                break;
            }
            Thread.sleep(300);
        }

        traderCall.queryInvestorPosition("").outConsole("queryInvestorPosition");
        while(true) {
            if (envCtn.isPosition()) {
                break;
            }
            Thread.sleep(300);
        }

        //行情环境
        var mdApi = CThostFtdcMdApi.CreateFtdcMdApi();
        var pMdSpi = new MixMdSpi(envCtn,
                mdApi, cnf.getBrokerID(), cnf.getAccountID(), cnf.getPassword(), cnf.getSymbols(), debugMode);
        mdApi.RegisterSpi(pMdSpi);
        mdApi.RegisterFront(cnf.getMdServer());
        mdApi.Init();

        traderApi.Join();
        mdApi.Join();
    }
}

