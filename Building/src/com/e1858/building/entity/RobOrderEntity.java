package com.e1858.building.entity;

import com.e1858.building.bean.RobOrder;
import com.hg.android.entitycache.CacheEntityWithSpecifiedId;

@SuppressWarnings("serial")
public class RobOrderEntity extends CacheEntityWithSpecifiedId<RobOrder> {

	@Override
	protected String generateID(RobOrder bean) {
		return bean.getRobOrderID();
	}

	@Override
	protected Class<RobOrder> getBeanClass() {
		return RobOrder.class;
	}

}
