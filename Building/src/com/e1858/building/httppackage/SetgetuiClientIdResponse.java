package com.e1858.building.httppackage;

import com.e1858.building.bean.PacketResp;
import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class SetgetuiClientIdResponse extends PacketResp {

	public SetgetuiClientIdResponse() {
		command = HttpDefine.SETGETUICLIENTID_RESP;
	}

}
