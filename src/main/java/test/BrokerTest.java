package test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kcr.jctpcli.env.Broker;
import org.kcr.jctpcli.env.Hold;
import org.kcr.jctpcli.env.Instrument;
import org.kcr.jctpcli.mock.MockTrader;

import static org.junit.jupiter.api.Assertions.*;

class BrokerTest {

    public static Hold hold = new Hold("exchangeID", "instrumentID");
    public static Broker broker = new Broker(new MockTrader(), hold);

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
        hold.available = 5000;
    }

    @Test
    void executeOpenBuy() {
        broker.executeOpenBuy(100, 1);
        //broker.hold.orderTracker
    }

    @Test
    void executeOpenSell() {
    }

    @Test
    void executeCloseBuy() {
    }

    @Test
    void executeCloseSell() {
    }

    @Test
    void executeOrder() {
    }
}