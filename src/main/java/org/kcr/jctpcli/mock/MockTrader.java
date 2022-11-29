package org.kcr.jctpcli.mock;

import org.kcr.jctpcli.env.Instrument;
import org.kcr.jctpcli.env.OrderTracker;
import org.kcr.jctpcli.trader.ITrader;

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
        orderTracker.OnOpenBuyReq(orderRef, volume, price);
        return true;
    }

    @Override
    public boolean closeBuy(Instrument instrument, double price, int volume) {
        var orderRef = Long.toString(orderRefAtom.incrementAndGet());
        orderTracker.OnCloseBuyReq(orderRef, volume, price);
        return true;
    }

    @Override
    public boolean openSell(Instrument instrument, double price, int volume) {
        var orderRef = Long.toString(orderRefAtom.incrementAndGet());
        orderTracker.OnOpenSellReq(orderRef, volume, price);
        return true;
    }

    @Override
    public boolean closeSell(Instrument instrument, double price, int volume) {
        var orderRef = Long.toString(orderRefAtom.incrementAndGet());
        orderTracker.OnCloseSellReq(orderRef, volume, price);
        return true;
    }

    @Override
    public void cancelOrder(String orderRef) {
        orderTracker.OnOrderCancelReq(orderRef);
    }
}
