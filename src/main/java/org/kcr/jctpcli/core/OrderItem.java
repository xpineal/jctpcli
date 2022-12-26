package org.kcr.jctpcli.core;

import java.util.ArrayList;

// 订单内容
public class OrderItem {
    public int volume; // 量
    public double price; // 价
    public Direction direction; //方向(开多/平多/开空/平空)
    public TradeState state; //交易状态(未成交/部分成交/撤单中/全部成交)

    public String instrumentID;

    // 如果需要进行tick行情计数，可以在这里添加字段

    public static ArrayList<OrderItem> MakeOrderItems(
            int volume, double price, String exchangeID, String instrumentID, Direction direction) {
        var orders = new ArrayList<OrderItem>(1);
        orders.add(new OrderItem(volume, price, instrumentID, direction));
        return orders;
    }

    public OrderItem(int volume, double price, String instrumentID) {
        this.volume = volume;
        this.price = price;
        this.state = TradeState.NotTrade;
        this.instrumentID = instrumentID;
    }

    public OrderItem(int volume, double price, String instrumentID, Direction direction) {
        this.volume = volume;
        this.price = price;
        this.direction = direction;
        this.instrumentID = instrumentID;
    }

    // 此订单花费
    public double orderCancelCost(Instrument instrument) {
        switch (direction) {
            case OpenBuy:
                return instrument.openBuyCost(this);
            case OpenSell:
                return instrument.openSellCost(this);
            case CloseBuy:
            case CloseSell:
                return instrument.closeFee(this);
        }
        return 0;
    }
}
