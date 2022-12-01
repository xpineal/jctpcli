package test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kcr.jctpcli.env.Broker;
import org.kcr.jctpcli.env.ExeRet;
import org.kcr.jctpcli.env.Hold;
import org.kcr.jctpcli.env.Instrument;
import org.kcr.jctpcli.mock.MockTrader;

import static org.junit.jupiter.api.Assertions.*;

class BrokerTest {

    public static Hold hold = new Hold("exchangeID", "instrumentID");
    public static Broker broker = new Broker(new MockTrader(hold.orderTracker), hold);

    @BeforeAll
    static void init() {
        initHold();
    }

    private static void initHold() {
        hold.instrument.openRatioByMoney = 100;
        hold.instrument.openRatioByVolume = 1;
        hold.instrument.closeRatioByMoney = 200;
        hold.instrument.closeRatioByVolume = 2;
        hold.instrument.closeTodayRatioByMoney = 400;
        hold.instrument.closeTodayRatioByVolume = 4;

        hold.instrument.longMarginRatio = 15;
        hold.instrument.shortMarginRatio = 10;
        hold.instrument.volumeMultiple = 50;
        hold.instrument.priceTick = 0.1;

        // 初始资金5000
        hold.available = 500000;
    }

    @Test
    void executeOpenBuy1() {
        printExeRet(broker.executeOpenBuy(100, 1));
        printHold("open buy", broker.hold);
        broker.hold.OnOrderCancelled("1");
        printHold("cancel open buy", broker.hold);
    }

    @Test
    void executeOpenBuy2() {
        printExeRet(broker.executeOpenBuy(100, 1));
        printHold("open buy", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 100);
        printHold("done buy", broker.hold);
    }

    @Test
    void executeOpenBuy3() {
        printExeRet(broker.executeOpenBuy(100, 1));
        printHold("open buy", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 90);
        printHold("done buy", broker.hold);
    }

    @Test
    void executeOpenBuy4() {
        printExeRet(broker.executeOpenBuy(100, 2));
        printHold("open buy", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 90);
        printHold("done buy1", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 100);
        printHold("done buy2", broker.hold);
    }

    @Test
    void executeOpenBuy5() {
        printExeRet(broker.executeOpenBuy(100, 2));
        printHold("open buy", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 90);
        printHold("done buy1", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 80);
        printHold("done buy2", broker.hold);
    }

    @Test
    void executeOpenSell1() {
        printExeRet(broker.executeOpenSell(100, 1));
        printHold("open sell", broker.hold);
        broker.hold.OnOrderCancelled("1");
        printHold("cancel open sell", broker.hold);
    }

    @Test
    void executeOpenSell2() {
        printExeRet(broker.executeOpenSell(100, 1));
        printHold("open sell", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 100);
        printHold("done sell", broker.hold);
    }

    @Test
    void executeOpenSell3() {
        printExeRet(broker.executeOpenSell(100, 1));
        printHold("open sell", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 110);
        printHold("done sell", broker.hold);
    }

    @Test
    void executeOpenSell4() {
        printExeRet(broker.executeOpenSell(100, 2));
        printHold("open sell", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 110);
        printHold("done sell1", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 100);
        printHold("done sell2", broker.hold);
    }

    @Test
    void executeOpenSell5() {
        printExeRet(broker.executeOpenSell(100, 2));
        printHold("open sell", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 110);
        printHold("done sell1", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 120);
        printHold("done sell2", broker.hold);
    }

    @Test
    void executeCloseBuy1() {
        printExeRet(broker.executeOpenBuy(100, 1));
        //printHold("open buy", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 100);
        printHold("done buy", broker.hold);

        printExeRet(broker.executeCloseBuy(110, 1));
        //printHold("close buy", broker.hold);
        broker.hold.OnOrderTrade("2", 1, 110);
        printHold("done close", broker.hold);
    }

    @Test
    void executeCloseBuy11() {
        printExeRet(broker.executeOpenBuy(100, 1));
       //printHold("open buy", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 100);
        printHold("done buy", broker.hold);

        printExeRet(broker.executeCloseBuy(90, 1));
        printHold("close buy", broker.hold);
        broker.hold.OnOrderTrade("2", 1, 90);
        printHold("done close", broker.hold);
    }

