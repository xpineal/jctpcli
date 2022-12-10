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
    public void cancelOrder(OrderInfo order);

    // 认证
    TraderReq authenticate();

    // 登录
    public TraderReq login();

    // 查询当前交易日
    public String getTradingDay();

    public TraderReq queryInstrumentCommissionRate(Instrument instrument);

    public TraderReq queryInvestorPosition(String instrumentID);

    public TraderReq queryTradeAccount(String currencyID);

    public TraderReq queryInstrument(String instrumentID, String exchangeID);

    public void setAtom(int frontID, int sessionID, long orderRef);

    public boolean needFence();
}
