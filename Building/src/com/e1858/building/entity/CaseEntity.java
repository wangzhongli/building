package com.e1858.building.entity;

import com.e1858.building.bean.CaseBean;
import com.hg.android.entitycache.CacheEntityWithSpecifiedId;

@SuppressWarnings("serial")
public class CaseEntity extends CacheEntityWithSpecifiedId<CaseBean> {

	@Override
	protected Class<CaseBean> getBeanClass() {
		return CaseBean.class;
	}

	@Override
	protected String generateID(CaseBean bean) {
		return bean.getID();
	}

}
