package org.kcr.jctpcli.env;

import org.kr.jctp.CThostFtdcInstrumentCommissionRateField;
import org.kr.jctp.CThostFtdcInstrumentField;

// 合约信息
public class Instrument {
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

    public void outPut() {
        System.out.printf("合约乘数 : %d ; 最小变动价格 : %f\n", volumeMultiple, priceTick);
    }
}
