package org.kcr.jctpcli.core;

import org.kr.jctp.CThostFtdcInstrumentField;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Hand {
    // 可用资金
    public double available;

    // 订单追踪
    public OrderTracker orderTracker;

    // 合约
    // public Instrument instrument;
    public HashMap<String, Instrument> instrumentHash;

    private Lock lock;

    public Hand() {
        orderTracker = new OrderTracker();
        instrumentHash = new HashMap<>();
        lock = new ReentrantLock();
    }
    
    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public void putInstrumentIfAbsent(Instrument instrument) {
        instrumentHash.putIfAbsent(instrument.instrumentID, instrument);
    }

    public void upsertInstrument(CThostFtdcInstrumentField pInstrument) {
        upsertInstrument(pInstrument, false);
    }

    public void upsertInstrument(InstrInfo pInstrument) {
        var instrument = instrumentHash.get(pInstrument.instrumentID);
        if (instrument == null) {
            instrument = new Instrument(pInstrument.exchangeID, pInstrument.instrumentID, false);
            instrumentHash.put(pInstrument.instrumentID, instrument);
        }
        instrument.setInstrumentRatio(pInstrument);
    }

    public void upsertInstrument(CThostFtdcInstrumentField pInstrument, boolean closeToday) {
        var instrID = pInstrument.getInstrumentID();
        var instrument = instrumentHash.get(instrID);
        if (instrument == null) {
            instrument = new Instrument(pInstrument.getExchangeID(), instrID, closeToday);
            instrumentHash.put(instrID, instrument);
        }
        instrument.setInstrumentRatio(pInstrument);
    }

    public Instrument getInstrument(String instrumentID) {
        return instrumentHash.get(instrumentID);
    }

    public Instrument getInstrumentWithLock(String instrumentID) {
        Instrument outInst = null;
        try {
            lock();
            outInst = instrumentHash.get(instrumentID);
        }finally {
            unlock();
        }
        return outInst;
    }

    public boolean canOpenBuy(Instrument instrument, OrderItem order) {
        var cost = instrument.openBuyCost(order.price, order.volume);
        return available >= cost;
    }

    public boolean canOpenSell(Instrument instrument, OrderItem order) {
        return available >= instrument.openSellCost(order.price, order.volume);
    }

    public boolean canCloseBuy(Instrument instrument, int volume) {
        if (instrument.buyHold.vol < volume) {
            //持仓数不够
            return false;
        }
        // 检查手续费
        return available >= instrument.closeFee(volume);
    }

    public boolean canCloseSell(Instrument instrument, int volume) {
        if (instrument.sellHold.vol < volume) {
            //持仓数不够
            return false;
        }
        // 检查手续费
        return available >= instrument.closeFee(volume);
    }

    public boolean OnOrderTrade(String orderRef, int volume, double price) {
        var r = orderTracker.OnOrderTrade(orderRef, volume);
        if (r == null) {
            System.out.printf("订单追踪中没有对应的单号:%s\n", orderRef);
            return false;
        }

        var existPrice = r.orderItem.price;
        r.orderItem.price = price;
        var instrument = getInstrument(r.orderItem.instrumentID);
        if (instrument == null) {
            System.out.printf("系统中没有对应的合约信息:%s\n", orderRef);
            return false;
        }
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

    public boolean OnOrderCancelled(String orderRef) {
        var r = orderTracker.OnOrderCancelled(orderRef);
        if (r == null) {
            System.out.printf("订单追踪中没有可以撤销的单号:%s\n", orderRef);
            return false;
        }
        var instrument = getInstrument(r.instrumentID);
        if (instrument == null) {
            System.out.printf("系统中没有对应的合约信息可以撤销:%s\n", orderRef);
            return false;
        }
        // 撤单成功 -- 把可用金额加回去
        available += r.orderCancelCost(instrument);
        return true;
    }
}
