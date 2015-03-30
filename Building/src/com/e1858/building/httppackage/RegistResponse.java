package com.e1858.building.httppackage;

import com.e1858.building.bean.PacketResp;
import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class RegistResponse extends PacketResp {

	public RegistResponse() {
		command = HttpDefine.REGIST_RESP;
	}
}
