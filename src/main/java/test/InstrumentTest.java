package test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kcr.jctpcli.core.Direction;
import org.kcr.jctpcli.core.Instrument;
import org.kcr.jctpcli.core.OrderInfo;

import static org.junit.jupiter.api.Assertions.*;

class InstrumentTest {
    public static Instrument instrument = new Instrument("exchangeID", "instrumentID");

    @BeforeAll
    static void init() {
        instrument.openRatioByMoney = 100;
        instrument.openRatioByVolume = 1;
        instrument.closeRatioByMoney = 200;
        instrument.closeRatioByVolume = 2;
        instrument.closeTodayRatioByMoney = 400;
        instrument.closeTodayRatioByVolume = 4;

        instrument.longMarginRatio = 15;
        instrument.shortMarginRatio = 10;
        instrument.volumeMultiple = 50;
        instrument.priceTick = 0.1;
    }

    @Test
    void buyMargin() {
        assertEquals(0, instrument.buyMargin(100, 0));
        outBuyMargin(100, 1);
        outBuyMargin(100, 2);
        outBuyMargin(100, 4);
        outBuyMargin(50, 4);
        outBuyMargin(50, 2);
        outBuyMargin(50, 1);
    }

    @Test
    void sellMargin() {
        outSellMargin(100, 1);
        outSellMargin(100, 2);
        outSellMargin(100, 4);
        outSellMargin(50, 4);
        outSellMargin(50, 2);
        outSellMargin(50, 1);
    }

    @Test
    void openBuyCost() {
        outOpenBuyCost(100, 1);
        outOpenBuyCost(100, 2);
        outOpenBuyCost(100, 4);
        outOpenBuyCost(50, 4);
        outOpenBuyCost(50, 2);
        outOpenBuyCost(50, 1);
    }

    @Test
    void openSellCost() {
        outOpenSellCost(100, 1);
        outOpenSellCost(100, 2);
        outOpenSellCost(100, 4);
        outOpenSellCost(50, 4);
        outOpenSellCost(50, 2);
        outOpenSellCost(50, 1);
    }

    @Test
    void closeFee() {
        outCloseFee(100, 1);
        outCloseFee(100, 2);
        outCloseFee(100, 4);
        outCloseFee(50, 4);
        outCloseFee(50, 2);
        outCloseFee(50, 1);
    }

    @Test
    void onOrderTradeBuy1() {
        var orderInfo = new OrderInfo("1", 1, 100, instrument.exchangeID, instrument.instrumentID, Direction.OpenBuy);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
        orderInfo = new OrderInfo("2", 1, 120, instrument.exchangeID, instrument.instrumentID, Direction.CloseBuy);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
    }

    @Test
    void onOrderTradeBuy2() {
        var orderInfo = new OrderInfo("1", 2, 100, instrument.exchangeID, instrument.instrumentID, Direction.OpenBuy);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
        orderInfo = new OrderInfo("2", 1, 120, instrument.exchangeID, instrument.instrumentID, Direction.CloseBuy);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
        orderInfo = new OrderInfo("2", 1, 120, instrument.exchangeID, instrument.instrumentID, Direction.CloseBuy);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
    }

    @Test
    void onOrderTradeBuy3() {
        var orderInfo = new OrderInfo("1", 2, 100, instrument.exchangeID, instrument.instrumentID, Direction.OpenBuy);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
        orderInfo = new OrderInfo("2", 2, 120, instrument.exchangeID, instrument.instrumentID, Direction.CloseBuy);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
    }

    @Test
    void onOrderTradeBuy4() {
        var orderInfo = new OrderInfo("1", 1, 100, instrument.exchangeID, instrument.instrumentID, Direction.OpenBuy);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
        orderInfo = new OrderInfo("2", 1, 80, instrument.exchangeID, instrument.instrumentID, Direction.CloseBuy);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
    }

    @Test
    void onOrderTradeBuy5() {
        var orderInfo = new OrderInfo("1", 2, 100, instrument.exchangeID, instrument.instrumentID, Direction.OpenBuy);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
        orderInfo = new OrderInfo("2", 1, 80, instrument.exchangeID, instrument.instrumentID, Direction.CloseBuy);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
        orderInfo = new OrderInfo("2", 1, 60, instrument.exchangeID, instrument.instrumentID, Direction.CloseBuy);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
    }

