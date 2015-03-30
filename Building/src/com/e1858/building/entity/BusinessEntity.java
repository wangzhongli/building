package com.e1858.building.entity;

import com.e1858.building.bean.BusinessBean;
import com.hg.android.entitycache.CacheEntityWithSpecifiedId;

@SuppressWarnings("serial")
public class BusinessEntity extends CacheEntityWithSpecifiedId<BusinessBean> {

	@Override
	protected Class<BusinessBean> getBeanClass() {
		return BusinessBean.class;
	}

	@Override
	protected String generateID(BusinessBean bean) {
		return bean.getID();
	}

}
