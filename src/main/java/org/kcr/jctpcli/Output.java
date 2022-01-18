package org.kcr.jctpcli;

import org.jetbrains.annotations.NotNull;
import org.kr.jctp.*;

public class Output {
    //打印请求与同步返回值
    public static void pRequest(String head, @NotNull TraderReq r) {
        System.out.printf("%s req:%d -- result:%d\n", head, r.requestID, r.resultCode);
    }

    //打印"response"/"request id"/"is last"，如果有错，直接返回
    public static boolean pResponse(String head, CThostFtdcRspInfoField rsp, int rID, boolean bLast) {
        if (rsp != null && rsp.getErrorID() != 0)
        {
            System.out.printf("%s: error msg[%s], error id[%d], request id[%d]\n",
                    head, rsp.getErrorMsg(), rsp.getErrorID(), rID);
            return true;
        }
        System.out.printf("%s -- request id:%d, is last:%b\n", head, rID, bLast);
        return false;
    }

    //打印input order
    public static void pInputOrder(String head, @NotNull CThostFtdcInputOrderField pInputOrder) {
        System.out.println(head + " ---> :");
        System.out.printf("brokerID:%s, investorID:%s, instrumentID:%s, orderRef:%s, userID:%s, priceType:%c\n",
                pInputOrder.getBrokerID(), pInputOrder.getInvestorID(), pInputOrder.getInstrumentID(),
                pInputOrder.getOrderRef(), pInputOrder.getUserID(), pInputOrder.getOrderPriceType());
        System.out.printf("direction:%c, offsetFlag:%s, hedgeFlag:%s, limitPrice:%f, volumeTotal:%d, timeCondition:%c\n",
                pInputOrder.getDirection(), pInputOrder.getCombOffsetFlag(), pInputOrder.getCombHedgeFlag(),
                pInputOrder.getLimitPrice(), pInputOrder.getVolumeTotalOriginal(), pInputOrder.getTimeCondition());
        System.out.printf("gtdDate:%s, volumeCondition:%c, minVolume:%d, contingentCondition:%c, stopPrice:%f, forceCloseReason:%c\n",
                pInputOrder.getGTDDate(), pInputOrder.getVolumeCondition(), pInputOrder.getMinVolume(),
                pInputOrder.getContingentCondition(), pInputOrder.getStopPrice(), pInputOrder.getForceCloseReason());
        System.out.printf("autoSuspend:%d, businessUnit:%s, requestID:%d, userForceClose:%d, isSwapOrder:%d, exchangeID:%s\n",
                pInputOrder.getIsAutoSuspend(), pInputOrder.getBusinessUnit(), pInputOrder.getRequestID(),
                pInputOrder.getUserForceClose(), pInputOrder.getIsSwapOrder(), pInputOrder.getExchangeID());
        System.out.printf("investUnitID:%s, accountID:%s, currencyID:%s, clientID:%s, ipAddress:%s, macAddress:%s\n",
                pInputOrder.getInvestUnitID(), pInputOrder.getAccountID(), pInputOrder.getCurrencyID(),
                pInputOrder.getClientID(), pInputOrder.getIPAddress(), pInputOrder.getMacAddress());
    }

