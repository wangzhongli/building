package com.e1858.building.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.e1858.building.MainApplication;
import com.e1858.building.bean.ServiceTypeBean;
import com.e1858.building.entity.EntityOpenHelper;
import com.e1858.building.entity.ServiceTypeEntity;
import com.hg.android.ormlite.extra.OrmLiteNotificationCenter;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

public class DbOpenUtils {
	public static List<ServiceTypeBean> readAllServiceTypes(Context context) {
		List<ServiceTypeEntity> list = null;
		List<ServiceTypeBean> types = new ArrayList<ServiceTypeBean>();
		OrmLiteSqliteOpenHelper openHelper = OpenHelperManager.getHelper(context, EntityOpenHelper.class);
		try {
			Dao<ServiceTypeEntity, String> dao = openHelper.getDao(ServiceTypeEntity.class);
			list = dao.queryForAll();
			for (ServiceTypeEntity st : list) {
				types.add(st.getBean());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		OpenHelperManager.releaseHelper();
		return types;
	}

	public static <IDType, EntityType> void deleteEntityByID(IDType ID, Class<EntityType> klass) {
		EntityOpenHelper openHelper = OpenHelperManager
				.getHelper(MainApplication.getInstance(), EntityOpenHelper.class);
		try {
			Dao<EntityType, IDType> dao = openHelper.getDao(klass);
			dao.deleteById(ID);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		OpenHelperManager.releaseHelper();
		OrmLiteNotificationCenter.sharedInstance().notifyChange(klass);
	}
}
