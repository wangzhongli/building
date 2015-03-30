package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;
import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class SetMoneyPasswordRquest extends AutoFillPacketRequest {
	@Expose
	private String	verify			= "";
	@Expose
	private String	eNewPassword	= "";
	@Expose
	private String	mobile			= "";
	private String	realName		= "";
	private String	idCardNumber	= "";

	public SetMoneyPasswordRquest() {
		super(HttpDefine.SETMONEYPASSWORD);
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

}
