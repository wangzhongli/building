package com.e1858.building.bean;

import java.io.Serializable;

/**
 * 日期: 2015年1月7日 下午4:45:04
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class BusinessBean implements Serializable {
	public String	type;				//	String	交易类型
	public double	money;				//	交易金额
	public String	serviceType;		//	String	服务类型
	public String	tradeTime;			//	String 	交易时间
	public String	serialNumber;		//	String	流水单号
	public String	result;			//	String	交易结果
	public String	receivedTime;		//	String	到账时间
	public String	acceptCardOwner;	//	String	对方姓名
	public String	acceptCardBankName; //	String	对方银行卡名字
	public String	acceptCardNumer;	//	String	对方银行卡账号

	public String	ID;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(String receivedTime) {
		this.receivedTime = receivedTime;
	}

	public String getAcceptCardOwner() {
		return acceptCardOwner;
	}

	public void setAcceptCardOwner(String acceptCardOwner) {
		this.acceptCardOwner = acceptCardOwner;
	}

	public String getAcceptCardBankName() {
		return acceptCardBankName;
	}

	public void setAcceptCardBankName(String acceptCardBankName) {
		this.acceptCardBankName = acceptCardBankName;
	}

	public String getAcceptCardNumer() {
		return acceptCardNumer;
	}

	public void setAcceptCardNumer(String acceptCardNumer) {
		this.acceptCardNumer = acceptCardNumer;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

}