    @Test
    void executeCloseBuy13() {
        printExeRet(broker.executeOpenBuy(100, 1));
        //printHold("open buy", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 100);
        printHold("done buy", broker.hold);

        printExeRet(broker.executeCloseBuy(90, 1));
        printHold("close buy", broker.hold);
        broker.hold.OnOrderTrade("2", 1, 100);
        printHold("done close", broker.hold);
    }

    @Test
    void executeCloseBuy2() {
        printExeRet(broker.executeOpenBuy(100, 2));
        broker.hold.OnOrderTrade("1", 1, 90);
        printHold("done buy1", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 80);
        printHold("done buy2", broker.hold);

        printExeRet(broker.executeCloseBuy(110, 1));
        broker.hold.OnOrderTrade("2", 1, 100);
        printHold("done close buy1", broker.hold);

        printExeRet(broker.executeCloseBuy(110, 1));
        broker.hold.OnOrderTrade("3", 1, 110);
        printHold("done close buy2", broker.hold);
    }

    @Test
    void executeCloseBuy21() {
        printExeRet(broker.executeOpenBuy(100, 2));
        broker.hold.OnOrderTrade("1", 1, 90);
        printHold("done buy1", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 80);
        printHold("done buy2", broker.hold);

        printExeRet(broker.executeCloseBuy(110, 1));
        broker.hold.OnOrderTrade("2", 1, 70);
        printHold("done close buy1", broker.hold);

        printExeRet(broker.executeCloseBuy(110, 1));
        broker.hold.OnOrderTrade("3", 1, 60);
        printHold("done close buy2", broker.hold);
    }

    @Test
    void executeCloseBuy221() {
        printExeRet(broker.executeOpenBuy(100, 2));
        broker.hold.OnOrderTrade("1", 1, 90);
        printHold("done buy1", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 80);
        printHold("done buy2", broker.hold);

        printExeRet(broker.executeCloseBuy(110, 1));
        broker.hold.OnOrderTrade("2", 1, 90);
        printHold("done close buy1", broker.hold);

        printExeRet(broker.executeCloseBuy(110, 1));
        broker.hold.OnOrderTrade("3", 1, 80);
        printHold("done close buy2", broker.hold);
    }

    @Test
    void executeCloseSell1() {
        printExeRet(broker.executeOpenSell(100, 1));
        printHold("open sell", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 100);
        printHold("done sell", broker.hold);

        printExeRet(broker.executeCloseSell(90, 1));
        printHold("close sell", broker.hold);
        broker.hold.OnOrderTrade("2", 1, 90);
        printHold("done close sell", broker.hold);
    }

    @Test
    void executeCloseSell12() {
        printExeRet(broker.executeOpenSell(100, 1));
        printHold("open sell", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 100);
        printHold("done sell", broker.hold);

        printExeRet(broker.executeCloseSell(120, 1));
        printHold("close sell", broker.hold);
        broker.hold.OnOrderTrade("2", 1, 110);
        printHold("done close sell", broker.hold);
    }

    @Test
    void executeCloseSell13() {
        printExeRet(broker.executeOpenSell(100, 1));
        printHold("open sell", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 100);
        printHold("done sell", broker.hold);

        printExeRet(broker.executeCloseSell(120, 1));
        printHold("close sell", broker.hold);
        broker.hold.OnOrderTrade("2", 1, 100);
        printHold("done close sell", broker.hold);
    }

    @Test
    void executeCloseSell2() {
        printExeRet(broker.executeOpenSell(100, 2));
        broker.hold.OnOrderTrade("1", 1, 90);
        printHold("done sell1", broker.hold);
        broker.hold.OnOrderTrade("1", 1, 80);
        printHold("done sell2", broker.hold);

        printExeRet(broker.executeCloseSell(90, 1));
        broker.hold.OnOrderTrade("2", 1, 80);
        printHold("done close sell1", broker.hold);

        printExeRet(broker.executeCloseSell(80, 1));
        broker.hold.OnOrderTrade("3", 1, 90);
        printHold("done close sell2", broker.hold);
    }

    @Test
    void executeOrder() {
    }

    private void printHold(String _title, Hold _hold) {
        System.out.println(_title);
        System.out.printf("available:%f\n", _hold.available);
        System.out.println("hold info:");
        System.out.println(broker.hold.instrument.holdInfo());
        System.out.println("all order info:");
        System.out.println(broker.hold.orderTracker.allOrdersToString());
        System.out.println("");
    }

    private void printExeRet(ExeRet r) {
        System.out.print("execute result:");
        System.out.println(r);
    }
}