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
        var cost = instrument.openBuyCost(price, volume);
        return available >= cost;
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

    public boolean OnOrderTrade(String orderRef, int volume, double price) {
        var r = orderTracker.OnOrderTrade(orderRef, volume);

        if (r != null) {
            var existPrice = r.orderItem.price;
            r.orderItem.price = price;
            var retAvailable = instrument.OnOrderTrade(r);
            switch (r.orderItem.direction){
                case OpenBuy:
                    // 做多后可能会有回款
                    available += instrument.buyMargin(existPrice-price, volume);
                    break;
                case OpenSell:
                    // 做空后可能会有补款
                    available += instrument.sellMargin(existPrice-price, volume);
                    break;
                case CloseBuy:
                case CloseSell:
                    // 平后回款
                    available += retAvailable;
                    break;
            }
            return true;
        }
        return false;
    }

    public boolean OnOrderCancelled(String orderRef) {
        var r = orderTracker.OnOrderCancelled(orderRef);
        if (r != null) {
            // 撤单成功 -- 把可用金额加回去
            available += r.orderCancelCost(instrument);
            return true;
        }
        return false;
    }
}
