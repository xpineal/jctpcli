package org.kcr.jctpcli.dryrun;

import org.jetbrains.annotations.NotNull;
import org.kcr.jctpcli.*;
import org.kcr.jctpcli.cnf.Cnf;
import org.kcr.jctpcli.cnf.FJson;
import org.kcr.jctpcli.util.Input;
import org.kcr.jctpcli.util.Output;
import org.kr.jctp.*;

public class Trader extends CThostFtdcTraderSpi{

    public Trader(TraderCall traderCall, Cnf cnf)
    {
        this.traderCall = traderCall;
        this.cnf = cnf;
    }

    @Override
    public void OnFrontConnected(){
        System.out.println("Trade OnFrontConnected");
        traderCall.authenticate().outConsole("Trade Send ReqAuthenticate");
    }

    @Override
    public void OnFrontDisconnected(int nReason) {
        System.out.printf("Trade OnFrontDisconnected nReason: %d\n", nReason);
    };

    @Override
    public void OnHeartBeatWarning(int nTimeLapse) {
        System.out.printf("Trade OnHeartBeatWarning time lapse:%d\n", nTimeLapse);
    }

    @Override
    public void OnRspAuthenticate(CThostFtdcRspAuthenticateField pRspAuthenticateField,
                                  CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast)
    {
        if (Output.pResponse("Trade OnRspAuthenticate", pRspInfo, nRequestID, bIsLast)) {
            return;
        }
        if (pRspAuthenticateField == null) {
            System.out.println("Trade OnRspAuthenticate empty");
            return;
        }
        Output.pRspAuth("Trade", pRspAuthenticateField);
        traderCall.login().outConsole("Trade traderCall.login()");
    }

