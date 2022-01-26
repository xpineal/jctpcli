package org.kcr.jctpcli;

//封装订单请求
public class OrderReq {
    public int requestID;
    public int resultCode;
    public String orderRef;

    public OrderReq(int rID, String orderRef) {
        this.requestID = rID;
        this.orderRef = orderRef;
    }

    @Override
    public String toString() {
        return "OrderReq{" +
                "requestID=" + requestID +
                ", resultCode=" + resultCode +
                ", orderRef='" + orderRef + '\'' +
                '}';
    }

    public void outConsole() {
        System.out.printf("报单 单号:%s 请求:%d -- 回应:%d\n", orderRef, requestID, resultCode);
    }
}

