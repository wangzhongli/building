package com.e1858.building.entity;

import com.e1858.building.bean.Notice;
import com.hg.android.entitycache.CacheEntityWithSpecifiedId;

public class NoticeEntity extends CacheEntityWithSpecifiedId<Notice> {

	/*
	 */
	@Override
	protected String generateID(Notice bean) {
		return "" + bean.getID();
	}

	/*
	 */
	@Override
	protected Class<Notice> getBeanClass() {
		return Notice.class;
	}

}
