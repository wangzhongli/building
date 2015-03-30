package com.e1858.building.httppackage;

import com.e1858.building.bean.PacketResp;
import com.e1858.building.bean.ServiceInfoBean;

/**
 * 日期: 2015年1月6日 下午7:35:02
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class GetMyServiceInfoResponse extends PacketResp {

	public ServiceInfoBean	serviceInfo;

	public ServiceInfoBean getServiceInfo() {
		return serviceInfo;
	}

	public void setServiceInfo(ServiceInfoBean serviceInfo) {
		this.serviceInfo = serviceInfo;
	}
}