    @Test
    void onOrderTradeBuy6() {
        var orderInfo = new OrderInfo("1", 2, 100, instrument.exchangeID, instrument.instrumentID, Direction.OpenBuy);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
        orderInfo = new OrderInfo("2", 2, 60, instrument.exchangeID, instrument.instrumentID, Direction.CloseBuy);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
    }

    @Test
    void onOrderTradeSell1() {
        var orderInfo = new OrderInfo("1", 1, 100, instrument.exchangeID, instrument.instrumentID, Direction.OpenSell);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
        orderInfo = new OrderInfo("2", 1, 120, instrument.exchangeID, instrument.instrumentID, Direction.CloseSell);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
    }

    @Test
    void onOrderTradeSell2() {
        var orderInfo = new OrderInfo("1", 2, 100, instrument.exchangeID, instrument.instrumentID, Direction.OpenSell);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
        orderInfo = new OrderInfo("2", 1, 120, instrument.exchangeID, instrument.instrumentID, Direction.CloseSell);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
        orderInfo = new OrderInfo("2", 1, 120, instrument.exchangeID, instrument.instrumentID, Direction.CloseSell);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
    }

    @Test
    void onOrderTradeSell3() {
        var orderInfo = new OrderInfo("1", 2, 100, instrument.exchangeID, instrument.instrumentID, Direction.OpenSell);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
        orderInfo = new OrderInfo("2", 2, 120, instrument.exchangeID, instrument.instrumentID, Direction.CloseSell);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
    }

    @Test
    void onOrderTradeSell4() {
        var orderInfo = new OrderInfo("1", 1, 100, instrument.exchangeID, instrument.instrumentID, Direction.OpenSell);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
        orderInfo = new OrderInfo("2", 1, 80, instrument.exchangeID, instrument.instrumentID, Direction.CloseSell);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
    }

    @Test
    void onOrderTradeSell5() {
        var orderInfo = new OrderInfo("1", 2, 100, instrument.exchangeID, instrument.instrumentID, Direction.OpenSell);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
        orderInfo = new OrderInfo("2", 1, 80, instrument.exchangeID, instrument.instrumentID, Direction.CloseSell);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
        orderInfo = new OrderInfo("2", 1, 60, instrument.exchangeID, instrument.instrumentID, Direction.CloseSell);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
    }

    @Test
    void onOrderTradeSell6() {
        var orderInfo = new OrderInfo("1", 2, 100, instrument.exchangeID, instrument.instrumentID, Direction.OpenSell);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
        orderInfo = new OrderInfo("2", 2, 60, instrument.exchangeID, instrument.instrumentID, Direction.CloseSell);
        instrument.OnOrderTrade(orderInfo);
        System.out.println(instrument.holdInfo());
    }

    private void outBuyMargin(double price, int volume) {
        System.out.println(
                String.format("price:%f, volume:%d, buy margin:%f", price, volume, instrument.buyMargin(price, volume)));
    }

    private void outSellMargin(double price, int volume) {
        System.out.println(
                String.format("price:%f, volume:%d, sell margin:%f", price, volume, instrument.sellMargin(price, volume)));
    }

    private void outOpenBuyCost(double price, int volume) {
        System.out.println(
                String.format("price:%f, volume:%d, buy margin:%f", price, volume, instrument.buyMargin(price, volume)));
        System.out.println(
                String.format("price:%f, volume:%d, open buy cost:%f", price, volume, instrument.openBuyCost(price, volume)));
    }

    private void outOpenSellCost(double price, int volume) {
        System.out.println(
                String.format("price:%f, volume:%d, sell margin:%f", price, volume, instrument.sellMargin(price, volume)));
        System.out.println(
                String.format("price:%f, volume:%d, open sell cost:%f", price, volume, instrument.openSellCost(price, volume)));
    }

    private void outCloseFee(double price, int volume) {
        System.out.println(
                String.format("price:%f, volume:%d, close fee:%f", price, volume, instrument.closeFee(volume)));
    }
}