    @Override
    public void OnRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin,
                               CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast)
    {
        if (Output.pResponse("Trade OnRspUserLogin", pRspInfo, nRequestID, bIsLast)) {
            return;
        }
        if (pRspUserLogin == null) {
            System.out.println("Trade OnRspUserLogin empty");
            return;
        }
        //设置登录后相关的字段
        setupEnvAfterLogin(pRspUserLogin);
        //打印登录信息
        outputLoginInfo();
    }

    @Override
    public void OnRspUserLogout(
            CThostFtdcUserLogoutField pUserLogout, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (Output.pResponse("Trade OnRspUserLogout", pRspInfo, nRequestID, bIsLast)) {
            return;
        }
        if (pUserLogout == null) {
            System.out.println("Trade OnRspUserLogout empty");
        }
    }

    @Override
    public void OnRspOrderInsert(CThostFtdcInputOrderField pInputOrder,
                                 CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (Output.pResponse("Trade OnRspOrderInsert", pRspInfo, nRequestID, bIsLast)) {
            return;
        }
        if (pInputOrder == null) {
            System.out.println("Trade OnRspOrderInsert empty");
            return;
        }
        Output.pInputOrder("Trade", pInputOrder);
    }

    @Override
    public void OnRspOrderAction(CThostFtdcInputOrderActionField pInputOrderAction,
                                 CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (Output.pResponse("Trade OnRspOrderAction", pRspInfo, nRequestID, bIsLast)) {
            return;
        }
        if (pInputOrderAction == null) {
            System.out.println("Trade OnRspOrderAction empty");
            return;
        }
        Output.pInputOrderAction("Trade", pInputOrderAction);
    }

    @Override
    public void OnRspQryOrder(CThostFtdcOrderField pOrder,
                              CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (Output.pResponse("Trade OnRspQryOrder", pRspInfo, nRequestID, bIsLast)) {
            return;
        }
        if (pOrder == null) {
            System.out.println("Trade OnRspQryOrder empty");
            return;
        }
        Output.pOrder("Trade", pOrder);
    }

    @Override
    public void OnRspQryTrade(CThostFtdcTradeField pTrade,
                              CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (Output.pResponse("Trade OnRspQryTrade", pRspInfo, nRequestID, bIsLast)) {
            return;
        }
        if (pTrade == null) {
            System.out.println("Trade OnRspQryTrade empty");
            return;
        }
        Output.pTrade("Trade", pTrade);
    }

    @Override
    public void OnRspQryInvestorPosition(CThostFtdcInvestorPositionField pInvestorPosition,
                                         CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (Output.pResponse("Trade OnRspQryInvestorPosition", pRspInfo, nRequestID, bIsLast)) {
            return;
        }
        if (pInvestorPosition == null) {
            System.out.println("Trade OnRspQryInvestorPosition empty");
            return;
        }
        Output.pInvestorPosition("Trade", pInvestorPosition);
    }

    @Override
    public void OnRspQryTradingAccount(CThostFtdcTradingAccountField pTradingAccount,
                                       CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (Output.pResponse("Trade OnRspQryTradingAccount", pRspInfo, nRequestID, bIsLast)) {
            return;
        }
        if (pTradingAccount == null) {
            System.out.println("Trade OnRspQryTradingAccount empty");
            return;
        }
        Output.pTradingAccount("Trade", pTradingAccount);
    }

    @Override
    public void OnRspQryInvestor(CThostFtdcInvestorField pInvestor, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (Output.pResponse("Trade OnRspQryInvestor", pRspInfo, nRequestID, bIsLast)) {
            return;
        }
        if (pInvestor == null) {
            System.out.println("Trade OnRspQryInvestor empty");
            return;
        }
        Output.pInvestor("Trade", pInvestor);
    }

    @Override
    public void OnRspQryTradingCode(CThostFtdcTradingCodeField pTradingCode,
                                    CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (Output.pResponse("Trade OnRspQryTradingCode", pRspInfo, nRequestID, bIsLast)) {
            return;
        }
        if (pTradingCode == null) {
            System.out.println("Trade OnRspQryTradingCode empty");
            return;
        }
        Output.pTradingCode("Trade", pTradingCode);
    }

    @Override
    public void OnRspQryInstrumentMarginRate(CThostFtdcInstrumentMarginRateField pInstrumentMarginRate,
                                             CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (Output.pResponse("Trade OnRspQryInstrumentMarginRate", pRspInfo, nRequestID, bIsLast)) {
            return;
        }
        if (pInstrumentMarginRate == null) {
            System.out.println("Trade OnRspQryInstrumentMarginRate empty");
            return;
        }
        Output.pInstrumentMarginRate("Trade", pInstrumentMarginRate);
    }

    @Override
    public void OnRspQryInstrumentCommissionRate(CThostFtdcInstrumentCommissionRateField pInstrumentCommissionRate,
                                                 CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (Output.pResponse("Trade OnRspQryInstrumentCommissionRate", pRspInfo, nRequestID, bIsLast)) {
            return;
        }
        if (pInstrumentCommissionRate == null) {
            System.out.println("Trade OnRspQryInstrumentCommissionRate empty");
            return;
        }
        Output.pInstrumentCommissionRate("Trade", pInstrumentCommissionRate);
    }

    @Override
    public void OnRspQryExchange(CThostFtdcExchangeField pExchange,
                                 CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (Output.pResponse("Trade OnRspQryExchange", pRspInfo, nRequestID, bIsLast)) {
            return;
        }
        if (pExchange == null) {
            System.out.println("Trade OnRspQryExchange empty");
            return;
        }
        Output.pExchange("Trade", pExchange);
    }

    @Override
    public void OnRspQryInstrument(CThostFtdcInstrumentField pInstrument,
                                   CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (Output.pResponse("Trade OnRspQryInstrument", pRspInfo, nRequestID, bIsLast)) {
            return;
        }
        if (pInstrument == null) {
            System.out.println("Trade OnRspQryInstrument empty");
            return;
        }
        Output.pInstrument("Trade", pInstrument);
    }

