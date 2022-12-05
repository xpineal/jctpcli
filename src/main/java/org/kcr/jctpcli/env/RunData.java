package org.kcr.jctpcli.env;

import org.kcr.jctpcli.md.MarketData;

import java.util.ArrayList;

public class RunData {
    // 合约信息，里面包括了持仓信息
    public Instrument instrument;
    // 行情信息
    public MarketData marketData;
    // 进行中的订单信息
    public ArrayList<OrderInfo> orders;

    // 可用资金
    public double available;

    public RunData(Instrument _instrument, MarketData _marketData, ArrayList<OrderInfo> _orders, double _available) {
        instrument = _instrument;
        marketData = _marketData;
        orders = _orders;
        available = _available;
    }
}
