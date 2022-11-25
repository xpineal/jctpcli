package org.kcr.jctpcli.md;

import org.kcr.jctpcli.cnf.Cnf;
import org.kcr.jctpcli.old.Commerce;
import org.kcr.jctpcli.util.Output;
import org.kcr.jctpcli.old.Prameter;
import org.kr.jctp.*;

public class MixMdSpi extends CThostFtdcMdSpi {
	private final CThostFtdcMdApi mdApi;
	private final Commerce commerce;
	private final CThostFtdcReqUserLoginField loginField;
	private final String[] instruments;

	public MixMdSpi(CThostFtdcMdApi mdApi, Commerce commerce, Cnf cnf) {
		this.mdApi = mdApi;
		this.commerce = commerce;
		this.loginField = new CThostFtdcReqUserLoginField();
		loginField.setBrokerID(cnf.getBrokerID());
		loginField.setUserID(cnf.getAccountID());
		loginField.setPassword(cnf.getPassword());
		this.instruments = cnf.getSymbols();
		Prameter.initRecorder();
	}

	@Override
	public void OnFrontConnected() {
		System.out.println("连接上行情服务器");
		mdApi.ReqUserLogin(loginField, 0);
	}

	@Override
	public void OnFrontDisconnected(int nReason) {
		System.out.printf("行情服务器连接断开,原因: %d\n", nReason);
	}

	@Override
	public void OnHeartBeatWarning(int nTimeLapse) {
		System.out.printf("行情服务器心跳告警, 时间流逝:%d\n", nTimeLapse);
	}

	@Override
	public void OnRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin, CThostFtdcRspInfoField pRspInfo,
			int nRequestID, boolean bIsLast) {
		if (Output.pResponse("行情服务器登录", pRspInfo)) {
			return;
		}
		if (pRspUserLogin == null) {
			System.out.println("行情服务期返回用户登录信息为空");
			return;
		}
		mdApi.SubscribeMarketData(instruments);
	}

	@Override
	public void OnRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		Output.pResponse("行情服务回包错误", pRspInfo, nRequestID, bIsLast);
	}

	@Override
	public void OnRtnDepthMarketData(CThostFtdcDepthMarketDataField pDepthMarketData) {
		// 测试
		String updateTime = pDepthMarketData.getUpdateTime();
		double bidPrice = pDepthMarketData.getBidPrice1();
		int bidVolume = pDepthMarketData.getBidVolume1();
		double askPrice = pDepthMarketData.getAskPrice1();
		int askVolume = pDepthMarketData.getAskVolume1();
		double lastPrice = pDepthMarketData.getLastPrice();
		int volume = pDepthMarketData.getVolume();
		double turnover = pDepthMarketData.getTurnover();
//		double averagePrice = pDepthMarketData.getAveragePrice();
//		double openInterest = pDepthMarketData.getOpenInterest();
//		double preOpenInterest = pDepthMarketData.getPreOpenInterest();
//		double settlemenPrice = pDepthMarketData.getSettlementPrice();
//		double preSettlemenPrice = pDepthMarketData.getPreSettlementPrice();

		commerce.marketProcess(updateTime, lastPrice, bidPrice, askPrice, bidVolume, askVolume);

		Prameter.recorder.writeMD(updateTime + "," + Double.toString(bidPrice)
											 + "," + Integer.toString(bidVolume)
											 + "," + Double.toString(askPrice)
											 + "," + Integer.toString(askVolume)
											 + "," + Double.toString(lastPrice)
											 + "," + Integer.toString(volume)
											 + "," + Double.toString(turnover));
//											 + "," + Double.toString(averagePrice)
//											 + "," + Double.toString(preSettlemenPrice)
//											 + "," + Double.toString(settlemenPrice)
//											 + "," + Double.toString(preOpenInterest)
//											 + "," + Double.toString(openInterest)

//		if (Prameter.debugMode) {
//			Output.pDepthMarketData("行情", pDepthMarketData);
//		}
	}

}
