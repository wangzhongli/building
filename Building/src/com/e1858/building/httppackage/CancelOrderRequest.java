package com.e1858.building.httppackage;

import com.e1858.building.bean.CancelCause;
import com.e1858.building.net.HttpDefine;
import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class CancelOrderRequest extends AutoFillPacketRequest {
	@Expose
	private String		orderID	= "";	//订单信息ID
	private String		remark	= "";	//取消订单备注
	public CancelCause	cancelCause;

	public CancelOrderRequest() {
		super(HttpDefine.CANCELORDER);
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public CancelCause getCancelCause() {
		return cancelCause;
	}

	public void setCancelCause(CancelCause cancelCause) {
		this.cancelCause = cancelCause;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
