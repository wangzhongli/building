package com.e1858.building.httppackage;

import java.util.List;

import com.e1858.building.bean.Notice;
import com.e1858.building.bean.PacketResp;
import com.e1858.building.net.HttpDefine;

/**
 * 功能：
 * 日期: 2015-2-4 上午11:22:22
 * 作者: wangzhongli
 */
@SuppressWarnings("serial")
public class GetNoticesResponse extends PacketResp {
	public List<Notice>	notices;

	public GetNoticesResponse() {
		command = HttpDefine.GETNOTICES_RESP;
	}

	public List<Notice> getNotices() {
		return notices;
	}

	public void setNotices(List<Notice> notices) {
		this.notices = notices;
	}

}
