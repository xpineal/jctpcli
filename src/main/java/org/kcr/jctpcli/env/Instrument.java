package org.kcr.jctpcli.env;

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
    // 多头持仓和价格
    public int buyVol = 0;
    public double buyPrice = 0;
    public double actBuyPrice = 0;
    public double marginBuyPrice = 0;
    // 多单利润
    public double buyProfile = 0;
    // 开多手续费总和
    public double openBuyTotalFee = 0;
    // 平多手续费总和
    public double closeBuyTotalFee = 0;

    // 空头持仓和价格
    public int sellVol = 0;
    public double sellPrice = 0;
    public double actSellPrice = 0;
    public double marginSellPrice = 0;
    // 空单利润
    public double sellProfile = 0;
    // 开空手续费总和
    public double openSellTotalFee = 0;
    // 平空手续费
    public double closeSellTotalFee = 0;

    public Instrument(String _exchangeID, String _instrumentID) {
        exchangeID = _exchangeID;
        instrumentID = _instrumentID;
    }

    public Instrument(String _exchangeID, String _instrumentID, boolean _closeToday) {
        exchangeID = _exchangeID;
        instrumentID = _instrumentID;
        closeToday = _closeToday;
    }

    public String holdInfo() {
        var sb = new StringBuffer(512);
        sb.append("多 -- ");
        sb.append("数量:").append(buyVol).append(",");
        sb.append("价格:").append(buyPrice).append(",");
        sb.append("实际价格:").append(actBuyPrice).append(",");
        sb.append("保证金价格:").append(marginBuyPrice).append(",");
        sb.append("利润:").append(buyProfile).append(",");
        sb.append("开手续费:").append(openBuyTotalFee).append(",");
        sb.append("平手续费:").append(closeBuyTotalFee).append("\n");

        sb.append("空 -- ");
        sb.append("数量:").append(sellVol).append(",");
        sb.append("价格:").append(sellPrice).append(",");
        sb.append("实际价格:").append(actSellPrice).append(",");
        sb.append("保证金价格:").append(marginSellPrice).append(",");
        sb.append("利润:").append(sellProfile).append(",");
        sb.append("开手续费:").append(openSellTotalFee).append(",");
        sb.append("平手续费:").append(closeSellTotalFee).append("\n");
        return sb.toString();
    }

    public void setInstrumentRatio(CThostFtdcInstrumentField pInstrument) {
        // 多头保证金率
        longMarginRatio = pInstrument.getLongMarginRatio();
        // 空头保证金率
        shortMarginRatio = pInstrument.getShortMarginRatio();
        // 合约乘数
        volumeMultiple = pInstrument.getVolumeMultiple();
        // 合约最小变动价格
        priceTick = pInstrument.getPriceTick();
    }

    // 查询手续费及率
    public void setRatio(CThostFtdcInstrumentCommissionRateField tc) {
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
        var total = buyPrice*buyVol + order.total();
        // 实际的价格需要考虑手续费
        var actTotal = actBuyPrice*buyVol + order.total() + openFeePrice(order.orderItem.volume);
        // 实缴保证金
        var marginTotal = marginBuyPrice*buyVol + buyMargin(order.orderItem.price, order.orderItem.volume);
        buyVol += order.orderItem.volume;
        openBuyTotalFee += openFee(order.orderItem.volume);

        if (buyVol > 0) {
            buyPrice = total/buyVol;
            actBuyPrice = actTotal/buyVol;
            marginBuyPrice = marginTotal/buyVol;
        }else {
            System.out.printf("错误的开多订单信息:%s\n", order.ToString());
        }
    }

    // 开空
    private void OnOpenSell(OrderInfo order) {
        var total = sellPrice*sellVol + order.total();
        // 实际的价格需要考虑手续费
        var actTotal = actSellPrice*sellVol + order.total() - openFeePrice(order.orderItem.volume);
        // 实缴保证金
        var marginTotal = marginSellPrice*sellVol + sellMargin(order.orderItem.price, order.orderItem.volume);
        sellVol += order.orderItem.volume;
        openSellTotalFee += openFee(order.orderItem.volume);

        if (sellVol > 0) {
            sellPrice = total/sellVol;
            actSellPrice = actTotal/sellVol;
            marginSellPrice = marginTotal/sellVol;
        }else {
            System.out.printf("错误的开空订单信息:%s\n", order.ToString());
        }
    }

    // 平多 -- 返回可用资金增量
    // 部分平只返回对应的保证金
    // 全平返回对应保证金+利润
    private double OnCloseBuy(OrderInfo order) {
        if (order.orderItem.volume > buyVol) {
            System.out.printf("错误的平多订单信息:%s\n", order.ToString());
            return 0;
        }
        var cf = closeFee(order.orderItem.volume);
        closeBuyTotalFee += cf;
        // 计算系统返还的保证金
        var retMargin = marginBuyPrice*order.orderItem.volume;

        if (order.orderItem.volume == buyVol) {
            // 全平
            // 全平时才计入利润
            // 计算利润
            var benefit = profile(order.orderItem.price-actBuyPrice, order.orderItem.volume) - cf;
            buyProfile += benefit;
            buyVol = 0;
            buyPrice = 0;
            actBuyPrice = 0;
            marginBuyPrice = 0;
            // 返回可用资金增量 = 返还的保证金+利润+开平手续费(利润部分已经扣除了开平手续费，这里需要加回来)
            return retMargin + benefit + cf + openFee(order.orderItem.volume);
        }

        var orderTotal = order.total();
        var total = (buyPrice*buyVol - orderTotal);
        // 实际的价格需要考虑手续费
        var actTotal = (actBuyPrice*buyVol - orderTotal + closeFeePrice(order.orderItem.volume));
        buyVol -= order.orderItem.volume;
        buyPrice = total/buyVol;
        actBuyPrice = actTotal/buyVol;
        // 返回可用资金增量 = 返还的保证金+开平手续费(手续费计入到价格中了，这里需要加回来)
        return retMargin + cf + openFee(order.orderItem.volume);
    }

    // 平空 -- 返回可用资金增量(包括返还的保证金+利润+开平手续费)
    private double OnCloseSell(OrderInfo order) {
        if (order.orderItem.volume > sellVol) {
            System.out.printf("错误的平空订单信息:%s\n", order.ToString());
            return 0;
        }
        var cf = closeFee(order.orderItem.volume);
        closeSellTotalFee += cf;
        // 计算系统返还的保证金
        var retMargin = marginSellPrice*order.orderItem.volume;

        if (order.orderItem.volume == sellVol) {
            // 全平
            // 全平时才计入利润
            // 计算利润
            var benefit = profile(actSellPrice-order.orderItem.price, order.orderItem.volume) - cf;
            sellProfile += benefit;
            sellVol = 0;
            sellPrice = 0;
            actSellPrice = 0;
            marginSellPrice = 0;
            // 返回可用资金增量 = 返还的保证金+利润+开平手续费(利润部分已经扣除了开平手续费，这里需要加回来)
            return retMargin + benefit + cf + openFee(order.orderItem.volume);
        }

        var orderTotal = order.total();
        var total = (sellPrice*sellPrice - orderTotal);
        // 实际的价格需要考虑手续费
        var actTotal = (actSellPrice*sellVol - orderTotal - closeFeePrice(order.orderItem.volume));
        sellVol -= order.orderItem.volume;
        sellPrice = total/sellVol;
        actSellPrice = actTotal/sellVol;
        // 返回可用资金增量 = 返还的保证金+开平手续费(手续费计入到价格中了，这里需要加回来)
        return retMargin + cf + openFee(order.orderItem.volume);
    }

}
