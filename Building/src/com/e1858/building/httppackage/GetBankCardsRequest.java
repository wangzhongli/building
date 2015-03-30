package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

/**
 * 日期: 2015年1月7日 下午6:41:28
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class GetBankCardsRequest extends AutoFillPacketRequest {

	/**
	 * @param command
	 */
	public GetBankCardsRequest() {
		super(HttpDefine.GETMYBANKCARDS);
	}

}
