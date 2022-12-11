package org.kcr.jctpcli.core;

import java.util.ArrayList;

// 交易指令
public class TradeCmd {
    public ArrayList<OrderItem> exeOrders; //需要做的交易
    public ArrayList<OrderInfo> recallOrders; // 需要撤单的订单号列表

    public boolean printInfo;

    public TradeCmd(ArrayList<OrderItem> _exeOrders, ArrayList<OrderInfo> _recallOrders) {
        exeOrders = _exeOrders;
        recallOrders = _recallOrders;
        printInfo = false;
    }

    public TradeCmd(ArrayList<OrderItem> _exeOrders, ArrayList<OrderInfo> _recallOrders, boolean _printInfo) {
        exeOrders = _exeOrders;
        recallOrders = _recallOrders;
        printInfo = _printInfo;
    }

    @Override
    public String toString() {
        var sb = new StringBuffer(512);
        if (exeOrders != null) {
            sb.append("to be executed orders:\n");
            for (OrderItem order:exeOrders) {
                sb.append("direction:").append(order.direction).append(",");
                sb.append("volume:").append(order.volume).append(",");
                sb.append("price:").append(order.price).append(",");
            }
        }else {
            sb.append("to be executed orders are empty\n");
        }

        if (recallOrders != null) {
            sb.append("cancel orders:\n");
            for (OrderInfo order:recallOrders) {
                sb.append("order ref:").append(order.orderRef).append(",");
                sb.append("order direction:").append(order.orderItem.direction).append(",");
                sb.append("order volume:").append(order.orderItem.volume).append(",");
                sb.append("order price:").append(order.orderItem.price).append(",");
                sb.append("order state:").append(order.orderItem.state).append(",");
            }
        }else{
            sb.append("cancel orders are empty");
        }

        return sb.toString();
    }

}
