package org.kcr.jctpcli;

import org.kcr.jctpcli.*;
import org.kr.jctp.*;

import static org.junit.jupiter.api.Assertions.*;

class OutputTest {

    @org.junit.jupiter.api.Test
    void pRequest() {
        Output.pRequest("", new TraderReq(0));
    }

    @org.junit.jupiter.api.Test
    void pResponse() {
        Output.pResponse("", new CThostFtdcRspInfoField(), 0, true);
    }

    @org.junit.jupiter.api.Test
    void pRspAuth() {
        Output.pRspAuth("", new CThostFtdcRspAuthenticateField());
    }

    @org.junit.jupiter.api.Test
    void pInputOrder() {
        Output.pInputOrder("", new CThostFtdcInputOrderField());
    }

    @org.junit.jupiter.api.Test
    void pOrder() {
        Output.pOrder("", new CThostFtdcOrderField());
    }

    @org.junit.jupiter.api.Test
    void pTrade() {
        Output.pTrade("", new CThostFtdcTradeField());
    }

    @org.junit.jupiter.api.Test
    void pInvestorPosition() {
        Output.pInvestorPosition("", new CThostFtdcInvestorPositionField());
    }

    @org.junit.jupiter.api.Test
    void pTradingAccount() {
        Output.pTradingAccount("", new CThostFtdcTradingAccountField());
    }

    @org.junit.jupiter.api.Test
    void pInvestor() {
        Output.pInvestor("", new CThostFtdcInvestorField());
    }

    @org.junit.jupiter.api.Test
    void pTradingCode() {
        Output.pTradingCode("", new CThostFtdcTradingCodeField());
    }

    @org.junit.jupiter.api.Test
    void pInstrumentMarginRate() {
        Output.pInstrumentMarginRate("", new CThostFtdcInstrumentMarginRateField());
    }

    @org.junit.jupiter.api.Test
    void pInstrumentCommissionRate() {
        Output.pInstrumentCommissionRate("", new CThostFtdcInstrumentCommissionRateField());
    }

    @org.junit.jupiter.api.Test
    void pInstrumentCommRate() {
        Output.pInstrumentCommRate("", new CThostFtdcInstrumentOrderCommRateField());
    }

    @org.junit.jupiter.api.Test
    void pExchange() {
        Output.pExchange("", new CThostFtdcExchangeField());
    }

    @org.junit.jupiter.api.Test
    void pInstrument() {
        Output.pInstrument("", new CThostFtdcInstrumentField());
    }

    @org.junit.jupiter.api.Test
    void pInstrumentStatus() {
        Output.pInstrumentStatus("", new CThostFtdcInstrumentStatusField());
    }

    @org.junit.jupiter.api.Test
    void pInvestorPositionDetail() {
        Output.pInvestorPositionDetail("", new CThostFtdcInvestorPositionDetailField());
    }

    @org.junit.jupiter.api.Test
    void pExchangeMarginRate() {
        Output.pExchangeMarginRate("", new CThostFtdcExchangeMarginRateField());
    }

    @org.junit.jupiter.api.Test
    void pExchangeMarginRateAdjust() {
        Output.pExchangeMarginRateAdjust("", new CThostFtdcExchangeMarginRateAdjustField());
    }

    @org.junit.jupiter.api.Test
    void pLoginInfo() {
        Output.pLoginInfo("", new CThostFtdcRspUserLoginField());
    }

    @org.junit.jupiter.api.Test
    void pSpecificInstrument() {
        Output.pSpecificInstrument("", new CThostFtdcSpecificInstrumentField());
    }

    @org.junit.jupiter.api.Test
    void pDepthMarketData() {
        Output.pDepthMarketData("", new CThostFtdcDepthMarketDataField());
    }

    @org.junit.jupiter.api.Test
    void pInputOrderAction() {
        Output.pInputOrderAction("", new CThostFtdcInputOrderActionField());
    }

    @org.junit.jupiter.api.Test
    void pOrderAction() {
        Output.pOrderAction("", new CThostFtdcOrderActionField());
    }

    @org.junit.jupiter.api.Test
    void pInvestorPositionForComb() {
        Output.pInvestorPositionForComb("", new CThostFtdcInvestorPositionForCombField());
    }
}
