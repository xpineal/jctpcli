package org.kcr.jctpcli;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.kr.jctp.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

class TraderReq {
    public TraderReq(int rID) {
        requestID = rID;
    }

    @Override
    public String toString() {
        return "TradeReq{" +
                "requestID=" + requestID +
                ", resultCode=" + resultCode +
                '}';
    }

    public int requestID;
    public int resultCode;
}

class OrderReq {
    public OrderReq(int rID, String orderRef) {
        this.requestID = rID;
        this.orderRef = orderRef;
    }

    @Override
    public String toString() {
        return "OrderReq{" +
                "requestID=" + requestID +
                ", resultCode=" + resultCode +
                ", orderRef='" + orderRef + '\'' +
                '}';
    }

    public int requestID;
    public int resultCode;
    public String orderRef;
}

class OrderCond {
    public char OrderPriceType;
    public char ContingentCondition;
    public char TimeCondition;
    public char VolumeCondition;

    public OrderCond(char priceType, char contCondition, char timeCondition, char volCondition){
        OrderPriceType = priceType;
        ContingentCondition = contCondition;
        TimeCondition = timeCondition;
        VolumeCondition = volCondition;
    }

    //开限价单
    public static void FAKLimitPrice(CThostFtdcInputOrderField input) {
        OrderCond cond;
        switch (input.getExchangeID()){
            case SHFE:
                cond = LimitPriceFAK_SHFE;
                break;
            case CFFEX:
                cond = LimitPriceFAK_CFFEX;
                break;
            case DCE:
                cond = LimitPriceFAK_DCE;
                break;
            case CZCE:
                cond = LimitPriceFAK_CZCE;
                break;
            case INE:
                cond = LimitPriceFAK_INE;
                break;
            default:
                return;
        }
        SetCond(input, cond);
    }

    //开市价单
    public static void FAKAnyPrice(CThostFtdcInputOrderField input) {
        OrderCond cond;
        switch (input.getExchangeID()){
            case CFFEX:
                cond = AnyPriceFAK_CFFEX;
                break;
            case DCE:
                cond = AnyPriceFAK_DCE;
                break;
            case CZCE:
                cond = AnyPriceFAK_CZCE;
                break;
            case INE:
                cond = AnyPriceFAK_INE;
                break;
            default:
                return;
        }
        SetCond(input, cond);
    }

    //设置订单条件
    public static void SetCond(CThostFtdcInputOrderField input, OrderCond cond) {
        input.setOrderPriceType(cond.OrderPriceType);
        input.setContingentCondition(cond.ContingentCondition);
        input.setTimeCondition(cond.TimeCondition);
        input.setVolumeCondition(cond.VolumeCondition);
    }

