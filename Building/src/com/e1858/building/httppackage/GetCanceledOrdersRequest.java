package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class GetCanceledOrdersRequest extends AutoFillPacketRequest {
	private int	offset;
	private int	count;

	public GetCanceledOrdersRequest() {
		super(HttpDefine.GETCANCELEDORDER);
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
