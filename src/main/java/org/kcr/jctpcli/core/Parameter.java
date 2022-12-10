package org.kcr.jctpcli.core;

import org.kcr.jctpcli.cnf.Cnf;

public class Parameter {
    // 交易日
    public static String tradingDay;
    // 调试模式
    public static boolean debugMode = false;
    // 记录行情
    public static boolean recordMd = false;

    // 配置
    public static Cnf cnf;

    // 报单引用前缀
    public static String orderRefPrefix = "ref";
}
