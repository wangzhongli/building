package com.e1858.building.entity;

import com.e1858.building.bean.BankCardBean;
import com.hg.android.entitycache.CacheEntityWithSpecifiedId;

@SuppressWarnings("serial")
public class BankCardEntity extends CacheEntityWithSpecifiedId<BankCardBean> {

	@Override
	protected Class<BankCardBean> getBeanClass() {
		return BankCardBean.class;
	}

	@Override
	protected String generateID(BankCardBean bean) {
		return bean.getID();
	}

}
