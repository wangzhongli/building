package com.e1858.building.entity;

import com.e1858.building.bean.BankBean;
import com.hg.android.entitycache.CacheEntityWithGeneratedId;

@SuppressWarnings("serial")
public class BankEntity extends CacheEntityWithGeneratedId<BankBean> {

	@Override
	protected Class<BankBean> getBeanClass() {
		return BankBean.class;
	}

}
