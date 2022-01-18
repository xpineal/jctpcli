package org.kcr.jctpcli;

import org.kr.jctp.CThostFtdcTraderApi;
import org.kr.jctp.jctpJNI;

public class DemoVersion {
    public static void main(String []args) throws InterruptedException {
        if (jctpJNI.libraryLoaded()) {
            // do something with CTP, here we print its sdk version
            System.out.println(CThostFtdcTraderApi.GetApiVersion());
            // release native gc root in jni, jctp will be unavailable after doing this
            jctpJNI.release();
        } else {
            System.err.println("Library load failed!");
        }
//        System.out.println(CThostFtdcMdApi.GetApiVersion());
//        System.out.println(CThostFtdcTraderApi.GetApiVersion());
    }
}
