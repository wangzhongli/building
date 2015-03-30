package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

/**
 * 日期: 2015年1月16日 上午9:34:23
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class CheckMoneyPasswordRequest extends AutoFillPacketRequest {

	String	moneyPassword;

	public String getMoneyPassword() {
		return moneyPassword;
	}

	public void setMoneyPassword(String moneyPassword) {
		this.moneyPassword = moneyPassword;
	}

	/**
	 * @param command
	 */
	public CheckMoneyPasswordRequest() {
		super(HttpDefine.CHECKMONEYPASSWORD);
	}

}
