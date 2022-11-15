package org.kcr.jctpcli.trader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OrderTrace {
    // 锁
    private Lock lock;
    // 追踪hash
    private HashMap<String, OrderValue> orderTrackHash;

    public OrderTrace() {
        lock = new ReentrantLock();
        orderTrackHash = new HashMap<>();
    }

    // 提交开多单后调用以便跟踪订单
    public void addOpenBuy(String orderRef, int volume, double price) {
        addOrder(orderRef, volume, price, Direction.OpenBuy);
    }

    // 提交平多单后调用以便跟踪订单
    public void addCloseBuy(String orderRef, int volume, double price) {
        addOrder(orderRef, volume, price, Direction.CloseBuy);
    }

    // 提交开空单后调用以便跟踪订单
    public void addOpenSell(String orderRef, int volume, double price) {
        addOrder(orderRef, volume, price, Direction.OpenSell);
    }

    // 提交平多单后调用以便跟踪订单
    public void addCloseSell(String orderRef, int volume, double price) {
        addOrder(orderRef, volume, price, Direction.CloseSell);
    }

    // 交易信息回包后调用
    public OrderValue reduceOrder(String orderRef, int volume, double price) {
        var r = new OrderValue(volume, price);
        lock.lock();
        var existOrder = orderTrackHash.get(orderRef);
        if (existOrder == null) {
            lock.unlock();
            return null;
        }
        existOrder.volume -= r.volume;
        if (existOrder.volume == 0) {
            orderTrackHash.remove(orderRef);
        }else {
            existOrder.state = TradeState.HalfTrade;
        }
        lock.unlock();
        return r;
    }

    // 提交撤单信息后调用以便跟踪订单
    public void recall(String orderRef) {
        lock.lock();
        var existOrder = orderTrackHash.get(orderRef);
        if (existOrder != null) {
            existOrder.state = TradeState.Recall;
        }
        lock.unlock();
    }

    // 撤单回包后调用
    public void recallOK(String orderRef) {
        lock.lock();
        orderTrackHash.remove(orderRef);
        lock.unlock();
    }

    // 获取所有没有成交的订单单号
    public ArrayList<String> remainOrderKeys() {
        lock.lock();
        var size = orderTrackHash.size();
        var a = new ArrayList<String>(size);
        orderTrackHash.forEach((key, value)->{
            // 正在撤单的订单不用算在未成交中
            if (value.state != TradeState.Recall) {
                a.add(key);
            }
        });
        lock.unlock();
        return a;
    }

    // 获取所有没有成交的订单信息
    public ArrayList<OrderInfo> remainOrders() {
        lock.lock();
        var size = orderTrackHash.size();
        var a = new ArrayList<OrderInfo>(size);
        orderTrackHash.forEach((key, value)->{
            // 正在撤单的订单不用算在未成交中
            if (value.state != TradeState.Recall) {
                a.add(new OrderInfo(key, value));
            }
        });
        lock.unlock();
        return a;
    }

    private void addOrder(String orderRef, int volume, double price, Direction direct) {
        lock.lock();
        var order = new OrderValue(volume, price);
        order.direction = direct;
        orderTrackHash.put(orderRef, order);
        lock.unlock();
    }
}
