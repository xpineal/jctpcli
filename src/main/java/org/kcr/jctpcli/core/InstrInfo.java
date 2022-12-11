package org.kcr.jctpcli.core;

public class InstrInfo {
    public String exchangeID;
    public String instrumentID;
    public double longMarginRatio;
    public double shortMarginRatio;
    public double priceTick;
    public int volumeMultiple;

    public InstrInfo(String ecID, String insID, double lRatio, double shRatio, double pTick, int vMul) {
        exchangeID = ecID;
        instrumentID = insID;
        longMarginRatio = lRatio;
        shortMarginRatio = shRatio;
        priceTick = pTick;
        volumeMultiple = vMul;
    }
}
