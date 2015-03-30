package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class GetNotReserveOrdersRequest extends AutoFillPacketRequest {
	private int	offset;
	private int	count;

	public GetNotReserveOrdersRequest() {
		super(HttpDefine.GETNOTRESERVEORDERS);
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