    //郑商所
    public static final OrderCond LimitPrice_CZCE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_GFD,
            jctpConstants.THOST_FTDC_VC_AV
    );
    public static final OrderCond LimitPriceFAK_CZCE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_IOC,
            jctpConstants.THOST_FTDC_VC_AV
    );
    public static final OrderCond AnyPrice_CZCE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_AnyPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_GFD,
            jctpConstants.THOST_FTDC_VC_AV
    );
    public static final OrderCond AnyPriceFAK_CZCE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_AnyPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_IOC,
            jctpConstants.THOST_FTDC_VC_AV
    );

    //大商所
    public static final OrderCond LimitPrice_DCE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_GFD,
            jctpConstants.THOST_FTDC_VC_AV
    );
    public static final OrderCond LimitPriceFAK_DCE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_IOC,
            jctpConstants.THOST_FTDC_VC_AV
    );
    //忽略FOK
    /*public static final TradeCombine LimitPriceFOK_DCE = new TradeCombine(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_IOC,
            jctpConstants.THOST_FTDC_VC_CV
    );*/
    public static final OrderCond AnyPrice_DCE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_AnyPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_GFD,
            jctpConstants.THOST_FTDC_VC_AV
    );
    public static final OrderCond AnyPriceFAK_DCE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_AnyPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_IOC,
            jctpConstants.THOST_FTDC_VC_AV
    );
    //忽略FOK
    /*public static final TradeCombine AnyPriceFOK_DCE = new TradeCombine(
            jctpConstants.THOST_FTDC_OPT_AnyPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_IOC,
            jctpConstants.THOST_FTDC_VC_CV
    );*/

    //上期所 -- 只支持限价单
    public static final OrderCond LimitPrice_SHFE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_GFD,
            jctpConstants.THOST_FTDC_VC_AV
    );
    public static final OrderCond LimitPriceFAK_SHFE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_IOC,
            jctpConstants.THOST_FTDC_VC_AV
    );
    //忽略FOK
    /*public static final TradeCombine LimitPriceFOK_SHFE = new TradeCombine(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_IOC,
            jctpConstants.THOST_FTDC_VC_CV
    );*/
    //忽略上期所的最小数量报单

    //中金所
    public static final OrderCond LimitPrice_CFFEX = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_GFD,
            jctpConstants.THOST_FTDC_VC_AV
    );
    public static final OrderCond LimitPriceFAK_CFFEX = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_IOC,
            jctpConstants.THOST_FTDC_VC_AV
    );
    //忽略FOK
    /*public static final TradeCombine LimitPriceFOK_CFFEX = new TradeCombine(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_IOC,
            jctpConstants.THOST_FTDC_VC_CV
    );*/
    //忽略中金所的最小数量报单

    //忽略中金所的五档市价报单
    public static final OrderCond AnyPrice_CFFEX = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_BestPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_GFD,
            jctpConstants.THOST_FTDC_VC_AV
    );
    public static final OrderCond AnyPriceFAK_CFFEX = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_BestPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_IOC,
            jctpConstants.THOST_FTDC_VC_AV
    );

    //能源所 -- 没有具体文档，使用与郑商所同样配置
    public static final OrderCond LimitPrice_INE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_GFD,
            jctpConstants.THOST_FTDC_VC_AV
    );
    public static final OrderCond LimitPriceFAK_INE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_IOC,
            jctpConstants.THOST_FTDC_VC_AV
    );
    public static final OrderCond AnyPrice_INE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_AnyPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_GFD,
            jctpConstants.THOST_FTDC_VC_AV
    );
    public static final OrderCond AnyPriceFAK_INE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_AnyPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_IOC,
            jctpConstants.THOST_FTDC_VC_AV
    );

    //exchange id
    public static final String SHFE = "SHFE";
    public static final String CFFEX = "CFFEX";
    public static final String DCE = "DCE";
    public static final String CZCE = "CZCE";
    public static final String INE = "INE";
}

public class TraderCall {
    //构造函数
    public TraderCall(CThostFtdcTraderApi traderApi,
                      String brokerID, String investorID, String password, String appID, String authCode)
    {
        this.traderApi =  traderApi;
        this.brokerID = brokerID;
        this.investorID = investorID;
        this.password = password;
        this.appID = appID;
        this.authCode = authCode;

        this.reqIDAtom = new AtomicInteger(0);
        this.frontIDAtom = new AtomicInteger();
        this.sessionIDAtom = new AtomicInteger();
        this.orderRefAtom = new AtomicLong();
    }

    //设置front/session/previous max order ref
    public void setAtom(int frontID, int sessionID, long orderRef) {
        frontIDAtom.set(frontID);
        sessionIDAtom.set(sessionID);
        orderRefAtom.set(orderRef);
    }

    //生成限价开买单(做多)报单的对象
    public CThostFtdcInputOrderField genLimitPriceOpenBuyOrder(
            String exchangeID, String instrumentID, double price, int vol) {
        var order = genOrder(exchangeID, instrumentID, Open, vol, true);
        order.setLimitPrice(price);
        OrderCond.FAKLimitPrice(order);
        return order;
    }

