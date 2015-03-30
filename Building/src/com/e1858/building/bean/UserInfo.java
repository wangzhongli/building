package com.e1858.building.bean;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class UserInfo implements Serializable {
	public String		userID;			//用户ID
	public String		headPortrait;		//头像url
	public String		userName;			//账号
	public String		workerName;		//工人名称
	public String		realName;			//真实姓名
	public String		mobile;			//手机号
	public String		address;			//所在地
	public String		userNumber;		//用户编号
	public int			serviceCount;		//工人服务次数
	public String		province;			//	String	省
	public String		city;				//	String	市
	public String		district;			//	String	区
	public List<String>	authPictureUrls;	//	String数组	实名认证图片数组
	public String		IDNumber;			//	实名认证身份证号

	public int			realNameAuthState;	//通过实名认证实名认证状态(身份证上传及身份证号审核通过)-1未提交0待认证1认证成功2认证失败
	public int			mobileMan;			//手机达人 1认证 0未认证
	public int			honestMan;			//诚信达人  1达人 0不是达人

	public boolean		isSetMoneyPwd;

	public String getHeadPortrait() {
		return headPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}

	public int getServiceCount() {
		return serviceCount;
	}

	public void setServiceCount(int serviceCount) {
		this.serviceCount = serviceCount;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public List<String> getAuthPictureUrls() {
		return authPictureUrls;
	}

	public void setAuthPictureUrls(List<String> authPictureUrls) {
		this.authPictureUrls = authPictureUrls;
	}

	public String getIDNumber() {
		return IDNumber;
	}

	public void setIDNumber(String iDNumber) {
		IDNumber = iDNumber;
	}

	public int getRealNameAuthState() {
		return realNameAuthState;
	}

	public void setRealNameAuthState(int realNameAuthState) {
		this.realNameAuthState = realNameAuthState;
	}

	public int getMobileMan() {
		return mobileMan;
	}

	public void setMobileMan(int mobileMan) {
		this.mobileMan = mobileMan;
	}

	public int getHonestMan() {
		return honestMan;
	}

	public void setHonestMan(int honestMan) {
		this.honestMan = honestMan;
	}

	public boolean isSetMoneyPwd() {
		return isSetMoneyPwd;
	}

	public void setSetMoneyPwd(boolean isSetMoneyPwd) {
		this.isSetMoneyPwd = isSetMoneyPwd;
	}

}
