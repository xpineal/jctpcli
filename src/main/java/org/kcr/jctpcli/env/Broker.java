package org.kcr.jctpcli.env;

import org.kcr.jctpcli.md.MarketData;
import org.kcr.jctpcli.trader.Fence;
import org.kcr.jctpcli.trader.TraderCall;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Broker {
    // trade caller
    private TraderCall traderCall;

    // 包装订单跟踪，持仓情况，资金和合约
    public Hold hold;

    public Broker(TraderCall _traderCall, Hold _hold) {
        traderCall = _traderCall;
        hold = _hold;
    }

    // 使用Hold的lock
    public void lock() {
        hold.lock();
    }

    // 使用Hold的lock
    public void unlock() {
        hold.unlock();
    }

    public void marketProcess(MarketData md) {

    }

}
