package com.e1858.building.httppackage;

import com.e1858.building.bean.PacketResp;
import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class SignAddressResponse extends PacketResp {

	public SignAddressResponse() {
		command = HttpDefine.SIGNADDRESS_RESP;
	}

}
