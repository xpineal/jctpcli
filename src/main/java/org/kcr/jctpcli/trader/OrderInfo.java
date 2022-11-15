package org.kcr.jctpcli.trader;

public class OrderInfo {
    public String orderRef; //订单编号
    public OrderValue orderValue; //订单内容

    public OrderInfo(String orderRef, OrderValue value) {
        this.orderRef = orderRef;
        this.orderValue = value;
    }
}
