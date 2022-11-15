package org.kcr.jctpcli.trader;

public enum TradeState {
    NotTrade, //未成交
    HalfTrade, //部分成交
    Recall, //撤单中
    AllTrade, //全部成交
}
