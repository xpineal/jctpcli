package org.kcr.jctpcli.strategy;

import org.kcr.jctpcli.core.*;
import org.kcr.jctpcli.cnf.Cmd;
import org.kcr.jctpcli.cnf.FJson;

public class ManualStrategy implements IStrategy {
    public TradeCmd makeDecision(RunData runData) {
        // 读取外部输入
        var cmd = FJson.readCmdThenReset("/home/builder/java/runv2/ctp-trade.json");
        if (cmd == null) {
            //System.out.println("手动下单输入为空");
            return new TradeCmd(null, null);
        }else{
            System.out.println(cmd);
        }

        System.out.print("手动下单输入 ------ :");
        switch (cmd.mode){
            case Cmd.OpenBuy:
                System.out.println("开多");
                return new TradeCmd(OrderItem.MakeOrderItems(1, cmd.price,
                        runData.instrument.exchangeID, runData.instrument.instrumentID, Direction.OpenBuy), null);
            case Cmd.OpenSell:
                System.out.println("开空");
                return new TradeCmd(OrderItem.MakeOrderItems(1, cmd.price,
                        runData.instrument.exchangeID, runData.instrument.instrumentID, Direction.OpenSell), null);
            case Cmd.CloseBuy:
                System.out.println("平多");
                return new TradeCmd(OrderItem.MakeOrderItems(1, cmd.price,
                        runData.instrument.exchangeID, runData.instrument.instrumentID, Direction.CloseBuy), null);
            case Cmd.CloseSell:
                System.out.println("平空");
                return new TradeCmd(OrderItem.MakeOrderItems(1, cmd.price,
                        runData.instrument.exchangeID, runData.instrument.instrumentID, Direction.CloseSell), null);
            case Cmd.Recall:
                System.out.println("撤单");
                return new TradeCmd(null, runData.orders);
            case Cmd.Print:
                System.out.println("打印");
                return new TradeCmd(null, null, true);
        }
        System.out.println("空包");
        return new TradeCmd(null, null);
    }
}
