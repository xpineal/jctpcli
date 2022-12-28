package org.kcr.jctpcli.core;

import org.kr.jctp.CThostFtdcInvestorPositionField;

// 持仓信息 -- 某个方向
public class Hold {
    public int vol = 0; //数量
    public double price = 0; //价格(不计算手续费)
    public double marginPrice = 0; //保证金价格
    public double profile = 0; //利润
    public double openTotalFee = 0; //开手续费总和
    public double closeTotalFee = 0; //平手续费总和

    public Hold() {
        vol = 0;
        price = 0;
        marginPrice = 0;
        profile = 0;
        openTotalFee = 0;
        closeTotalFee = 0;
    }

    @Override
    public String toString() {
        var sb = new StringBuffer(128);
        sb.append("数量:").append(vol).append(",");
        sb.append("价格:").append(price).append(",");
        sb.append("保证金价格:").append(marginPrice).append(",");
        sb.append("利润:").append(profile).append(",");
        sb.append("开手续费:").append(openTotalFee).append(",");
        sb.append("平手续费:").append(closeTotalFee);
        return sb.toString();
    }

    // 不含手续费的总价格
    public double totalPrice() {
        return price * vol;
    }

    public double totalMargin() {
        return marginPrice * vol;
    }

    public void feed(Instrument instrument, CThostFtdcInvestorPositionField pInvestorPosition, boolean openBuy) {
        var openVol = pInvestorPosition.getPosition();
        if (openVol > 0) {
            vol += openVol;
            price = pInvestorPosition.getOpenCost()/(vol*instrument.volumeMultiple);
            if (openBuy) {
                marginPrice = instrument.buyMargin(price, 1);
            }else {
                marginPrice = instrument.sellMargin(price, 1);
            }

            openTotalFee += instrument.openFee(price, vol);
        }
    }

    // orderPrice -- 订单总价格
    // feePrice -- 手续费价格
    public boolean addVol(int dVol, double orderPrice, double dMargin, double openFee) {
        var totalVtPrice = totalPrice() + orderPrice;
        var totalMargin = totalMargin() + dMargin;
        vol += dVol;
        openTotalFee += openFee;
        if (vol > 0) {
            price = totalVtPrice/vol;
            marginPrice = totalMargin/vol;
            return true;
        }
        return false;
    }

    public double reduceVol(int dVol, double dProfile, double openFee, double closeFee) {
        // 累计平仓手续费
        closeTotalFee += closeFee;
        // 计算系统返还的保证金
        var retMargin = marginPrice * dVol;
        // 累计利润
        profile += dProfile - closeFee - openFee;
        // 减少数量
        vol -= dVol;
        // 返回可用资金增量 = 返还的保证金+利润
        return retMargin + dProfile;
    }
}
