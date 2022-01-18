package org.kcr.jctpcli;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Cnf {
    private String tradeServer;
    private String mdServer;

    private String brokerID;
    private String accountID;
    private String password;

    private String appID;
    private String authCode;

    private String exchangeID;
    private CnfInstrument[] instruments;

    private String[] symbols;
    private Map<String, String> instrHash;

    public Cnf(String tradeServer, String mdServer, String brokerID,
               String accountID, String password, String appID,
               String authCode, String exchangeID, CnfInstrument[] instruments) {
        this.tradeServer = tradeServer;
        this.mdServer = mdServer;
        this.brokerID = brokerID;
        this.accountID = accountID;
        this.password = password;
        this.appID = appID;
        this.authCode = authCode;
        this.exchangeID = exchangeID;
        this.instruments = instruments;
        this.instrHash = new HashMap<String, String>();
    }

    public void refresh() {
        var l = instruments.length;
        symbols = new String[l];
        for (int i = 0; i < l; i++) {
            symbols[i] = instruments[i].getInstrumentID();
            instrHash.put(instruments[i].getInstrumentID(), instruments[i].getExchangeID());
        }
    }

    public String getInstrExchangeID(String instrumentID) {
        return instrHash.get(instrumentID);
    }

    public String[] getSymbols() {
        return symbols;
    }

    @Override
    public String toString() {
        return "Cnf{" +
                "tradeServer='" + tradeServer + '\'' +
                ", mdServer='" + mdServer + '\'' +
                ", brokerID='" + brokerID + '\'' +
                ", accountID='" + accountID + '\'' +
                ", password='" + password + '\'' +
                ", appID='" + appID + '\'' +
                ", authCode='" + authCode + '\'' +
                ", exchangeID='" + exchangeID + '\'' +
                ", instruments=" + Arrays.toString(instruments) +
                '}';
    }

    public String getTradeServer() {
        return tradeServer;
    }

    public void setTradeServer(String tradeServer) {
        this.tradeServer = tradeServer;
    }

    public String getMdServer() {
        return mdServer;
    }

    public void setMdServer(String mdServer) {
        this.mdServer = mdServer;
    }

    public String getBrokerID() {
        return brokerID;
    }

    public void setBrokerID(String brokerID) {
        this.brokerID = brokerID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getExchangeID() {
        return exchangeID;
    }

    public void setExchangeID(String exchangeID) {
        this.exchangeID = exchangeID;
    }

    public CnfInstrument[] getInstruments() {
        return instruments;
    }

    public void setInstruments(CnfInstrument[] instruments) {
        this.instruments = instruments;
    }
}
