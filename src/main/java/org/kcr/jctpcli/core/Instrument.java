package org.kcr.jctpcli.core;

import org.kr.jctp.CThostFtdcInstrumentCommissionRateField;
import org.kr.jctp.CThostFtdcInstrumentField;

// 合约信息
public class Instrument {
    // 交易所
    public String exchangeID;
    // 合约ID
    public String instrumentID;
    // 合约数量乘数
    public int volumeMultiple;
    // 合约最小变动价格
    public double priceTick;
    // 多头保证金率
    public double longMarginRatio;
    // 空头保证金率
    public double shortMarginRatio;
    // 开仓手续费率
    public double openRatioByMoney;
    // 开仓手续费
    public double openRatioByVolume;
    // 平仓手续费率
    public double closeRatioByMoney;
    // 平仓手续费
    public double closeRatioByVolume;
    // 平今仓手续费率
    public double closeTodayRatioByMoney;
    // 平今仓手续费
    public double closeTodayRatioByVolume;

    // 合约是否平今仓
    public boolean closeToday = false;

    // 合约持仓信息
    // 多头持仓相关信息
    public Hold buyHold = new Hold();
    // 空头持仓相关信息
    public Hold sellHold = new Hold();

    public Instrument(String _exchangeID, String _instrumentID) {
        exchangeID = _exchangeID;
        instrumentID = _instrumentID;
    }

    public Instrument(String _exchangeID, String _instrumentID, boolean _closeToday) {
        exchangeID = _exchangeID;
        instrumentID = _instrumentID;
        closeToday = _closeToday;
    }

    @Override
    public String toString() {
        var sb = new StringBuffer(512);
        sb.append("多 -- ");
        sb.append(buyHold).append("\n");

        sb.append("空 -- ");
        sb.append(sellHold).append("\n");
        return sb.toString();
    }

    public void setInstrumentRatio(CThostFtdcInstrumentField pInstrument) {
        // 额外的手续费从API中拿不到，需要从配置读取
        var addMargin = Parameter.cnf.getAddMargin();
        // 手续费查不到，需要从配置读取
        openRatioByVolume = Parameter.cnf.getOpenRatio();
        closeRatioByVolume =  Parameter.cnf.getCloseRatio();

        // 多头保证金率
        longMarginRatio = pInstrument.getLongMarginRatio() + addMargin;
        // 空头保证金率
        shortMarginRatio = pInstrument.getShortMarginRatio() + addMargin;
        // 合约乘数
        volumeMultiple = pInstrument.getVolumeMultiple();
        // 合约最小变动价格
        priceTick = pInstrument.getPriceTick();
    }

    public void setInstrumentRatio(InstrInfo pInstrument) {
        // 额外的手续费从API中拿不到，需要从配置读取
        var addMargin = Parameter.cnf.getAddMargin();
        // 手续费查不到，需要从配置读取
        openRatioByVolume = Parameter.cnf.getOpenRatio();
        closeRatioByVolume =  Parameter.cnf.getCloseRatio();

        // 多头保证金率
        longMarginRatio = pInstrument.longMarginRatio + addMargin;
        // 空头保证金率
        shortMarginRatio = pInstrument.shortMarginRatio + addMargin;
        // 合约乘数
        volumeMultiple = pInstrument.volumeMultiple;
        // 合约最小变动价格
        priceTick = pInstrument.priceTick;
    }

    // 查询手续费及率
    /*public void setRatio(CThostFtdcInstrumentCommissionRateField tc) {
        // 开仓手续费率
        openRatioByMoney = tc.getOpenRatioByMoney();
        // 开仓手续费
        openRatioByVolume = tc.getOpenRatioByVolume();
        // 平仓手续费率
        closeRatioByMoney = tc.getCloseRatioByMoney();
        // 平仓手续费
        closeRatioByVolume = tc.getCloseRatioByVolume();
        // 平今仓手续费率
        closeTodayRatioByMoney = tc.getCloseTodayRatioByMoney();
        // 平今仓手续费
        closeTodayRatioByVolume = tc.getCloseTodayRatioByVolume();
    }

    public void setRatio(double _ratio) {
    	openRatioByVolume = _ratio;
    	closeRatioByVolume = _ratio;
    }*/
     
