package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

/**
 * 日期: 2015年1月7日 下午4:56:23
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
@SuppressWarnings("serial")
public class GetMyBusinessesRequest extends AutoFillPacketRequest {

	public int	offset;
	public int	count;

	/**
	 * @param command
	 */
	public GetMyBusinessesRequest() {
		super(HttpDefine.GETMYBUSINESSINFOS);
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
