package com.e1858.building.bean;

import java.io.Serializable;

/**
 * 日期: 2015年1月7日 下午6:14:20
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class BankBean implements Serializable {
	public String	bankName;	//	String	银行名称
	public String	cardType;	//	String	银行卡类型
	public String	cardUrl;	//	String 	银行图标地址
	public String	ID;		//	String	ID

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardUrl() {
		return cardUrl;
	}

	public void setCardUrl(String cardUrl) {
		this.cardUrl = cardUrl;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

}
