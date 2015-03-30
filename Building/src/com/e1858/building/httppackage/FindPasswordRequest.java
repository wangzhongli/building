package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;
import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class FindPasswordRequest extends PacketRequest {
	@Expose
	private String	phoneNum		= "";
	@Expose
	private String	eNewPassword	= "";
	@Expose
	private String	verify			= "";

	public FindPasswordRequest() {
		super(HttpDefine.FINDPASSWORD);
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String geteNewPassword() {
		return eNewPassword;
	}

	public void seteNewPassword(String eNewPassword) {
		this.eNewPassword = eNewPassword;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

}
