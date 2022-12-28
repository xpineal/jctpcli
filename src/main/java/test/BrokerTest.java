package test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kcr.jctpcli.cnf.Cnf;
import org.kcr.jctpcli.cnf.CnfInstrument;
import org.kcr.jctpcli.core.*;
import org.kcr.jctpcli.mock.MockTrader;
import org.kcr.jctpcli.strategy.EmptyStrategy;
import org.kr.jctp.CThostFtdcInstrumentField;

import static org.junit.jupiter.api.Assertions.*;

class BrokerTest {

    public static Hand hand = new Hand();
    public static Broker broker = new Broker(new MockTrader(hand.orderTracker), new EmptyStrategy(), hand);

    @BeforeAll
    static void init() {
        initHold();
    }

    private static void initHold() {
        var ins = new CnfInstrument[1];
        ins[0] = new CnfInstrument("v2301", "DCE");
        Parameter.cnf = new Cnf("tradeSrv", "mdSrv",
                "brokerID", "accountID", "password", "appID", "authCode",
                0.03, 1, 1, ins);
        Parameter.cnf.refresh();

        var pInstrument = new InstrInfo("exchangeID", "instrumentID",
                0.11, 0.11, 0.1, 5);
        pInstrument.openRatioByVolume = 1;
        pInstrument.closeRatioByVolume = 1;
        hand.upsertInstrument(pInstrument);

        // 初始资金5000
        hand.available = 500000;
    }

    @Test
    void executeOpenBuy1() {
        var instrument = hand.getInstrument("instrumentID");
        printExeRet(broker.executeOpenBuy(instrument, new OrderItem(1, 6000, instrument.instrumentID)));
        printHold("open buy");
        broker.hand.OnOrderCancelled("1");
        printHold("cancel open buy");
        assertEquals(broker.hand.available, 500000);
    }

    @Test
    void executeOpenBuy2() {
        var instrument = hand.getInstrument("instrumentID");
        printExeRet(broker.executeOpenBuy(instrument, 1, 6000));
        printHold("open buy");
        broker.hand.OnOrderTrade("1", 1, 6000);
        printHold("done buy");
    }

    @Test
    void executeOpenBuy3() {
        var instrument = hand.getInstrument("instrumentID");
        printExeRet(broker.executeOpenBuy(instrument, new OrderItem(1, 6100, instrument.instrumentID)));
        printHold("open buy");
        broker.hand.OnOrderTrade("1", 1, 6000);
        printHold("done buy");
    }

    @Test
    void executeOpenBuy4() {
        var instrument = hand.getInstrument("instrumentID");
        printExeRet(broker.executeOpenBuy(instrument, 2, 6100));
        printHold("open buy");
        broker.hand.OnOrderTrade("1", 1, 6000);
        printHold("done buy1");
        broker.hand.OnOrderTrade("1", 1, 6100);
        printHold("done buy2");
    }

    @Test
    void executeOpenBuy5() {
        var instrument = hand.getInstrument("instrumentID");
        printExeRet(broker.executeOpenBuy(instrument, new OrderItem(2, 100, instrument.instrumentID)));
        printHold("open buy");
        broker.hand.OnOrderTrade("1", 1, 90);
        printHold("done buy1");
        broker.hand.OnOrderTrade("1", 1, 80);
        printHold("done buy2");
    }

    @Test
    void executeOpenSell1() {
        var instrument = hand.getInstrument("instrumentID");
        printExeRet(broker.executeOpenSell(instrument, new OrderItem(1, 6000, instrument.instrumentID)));
        printHold("open sell");
        broker.hand.OnOrderCancelled("1");
        printHold("cancel open sell");
    }

    @Test
    void executeOpenSell2() {
        var instrument = hand.getInstrument("instrumentID");
        printExeRet(broker.executeOpenSell(instrument, new OrderItem(1, 6000, instrument.instrumentID)));
        printHold("open sell");
        broker.hand.OnOrderTrade("1", 1, 6000);
        printHold("done sell");
    }

