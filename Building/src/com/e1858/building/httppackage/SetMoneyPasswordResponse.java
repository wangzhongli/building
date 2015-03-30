package com.e1858.building.httppackage;

import com.e1858.building.bean.PacketResp;
import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class SetMoneyPasswordResponse extends PacketResp {

	public SetMoneyPasswordResponse() {
		command = HttpDefine.SETCHANGEMONEYPASSWORD_RESP;
	}

}
