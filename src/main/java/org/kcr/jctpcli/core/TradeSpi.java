package org.kcr.jctpcli.core;

import org.kr.jctp.jctpConstants;
import org.kcr.jctpcli.util.Output;
import org.kcr.jctpcli.util.Util;
import org.kr.jctp.*;

import java.util.*;


public class TradeSpi extends CThostFtdcTraderSpi {
	// 初始化标记
	public Fence fence;

	private ITrader traderCall;
	// 持仓对象
	private Hand hand;

	private ArrayDeque<Instrument> toQueryInstruments;
	private ArrayDeque<Instrument> toQueryInstrumentCommissions;
	private HashMap<Integer, String> queryCommissionHash;

	public TradeSpi(TraderCall _traderCall, Hand _hand) {
		traderCall = _traderCall;
		hand = _hand;
		fence = new Fence();
		toQueryInstruments = new ArrayDeque<>();
		toQueryInstrumentCommissions = new ArrayDeque<>();
		queryCommissionHash = new HashMap<>();
	}

	@Override
	public void OnFrontConnected() {
		System.out.println("连接上交易服务器");
		traderCall.authenticate();
	}

	@Override
	public void OnFrontDisconnected(int nReason) {
		System.out.printf("交易服务器连接断开,原因: %d\n", nReason);
	}

	@Override
	public void OnHeartBeatWarning(int nTimeLapse) {
		System.out.printf("交易服务器心跳告警，时间流逝: %d\n", nTimeLapse);
	}

	@Override
	public void OnRspAuthenticate(CThostFtdcRspAuthenticateField pRspAuthenticateField, CThostFtdcRspInfoField pRspInfo,
			int nRequestID, boolean bIsLast) {
		if (Output.pResponse("客户端认证响应", pRspInfo)) {
			return;
		}
		if (pRspAuthenticateField == null) {
			System.out.printf("客户端认证响应返回信息为空:%d-%b\n", nRequestID, bIsLast);
			return;
		}

		if (bIsLast) {
			if (Parameter.debugMode) {
				Output.pRspAuth("客户端认证响应", pRspAuthenticateField);
			}
			System.out.println("start to login --------------");
			traderCall.login();	
		}
		
	}

