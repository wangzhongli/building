package com.e1858.building.httppackage;

import com.e1858.building.bean.PacketResp;
import com.e1858.building.bean.UserInfo;
import com.e1858.building.net.HttpDefine;
import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class VerifyLoginResponse extends PacketResp {
	@Expose
	private String		key;
	@Expose
	private int			userID;
	@Expose
	private UserInfo	userInfo;

	public VerifyLoginResponse() {
		command = HttpDefine.LOGIN_RESP;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

}
