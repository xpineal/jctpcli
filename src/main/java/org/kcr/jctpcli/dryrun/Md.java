package org.kcr.jctpcli.dryrun;

import org.kcr.jctpcli.cnf.Cnf;
import org.kcr.jctpcli.cnf.FJson;
import org.kcr.jctpcli.util.Output;
import org.kr.jctp.*;

class MdSpi extends CThostFtdcMdSpi{
	MdSpi(CThostFtdcMdApi mdApi, Cnf cnf)
	{
		this.mdApi =  mdApi;
		this.cnf = cnf;
	}

	@Override
	public void OnFrontConnected(){
		System.out.println("MD OnFrontConnected");
		CThostFtdcReqUserLoginField field = new CThostFtdcReqUserLoginField();
		field.setBrokerID(cnf.getBrokerID());
		field.setUserID(cnf.getAccountID());
		field.setPassword(cnf.getPassword());
		var r = mdApi.ReqUserLogin(field, 0);
		System.out.printf("MD mdApi.ReqUserLogin:%d\n", r);
	}

	@Override
	public void OnFrontDisconnected(int nReason) {
		System.out.printf("MD OnFrontDisconnected nReason: %d\n", nReason);
	};

	@Override
	public void OnHeartBeatWarning(int nTimeLapse) {
		System.out.printf("MD OnHeartBeatWarning time lapse:%d\n", nTimeLapse);
	}

	@Override
	public void OnRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin, CThostFtdcRspInfoField pRspInfo,
							   int nRequestID, boolean bIsLast) {
		if (Output.pResponse("MD OnRspUserLogin", pRspInfo, nRequestID, bIsLast)) {
			return;
		}
		if (pRspUserLogin == null) {
			System.out.println("MD OnRspUserLogin empty");
			return;
		}
		Output.pLoginInfo("MD", pRspUserLogin);
		var r = mdApi.SubscribeMarketData(cnf.getSymbols());
		System.out.printf("MD mdApi.SubscribeMarketData:%d\n", r);
	}

	@Override
	public void OnRspUserLogout(
			CThostFtdcUserLogoutField pUserLogout, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		if (Output.pResponse("MD OnRspUserLogout", pRspInfo, nRequestID, bIsLast)) {
			return;
		}
		if (pUserLogout == null) {
			System.out.println("MD OnRspUserLogout empty");
		}
	}

	@Override
	public void OnRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		Output.pResponse("MD OnRspError", pRspInfo, nRequestID, bIsLast);
	}

	@Override
	public void OnRspSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument,
								   CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		if (Output.pResponse("MD OnRspSubMarketData", pRspInfo, nRequestID, bIsLast)) {
			return;
		}
		if (pSpecificInstrument == null) {
			System.out.println("MD OnRspSubMarketData empty");
			return;
		}
		Output.pSpecificInstrument("MD", pSpecificInstrument);
	}

	@Override
	public void OnRtnDepthMarketData(CThostFtdcDepthMarketDataField pDepthMarketData) {
		Output.pDepthMarketData("MD", pDepthMarketData);
	}

	private CThostFtdcMdApi mdApi;
	private Cnf cnf;
}

public class Md {
	public static void main(String[] args) {
		if (jctpJNI.libraryLoaded()) {
			// do something with CTP, here we print its sdk version
			System.out.println(CThostFtdcTraderApi.GetApiVersion());
			// release native gc root in jni, jctp will be unavailable after doing this
			jctpJNI.release();
		} else {
			System.err.println("Library load failed!");
		}

		var cnf = FJson.readCnf("./cnf.json");
		cnf.refresh();
		System.out.println(cnf);

		CThostFtdcMdApi mdApi = CThostFtdcMdApi.CreateFtdcMdApi();
		MdSpi pMdSpi = new MdSpi(mdApi, cnf);
		mdApi.RegisterSpi(pMdSpi);
		mdApi.RegisterFront(cnf.getMdServer());
		mdApi.Init();
		mdApi.Join();
	}
}
