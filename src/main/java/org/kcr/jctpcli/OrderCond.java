package org.kcr.jctpcli;

import org.jetbrains.annotations.NotNull;
import org.kr.jctp.CThostFtdcInputOrderField;
import org.kr.jctp.jctpConstants;

public class OrderCond {
    //exchange id
    public static final String SHFE = "SHFE";
    public static final String CFFEX = "CFFEX";
    public static final String DCE = "DCE";
    public static final String CZCE = "CZCE";
    public static final String INE = "INE";

    public char OrderPriceType;
    public char ContingentCondition;
    public char TimeCondition;
    public char VolumeCondition;

    public OrderCond(char priceType, char contCondition, char timeCondition, char volCondition){
        OrderPriceType = priceType;
        ContingentCondition = contCondition;
        TimeCondition = timeCondition;
        VolumeCondition = volCondition;
    }

    //开限价单
    public static void FAKLimitPrice(@NotNull CThostFtdcInputOrderField input) {
        OrderCond cond;
        switch (input.getExchangeID()){
            case SHFE:
                cond = LimitPriceFAK_SHFE;
                break;
            case CFFEX:
                cond = LimitPriceFAK_CFFEX;
                break;
            case DCE:
                cond = LimitPriceFAK_DCE;
                break;
            case CZCE:
                cond = LimitPriceFAK_CZCE;
                break;
            case INE:
                cond = LimitPriceFAK_INE;
                break;
            default:
                return;
        }
        SetCond(input, cond);
    }

    //开限价单
    public static void GFDLimitPrice(@NotNull CThostFtdcInputOrderField input) {
        OrderCond cond;
        switch (input.getExchangeID()){
            case SHFE:
                cond = LimitPrice_SHFE;
                break;
            case CFFEX:
                cond = LimitPrice_CFFEX;
                break;
            case DCE:
                cond = LimitPrice_DCE;
                break;
            case CZCE:
                cond = LimitPrice_CZCE;
                break;
            case INE:
                cond = LimitPrice_INE;
                break;
            default:
                return;
        }
        SetCond(input, cond);
    }

    //设置订单条件
    public static void SetCond(@NotNull CThostFtdcInputOrderField input, @NotNull OrderCond cond) {
        input.setOrderPriceType(cond.OrderPriceType);
        input.setContingentCondition(cond.ContingentCondition);
        input.setTimeCondition(cond.TimeCondition);
        input.setVolumeCondition(cond.VolumeCondition);
    }

    //郑商所
    public static final OrderCond LimitPrice_CZCE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_GFD,
            jctpConstants.THOST_FTDC_VC_AV
    );
    public static final OrderCond LimitPriceFAK_CZCE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_IOC,
            jctpConstants.THOST_FTDC_VC_AV
    );

    //大商所
    public static final OrderCond LimitPrice_DCE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_GFD,
            jctpConstants.THOST_FTDC_VC_AV
    );
    public static final OrderCond LimitPriceFAK_DCE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_IOC,
            jctpConstants.THOST_FTDC_VC_AV
    );

    //上期所 -- 只支持限价单
    public static final OrderCond LimitPrice_SHFE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_GFD,
            jctpConstants.THOST_FTDC_VC_AV
    );
    public static final OrderCond LimitPriceFAK_SHFE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_IOC,
            jctpConstants.THOST_FTDC_VC_AV
    );

    //中金所
    public static final OrderCond LimitPrice_CFFEX = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_GFD,
            jctpConstants.THOST_FTDC_VC_AV
    );

    public static final OrderCond LimitPriceFAK_CFFEX = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_IOC,
            jctpConstants.THOST_FTDC_VC_AV
    );

    //能源所 -- 没有具体文档，使用与郑商所同样配置
    public static final OrderCond LimitPrice_INE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_GFD,
            jctpConstants.THOST_FTDC_VC_AV
    );
    public static final OrderCond LimitPriceFAK_INE = new OrderCond(
            jctpConstants.THOST_FTDC_OPT_LimitPrice,
            jctpConstants.THOST_FTDC_CC_Immediately,
            jctpConstants.THOST_FTDC_TC_IOC,
            jctpConstants.THOST_FTDC_VC_AV
    );
}

