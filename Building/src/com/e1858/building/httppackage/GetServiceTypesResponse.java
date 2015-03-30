package com.e1858.building.httppackage;

import java.util.List;

import com.e1858.building.bean.PacketResp;
import com.e1858.building.bean.ServiceTypeBean;

/**
 * 日期: 2015年1月5日 下午5:09:49
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class GetServiceTypesResponse extends PacketResp {

	public List<ServiceTypeBean>	serviceTypes;

	public List<ServiceTypeBean> getServiceTypes() {
		return serviceTypes;
	}

	public void setServiceTypes(List<ServiceTypeBean> serviceTypes) {
		this.serviceTypes = serviceTypes;
	}

}
