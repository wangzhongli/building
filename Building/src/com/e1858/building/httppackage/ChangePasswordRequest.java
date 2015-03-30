package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;
import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class ChangePasswordRequest extends AutoFillPacketRequest {
	@Expose
	private String	oldPassword		= "";
	@Expose
	private String	eNewPassword	= "";
	@Expose
	private String	mobile			= "";

	public ChangePasswordRequest() {
		super(HttpDefine.CHANGEPASSWORD);
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
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

}
