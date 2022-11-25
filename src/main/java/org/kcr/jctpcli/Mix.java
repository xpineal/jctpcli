package org.kcr.jctpcli;

import org.kcr.jctpcli.cnf.FJson;
import org.kcr.jctpcli.old.Commerce;
import org.kcr.jctpcli.env.Fence;
import org.kcr.jctpcli.md.MixMdSpi;
import org.kcr.jctpcli.env.Broker;
import org.kcr.jctpcli.trader.MixTradeSpi;
import org.kcr.jctpcli.trader.TraderCall;
import org.kcr.jctpcli.old.Prameter;
import org.kr.jctp.CThostFtdcMdApi;
import org.kr.jctp.CThostFtdcTraderApi;
import org.kr.jctp.THOST_TE_RESUME_TYPE;

public class Mix {
	public static void main(String[] args) throws InterruptedException {
		// 输出测试信息
		if (args.length > 0) {
			if (args[0].equals("debug")) {
				Prameter.debugMode = true;
			}
		}
				
		Prameter.debugMode = true;
		// 读取配置文件
		var cnf = FJson.readCnf("./cnf.json");
		cnf.refresh();
		var instrList = cnf.getInstruments();
		if (instrList.length == 0) {
			System.out.println("没有相关的合约配置");
			return;
		}

		// 交易接口的封装
		var traderApi = CThostFtdcTraderApi.CreateFtdcTraderApi();
		// 设置合约及交易所 注：暂考虑一个合约
		var broker = new Broker(instrList[0].getExchangeID(), instrList[0].getInstrumentID());
		var traderCall = new TraderCall(traderApi, broker, cnf.getBrokerID(), cnf.getAccountID(), cnf.getPassword(),
				cnf.getAppID(), cnf.getAuthCode());
		// 交易环境
		var tradeSpi = new MixTradeSpi(traderCall, broker);
		traderApi.RegisterSpi(tradeSpi);
		traderApi.SubscribePrivateTopic(THOST_TE_RESUME_TYPE.THOST_TERT_QUICK);
		traderApi.SubscribePublicTopic(THOST_TE_RESUME_TYPE.THOST_TERT_QUICK);
		traderApi.RegisterFront(cnf.getTradeServer());
		traderApi.Init();

		while (true) {
			if (broker.fence.ready()) {
				break;
			}
			Thread.sleep(300);
		}

		// 打印合约
		broker.instrument.outPut();

		// 测试
		var commerce = new Commerce(traderCall);

		// 行情环境
		var mdApi = CThostFtdcMdApi.CreateFtdcMdApi();
		var pMdSpi = new MixMdSpi(mdApi, commerce, cnf);
		mdApi.RegisterSpi(pMdSpi);
		mdApi.RegisterFront(cnf.getMdServer());
		mdApi.Init();

		traderApi.Join();
		mdApi.Join();
	}
}
