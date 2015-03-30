package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;
import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class VerifyLoginRequest extends PacketRequest {
	@Expose
	private String	phoneNum	= "";
	@Expose
	private String	verify		= "";

	public VerifyLoginRequest() {
		super(HttpDefine.LOGIN_VERIFY);
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}
}
