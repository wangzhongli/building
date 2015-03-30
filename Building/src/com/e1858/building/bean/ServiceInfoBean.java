package com.e1858.building.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 日期: 2015年1月6日 下午7:34:08
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
@SuppressWarnings("serial")
public class ServiceInfoBean implements Serializable {
	public List<ServiceTypeBean>	serviceTypes;			//	
	public String					serviceTime;			//	String	服务时间
	public int						servicePersons;		//	String	服务人数
	public int						serviceCarCount;		//	String	货车数量
	public float					serviceCarPower;		//	String	货车吨位
	public String					pickupAddress;			//	String	推荐提货地址
	public String					serviceDescription;	//	String	服务说明

	public String					serviceProvince;		//	String	省
	public String					serviceCity;			//	String	市
	public List<String>				serviceDistricts;		//	String	区
	public List<String>				serviceWorkersTypes;	//	String	工人工种

	public List<ServiceTypeBean> getServiceTypes() {
		return serviceTypes;
	}

	public void setServiceTypes(List<ServiceTypeBean> serviceTypes) {
		this.serviceTypes = serviceTypes;
	}

	public String getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
	}

	public int getServicePersons() {
		return servicePersons;
	}

	public void setServicePersons(int servicePersons) {
		this.servicePersons = servicePersons;
	}

	public float getServiceCarPower() {
		return serviceCarPower;
	}

	public void setServiceCarPower(float serviceCarPower) {
		this.serviceCarPower = serviceCarPower;
	}

	public int getServiceCarCount() {
		return serviceCarCount;
	}

	public void setServiceCarCount(int serviceCarCount) {
		this.serviceCarCount = serviceCarCount;
	}

	public String getPickupAddress() {
		return pickupAddress;
	}

	public void setPickupAddress(String pickupAddress) {
		this.pickupAddress = pickupAddress;
	}

	public String getServiceProvince() {
		return serviceProvince;
	}

	public void setServiceProvince(String serviceProvince) {
		this.serviceProvince = serviceProvince;
	}

	public String getServiceCity() {
		return serviceCity;
	}

	public void setServiceCity(String serviceCity) {
		this.serviceCity = serviceCity;
	}

	public List<String> getServiceDistricts() {
		return serviceDistricts;
	}

	public void setServiceDistricts(List<String> serviceDistricts) {
		this.serviceDistricts = serviceDistricts;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	public List<String> getServiceWorkersTypes() {
		return serviceWorkersTypes;
	}

	public void setServiceWorkersTypes(List<String> serviceWorkersTypes) {
		this.serviceWorkersTypes = serviceWorkersTypes;
	}

}
