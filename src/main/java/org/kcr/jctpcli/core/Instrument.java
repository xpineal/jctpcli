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

    // 合约持仓信息
    // 多头持仓相关信息
    public Hold buyHold = new Hold();
    // 空头持仓相关信息
    public Hold sellHold = new Hold();

    public Instrument(String _exchangeID, String _instrumentID) {
        exchangeID = _exchangeID;
        instrumentID = _instrumentID;
        openRatioByMoney = 0;
        openRatioByVolume = 0;
        closeRatioByMoney = 0;
        closeRatioByVolume = 0;
        closeTodayRatioByMoney = 0;
        closeTodayRatioByVolume = 0;
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
        var exchangeID = pInstrument.getExchangeID();

        if (exchangeID.equals("CFFEX")) {
            // 多头保证金率
            longMarginRatio = pInstrument.getLongMarginRatio();
            // 空头保证金率
            shortMarginRatio = pInstrument.getShortMarginRatio();
        }else {
            // 多头保证金率
            longMarginRatio = pInstrument.getLongMarginRatio() + addMargin;
            // 空头保证金率
            shortMarginRatio = pInstrument.getShortMarginRatio() + addMargin;
        }

        // 合约乘数
        volumeMultiple = pInstrument.getVolumeMultiple();
        // 合约最小变动价格
        priceTick = pInstrument.getPriceTick();
        if (Parameter.debugMode) {
            System.out.printf("设置合约相关费率:%s -- long margin ratio:%f -- short margin ratio:%f volume multiple:%d\n",
                    instrumentID, longMarginRatio, shortMarginRatio, volumeMultiple);
        }
    }

    public void setInstrumentRatio(InstrInfo pInstrument) {
        // 额外的手续费从API中拿不到，需要从配置读取
        var addMargin = Parameter.cnf.getAddMargin();

        if (pInstrument.exchangeID.equals("CFFEX")) {
            // 多头保证金率
            longMarginRatio = pInstrument.longMarginRatio;
            // 空头保证金率
            shortMarginRatio = pInstrument.shortMarginRatio;
        }else {
            // 多头保证金率
            longMarginRatio = pInstrument.longMarginRatio + addMargin;
            // 空头保证金率
            shortMarginRatio = pInstrument.shortMarginRatio + addMargin;
        }

        // 合约乘数
        volumeMultiple = pInstrument.volumeMultiple;
        // 合约最小变动价格
        priceTick = pInstrument.priceTick;
        if (Parameter.debugMode) {
            System.out.printf("设置合约相关费率:%s -- long margin ratio:%f -- short margin ratio:%f volume multiple:%d\n",
                    pInstrument.instrumentID, longMarginRatio, shortMarginRatio, volumeMultiple);
        }
    }

    public void setOpenCloseRatio(CThostFtdcInstrumentCommissionRateField tc) {
        var openRatioByVol = tc.getOpenRatioByVolume();
        var openRatioByMo = tc.getOpenRatioByMoney();
        var closeRatioByVol = tc.getCloseRatioByVolume();
        var closeRatioByMo = tc.getCloseRatioByMoney();
        var closeTodayRatioByVol = tc.getCloseTodayRatioByVolume();
        var closeTodayRatioByMo = tc.getCloseTodayRatioByMoney();

        if (openRatioByVol > 0) {
            openRatioByVolume = openRatioByVol;
        }
        if (openRatioByMo > 0) {
            openRatioByMoney = openRatioByMo;
        }
        if (closeRatioByVol > 0) {
            closeRatioByVolume = closeRatioByVol;
        }
        if (closeRatioByMo > 0) {
            closeRatioByMoney = closeRatioByMo;
        }
        if (closeTodayRatioByVol > 0) {
            closeTodayRatioByVolume = closeTodayRatioByVol;
        }
        if (closeTodayRatioByMo > 0) {
            closeTodayRatioByMoney = closeTodayRatioByMo;
        }
    }

    public void setOpenCloseRatio(InstrInfo inst) {
        if (inst.openRatioByVolume > 0) {
            openRatioByVolume = inst.openRatioByVolume;
        }
        if (inst.openRatioByMoney > 0) {
            openRatioByMoney = inst.openRatioByMoney;
        }
        if (inst.closeRatioByVolume > 0) {
            closeRatioByVolume = inst.closeRatioByVolume;
        }
        if (inst.closeRatioByMoney > 0) {
            closeRatioByMoney = inst.closeRatioByMoney;
        }
        if (inst.closeTodayRatioByVolume > 0) {
            closeTodayRatioByVolume = inst.closeTodayRatioByVolume;
        }
        if (inst.closeTodayRatioByMoney > 0) {
            closeTodayRatioByMoney = inst.closeTodayRatioByMoney;
        }
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
        return price * volumeMultiple * longMarginRatio * volume;
    }

    // 开多平多保证金
    public double buyMargin(OrderItem order) {
        return buyMargin(order.price, order.volume);
    }

    // 做空平空保证金
    public double sellMargin(double price, int volume) {
        return price * volumeMultiple * shortMarginRatio*volume;
    }

    // 做空平空保证金
    public double sellMargin(OrderItem order) {
        return sellMargin(order.price, order.volume);
    }

    // 利润
    public double profile(double priceDelta, int volume) {
        return priceDelta * volumeMultiple * volume;
    }

    // 开仓手续费
    public double openFee(double price, int volume) {
        if (openRatioByVolume > 0) {
            return volume * openRatioByVolume;
        }
        return price * volumeMultiple * openRatioByMoney * volume;
    }

    // 开仓手续费
    public double openFee(OrderItem order) {
        return openFee(order.price, order.volume);
    }

    // 开仓手续费转化成价格
    public double openFeePrice(OrderItem order) {
        return openFee(order) / volumeMultiple;
    }

    public double openFeePrice(double price, int volume) {
        return openFee(price, volume) / volumeMultiple;
    }

    // 开多费用(保证金和手续费)
    public double openBuyCost(double price, int volume) {
        return buyMargin(price, volume) + openFee(price, volume);
    }

    public double openBuyCost(OrderItem order) {
        return buyMargin(order) + openFee(order);
    }

    public double openSellCost(double price, int volume) {
        return sellMargin(price, volume) + openFee(price, volume);
    }

    public double openSellCost(OrderItem order) {
        return sellMargin(order) + openFee(order);
    }

    // 平仓手续费
    public double closeFee(double price, int volume) {
        if (closeTodayRatioByVolume > 0) {
            return volume * closeTodayRatioByVolume;
        }
        if (closeTodayRatioByMoney > 0) {
            return price * volumeMultiple * closeTodayRatioByMoney * volume;
        }
        if (closeRatioByVolume > 0) {
            return volume * closeRatioByVolume;
        }
        return price * volumeMultiple * closeRatioByMoney * volume;
    }

    // 平仓手续费
    public double closeFee(OrderItem order) {
        return closeFee(order.price, order.volume);
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
        if (!buyHold.addVol(order.orderItem.volume,
                order.total(), buyMargin(order.orderItem), openFee(order.orderItem))){
            System.out.printf("错误的开多订单信息:%s\n", order);
        }
    }

    // 开空
    private void OnOpenSell(OrderInfo order) {
        // 实际的价格需要考虑手续费
        if (!sellHold.addVol(order.orderItem.volume,
                order.total(), sellMargin(order.orderItem), openFee(order.orderItem))){
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
                openFee(buyHold.price, order.orderItem.volume), closeFee(order.orderItem));
    }

    // 平空 -- 返回可用资金增量(包括返还的保证金+利润+开平手续费)
    private double OnCloseSell(OrderInfo order) {
        if (order.orderItem.volume > sellHold.vol) {
            System.out.printf("错误的平空订单信息:%s\n", order);
            return 0;
        }
        return sellHold.reduceVol(order.orderItem.volume,
                profile(sellHold.price -order.orderItem.price, order.orderItem.volume),
                openFee(sellHold.price, order.orderItem.volume), closeFee(order.orderItem));
    }
}
