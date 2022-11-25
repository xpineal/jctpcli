package org.kcr.jctpcli.old;

import org.kcr.jctpcli.trader.TraderCall;

public class Commerce {

	private final TraderCall traderCall;

	public Commerce(TraderCall traderCall) {
		this.traderCall = traderCall;
	}

	public void marketProcess(String updateTime, double lastPrice, double bidPrice, double askPrice, int bidVolume,
			int askVolume) {

//		if (Prameter.maxPrice == 0d && Prameter.minPrice == 0d) {
//			Prameter.maxPrice = lastPrice;
//			Prameter.minPrice = lastPrice;
//
//			return;
//		}
//
//		if (Prameter.traderTime(updateTime)) {
//			if (Prameter.buyCount > 0) {
//				closeBuy(updateTime, lastPrice, bidPrice, askPrice, bidVolume, askVolume);
//			}
//			if (Prameter.sellCount > 0) {
//				closeSell(updateTime, lastPrice, bidPrice, askPrice, bidVolume, askVolume);
//			}
//		}
//
//		riseMax(updateTime, lastPrice, bidPrice, askPrice, bidVolume, askVolume);
//		descentMin(updateTime, lastPrice, bidPrice, askPrice, bidVolume, askVolume);
	}

	public void riseMax(String updateTime, double lastPrice, double bidPrice, double askPrice, int bidVolume,
			int askVolume) {
//		if (lastPrice >= Prameter.maxPrice) {// 开多 平空
//			Prameter.bsFlg = 1;
//			Prameter.maxPrice = lastPrice;
//
//			openBuy(updateTime, lastPrice, bidPrice, askPrice, bidVolume, askVolume);
//			closeSell(updateTime, lastPrice, bidPrice, askPrice, bidVolume, askVolume);
//		}
//
//		if (Prameter.bsFlg == 1) {
//			if (Prameter.maxPrice - lastPrice >= 14 * Prameter.priceTick) {// 开空 平多
//				Prameter.minPrice = lastPrice;
//				Prameter.bsFlg = -1;
//			}
//
////			if (Prameter.maxPrice - lastPrice >= 4 * Prameter.priceTick
////					&& Prameter.maxPrice - lastPrice < 14 * Prameter.priceTick) {// 平多
////				closeBuy(updateTime, lastPrice, bidPrice, askPrice, bidVolume, askVolume);
////			}
//		}

	}

	public void descentMin(String updateTime, double lastPrice, double bidPrice, double askPrice, int bidVolume,
			int askVolume) {
//		if (lastPrice <= Prameter.minPrice) {// 开空 平多
//			Prameter.bsFlg = -1;
//			Prameter.minPrice = lastPrice;
//
//			openSell(updateTime, lastPrice, bidPrice, askPrice, bidVolume, askVolume);
//			closeBuy(updateTime, lastPrice, bidPrice, askPrice, bidVolume, askVolume);
//		}
//
//		if (Prameter.bsFlg == -1) {
//			if (lastPrice - Prameter.minPrice >= 14 * Prameter.priceTick) {// 开多 平空
//				Prameter.maxPrice = lastPrice;
//				Prameter.bsFlg = 1;
//				riseMax(updateTime, lastPrice, bidPrice, askPrice, bidVolume, askVolume);
//			}
//
////			if (lastPrice - Prameter.minPrice >= 4 * Prameter.priceTick
////					&& lastPrice - Prameter.minPrice < 14 * Prameter.priceTick) {// 平空
////				closeSell(updateTime, lastPrice, bidPrice, askPrice, bidVolume, askVolume);
////			}
//		}
	}

	public void openBuy(String updateTime, double lastPrice, double bidPrice, double askPrice, int bidVolume,
			int askVolume) { // 开多
		if (Prameter.preBuyCount != 0) {
			return;
		}
//		// 可买数量
//		int buyCount = 0;
//		// 开多1手合约保证金
//		double buyMargin = askPrice * Prameter.volumeMultiple * Prameter.longMarginRatio;
//
//		if (Prameter.sellCount == 0) {// 无持仓，买一半资金
//			buyCount = (int) (Prameter.available / buyMargin / 2);
//		} else {// 有持仓，买所有可用资金
//			buyCount = (int) (Prameter.available / buyMargin);
//		}
//
//		if (buyCount == 0) {// 开多
//			return;
//		}

		traderCall.openBuy(bidPrice + 1d, 1);

		Prameter.preBuyCount = 1;

//		Prameter.buyCount = buyCount;
//		Prameter.openBuyPrice = askPrice;

//		Prameter.available -= buyCount * (buyMargin + Prameter.openRatioByVolume);
//		Prameter.recorder
//				.writeMM(updateTime + " -- openBuy : " + String.valueOf(askPrice) + "," + String.valueOf(buyCount));
	}

	public void openSell(String updateTime, double lastPrice, double bidPrice, double askPrice, int bidVolume,
			int askVolume) {// 开空
//		if (Prameter.preSellCount != 0) {
//			return;
//		}
////		// 可卖数量
////		int sellCount = 0;
////		// 开空1手合约保证金
////		double sellMargin = bidPrice * Prameter.volumeMultiple * Prameter.shortMarginRatio;
////
////		if (Prameter.buyCount == 0) {// 无持仓，买一半资金
////			sellCount = (int) (Prameter.available / sellMargin / 2);
////		} else {// 有持仓，买所有可用资金
////			sellCount = (int) (Prameter.available / sellMargin);
////		}
////
////		if (sellCount == 0) {// 开空
////			return;
////		}
//		traderCall.openSell(askPrice - 1d, 1);
//
//		Prameter.preSellCount = 1;
//
////		Prameter.sellCount = sellCount;
////		Prameter.openSellPrice = bidPrice;
////
////		Prameter.available -= sellCount * (sellMargin + Prameter.openRatioByVolume);
////		Prameter.recorder
////				.writeMM(updateTime + " -- openSell : " + String.valueOf(bidPrice) + "," + String.valueOf(sellCount));
	}

	public void closeBuy(String updateTime, double lastPrice, double bidPrice, double askPrice, int bidVolume,
			int askVolume) {// 平多
//		if (Prameter.preBuyCount == 0) {
//			return;
//		}
//
//		traderCall.closeBuy(askPrice - 1d, 1, "close");
//
//		// Prameter.preBuyCount = 0;
//
////		// 平多1手合约保证金
////		double sellMargin = bidPrice * Prameter.volumeMultiple * Prameter.longMarginRatio;
////		Prameter.available += Prameter.buyCount * (sellMargin - Prameter.closeRatioByVolume);
////		Prameter.recorder.writeMM(
////				updateTime + " -- closeBuy : " + String.valueOf(bidPrice) + "," + String.valueOf(Prameter.buyCount));
////		Prameter.buyCount = 0;
	}

	public void closeSell(String updateTime, double lastPrice, double bidPrice, double askPrice, int bidVolume,
			int askVolume) {// 平空
//		if (Prameter.preSellCount == 0) {
//			return;
//		}
//
//		traderCall.closeSell(bidPrice + 1d, 1, "close");
//
//		Prameter.preSellCount = 0;
//
////		 平空1手合约保证金
////		double buyMargin = askPrice * Prameter.volumeMultiple * Prameter.shortMarginRatio;
////		Prameter.available += Prameter.sellCount * (buyMargin - Prameter.closeRatioByVolume);
////		Prameter.recorder.writeMM(
////				updateTime + " -- closeSell : " + String.valueOf(askPrice) + "," + String.valueOf(Prameter.sellCount));
////		Prameter.sellCount = 0;
	}
}
