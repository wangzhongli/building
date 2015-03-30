package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

/**
 * 日期: 2015年1月8日 上午9:44:53
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class DeleteBankCardRequest extends AutoFillPacketRequest {

	public String	ID;
	public String	moneyPassword;

	/**
	 * @param command
	 */
	public DeleteBankCardRequest() {
		super(HttpDefine.DELETEBANKCARD);
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getMoneyPassword() {
		return moneyPassword;
	}

	public void setMoneyPassword(String moneyPassword) {
		this.moneyPassword = moneyPassword;
	}

}
