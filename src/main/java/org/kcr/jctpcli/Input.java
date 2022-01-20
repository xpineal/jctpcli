package org.kcr.jctpcli;

import java.util.Scanner;

public class Input {
    private static Scanner scanner = new Scanner(System.in);

    public static int inputMenu() {
        System.out.println("输入: 1-查询持仓 2-查询持仓详情 3-查询账户 4-查询投资者 5-查询组合");
        System.out.println("6-开买多 7-开买空 8-平买多 9-平买空 10-撤销单 11-查询行情 12-查询合约 13-开当天买多 14-查询订单");
        return scanner.nextInt();
    }

    public static double inputPrice() {
        System.out.println("请输入价格:");
        return scanner.nextDouble();
    }

    public static String inputOrderSysID() {
        System.out.println("请输入order sys id:");
        return scanner.next();
    }
}