    // 开多平多保证金
    public double buyMargin(double price, int volume) {
        return price * volumeMultiple * longMarginRatio*volume;
    }

    // 做空平空保证金
    public double sellMargin(double price, int volume) {
        return price * volumeMultiple * shortMarginRatio*volume;
    }

    // 利润
    public double profile(double priceDelta, int volume) {
        return priceDelta * volumeMultiple * volume;
    }

    // 开仓手续费转化成价格
    public double openFeePrice(int volume) {
        return (openRatioByVolume*volume) / volumeMultiple;
    }

    // 开仓手续费
    public double openFee(int volume) {
        return openRatioByVolume * volume;
    }

    // 开多费用(保证金和手续费)
    public double openBuyCost(double price, int volume) {
        return buyMargin(price, volume) + openRatioByVolume * volume;
    }

    // 开空费用(保证金和手续费)
    public double openSellCost(double price, int volume) {
        return sellMargin(price, volume) + openRatioByVolume * volume;
    }

    // 平仓手续费转化成价格
    public double closeFeePrice(int volume) {
        return (closeRatioByVolume*volume) / volumeMultiple;
    }

    // 平仓手续费
    public double closeFee(int volume) {
        if (closeToday) {
            return volume * closeTodayRatioByVolume;
        }
        return volume * closeRatioByVolume;
    }

    public void outPut() {
        System.out.printf("合约乘数 : %d ; 最小变动价格 : %f\n", volumeMultiple, priceTick);
    }

    // 处理订单成交回包
    public double OnOrderTrade(OrderInfo order) {
        switch (order.orderItem.direction) {
            case OpenBuy:
                OnOpenBuy(order);
                break;
            case OpenSell:
                OnOpenSell(order);
                break;
            case CloseBuy:
                return OnCloseBuy(order);
            case CloseSell:
                return OnCloseSell(order);
        }
        return 0;
    }

    // 开多
    private void OnOpenBuy(OrderInfo order) {
        // 实际的价格需要考虑手续费
        if (!buyHold.addVol(order.orderItem.volume,  order.total() + openFeePrice(order.orderItem.volume),
                buyMargin(order.orderItem.price, order.orderItem.volume), openFee(order.orderItem.volume))){
            System.out.printf("错误的开多订单信息:%s\n", order);
        }
    }

    // 开空
    private void OnOpenSell(OrderInfo order) {
        // 实际的价格需要考虑手续费
        if (!sellHold.addVol(order.orderItem.volume,order.total() - openFeePrice(order.orderItem.volume),
                sellMargin(order.orderItem.price, order.orderItem.volume),  openFee(order.orderItem.volume))){
            System.out.printf("错误的开空订单信息:%s\n", order);
        }
    }

    // 平多 -- 返回可用资金增量(包括返还的保证金+利润+开平手续费)
    // 返回对应保证金+利润
    private double OnCloseBuy(OrderInfo order) {
        if (order.orderItem.volume > buyHold.vol) {
            System.out.printf("错误的平多订单信息:%s\n", order);
            return 0;
        }
        return buyHold.reduceVol(order.orderItem.volume,
                profile(order.orderItem.price-buyHold.price, order.orderItem.volume),
                openFee(order.orderItem.volume), closeFee(order.orderItem.volume));
    }

    // 平空 -- 返回可用资金增量(包括返还的保证金+利润+开平手续费)
    private double OnCloseSell(OrderInfo order) {
        if (order.orderItem.volume > sellHold.vol) {
            System.out.printf("错误的平空订单信息:%s\n", order);
            return 0;
        }
        return sellHold.reduceVol(order.orderItem.volume,
                profile(sellHold.price-order.orderItem.price, order.orderItem.volume),
                openFee(order.orderItem.volume), closeFee(order.orderItem.volume));
    }

}
