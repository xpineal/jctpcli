package org.kcr.jctpcli.old;

import org.kcr.jctpcli.util.Recorder;
import org.kr.jctp.CThostFtdcInstrumentCommissionRateField;
import org.kr.jctp.CThostFtdcInstrumentField;
import org.kr.jctp.CThostFtdcTradingAccountField;

public class Prameter {
	// 多空方向
	public static int bsFlg;
	// 最高价
	public static double maxPrice;
	// 最低价
	public static double minPrice;
	// 开多价
	public static double openBuyPrice;
	// 开空价
	public static double openSellPrice;

	// 做多持仓
	public static int buyCount;
	// 做空持仓
	public static int sellCount;

	public static int preBuyCount;
	public static int preSellCount;
	// 可用资金
	public static double available;
	// 合约数量乘数
	public static int volumeMultiple;
	// 合约最小变动价格
	public static double priceTick;

	// 多头保证金率
	public static double longMarginRatio;
	// 空头保证金率
	public static double shortMarginRatio;
	// 开仓手续费率
	public static double openRatioByMoney;
	// 开仓手续费
	public static double openRatioByVolume;
	// 平仓手续费率
	public static double closeRatioByMoney;
	// 平仓手续费
	public static double closeRatioByVolume;
	// 平今仓手续费率
	public static double closeTodayRatioByMoney;
	// 平今仓手续费
	public static double closeTodayRatioByVolume;

	// 交易日
	public static String tradingDay;

	// 测试
	public static Recorder recorder;
	public static boolean debugMode = false;

	public static void initRecorder() {
		recorder = new Recorder();
	}

//	// 可用资金
//	public static void setAvailable(CThostFtdcTradingAccountField account) {
//		// 可用资金
//		available = account.getAvailable();
//	}

//	// 查询保证金率 合约乘数 最小变动价格
//	public static void setInstrutmentRatio(CThostFtdcInstrumentField pInstrument) {
//		// 多头保证金率
//		longMarginRatio = pInstrument.getLongMarginRatio();
//		// 空头保证金率
//		shortMarginRatio = pInstrument.getShortMarginRatio();
//		// 合约乘数
//		volumeMultiple = pInstrument.getVolumeMultiple();
//		// 合约最小变动价格
//		priceTick = pInstrument.getPriceTick();
//	}
//
//	// 查询手续费及率
//	public static void setRatio(CThostFtdcInstrumentCommissionRateField tc) {
//		// 开仓手续费率
//		openRatioByMoney = tc.getOpenRatioByMoney();
//		// 开仓手续费
//		openRatioByVolume = tc.getOpenRatioByVolume();
//		// 平仓手续费率
//		closeRatioByMoney = tc.getCloseRatioByMoney();
//		// 平仓手续费
//		closeRatioByVolume = tc.getCloseRatioByVolume();
//		// 平今仓手续费率
//		closeTodayRatioByMoney = tc.getCloseTodayRatioByMoney();
//		// 平今仓手续费
//		closeTodayRatioByVolume = tc.getCloseTodayRatioByVolume();
//	}

//	public static boolean traderTime(String updateTime) {
//		int ut = Integer.parseInt(updateTime.replace(":", ""));
//
//		if (ut >= 112955 && ut < 133000) {// 上午
//			return true;
//		}
//		if (ut >= 145955 && ut < 210000) {// 下午
//			return true;
//		}
//		if (ut >= 225955 || ut < 90000) {// 夜间
//			return true;
//		}
//
//		return false;
//	}

//	public static void outPut() {
//		System.out.printf("合约乘数 : %d ; 最小变动价格 : %f\n", volumeMultiple, priceTick);
//		// System.out.printf(" \n"，);
//	}
}
