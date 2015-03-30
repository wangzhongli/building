package com.e1858.building.httppackage;

import com.e1858.building.bean.PacketResp;
import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class ServiceDoneUpPicResponse extends PacketResp {

	public ServiceDoneUpPicResponse() {
		command = HttpDefine.SERVICEDONEUPPIC_RESP;
	}

}
