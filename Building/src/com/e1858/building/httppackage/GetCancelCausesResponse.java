package com.e1858.building.httppackage;

import java.util.List;

import com.e1858.building.bean.CancelCause;
import com.e1858.building.bean.PacketResp;
import com.e1858.building.net.HttpDefine;

/**
 * 功能：
 * 日期: 2015-2-4 上午11:22:22
 * 作者: wangzhongli
 */
@SuppressWarnings("serial")
public class GetCancelCausesResponse extends PacketResp {
	public List<CancelCause>	cancelCauses;

	public GetCancelCausesResponse() {
		command = HttpDefine.GETCANCELCAUSES_RESP;
	}

	public List<CancelCause> getCancelCauses() {
		return cancelCauses;
	}

	public void setCancelCauses(List<CancelCause> cancelCauses) {
		this.cancelCauses = cancelCauses;
	}

}
