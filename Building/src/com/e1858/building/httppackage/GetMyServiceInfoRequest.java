package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

/**
 * 日期: 2015年1月6日 下午7:34:28
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class GetMyServiceInfoRequest extends AutoFillPacketRequest {

	/**
	 * @param command
	 */
	public GetMyServiceInfoRequest() {
		super(HttpDefine.GETMYSERVICEINFO);
	}

}
