package org.kcr.jctpcli.core;

import org.kcr.jctpcli.cnf.Cnf;
import org.kcr.jctpcli.util.Recorder;
import org.kcr.jctpcli.util.Output;
import org.kr.jctp.*;

public class MdSpi extends CThostFtdcMdSpi {
	private final CThostFtdcMdApi mdApi;
	private final Broker broker;
	private final CThostFtdcReqUserLoginField loginField;
	private final String[] instruments;

	private Recorder recorder;

	public MdSpi(CThostFtdcMdApi _mdApi, Broker _broker, Cnf _cnf) {
		mdApi = _mdApi;
		broker = _broker;
		loginField = new CThostFtdcReqUserLoginField();
		loginField.setBrokerID(_cnf.getBrokerID());
		loginField.setUserID(_cnf.getAccountID());
		loginField.setPassword(_cnf.getPassword());
		instruments = _cnf.getSymbols();
		recorder = new Recorder();
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
		var md = new MarketData(pDepthMarketData);
		//System.out.println(md.brief());

		if (Parameter.debugMode) {
			System.out.println("行情信息:");
			System.out.println(md);
		}

		if (Parameter.recordMd) {
			recorder.writeMD(md.toString());
		}

		broker.lock();
		broker.marketProcess(md);
		broker.unlock();
	}

}
