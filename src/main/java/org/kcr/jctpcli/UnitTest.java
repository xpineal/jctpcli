package org.kcr.jctpcli;

import org.kr.jctp.*;

public class UnitTest {

    public static void TestOutput() {
        Output.pRequest("", new TraderReq(0));
        Output.pResponse("", new CThostFtdcRspInfoField(), 0, true);
        Output.pRspAuth("", new CThostFtdcRspAuthenticateField());
        Output.pInputOrder("", new CThostFtdcInputOrderField());
        Output.pOrder("", new CThostFtdcOrderField());
        Output.pTrade("", new CThostFtdcTradeField());
        Output.pInvestorPosition("", new CThostFtdcInvestorPositionField());
        Output.pTradingAccount("", new CThostFtdcTradingAccountField());
        Output.pInvestor("", new CThostFtdcInvestorField());
        Output.pTradingCode("", new CThostFtdcTradingCodeField());
        Output.pInstrumentMarginRate("", new CThostFtdcInstrumentMarginRateField());
        Output.pInstrumentCommissionRate("", new CThostFtdcInstrumentCommissionRateField());
        Output.pInstrumentCommRate("", new CThostFtdcInstrumentOrderCommRateField());
        Output.pExchange("", new CThostFtdcExchangeField());
        Output.pInstrument("", new CThostFtdcInstrumentField());
        Output.pInstrumentStatus("", new CThostFtdcInstrumentStatusField());
        Output.pInvestorPositionDetail("", new CThostFtdcInvestorPositionDetailField());
        Output.pExchangeMarginRate("", new CThostFtdcExchangeMarginRateField());
        Output.pExchangeMarginRateAdjust("", new CThostFtdcExchangeMarginRateAdjustField());
        Output.pLoginInfo("", new CThostFtdcRspUserLoginField());
        Output.pSpecificInstrument("", new CThostFtdcSpecificInstrumentField());
        Output.pDepthMarketData("", new CThostFtdcDepthMarketDataField());
        Output.pInputOrderAction("", new CThostFtdcInputOrderActionField());
        Output.pOrderAction("", new CThostFtdcOrderActionField());
        Output.pInvestorPositionForComb("", new CThostFtdcInvestorPositionForCombField());
    }

    public static void main(String[] args) throws InterruptedException {
        TestOutput();
    }
}
