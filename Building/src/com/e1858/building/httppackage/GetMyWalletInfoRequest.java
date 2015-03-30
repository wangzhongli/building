package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

/**
 * 日期: 2015年1月8日 上午9:44:53
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class GetMyWalletInfoRequest extends AutoFillPacketRequest {

	/**
	 * @param command
	 */
	public GetMyWalletInfoRequest() {
		super(HttpDefine.GETMYMONEYINFO);
	}

}
