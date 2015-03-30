package com.e1858.building.httppackage;

import com.e1858.building.bean.PacketResp;
import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class FindPasswordResponse extends PacketResp {

	public FindPasswordResponse() {
		command = HttpDefine.FINDPASSWORD_RESP;
	}

}
