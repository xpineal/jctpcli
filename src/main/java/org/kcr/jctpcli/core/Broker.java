package org.kcr.jctpcli.core;

public class Broker {
    // trade caller
    private ITrader traderCall;

    // strategy
    private IStrategy strategy;

    // 包装订单跟踪，持仓情况，资金和合约
    public Hand hand;

    public Broker(ITrader _traderCall, IStrategy _strategy, Hand _hand) {
        traderCall = _traderCall;
        strategy = _strategy;
        hand = _hand;
    }

    // 使用Hold的lock
    public void lock() {
        hand.lock();
    }

    // 使用Hold的lock
    public void unlock() {
        hand.unlock();
    }

    // 执行开多
    public ExeRet executeOpenBuy(Instrument instrument, OrderItem order) {
        if (hand.canOpenBuy(instrument, order)) {
            if (traderCall.openBuy(instrument, order.price, order.volume)) {
                hand.available -= instrument.openBuyCost(order);
                return ExeRet.OK;
            }
            return ExeRet.Fail;
        }
        return ExeRet.NoMoney;
    }

    public ExeRet executeOpenBuy(Instrument instrument, int volume, double price) {
        var order = new OrderItem(volume, price, instrument.instrumentID);
        return executeOpenBuy(instrument, order);
    }

    // 执行开空
    public ExeRet executeOpenSell(Instrument instrument, OrderItem order) {
        if (hand.canOpenSell(instrument, order)) {
            if (traderCall.openSell(instrument, order.price, order.volume)) {
                hand.available -= instrument.openSellCost(order);
                return ExeRet.OK;
            }
            return ExeRet.Fail;
        }
        return ExeRet.NoMoney;
    }

    public ExeRet executeOpenSell(Instrument instrument, int volume, double price) {
        var order = new OrderItem(volume, price, instrument.instrumentID);
        return executeOpenSell(instrument, order);
    }

    // 执行平多
    public ExeRet executeCloseBuy(Instrument instrument, OrderItem order) {
        if (hand.canCloseBuy(instrument, order)) {
            if (traderCall.closeBuy(instrument, order.price, order.volume)) {
                hand.available -= instrument.closeFee(order);
                return ExeRet.OK;
            }
            return ExeRet.Fail;
        }
        return ExeRet.NoMoney;
    }

    public ExeRet executeCloseBuy(Instrument instrument, int volume, double price) {
        var order = new OrderItem(volume, price, instrument.instrumentID);
        return executeCloseBuy(instrument, order);
    }

    public ExeRet executeCloseSell(Instrument instrument, OrderItem order) {
        if (hand.canCloseSell(instrument, order)) {
            if (traderCall.closeSell(instrument, order.price, order.volume)) {
                hand.available -= instrument.closeFee(order);
                return ExeRet.OK;
            }
            return ExeRet.Fail;
        }
        return ExeRet.NoMoney;
    }

    public ExeRet executeCloseSell(Instrument instrument, int volume, double price) {
        var order = new OrderItem(volume, price, instrument.instrumentID);
        return executeCloseSell(instrument, order);
    }

    public void marketProcess(MarketData md) {
        var instrument = hand.getInstrument(md.instrumentID);
        if (instrument == null) {
            System.out.printf("出现了没有合约信息的行情:%s\n", md.instrumentID);
            return;
        }
        var cmd = strategy.makeDecision(
                new RunData(instrument, md, hand.orderTracker.allOrders(), hand.available));

        if (Parameter.debugMode) {
            /*System.out.println("决策信息:");
            if (cmd == null) {
                System.out.println("决策信息为空");
            }else{
                System.out.println(cmd.brief());
            }*/
        }

        if (cmd == null) {
            System.out.println("决策不应该为空引用");
            return;
        }

        if (cmd.printInfo) {
            //只做打印
            System.out.printf("可用资金:%f", hand.available);
            return;
        }

        var recallOrders = cmd.recallOrders;
        if (recallOrders != null) {
            // 撤单
            for (OrderInfo order:recallOrders) {
                var instr = hand.getInstrument(order.orderItem.instrumentID);
                if (instr == null) {
                    System.out.printf("撤销订单但拿不到合约信息:%s\n", order.orderItem.instrumentID);
                    continue;
                }
                traderCall.cancelOrder(order, instr.exchangeID);
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
        var instrument = hand.getInstrument(order.instrumentID);
        if (instrument == null) {
            System.out.printf("找不到对应的合约id:%s\n", order.instrumentID);
            return ExeRet.Fail;
        }
        switch (order.direction) {
            case OpenBuy:
                return executeOpenBuy(instrument, order);
            case OpenSell:
                return executeOpenSell(instrument, order);
            case CloseBuy:
                return executeCloseBuy(instrument, order);
            case CloseSell:
                return executeCloseSell(instrument, order);
        }
        return ExeRet.Fail;
    }

}