    //生成市价开买单(做多)报单的对象
    public CThostFtdcInputOrderField genAnyPriceOpenBuyOrder(String exchangeID, String instrumentID, int vol) {
        var order = genOrder(exchangeID, instrumentID, Open, vol, true);
        OrderCond.FAKAnyPrice(order);
        return order;
    }

    //生成限价开卖单(做空)报单的对象
    public CThostFtdcInputOrderField genLimitPriceOpenSellOrder(
            String exchangeID, String instrumentID, double price, int vol) {
        var order = genOrder(exchangeID, instrumentID, Open, vol, false);
        order.setLimitPrice(price);
        OrderCond.FAKLimitPrice(order);
        return order;
    }

    //生成市价开卖单(做空)报单的对象
    public CThostFtdcInputOrderField genAnyPriceOpenSellOrder(
            String exchangeID, String instrumentID, int vol) {
        var order = genOrder(exchangeID, instrumentID, Open, vol, false);
        OrderCond.FAKAnyPrice(order);
        return order;
    }

    //生成订单的对象
    public CThostFtdcInputOrderField genOrder(
            String exchangeID, String instrumentID, String combOffsetFlag, int vol, boolean buy) {
        var order = new CThostFtdcInputOrderField();
        var orderRef = Long.toString(orderRefAtom.incrementAndGet());
        order.setBrokerID(brokerID);
        order.setInvestorID(investorID);
        order.setInstrumentID(instrumentID);
        order.setOrderRef(orderRef);
        //暂时只使用投机的标记
        order.setCombHedgeFlag(HFSpeculation);
        order.setVolumeTotalOriginal(vol);
        order.setExchangeID(exchangeID);
        if (buy) {
            order.setDirection(jctpConstants.THOST_FTDC_D_Buy);
        }else{
            order.setDirection(jctpConstants.THOST_FTDC_D_Sell);
        }
        order.setCombOffsetFlag(combOffsetFlag);
        return order;
    }

    //生成撤单的对象
    public CThostFtdcInputOrderActionField genCancelOrder(
            String orderRef, int frontID, int sessionID) {
        var action = new CThostFtdcInputOrderActionField();
        action.setOrderRef(orderRef);
        action.setFrontID(frontID);
        action.setSessionID(sessionID);
        action.setActionFlag(jctpConstants.THOST_FTDC_AF_Delete);
        return action;
    }

    //生成撤单的对象
    public CThostFtdcInputOrderActionField genCancelOrder(
            String exchangeID, String orderSysID, String instrumentID) {
        var action = new CThostFtdcInputOrderActionField();
        action.setExchangeID(exchangeID);
        action.setOrderSysID(orderSysID);
        action.setInstrumentID(instrumentID);
        action.setActionFlag(jctpConstants.THOST_FTDC_AF_Delete);
        return action;
    }

    //生成查询报单信息的对象
    public CThostFtdcQryOrderField genQryOrder(
            String exchangeID, String instrument, String orderSysID, String startTime, String endTime) {
        var qry = new CThostFtdcQryOrderField();
        qry.setBrokerID(brokerID);
        qry.setInvestorID(investorID);
        qry.setExchangeID(exchangeID);
        qry.setInstrumentID(instrument);
        qry.setOrderSysID(orderSysID);
        qry.setInsertTimeStart(startTime);
        qry.setInsertTimeEnd(endTime);
        return qry;
    }

    //生成查询报单信息的对象
    public CThostFtdcQryOrderField genQryOrder(String instrument, String exchangeID, String orderSysID) {
        return genQryOrder(instrument, exchangeID, orderSysID, "", "");
    }

    //生成查询报单信息的对象
    public CThostFtdcQryOrderField genQryOrder(
            String instrument, String exchangeID, String startTime, String endTime) {
        return genQryOrder(instrument, exchangeID, "", startTime, endTime);
    }

    //生成查询报单信息的对象
    public CThostFtdcQryOrderField genQryOrder(String startTime, String endTime) {
        return genQryOrder("", "", "", startTime, endTime);
    }

