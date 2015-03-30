package com.e1858.building.httppackage;

import com.e1858.building.bean.PacketResp;
import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class PostOrderInfoResponse extends PacketResp {

	public PostOrderInfoResponse() {
		command = HttpDefine.POSTORDERINFOS_RESP;
	}

}
