package com.e1858.building.httppackage;

import java.util.List;

import com.e1858.building.bean.BankCardBean;
import com.e1858.building.bean.PacketResp;

/**
 * 日期: 2015年1月7日 下午6:42:41
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class GetBankCardsResponse extends PacketResp {
	public List<BankCardBean>	bankCards;

	public List<BankCardBean> getBankCards() {
		return bankCards;
	}

	public void setBankCards(List<BankCardBean> bankCards) {
		this.bankCards = bankCards;
	}

}