    //生成查询成交信息的对象
    public CThostFtdcQryTradeField genQryTrade(
            String instrument, String exchangeID, String tradeID, String startTime, String endTime) {
        var qry = new CThostFtdcQryTradeField();
        qry.setBrokerID(brokerID);
        qry.setInvestorID(investorID);
        qry.setInstrumentID(instrument);
        qry.setExchangeID(exchangeID);
        qry.setTradeID(tradeID);
        qry.setTradeTimeStart(startTime);
        qry.setTradeTimeEnd(endTime);
        return qry;
    }

    //生成查询成交信息的对象
    public CThostFtdcQryTradeField genQryTrade(String instrument, String exchangeID, String tradeID) {
        return genQryTrade(instrument, exchangeID, tradeID, "", "");
    }

    //生成查询成交信息的对象
    public CThostFtdcQryTradeField genQryTrade(String instrument, String exchangeID, String startTime, String endTime) {
        return genQryTrade(instrument, exchangeID, "", startTime, endTime);
    }

    //生成查询成交信息的对象
    public CThostFtdcQryTradeField genQryTrade(String startTime, String endTime) {
        return genQryTrade("", "", "", startTime, endTime);
    }

    //生成查询投资者持仓的对象
    public CThostFtdcQryInvestorPositionField genQryInvestorPosition(String instrumentID) {
        var qry = new CThostFtdcQryInvestorPositionField();
        qry.setBrokerID(brokerID);
        qry.setInvestorID(investorID);
        qry.setInstrumentID(instrumentID);
        return qry;
    }

    //生成查询资金账户的对象
    public CThostFtdcQryTradingAccountField genQryTradeAccount(String currencyID){
        var qry = new CThostFtdcQryTradingAccountField();
        qry.setBrokerID(brokerID);
        qry.setInvestorID(investorID);
        qry.setCurrencyID(currencyID);
        return qry;
    }

    //生成查询投资者的对象
    public CThostFtdcQryInvestorField genQryInvestor() {
        var qry = new CThostFtdcQryInvestorField();
        qry.setBrokerID(brokerID);
        qry.setInvestorID(investorID);
        return qry;
    }

    //生成查询交易编码的对象
    public CThostFtdcQryTradingCodeField genQryTradingCode(String exchangeID, String clientID, char clientIDType) {
        var qry = new CThostFtdcQryTradingCodeField();
        qry.setBrokerID(brokerID);
        qry.setInvestorID(investorID);
        qry.setExchangeID(exchangeID);
        qry.setClientID(clientID);
        qry.setClientIDType(clientIDType);
        return qry;
    }

    //生成查询合约保证金率的对象
    public CThostFtdcQryInstrumentMarginRateField genQryInstrumentMarginRate(String instrumentID, char hedgeFlag) {
        var qry = new CThostFtdcQryInstrumentMarginRateField();
        qry.setBrokerID(brokerID);
        qry.setInvestorID(investorID);
        qry.setInstrumentID(instrumentID);
        qry.setHedgeFlag(hedgeFlag);
        return qry;
    }

    //生成查询合约手续费的对象
    public CThostFtdcQryInstrumentCommissionRateField genQryInstrumentCommissionRate(String instrumentID) {
        var qry = new CThostFtdcQryInstrumentCommissionRateField();
        qry.setBrokerID(brokerID);
        qry.setInvestorID(investorID);
        qry.setInstrumentID(instrumentID);
        return qry;
    }

    //生成查询交易所的对象
    public CThostFtdcQryExchangeField genQryExchange(String exchangeID) {
        var qry = new CThostFtdcQryExchangeField();
        qry.setExchangeID(exchangeID);
        return qry;
    }

    //生成查询合约的对象
    public CThostFtdcQryInstrumentField genQryInstrument(
            String instrumentID, String exchangeID, String instrIDInExchange, String productID) {
        var qry = new CThostFtdcQryInstrumentField();
        qry.setInstrumentID(instrumentID);
        qry.setExchangeID(exchangeID);
        qry.setExchangeInstID(instrIDInExchange);
        qry.setProductID(productID);
        return qry;
    }