    //打印查询的订单信息
    public static void pOrder(String head, @NotNull CThostFtdcOrderField pOrder) {
        System.out.println(head + "query order ---> :");
        System.out.printf(
                "brokerID:%s, investorID:%s, instrumentID:%s, orderRef:%s, userID:%s, priceType:%c, direction:%c\n",
                pOrder.getBrokerID(), pOrder.getInvestorID(), pOrder.getInstrumentID(), pOrder.getOrderRef(),
                pOrder.getUserID(), pOrder.getOrderPriceType(), pOrder.getDirection());

        System.out.printf(
                "offsetFlag:%s, hedgeFlag:%s, limitPrice:%f, volumeTotal:%d, timeCondition:%c, gtdDate:%s\n",
                pOrder.getCombOffsetFlag(), pOrder.getCombHedgeFlag(), pOrder.getLimitPrice(),
                pOrder.getVolumeTotalOriginal(), pOrder.getTimeCondition(), pOrder.getGTDDate());

        System.out.printf(
                "volumeCondition:%c, minVolume:%d, contingentCondition:%c, stopPrice:%f, forceCloseReason:%c\n",
                pOrder.getVolumeCondition(), pOrder.getMinVolume(), pOrder.getContingentCondition(),
                pOrder.getStopPrice(), pOrder.getForceCloseReason());

        System.out.printf(
                "autoSuspend:%d, businessUnit:%s, requestID:%d, orderLocalID:%s, exchangeID:%s, participantID:%s\n",
                pOrder.getIsAutoSuspend(), pOrder.getBusinessUnit(), pOrder.getRequestID(), pOrder.getOrderLocalID(),
                pOrder.getExchangeID(), pOrder.getParticipantID());

        System.out.printf(
                "clientID:%s, instrInExchangeID:%s, traderID:%s, installID:%d, submitStatus:%c, notifySeq:%d\n",
                pOrder.getClientID(), pOrder.getExchangeInstID(), pOrder.getTraderID(), pOrder.getInstallID(),
                pOrder.getOrderSubmitStatus(), pOrder.getNotifySequence());

        System.out.printf(
                "tradingDay:%s, settleID:%d, orderSysID:%s, orderSource:%c, orderStatus:%c, orderType:%c\n",
                pOrder.getTradingDay(), pOrder.getSettlementID(), pOrder.getOrderSysID(),
                pOrder.getOrderSource(), pOrder.getOrderStatus(), pOrder.getOrderType());

        System.out.printf(
                "volTraded:%d, volTotal:%d, insertDate:%s, insertTime:%s, activeTime:%s, suspendTime:%s\n",
                pOrder.getVolumeTraded(), pOrder.getVolumeTotal(), pOrder.getInsertDate(),
                pOrder.getInsertTime(), pOrder.getActiveTime(), pOrder.getSuspendTime());

        System.out.printf(
                "updateTime:%s, cancelTime:%s, activeTraderID:%s, clearingPartID:%s, seqNo:%d, frontID:%d\n",
                pOrder.getUpdateTime(), pOrder.getCancelTime(), pOrder.getActiveTraderID(),
                pOrder.getClearingPartID(), pOrder.getSequenceNo(), pOrder.getFrontID());

        System.out.printf(
                "sessionID:%d, productInfo:%s, statusMsg:%s, userForceClose:%d, activeUserID:%s, brokerOrderSeq:%d\n",
                pOrder.getSessionID(), pOrder.getUserProductInfo(), pOrder.getStatusMsg(),
                pOrder.getUserForceClose(), pOrder.getActiveUserID(), pOrder.getBrokerOrderSeq());

        System.out.printf(
                "relativeOrderSysID:%s, ZCETotalTradedVol:%d, isSwapOrder:%d, branchID:%s, investUnitID:%s\n",
                pOrder.getRelativeOrderSysID(), pOrder.getZCETotalTradedVolume(), pOrder.getIsSwapOrder(),
                pOrder.getBranchID(), pOrder.getInvestUnitID());

        System.out.printf(
                "accountID:%s, currencyID:%s, ipAddress:%s, macAddress:%s\n",
                pOrder.getAccountID(), pOrder.getCurrencyID(), pOrder.getIPAddress(), pOrder.getMacAddress());
        System.out.println("");
    }

