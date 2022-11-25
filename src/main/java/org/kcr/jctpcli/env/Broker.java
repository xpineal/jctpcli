package org.kcr.jctpcli.env;

import org.kcr.jctpcli.trader.TraderCall;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Broker {
    // 交易所
    public String exchangeID;
    // 合约
    public String instrumentID;

    // 订单追踪
    public OrderTracker orderTracker;
    // 持仓统计
    public Holder holder;

    // 可用资金
    public double available;

    // 合约
    public Instrument instrument;

    // 初始化标记
    public Fence fence;

    // 锁
    private Lock lock;

    // trade caller
    private TraderCall traderCall;

    public Broker(TraderCall _traderCall, String _exchangeID, String _instrumentID) {
        traderCall = _traderCall;
        exchangeID = _exchangeID;
        instrumentID = _instrumentID;
        orderTracker = new OrderTracker();
        holder = new Holder();
        instrument = new Instrument();
        fence = new Fence();
        lock = new ReentrantLock();
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public void openBuy(double price, int volume) {

    }
}
