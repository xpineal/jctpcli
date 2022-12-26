package org.kcr.jctpcli.core;

import java.util.ArrayList;
import java.util.HashMap;

// 订单跟踪
public class OrderTracker {
    // 追踪hash
    private HashMap<String, OrderItem> orderTrackHash;

    public OrderTracker() {
        orderTrackHash = new HashMap<>();
    }

    public OrderItem getOrder(String orderRef) {
        return orderTrackHash.get(orderRef);
    }

    // 提交开多单后调用以便跟踪订单
    public void OnOpenBuyReq(String orderRef, OrderItem order) {
        addOrder(orderRef, order, Direction.OpenBuy);
    }

    // 提交平多单后调用以便跟踪订单
    public void OnCloseBuyReq(String orderRef, OrderItem order) {
        addOrder(orderRef, order, Direction.CloseBuy);
    }

    // 提交开空单后调用以便跟踪订单
    public void OnOpenSellReq(String orderRef, OrderItem order) {
        addOrder(orderRef, order, Direction.OpenSell);
    }

    // 提交平多单后调用以便跟踪订单
    public void OnCloseSellReq(String orderRef, OrderItem order) {
        addOrder(orderRef, order, Direction.CloseSell);
    }

    // 处理订单成交回包
    public OrderInfo OnOrderTrade(String orderRef, int volume) {
        var existOrder = orderTrackHash.get(orderRef);
        if (existOrder == null) {
            return null;
        }
        var r = new OrderInfo(orderRef, volume, existOrder.price, existOrder.instrumentID, existOrder.direction);
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
    public OrderItem OnOrderCancelled(String orderRef) {
        return orderTrackHash.remove(orderRef);
    }

    public ArrayList<String> allOrderKeys() {
        var size = orderTrackHash.size();
        var a = new ArrayList<String>(size);
        orderTrackHash.forEach((key, value)->{
            a.add(key);
        });
        return a;
    }

    public ArrayList<OrderInfo> allOrders() {
        var size = orderTrackHash.size();
        var a = new ArrayList<OrderInfo>(size);
        orderTrackHash.forEach((key, value)->{
            a.add(new OrderInfo(key, value));
        });
        return a;
    }

    public String allOrdersToString() {
        var sb = new StringBuffer(512);
        sb.append("[\n");
        orderTrackHash.forEach((key, value)->{
            sb.append("order ref:").append(key).append(",");
            sb.append("direction:").append(value.direction).append(",");
            sb.append("price:").append(value.volume).append(",");
            sb.append("state:").append(value.price).append(",");
            sb.append("direction:").append(value.state).append("\n");
        });
        sb.append("]");
        return sb.toString();
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

    private void addOrder(String orderRef, OrderItem order, Direction direct) {
        order.direction = direct;
        orderTrackHash.put(orderRef, order);
    }
}
