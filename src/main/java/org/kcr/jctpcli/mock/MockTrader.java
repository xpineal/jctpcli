package org.kcr.jctpcli.mock;

import org.kcr.jctpcli.core.*;

import java.util.concurrent.atomic.AtomicLong;

// 此类仅用于测试
public class MockTrader implements ITrader {

    private final AtomicLong orderRefAtom;
    // 订单跟踪
    private final OrderTracker orderTracker;

    public MockTrader(OrderTracker _orderTracker) {
        orderRefAtom = new AtomicLong(0);
        orderTracker = _orderTracker;
    }

    @Override
    public boolean openBuy(Instrument instrument, double price, int volume) {
        var orderRef = Long.toString(orderRefAtom.incrementAndGet());
        orderTracker.OnOpenBuyReq(orderRef, new OrderItem(volume, price, instrument.instrumentID));
        return true;
    }

    @Override
    public boolean closeBuy(Instrument instrument, double price, int volume) {
        var orderRef = Long.toString(orderRefAtom.incrementAndGet());
        orderTracker.OnCloseBuyReq(orderRef, new OrderItem(volume, price, instrument.instrumentID));
        return true;
    }

    @Override
    public boolean openSell(Instrument instrument, double price, int volume) {
        var orderRef = Long.toString(orderRefAtom.incrementAndGet());
        orderTracker.OnOpenSellReq(orderRef, new OrderItem(volume, price, instrument.instrumentID));
        return true;
    }

    @Override
    public boolean closeSell(Instrument instrument, double price, int volume) {
        var orderRef = Long.toString(orderRefAtom.incrementAndGet());
        orderTracker.OnCloseSellReq(orderRef, new OrderItem(volume, price, instrument.instrumentID));
        return true;
    }

    @Override
    public void cancelOrder(OrderInfo order, String exchangeID) {
        orderTracker.OnOrderCancelReq(order.orderRef);
    }

    @Override
    public QueryReq authenticate() {
        return null;
    }

    @Override
    public QueryReq login() {
        return null;
    }

    @Override
    public String getTradingDay() {
        return "2022-12-05";
    }

    @Override
    public QueryReq queryInstrumentCommissionRate(Instrument instrument) {
        return null;
    }

    @Override
    public QueryReq queryInvestorPosition(String instrumentID) {
        return null;
    }

    @Override
    public QueryReq queryTradeAccount(String currencyID) {
        return null;
    }

    @Override
    public QueryReq queryInstrument(String instrumentID, String exchangeID) {
        return null;
    }

    @Override
    public void setAtom(int frontID, int sessionID, long orderRef) {
    }

    @Override
    public boolean needFence() {
        return false;
    }
}