    //打印查询的交易信息
    public static void pTrade(String head, @NotNull CThostFtdcTradeField pTrade) {
        System.out.println(head + "query trade ---> :");
        System.out.printf(
                "brokerID:%s, investorID:%s, instrumentID:%s, orderRef:%s, userID:%s, exchangeID:%s, tradeID:%s\n",
                pTrade.getBrokerID(), pTrade.getInvestorID(), pTrade.getInstrumentID(), pTrade.getOrderRef(),
                pTrade.getUserID(), pTrade.getExchangeID(), pTrade.getTradeID());

        System.out.printf(
                "direction:%c, orderSysID:%s, participantID:%s, clientID:%s, tradingRole:%c, instrInExchangeID:%s\n",
                pTrade.getDirection(), pTrade.getOrderSysID(), pTrade.getParticipantID(),
                pTrade.getClientID(), pTrade.getTradingRole(), pTrade.getExchangeInstID());

        System.out.printf(
                "offsetFlag:%c, hedgeFlag:%c, price:%f, volume:%d, tradeDate:%s, tradeTime:%s, tradeType:%c\n",
                pTrade.getOffsetFlag(), pTrade.getHedgeFlag(), pTrade.getPrice(), pTrade.getVolume(),
                pTrade.getTradeDate(), pTrade.getTradeTime(), pTrade.getTradeType());

        System.out.printf(
                "priceSource:%c, tradeID:%s, orderLocalID:%s, clearingPartID:%s, businessUnit:%s, seqNo:%d\n",
                pTrade.getPriceSource(), pTrade.getTradeID(), pTrade.getOrderLocalID(), pTrade.getClearingPartID(),
                pTrade.getBusinessUnit(), pTrade.getSequenceNo());

        System.out.printf(
                "tradingDay:%s, settleID:%d, brokerOrderSeq:%d, tradeSource:%c\n",
                pTrade.getTradingDay(), pTrade.getSettlementID(), pTrade.getBrokerOrderSeq(), pTrade.getTradeSource());
        System.out.println("");
    }

    //打印查询投资者持仓
    public static void pInvestorPosition(String head, @NotNull CThostFtdcInvestorPositionField info) {
        System.out.println(head + "query investor position ---> :");
        System.out.printf(
                "instrumentID:%s, brokerID:%s, investorID:%s, 持仓多空方向:%c, 投机套保标志:%c, 持仓日期标志:%c, 上日持仓:%d\n",
                info.getInstrumentID(), info.getBrokerID(), info.getInvestorID(), info.getPosiDirection(),
                info.getHedgeFlag(), info.getPositionDate(), info.getYdPosition());

        System.out.printf(
                "交易日:%s, 持仓:%d, 今日持仓:%d, 多头冻结:%d, 多头冻结金额:%f, 空头冻结:%d, 空头冻结金额:%f, 开仓量:%d, 开仓金额:%f, 平仓量:%d, 平仓金额:%f\n",
                info.getTradingDay(), info.getPosition(), info.getTodayPosition(), info.getLongFrozen(),
                info.getLongFrozenAmount(), info.getShortFrozen(), info.getShortFrozenAmount(),
                info.getOpenVolume(), info.getOpenAmount(), info.getCloseVolume(), info.getCloseAmount());

        System.out.printf(
                "持仓成本:%f, 上次占用的保证金:%f, 占用的保证金:%f, 冻结的保证金:%f, 冻结的资金:%f, 冻结的手续费:%f, 资金差额:%f, 手续费:%f\n",
                info.getPositionCost(), info.getPreMargin(), info.getUseMargin(), info.getFrozenMargin(),
                info.getFrozenCash(), info.getFrozenCommission(), info.getCashIn(), info.getCommission());

        System.out.printf(
                "平仓盈亏:%f, 持仓盈亏:%f, 上次结算价:%f, 本次结算价:%f, 结算编号:%d, 开仓成本:%f, 交易所保证金:%f, 组合成交形成的持仓:%d\n",
                info.getCloseProfit(), info.getPositionProfit(), info.getPreSettlementPrice(),
                info.getSettlementPrice(), info.getSettlementID(), info.getOpenCost(),
                info.getExchangeMargin(), info.getCombPosition());

        System.out.printf(
                "组合多头冻结:%d, 组合空头冻结:%d, 逐日盯市平仓盈亏:%f, 逐笔对冲平仓盈亏:%f, 保证金率:%f, 保证金率(按手数):%f\n",
                info.getCombLongFrozen(), info.getCombShortFrozen(), info.getCloseProfitByDate(),
                info.getCloseProfitByTrade(), info.getMarginRateByMoney(), info.getMarginRateByVolume());

        System.out.printf(
                "执行冻结:%d, 执行冻结金额:%f, 放弃执行冻结:%d\n",
                info.getStrikeFrozen(), info.getStrikeFrozenAmount(), info.getAbandonFrozen());
        System.out.println("");
    }

