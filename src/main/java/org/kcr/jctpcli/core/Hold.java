package org.kcr.jctpcli.core;

// 持仓信息 -- 某个方向
public class Hold {
    public int vol = 0; //数量
    public double price = 0; //实际价格
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

    public double totalPrice() {
        return price * vol;
    }

    public double totalMargin() {
        return marginPrice * vol;
    }

    public boolean addVol(int dVol, double dPrice, double dMargin, double openFee) {
        vol += dVol;
        openTotalFee += openFee;
        var totalPrice = totalPrice() + dPrice;
        var totalMargin = totalMargin() + dMargin;
        if (vol > 0) {
            price = totalPrice/vol;
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
        profile += dProfile - closeFee;
        // 减少数量
        vol -= dVol;
        // 返回可用资金增量 = 返还的保证金+开手续费(手续费计入到价格中了，这里需要加回来)
        return retMargin + dProfile + openFee;
    }
}