    //生成查询合约的对象
    public CThostFtdcQryInstrumentField genQryInstrument(String instrumentID, String exchangeID) {
        return genQryInstrument(instrumentID, exchangeID, "", "");
    }

    //生成查询合约的对象
    public CThostFtdcQryInstrumentField genQryInstrument(String instrumentID) {
        return genQryInstrument(instrumentID, "", "", "");
    }

    //生成查询行情的对象
    public CThostFtdcQryDepthMarketDataField genQryDepthMarketData(String instrumentID) {
        var qry = new CThostFtdcQryDepthMarketDataField();
        qry.setInstrumentID(instrumentID);
        return qry;
    }

    //生成查询合约状态的对象
    public CThostFtdcQryInstrumentStatusField genQryInstrumentStatus(
            String exchangeID, String instrIDInExchange) {
        var qry = new CThostFtdcQryInstrumentStatusField();
        qry.setExchangeID(exchangeID);
        qry.setExchangeInstID(instrIDInExchange);
        return qry;
    }

    //生成查询合约状态的对象
    public CThostFtdcQryInstrumentStatusField genQryInstrumentStatus(String exchangeID) {
        var qry = new CThostFtdcQryInstrumentStatusField();
        qry.setExchangeID(exchangeID);
        qry.setExchangeInstID("");
        return qry;
    }

    //生成查询持仓明细的对象
    public CThostFtdcQryInvestorPositionDetailField genQryInvestorPositionDetail(String instrumentID) {
        var qry = new CThostFtdcQryInvestorPositionDetailField();
        qry.setBrokerID(brokerID);
        qry.setInvestorID(investorID);
        qry.setInstrumentID(instrumentID);
        return qry;
    }

    //生成查询持仓明细的对象
    public CThostFtdcQryInvestorPositionDetailField genQryInvestorPositionDetail() {
        var qry = new CThostFtdcQryInvestorPositionDetailField();
        qry.setBrokerID(brokerID);
        qry.setInvestorID(investorID);
        qry.setInstrumentID("");
        return qry;
    }

    //生成查询交易所保证金率的对象
    public CThostFtdcQryExchangeMarginRateField genQryExchangeMarginRate(String instrumentID, char hedgeFlag) {
        var qry = new CThostFtdcQryExchangeMarginRateField();
        qry.setBrokerID(brokerID);
        qry.setInstrumentID(instrumentID);
        qry.setHedgeFlag(hedgeFlag);
        return qry;
    }

    //生成查询交易所调整保证金率的对象
    public CThostFtdcQryExchangeMarginRateAdjustField genQryExchangeMarginAdjust(String instrumentID, char hedgeFlag) {
        var qry = new CThostFtdcQryExchangeMarginRateAdjustField();
        qry.setBrokerID(brokerID);
        qry.setInstrumentID(instrumentID);
        qry.setHedgeFlag(hedgeFlag);
        return qry;
    }

    //生成查询经纪公司报单手续费的对象
    public CThostFtdcQryInstrumentOrderCommRateField genQryInstrumentOrderCommRate(String instrumentID, char hedgeFlag) {
        var qry = new CThostFtdcQryInstrumentOrderCommRateField();
        qry.setBrokerID(brokerID);
        qry.setInvestorID(investorID);
        qry.setInstrumentID(instrumentID);
        qry.setHedgeFlag(hedgeFlag);
        return qry;
    }

    //生成查询单腿汇总的对象
    public CThostFtdcQryInvestorPositionForCombField genQryInvestorPositionForComb(
            String exchangeID, String legInstrumentID) {
        var qry = new CThostFtdcQryInvestorPositionForCombField();
        qry.setBrokerID(brokerID);
        qry.setInvestorID(investorID);
        qry.setExchangeID(exchangeID);
        qry.setLegInstrumentID(legInstrumentID);
        return qry;
    }

