package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

/**
 * 功能：
 * 日期: 2015-2-4 上午11:19:16
 * 作者: wangzhongli
 */
public class GetCancelCausesRequest extends AutoFillPacketRequest {

	public int	type	= 0;	//类型1，待预约取消原因2.待完成取消原因

	/**
	 * @param command
	 */
	public GetCancelCausesRequest() {
		super(HttpDefine.GETCANCELCAUSES);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
