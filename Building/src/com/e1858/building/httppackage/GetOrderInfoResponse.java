package com.e1858.building.httppackage;

import com.e1858.building.bean.OrderInfo;
import com.e1858.building.bean.PacketResp;
import com.e1858.building.net.HttpDefine;
import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class GetOrderInfoResponse extends PacketResp {
	@Expose
	private OrderInfo	orderInfo;

	public GetOrderInfoResponse() {
		command = HttpDefine.GETROBORDERINFO_RESP;
	}

	public OrderInfo getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}
}
