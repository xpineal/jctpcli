package org.kcr.jctpcli.trader;

import org.kcr.jctpcli.env.Instrument;

public interface ITrader {
    // 开多
    public boolean openBuy(Instrument instrument, double price, int volume);
    // 平多
    public boolean closeBuy(Instrument instrument, double price, int volume);
    // 开空
    public boolean openSell(Instrument instrument, double price, int volume);
    // 平空
    public boolean closeSell(Instrument instrument, double price, int volume);
    // 撤单
    public void cancelOrder(String orderRef);
}
