package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class GetLogisticInfosRequest extends AutoFillPacketRequest {
	private int		offset;
	private int		count;
	private String	orderID;

	public GetLogisticInfosRequest() {
		super(HttpDefine.GETLOGISTICINFOS);
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

}