	@Override
	public void OnRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin, CThostFtdcRspInfoField pRspInfo,
			int nRequestID, boolean bIsLast) {
		if (Output.pResponse("登录请求响应", pRspInfo)) {
			return;
		}
		if (pRspUserLogin == null) {
			System.out.printf("登录请求响应返回信息为空:%d-%b\n", nRequestID, bIsLast);
			return;
		}

		// 设置登录后相关的字段
		setupEnvAfterLogin(pRspUserLogin);
		// 登录后查询相关数据
		// 查询资金账户
		if (bIsLast) {
			if (Parameter.debugMode) {
				Output.pLoginInfo("登录请求响应", pRspUserLogin);
			}
			Parameter.tradingDay = traderCall.getTradingDay();
			fence.doneLogin();
			Util.sleepAvoidTooFreq();
			System.out.println("start to query account --------------");
			traderCall.queryTradeAccount("");
			//setupParameterAfterLogin();	
		}		
	}

	// 设置登录后相关字段
	private void setupEnvAfterLogin(CThostFtdcRspUserLoginField rsp) {
		long rf;

		var frontID = rsp.getFrontID();
		var sessionID = rsp.getSessionID();
		var maxOrderRef = rsp.getMaxOrderRef();

		if (maxOrderRef != null && (!maxOrderRef.strip().equals(""))) {
			rf = Long.parseLong(maxOrderRef);
		} else {
			rf = 0;
		}
		traderCall.setAtom(frontID, sessionID, rf);
	}

	// 投资人信息
	/*@Override
	public void OnRspQryInvestor(CThostFtdcInvestorField pInvestor, CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
		if (Output.pResponse("投资人信息", pRspInfo, nRequestID, bIsLast)) {
			return;
		}
		if (pInvestor == null) {
			System.out.printf("投资人返回信息为空:%d-%b\n", nRequestID, bIsLast);
			return;
		}

		env.bInvestor = true;

		if (Prameter.debugMode) {
			Output.pInvestor("投资人信息", pInvestor);
		}
	}*/

	// 请求查询资金账户响应
	@Override
	public void OnRspQryTradingAccount(CThostFtdcTradingAccountField pTradingAccount, CThostFtdcRspInfoField pRspInfo,
			int nRequestID, boolean bIsLast) {
		if (Output.pResponse("请求查询资金账户响应", pRspInfo)) {
			return;
		}
		if (pTradingAccount == null) {
			System.out.printf("请求查询资金账户响应返回为空:%d-%b\n", nRequestID, bIsLast);
			return;
		}
		
		// 查询持仓
		// traderCall.queryInvestorPosition("");
				
		// 查询资金账户可用资金
		hand.available = pTradingAccount.getAvailable();
		fence.doneAccount();

		if (bIsLast) {
			if (Parameter.debugMode) {
				Output.pTradingAccount("请求查询资金账户响应", pTradingAccount);
			}
			// 查询合约
			queryInstruments();
		}
	}

	private void queryInstruments() {
		var ins = Parameter.cnf.getInstruments();
		var size = ins.length;
		for (int i = 0; i < size; i++) {
			toQueryInstruments.add(new Instrument(ins[i].getExchangeID(), ins[i].getInstrumentID()));
		}
		queryOneInstrument();
	}

	// 请求查询合约
	private boolean queryOneInstrument() {
		if (toQueryInstruments.size() == 0) {
			return false;
		}
		var inst = toQueryInstruments.removeFirst();
		Util.sleepAvoidTooFreq();
		var r = traderCall.queryInstrument(inst.instrumentID, inst.exchangeID);
		if (r.resultCode != 0) {
			System.out.printf("query instrument %s error code:%d\n", inst.instrumentID, r.resultCode);
		}
		return true;
	}

	// 请求查询合约响应
	@Override
	public void OnRspQryInstrument(CThostFtdcInstrumentField pInstrument, CThostFtdcRspInfoField pRspInfo,
								   int nRequestID, boolean bIsLast) {
		if (Output.pResponse("请求查询合约响应头", pRspInfo, nRequestID, bIsLast)) {
			return;
		}
		if (pInstrument == null) {
			System.out.println("请求查询合约响应返回信息为空");
			return;
		}

		if (pInstrument.getOptionsType() == 0) {
			if (Parameter.debugMode) {
				Output.pInstrument("请求查询合约响应", pInstrument);
			}
			// 设置保证金率 合约乘数 单位价格
			onRspQryInstrument(pInstrument);
			toQueryInstrumentCommissions.add(new Instrument(pInstrument.getExchangeID(), pInstrument.getInstrumentID()));
		}

		if (bIsLast) {
			if (toQueryInstruments.size() == 0) {
				fence.doneInstrument();
				queryOneInstrumentCommission();
			}else {
				queryOneInstrument();
			}
		}
	}

	// 合约信息处理
	private void onRspQryInstrument(CThostFtdcInstrumentField pInstrument) {
		hand.lock();
		try{
			// 设置保证金率 合约乘数 单位价格
			hand.upsertInstrument(pInstrument);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			hand.unlock();
		}
	}

	// 请求查询合约
	private boolean queryOneInstrumentCommission() {
		if (toQueryInstrumentCommissions.size() == 0) {
			return false;
		}
		var inst = toQueryInstrumentCommissions.removeFirst();
		Util.sleepAvoidTooFreq();
		var r = traderCall.queryInstrumentCommissionRate(inst);
		if (r.resultCode != 0) {
			System.out.printf("query instrument %s commission rate error code:%d\n", inst.instrumentID, r.resultCode);
		}
		queryCommissionHash.put(r.requestID, inst.instrumentID);
		return true;
	}

	// 请求查询合约手续费率响应
	@Override
	public void OnRspQryInstrumentCommissionRate(CThostFtdcInstrumentCommissionRateField pInstrumentCommissionRate,
												 CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		if (Output.pResponse("请求查询合约手续费率响应", pRspInfo, nRequestID, bIsLast)) {
			return;
		}
		if (pInstrumentCommissionRate == null) {
			System.out.println("请求查询合约手续费率响应返回信息为空");
			return;
		}

		if (Parameter.debugMode) {
			Output.pInstrumentCommissionRate("请求查询合约手续费率响应", pInstrumentCommissionRate);
		}

		var instrumentID = queryCommissionHash.get(nRequestID);
		if (instrumentID == null) {
			System.out.printf("get instrument in query commission hash map but it's null:%s", nRequestID);
			return;
		}
		// 查询手续费及率
		try {
			hand.lock();
			var existInst = hand.getInstrument(instrumentID);
			if (existInst == null) {
				System.out.printf("get instrument in query commission rate but it's null:%s", instrumentID);
				return;
			}
			existInst.setOpenCloseRatio(pInstrumentCommissionRate);
		}finally {
			hand.unlock();
		}

		if (bIsLast) {
			if (toQueryInstrumentCommissions.size() == 0) {
				fence.doneCommissionRate();
				Util.sleepAvoidTooFreq();
				traderCall.queryInvestorPosition("");
			}else {
				queryOneInstrumentCommission();
			}
		}
	}

	// 请求查询投资者持仓响应
	@Override
	public void OnRspQryInvestorPosition(CThostFtdcInvestorPositionField pInvestorPosition,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		if (Output.pResponse("请求查询投资者持仓响应", pRspInfo)) {
			return;
		}
		if (pInvestorPosition == null) {
			System.out.printf("请求查询投资者持仓响应:%d-%b\n", nRequestID, bIsLast);
			return;
		}

		if (Parameter.debugMode) {

			Output.pInvestorPosition("请求查询投资者持仓响应", pInvestorPosition);
		}

		try {
			hand.lock();
			var existInst = hand.getInstrument(pInvestorPosition.getInstrumentID());
			if (existInst == null) {
				System.out.println("get instrument in query investor position but it's null");
				if (bIsLast) {
					fence.doneInvestorPosition();
				}
				return;
			}
			var c = pInvestorPosition.getPosiDirection();
			if (c == jctpConstants.THOST_FTDC_PD_Long) {
				existInst.buyHold.feed(existInst, pInvestorPosition, true);
			}else {
				existInst.sellHold.feed(existInst, pInvestorPosition, false);
			}
		}finally {
			hand.unlock();
		}

		if (bIsLast) {
			fence.doneInvestorPosition();
			// 查询合约
			// queryInstruments();
		}
	}

	// 投资者持仓明细
	/*@Override
	public void OnRspQryInvestorPositionDetail(CThostFtdcInvestorPositionDetailField pInvestorPositionDetail,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		if (Output.pResponse("投资者持仓明细", pRspInfo, nRequestID, bIsLast)) {
			return;
		}
		if (pInvestorPositionDetail == null) {
			System.out.printf("投资者持仓明细返回为空:%d-%b\n", nRequestID, bIsLast);
			return;
		}

		if (Prameter.debugMode) {
			Output.pInvestorPositionDetail("投资者持仓明细", pInvestorPositionDetail);
		}

	}*/

	// 报单错误回报
	@Override
	public void OnErrRtnOrderInsert(CThostFtdcInputOrderField pInputOrder, CThostFtdcRspInfoField rsp) {
		if (rsp != null && rsp.getErrorID() != 0) {
			System.out.printf("报单错误: error msg[%s], error id[%d]\n", rsp.getErrorMsg(), rsp.getErrorID());
		}

		if (pInputOrder == null) {
			System.out.println("报单错误返回信息为空");
			return;
		}

		Output.pInputOrder("报单错误", pInputOrder);
		// TODO : 后续可以考虑在这里移除追踪的订单，按撤单成功处理
	}

	// 报单操作错误回报
	@Override
	public void OnErrRtnOrderAction(CThostFtdcOrderActionField pOrderAction, CThostFtdcRspInfoField rsp) {
		if (rsp != null && rsp.getErrorID() != 0) {
			System.out.printf("报单操作错误回报: error msg[%s], error id[%d]\n", rsp.getErrorMsg(), rsp.getErrorID());
		}

		if (pOrderAction == null) {
			System.out.println("报单操作错误回报返回信息为空");
			return;
		}

		if (Parameter.debugMode) {
			Output.pOrderAction("报单操作错误回报", pOrderAction);
		}

		// TODO : 撤单失败，理论上不用处理，因为前面的交易信息肯定回包了，后续可以观察
	}

	// 交易异常回包
	@Override
	public void OnRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		Output.pResponse("交易异常回包错误", pRspInfo, nRequestID, bIsLast);
	}

	// 报单通知
	@Override
	public void OnRtnOrder(CThostFtdcOrderField pOrder) {
		if (pOrder == null) {
			System.out.println("报单通知信息回包为空");
			return;
		}

		// 被动撤单和主动撤单都会返回此状态
		if (pOrder.getOrderStatus() == jctpConstants.THOST_FTDC_OST_Canceled) {
			// TODO 后续可以做一个序号跟踪保证可重入
			onRecall(pOrder);
		}else {
			onRtnOrder(pOrder);
		}

		if (Parameter.debugMode) {
			Output.pOrder("报单状态通知", pOrder);
		}
	}

	// 撤单处理
	private void onRecall(CThostFtdcOrderField pOrder) {
		hand.lock();
		try {
			hand.OnOrderCancelled(pOrder.getOrderRef());
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			hand.unlock();
		}
	}

	private void onRtnOrder(CThostFtdcOrderField pOrder) {
		hand.lock();
		try {
			catchManualOrder(pOrder);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			hand.unlock();
		}
	}

	// 捕捉手动订单
	private void catchManualOrder(CThostFtdcOrderField pOrder) {
		var orderRef = pOrder.getOrderRef();
		var order = hand.orderTracker.getOrder(orderRef);
		var instrumentID = pOrder.getInstrumentID();
		if (order == null) {
			//手动订单，原来的记录中没有此记录
			var instrument = hand.getInstrument(instrumentID);
			if (instrument == null) {
				System.out.printf("出现了没有合约信息的订单:%s - %s\n", instrumentID, orderRef);
				return;
			}
			var dir = figureDirection(pOrder);
			var orderItem = new OrderItem(pOrder.getVolumeTotalOriginal(), pOrder.getLimitPrice(), instrumentID);
			switch (dir) {
				case OpenBuy:
					hand.orderTracker.OnOpenBuyReq(orderRef, orderItem);
					hand.available -= instrument.openBuyCost(orderItem);
				case OpenSell:
					hand.orderTracker.OnOpenSellReq(orderRef, orderItem);
					hand.available -= instrument.openSellCost(orderItem);
				case CloseBuy:
					hand.orderTracker.OnCloseBuyReq(orderRef, orderItem);
					hand.available -= instrument.closeFee(orderItem);
				case CloseSell:
					hand.orderTracker.OnCloseSellReq(orderRef, orderItem);
					hand.available -= instrument.closeFee(orderItem);
			}
		}

		var exchangeID = pOrder.getExchangeID();
		var dir = pOrder.getDirection();
		var offsetFlag = pOrder.getCombOffsetFlag();

	}

	private Direction figureDirection(CThostFtdcOrderField pOrder) {
		var dir = pOrder.getDirection();
		var offsetFlag = pOrder.getCombOffsetFlag();

		if (offsetFlag.equals(TraderCall.open) && dir == jctpConstants.THOST_FTDC_D_Buy) {
			return Direction.OpenBuy;
		}
		if (offsetFlag.equals(TraderCall.open) && dir == jctpConstants.THOST_FTDC_D_Sell) {
			return Direction.OpenSell;
		}
		if (offsetFlag.equals(TraderCall.close) || offsetFlag.equals(TraderCall.closeToday)) {
			if (dir == jctpConstants.THOST_FTDC_D_Buy) {
				return Direction.CloseBuy;
			}
			if (dir == jctpConstants.THOST_FTDC_D_Sell) {
				return Direction.CloseSell;
			}
		}
		return Direction.None;
	}

	// 成交通知
	@Override
	public void OnRtnTrade(CThostFtdcTradeField pTrade) {
		if (pTrade == null) {
			System.out.println("成交通知信息回包为空");
			return;
		}
		// TODO 后续可以做一个序号跟踪保证可重入
		// pTrade.getSequenceNo()
		// 成交回包
		onTrade(pTrade);
		if (Parameter.debugMode) {
			Output.pTrade("成交通知", pTrade);
		}
	}

	// 成交处理
	private void onTrade(CThostFtdcTradeField pTrade) {
		hand.lock();
		try{
			hand.OnOrderTrade(pTrade.getOrderRef(), pTrade.getVolume(), pTrade.getPrice());
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			hand.unlock();
		}
	}

	// 报单录入请求响应
	@Override
	public void OnRspOrderInsert(CThostFtdcInputOrderField pInputOrder, CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
		if (Output.pResponse("报单录入请求响应", pRspInfo, nRequestID, bIsLast)) {
			return;
		}
		if (pInputOrder == null) {
			System.out.println("报单录入请求响应返回信息为空");
			return;
		}

		if (Parameter.debugMode) {
			Output.pInputOrder("报单录入请求响应", pInputOrder);
		}
		// TODO ：理论上不用处理，这里是mini的回包
	}

	// 报单操作请求响应
	@Override
	public void OnRspOrderAction(CThostFtdcInputOrderActionField pInputOrderAction, CThostFtdcRspInfoField pRspInfo,
			int nRequestID, boolean bIsLast) {
		if (Output.pResponse("报单操作请求响应", pRspInfo, nRequestID, bIsLast)) {
			return;
		}
		if (pInputOrderAction == null) {
			System.out.println("报单操作请求响应返回信息为空");
			return;
		}

		if (Parameter.debugMode) {
			Output.pInputOrderAction("报单操作请求响应", pInputOrderAction);
		}
		// TODO : 理论上不用处理，这里是mini的撤单回包
	}

	// 请求查询报单响应
	@Override
	public void OnRspQryOrder(CThostFtdcOrderField pOrder, CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
		if (Output.pResponse("请求查询报单响应", pRspInfo, nRequestID, bIsLast)) {
			return;
		}
		if (pOrder == null) {
			System.out.println("请求查询报单响应返回信息为空");
			return;
		}
		Output.pOrder("请求查询报单响应", pOrder);
	}

	// 请求查询成交响应
	@Override
	public void OnRspQryTrade(CThostFtdcTradeField pTrade, CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
		if (Output.pResponse("请求查询成交响应", pRspInfo, nRequestID, bIsLast)) {
			return;
		}
		if (pTrade == null) {
			System.out.println("请求查询成交响应返回信息为空");
			return;
		}

		Output.pTrade("请求查询成交响应", pTrade);
	}

	// 设置登录后相关参数
	/*private void setupParameterAfterLogin() {
		// 查询投资人信息
		// traderCall.queryInvestor();
		// 查询资金账户
		// traderCall.queryTradeAccount("");
		// 查询持仓
		// traderCall.queryInvestorPosition("");
		// 查询合约
		// traderCall.queryInstrument(hold.instrument.instrumentID, hold.instrument.exchangeID);
		// 查询保证金
		// traderCall.queryInstrumentMarginRate(Prameter.instrumentID);
		// 查询手续费
		// traderCall.queryInstrumentCommissionRate(hold.instrument.instrumentID);
	}*/
}
