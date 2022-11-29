package org.kcr.jctpcli.env;

public class Strategy {
    // 决定交易指令
    public static TradeCmd makeDecision(RunData runData) {
        // 先填充什么也不做的交易指令
        return new TradeCmd(null, null);
    }
}
