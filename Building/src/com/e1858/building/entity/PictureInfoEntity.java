package com.e1858.building.entity;

import com.e1858.building.bean.PictureInfo;
import com.hg.android.entitycache.CacheEntityWithSpecifiedId;

@SuppressWarnings("serial")
public class PictureInfoEntity extends CacheEntityWithSpecifiedId<PictureInfo> {

	@Override
	protected Class<PictureInfo> getBeanClass() {
		return PictureInfo.class;
	}

	@Override
	protected String generateID(PictureInfo bean) {
		return bean.getID();
	}

}
