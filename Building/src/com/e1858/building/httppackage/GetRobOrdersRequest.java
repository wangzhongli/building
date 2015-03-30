package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;
import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class GetRobOrdersRequest extends AutoFillPacketRequest {
	@Expose
	private String	longitude;		//经度
	private String	latitude;		//纬度
	private int		offset;
	private int		count;
	private String	city;			//所在城市
	private String	province;		//所在省
	private int		serviceType;	//分类条件
	private String	serviceArea;

//	private int		time;			//1.今天2.明天3.后天

	public GetRobOrdersRequest() {
		super(HttpDefine.GETROBORDERS);
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getServiceType() {
		return serviceType;
	}

	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceArea() {
		return serviceArea;
	}

	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}

//	public int getTime() {
//		return time;
//	}
//
//	public void setTime(int time) {
//		this.time = time;
//	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

}
