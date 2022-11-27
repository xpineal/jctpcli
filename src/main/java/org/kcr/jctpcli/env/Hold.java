package org.kcr.jctpcli.env;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Hold {
    // 可用资金
    public double available;

    // 订单追踪
    public OrderTracker orderTracker;

    // 合约
    public Instrument instrument;

    private Lock lock;

    public Hold(String exchangeID, String instrumentID) {
        orderTracker = new OrderTracker();
        instrument = new Instrument(exchangeID, instrumentID);
        lock = new ReentrantLock();
    }
    
    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public boolean canOpenBuy(Instrument instrument, double price, int volume) {
        return available >= instrument.openBuyCost(price, volume);
    }

    public boolean canOpenSell(Instrument instrument, double price, int volume) {
        return available >= instrument.openSellCost(price, volume);
    }

    public boolean canCloseBuy(Instrument instrument, int volume) {
        if (instrument.buyVol < volume) {
            //持仓数不够
            return false;
        }
        // 检查手续费
        return available >= instrument.closeFee(volume);
    }

    public boolean canCloseSell(Instrument instrument, int volume) {
        if (instrument.sellVol < volume) {
            //持仓数不够
            return false;
        }
        // 检查手续费
        return available >= instrument.closeFee(volume);
    }
}
