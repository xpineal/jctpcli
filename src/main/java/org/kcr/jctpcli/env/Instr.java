package org.kcr.jctpcli.env;

import org.jetbrains.annotations.NotNull;
import org.kr.jctp.CThostFtdcDepthMarketDataField;
import org.kr.jctp.CThostFtdcInvestorPositionField;
import org.kr.jctp.jctpConstants;

//合约信息
public class Instr {
    public String id;
    public String exchangeID;
    //买1
    public double buyPrice;
    //卖1
    public double sellPrice;
    //最新价
    public double price;

    //开多单的量
    public int CLong;
    //开空单的量
    public int CShort;
    //往日开多单的量
    public int CLongYsd;
    //往日开空单的量
    public int CShortYsd;

    public Instr(String id, String exchangeID) {
        this.id = id;
        this.exchangeID = exchangeID;
        this.CLong = 0;
        this.CShort = 0;
        this.CLongYsd = 0;
        this.CShortYsd = 0;
    }

    //填充启动式持仓信息
    public void feedPosition(@NotNull CThostFtdcInvestorPositionField pInfo) {
        var posDir = pInfo.getPosiDirection();
        var pDate = pInfo.getPositionDate();
        var vol = pInfo.getPosition();
        if (posDir == jctpConstants.THOST_FTDC_PD_Long) {
            if (pDate == jctpConstants.THOST_FTDC_PSD_Today) {
                this.CLong += vol;
            }else{
                this.CLongYsd += vol;
            }
        }
        if (posDir == jctpConstants.THOST_FTDC_PD_Short) {
            if (pDate == jctpConstants.THOST_FTDC_PSD_Today) {
                this.CShort += vol;
            }else{
                this.CShortYsd += vol;
            }
        }
    }

    //填充行情
    public void feedMarket(@NotNull CThostFtdcDepthMarketDataField pInfo) {
        buyPrice = pInfo.getBidPrice1();
        sellPrice = pInfo.getAskPrice1();
        price = pInfo.getLastPrice();
    }

    //减多单
    public void decOpenBuyNum(int vol) {
        //多单数量减
        //平仓时如果有昨日的持仓，优先扣除昨日持仓
        if (CLongYsd > 0) {
            var delta = vol - CLongYsd;
            if (delta > 0) {
                CLongYsd = 0;
                CLong -= delta;
            }else{
                CLongYsd -= vol;
            }
        }else{
            CLong -= vol;
        }
    }

    //减空单
    public void decOpenSellNum(int vol) {
        //空单数量减
        //平仓时如果有昨日的持仓，优先扣除昨日持仓
        if (CShortYsd > 0) {
            var delta = vol - CShortYsd;
            if (delta > 0) {
                CShortYsd = 0;
                CShort -= delta;
            }else{
                CShortYsd -= vol;
            }
        }else{
            CShort -= vol;
        }
    }

    //clone
    public Instr clone() {
        var c = new Instr(id, exchangeID);
        c.buyPrice = buyPrice;
        c.sellPrice = sellPrice;
        c.price = price;

        c.CLongYsd = CLongYsd;
        c.CLong = CLong;
        c.CShortYsd = CShortYsd;
        c.CShort = CShort;
        return c;
    }
}
