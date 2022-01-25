package org.kcr.jctpcli.env;

import org.jetbrains.annotations.NotNull;
import org.kr.jctp.CThostFtdcDepthMarketDataField;
import org.kr.jctp.CThostFtdcInvestorPositionField;
import org.kr.jctp.jctpConstants;

import java.util.concurrent.atomic.AtomicInteger;

//合约信息
public class Instr {
    public Instr(String id, String exchangeID) {
        this.id = id;
        this.exchangeID = exchangeID;
        this.CLong = 0;
        this.CShort = 0;
        this.CLongYsd = 0;
        this.CShortYsd = 0;
    }

    public String id;
    public String exchangeID;
    //买1
    public double buyPrice;
    //卖1
    public double askPrice;
    //最新价
    public double price;

    //买多单的量
    public int CLong;
    //买空单的量
    public int CShort;
    //昨日买多单的量
    public int CLongYsd;
    //昨日买空单的量
    public int CShortYsd;


    //填充启动式持仓信息
    public void feedPosition(@NotNull CThostFtdcInvestorPositionField pInfo) {
        var posDir = pInfo.getPosiDirection();
        var pDate = pInfo.getPositionDate();
        var vol = pInfo.getPosition();
        if (posDir == jctpConstants.THOST_FTDC_PD_Long) {
            if (pDate == jctpConstants.THOST_FTDC_PSD_Today) {
                this.CLongYsd += vol;
            }else{
                this.CLongYsd += vol;
            }
        }
        if (posDir == jctpConstants.THOST_FTDC_PD_Short) {
            if (pDate == jctpConstants.THOST_FTDC_PSD_Today) {
                this.CShort += vol;
            }else{
                this.CShortYsd = vol;
            }
        }
    }

    //填充行情
    public void feedMarket(@NotNull CThostFtdcDepthMarketDataField pInfo) {
        buyPrice = pInfo.getBidPrice1();
        askPrice = pInfo.getAskPrice1();
        price = pInfo.getLastPrice();
    }

    //clone
    public Instr clone() {
        var c = new Instr(id, exchangeID);
        c.buyPrice = buyPrice;
        c.askPrice = askPrice;
        c.price = price;

        c.CLongYsd = CLongYsd;
        c.CLong = CLong;
        c.CShortYsd = CShortYsd;
        c.CShort = CShort;
        return c;
    }
}
