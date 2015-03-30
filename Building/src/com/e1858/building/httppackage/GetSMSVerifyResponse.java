package com.e1858.building.httppackage;

import com.e1858.building.bean.PacketResp;
import com.e1858.building.net.HttpDefine;
import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class GetSMSVerifyResponse extends PacketResp {
	@Expose
	private String	verify;

	public GetSMSVerifyResponse() {
		command = HttpDefine.VERIFY_RESP;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

}
