package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

/**
 * 日期: 2015年1月6日 下午5:51:35
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
@SuppressWarnings("serial")
public class GetMyProfileRequest extends AutoFillPacketRequest {

	/**
	 * @param command
	 */
	public GetMyProfileRequest() {
		super(HttpDefine.GETMYPROFILE);
	}

}
