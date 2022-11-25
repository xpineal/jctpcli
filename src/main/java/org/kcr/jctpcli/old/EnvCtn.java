package org.kcr.jctpcli.old;

import org.kcr.jctpcli.trader.TraderCall;
import org.kr.jctp.CThostFtdcOrderField;
import org.kr.jctp.CThostFtdcTradeField;
import org.kr.jctp.jctpConstants;

public class EnvCtn {

    private TraderCall traderCall;

    // 未成交撤回回报
    public void orderCancel(CThostFtdcOrderField pInfo) {
//        // TODO: 重要的几个数据
//        // 原始报单数量
//        // int volumeTotalOriginal= pInfo.getVolumeTotalOriginal();
//        // 成交数量
//        // int volumeTreaed = pInfo.getVolumeTraded();
//        // 剩余数量
//        int volumeTotal = pInfo.getVolumeTotal();
//        // 多空方向
//        char direction = pInfo.getDirection();
//        // 开平标记(开仓和平仓)
//        String combOffsetFLag = pInfo.getCombOffsetFlag();
//        // 下单价格
//        double limitPrice = pInfo.getLimitPrice();
//
//        if (combOffsetFLag.equals(Character.toString(jctpConstants.THOST_FTDC_OF_Open)) == false) {// 平仓
//            if (direction == jctpConstants.THOST_FTDC_D_Buy) {// 平空
//                traderCall.closeSell(limitPrice + 10 * Prameter.priceTick, volumeTotal, combOffsetFLag);
//            } else {// 平多
//                traderCall.closeBuy(limitPrice - 10 * Prameter.priceTick, volumeTotal, combOffsetFLag);
//            }
//        }

    }

    // 成交回报
    public void orderTrade(CThostFtdcTradeField pInfo) {
//        // TODO: 重要的数据
//        // 价格
//        var price = pInfo.getPrice();
//        // 数量
//        var vol = pInfo.getVolume();
//        // 多空方向
//        var dir = pInfo.getDirection();
//        // 开平标记(开仓和平仓)
//        var offset = pInfo.getOffsetFlag();
//
//        System.out.println("dir : " + dir + "  offset : " + offset);
//
//        if (offset == jctpConstants.THOST_FTDC_OF_Open) {// 开仓
//            if (dir == jctpConstants.THOST_FTDC_D_Buy) {// 开多
//                Prameter.buyCount += vol;
//                Prameter.available -= vol
//                        * (price * Prameter.volumeMultiple * Prameter.longMarginRatio + Prameter.openRatioByVolume);
//            } else {// 开空
//                Prameter.sellCount += vol;
//                Prameter.available -= vol * (price * Prameter.volumeMultiple * Prameter.shortMarginRatio
//                        + Prameter.openRatioByVolume);
//            }
//        } else {// 平仓
//            if (dir == jctpConstants.THOST_FTDC_D_Buy) {// 平空
//                Prameter.sellCount -= vol;
//                Prameter.available += vol * (price * Prameter.volumeMultiple * Prameter.longMarginRatio
//                        - Prameter.closeRatioByVolume);
//            } else {// 平多
//                Prameter.buyCount -= vol;
//                Prameter.available += vol * (price * Prameter.volumeMultiple * Prameter.shortMarginRatio
//                        - Prameter.closeRatioByVolume);
//            }
//        }
//
//        Prameter.preBuyCount = Prameter.buyCount;
//        Prameter.preSellCount = Prameter.sellCount;
//
//        System.out.println("buyCOunt : " + Prameter.buyCount + "  sellCount : " + Prameter.sellCount);
//        System.out.println("preBuyCOunt : " + Prameter.preBuyCount + "  preSellCount : " + Prameter.preSellCount);

    }
}
