package com.e1858.building.entity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class EntityOpenHelper extends OrmLiteSqliteOpenHelper {
	static final int		VERSION_CREATE_20150105	= 2;
	static final int		VERSION_CURRENT			= VERSION_CREATE_20150105;

	static List<Class<?>>	sEntityClassList		= new ArrayList<Class<?>>();

	static {
		registEntity(BankCardEntity.class);
		registEntity(BankEntity.class);
		registEntity(BusinessEntity.class);
		registEntity(CaseEntity.class);
		registEntity(RobOrderEntity.class);
		registEntity(OrderEntity.class);
		registEntity(ServiceTypeEntity.class);
		registEntity(PictureInfoEntity.class);
		registEntity(NoticeEntity.class);
	}

	public static void registEntity(Class<?> clazz) {
		sEntityClassList.add(clazz);
	}

	public EntityOpenHelper(Context context) {
		super(context, "e1858_cache.db", null, VERSION_CURRENT);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			for (Class<?> clazz : sEntityClassList) {
				TableUtils.createTableIfNotExists(arg1, clazz);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3) {
		try {
			for (Class<?> clazz : sEntityClassList) {
				TableUtils.dropTable(arg1, clazz, true);
			}
			onCreate(arg0, arg1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDowngrade(SQLiteDatabase arg0, int arg2, int arg3) {
		super.onUpgrade(arg0, arg2, arg3);
	}
}
