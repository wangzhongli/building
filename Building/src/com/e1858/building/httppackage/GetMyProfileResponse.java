package com.e1858.building.httppackage;

import com.e1858.building.bean.PacketResp;
import com.e1858.building.bean.UserInfo;

/**
 * 日期: 2015年1月6日 下午5:52:15
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class GetMyProfileResponse extends PacketResp {
	public UserInfo	userInfo;

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
}
