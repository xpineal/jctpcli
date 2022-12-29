package org.kcr.jctpcli.core;

public class QueryReq {
	public int requestID;
	public int resultCode;

	public QueryReq(int rID) {
		requestID = rID;
	}

	@Override
	public String toString() {
		var sb = new StringBuffer(128);
		sb.append("requestID:").append(requestID).append(",");
		sb.append("resultCode:").append(resultCode);
		return sb.toString();
	}
}
