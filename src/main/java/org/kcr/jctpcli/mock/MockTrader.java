package org.kcr.jctpcli.mock;

import org.kcr.jctpcli.env.Instrument;
import org.kcr.jctpcli.trader.ITrader;

import java.util.concurrent.atomic.AtomicLong;

public class MockTrader implements ITrader {

    private final AtomicLong orderRefAtom;

    public MockTrader() {
        orderRefAtom = new AtomicLong();
    }

    @Override
    public boolean openBuy(Instrument instrument, double price, int volume) {
        return true;
    }

    @Override
    public boolean closeBuy(Instrument instrument, double price, int volume) {
        return true;
    }

    @Override
    public boolean openSell(Instrument instrument, double price, int volume) {
        return true;
    }

    @Override
    public boolean closeSell(Instrument instrument, double price, int volume) {
        return true;
    }

    @Override
    public void cancelOrder(String orderRef) {
    }
}
