package org.kcr.jctpcli;

import org.kcr.jctpcli.util.Output;

public class TraderReq {
    public int requestID;
    public int resultCode;

    public TraderReq(int rID) {
        requestID = rID;
    }

    @Override
    public String toString() {
        return "TradeReq{" +
                "requestID=" + requestID +
                ", resultCode=" + resultCode +
                '}';
    }
    
    public void outConsole(String head) {
        Output.pRequest(head, this);
    }
}
