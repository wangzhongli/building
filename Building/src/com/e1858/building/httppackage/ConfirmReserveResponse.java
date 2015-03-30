package com.e1858.building.httppackage;

import com.e1858.building.bean.PacketResp;
import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class ConfirmReserveResponse extends PacketResp {

	public ConfirmReserveResponse() {
		command = HttpDefine.CONFIRMRESERVE_RESP;
	}

}
