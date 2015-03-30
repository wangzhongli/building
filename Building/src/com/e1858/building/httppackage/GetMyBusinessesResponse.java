package com.e1858.building.httppackage;

import java.util.List;

import com.e1858.building.bean.BusinessBean;
import com.e1858.building.bean.PacketResp;

/**
 * 日期: 2015年1月7日 下午5:02:18
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
@SuppressWarnings("serial")
public class GetMyBusinessesResponse extends PacketResp {
	public List<BusinessBean>	businessInfos;

	public List<BusinessBean> getBusinessInfos() {
		return businessInfos;
	}

	public void setBusinessInfos(List<BusinessBean> businessInfos) {
		this.businessInfos = businessInfos;
	}

}
