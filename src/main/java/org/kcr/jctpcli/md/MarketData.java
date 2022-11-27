package org.kcr.jctpcli.md;

import org.kr.jctp.CThostFtdcDepthMarketDataField;

public class MarketData {
    // 交易日
    public String tradingDay;
    // 业务日期
    public String actionDay;
    // 合约ID
    public String instrumentID;
    // 最后修改时间
    public String updateTime;
    // 最新价
    public double lastPrice;
    // 上次结算价
    public double preSettlementPrice;
    // 昨收盘
    public double preClosePrice;
    // 昨持仓量
    public double preOpenInterest;
    // 今开盘
    public double openPrice;
    // 最高价
    public double highestPrice;
    // 最低价
    public double lowestPrice;

    // 成交金额
    public double turnover;
    // 持仓量
    public double openInterest;
    // 本次结算价
    public double settlementPrice;
    // 涨停价
    public double upperLimitPrice;
    // 跌停价
    public double lowerLimitPrice;
    // 昨虚实度
    public double preDelta;
    // 今虚实度
    public double currDelta;
    // 当日均价
    public double averagePrice;
    // 上带价
    public double brandingUpperPrice;
    // 下带价
    public double brandingLowerPrice;

    // 申买价1 - 5
    public double bidPrice1;
    public double bidPrice2;
    public double bidPrice3;
    public double bidPrice4;
    public double bidPrice5;
    // 申卖价1-5
    public double askPrice1;
    public double askPrice2;
    public double askPrice3;
    public double askPrice4;
    public double askPrice5;

    // 申买量1 - 5
    public int bidVolume1;
    public int bidVolume2;
    public int bidVolume3;
    public int bidVolume4;
    public int bidVolume5;

    // 申卖量1 - 5
    public int askVolume1;
    public int askVolume2;
    public int askVolume3;
    public int askVolume4;
    public int askVolume5;

    // 数量
    public int volume;

    public MarketData(CThostFtdcDepthMarketDataField pDepthMarketData) {
        tradingDay = pDepthMarketData.getTradingDay();
        instrumentID = pDepthMarketData.getInstrumentID();
        lastPrice = pDepthMarketData.getLastPrice();
        preSettlementPrice = pDepthMarketData.getPreSettlementPrice();
        preClosePrice = pDepthMarketData.getPreClosePrice();
        preOpenInterest = pDepthMarketData.getPreOpenInterest();
        openPrice = pDepthMarketData.getOpenPrice();
        highestPrice = pDepthMarketData.getHighestPrice();
        lowestPrice = pDepthMarketData.getLowestPrice();
        volume = pDepthMarketData.getVolume();
        turnover = pDepthMarketData.getTurnover();
        openInterest = pDepthMarketData.getOpenInterest();
        settlementPrice = pDepthMarketData.getSettlementPrice();
        upperLimitPrice = pDepthMarketData.getUpperLimitPrice();
        lowerLimitPrice = pDepthMarketData.getLowerLimitPrice();
        preDelta = pDepthMarketData.getPreDelta();
        currDelta = pDepthMarketData.getCurrDelta();
        updateTime = pDepthMarketData.getUpdateTime();

        bidPrice1 = pDepthMarketData.getBidPrice1();
        bidVolume1 = pDepthMarketData.getBidVolume1();
        bidPrice2 = pDepthMarketData.getBidPrice2();
        bidVolume2 = pDepthMarketData.getBidVolume2();
        bidPrice3 = pDepthMarketData.getBidPrice3();
        bidVolume3 = pDepthMarketData.getBidVolume3();
        bidPrice4 = pDepthMarketData.getBidPrice4();
        bidVolume4 = pDepthMarketData.getBidVolume4();
        bidPrice5 = pDepthMarketData.getBidPrice5();
        bidVolume5 = pDepthMarketData.getBidVolume5();

        askPrice1 = pDepthMarketData.getAskPrice1();
        askVolume1 = pDepthMarketData.getAskVolume1();
        askPrice2 = pDepthMarketData.getAskPrice2();
        askVolume2 = pDepthMarketData.getAskVolume2();
        askPrice3 = pDepthMarketData.getAskPrice3();
        askVolume3 = pDepthMarketData.getAskVolume3();
        askPrice4 = pDepthMarketData.getAskPrice4();
        askVolume4 = pDepthMarketData.getAskVolume4();
        askPrice5 = pDepthMarketData.getAskPrice5();
        askVolume5 = pDepthMarketData.getAskVolume5();

        averagePrice = pDepthMarketData.getAveragePrice();
        actionDay = pDepthMarketData.getActionDay();
        brandingUpperPrice = pDepthMarketData.getBandingUpperPrice();
        brandingLowerPrice = pDepthMarketData.getBandingLowerPrice();
    }

    public String brief() {
        return updateTime + "," + Double.toString(bidPrice1)
                + "," + Integer.toString(bidVolume1)
                + "," + Double.toString(askPrice1)
                + "," + Integer.toString(askVolume1)
                + "," + Double.toString(lastPrice)
                + "," + Integer.toString(volume)
                + "," + Double.toString(turnover);
    }

}