    //打印查询的账户信息
    public static void pTradingAccount(String head, @NotNull CThostFtdcTradingAccountField account) {
        System.out.println(head + "query account ---> :");
        System.out.printf(
                "交易日:%s, brokerID:%s, accountID:%s, 上次质押金额:%f, 上次信用额度:%f, 上次存款额:%f, 上次结算准备金:%f, 上次占用的保证金:%f\n",
                account.getTradingDay(), account.getBrokerID(), account.getAccountID(), account.getPreMortgage(),
                account.getPreCredit(), account.getPreDeposit(), account.getPreBalance(), account.getPreMargin());

        System.out.printf(
                "利息基数:%f, 利息收入:%f, 入金金额:%f, 出金金额:%f, 冻结的保证金:%f, 冻结的资金:%f, 冻结的手续费:%f, 分仓冻结资金:%f\n",
                account.getInterestBase(), account.getInterest(), account.getDeposit(), account.getWithdraw(),
                account.getFrozenMargin(), account.getFrozenCash(), account.getFrozenCommission(),
                account.getFrozenPartition());

        System.out.printf(
                "当前保证金总额:%f, 资金差额:%f, 手续费:%f, 平仓盈亏:%f, 持仓盈亏:%f, 期货结算准备金:%f, 可用资金:%f, 可取资金:%f\n",
                account.getCurrMargin(), account.getCashIn(), account.getCommission(), account.getCloseProfit(),
                account.getPositionProfit(), account.getBalance(), account.getAvailable(), account.getWithdrawQuota());

        System.out.printf(
                "基本准备金:%f, 结算编号:%d, 信用额度:%f, 质押金额:%f, 交易所保证金:%f, 投资者交割保证金:%f, 交易所交割保证金:%f, 保底期货结算准备金:%f\n",
                account.getReserve(), account.getSettlementID(), account.getCredit(), account.getMortgage(),
                account.getExchangeMargin(), account.getDeliveryMargin(), account.getExchangeDeliveryMargin(),
                account.getReserveBalance());

        System.out.printf(
                "币种代码:%s, 上次货币质入金额:%f, 上次货币质出金额:%f, 货币质入金额:%f, 货币质出金额:%f, 货币质押余额:%f, 可质押货币金额:%f\n",
                account.getCurrencyID(), account.getPreFundMortgageIn(), account.getPreFundMortgageOut(),
                account.getFundMortgageIn(), account.getFundMortgageOut(), account.getFundMortgageAvailable(),
                account.getMortgageableFund());

        System.out.printf(
                "特殊产品 -> 占用保证金:%s, 冻结保证金:%f, 手续费:%f, 冻结手续费:%f, 持仓盈亏:%f, 平仓盈亏:%f, 持仓盈亏算法盈亏:%f, 交易所保证金:%f\n",
                account.getSpecProductMargin(), account.getSpecProductFrozenMargin(), account.getSpecProductCommission(),
                account.getSpecProductFrozenCommission(), account.getSpecProductPositionProfit(),
                account.getSpecProductCloseProfit(), account.getSpecProductPositionProfitByAlg(),
                account.getSpecProductExchangeMargin());
        System.out.println("");
    }

    //打印查询的投资者信息
    public static void pInvestor(String head, @NotNull CThostFtdcInvestorField account) {
        System.out.println(head + "query investor ---> :");
        System.out.printf(
                "brokerID:%s, investorID:%s, 分组代码:%s, Name:%s, ID类:%c, ID:%s, 是否活跃:%d\n",
                account.getBrokerID(), account.getInvestorID(), account.getInvestorGroupID(), account.getInvestorName(),
                account.getIdentifiedCardType(), account.getIdentifiedCardNo(), account.getIsActive());

        System.out.printf(
                "电话:%s, Address:%s, 开户日期:%s, 手机:%s, 手续费率模板代码:%s, 保证金率模板代码:%s\n",
                account.getTelephone(), account.getAddress(), account.getOpenDate(), account.getMobile(),
                account.getCommModelID(), account.getMarginModelID());
        System.out.println("");
    }

