package org.kcr.jctpcli.core;

public interface IStrategy {
    // 决定交易指令
    TradeCmd makeDecision(RunData runData);
}
