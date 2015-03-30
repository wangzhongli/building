package com.e1858.building.httppackage;

import java.util.List;

import com.e1858.building.bean.BankBean;
import com.e1858.building.bean.PacketResp;

/**
 * 日期: 2015年1月7日 下午6:42:41
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class GetBanksResponse extends PacketResp {
	public List<BankBean>	bankInfos;

	public List<BankBean> getBankInfos() {
		return bankInfos;
	}

	public void setBankInfos(List<BankBean> bankInfos) {
		this.bankInfos = bankInfos;
	}

}
