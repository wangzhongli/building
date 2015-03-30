package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;
import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class PostOrderInfoRequest extends AutoFillPacketRequest {
	@Expose
	private String	orderID	= "";	//抢单信息ID

	public PostOrderInfoRequest() {
		super(HttpDefine.POSTORDERINFOS);
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

}
