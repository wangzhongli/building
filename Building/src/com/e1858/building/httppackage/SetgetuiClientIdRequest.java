package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

/**
 * 功能：
 * 日期: 2015-2-8 下午5:28:47
 * 作者: wangzhongli
 */
@SuppressWarnings("serial")
public class SetgetuiClientIdRequest extends AutoFillPacketRequest {
	public String	clientID;		//个推id
	public String	deviceToken;	//ios的Android无关
	public int		deviceType;	//设备系统类型1 安卓  2  ios

	public SetgetuiClientIdRequest() {
		super(HttpDefine.SETGETUICLIENTID);
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

}
