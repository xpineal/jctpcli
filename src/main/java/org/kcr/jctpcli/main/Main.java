package org.kcr.jctpcli.main;

import org.kcr.jctpcli.core.*;
import org.kcr.jctpcli.cnf.FJson;
import org.kcr.jctpcli.strategy.ManualStrategy;
import org.kr.jctp.CThostFtdcMdApi;
import org.kr.jctp.CThostFtdcTraderApi;
import org.kr.jctp.THOST_TE_RESUME_TYPE;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		// 输出测试信息
		if (args.length > 0) {
			if (args[0].equals("debug")) {
				Parameter.debugMode = true;
			}
		}

		// 读取配置文件
		var cnf = FJson.readCnf("./cnf.json");
		cnf.refresh();
		var instrList = cnf.getInstruments();
		if (instrList.length == 0) {
			System.out.println("没有相关的合约配置");
			return;
		}
		Parameter.cnf = cnf;
		System.out.println("配置文件读取成功");

		// 交易接口的封装
		var traderApi = CThostFtdcTraderApi.CreateFtdcTraderApi();
		// 持仓
		// 设置合约及交易所 注：暂考虑一个合约
		var hold = new Hand();
		// 下单对象
		var traderCall = new TraderCall(traderApi, hold.orderTracker,
				cnf.getBrokerID(), cnf.getAccountID(), cnf.getPassword(), cnf.getAppID(), cnf.getAuthCode());

		// 交易对象
		var broker = new Broker(traderCall,  new ManualStrategy(), hold);

		// 交易环境
		var tradeSpi = new TradeSpi(traderCall, hold);
		traderApi.RegisterSpi(tradeSpi);
		traderApi.SubscribePrivateTopic(THOST_TE_RESUME_TYPE.THOST_TERT_QUICK);
		traderApi.SubscribePublicTopic(THOST_TE_RESUME_TYPE.THOST_TERT_QUICK);
		traderApi.RegisterFront(cnf.getTradeServer());
		traderApi.Init();
		System.out.println("交易环境初始化");

		while (true) {
			if (tradeSpi.fence.ready()) {
				break;
			}
			Thread.sleep(300);
		}

		Thread.sleep(2000);
		System.out.println("交易环境初始化完成");
		// 打印合约
		//hold.instrument.outPut();

		// 行情环境
		var mdApi = CThostFtdcMdApi.CreateFtdcMdApi();
		var pMdSpi = new MdSpi(mdApi, broker, cnf);
		mdApi.RegisterSpi(pMdSpi);
		mdApi.RegisterFront(cnf.getMdServer());
		mdApi.Init();

		traderApi.Join();
		mdApi.Join();
	}
}
