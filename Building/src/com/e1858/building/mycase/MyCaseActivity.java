package com.e1858.building.mycase;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.view.Menu;
import android.view.MenuItem;

import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.entity.EntityOpenHelper;
import com.e1858.building.entity.ServiceTypeEntity;
import com.e1858.building.httppackage.GetServiceTypesRequest;
import com.e1858.building.httppackage.GetServiceTypesResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.ResponseUtils;
import com.hg.android.entitycache.EntityCacheHelper;
import com.hg.android.utils.HGUtils;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

public class MyCaseActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycase);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		initTabs();

		loadServiceTypes(actionBar.getTabCount() == 0);
	}

	List<ServiceTypeEntity> readAllServiceTypes() {
		List<ServiceTypeEntity> list = null;
		OrmLiteSqliteOpenHelper openHelper = OpenHelperManager.getHelper(getActivity(), EntityOpenHelper.class);
		try {
			Dao<ServiceTypeEntity, String> dao = openHelper.getDao(ServiceTypeEntity.class);
			list = dao.queryForAll();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		OpenHelperManager.releaseHelper();
		return list;
	}

	void initTabs() {
		List<ServiceTypeEntity> list = readAllServiceTypes();
		ActionBar actionBar = getSupportActionBar();
		actionBar.removeAllTabs();
		if (HGUtils.isListEmpty(list)) {
			return;
		}

		for (ServiceTypeEntity entity : list) {
			final String serviceTypeID = "" + entity.getBean().getID();
			actionBar.addTab(actionBar.newTab().setText(entity.getBean().getName())
					.setTabListener(new ActionBar.TabListener() {
						CaseListFragment	frag;

						@Override
						public void onTabReselected(Tab arg0, FragmentTransaction fragmentTransaction) {

						}

						@Override
						public void onTabSelected(Tab arg0, FragmentTransaction fragmentTransaction) {
							if (frag == null) {
								frag = new CaseListFragment();
								Bundle b = new Bundle();
								b.putCharSequence(CaseListFragment.IntentKey_ServiceTypeID, serviceTypeID);
								frag.setArguments(b);
							}
							fragmentTransaction.replace(R.id.fragmentContainer, frag);
						}

						@Override
						public void onTabUnselected(Tab arg0, FragmentTransaction fragmentTransaction) {

						}
					}));
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	protected void loadServiceTypes(final boolean showProgress) {

		ResponseHandler<GetServiceTypesResponse> responseHandler = new ResponseHandler<GetServiceTypesResponse>() {
			@Override
			public void onStart() {
				if (showProgress) {
					beginEmptyProgress(null);
				}
			}

			@Override
			public void onFinish(GetServiceTypesResponse response, String error) {
				stopEmptyProgress(null);
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntities(ServiceTypeEntity.class,
							response.getServiceTypes(), 0);
					if (!isDestroyedApi8()) {
						initTabs();
					}
				}
			}
		};

		GetServiceTypesRequest request = new GetServiceTypesRequest();
		HttpPacketClient.postPacketAsynchronous(request, GetServiceTypesResponse.class, responseHandler, true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

}
