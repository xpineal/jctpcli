package org.kcr.jctpcli.core;

//封装订单请求
public class OrderReq {
	public int requestID;
	public int resultCode;
	public String orderRef;

	public OrderReq(int rID, String orderRef) {
		this.requestID = rID;
		this.orderRef = orderRef;
	}

	@Override
	public String toString() {
		var sb = new StringBuffer(128);
		sb.append("requestID:").append(requestID).append(",");
		sb.append("resultCode:").append(resultCode).append(",");
		sb.append("orderRef:").append(orderRef);
		return sb.toString();
	}
}
