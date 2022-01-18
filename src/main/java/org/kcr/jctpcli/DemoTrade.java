//本demo流程是认证--->登录--->确认结算单--->下单
package org.kcr.jctpcli;

import org.kr.jctp.*;

class DemoTradeSpi extends CThostFtdcTraderSpi{
    DemoTradeSpi(CThostFtdcTraderApi traderApi, Cnf cnf, DemoOrderArg demoOrderArg)
    {
        this.traderApi =  traderApi;
        this.cnf = cnf;
        this.demoOrderArg = demoOrderArg;
        this.reqId = 0;
    }

    @Override
    public void OnRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if (pRspInfo != null) {
            System.out.printf("request id:%d, error msg:%s\n",
                    nRequestID, pRspInfo.getErrorMsg());
        }
    }

    @Override
    public void OnFrontConnected(){
        System.out.println("On Front Connected");
        CThostFtdcReqAuthenticateField field = new CThostFtdcReqAuthenticateField();
        field.setBrokerID(cnf.getBrokerID());
        field.setUserID(cnf.getAccountID());
        field.setAppID(cnf.getAppID());
        field.setAuthCode(cnf.getAuthCode());
        var r = traderApi.ReqAuthenticate(field, ++reqId);
        System.out.printf("Send ReqAuthenticate result:%d\n", r);
    }

    @Override
    public void OnFrontDisconnected(int nReason) {
        System.out.printf("OnFrontDisconnected nReason[%d]\n", nReason);
    };
	
    @Override
    public void OnRspAuthenticate(CThostFtdcRspAuthenticateField pRspAuthenticateField, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast)
    {
        if (pRspInfo != null && pRspInfo.getErrorID() != 0)
        {
             System.out.printf("Login ErrorID[%d] ErrMsg[%s]\n", pRspInfo.getErrorID(), pRspInfo.getErrorMsg());
             return;
        }
        System.out.println("OnRspAuthenticate success!!!");
		CThostFtdcReqUserLoginField field = new CThostFtdcReqUserLoginField();
        field.setBrokerID(cnf.getBrokerID());
        field.setUserID(cnf.getAccountID());
        field.setPassword(cnf.getPassword());
        var r = traderApi.ReqUserLogin(field, ++reqId);
        System.out.printf("Send login result:%d\n", r);
    }

    @Override
    public void OnRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin,
                               CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast)
    {
        if (pRspInfo != null && pRspInfo.getErrorID() != 0)
        {
            System.out.printf("Login ErrorID[%d] ErrMsg[%s]\n", pRspInfo.getErrorID(), pRspInfo.getErrorMsg());
            return;
        }

        var rA = new CThostFtdcQryTradingAccountField();
        rA.setBrokerID(cnf.getBrokerID());
        rA.setInvestorID(cnf.getAccountID());
        traderApi.ReqQryTradingAccount(rA, ++reqId);
		/*
		查询资金账户
        System.out.println("Login success!!!");
        CThostFtdcQryTradingAccountField qryTradingAccount = new CThostFtdcQryTradingAccountField();
        qryTradingAccount.setBrokerID(m_BrokerId);
        qryTradingAccount.setCurrencyID(m_CurrencyId);;
        qryTradingAccount.setInvestorID(m_InvestorId);
        m_traderapi.ReqQryTradingAccount(qryTradingAccount, 1);
		*/
		
		/*
		查询结算单
        CThostFtdcQrySettlementInfoField qrysettlement = new CThostFtdcQrySettlementInfoField();
        qrysettlement.setBrokerID(m_BrokerId);
        qrysettlement.setInvestorID(m_InvestorId);
        qrysettlement.setTradingDay(m_TradingDay);
        qrysettlement.setAccountID(m_AccountId);
        qrysettlement.setCurrencyID(m_CurrencyId);
        m_traderapi.ReqQrySettlementInfo(qrysettlement, 2);
		*/

        //确认结算单
        //CThostFtdcSettlementInfoConfirmField confirmfield = new CThostFtdcSettlementInfoConfirmField();
        //TODO
        //traderApi.ReqSettlementInfoConfirm(confirmfield, 0);
    }

    @Override
    public void OnRspQryTradingAccount(CThostFtdcTradingAccountField pTradingAccount, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast)
    {
        if (pRspInfo != null && pRspInfo.getErrorID() != 0)
        {
            System.out.printf("OnRspQryTradingAccount ErrorID[%d] ErrMsg[%s]\n", pRspInfo.getErrorID(), pRspInfo.getErrorMsg());
            return;
        }

        if (pTradingAccount != null)
        {
            System.out.printf("Balance[%f]Available[%f]WithdrawQuota[%f]Credit[%f]\n",
                pTradingAccount.getBalance(), pTradingAccount.getAvailable(), pTradingAccount.getWithdrawQuota(),
                pTradingAccount.getCredit());
        }
        else
        {
            System.out.printf("NULL obj\n");
        }
        placeOrder(demoOrderArg.instrumentID, demoOrderArg.direction, demoOrderArg.offsetFlag, demoOrderArg.volume, demoOrderArg.price);
    }

    @Override
    public void OnRspOrderInsert( CThostFtdcInputOrderField pInputOrder, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast )
    {
        if (pInputOrder == null || (pRspInfo != null && pRspInfo.getErrorID() != 0))
        {
            System.out.printf("OnRspOrderInsert failed ErrorID[%d] ErrMsg[%s]\n", pRspInfo.getErrorID(), pRspInfo.getErrorMsg());
        }
        else
        {
            System.out.printf("OnRspOrderInsert success.\n");
        }
    }

    @Override
    public void OnRtnOrder( CThostFtdcOrderField pOrder )
    {
        System.out.printf("OnRtnOrder\n");
        if(pOrder!= null)
        {
            System.out.printf("OrderStatus[%c]\n",pOrder.getOrderStatus());
            System.out.printf("OrderSysID[%s]\n",pOrder.getOrderSysID());
        }
    }

    @Override
    public void OnRtnTrade( CThostFtdcTradeField pTrade )
    {
        System.out.printf("OnRtnTrade\n");
        if(pTrade!= null)
        {
            System.out.printf("Price[%f]\n",pTrade.getPrice());
            System.out.printf("Volume[%d]\n",pTrade.getVolume());
            System.out.printf("OrderSysID[%s]\n",pTrade.getOrderSysID());
        }
    }

    public void placeOrder(String instrumentID, char direction, char offsetFlag, int volume, double price)
    {
        CThostFtdcInputOrderField orderfield = new CThostFtdcInputOrderField();
        orderfield.setBrokerID(cnf.getBrokerID());
        orderfield.setUserID(cnf.getAccountID());
        orderfield.setInvestorID(cnf.getAccountID());
        orderfield.setInstrumentID(instrumentID);
        orderfield.setDirection(direction);
        orderfield.setCombOffsetFlag(Character.toString(offsetFlag));
        orderfield.setCombHedgeFlag(Character.toString(jctpConstants.THOST_FTDC_HF_Speculation));
        orderfield.setVolumeTotalOriginal(volume);
        orderfield.setTimeCondition(jctpConstants.THOST_FTDC_TC_GFD);
        orderfield.setVolumeCondition(jctpConstants.THOST_FTDC_VC_AV);
        orderfield.setMinVolume(1);
        orderfield.setForceCloseReason(jctpConstants.THOST_FTDC_FCC_NotForceClose);
        orderfield.setUserForceClose(0);
        orderfield.setContingentCondition(jctpConstants.THOST_FTDC_CC_Immediately);
        orderfield.setLimitPrice(price);
        orderfield.setOrderPriceType(jctpConstants.THOST_FTDC_OPT_LimitPrice);
        var r = traderApi.ReqOrderInsert(orderfield, ++reqId);
        System.out.printf("ReqOrderInsert result:%d\n", r);
    }


    private CThostFtdcTraderApi traderApi;
    private Cnf cnf;
    private int reqId;
    private DemoOrderArg demoOrderArg;
}

