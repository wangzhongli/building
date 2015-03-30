package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class LogoutRequest extends AutoFillPacketRequest {

	public LogoutRequest() {
		super(HttpDefine.LOGOUT);
	}

}
