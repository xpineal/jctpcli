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

    // 空头持仓和价格
    public int sellVol = 0;
    public double sellPrice = 0;

    // 多单利润
    public double buyProfile = 0;
    // 空单利润
    public double sellProfile = 0;

    // 开多手续费
    public double openBuyFee = 0;
    // 开空手续费
    public double openSellFee = 0;
    // 平多手续费
    public double closeBuyFee = 0;
    // 平空手续费
    public double closeSellFee = 0;

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
        sb.append("buy volume:").append(buyVol).append(",");
        sb.append("buy price:").append(buyPrice).append(",");
        sb.append("sell volume:").append(sellVol).append(",");
        sb.append("sell price:").append(sellPrice).append(",");
        sb.append("buy profile:").append(buyProfile).append(",");
        sb.append("sell profile:").append(sellProfile).append(",");
        sb.append("open buy fee:").append(openBuyFee).append(",");
        sb.append("open sell fee:").append(openSellFee).append(",");
        sb.append("close buy fee:").append(closeBuyFee).append(",");
        sb.append("close sell fee:").append(closeSellFee);
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

    // 开多费用(保证金和手续费)
    public double openBuyCost(double price, int volume) {
        return buyMargin(price, volume) + openRatioByVolume * volume;
    }

    // 开空费用(保证金和手续费)
    public double openSellCost(double price, int volume) {
        return sellMargin(price, volume) + openRatioByVolume * volume;
    }

    // 开仓手续费
    public double openFee(int volume) {
        return volume * openRatioByVolume;
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
    public void OnOrderTrade(OrderInfo order) {
        switch (order.orderItem.direction) {
            case OpenBuy:
                OnOpenBuy(order);
                break;
            case OpenSell:
                OnOpenSell(order);
                break;
            case CloseBuy:
                OnCloseBuy(order);
                break;
            case CloseSell:
                OnCloseSell(order);
                break;
        }
    }

    // 开多
    private void OnOpenBuy(OrderInfo order) {
        var of = openFee(order.orderItem.volume);
        var total = buyPrice*buyVol + of + order.total();
        buyVol += order.orderItem.volume;
        openBuyFee += of;

        if (buyVol > 0) {
            buyPrice = total/buyVol;
        }else {
            System.out.printf("错误的开多订单信息:%s\n", order.ToString());
        }
    }

    // 开空
    private void OnOpenSell(OrderInfo order) {
        var of = openFee(order.orderItem.volume);
        var total = sellPrice*sellVol - of + order.total();
        sellVol += order.orderItem.volume;
        openSellFee += of;
        if (sellVol > 0) {
            sellPrice = total/sellVol;
        }else {
            System.out.printf("错误的开空订单信息:%s\n", order.ToString());
        }
    }

    // 平多
    private double OnCloseBuy(OrderInfo order) {
        if (order.orderItem.volume > buyVol) {
            System.out.printf("错误的平多订单信息:%s\n", order.ToString());
//            buyVol = 0;
//            buyPrice = 0;
            return 0;
        }
        // 计算获利
        // 利润 = 平多价格*平多数量(order.total()) - 买多均价*平多数量 - 手续费
        var cf = closeFee(order.orderItem.volume);
        var benefit = order.total() - buyPrice*order.orderItem.volume - cf;
        closeBuyFee += cf;
        if (order.orderItem.volume == buyVol) {
            // 全平
            buyVol = 0;
            buyPrice = 0;
            // 全平清仓才计算利润
            buyProfile += benefit;
            return benefit;
        }

        buyVol -= order.orderItem.volume;
        // 当前均价 = 当前持仓成本/当前持仓数量
        // 当前持仓成本 = 剩余数量*平仓前均价 - 利润
        buyPrice = (buyVol * buyPrice-benefit)/buyVol;
        return benefit;
    }

    // 平空
    private double OnCloseSell(OrderInfo order) {
        if (order.orderItem.volume > sellVol) {
            System.out.printf("错误的平空订单信息:%s\n", order.ToString());
//            sellVol = 0;
//            sellPrice = 0;
            return 0;
        }
        // 计算获利，平空只有在价格下跌时才有利润，其利润正好和平多相反
        // 利润 = 买空均价*平空数量 - 平空价格*平空数量(order.total()) - 手续费
        var cf = closeFee(order.orderItem.volume);
        var benefit = (sellPrice*order.orderItem.volume) -order.total() - cf;
        closeSellFee += cf;
        if (order.orderItem.volume == sellVol) {
            // 全平
            sellVol = 0;
            sellPrice = 0;
            // 全平清仓才计算利润
            sellProfile += benefit;
            return benefit;
        }

        sellVol -= order.orderItem.volume;
        // 空头势能越高越有利，做空时点位高则势能高
        // 当前均价 = 当前空头势能/当前持仓数量
        // 当前空头势能 = 剩余数量*平仓前均价 + 利润
        sellPrice = (sellVol * sellPrice+benefit)/sellVol;
        return benefit;
    }

}
