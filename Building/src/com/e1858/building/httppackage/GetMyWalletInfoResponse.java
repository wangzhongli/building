package com.e1858.building.httppackage;

import com.e1858.building.bean.PacketResp;

/**
 * 日期: 2015年1月6日 下午7:35:02
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class GetMyWalletInfoResponse extends PacketResp {

	public float	balance;		//	float	可用余额
	public int		bankCardCount;	//	Int 	银行卡数量

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public int getBankCardCount() {
		return bankCardCount;
	}

	public void setBankCardCount(int bankCardCount) {
		this.bankCardCount = bankCardCount;
	}

}
