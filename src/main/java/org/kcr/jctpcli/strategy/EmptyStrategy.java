package org.kcr.jctpcli.strategy;

import org.kcr.jctpcli.core.IStrategy;
import org.kcr.jctpcli.core.RunData;
import org.kcr.jctpcli.core.TradeCmd;

public class EmptyStrategy implements IStrategy {
    public TradeCmd makeDecision(RunData runData) {
        // 先填充什么也不做的交易指令
        return new TradeCmd(null, null);
    }
}
