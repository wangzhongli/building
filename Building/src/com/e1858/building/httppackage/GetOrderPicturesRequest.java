package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

public class GetOrderPicturesRequest extends AutoFillPacketRequest {
	private String	orderID;
	private int		offset;
	private int		count;

	/**
	 * @param command
	 */
	public GetOrderPicturesRequest() {
		super(HttpDefine.GETORDERPICTURE);
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
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

}
