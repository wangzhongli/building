package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

/**
 * 功能：
 * 日期: 2015-2-4 上午11:19:16
 * 作者: wangzhongli
 */
@SuppressWarnings("serial")
public class GetNoticesRequest extends AutoFillPacketRequest {
	private int	offset;
	private int	count;
	private int	type;	//1.公有通知2.私有通知3.全部通知

	/**
	 * @param command
	 */
	public GetNoticesRequest() {
		super(HttpDefine.GETNOTICES);
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
