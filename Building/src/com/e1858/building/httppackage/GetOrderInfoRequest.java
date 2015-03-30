package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;
import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class GetOrderInfoRequest extends AutoFillPacketRequest {
	@Expose
	private String	orderID	= "";	//抢单信息ID

	public GetOrderInfoRequest() {
		super(HttpDefine.GETROBORDERINFO);
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

}
