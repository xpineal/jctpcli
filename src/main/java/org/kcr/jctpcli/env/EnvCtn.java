package org.kcr.jctpcli.env;

import org.jetbrains.annotations.NotNull;
import org.kcr.jctpcli.TraderCall;
import org.kr.jctp.*;

import java.util.concurrent.locks.*;

public class EnvCtn {
    //锁
    private final Lock lock = new ReentrantLock();

    //交易实例
    private TraderCall traderCall;
    //合约的Map
    //暂时只考虑一个合约
    //private Map<String, Instr> holdMap = new HashMap<>();
    Instr instrObj;
    //可用资金
    private double availableAmount;
    //持仓获取完成
    private boolean bPosCompleted;
    //登录完成
    private boolean bLogin;
    //账号查询完成
    private boolean bAccount;

    //构造函数
    public EnvCtn(TraderCall traderCall, Instr instr) {
        this.traderCall = traderCall;
        this.instrObj = instr;
        this.availableAmount = 0;
        this.bPosCompleted = false;
        this.bLogin = false;
    }

    //是否已经完成登录
    public boolean isLogin() {
        lock.lock();
        boolean b;
        try {
            b = bLogin;
        }finally {
            lock.unlock();
        }
        return b;
    }

    //是否已经完成获取账号信息
    public boolean isAccount() {
        lock.lock();
        boolean b;
        try {
            b = bAccount;
        }finally {
            lock.unlock();
        }
        return b;
    }

    //是否已经完成获取持仓信息
    public boolean isPosition() {
        lock.lock();
        boolean b;
        try {
            b = bPosCompleted;
        }finally {
            lock.unlock();
        }
        return b;
    }

    //填充登录
    public void feedLogin() {
        lock.lock();
        try {
            bLogin = true;
        }finally {
            lock.unlock();
        }
    }

    //填充启动式持仓信息
    public void feedPosition(@NotNull CThostFtdcInvestorPositionField pInfo, boolean bLast) {
        //var instr = pInfo.getInstrumentID();
        lock.lock();
        try {
            //TODO: 后续用Map
            //var ch = holdMap.get(instr);
            //ch.feedPosition(pInfo);
            instrObj.feedPosition(pInfo);
            if (bLast) {
                bPosCompleted = true;
            }
        }finally {
            lock.unlock();
        }
    }

    //填充空的持仓信息
    public void feedEmptyPosition() {
        lock.lock();
        try {
            bPosCompleted = true;
        }finally {
            lock.unlock();
        }
    }

    //填充行情信息
    public Instr feedMarket(@NotNull CThostFtdcDepthMarketDataField pInfo) {
        var instrID = pInfo.getInstrumentID();
        Instr instr;
        lock.lock();
        try {
            //TODO: 后续用Map
            //var ch = holdMap.get(instrID);
            //ch.feedMarket(pInfo);
            //instr = ch.clone();
            instrObj.feedMarket(pInfo);
            instr = instrObj.clone();
        }finally {
            lock.unlock();
        }
        tradeGo(instr);
        return instr;
    }

    //填充账号信息
    public void feedAccount(@NotNull CThostFtdcTradingAccountField pInfo) {
        lock.lock();
        try {
            availableAmount = pInfo.getAvailable();
            bAccount = true;
        }finally {
            lock.unlock();
        }
    }

    //报单被撤的回调
    public void feedOrderCancel(@NotNull CThostFtdcOrderField pInfo) {
        //TODO: 重要的几个数据
        // 原始报单数量
        // pInfo.getVolumeTotalOriginal();
        // 成交数量
        // pInfo.getVolumeTraded();
        // 剩余数量
        // pInfo.getVolumeTotal();
        // 多空方向
        // pInfo.getDirection();
        // 开平标记(开仓和平仓)
        // pInfo.getCombOffsetFlag();
    }

    //填充成交信息
    public void feedTrade(@NotNull CThostFtdcTradeField pInfo) {
        //TODO: 重要的数据
        // 数量
        // pInfo.getVolume();
        // 多空方向
        // pInfo.getDirection();
        // 开平标记(开仓和平仓)
        // pInfo.getOffsetFlag();

        var vol = pInfo.getVolume();
        var dir = pInfo.getDirection();
        var offset = pInfo.getOffsetFlag();
        lock.lock();
        try {
            if (dir == jctpConstants.THOST_FTDC_D_Buy) {
                //买方向 -- 做多
                if (offset == jctpConstants.THOST_FTDC_OF_Open) {
                    //多单数量加
                    instrObj.CLong += vol;
                }else{
                    //空单数量减
                    //平仓时如果有昨日的持仓，优先扣除昨日持仓
                    if (instrObj.CShortYsd > 0) {
                        var delta = vol - instrObj.CShortYsd;
                        if (delta > 0) {
                            instrObj.CShortYsd = 0;
                            instrObj.CShort -= delta;
                        }else{
                            instrObj.CShortYsd -= vol;
                        }
                    }else{
                        instrObj.CShort -= vol;
                    }
                }
            }else {
                //卖方向 -- 做空
                if (offset == jctpConstants.THOST_FTDC_OF_Open) {
                    //空单数量加
                    instrObj.CShort += vol;
                }else{
                    //多单数量减
                    //平仓时如果有昨日的持仓，优先扣除昨日持仓
                    if (instrObj.CLongYsd > 0) {
                        var delta = vol - instrObj.CLongYsd;
                        if (delta > 0) {
                            instrObj.CLongYsd = 0;
                            instrObj.CLong -= delta;
                        }else{
                            instrObj.CLongYsd -= vol;
                        }
                    }else{
                        instrObj.CLong -= vol;
                    }
                }
            }
        }finally {
            lock.unlock();
        }
    }

