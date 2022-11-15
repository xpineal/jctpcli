package org.kcr.jctpcli.trader;

import org.kcr.jctpcli.env.EnvCtn;
import org.kcr.jctpcli.util.Output;
import org.kcr.jctpcli.util.Prameter;
import org.kr.jctp.*;

public class MixTradeSpi extends CThostFtdcTraderSpi {
	private final TraderCall traderCall;
	// 行情交易对象
	private final EnvCtn env;

	public MixTradeSpi(TraderCall traderCall, EnvCtn env) {
		this.traderCall = traderCall;
		this.env = env;
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

		traderCall.login();

		if (Prameter.debugMode) {
			Output.pRspAuth("客户端认证响应", pRspAuthenticateField);
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
		setupPrameterAfterLogin();

		Prameter.tradingDay = traderCall.getTradingDay();

		env.bLogin = true;

		if (Prameter.debugMode) {
			Output.pLoginInfo("登录请求响应", pRspUserLogin);
		}
	}

//	// 投资人信息
//	@Override
//	public void OnRspQryInvestor(CThostFtdcInvestorField pInvestor, CThostFtdcRspInfoField pRspInfo, int nRequestID,
//			boolean bIsLast) {
//		if (Output.pResponse("投资人信息", pRspInfo, nRequestID, bIsLast)) {
//			return;
//		}
//		if (pInvestor == null) {
//			System.out.printf("投资人返回信息为空:%d-%b\n", nRequestID, bIsLast);
//			return;
//		}
//
//		env.bInvestor = true;
//
//		if (Prameter.debugMode) {
//			Output.pInvestor("投资人信息", pInvestor);
//		}
//	}

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
		// 查询资金账户可用资金
		Prameter.setAvailable(pTradingAccount);
		env.bAccount = true;

		if (Prameter.debugMode) {
			Output.pTradingAccount("请求查询资金账户响应", pTradingAccount);
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

		// pInvestorPosition.getPositionDate();// ‘1’ 当日仓 ‘2‘ 历史仓

		if (Prameter.debugMode) {
			Output.pInvestorPosition("请求查询投资者持仓响应", pInvestorPosition);
		}
	}

//	// 投资者持仓明细
//	@Override
//	public void OnRspQryInvestorPositionDetail(CThostFtdcInvestorPositionDetailField pInvestorPositionDetail,
//			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
//		if (Output.pResponse("投资者持仓明细", pRspInfo, nRequestID, bIsLast)) {
//			return;
//		}
//		if (pInvestorPositionDetail == null) {
//			System.out.printf("投资者持仓明细返回为空:%d-%b\n", nRequestID, bIsLast);
//			return;
//		}
//
//		if (Prameter.debugMode) {
//			Output.pInvestorPositionDetail("投资者持仓明细", pInvestorPositionDetail);
//		}
//
//	}
//
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

		if (Prameter.debugMode) {
			Output.pInputOrder("报单错误", pInputOrder);
		}

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

		if (Prameter.debugMode) {
			Output.pOrderAction("报单操作错误回报", pOrderAction);
		}

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
		
		// 如果报单被交易所拒绝，此处就返回被动撤单
		if (pOrder.getOrderStatus() == jctpConstants.THOST_FTDC_OST_Canceled) {			
			env.orderCancel(pOrder);
		}

		if (Prameter.debugMode) {
			Output.pOrder("报单通知", pOrder);
		}
	}

	// 成交通知
	@Override
	public void OnRtnTrade(CThostFtdcTradeField pTrade) {
		if (pTrade == null) {
			System.out.println("成交通知信息回包为空");
			return;
		}
		// 成交回报
		env.orderTrade(pTrade);

		if (Prameter.debugMode) {
			Output.pTrade("成交通知", pTrade);
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

		if (Prameter.debugMode) {
			Output.pInputOrder("报单录入请求响应", pInputOrder);
		}

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

		if (Prameter.debugMode) {
			Output.pInputOrderAction("报单操作请求响应", pInputOrderAction);
		}

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

		if (Prameter.debugMode) {
			Output.pOrder("请求查询报单响应", pOrder);
		}

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

		if (Prameter.debugMode) {
			Output.pTrade("请求查询成交响应", pTrade);
		}
	}

	// 请求查询合约响应
	@Override
	public void OnRspQryInstrument(CThostFtdcInstrumentField pInstrument, CThostFtdcRspInfoField pRspInfo,
			int nRequestID, boolean bIsLast) {
		if (Output.pResponse("请求查询合约响应", pRspInfo, nRequestID, bIsLast)) {
			return;
		}
		if (pInstrument == null) {
			System.out.println("请求查询合约响应返回信息为空");
			return;
		}

		// 设置保证金率 合约乘数 单位价格
		Prameter.setInstrutmentRatio(pInstrument);
		env.bInstrument = true;

		if (Prameter.debugMode) {
			Output.pInstrument("请求查询合约响应", pInstrument);
		}
	}

//	// 保证金率或保证金
//	@Override
//	public void OnRspQryInstrumentMarginRate(CThostFtdcInstrumentMarginRateField pInstrumentMarginRate,
//			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
//		if (Output.pResponse("保证金率或保证金", pRspInfo, nRequestID, bIsLast)) {
//			return;
//		}
//		if (pInstrumentMarginRate == null) {
//			System.out.println("保证金率或保证金返回信息为空");
//			return;
//		}
//		// 查询保证金率及费
//		// env.bInstrumentMarginRate = true;
//
//		if (Prameter.debugMode) {
//			Output.pInstrumentMarginRate("保证金率或保证金", pInstrumentMarginRate);
//		}
//
//	}

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
		// 查询手续费及率
		Prameter.setRatio(pInstrumentCommissionRate);
		env.bInstrumentCommissionRate = true;

		if (Prameter.debugMode) {
			Output.pInstrumentCommissionRate("请求查询合约手续费率响应", pInstrumentCommissionRate);
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

	// 设置登录后相关参数
	private void setupPrameterAfterLogin() {
		// 查询投资人信息
		// traderCall.queryInvestor();
		// 查询资金账户
		traderCall.queryTradeAccount("");
		// 查询持仓
		traderCall.queryInvestorPosition("");
		// 查询合约
		traderCall.queryInstrument(Prameter.instrumentID, Prameter.exchangeID);
		// 查询保证金
		// traderCall.queryInstrumentMarginRate(Prameter.instrumentID);
		// 查询手续费
		traderCall.queryInstrumentCommissionRate(Prameter.instrumentID);

	}
}
