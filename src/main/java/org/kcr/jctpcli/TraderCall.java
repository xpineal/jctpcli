package org.kcr.jctpcli;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.kr.jctp.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TraderCall {
    //flag
    //开平标记
    private static final String Open = Character.toString(jctpConstants.THOST_FTDC_OF_Open);
    private static final String Close = Character.toString(jctpConstants.THOST_FTDC_OF_Close);
    //投机标记 -- 暂时只用投机标记
    private static final String HFSpeculation = Character.toString(jctpConstants.THOST_FTDC_HF_Speculation);

    private final CThostFtdcTraderApi traderApi;
    private final String brokerID;
    private final String investorID;
    private final String password;
    private final String appID;
    private final String authCode;

    private final AtomicInteger reqIDAtom;
    private final AtomicInteger frontIDAtom;
    private final AtomicInteger sessionIDAtom;
    private final AtomicLong orderRefAtom;

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

    //生成限价开卖单(做空)报单的对象
    public CThostFtdcInputOrderField genLimitPriceOpenSellOrder(
            String exchangeID, String instrumentID, double price, int vol) {
        var order = genOrder(exchangeID, instrumentID, Open, vol, false);
        order.setLimitPrice(price);
        OrderCond.FAKLimitPrice(order);
        return order;
    }

    //生成限价平买单(做多)报单的对象
    public CThostFtdcInputOrderField genLimitPriceCloseBuyOrder(
            String exchangeID, String instrumentID, double price, int vol) {
        var order = genOrder(exchangeID, instrumentID, Close, vol, false);
        order.setLimitPrice(price);
        OrderCond.FAKLimitPrice(order);
        return order;
    }

    //生成限价平买单(做多)报单的对象
    public CThostFtdcInputOrderField genLimitPriceCloseBuyOrder(
            String exchangeID, String instrumentID, double price, int vol, String closeFlag) {
        var order = genOrder(exchangeID, instrumentID, closeFlag, vol, false);
        order.setLimitPrice(price);
        OrderCond.FAKLimitPrice(order);
        return order;
    }

    //生成限价平卖单(做空)报单的对象
    public CThostFtdcInputOrderField genLimitPriceCloseSellOrder(
            String exchangeID, String instrumentID, double price, int vol) {
        var order = genOrder(exchangeID, instrumentID, Close, vol, true);
        order.setLimitPrice(price);
        OrderCond.FAKLimitPrice(order);
        return order;
    }

    //生成限价平卖单(做空)报单的对象
    public CThostFtdcInputOrderField genLimitPriceCloseSellOrder(
            String exchangeID, String instrumentID, double price, int vol, String closeFlag) {
        var order = genOrder(exchangeID, instrumentID, closeFlag, vol, true);
        order.setLimitPrice(price);
        OrderCond.FAKLimitPrice(order);
        return order;
    }

    //生成当天有效限价开买单(做多)报单的对象
    public CThostFtdcInputOrderField genGFDLimitPriceOpenBuyOrder(
            String exchangeID, String instrumentID, double price, int vol) {
        var order = genOrder(exchangeID, instrumentID, Open, vol, true);
        order.setLimitPrice(price);
        OrderCond.GFDLimitPrice(order);
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
    public CThostFtdcQryInstrumentField genQryInstrument(String instrumentID) {
        return genQryInstrument(instrumentID, "", "", "");
    }

    //生成查询行情的对象
    public CThostFtdcQryDepthMarketDataField genQryDepthMarketData(String instrumentID) {
        var qry = new CThostFtdcQryDepthMarketDataField();
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

//    新版本不再支持
//    //生成查询单腿汇总的对象
//    public CThostFtdcQryInvestorPositionForCombField genQryInvestorPositionForComb() {
//        var qry = new CThostFtdcQryInvestorPositionForCombField();
//        qry.setBrokerID(brokerID);
//        qry.setInvestorID(investorID);
//        qry.setExchangeID("");
//        qry.setLegInstrumentID("");
//        return qry;
//    }

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

    //限价开买单(做多)
    public OrderReq addLimitPriceOpenBuyOrder(
            String exchangeID, String instrumentID, double price, int vol) {
        var order = genLimitPriceOpenBuyOrder(exchangeID, instrumentID, price, vol);
        return addOrder(order);
    }

    //限价开卖单(做空)
    public OrderReq addLimitPriceOpenSellOrder(
            String exchangeID, String instrumentID, double price, int vol) {
        var order = genLimitPriceOpenSellOrder(exchangeID, instrumentID, price, vol);
        return addOrder(order);
    }

    //限价平做多买单
    public OrderReq addLimitPriceCloseBuyOrder(
            String exchangeID, String instrumentID, double price, int vol) {
        var order = genLimitPriceCloseBuyOrder(exchangeID, instrumentID, price, vol);
        return addOrder(order);
    }

    //限价平做多买单
    public OrderReq addLimitPriceCloseBuyOrder(
            String exchangeID, String instrumentID, double price, int vol, String closeFlag) {
        var order = genLimitPriceCloseBuyOrder(exchangeID, instrumentID, price, vol, closeFlag);
        return addOrder(order);
    }

    //限价平做空卖单
    public OrderReq addLimitPriceCloseSellOrder(
            String exchangeID, String instrumentID, double price, int vol) {
        var order = genLimitPriceCloseSellOrder(exchangeID, instrumentID, price, vol);
        return addOrder(order);
    }

    //限价平做空卖单
    public OrderReq addLimitPriceCloseSellOrder(
            String exchangeID, String instrumentID, double price, int vol, String closeFlag) {
        var order = genLimitPriceCloseSellOrder(exchangeID, instrumentID, price, vol, closeFlag);
        return addOrder(order);
    }

    //当天限价开买单(做多)
    public OrderReq addGFDLimitPriceOpenBuyOrder(
            String exchangeID, String instrumentID, double price, int vol) {
        var order = genGFDLimitPriceOpenBuyOrder(exchangeID, instrumentID, price, vol);
        return addOrder(order);
    }

    //撤单
    public OrderReq cancelOrder(@NotNull CThostFtdcInputOrderActionField order) {
        var r = new OrderReq(reqIDAtom.getAndIncrement(), order.getOrderRef());
        r.resultCode = traderApi.ReqOrderAction(order, r.requestID);
        return r;
    }

    //撤单
    public OrderReq cancelOrder(
            String exchangeID, String orderSysID, String instrumentID) {
        var order = genCancelOrder(exchangeID, orderSysID, instrumentID);
        return cancelOrder(order);
    }

    //查询报单信息
    public TraderReq queryOrder(CThostFtdcQryOrderField qry) {
        var r = genReq();
        r.resultCode = traderApi.ReqQryOrder(qry, r.requestID);
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
    public TraderReq queryTradeAccount(String currencyID) {
        var qry = genQryTradeAccount(currencyID);
        var r = genReq();
        r.resultCode = traderApi.ReqQryTradingAccount(qry, r.requestID);
        return r;
    }

    //查询投资者
    public TraderReq queryInvestor() {
        var qry = genQryInvestor();
        var r = genReq();
        r.resultCode = traderApi.ReqQryInvestor(qry, r.requestID);
        return r;
    }

    //查询合约
    public TraderReq queryInstrument(CThostFtdcQryInstrumentField qry) {
        var r = genReq();
        r.resultCode = traderApi.ReqQryInstrument(qry, r.requestID);
        return r;
    }

    //查询合约
    public TraderReq queryInstrument(String instrumentID) {
        var qry = genQryInstrument(instrumentID);
        return queryInstrument(qry);
    }

    //查询行情
    public TraderReq queryDepthMarketData(String instrumentID) {
        var qry = genQryDepthMarketData(instrumentID);
        var r = genReq();
        r.resultCode = traderApi.ReqQryDepthMarketData(qry, r.requestID);
        return r;
    }

    //查询持仓明细
    public TraderReq queryInvestorPositionDetail() {
        var qry = genQryInvestorPositionDetail();
        var r = genReq();
        r.resultCode = traderApi.ReqQryInvestorPositionDetail(qry, r.requestID);
        return r;
    }

//    新版本不再支持
//    //查询单腿汇总
//    public TraderReq queryInvestorPositionForComb() {
//        var qry = genQryInvestorPositionForComb();
//        var r = genReq();
//        r.resultCode = traderApi.ReqQryInvestorPositionForComb(qry, r.requestID);
//        return r;
//    }

    //生成request
    @Contract(" -> new")
    @NotNull
    private TraderReq genReq() {
        return new TraderReq(reqIDAtom.getAndIncrement());
    }

}
