package org.kcr.jctpcli.strategy;

import org.kcr.jctpcli.env.IStrategy;
import org.kcr.jctpcli.env.RunData;
import org.kcr.jctpcli.env.TradeCmd;

public class EmptyStrategy implements IStrategy {
    public TradeCmd makeDecision(RunData runData) {
        // 先填充什么也不做的交易指令
        return new TradeCmd(null, null);
    }
}
