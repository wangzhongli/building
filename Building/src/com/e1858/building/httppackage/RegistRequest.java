package com.e1858.building.httppackage;

import java.util.List;

import com.e1858.building.bean.ServiceTypeBean;
import com.e1858.building.net.HttpDefine;
import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class RegistRequest extends PacketRequest {
	@Expose
	private String					password;				//密码
	@Expose
	private String					verify;				//验证码
	@Expose
	private String					mobile;				//手机号
	@Expose
	private String					workerName;			//工人名称
	@Expose
	private String					workerRealName;		//工人真实名字
	@Expose
	private List<ServiceTypeBean>	serviceTypes;			//服务类型
	@Expose
	private String					serviceProvince;		//服务区域 省
	private String					serviceCity;			//服务区域 城市
	private List<String>			serviceDistricts;		//服务类型
	private List<String>			serviceWorkersTypes;	//工种类型

	public RegistRequest() {
		super(HttpDefine.REGIST);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public String getWorkerRealName() {
		return workerRealName;
	}

	public void setWorkerRealName(String workerRealName) {
		this.workerRealName = workerRealName;
	}

	public List<ServiceTypeBean> getServiceTypes() {
		return serviceTypes;
	}

	public void setServiceTypes(List<ServiceTypeBean> serviceTypes) {
		this.serviceTypes = serviceTypes;
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

	public List<String> getServiceWorkersTypes() {
		return serviceWorkersTypes;
	}

	public void setServiceWorkersTypes(List<String> serviceWorkersTypes) {
		this.serviceWorkersTypes = serviceWorkersTypes;
	}

}