//    新版本不再支持
//    @Override
//    public void OnRspQryInvestorPositionForComb(CThostFtdcInvestorPositionForCombField pCombAction,
//                                                CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
//        if (Output.pResponse("Trade OnRspQryInvestorPositionForComb", pRspInfo, nRequestID, bIsLast)) {
//            return;
//        }
//        if (pCombAction == null) {
//            System.out.println("Trade OnRspQryInvestorPositionForComb empty");
//            return;
//        }
//        Output.pInvestorPositionForComb("Trade", pCombAction);
//    }

    @Override
    public void OnRspQryDepthMarketData(CThostFtdcDepthMarketDataField pDepthMarketData,
                                        CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (Output.pResponse("Trade OnRspQryDepthMarketData", pRspInfo, nRequestID, bIsLast)) {
            return;
        }
        if (pDepthMarketData == null) {
            System.out.println("Trade OnRspQryDepthMarketData empty");
            return;
        }
        Output.pDepthMarketData("Trade", pDepthMarketData);
    }

//    新版本不再支持
//    @Override
//    public void OnRspQryInstrumentStatus(CThostFtdcInstrumentStatusField pInstrumentStatus,
//                                         CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
//        if (Output.pResponse("Trade OnRspQryInstrumentStatus", pRspInfo, nRequestID, bIsLast)) {
//            return;
//        }
//        if (pInstrumentStatus == null) {
//            System.out.println("Trade OnRspQryInstrumentStatus empty");
//            return;
//        }
//        Output.pInstrumentStatus("Trade", pInstrumentStatus);
//    }

    @Override
    public void OnRspQryInvestorPositionDetail(CThostFtdcInvestorPositionDetailField pInvestorPositionDetail,
                                               CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (Output.pResponse("Trade OnRspQryInvestorPositionDetail", pRspInfo, nRequestID, bIsLast)) {
            return;
        }
        if (pInvestorPositionDetail == null) {
            System.out.println("Trade OnRspQryInvestorPositionDetail empty");
            return;
        }
        Output.pInvestorPositionDetail("Trade", pInvestorPositionDetail);
    }

    @Override
    public void OnRspQryExchangeMarginRate(CThostFtdcExchangeMarginRateField pExchangeMarginRate,
                                           CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (Output.pResponse("Trade OnRspQryExchangeMarginRate", pRspInfo, nRequestID, bIsLast)) {
            return;
        }
        if (pExchangeMarginRate == null) {
            System.out.println("Trade OnRspQryExchangeMarginRate empty");
            return;
        }
        Output.pExchangeMarginRate("Trade", pExchangeMarginRate);
    }

    @Override
    public void OnRspQryExchangeMarginRateAdjust(CThostFtdcExchangeMarginRateAdjustField pExchangeMarginRateAdjust,
                                                 CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (Output.pResponse("Trade OnRspQryExchangeMarginRateAdjust", pRspInfo, nRequestID, bIsLast)) {
            return;
        }
        if (pExchangeMarginRateAdjust == null) {
            System.out.println("Trade OnRspQryExchangeMarginRateAdjust empty");
            return;
        }
        Output.pExchangeMarginRateAdjust("Trade", pExchangeMarginRateAdjust);
    }


    @Override
    public void OnRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        Output.pResponse("Trade OnRspError", pRspInfo, nRequestID, bIsLast);
    }

    @Override
    public void OnRtnOrder(CThostFtdcOrderField pOrder) {
        if (pOrder == null) {
            System.out.println("Trade OnRtnOrder empty");
            return;
        }
        Output.pOrder("Trade", pOrder);
    }

    @Override
    public void OnRtnTrade(CThostFtdcTradeField pTrade) {
        if (pTrade == null) {
            System.out.println("Trade OnRtnTrade empty");
            return;
        }
        Output.pTrade("Trade", pTrade);
    }

    @Override
    public void OnErrRtnOrderInsert(CThostFtdcInputOrderField pInputOrder, CThostFtdcRspInfoField rsp) {
        if (rsp != null && rsp.getErrorID() != 0)
        {
            System.out.printf("Trade.OnErrRtnOrderInsert: error msg[%s], error id[%d]\n",
                    rsp.getErrorMsg(), rsp.getErrorID());
        }

        if (pInputOrder == null) {
            System.out.println("Trade OnErrRtnOrderInsert empty");
            return;
        }
        Output.pInputOrder("Trade", pInputOrder);
    }

    @Override
    public void OnErrRtnOrderAction(CThostFtdcOrderActionField pOrderAction, CThostFtdcRspInfoField rsp) {
        if (rsp != null && rsp.getErrorID() != 0)
        {
            System.out.printf("Trade.OnErrRtnOrderAction: error msg[%s], error id[%d]\n",
                    rsp.getErrorMsg(), rsp.getErrorID());
        }

        if (pOrderAction == null) {
            System.out.println("Trade OnErrRtnOrderAction empty");
            return;
        }
        Output.pOrderAction("Trade", pOrderAction);
    }

    @Override
    public void OnRtnInstrumentStatus(CThostFtdcInstrumentStatusField pInstrumentStatus) {
        if (pInstrumentStatus == null) {
            System.out.println("Trade OnRtnInstrumentStatus empty");
            return;
        }
        Output.pInstrumentStatus("Trade", pInstrumentStatus);
    }

    @Override
    public void OnRspQryInstrumentOrderCommRate(CThostFtdcInstrumentOrderCommRateField pInstrumentOrderCommRate,
                                                CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (Output.pResponse("Trade OnRspQryInstrumentOrderCommRate", pRspInfo, nRequestID, bIsLast)) {
            return;
        }
        if (pInstrumentOrderCommRate == null) {
            System.out.println("Trade OnRspQryExchangeMarginRateAdjust empty");
            return;
        }
        Output.pInstrumentCommRate("Trade", pInstrumentOrderCommRate);
    }

    //打印登录后相关字段
    public void outputLoginInfo() {
        System.out.printf(
                "Trade --->:\nfrontID:%d, sessionID:%d, tradingDay:%s, brokerID:%s, userID:%s, systemName:%s, maxOrderRef:%s, "+
                "loginTime:%s, SHFETime:%s, DCETime:%s, CZCETime:%s, CFFEXTime:%s, INETime:%s\n",
                frontID, sessionID, tradingDay, brokerID, userID, systemName, maxOrderRef,
                loginTime, SHFETime, DCETime, CZCETime, FFEXTime, INETime);
    }

    //设置登录后相关字段
    private void setupEnvAfterLogin(@NotNull CThostFtdcRspUserLoginField rsp) {
        long rf;

        tradingDay = rsp.getTradingDay();
        brokerID = rsp.getBrokerID();
        userID = rsp.getUserID();
        systemName = rsp.getSystemName();
        frontID = rsp.getFrontID();
        sessionID = rsp.getSessionID();
        maxOrderRef = rsp.getMaxOrderRef();

        loginTime = rsp.getLoginTime();
        SHFETime = rsp.getSHFETime();
        DCETime = rsp.getDCETime();
        CZCETime = rsp.getDCETime();
        FFEXTime = rsp.getFFEXTime();
        INETime = rsp.getINETime();

        if (maxOrderRef != null && (!maxOrderRef.strip().equals(""))) {
            rf = Long.parseLong(maxOrderRef);
        }else{
            rf = 0;
        }
        traderCall.setAtom(frontID, sessionID, rf);
    }

    private TraderCall traderCall;
    private Cnf cnf;

    //登录后相关字段
    private int frontID;
    private int sessionID;
    private String tradingDay;
    private String brokerID;
    private String userID;
    private String systemName;
    private String maxOrderRef;
    //只做调试使用
    private String loginTime; //登录成功时间
    private String SHFETime;  //上期所时间
    private String DCETime;   //大商所时间
    private String CZCETime;  //郑商所时间
    private String FFEXTime;  //中金所时间
    private String INETime;   //能源所时间

    public static void main(String[] args) throws InterruptedException {
        var cnf = FJson.readCnf("./cnf.json");
        CThostFtdcTraderApi traderApi;
        System.out.println(cnf);
        cnf.refresh();
        if (args.length > 0) {
            traderApi = CThostFtdcTraderApi.CreateFtdcTraderApi(args[0]);
        }else{
            traderApi = CThostFtdcTraderApi.CreateFtdcTraderApi();
        }
        System.out.printf("CTP API Version [%s]\n", CThostFtdcTraderApi.GetApiVersion());//输出api版本号

        var traderCall =  new TraderCall(traderApi,
                cnf.getBrokerID(), cnf.getAccountID(), cnf.getPassword(), cnf.getAppID(), cnf.getAuthCode());

        var pTrader = new Trader(traderCall, cnf);
        traderApi.RegisterSpi(pTrader);
        traderApi.SubscribePrivateTopic(THOST_TE_RESUME_TYPE.THOST_TERT_QUICK);
        traderApi.SubscribePublicTopic(THOST_TE_RESUME_TYPE.THOST_TERT_QUICK);
        traderApi.RegisterFront(cnf.getTradeServer());
        traderApi.Init();

        while (true) {
            Thread.sleep(6000);
            var menu = Input.inputMenu();
            double price;
            String orderSysID;
            switch (menu){
                case 1:
                    traderCall.queryInvestorPosition("").outConsole("queryInvestorPosition");
                    break;
                case 2:
                    traderCall.queryInvestorPositionDetail().outConsole("queryInvestorPositionDetail");
                    break;
                case 3:
                    traderCall.queryTradeAccount("").outConsole("queryTradeAccount");
                    break;
                case 4:
                    traderCall.queryInvestor().outConsole("queryInvestor");
                    break;
                case 5:
                    //traderCall.queryInvestorPositionForComb().outConsole("queryInvestorPositionForComb");
                    System.out.println("不再支持单腿查询");
                    break;
                case 6:
                    price = Input.inputPrice();
                    traderCall.addLimitPriceOpenBuyOrder(
                            "CZCE", "MA205", price, 1).outConsole();
                    break;
                case 7:
                    price = Input.inputPrice();
                    traderCall.addLimitPriceOpenSellOrder(
                            "CZCE", "MA205", price, 1).outConsole();
                    break;
                case 8:
                    price = Input.inputPrice();
                    traderCall.addLimitPriceCloseBuyOrder(
                            "CZCE", "MA205", price, 1).outConsole();
                    break;
                case 9:
                    price = Input.inputPrice();
                    traderCall.addLimitPriceCloseSellOrder(
                            "CZCE", "MA205", price, 1).outConsole();
                    break;
                case 10:
                    orderSysID = Input.inputOrderSysID();
                    traderCall.cancelOrder(
                            "CZCE", orderSysID, "MA205").outConsole();
                    break;
                case 11:
                    traderCall.queryDepthMarketData("MA205").
                            outConsole("queryDepthMarketData");
                    break;
                case 12:
                    traderCall.queryInstrument("MA205").
                            outConsole("queryInstrument");
                    break;
                case 13:
                    price = Input.inputPrice();
                    traderCall.addGFDLimitPriceOpenBuyOrder(
                            "CZCE", "MA205", price, 1).outConsole();
                    break;
                case 14:
                    orderSysID = Input.inputOrderSysID();
                    var qry = traderCall.genQryOrder(
                            "MA205", "CZCE", orderSysID);
                    traderCall.queryOrder(qry).outConsole("queryOrder");
                    break;
            }
        }


        //traderCall.queryInvestorPositionDetail().outConsole("queryInvestorPositionDetail");
        //traderCall.queryTradeAccount("").outConsole("queryTradeAccount");
        //traderCall.queryInvestor().outConsole("queryInvestor");
        //traderCall.queryInstrument("MA205").outConsole("queryInstrument");
        //traderCall.queryInvestorPositionDetail().outConsole("queryInvestorPositionDetail");
        //traderCall.queryInvestorPositionForComb().outConsole("queryInvestorPositionForComb");
        //traderCall.queryDepthMarketData("MA205");

//        Thread.sleep(6000);
//        var r = traderCall.addLimitPriceOpenBuyOrder("CZCE", "MA205", 2876, 1);
//        r.outConsole();
//        Thread.sleep(10);
//        traderCall.cancelOrder(r.orderRef).outConsole();

//
//        Thread.sleep(6000);
//        traderCall.addLimitPriceOpenSellOrder("CZCE", "MA205", 2876, 1).outConsole();
//
//        Thread.sleep(6000);
//        var r = traderCall.addLimitPriceOpenBuyOrder(
//                "CZCE", "MA205", 2876, 1);
//        r.outConsole();
//        Thread.sleep(1000);
//        traderCall.cancelOrder(r.orderRef).outConsole();
        //traderApi.Join();
    }
}
