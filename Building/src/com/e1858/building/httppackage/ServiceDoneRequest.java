package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class ServiceDoneRequest extends AutoFillPacketRequest {

	private String	orderID		= "";
	private String	serviceCode	= "";

	public ServiceDoneRequest() {
		super(HttpDefine.SERVICEDONE);
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

}
