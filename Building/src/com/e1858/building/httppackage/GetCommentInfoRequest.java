package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class GetCommentInfoRequest extends AutoFillPacketRequest {
	private String	orderID;

	public GetCommentInfoRequest() {
		super(HttpDefine.GETCOMMENTINFO);
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

}
