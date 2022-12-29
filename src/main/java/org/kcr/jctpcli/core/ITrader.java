package org.kcr.jctpcli.core;

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
    public void cancelOrder(OrderInfo order, String exchangeID);

    // 认证
    QueryReq authenticate();

    // 登录
    public QueryReq login();

    // 查询当前交易日
    public String getTradingDay();

    public QueryReq queryInstrumentCommissionRate(Instrument instrument);

    public QueryReq queryInvestorPosition(String instrumentID);

    public QueryReq queryTradeAccount(String currencyID);

    public QueryReq queryInstrument(String instrumentID, String exchangeID);

    public void setAtom(int frontID, int sessionID, long orderRef);

    public boolean needFence();
}
