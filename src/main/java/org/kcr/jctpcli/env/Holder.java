package org.kcr.jctpcli.env;

public class Holder {
    // 多头持仓和价格
    public int buyVol;
    public double buyPrice;

    // 空头持仓和价格
    public int sellVol;
    public double sellPrice;

    // 多单利润
    public double buyProfile;
    // 空单利润
    public double sellProfile;

    public Holder() {
        buyVol = 0;
        buyPrice = 0;
        sellVol = 0;
        sellPrice = 0;
        buyProfile = 0;
        sellProfile = 0;
    }

    // 处理订单成交回包
    public void OnOrderTrade(OrderInfo order) {
        switch (order.orderItem.direction) {
            case OpenBuy:
                OnOpenBuy(order);
                break;
            case OpenSell:
                OnOpenSell(order);
                break;
            case CloseBuy:
                OnCloseBuy(order);
                break;
            case CloseSell:
                OnCloseSell(order);
                break;
        }
    }

    // 开多
    private void OnOpenBuy(OrderInfo order) {
        var total = buyPrice*buyVol + order.total();
        buyVol += order.orderItem.volume;
        if (buyVol > 0) {
            buyPrice = total/buyVol;
        }else {
            System.out.printf("错误的开多订单信息:%s\n", order.ToString());
        }
    }

    // 开空
    private void OnOpenSell(OrderInfo order) {
        var total = sellPrice*sellVol + order.total();
        sellVol += order.orderItem.volume;
        if (sellVol > 0) {
            sellPrice = total/sellVol;
        }else {
            System.out.printf("错误的开空订单信息:%s\n", order.ToString());
        }
    }

    // 平多
    private double OnCloseBuy(OrderInfo order) {
        if (order.orderItem.volume > buyVol) {
            System.out.printf("错误的平多订单信息:%s\n", order.ToString());
            buyVol = 0;
            buyPrice = 0;
            return 0;
        }
        // 计算获利
        // 利润 = 平多价格*平多数量(order.total()) - 买多均价*平多数量
        var benefit = order.total() - (buyPrice*order.orderItem.volume);
        buyProfile += benefit;
        if (order.orderItem.volume == buyVol) {
            // 全平
            buyVol = 0;
            buyPrice = 0;
            return benefit;
        }

        buyVol -= order.orderItem.volume;
        // 当前均价 = 当前持仓成本/当前持仓数量
        // 当前持仓成本 = 剩余数量*平仓前均价 - 利润
        buyPrice = (buyVol * buyPrice-benefit)/buyVol;
        return benefit;
    }

    // 平空
    private double OnCloseSell(OrderInfo order) {
        if (order.orderItem.volume > sellVol) {
            System.out.printf("错误的平空订单信息:%s\n", order.ToString());
            sellVol = 0;
            sellPrice = 0;
            return 0;
        }
        // 计算获利，平空只有在价格下跌时才有利润，其利润正好和平多相反
        // 利润 = 买空均价*平空数量 - 平空价格*平空数量(order.total())
        var benefit = (sellPrice*order.orderItem.volume) -order.total();
        sellProfile += benefit;
        if (order.orderItem.volume == sellVol) {
            // 全平
            sellVol = 0;
            sellPrice = 0;
            return benefit;
        }

        sellVol -= order.orderItem.volume;
        // 空头势能越高越有利，做空时点位高则势能高
        // 当前均价 = 当前空头势能/当前持仓数量
        // 当前空头势能 = 剩余数量*平仓前均价 + 利润
        sellPrice = (sellVol * sellPrice+benefit)/sellVol;
        return benefit;
    }

}