    //生成查询单腿汇总的对象
    public CThostFtdcQryInvestorPositionForCombField genQryInvestorPositionForComb() {
        var qry = new CThostFtdcQryInvestorPositionForCombField();
        qry.setBrokerID(brokerID);
        qry.setInvestorID(investorID);
        qry.setExchangeID("");
        qry.setLegInstrumentID("");
        return qry;
    }

    //认证
    public TraderReq authenticate() {
        var field = new CThostFtdcReqAuthenticateField();
        field.setBrokerID(brokerID);
        //field.setUserID(cnf.getAccountID());
        field.setAuthCode(authCode);
        field.setAppID(appID);
        var r = genReq();
        r.resultCode = traderApi.ReqAuthenticate(field, r.requestID);
        return r;
    }

    //登录
    public TraderReq login() {
        var field = new CThostFtdcReqUserLoginField();
        field.setBrokerID(brokerID);
        field.setUserID(investorID);
        field.setPassword(password);
        var r = genReq();
        r.resultCode = traderApi.ReqUserLogin(field, r.requestID);
        return r;
    }

    //报单
    public OrderReq addOrder(@NotNull CThostFtdcInputOrderField order) {
        var r = new OrderReq(reqIDAtom.getAndIncrement(), order.getOrderRef());
        r.resultCode = traderApi.ReqOrderInsert(order, r.requestID);
        return r;
    }

    //查询报单信息
    public TraderReq queryOrder(CThostFtdcQryOrderField qry) {
        var r = genReq();
        r.resultCode = traderApi.ReqQryOrder(qry, r.requestID);
        return r;
    }

    //查询成交信息
    public TraderReq queryTrade(CThostFtdcQryTradeField qry) {
        var r = genReq();
        r.resultCode = traderApi.ReqQryTrade(qry, r.requestID);
        return r;
    }

    //查询投资者持仓
    public TraderReq queryInvestorPosition(CThostFtdcQryInvestorPositionField qry) {
        var r = genReq();
        r.resultCode = traderApi.ReqQryInvestorPosition(qry, r.requestID);
        return r;
    }

    //查询投资者持仓
    public TraderReq queryInvestorPosition(String instrumentID) {
        var qry = genQryInvestorPosition(instrumentID);
        var r = genReq();
        r.resultCode = traderApi.ReqQryInvestorPosition(qry, r.requestID);
        return r;
    }

    //查询资金账户
    public TraderReq queryTradeAccount(CThostFtdcQryTradingAccountField qry) {
        var r = genReq();
        r.resultCode = traderApi.ReqQryTradingAccount(qry, r.requestID);
        return r;
    }

    //查询资金账户
    public TraderReq queryTradeAccount(String currencyID) {
        var qry = genQryTradeAccount(currencyID);
        var r = genReq();
        r.resultCode = traderApi.ReqQryTradingAccount(qry, r.requestID);
        return r;
    }

    //查询投资者
    public TraderReq queryInvestor(CThostFtdcQryInvestorField qry) {
        var r = genReq();
        r.resultCode = traderApi.ReqQryInvestor(qry, r.requestID);
        return r;
    }

    //查询投资者
    public TraderReq queryInvestor() {
        var qry = genQryInvestor();
        var r = genReq();
        r.resultCode = traderApi.ReqQryInvestor(qry, r.requestID);
        return r;
    }

    //查询交易编码
    public TraderReq queryTradingCode(CThostFtdcQryTradingCodeField qry) {
        var r = genReq();
        r.resultCode = traderApi.ReqQryTradingCode(qry, r.requestID);
        return r;
    }

    //查询交易编码
    public TraderReq queryTradingCode(String exchangeID, String clientID, char clientIDType) {
        var qry = genQryTradingCode(exchangeID, clientID, clientIDType);
        var r = genReq();
        r.resultCode = traderApi.ReqQryTradingCode(qry, r.requestID);
        return r;
    }

    //查询合约保证金率
    public TraderReq queryInstrumentMarginRate(CThostFtdcQryInstrumentMarginRateField qry) {
        var r = genReq();
        r.resultCode = traderApi.ReqQryInstrumentMarginRate(qry, r.requestID);
        return r;
    }

