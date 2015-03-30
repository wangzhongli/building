package com.e1858.building.httppackage;

import java.util.List;

import com.e1858.building.bean.OrderInfo;
import com.e1858.building.bean.PacketResp;
import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class GetNotCompleteOrdersResponse extends PacketResp {
	private List<OrderInfo>	orderInfos;

	public GetNotCompleteOrdersResponse() {
		command = HttpDefine.GETNOTCOMPLETEORDERS_RESP;
	}

	public List<OrderInfo> getOrderInfos() {
		return orderInfos;
	}

	public void setOrderInfos(List<OrderInfo> orderInfos) {
		this.orderInfos = orderInfos;
	}

}