    //打印查询的交易编码
    public static void pTradingCode(String head, @NotNull CThostFtdcTradingCodeField tc) {
        System.out.println(head + "query trading code ---> :");
        System.out.printf(
                "brokerID:%s, investorID:%s, exchangeID:%s, clientID:%s, isActive:%c, clientIDType:%c\n",
                tc.getBrokerID(), tc.getInvestorID(), tc.getExchangeID(), tc.getClientID(), tc.getClientIDType());
    }

    //打印查询的合约保证金率
    public static void pInstrumentMarginRate(String head, @NotNull CThostFtdcInstrumentMarginRateField tc) {
        System.out.println(head + "query instrument margin rate ---> :");
        System.out.printf(
                "instrumentID:%s, 投资者范围:%c, brokerID:%s, investorID:%s\n",
                tc.getInstrumentID(), tc.getInvestorRange(), tc.getBrokerID(), tc.getInvestorID());
        System.out.printf(
                "hedgeFlag:%c, 多头保证金率:%f, 多头保证金费:%f, 空头保证金率:%f, 空头保证金费:%f, 是否相对交易所收取:%d\n",
                tc.getHedgeFlag(), tc.getLongMarginRatioByMoney(), tc.getLongMarginRatioByVolume(),
                tc.getShortMarginRatioByMoney(), tc.getShortMarginRatioByVolume(), tc.getIsRelative());
    }

    //打印查询的合约手续费率
    public static void pInstrumentCommissionRate(String head, @NotNull CThostFtdcInstrumentCommissionRateField tc) {
        System.out.println(head + "query instrument commission rate ---> :");
        System.out.printf(
                "instrumentID:%s, 投资者范围:%c, brokerID:%s, investorID:%s\n",
                tc.getInstrumentID(), tc.getInvestorRange(), tc.getBrokerID(), tc.getInvestorID());
        System.out.printf(
                "开仓手续费率:%f, 开仓手续费:%f, 平仓手续费率:%f, 平仓手续费:%f, 平今手续费率:%f, 平今手续费:%f\n",
                tc.getOpenRatioByMoney(), tc.getOpenRatioByVolume(), tc.getCloseRatioByMoney(),
                tc.getCloseRatioByVolume(), tc.getCloseTodayRatioByMoney(), tc.getCloseTodayRatioByVolume());
    }

    //打印查询的交易所
    public static void pExchange(String head, @NotNull CThostFtdcExchangeField tc) {
        System.out.println(head + "query exchange ---> :");
        System.out.printf(
                "exchangeID:%s, 交易所名称:%c, 交易所属性:%s\n",
                tc.getExchangeID(), tc.getExchangeName(), tc.getExchangeProperty());
    }

    //打印查询的合约
    public static void pInstrument(String head, @NotNull CThostFtdcInstrumentField tc) {
        System.out.println(head + "query instrument ---> :");
        System.out.printf(
                "instrumentID:%s, exchangeID:%s, 合约名称:%s, 合约在交易所的代码:%s\n",
                tc.getInstrumentID(), tc.getExchangeID(), tc.getInstrumentName(), tc.getExchangeInstID());

        System.out.printf(
                "产品代码:%s, 产品类型:%c, 交割年份:%d, 交割月:%d, 市价单下单量范围:[%d, %d], 限价单下单量范围[%d, %d]\n",
                tc.getProductID(), tc.getProductClass(), tc.getDeliveryYear(), tc.getDeliveryMonth(),
                tc.getMinMarketOrderVolume(), tc.getMaxMarketOrderVolume(),
                tc.getMinLimitOrderVolume(), tc.getMaxLimitOrderVolume());

        System.out.printf(
                "合约数量乘数:%d, 最小变动价位:%f, 创建日:%s, 上市日:%s, 到期日:%s, 开始交割日:%s, 结束交割日:%s, 合约生命周期状态:%c\n",
                tc.getVolumeMultiple(), tc.getPriceTick(), tc.getCreateDate(), tc.getOpenDate(),
                tc.getExpireDate(), tc.getStartDelivDate(), tc.getEndDelivDate(), tc.getInstLifePhase());

        System.out.printf(
                "当前是否交易:%d, 持仓类型:%c, 持仓日期类型:%c, 多头保证金率:%f, 空头保证金率:%f, 是否使用大额单边保证金算法:%c\n",
                tc.getIsTrading(), tc.getPositionType(), tc.getPositionDateType(), tc.getLongMarginRatio(),
                tc.getShortMarginRatio(), tc.getMaxMarginSideAlgorithm());

        System.out.printf(
                "基础商品代码:%s, 执行价:%f, 期权类型:%c, 合约基础商品乘数:%f, 组合类型:%c\n",
                tc.getUnderlyingInstrID(), tc.getStrikePrice(), tc.getOptionsType(), tc.getUnderlyingMultiple(),
                tc.getCombinationType());
        System.out.println("");
    }

