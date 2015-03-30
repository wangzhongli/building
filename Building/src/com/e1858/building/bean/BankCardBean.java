package com.e1858.building.bean;

import java.io.Serializable;

/**
 * 日期: 2015年1月7日 下午6:43:36
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
@SuppressWarnings("serial")
public class BankCardBean implements Serializable {

	public String	bankName;			//	String	银行名称
	public String	cardNumber;		//	String	银行卡号
	public String	cardType;			//	String	银行卡类型
	public String	cardUrl;			//	String 	银行卡图标地址
	public String	cardOwner;			//	String	持卡人姓名
	public String	cardOwnerMobile;	//	String	持卡人手机

	public String	ID;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
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

	public String getCardOwner() {
		return cardOwner;
	}

	public void setCardOwner(String cardOwner) {
		this.cardOwner = cardOwner;
	}

	public String getCardOwnerMobile() {
		return cardOwnerMobile;
	}

	public void setCardOwnerMobile(String cardOwnerMobile) {
		this.cardOwnerMobile = cardOwnerMobile;
	}

}
