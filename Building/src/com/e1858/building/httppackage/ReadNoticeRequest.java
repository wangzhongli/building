package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class ReadNoticeRequest extends AutoFillPacketRequest {

	public String	ID; //	通知ID

	public ReadNoticeRequest() {
		super(HttpDefine.READNOTICE);
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

}