    //打印查询的合约状态
    public static void pInstrumentStatus(String head, @NotNull CThostFtdcInstrumentStatusField tc) {
        System.out.println(head + "query instrument status ---> :");
        System.out.printf(
                "instrumentID:%s, exchangeID:%s, 结算组代码:%s, 合约在交易所的代码:%s\n",
                tc.getInstrumentID(), tc.getExchangeID(), tc.getSettlementGroupID(), tc.getExchangeInstID());

        System.out.printf(
                "合约交易状态:%c, 交易阶段编号:%d, 进入时间:%s, 进入本状态原因:%c\n",
                tc.getInstrumentStatus(), tc.getTradingSegmentSN(), tc.getEnterTime(), tc.getEnterReason());
        System.out.println("");
    }

    //打印查询投资者持仓明细
    public static void pInvestorPositionDetail(String head, @NotNull CThostFtdcInvestorPositionDetailField info) {
        System.out.println(head + "query investor position detail ---> :");
        System.out.printf(
                "交易日:%s, instrumentID:%s, exchangeID:%s, brokerID:%s, investorID:%s, 持仓多空方向:%c, 投机套保标志:%c\n",
                info.getTradingDay(), info.getInstrumentID(), info.getExchangeID(), info.getBrokerID(),
                info.getInvestorID(), info.getDirection(), info.getHedgeFlag());

        System.out.printf(
                "开仓日期:%s, 成交编号:%s, 数量:%d, 开仓价:%f, 结算编号:%d, 成交类型:%c, 组合合约代码:%s\n",
                info.getOpenDate(), info.getTradeID(), info.getVolume(), info.getOpenPrice(), info.getSettlementID(),
                info.getTradeType(), info.getCombInstrumentID());

        System.out.printf(
                "逐日盯市平仓盈亏:%f, 逐笔对冲平仓盈亏:%f, 逐日盯市持仓盈亏:%f, 逐笔对冲持仓盈亏:%f\n",
                info.getCloseProfitByDate(), info.getCloseProfitByTrade(),
                info.getPositionProfitByDate(), info.getPositionProfitByTrade());

        System.out.printf(
                "投资者保证金:%f, 交易所保证金:%f, 保证金率:%f, 保证金率(按手数):%f, 昨结算价:%f, 结算价:%f, 平仓量:%d, 平仓金额:%f\n",
                info.getMargin(), info.getExchMargin(), info.getMarginRateByMoney(),
                info.getMarginRateByVolume(), info.getLastSettlementPrice(), info.getSettlementPrice(),
                info.getCloseVolume(), info.getCloseAmount());
        System.out.println("");
    }

    //打印查询的交易所保证金率
    public static void pExchangeMarginRate(String head, @NotNull CThostFtdcExchangeMarginRateField tc) {
        System.out.println(head + "query exchange margin rate ---> :");
        System.out.printf(
                "instrumentID:%s, brokerID:%s\n",
                tc.getInstrumentID(),tc.getBrokerID());
        System.out.printf(
                "hedgeFlag:%c, 多头保证金率:%f, 多头保证金费:%f, 空头保证金率:%f, 空头保证金费:%f\n",
                tc.getHedgeFlag(), tc.getLongMarginRatioByMoney(), tc.getLongMarginRatioByVolume(),
                tc.getShortMarginRatioByMoney(), tc.getShortMarginRatioByVolume());
        System.out.println("");
    }

