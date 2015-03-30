package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;
import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class LoginRequest extends PacketRequest {
	@Expose
	private String	username	= "";
	@Expose
	private String	password	= "";

	public LoginRequest() {
		super(HttpDefine.LOGIN);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
