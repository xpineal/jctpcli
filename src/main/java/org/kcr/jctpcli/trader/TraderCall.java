package org.kcr.jctpcli.trader;

import org.kcr.jctpcli.env.Instrument;
import org.kcr.jctpcli.env.OrderTracker;
import org.kr.jctp.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TraderCall {
	// flag
	// 开平标记 THOST_FTDC_OF_CloseToday
	private static final String open = Character.toString(jctpConstants.THOST_FTDC_OF_Open);
	private static final String close = Character.toString(jctpConstants.THOST_FTDC_OF_Close);
	private static final String closeToday = Character.toString(jctpConstants.THOST_FTDC_OF_CloseToday);
	// 投机标记 -- 暂时只用投机标记
	private static final String HFSpeculation = Character.toString(jctpConstants.THOST_FTDC_HF_Speculation);

	private final CThostFtdcTraderApi traderApi;
	private final String brokerID;
	private final String investorID;
	private final String password;
	private final String appID;
	private final String authCode;

	private final AtomicInteger reqIDAtom;
	private final AtomicInteger frontIDAtom;
	private final AtomicInteger sessionIDAtom;
	private final AtomicLong orderRefAtom;

	// 订单跟踪
	private OrderTracker orderTracker;

	// 构造函数
	public TraderCall(CThostFtdcTraderApi _traderApi, OrderTracker _orderTracker,
					  String _brokerID, String _investorID, String _password, String _appID, String _authCode) {
		traderApi = _traderApi;
		orderTracker = _orderTracker;

		brokerID = _brokerID;
		investorID = _investorID;
		password = _password;
		appID = _appID;
		authCode = _authCode;

		reqIDAtom = new AtomicInteger(0);
		frontIDAtom = new AtomicInteger();
		sessionIDAtom = new AtomicInteger();
		orderRefAtom = new AtomicLong();
	}

	// 设置front/session/previous max order ref
	public void setAtom(int frontID, int sessionID, long orderRef) {
		frontIDAtom.set(frontID);
		sessionIDAtom.set(sessionID);
		orderRefAtom.set(orderRef);
	}

	// 认证
	public TraderReq authenticate() {
		var field = new CThostFtdcReqAuthenticateField();
		field.setBrokerID(brokerID);
		// field.setUserID(cnf.getAccountID());
		field.setAuthCode(authCode);
		field.setAppID(appID);
		var r = genReq();
		r.resultCode = traderApi.ReqAuthenticate(field, r.requestID);
		return r;
	}

	// 登录
	public TraderReq login() {
		var field = new CThostFtdcReqUserLoginField();
		field.setBrokerID(brokerID);
		field.setUserID(investorID);
		field.setPassword(password);
		var r = genReq();
		r.resultCode = traderApi.ReqUserLogin(field, r.requestID);
		return r;
	}

	// 查询当前交易日
	public String getTradingDay() {
		return traderApi.GetTradingDay();
	}

	// 生成查询报单信息的对象
	public CThostFtdcQryOrderField genQryOrder(String exchangeID, String instrument, String orderSysID,
			String startTime, String endTime) {
		var qry = new CThostFtdcQryOrderField();
		qry.setBrokerID(brokerID);
		qry.setInvestorID(investorID);
		qry.setExchangeID(exchangeID);
		qry.setInstrumentID(instrument);
		qry.setOrderSysID(orderSysID);
		qry.setInsertTimeStart(startTime);
		qry.setInsertTimeEnd(endTime);
		return qry;
	}

	// 生成查询报单信息的对象
	public CThostFtdcQryOrderField genQryOrder(String instrument, String exchangeID, String orderSysID) {
		return genQryOrder(instrument, exchangeID, orderSysID, "", "");
	}

	// 查询报单信息
	public TraderReq queryOrder(CThostFtdcQryOrderField qry) {
		var r = genReq();
		r.resultCode = traderApi.ReqQryOrder(qry, r.requestID);
		return r;
	}

//	// 查询合约保证金率
//	// ReqQryInstrumentMarginRate
//	public CThostFtdcQryInstrumentMarginRateField genQryInstrumentMarginRate(String instrumentID) {
//		var qry = new CThostFtdcQryInstrumentMarginRateField();
//		qry.setBrokerID(brokerID);
//		qry.setInvestorID(investorID);
//		qry.setInstrumentID(instrumentID);
//		qry.setHedgeFlag(jctpConstants.THOST_FTDC_HF_Speculation);
//
//		return qry;
//
//	}
//
//	public TraderReq queryInstrumentMarginRate(String instrumentID) {
//		var qry = genQryInstrumentMarginRate(instrumentID);
//		var r = genReq();
//		r.resultCode = traderApi.ReqQryInstrumentMarginRate(qry, r.requestID);
//		return r;
//	}

	// 查询合约手续费
	// ReqQryInstrumentCommissionRate
	public CThostFtdcQryInstrumentCommissionRateField genQryInstrumentCommissionRate(String instrumentID) {
		var qry = new CThostFtdcQryInstrumentCommissionRateField();
		qry.setBrokerID(brokerID);
		qry.setInvestorID(investorID);
		qry.setInstrumentID(instrumentID);

		return qry;

	}

	public TraderReq queryInstrumentCommissionRate(String instrumentID) {
		var qry = genQryInstrumentCommissionRate(instrumentID);
		var r = genReq();
		r.resultCode = traderApi.ReqQryInstrumentCommissionRate(qry, r.requestID);
		return r;
	}

	// 生成查询投资者持仓的对象
	public CThostFtdcQryInvestorPositionField genQryInvestorPosition(String instrumentID) {
		var qry = new CThostFtdcQryInvestorPositionField();
		qry.setBrokerID(brokerID);
		qry.setInvestorID(investorID);
		qry.setInstrumentID(instrumentID);
		return qry;
	}

	// 查询投资者持仓
	public TraderReq queryInvestorPosition(String instrumentID) {
		var qry = genQryInvestorPosition(instrumentID);
		var r = genReq();
		r.resultCode = traderApi.ReqQryInvestorPosition(qry, r.requestID);
		return r;
	}

//	// 生成查询持仓明细的对象
//	public CThostFtdcQryInvestorPositionDetailField genQryInvestorPositionDetail() {
//		var qry = new CThostFtdcQryInvestorPositionDetailField();
//		qry.setBrokerID(brokerID);
//		qry.setInvestorID(investorID);
//		qry.setInstrumentID("");
//		return qry;
//	}
//
//	// 查询持仓明细
//	public TraderReq queryInvestorPositionDetail() {
//		var qry = genQryInvestorPositionDetail();
//		var r = genReq();
//		r.resultCode = traderApi.ReqQryInvestorPositionDetail(qry, r.requestID);
//		return r;
//	}

	// 生成查询资金账户的对象
	public CThostFtdcQryTradingAccountField genQryTradeAccount(String currencyID) {
		var qry = new CThostFtdcQryTradingAccountField();
		qry.setBrokerID(brokerID);
		qry.setInvestorID(investorID);
		qry.setCurrencyID(currencyID);
		return qry;
	}

	// 查询资金账户
	public TraderReq queryTradeAccount(String currencyID) {
		var qry = genQryTradeAccount(currencyID);
		var r = genReq();
		r.resultCode = traderApi.ReqQryTradingAccount(qry, r.requestID);
		return r;
	}

//	// 生成查询投资者的对象
//	public CThostFtdcQryInvestorField genQryInvestor() {
//		var qry = new CThostFtdcQryInvestorField();
//		qry.setBrokerID(brokerID);
//		qry.setInvestorID(investorID);
//		return qry;
//	}
//
//	// 查询投资者
//	public TraderReq queryInvestor() {
//		var qry = genQryInvestor();
//		var r = genReq();
//		r.resultCode = traderApi.ReqQryInvestor(qry, r.requestID);
//		return r;
//	}

	// 生成查询单腿汇总的对象
//	public CThostFtdcQryInvestorPositionForCombField genQryInvestorPositionForComb() {
//		var qry = new CThostFtdcQryInvestorPositionForCombField();
//		qry.setBrokerID(brokerID);
//		qry.setInvestorID(investorID);
//		qry.setExchangeID("");
//		qry.setLegInstrumentID("");
//		return qry;
//	}

	// 查询单腿汇总
//	public TraderReq queryInvestorPositionForComb() {
//		var qry = genQryInvestorPositionForComb();
//		var r = genReq();
//		r.resultCode = traderApi.ReqQryInvestorPositionForComb(qry, r.requestID);
//		return r;
//	}

	// 生成查询合约的对象
	public CThostFtdcQryInstrumentField genQryInstrument(String instrumentID, String exchangeID,
			String instrIDInExchange, String productID) {
		var qry = new CThostFtdcQryInstrumentField();
		qry.setInstrumentID(instrumentID);
		qry.setExchangeID(exchangeID);
		qry.setExchangeInstID(instrIDInExchange);
		qry.setProductID(productID);
		return qry;
	}

	// 查询合约
	public TraderReq queryInstrument(String instrumentID, String exchangeID) {
		var qry = genQryInstrument(instrumentID, exchangeID, "", "");
		var r = genReq();
		r.resultCode = traderApi.ReqQryInstrument(qry, r.requestID);
		return r;
	}

	// 生成订单的对象
	// bsFlg ： buy:开多，平空 , sell:开空，平多
	/*
	 * combOffsetFlag :
	 * HOST_FTDC_OF_Open是开仓，THOST_FTDC_OF_Close是平仓/平昨，THOST_FTDC_OF_CloseToday是平今。
	 * 除了上期所/能源中心外，不区分平今平昨，平仓统一使用THOST_FTDC_OF_Close。
	 */
	public CThostFtdcInputOrderField genOrder(
			Instrument instrument, double price, int vol, boolean bsFlg, boolean isOpen, char type) {
		var order = new CThostFtdcInputOrderField();
		var orderRef = Long.toString(orderRefAtom.incrementAndGet());
		order.setBrokerID(brokerID);
		order.setInvestorID(investorID);
		order.setInstrumentID(instrument.instrumentID);
		order.setOrderRef(orderRef);
		// 暂时只使用投机的标记
		order.setCombHedgeFlag(HFSpeculation);
		order.setVolumeTotalOriginal(vol);
		order.setExchangeID(instrument.exchangeID);

		if (bsFlg) {
			order.setDirection(jctpConstants.THOST_FTDC_D_Buy);
		} else {
			order.setDirection(jctpConstants.THOST_FTDC_D_Sell);
		}
		if (isOpen) {
			order.setCombOffsetFlag(open);
		}else {
			if (instrument.closeToday) {
				order.setCombOffsetFlag(closeToday);
			}else{
				order.setCombOffsetFlag(close);
			}
		}

		order.setLimitPrice(price);
		order.setOrderPriceType(jctpConstants.THOST_FTDC_OPT_LimitPrice);
		order.setContingentCondition(jctpConstants.THOST_FTDC_CC_Immediately);
		order.setTimeCondition(type);
		order.setVolumeCondition(jctpConstants.THOST_FTDC_VC_AV);

		return order;
	}

	public CThostFtdcInputOrderField genFATOrder(
			Instrument instrument, double price, int vol, boolean bsFlg, boolean isOpen) {
		return genOrder(instrument, price, vol, bsFlg, isOpen, jctpConstants.THOST_FTDC_TC_IOC);
	}

	public CThostFtdcInputOrderField genGFDOrder(
			Instrument instrument, double price, int vol, boolean bsFlg, boolean isOpen) {
		return genOrder(instrument, price, vol, bsFlg, isOpen, jctpConstants.THOST_FTDC_TC_GFD);
	}

	// 开多
	public boolean openBuy(Instrument instrument, double price, int volume) {
		CThostFtdcInputOrderField cof = genGFDOrder(instrument, price, volume, true, true);
		var r = addOrder(cof);
		if (r.resultCode == 0) {
			// 如果返回不是0，表示订单没有发送成功
			// 添加到追踪hash表中
			orderTracker.OnOpenBuyReq(r.orderRef, volume, price);
			return true;
		}
		return false;
	}

	// 平多
	public boolean closeBuy(Instrument instrument, double price, int volume) {
		CThostFtdcInputOrderField cof = genGFDOrder(instrument, price, volume, false, false);
		var r = addOrder(cof);
		if (r.resultCode == 0) {
			// 如果返回不是0，表示订单没有发送成功
			// 添加到追踪hash表中
			orderTracker.OnCloseBuyReq(r.orderRef, volume, price);
			return true;
		}
		return false;
	}

	// 开空
	public boolean openSell(Instrument instrument, double price, int volume) {
		CThostFtdcInputOrderField cof = genGFDOrder(instrument, price, volume, false, true);
		var r = addOrder(cof);
		if (r.resultCode == 0) {
			// 如果返回不是0，表示订单没有发送成功
			// 添加到追踪hash表中
			orderTracker.OnOpenSellReq(r.orderRef, volume, price);
			return true;
		}
		return false;
	}

	// 平空
	public boolean closeSell(Instrument instrument, double price, int volume) {
		CThostFtdcInputOrderField cof = genGFDOrder(instrument, price, volume, true, false);
		var r = addOrder(cof);
		if (r.resultCode == 0) {
			// 如果返回不是0，表示订单没有发送成功
			// 添加到追踪hash表中
			orderTracker.OnCloseSellReq(r.orderRef, volume, price);
			return true;
		}
		return false;
	}

	// 批量撤单(撤出所有未成交的单)
	public void cancelAllNotTradeOrders() {
		// 先获取所有未成交订单
		var orderRefs = orderTracker.remainOrderKeys();
		for (String orderRef:orderRefs) {
			cancelOrder(orderRef);
		}
	}

	// 批量撤所有单
	public void cancelAllOrders() {
		// 先获取所有未成交订单
		var orderRefs = orderTracker.allOrderKeys();
		for (String orderRef:orderRefs) {
			cancelOrder(orderRef);
		}
	}

	// 撤单
	public void cancelOrder(String orderRef) {
		var order = genCancelOrder(orderRef);
		var r = cancelOrder(order);
		if (r.resultCode == 0) {
			// 如果返回不是0，表示撤单没有发送成功
			// 更新追踪hash表中
			orderTracker.OnOrderCancelReq(orderRef);
		}
	}

	// 撤单
	private OrderReq cancelOrder(CThostFtdcInputOrderActionField order) {
		var r = new OrderReq(reqIDAtom.getAndIncrement(), order.getOrderRef());
		r.resultCode = traderApi.ReqOrderAction(order, r.requestID);
		return r;
	}

	// 报单
	private OrderReq addOrder(CThostFtdcInputOrderField order) {
		var r = new OrderReq(reqIDAtom.getAndIncrement(), order.getOrderRef());
		r.resultCode = traderApi.ReqOrderInsert(order, r.requestID);
		return r;
	}

	// 生成撤单的对象
	private CThostFtdcInputOrderActionField genCancelOrder(String orderRef) {
		var action = new CThostFtdcInputOrderActionField();
		action.setOrderRef(orderRef);
		action.setFrontID(frontIDAtom.get());
		action.setSessionID(sessionIDAtom.get());
		action.setActionFlag(jctpConstants.THOST_FTDC_AF_Delete);
		return action;
	}

//	// 生成查询行情的对象
//	public CThostFtdcQryDepthMarketDataField genQryDepthMarketData(String instrumentID) {
//		var qry = new CThostFtdcQryDepthMarketDataField();
//		qry.setInstrumentID(instrumentID);
//		return qry;
//	}
//
//	// 查询行情
//	public TraderReq queryDepthMarketData(String instrumentID) {
//		var qry = genQryDepthMarketData(instrumentID);
//		var r = genReq();
//		r.resultCode = traderApi.ReqQryDepthMarketData(qry, r.requestID);
//		return r;
//	}

	// 生成request
	private TraderReq genReq() {
		return new TraderReq(reqIDAtom.getAndIncrement());
	}

}
