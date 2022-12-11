package org.kcr.jctpcli.core;

import org.kcr.jctpcli.util.Output;

public class TraderReq {
	public int requestID;
	public int resultCode;

	public TraderReq(int rID) {
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
