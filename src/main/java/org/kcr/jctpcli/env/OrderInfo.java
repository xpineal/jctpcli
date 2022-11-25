package org.kcr.jctpcli.env;

// 订单号和订单内容
public class OrderInfo implements Comparable {
    public String orderRef; //订单编号
    public OrderItem orderItem; //订单内容

    public String ToString() {
        return String.format("订单编号:%s 数量:%d 价格:%f 方向:%d",
                orderRef, orderItem.volume, orderItem.price, orderItem.direction);
    }

    public OrderInfo(String orderRef, OrderItem value) {
        this.orderRef = orderRef;
        this.orderItem = value;
    }

    public OrderInfo(String orderRef, int vol, double price, Direction direct) {
        this.orderRef = orderRef;
        this.orderItem = new OrderItem(vol, price, direct);
    }

    // 订单总价
    public double total() {
        return orderItem.volume * orderItem.price;
    }

    public int compareTo(Object o) {
        var nc = (OrderInfo)o;
        if (orderItem.direction == Direction.OpenBuy) {
            return normalCompare(nc);
        }else{
            return -normalCompare(nc);
        }
    }

    private int normalCompare(OrderInfo o) {
        if (orderItem.price > o.orderItem.price) {
            return 1;
        }else if (orderItem.price < o.orderItem.price) {
            return -1;
        }
        return 0;
    }
}
