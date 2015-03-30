package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class GetMyCasesRequest extends AutoFillPacketRequest {

	public String	serviceTypeID;	//	
	public int		offset;		//	int	偏移量	True
	public int		count;			//	int	数量	True

	public GetMyCasesRequest() {
		super(HttpDefine.GETMYCASE);
	}

	public String getServiceTypeID() {
		return serviceTypeID;
	}

	public void setServiceTypeID(String serviceTypeID) {
		this.serviceTypeID = serviceTypeID;
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