    //查询合约保证金率
    public TraderReq queryInstrumentMarginRate(String instrumentID, char hedgeFlag) {
        var qry = genQryInstrumentMarginRate(instrumentID, hedgeFlag);
        var r = genReq();
        r.resultCode = traderApi.ReqQryInstrumentMarginRate(qry, r.requestID);
        return r;
    }

    //查询合约手续费
    public TraderReq queryInstrumentCommissionRate(CThostFtdcQryInstrumentCommissionRateField qry) {
        var r = genReq();
        r.resultCode = traderApi.ReqQryInstrumentCommissionRate(qry, r.requestID);
        return r;
    }

    //查询合约手续费
    public TraderReq queryInstrumentCommissionRate(String instrumentID) {
        var qry = genQryInstrumentCommissionRate(instrumentID);
        var r = genReq();
        r.resultCode = traderApi.ReqQryInstrumentCommissionRate(qry, r.requestID);
        return r;
    }

    //查询交易所
    public TraderReq queryExchange(CThostFtdcQryExchangeField qry) {
        var r = genReq();
        r.resultCode = traderApi.ReqQryExchange(qry, r.requestID);
        return r;
    }

    //查询交易所
    public TraderReq queryExchange(String exchangeID) {
        var qry = genQryExchange(exchangeID);
        var r = genReq();
        r.resultCode = traderApi.ReqQryExchange(qry, r.requestID);
        return r;
    }

    //查询合约
    public TraderReq queryInstrument(CThostFtdcQryInstrumentField qry) {
        var r = genReq();
        r.resultCode = traderApi.ReqQryInstrument(qry, r.requestID);
        return r;
    }

    //查询行情
    public TraderReq queryDepthMarketData(CThostFtdcQryDepthMarketDataField qry) {
        var r = genReq();
        r.resultCode = traderApi.ReqQryDepthMarketData(qry, r.requestID);
        return r;
    }

    //查询行情
    public TraderReq queryDepthMarketData(String instrumentID) {
        var qry = genQryDepthMarketData(instrumentID);
        var r = genReq();
        r.resultCode = traderApi.ReqQryDepthMarketData(qry, r.requestID);
        return r;
    }

    //查询合约状态
    public TraderReq queryInstrumentStatus(CThostFtdcQryInstrumentStatusField qry) {
        var r = genReq();
        r.resultCode = traderApi.ReqQryInstrumentStatus(qry, r.requestID);
        return r;
    }

    //查询合约状态
    public TraderReq queryInstrumentStatus(String exchangeID, String instrIDInExchange) {
        var qry = genQryInstrumentStatus(exchangeID, instrIDInExchange);
        var r = genReq();
        r.resultCode = traderApi.ReqQryInstrumentStatus(qry, r.requestID);
        return r;
    }

    //查询合约状态
    public TraderReq queryInstrumentStatus(String exchangeID) {
        var qry = genQryInstrumentStatus(exchangeID);
        var r = genReq();
        r.resultCode = traderApi.ReqQryInstrumentStatus(qry, r.requestID);
        return r;
    }

    //查询持仓明细
    public TraderReq queryInvestorPositionDetail(CThostFtdcQryInvestorPositionDetailField qry) {
        var r = genReq();
        r.resultCode = traderApi.ReqQryInvestorPositionDetail(qry, r.requestID);
        return r;
    }

    //查询持仓明细
    public TraderReq queryInvestorPositionDetail(String instrumentID) {
        var qry = genQryInvestorPositionDetail(instrumentID);
        var r = genReq();
        r.resultCode = traderApi.ReqQryInvestorPositionDetail(qry, r.requestID);
        return r;
    }

    //查询持仓明细
    public TraderReq queryInvestorPositionDetail(int rID) {
        var qry = genQryInvestorPositionDetail();
        var r = genReq();
        r.resultCode = traderApi.ReqQryInvestorPositionDetail(qry, r.requestID);
        return r;
    }

