package org.kcr.jctpcli.env;

public interface IStrategy {
    // 决定交易指令
    public TradeCmd makeDecision(RunData runData);
}