    @Test
    void executeOpenSell3() {
        var instrument = hand.getInstrument("instrumentID");
        printExeRet(broker.executeOpenSell(instrument, new OrderItem(1, 6000, instrument.instrumentID)));
        printHold("open sell");
        broker.hand.OnOrderTrade("1", 1, 6100);
        printHold("done sell");
    }

    @Test
    void executeOpenSell4() {
        var instrument = hand.getInstrument("instrumentID");
        printExeRet(broker.executeOpenSell(instrument, 2, 6000));
        printHold("open sell");
        broker.hand.OnOrderTrade("1", 1, 6100);
        printHold("done sell1");
        broker.hand.OnOrderTrade("1", 1, 6000);
        printHold("done sell2");
    }

    @Test
    void executeOpenSell5() {
        var instrument = hand.getInstrument("instrumentID");
        printExeRet(broker.executeOpenSell(instrument, new OrderItem(2, 100, instrument.instrumentID)));
        printHold("open sell");
        broker.hand.OnOrderTrade("1", 1, 110);
        printHold("done sell1");
        broker.hand.OnOrderTrade("1", 1, 120);
        printHold("done sell2");
    }

    @Test
    void executeCloseBuy1() {
        var instrument = hand.getInstrument("instrumentID");
        printExeRet(broker.executeOpenBuy(instrument, 1, 6000));
        //printHold("open buy", broker.hold);
        broker.hand.OnOrderTrade("1", 1, 6000);
        printHold("done buy");

        printExeRet(broker.executeCloseBuy(instrument, new OrderItem(1, 6100, instrument.instrumentID)));
        printHold("close buy");
        broker.hand.OnOrderTrade("2", 1, 6100);
        printHold("done close");
        assertEquals(delta(), 500000);
    }

    @Test
    void executeCloseBuy11() {
        executeCloseBuy1x(6000);
    }

    @Test
    void executeCloseBuy13() {
        executeCloseBuy1x(100);
    }

    void executeCloseBuy1x(double price) {
        var instrument = hand.getInstrument("instrumentID");
        printExeRet(broker.executeOpenBuy(instrument, new OrderItem(1, 6100, instrument.instrumentID)));
        //printHold("open buy", broker.hold);
        broker.hand.OnOrderTrade("1", 1, 6100);
        printHold("done buy");

        printExeRet(broker.executeCloseBuy(instrument, new OrderItem(1, price, instrument.instrumentID)));
        printHold("close buy");
        broker.hand.OnOrderTrade("2", 1, price);
        printHold("done close");
        assertEquals(delta(), 500000);
    }

    @Test
    void executeCloseBuy2() {
        executeCloseBuy2x(100, 110);
    }

    @Test
    void executeCloseBuy21() {
        executeCloseBuy2x(70, 60);
    }

    void executeCloseBuy2x(double price1, double price2) {
        var instrument = hand.getInstrument("instrumentID");
        printExeRet(broker.executeOpenBuy(instrument,2, 100));
        broker.hand.OnOrderTrade("1", 1, 90);
        printHold("done buy1");
        broker.hand.OnOrderTrade("1", 1, 80);
        printHold("done buy2");

        printExeRet(broker.executeCloseBuy(instrument, 1, 110));
        broker.hand.OnOrderTrade("2", 1, price1);
        printHold("done close buy1");

        printExeRet(broker.executeCloseBuy(instrument, 1, 110));
        broker.hand.OnOrderTrade("3", 1, price2);
        printHold("done close buy2");
        assertEquals(delta(), 500000);
    }

    @Test
    void executeCloseBuy221() {
        var instrument = hand.getInstrument("instrumentID");
        printExeRet(broker.executeOpenBuy(instrument, 2, 100));
        broker.hand.OnOrderTrade("1", 1, 90);
        printHold("done buy1");
        broker.hand.OnOrderTrade("1", 1, 80);
        printHold("done buy2");

        printExeRet(broker.executeCloseBuy(instrument, 1, 110));
        broker.hand.OnOrderTrade("2", 1, 90);
        printHold("done close buy1");

        printExeRet(broker.executeCloseBuy(instrument, 1, 110));
        broker.hand.OnOrderTrade("3", 1, 80);
        printHold("done close buy2");
        assertEquals(delta(), 500000);
    }

