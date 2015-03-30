package com.e1858.building.entity;

import com.e1858.building.bean.OrderInfo;
import com.hg.android.entitycache.CacheEntityWithSpecifiedId;

@SuppressWarnings("serial")
public class OrderEntity extends CacheEntityWithSpecifiedId<OrderInfo> {

	@Override
	protected String generateID(OrderInfo bean) {
		return bean.getOrderID();
	}

	@Override
	protected Class<OrderInfo> getBeanClass() {
		return OrderInfo.class;
	}

}