    //获取合约基本信息
    public Instr getInstr(String instrID) {
        Instr outInstr = null;
        lock.lock();
        try {
            //TODO: 后续用Map
            //var ch = holdMap.get(instrID);
            //if (ch != null) {
            //    outInstr = ch.clone();
            //}
            outInstr = instrObj.clone();
        }finally {
            lock.unlock();
        }
        return outInstr;
    }

    //TODO: for test
    private static int buyCount = 0;
    //真正的交易逻辑
    private void tradeGo(Instr instr) {
        //TODO: 真正的逻辑
        if (instr.sellPrice - instr.buyPrice < 5) {
            //卖1价与买1价差不大 -- 实际上需要看比例
            //先平仓
            closeHold(instr);
            //只做5次操作
            if (buyCount < 5) {
                //买1手 -- TODO 只是测试逻辑
                var vol = 1;
                openBuy(instr.sellPrice, vol); //用卖1价买
                openSell(instr.buyPrice, vol); //用买1价卖
                buyCount++;
            }
        }
    }

    //平已有持仓
    private void closeHold(Instr instr) {
        if (instr.CLongYsd > 0) {
            //平掉昨日多头持仓
            closeBuy(instr.buyPrice, instr.CLongYsd, closeYsdFlag(instr.exchangeID));
        }
        if (instr.CShortYsd > 0) {
            //平掉昨日空头持仓
            closeSell(instr.sellPrice, instr.CShortYsd, closeYsdFlag(instr.exchangeID));
        }
        if (instr.CLong > 0) {
            //平掉多头持仓
            closeBuy(instr.buyPrice, instr.CLong, closeTodayFlag(instr.exchangeID));
        }
        if (instr.CShort > 0) {
            //平掉空头持仓
            closeSell(instr.sellPrice, instr.CShort, closeTodayFlag(instr.exchangeID));
        }
    }

    //开多仓
    private void openBuy(double price, int vol) {
        String exchangeID;
        String instrID;

        if (availableAmount < 100) {
            System.out.printf("可用金额不够:%f\n", availableAmount);
            return;
        }

        lock.lock();
        try {
            exchangeID = instrObj.exchangeID;
            instrID = instrObj.id;
        }finally {
            lock.unlock();
        }
        traderCall.addLimitPriceOpenBuyOrder(exchangeID, instrID, price, vol);
    }

    //开空仓
    private void openSell(double price, int vol) {
        String exchangeID;
        String instrID;

        if (availableAmount < 100) {
            System.out.printf("可用金额不够:%f\n", availableAmount);
            return;
        }

        lock.lock();
        try {
            exchangeID = instrObj.exchangeID;
            instrID = instrObj.id;
        }finally {
            lock.unlock();
        }
        traderCall.addLimitPriceOpenSellOrder(exchangeID, instrID, price, vol);
    }

    //平多仓
    //closeFlag -- 上期所和能源所有用平今仓/平昨仓，这里需要传入标记
    private void closeBuy(double price, int vol, String closeFlag) {
        String exchangeID;
        String instrID;

        lock.lock();
        try {
            exchangeID = instrObj.exchangeID;
            instrID = instrObj.id;
        }finally {
            lock.unlock();
        }
        traderCall.addLimitPriceCloseBuyOrder(exchangeID, instrID, price, vol, closeFlag);
    }

    //平空仓
    //closeFlag -- 上期所和能源所有用平今仓/平昨仓，这里需要传入标记
    private void closeSell(double price, int vol, String closeFlag) {
        String exchangeID;
        String instrID;

        lock.lock();
        try {
            exchangeID = instrObj.exchangeID;
            instrID = instrObj.id;
        }finally {
            lock.unlock();
        }
        traderCall.addLimitPriceCloseSellOrder(exchangeID, instrID, price, vol, closeFlag);
    }

    //根据交易所确定平今仓标记
    private String closeTodayFlag(String exchangeID) {
        switch (exchangeID) {
            case SHFE:
            case INE:
                return CloseToday;
        }
        return Close;
    }

    //根据交易所确定平昨仓标记
    private String closeYsdFlag(String exchangeID) {
        switch (exchangeID) {
            case SHFE:
            case INE:
                return CloseYesterday;
        }
        return Close;
    }

    public static final String SHFE = "SHFE";
    public static final String INE = "INE";

    private static final String Close = Character.toString(jctpConstants.THOST_FTDC_OF_Close);
    private static final String CloseToday = Character.toString(jctpConstants.THOST_FTDC_OF_CloseToday);
    private static final String CloseYesterday = Character.toString(jctpConstants.THOST_FTDC_OF_CloseYesterday);
}