    @Test
    void executeCloseSell1() {
        var instrument = hand.getInstrument("instrumentID");
        printExeRet(broker.executeOpenSell(instrument, new OrderItem(1, 6000, instrument.instrumentID)));
        printHold("open sell");
        broker.hand.OnOrderTrade("1", 1, 6000);
        printHold("done sell");

        printExeRet(broker.executeCloseSell(instrument, 1, 6100));
        printHold("close sell");
        broker.hand.OnOrderTrade("2", 1, 6100);
        printHold("done close sell");
        assertEquals(delta(), 500000);
    }

    @Test
    void executeCloseSell12() {
        var instrument = hand.getInstrument("instrumentID");
        printExeRet(broker.executeOpenSell(instrument, 1, 6100));
        printHold("open sell");
        broker.hand.OnOrderTrade("1", 1, 6100);
        printHold("done sell");

        printExeRet(broker.executeCloseSell(instrument, 1, 6000));
        printHold("close sell");
        broker.hand.OnOrderTrade("2", 1, 6000);
        printHold("done close sell");
        assertEquals(delta(), 500000);
    }

    @Test
    void executeCloseSell13() {
        var instrument = hand.getInstrument("instrumentID");
        printExeRet(broker.executeOpenSell(instrument, new OrderItem(1, 100, instrument.instrumentID)));
        printHold("open sell");
        broker.hand.OnOrderTrade("1", 1, 100);
        printHold("done sell");

        printExeRet(broker.executeCloseSell(instrument, new OrderItem(1, 120, instrument.instrumentID)));
        printHold("close sell");
        broker.hand.OnOrderTrade("2", 1, 100);
        printHold("done close sell");
        assertEquals(delta(), 500000);
    }

    @Test
    void executeCloseSell2() {
        var instrument = hand.getInstrument("instrumentID");
        printExeRet(broker.executeOpenSell(instrument, new OrderItem(2, 100, instrument.instrumentID)));
        broker.hand.OnOrderTrade("1", 1, 90);
        printHold("done sell1");
        broker.hand.OnOrderTrade("1", 1, 80);
        printHold("done sell2");

        printExeRet(broker.executeCloseSell(instrument, new OrderItem(1, 90, instrument.instrumentID)));
        broker.hand.OnOrderTrade("2", 1, 80);
        printHold("done close sell1");

        printExeRet(broker.executeCloseSell(instrument, new OrderItem(1, 80, instrument.instrumentID)));
        broker.hand.OnOrderTrade("3", 1, 90);
        printHold("done close sell2");
        assertEquals(delta(), 500000);
    }

    @Test
    void executeTryFinally() {
        System.out.println(testTryFinally(1));
        System.out.println(testTryFinally(0));
        System.out.println(testTryFinally(10));
    }

    int testTryFinally(int divide) {
        try {
            return 10/divide;
        }catch (Exception e) {
            e.printStackTrace();
            System.out.print("exception:");
            System.out.println(e);
            return 0;
        }finally {
            System.out.printf("finally out:%d\n", divide);
        }
    }

    private void printHold(String _title) {
        var instrument = hand.getInstrument("instrumentID");
        var usedMoney =  500000-hand.available;
        var delta = delta();
        System.out.println(_title);
        System.out.printf("available:%f\n", hand.available);
        System.out.printf("use money:%f\n", usedMoney);
        System.out.println("hold info:");
        System.out.print("delta:");
        System.out.println(delta);
        System.out.print("balance:");
        System.out.println(delta+usedMoney);

        System.out.println(instrument);
        System.out.println("all order info:");
        System.out.println(broker.hand.orderTracker.allOrdersToString());
        System.out.println("");
    }

    private void printExeRet(ExeRet r) {
        System.out.print("execute result:");
        System.out.println(r);
    }

    private double delta() {
        var instrument = hand.getInstrument("instrumentID");
        return hand.available + instrument.buyHold.totalMargin() + instrument.sellHold.totalMargin()
                - instrument.buyHold.profile - instrument.sellHold.profile;
    }
}
