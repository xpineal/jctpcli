package org.kcr.jctpcli.env;

import org.kcr.jctpcli.md.MarketData;
import org.kcr.jctpcli.trader.ITrader;
import org.kcr.jctpcli.trader.TraderCall;

public class Broker {
    // trade caller
    private ITrader traderCall;

    // strategy
    private IStrategy strategy;

    // 包装订单跟踪，持仓情况，资金和合约
    public Hold hold;

    public Broker(ITrader _traderCall, IStrategy _strategy, Hold _hold) {
        traderCall = _traderCall;
        strategy = _strategy;
        hold = _hold;
    }

    // 使用Hold的lock
    public void lock() {
        hold.lock();
    }

    // 使用Hold的lock
    public void unlock() {
        hold.unlock();
    }

    // 执行开多
    public ExeRet executeOpenBuy(double price, int volume) {
        if (hold.canOpenBuy(hold.instrument, price, volume)) {
            if (traderCall.openBuy(hold.instrument, price, volume)) {
                hold.available -= hold.instrument.openBuyCost(price, volume);
                return ExeRet.OK;
            }
            return ExeRet.Fail;
        }
        return ExeRet.NoMoney;
    }

    // 执行开空
    public ExeRet executeOpenSell(double price, int volume) {
        if (hold.canOpenSell(hold.instrument, price, volume)) {
            if (traderCall.openSell(hold.instrument, price, volume)) {
                hold.available -= hold.instrument.openSellCost(price, volume);
                return ExeRet.OK;
            }
            return ExeRet.Fail;
        }
        return ExeRet.NoMoney;
    }

    // 执行平多
    public ExeRet executeCloseBuy(double price, int volume) {
        if (hold.canCloseBuy(hold.instrument, volume)) {
            if (traderCall.closeBuy(hold.instrument, price, volume)) {
                hold.available -= hold.instrument.closeFee(volume);
                return ExeRet.OK;
            }
            return ExeRet.Fail;
        }
        return ExeRet.NoMoney;
    }

    public ExeRet executeCloseSell(double price, int volume) {
        if (hold.canCloseSell(hold.instrument, volume)) {
            if (traderCall.closeSell(hold.instrument, price, volume)) {
                hold.available -= hold.instrument.closeFee(volume);
                return ExeRet.OK;
            }
            return ExeRet.Fail;
        }
        return ExeRet.NoMoney;
    }

    public void marketProcess(MarketData md) {
        var cmd = strategy.makeDecision(
                new RunData(hold.instrument, md, hold.orderTracker.allOrders(), hold.available));

        if (Parameter.debugMode) {
            System.out.println("行情信息:");
            System.out.println(md.brief());
            System.out.println("决策信息:");
            System.out.println(cmd.brief());
        }

        var recallOrders = cmd.recallOrders;
        if (recallOrders != null) {
            // 撤单
            for (OrderInfo order:recallOrders) {
                traderCall.cancelOrder(order.orderRef);
            }
        }

        var exeOrders = cmd.exeOrders;
        if (exeOrders != null) {
            // 下单
            for (OrderItem order:exeOrders) {
                var exeRet = executeOrder(order);
                if (exeRet == ExeRet.Fail) {
                    System.out.println("------------------可用资金不足-------------");
                }
                if (exeRet == ExeRet.Fail) {
                    System.out.println("------------------下单失败-------------");
                }
            }
        }
    }

    public ExeRet executeOrder(OrderItem order) {
        switch (order.direction) {
            case OpenBuy:
                return executeOpenBuy(order.price, order.volume);
            case OpenSell:
                return executeOpenSell(order.price, order.volume);
            case CloseBuy:
                return executeCloseBuy(order.price, order.volume);
            case CloseSell:
                return executeCloseSell(order.price, order.volume);
        }
        return ExeRet.Fail;
    }

}
