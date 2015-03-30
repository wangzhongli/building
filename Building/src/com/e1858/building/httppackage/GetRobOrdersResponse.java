package com.e1858.building.httppackage;

import java.util.List;

import com.e1858.building.bean.PacketResp;
import com.e1858.building.bean.RobOrder;
import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class GetRobOrdersResponse extends PacketResp {
	private List<RobOrder>	robOrders;

	public GetRobOrdersResponse() {
		command = HttpDefine.GETROBORDERS_RESP;
	}

	public List<RobOrder> getRobOrders() {
		return robOrders;
	}

	public void setRobOrders(List<RobOrder> robOrders) {
		this.robOrders = robOrders;
	}

}
