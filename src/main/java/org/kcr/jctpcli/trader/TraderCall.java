package org.kcr.jctpcli.trader;

import org.kcr.jctpcli.env.Instrument;
import org.kcr.jctpcli.env.OrderInfo;
import org.kcr.jctpcli.env.OrderItem;
import org.kcr.jctpcli.env.OrderTracker;
import org.kcr.jctpcli.util.Output;
import org.kr.jctp.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TraderCall implements ITrader{
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
		System.out.print("auth resulst:");
		System.out.println(r.resultCode);
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

	// 开多
	public boolean openBuy(Instrument instrument, double price, int volume) {
		CThostFtdcInputOrderField cof = genGFDOrder(instrument, price, volume, true, true);
		Output.pInputOrder("input order ---->", cof);
		var r = addOrder(cof);
		if (r.resultCode == 0) {
			// 如果返回不是0，表示订单没有发送成功
			// 添加到追踪hash表中
			orderTracker.OnOpenBuyReq(r.orderRef, volume, price, instrument.exchangeID, instrument.instrumentID);
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
			orderTracker.OnCloseBuyReq(r.orderRef, volume, price, instrument.exchangeID, instrument.instrumentID);
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
			orderTracker.OnOpenSellReq(r.orderRef, volume, price, instrument.exchangeID, instrument.instrumentID);
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
			orderTracker.OnCloseSellReq(r.orderRef, volume, price, instrument.exchangeID, instrument.instrumentID);
			return true;
		}
		return false;
	}

	// 撤单
	public void cancelOrder(OrderInfo order) {
		var o = genCancelOrder(order);
		var r = cancelOrder(o);
		if (r.resultCode == 0) {
			// 如果返回不是0，表示撤单没有发送成功
			// 更新追踪hash表中
			orderTracker.OnOrderCancelReq(order.orderRef);
		}
	}

	public TraderReq queryInstrumentCommissionRate(Instrument instrument) {
		var qry = genQryInstrumentCommissionRate(instrument);
		var r = genReq();
		r.resultCode = traderApi.ReqQryInstrumentCommissionRate(qry, r.requestID);
		return r;
	}

	// 查询投资者持仓
	public TraderReq queryInvestorPosition(String instrumentID) {
		var qry = genQryInvestorPosition(instrumentID);
		var r = genReq();
		r.resultCode = traderApi.ReqQryInvestorPosition(qry, r.requestID);
		return r;
	}

	// 查询资金账户
	public TraderReq queryTradeAccount(String currencyID) {
		var qry = genQryTradeAccount(currencyID);
		var r = genReq();
		r.resultCode = traderApi.ReqQryTradingAccount(qry, r.requestID);
		return r;
	}

	@Override
	public boolean needFence() {
		return true;
	}

	// 生成查询投资者持仓的对象
	private CThostFtdcQryInvestorPositionField genQryInvestorPosition(String instrumentID) {
		var qry = new CThostFtdcQryInvestorPositionField();
		qry.setBrokerID(brokerID);
		qry.setInvestorID(investorID);
		qry.setInstrumentID(instrumentID);
		return qry;
	}

	// 查询合约手续费
	// ReqQryInstrumentCommissionRate
	private CThostFtdcQryInstrumentCommissionRateField genQryInstrumentCommissionRate(Instrument instrument) {
		var qry = new CThostFtdcQryInstrumentCommissionRateField();
		qry.setBrokerID(brokerID);
		qry.setInvestorID(investorID);
		qry.setInstrumentID(instrument.instrumentID);
		qry.setExchangeID(instrument.exchangeID);

		return qry;

	}

	// 生成查询资金账户的对象
	private CThostFtdcQryTradingAccountField genQryTradeAccount(String currencyID) {
		var qry = new CThostFtdcQryTradingAccountField();
		qry.setBrokerID(brokerID);
		qry.setInvestorID(investorID);
		qry.setCurrencyID(currencyID);
		return qry;
	}

	// 查询合约
	public TraderReq queryInstrument(String instrumentID, String exchangeID) {
		var qry = genQryInstrument(instrumentID, exchangeID, "", "");
		var r = genReq();
		r.resultCode = traderApi.ReqQryInstrument(qry, r.requestID);
		return r;
	}


	// 生成查询合约的对象
	private CThostFtdcQryInstrumentField genQryInstrument(String instrumentID, String exchangeID,
														  String instrIDInExchange, String productID) {
		var qry = new CThostFtdcQryInstrumentField();
		qry.setInstrumentID(instrumentID);
		qry.setExchangeID(exchangeID);
		qry.setExchangeInstID(instrIDInExchange);
		qry.setProductID(productID);
		return qry;
	}

	private CThostFtdcInputOrderField genGFDOrder(
			Instrument instrument, double price, int vol, boolean isBuy, boolean isOpen) {
		return genOrder(instrument, price, vol, isBuy, isOpen, jctpConstants.THOST_FTDC_TC_GFD);
	}

	private CThostFtdcInputOrderField genFAKOrder(
			Instrument instrument, double price, int vol, boolean isBuy, boolean isOpen) {
		return genOrder(instrument, price, vol, isBuy, isOpen, jctpConstants.THOST_FTDC_TC_IOC);
	}

	// 生成订单的对象
	// isBuy ： buy:开多，平空 , sell:开空，平多
	/*
	 * combOffsetFlag :
	 * HOST_FTDC_OF_Open是开仓，THOST_FTDC_OF_Close是平仓/平昨，THOST_FTDC_OF_CloseToday是平今。
	 * 除了上期所/能源中心外，不区分平今平昨，平仓统一使用THOST_FTDC_OF_Close。
	 */
	private CThostFtdcInputOrderField genOrder(
			Instrument instrument, double price, int vol, boolean isBuy, boolean isOpen, char type) {
		var order = new CThostFtdcInputOrderField();
		//var orderRef = "ref-"+Long.toString(orderRefAtom.incrementAndGet());
		var orderRef = Long.toString(orderRefAtom.incrementAndGet());
		order.setBrokerID(brokerID); //broker id
		order.setInvestorID(investorID); //investor id
		order.setInstrumentID(instrument.instrumentID); //合约id
		order.setOrderRef(orderRef);
		// 暂时只使用投机的标记
		order.setCombHedgeFlag(HFSpeculation);
		order.setVolumeTotalOriginal(vol);
		order.setExchangeID(instrument.exchangeID);

		if (isBuy) {
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

		//额外添加的字段
		order.setAccountID(investorID);
		order.setClientID(appID);
		order.setCurrencyID("CNY");
		order.setForceCloseReason(jctpConstants.THOST_FTDC_FCC_NotForceClose);
		order.setGTDDate("");
		order.setIsAutoSuspend(0);
		order.setIsSwapOrder(0);
		order.setMinVolume(1);
		order.setUserID(investorID);
		order.setUserForceClose(0);
		return order;
	}

	// 撤单
	private OrderReq cancelOrder(CThostFtdcInputOrderActionField order) {
		var r = new OrderReq(reqIDAtom.getAndIncrement(), order.getOrderRef());
		order.setOrderActionRef(r.requestID);
		r.resultCode = traderApi.ReqOrderAction(order, r.requestID);
		return r;
	}

	// 报单
	private OrderReq addOrder(CThostFtdcInputOrderField order) {
		var r = new OrderReq(reqIDAtom.getAndIncrement(), order.getOrderRef());
		order.setRequestID(r.requestID);
		r.resultCode = traderApi.ReqOrderInsert(order, r.requestID);
		return r;
	}

	// 生成撤单的对象
	private CThostFtdcInputOrderActionField genCancelOrder(OrderInfo order) {
		var action = new CThostFtdcInputOrderActionField();
		action.setBrokerID(brokerID);
		action.setInvestorID(investorID);
		action.setExchangeID(order.orderItem.exchangeID);
		action.setOrderRef(order.orderRef);

		action.setUserID(investorID);
		action.setInstrumentID(order.orderItem.instrumentID);
		action.setFrontID(frontIDAtom.get());
		action.setSessionID(sessionIDAtom.get());
		action.setActionFlag(jctpConstants.THOST_FTDC_AF_Delete);
		return action;
	}

	// 生成request
	private TraderReq genReq() {
		return new TraderReq(reqIDAtom.getAndIncrement());
	}

}
