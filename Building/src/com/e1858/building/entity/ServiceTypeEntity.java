package com.e1858.building.entity;

import com.e1858.building.bean.ServiceTypeBean;
import com.hg.android.entitycache.CacheEntityWithSpecifiedId;

/**
 * 日期: 2015年1月5日 下午5:02:53
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class ServiceTypeEntity extends CacheEntityWithSpecifiedId<ServiceTypeBean> {

	/*
	 */
	@Override
	protected String generateID(ServiceTypeBean bean) {
		return "" + bean.getID();
	}

	/*
	 */
	@Override
	protected Class<ServiceTypeBean> getBeanClass() {
		return ServiceTypeBean.class;
	}

}