public class DemoTrade {
    public static DemoOrderArg parseArg(String[] args) {
        var a = new DemoOrderArg();
        a.instrumentID = args[0];
        a.direction = args[1].charAt(0);
        a.offsetFlag = args[2].charAt(0);
        a.volume = Integer.parseInt(args[3]);
        a.price = Double.parseDouble(args[4]);
        return a;
    }

	public static void main(String[] args) {
		var oArg = parseArg(args);
        System.out.printf("order arg:%s\n", oArg.toString());
        var cnf = FJson.readCnf("./cnf.json");
        System.out.println(cnf);
        cnf.refresh();
		//System.out.println(System.getProperty("java.library.path"));
		CThostFtdcTraderApi traderApi = CThostFtdcTraderApi.CreateFtdcTraderApi();
		System.out.printf("CTP API Version [%s]\n",traderApi.GetApiVersion());//输出api版本号
		DemoTradeSpi pTraderSpi = new DemoTradeSpi(traderApi, cnf, oArg);
		traderApi.RegisterSpi(pTraderSpi);
        traderApi.SubscribePrivateTopic(THOST_TE_RESUME_TYPE.THOST_TERT_QUICK);
        traderApi.SubscribePublicTopic(THOST_TE_RESUME_TYPE.THOST_TERT_QUICK);
		traderApi.RegisterFront(cnf.getTradeServer());
		traderApi.Init();
		traderApi.Join();
		return;
	}
}
