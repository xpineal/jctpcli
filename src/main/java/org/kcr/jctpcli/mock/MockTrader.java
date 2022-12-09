package org.kcr.jctpcli.mock;

import org.kcr.jctpcli.env.Instrument;
import org.kcr.jctpcli.env.OrderInfo;
import org.kcr.jctpcli.env.OrderTracker;
import org.kcr.jctpcli.trader.ITrader;
import org.kcr.jctpcli.trader.TraderReq;

import java.util.concurrent.atomic.AtomicLong;

public class MockTrader implements ITrader {

    private final AtomicLong orderRefAtom;
    // 订单跟踪
    private OrderTracker orderTracker;

    public MockTrader(OrderTracker _orderTracker) {
        orderRefAtom = new AtomicLong(0);
        orderTracker = _orderTracker;
    }

    @Override
    public boolean openBuy(Instrument instrument, double price, int volume) {
        var orderRef = Long.toString(orderRefAtom.incrementAndGet());
        orderTracker.OnOpenBuyReq(orderRef, volume, price, instrument.exchangeID, instrument.instrumentID);
        return true;
    }

    @Override
    public boolean closeBuy(Instrument instrument, double price, int volume) {
        var orderRef = Long.toString(orderRefAtom.incrementAndGet());
        orderTracker.OnCloseBuyReq(orderRef, volume, price, instrument.exchangeID, instrument.instrumentID);
        return true;
    }

    @Override
    public boolean openSell(Instrument instrument, double price, int volume) {
        var orderRef = Long.toString(orderRefAtom.incrementAndGet());
        orderTracker.OnOpenSellReq(orderRef, volume, price, instrument.exchangeID, instrument.instrumentID);
        return true;
    }

    @Override
    public boolean closeSell(Instrument instrument, double price, int volume) {
        var orderRef = Long.toString(orderRefAtom.incrementAndGet());
        orderTracker.OnCloseSellReq(orderRef, volume, price, instrument.exchangeID, instrument.instrumentID);
        return true;
    }

    @Override
    public void cancelOrder(OrderInfo order) {
        orderTracker.OnOrderCancelReq(order.orderRef);
    }

    @Override
    public TraderReq authenticate() {
        return null;
    }

    @Override
    public TraderReq login() {
        return null;
    }

    @Override
    public String getTradingDay() {
        return "2022-12-05";
    }

    @Override
    public TraderReq queryInstrumentCommissionRate(Instrument instrument) {
        return null;
    }

    @Override
    public TraderReq queryInvestorPosition(String instrumentID) {
        return null;
    }

    @Override
    public TraderReq queryTradeAccount(String currencyID) {
        return null;
    }

    @Override
    public TraderReq queryInstrument(String instrumentID, String exchangeID) {
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