    //查询交易所保证金率
    public TraderReq queryExchangeMarginRate(CThostFtdcQryExchangeMarginRateField qry) {
        var r = genReq();
        r.resultCode = traderApi.ReqQryExchangeMarginRate(qry, r.requestID);
        return r;
    }

    //查询交易所保证金率
    public TraderReq queryExchangeMarginRate(String instrumentID, char hedgeFlag) {
        var qry = genQryExchangeMarginRate(instrumentID, hedgeFlag);
        var r = genReq();
        r.resultCode = traderApi.ReqQryExchangeMarginRate(qry, r.requestID);
        return r;
    }

    //查询交易所调整保证金率
    public TraderReq queryExchangeMarginRateAdjust(CThostFtdcQryExchangeMarginRateAdjustField qry) {
        var r = genReq();
        r.resultCode = traderApi.ReqQryExchangeMarginRateAdjust(qry, r.requestID);
        return r;
    }

    //查询交易所调整保证金率
    public TraderReq queryExchangeMarginRateAdjust(String instrumentID, char hedgeFlag) {
        var qry = genQryExchangeMarginAdjust(instrumentID, hedgeFlag);
        var r = genReq();
        r.resultCode = traderApi.ReqQryExchangeMarginRateAdjust(qry, r.requestID);
        return r;
    }

    //查询经纪公司报单手续费
    public TraderReq queryInstrumentOrderCommRate(CThostFtdcQryInstrumentOrderCommRateField qry) {
        var r = genReq();
        r.resultCode = traderApi.ReqQryInstrumentOrderCommRate(qry, r.requestID);
        return r;
    }

    //查询经纪公司报单手续费
    public TraderReq queryInstrumentOrderCommRate(String instrumentID, char hedgeFlag) {
        var qry = genQryInstrumentOrderCommRate(instrumentID, hedgeFlag);
        var r = genReq();
        r.resultCode = traderApi.ReqQryInstrumentOrderCommRate(qry, r.requestID);
        return r;
    }

    //查询单腿汇总
    public TraderReq queryInvestorPositionForComb(CThostFtdcQryInvestorPositionForCombField qry) {
        var r = genReq();
        r.resultCode = traderApi.ReqQryInvestorPositionForComb(qry, r.requestID);
        return r;
    }

    //查询单腿汇总
    public TraderReq queryInvestorPositionForComb(String exchangeID, String legInstrumentID) {
        var qry = genQryInvestorPositionForComb(exchangeID, legInstrumentID);
        var r = genReq();
        r.resultCode = traderApi.ReqQryInvestorPositionForComb(qry, r.requestID);
        return r;
    }

    //查询单腿汇总
    public TraderReq queryInvestorPositionForComb() {
        var qry = genQryInvestorPositionForComb();
        var r = genReq();
        r.resultCode = traderApi.ReqQryInvestorPositionForComb(qry, r.requestID);
        return r;
    }

    //生成request
    @Contract(" -> new")
    @NotNull
    private TraderReq genReq() {
        return new TraderReq(reqIDAtom.getAndIncrement());
    }

    //flag
    //开平标记
    private static final String Open = Character.toString(jctpConstants.THOST_FTDC_OF_Open);
    private static final String Close = Character.toString(jctpConstants.THOST_FTDC_OF_Close);
    private static final String CloseToday = Character.toString(jctpConstants.THOST_FTDC_OF_CloseToday);
    private static final String CloseYesterday = Character.toString(jctpConstants.THOST_FTDC_OF_CloseYesterday);
    //投机标记 -- 暂时只用投机标记
    private static final String HFSpeculation = Character.toString(jctpConstants.THOST_FTDC_HF_Speculation);

    private CThostFtdcTraderApi traderApi;
    private String brokerID;
    private String investorID;
    private String password;
    private String appID;
    private String authCode;

    private AtomicInteger reqIDAtom;
    private AtomicInteger frontIDAtom;
    private AtomicInteger sessionIDAtom;
    private AtomicLong orderRefAtom;
}
