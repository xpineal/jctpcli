package org.kcr.jctpcli.core;

// 此类仅供测试使用
public class InstrInfo {
    public String exchangeID;
    public String instrumentID;
    public double longMarginRatio;
    public double shortMarginRatio;
    public double priceTick;
    public int volumeMultiple;

    // 开手续费率
    public double openRatioByVolume;
    // 平手续费率
    public double closeRatioByVolume;
    // 开手续费
    public double openRatioByMoney;
    // 平手续费
    public double closeRatioByMoney;

    // 平今手续费率
    public double closeTodayRatioByVolume;
    // 平今手续费
    public double closeTodayRatioByMoney;


    public InstrInfo(String ecID, String insID, double lRatio, double shRatio, double pTick, int vMul) {
        exchangeID = ecID;
        instrumentID = insID;
        longMarginRatio = lRatio;
        shortMarginRatio = shRatio;
        priceTick = pTick;
        volumeMultiple = vMul;

        openRatioByVolume = 0;
        openRatioByMoney = 0;
        closeRatioByVolume = 0;
        closeRatioByMoney = 0;
        closeTodayRatioByVolume = 0;
        closeTodayRatioByMoney = 0;
    }
}
