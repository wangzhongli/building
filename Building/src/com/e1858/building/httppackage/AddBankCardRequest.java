package com.e1858.building.httppackage;

import com.e1858.building.bean.BankCardBean;
import com.e1858.building.net.HttpDefine;

/**
 * 日期: 2015年1月7日 下午8:37:04
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class AddBankCardRequest extends AutoFillPacketRequest {
	BankCardBean	bankCard;

	/**
	 * @param command
	 */
	public AddBankCardRequest() {
		super(HttpDefine.ADDBANKCARD);
	}

	public BankCardBean getBankCard() {
		return bankCard;
	}

	public void setBankCard(BankCardBean bankCard) {
		this.bankCard = bankCard;
	}

}
