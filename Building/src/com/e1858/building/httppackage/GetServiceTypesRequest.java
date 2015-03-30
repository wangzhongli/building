package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

/**
 * 日期: 2015年1月5日 下午5:04:16
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class GetServiceTypesRequest extends PacketRequest {

	public GetServiceTypesRequest() {
		super(HttpDefine.GETSERVICETYPES);
	}

}