    //打印查询的调整保证金率
    public static void pExchangeMarginRateAdjust(String head, @NotNull CThostFtdcExchangeMarginRateAdjustField tc) {
        System.out.println(head + "query exchange margin rate ---> :");
        System.out.printf(
                "instrumentID:%s, brokerID:%s, hedgeFlag:%c\n",
                tc.getInstrumentID(),tc.getBrokerID(), tc.getHedgeFlag());
        System.out.printf(
                "跟随交易所投资者  多头保证金率:%f, 多头保证金费:%f, 空头保证金率:%f, 空头保证金费:%f\n",
                tc.getLongMarginRatioByMoney(), tc.getLongMarginRatioByVolume(),
                tc.getShortMarginRatioByMoney(), tc.getShortMarginRatioByVolume());

        System.out.printf(
                "交易所 多头保证金率:%f, 多头保证金率:%f, 空头保证金率:%f, 空头保证金费:%f\n",
                tc.getExchLongMarginRatioByMoney(), tc.getExchLongMarginRatioByVolume(),
                tc.getExchShortMarginRatioByMoney(), tc.getExchShortMarginRatioByVolume());

        System.out.printf(
                "不跟随交易所投资者 多头保证金率:%f, 多头保证金费:%f, 空头保证金率:%f, 空头保证金费:%f\n",
                tc.getNoLongMarginRatioByMoney(), tc.getNoLongMarginRatioByVolume(),
                tc.getNoShortMarginRatioByMoney(), tc.getNoShortMarginRatioByVolume());
        System.out.println("");
    }

    //打印登录信息
    public static void pLoginInfo(String head, @NotNull CThostFtdcRspUserLoginField info) {
        System.out.printf(
                "%s --> :\nfrontID:%d, sessionID:%d, tradingDay:%s, brokerID:%s, userID:%s, systemName:%s, maxOrderRef:%s, "+
                        "loginTime:%s, SHFETime:%s, DCETime:%s, CZCETime:%s, CFFEXTime:%s, INETime:%s\n", head,
                info.getFrontID(), info.getSessionID(), info.getTradingDay(), info.getBrokerID(), info.getUserID(),
                info.getSystemName(), info.getMaxOrderRef(),

                info.getLoginTime(), info.getSHFETime(), info.getDCETime(), info.getCZCETime(), info.getFFEXTime(),
                info.getINETime());
    }

    //打印合约ID
    public static void pSpecificInstrument(String head, @NotNull CThostFtdcSpecificInstrumentField instrument) {
        System.out.printf("%s instrumentID: %s\n", head, instrument.getInstrumentID());
    }

