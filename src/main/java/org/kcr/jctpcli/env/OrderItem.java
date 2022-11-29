package org.kcr.jctpcli.env;

import org.kcr.jctpcli.trader.TradeState;

// 订单内容
public class OrderItem {
    public int volume; // 量
    public double price; // 价
    public Direction direction; //方向(开多/平多/开空/平空)
    public TradeState state; //交易状态(未成交/部分成交/撤单中/全部成交)

    // 如果需要进行tick行情计数，可以在这里添加字段

    public OrderItem(int volume, double price) {
        this.volume = volume;
        this.price = price;
        this.state = TradeState.NotTrade;
    }

    public OrderItem(int volume, double price, Direction direction) {
        this.volume = volume;
        this.price = price;
        this.direction = direction;
    }

    // 此订单花费
    public double orderCancelCost(Instrument instrument) {
        switch (direction) {
            case OpenBuy:
                return instrument.openBuyCost(price, volume);
            case OpenSell:
                return instrument.openSellCost(price, volume);
            case CloseBuy:
            case CloseSell:
                return instrument.closeFee(volume);
        }
        return 0;
    }
}
