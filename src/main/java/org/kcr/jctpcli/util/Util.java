package org.kcr.jctpcli.util;

public class Util {
    public static boolean traderTime(String updateTime) {
        int ut = Integer.parseInt(updateTime.replace(":", ""));

        if (ut >= 112955 && ut < 133000) {// 上午
            return true;
        }
        if (ut >= 145955 && ut < 210000) {// 下午
            return true;
        }
        if (ut >= 225955 || ut < 90000) {// 夜间
            return true;
        }

        return false;
    }
}