    //打印深度行情
    public static void pDepthMarketData(String head, CThostFtdcDepthMarketDataField pd) {
        if (pd == null) {
            System.out.printf("%s : OnRtnDepthMarketData empty\n", head);
        }else{
            System.out.println(head + " ---> :");
            System.out.printf("交易日:%s, 业务日期:%s, 最后修改时间:%s, 最后修改毫秒:%d\n",
                    pd.getTradingDay(), pd.getActionDay(), pd.getUpdateTime(), pd.getUpdateMillisec());
            System.out.printf("合约代码:%s, 交易所代码:%s, 合约在交易所的代码:%s, 最新价:%f, 上次结算价:%f\n",
                    pd.getInstrumentID(), pd.getExchangeID(), pd.getExchangeInstID(),
                    pd.getLastPrice(), pd.getPreSettlementPrice());
            System.out.printf(
                    "昨收盘:%f, 昨持仓量:%f, 今开盘:%f, 最高价:%f, 最低价:%f, 数量:%d, 当日均价:%f, 成交金额:%f, 持仓量:%f\n",
                    pd.getPreClosePrice(), pd.getPreOpenInterest(), pd.getOpenPrice(), pd.getHighestPrice(),
                    pd.getLowestPrice(), pd.getVolume(), pd.getAveragePrice(), pd.getTurnover(), pd.getOpenInterest());

            System.out.printf(
                    "今收盘:%f, 本次结算价:%f, 涨停板价:%f, 跌停板价:%f, 昨虚实度:%f, 今虚实度:%f, 上带价:%f, 下带价:%f\n",
                    pd.getClosePrice(), pd.getSettlementPrice(), pd.getUpperLimitPrice(), pd.getLowerLimitPrice(),
                    pd.getPreDelta(), pd.getCurrDelta(), pd.getBandingUpperPrice(), pd.getBandingUpperPrice());

            System.out.printf("买一价:%f -- :%d, 买二价:%f -- :%d, 买三价:%f -- :%d, 买四价:%f -- :%d, 买五价:%f -- :%d\n",
                    pd.getBidPrice1(), pd.getBidVolume1(), pd.getBidPrice2(), pd.getBidVolume2(),
                    pd.getBidPrice3(), pd.getBidVolume3(), pd.getBidPrice4(), pd.getBidVolume4(),
                    pd.getBidPrice5(), pd.getBidVolume5());

            System.out.printf("卖一价:%f -- :%d, 卖二价:%f -- :%d, 卖三价:%f -- :%d, 卖四价:%f -- :%d, 卖五价:%f -- :%d\n",
                    pd.getAskPrice1(), pd.getAskVolume1(), pd.getAskPrice2(), pd.getAskVolume2(),
                    pd.getAskPrice3(), pd.getAskVolume3(), pd.getAskPrice4(), pd.getAskVolume4(),
                    pd.getAskPrice5(), pd.getAskVolume5());

            System.out.println("");
        }
    }

    //打印撤单信息
    public static void pInputOrderAction(String head, CThostFtdcInputOrderActionField act) {
        System.out.println(head + " input order action ---> :");
        System.out.printf(
                "brokerID:%s, investorID:%s, orderActionRef:%d, orderRef:%s, requestID:%d, frontID:%d, sessionID:%d\n",
                act.getBrokerID(), act.getInvestorID(), act.getOrderActionRef(), act.getOrderRef(), act.getRequestID(),
                act.getFrontID(), act.getSessionID());

        System.out.printf(
                "exchangeID:%s, orderSysID:%s, actionFlag:%c, limitPrice:%f, volChange:%d, userID:%s, instrumentID:%s\n",
                act.getExchangeID(), act.getOrderSysID(), act.getActionFlag(), act.getLimitPrice(),
                act.getVolumeChange(), act.getUserID(), act.getInstrumentID());

        System.out.printf("investUnitID:%s, ipAddress:%s, macAddress:%s\n",
                act.getInvestUnitID(), act.getIPAddress(), act.getMacAddress());
    }

    //打印撤单信息
    public static void pOrderAction(String head, CThostFtdcOrderActionField act) {
        System.out.println(head + " order action ---> :");
        System.out.printf(
                "brokerID:%s, investorID:%s, orderActionRef:%d, orderRef:%s, requestID:%d, frontID:%d, sessionID:%d\n",
                act.getBrokerID(), act.getInvestorID(), act.getOrderActionRef(), act.getOrderRef(), act.getRequestID(),
                act.getFrontID(), act.getSessionID());

        System.out.printf(
                "exchangeID:%s, orderSysID:%s, actionFlag:%c, limitPrice:%f, volChange:%d, actionDate:%s, actionTime:%s\n",
                act.getExchangeID(), act.getOrderSysID(), act.getActionFlag(), act.getLimitPrice(),
                act.getVolumeChange(), act.getActionDate(), act.getActionTime());

        System.out.printf("traderID:%s, installID:%d, userID:%s, instrumentID:%s, investUnitID:%s, ipAddress:%s, macAddress:%s\n",
                act.getTraderID(), act.getInstallID(), act.getUserID(), act.getInstrumentID(), act.getInvestUnitID(),
                act.getIPAddress(), act.getMacAddress());
    }
}
