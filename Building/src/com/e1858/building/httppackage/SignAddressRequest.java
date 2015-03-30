package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;
import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class SignAddressRequest extends AutoFillPacketRequest {
	@Expose
	private String	orderID		= "";	//订单id
	private String	address		= "";	//签到当前地址
	private String	longitude	= "";	//当前经度
	private String	latitude	= "";	//当前纬度
	private int		type		= 0;	//签到类型  1，上门签到2提货签到

	public SignAddressRequest() {
		super(HttpDefine.SIGNADDRESS);
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
