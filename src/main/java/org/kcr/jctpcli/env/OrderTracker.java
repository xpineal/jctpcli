package org.kcr.jctpcli.env;

import org.kcr.jctpcli.trader.TradeState;

import java.util.ArrayList;
import java.util.HashMap;

// 订单跟踪
public class OrderTracker {
    // 追踪hash
    private HashMap<String, OrderItem> orderTrackHash;

    public OrderTracker() {
        orderTrackHash = new HashMap<>();
    }

    // 提交开多单后调用以便跟踪订单
    public void OnOpenBuyReq(String orderRef, int volume, double price) {
        addOrder(orderRef, volume, price, Direction.OpenBuy);
    }

    // 提交平多单后调用以便跟踪订单
    public void OnCloseBuyReq(String orderRef, int volume, double price) {
        addOrder(orderRef, volume, price, Direction.CloseBuy);
    }

    // 提交开空单后调用以便跟踪订单
    public void OnOpenSellReq(String orderRef, int volume, double price) {
        addOrder(orderRef, volume, price, Direction.OpenSell);
    }

    // 提交平多单后调用以便跟踪订单
    public void OnCloseSellReq(String orderRef, int volume, double price) {
        addOrder(orderRef, volume, price, Direction.CloseSell);
    }

    // 处理订单成交回包
    public OrderInfo OnOrderTrade(String orderRef, int volume, double price) {
        var existOrder = orderTrackHash.get(orderRef);
        if (existOrder == null) {
            return null;
        }
        var r = new OrderInfo(orderRef, volume, price, existOrder.direction);
        existOrder.volume -= volume;
        if (existOrder.volume == 0) {
            orderTrackHash.remove(orderRef);
        }else {
            existOrder.state = TradeState.HalfTrade;
        }
        return r;
    }

    // 提交撤单信息后调用以便跟踪订单
    public void OnOrderCancelReq(String orderRef) {
        var existOrder = orderTrackHash.get(orderRef);
        if (existOrder != null) {
            existOrder.state = TradeState.Recall;
        }
    }

    // 撤单回包后调用
    public void OnOrderCancelled(String orderRef) {
        orderTrackHash.remove(orderRef);
    }

    public ArrayList<String> allOrderKeys() {
        var size = orderTrackHash.size();
        var a = new ArrayList<String>(size);
        orderTrackHash.forEach((key, value)->{
            a.add(key);
        });
        return a;
    }

    // 获取所有没有成交的订单单号
    public ArrayList<String> remainOrderKeys() {
        var size = orderTrackHash.size();
        var a = new ArrayList<String>(size);
        orderTrackHash.forEach((key, value)->{
            // 正在撤单的订单不用算在未成交中
            if (value.state != TradeState.Recall) {
                a.add(key);
            }
        });
        return a;
    }

    // 获取所有没有成交的订单信息
    public ArrayList<OrderInfo> remainOrders() {
        var size = orderTrackHash.size();
        var a = new ArrayList<OrderInfo>(size);
        orderTrackHash.forEach((key, value)->{
            // 正在撤单的订单不用算在未成交中
            if (value.state != TradeState.Recall) {
                a.add(new OrderInfo(key, value));
            }
        });
        return a;
    }

    private void addOrder(String orderRef, int volume, double price, Direction direct) {
        var order = new OrderItem(volume, price);
        order.direction = direct;
        orderTrackHash.put(orderRef, order);
    }
}