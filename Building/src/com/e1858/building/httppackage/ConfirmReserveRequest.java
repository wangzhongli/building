package com.e1858.building.httppackage;

import com.e1858.building.bean.CancelCause;
import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class ConfirmReserveRequest extends AutoFillPacketRequest {
	private String		orderID;
	private String		reserveTime;
	private CancelCause	cause;
	private String		remark;

	public ConfirmReserveRequest() {
		super(HttpDefine.CONFIRMRESERVE);
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getReserveTime() {
		return reserveTime;
	}

	public void setReserveTime(String reserveTime) {
		this.reserveTime = reserveTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public CancelCause getCause() {
		return cause;
	}

	public void setCause(CancelCause cause) {
		this.cause = cause;
	}

}
