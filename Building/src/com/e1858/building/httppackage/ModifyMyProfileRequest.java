package com.e1858.building.httppackage;

import com.e1858.building.bean.UserInfo;
import com.e1858.building.net.HttpDefine;

/**
 * 日期: 2015年1月6日 下午5:49:58
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class ModifyMyProfileRequest extends AutoFillPacketRequest {

	public UserInfo	userInfo;

	/**
	 * @param command
	 */
	public ModifyMyProfileRequest() {
		super(HttpDefine.MODIFYMYPROFILE);
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

}
