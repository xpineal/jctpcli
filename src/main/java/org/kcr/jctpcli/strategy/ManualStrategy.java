package org.kcr.jctpcli.strategy;

import org.kcr.jctpcli.cnf.Cmd;
import org.kcr.jctpcli.cnf.FJson;
import org.kcr.jctpcli.env.*;

public class ManualStrategy implements IStrategy {
    public TradeCmd makeDecision(RunData runData) {
        // 读取外部输入
        var cmd = FJson.readCmdThenReset("/home/kky/workspace/ctp-trade.json");
        switch (cmd.mode){
            case Cmd.OpenBuy:
                return new TradeCmd(OrderItem.MakeOrderItems(1, cmd.price, Direction.OpenBuy), null);
            case Cmd.OpenSell:
                return new TradeCmd(OrderItem.MakeOrderItems(1, cmd.price, Direction.OpenSell), null);
            case Cmd.CloseBuy:
                return new TradeCmd(OrderItem.MakeOrderItems(1, cmd.price, Direction.CloseBuy), null);
            case Cmd.CloseSell:
                return new TradeCmd(OrderItem.MakeOrderItems(1, cmd.price, Direction.CloseSell), null);
            case Cmd.Recall:
                return new TradeCmd(null, runData.orders);
        }
        return new TradeCmd(null, null);
    }
}
