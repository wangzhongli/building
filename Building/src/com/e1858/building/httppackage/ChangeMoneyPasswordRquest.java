package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class ChangeMoneyPasswordRquest extends AutoFillPacketRequest {
	private String	verify			= "";
	private String	eNewPassword	= "";
	private String	mobile			= "";
	private String	oldPassword		= "";

	public ChangeMoneyPasswordRquest() {
		super(HttpDefine.CHANGEMONEYPASSWORD);
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public String geteNewPassword() {
		return eNewPassword;
	}

	public void seteNewPassword(String eNewPassword) {
		this.eNewPassword = eNewPassword;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

}
