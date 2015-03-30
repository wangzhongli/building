package com.e1858.building.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RobOrder implements Serializable {

	public String	ID;				//抢单信息ID
	public String	robOrderID;		//抢单信息ID
	public String	serviceType;		//抢单信息服务类型
	public String	serviceTime;		//抢单信息服务时间
	public String	robOrderTime;		//抢单信息下单时间
	public String	robOrderAddress;	//抢单信息服务地址
	public double	latitude;			//经度
	public double	longitute;			//纬度
	public int		status;			//状态
	public String	goodsType;			//商品类型

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public String getRobOrderID() {
		return robOrderID;
	}

	public void setRobOrderID(String robOrderID) {
		this.robOrderID = robOrderID;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
	}

	public String getRobOrderTime() {
		return robOrderTime;
	}

	public void setRobOrderTime(String robOrderTime) {
		this.robOrderTime = robOrderTime;
	}

	public String getRobOrderAddress() {
		return robOrderAddress;
	}

	public void setRobOrderAddress(String robOrderAddress) {
		this.robOrderAddress = robOrderAddress;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitute() {
		return longitute;
	}

	public void setLongitute(double longitute) {
		this.longitute = longitute;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

}
