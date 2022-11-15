package org.kcr.jctpcli.trader;

public class OrderValue {
    public int volume; // 量
    public double price; // 价
    public Direction direction; //方向(开多/平多/开空/平空)
    public TradeState state; //交易状态(未成交/部分成交/撤单中/全部成交)

    // 如果需要进行tick行情计数，可以在这里添加字段

    public OrderValue(int volume, double price) {
        this.volume = volume;
        this.price = price;
        this.state = TradeState.NotTrade;
    }
}
