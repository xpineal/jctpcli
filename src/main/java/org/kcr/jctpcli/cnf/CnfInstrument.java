package org.kcr.jctpcli.cnf;

public class CnfInstrument {
	private String instrumentID;
	private String exchangeID;

	public CnfInstrument(String instrumentID, String exchangeID) {
		this.instrumentID = instrumentID;
		this.exchangeID = exchangeID;
	}

	public String getInstrumentID() {
		return instrumentID;
	}

	public void setInstrumentID(String instrumentID) {
		this.instrumentID = instrumentID;
	}

	public String getExchangeID() {
		return exchangeID;
	}

	public void setExchangeID(String exchangeID) {
		this.exchangeID = exchangeID;
	}

	@Override
	public String toString() {
		var sb = new StringBuffer(64);
		sb.append("instrumentID:").append(instrumentID).append("exchangeID:").append(exchangeID);
		return sb.toString();
	}
}
