package org.kcr.jctpcli.cnf;

public class Cmd {
    // 什么也不做
    public static final int NoMode = 0;
    // 1-开多
    public static final int OpenBuy = 1;
    // 2-开空
    public static final int OpenSell = 2;
    // 3-平多
    public static final int CloseBuy = 3;
    // 4-平空
    public static final int CloseSell = 4;
    // 5-撤单
    public static final int Recall = 5;

    // 6-打印持仓和可用资金
    public static final int Print = 6;

    public int mode;
    public double price;

    public Cmd() {
        mode = NoMode;
        price = 0;
    }

    @Override
    public String toString() {
        var sb = new StringBuffer(64);
        sb.append("mode:").append(mode).append(",").append("price:").append(price);
        return sb.toString();
    }
}
