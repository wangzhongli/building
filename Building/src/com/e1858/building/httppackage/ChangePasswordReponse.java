package com.e1858.building.httppackage;

import com.e1858.building.bean.PacketResp;
import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class ChangePasswordReponse extends PacketResp {

	public ChangePasswordReponse() {
		command = HttpDefine.CHANGEPASSWORD_RESP;
	}

}
