package com.e1858.building.httppackage;

import com.e1858.building.bean.Notice;
import com.e1858.building.bean.PacketResp;
import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class ReadNoticeResponse extends PacketResp {
	public Notice	notice;

	public ReadNoticeResponse() {
		command = HttpDefine.READNOTICE_RESP;
	}

	public Notice getNotice() {
		return notice;
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
	}

}
