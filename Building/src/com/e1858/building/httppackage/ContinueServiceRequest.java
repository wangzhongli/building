package com.e1858.building.httppackage;

import com.e1858.building.bean.CancelCause;
import com.e1858.building.net.HttpDefine;

/**
 * 功能：
 * 日期: 2015-2-4 上午11:19:16
 * 作者: wangzhongli
 */
@SuppressWarnings("serial")
public class ContinueServiceRequest extends AutoFillPacketRequest {
	public String		orderID;
	private String		reServiceTime;
	private String		remark;
	private CancelCause	cancelCause;

	public ContinueServiceRequest() {
		super(HttpDefine.CONTINUESERVICE);
	}

	public String getReServiceTime() {
		return reServiceTime;
	}

	public void setReServiceTime(String reServiceTime) {
		this.reServiceTime = reServiceTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public CancelCause getCancelCause() {
		return cancelCause;
	}

	public void setCancelCause(CancelCause cancelCause) {
		this.cancelCause = cancelCause;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

}
