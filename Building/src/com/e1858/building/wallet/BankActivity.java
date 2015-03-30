package com.e1858.building.wallet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.bean.BankBean;
import com.e1858.building.entity.BankEntity;
import com.e1858.building.entity.EntityOpenHelper;
import com.e1858.building.httppackage.GetBanksRequest;
import com.e1858.building.httppackage.GetBanksResponse;
import com.e1858.building.net.HttpDefine;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.ResponseUtils;
import com.hg.android.entitycache.EntityCacheHelper;
import com.hg.android.utils.HGUtils;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

/**
 * 日期: 2015年1月7日 下午4:42:07
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class BankActivity extends BaseActivity {
	public static final int		RequestCode_Pay	= 100000;

	public static final String	IntentKey_Bank	= "IntentKey_Bank";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_banks);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		initTabs();

		loadDatas(actionBar.getTabCount() == 0);
	}

	List<BankEntity> readAllBanks() {
		List<BankEntity> list = null;
		OrmLiteSqliteOpenHelper openHelper = OpenHelperManager.getHelper(getActivity(), EntityOpenHelper.class);
		try {
			Dao<BankEntity, String> dao = openHelper.getDao(BankEntity.class);
			list = dao.queryForAll();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		OpenHelperManager.releaseHelper();
		return list;
	}

	List<List<BankBean>> splitBanks(List<BankEntity> entities) {
		if (HGUtils.isListEmpty(entities)) {
			return null;
		}
		Map<String, List<BankBean>> map = new HashMap<String, List<BankBean>>();
		for (BankEntity entity : entities) {
			if (map.get(entity.getBean().getCardType()) == null) {
				map.put(entity.getBean().getCardType(), new ArrayList<BankBean>());
			}
			List<BankBean> list = map.get(entity.getBean().getCardType());
			list.add(entity.getBean());
		}
		Object[] sortedKeys = map.keySet().toArray();
		Arrays.sort(sortedKeys);

		List<List<BankBean>> listList = new ArrayList<List<BankBean>>();
		for (Object key : sortedKeys) {
			listList.add(map.get(key));
		}
		return listList;
	}

	/**
	 * 
	 */
	private void initTabs() {

		List<List<BankBean>> listList = splitBanks(readAllBanks());
		ActionBar actionBar = getSupportActionBar();
		actionBar.removeAllTabs();
		if (HGUtils.isListEmpty(listList)) {
			return;
		}

		for (final List<BankBean> list : listList) {
			String typeName = HttpDefine.getCardTypeName(list.get(0).getCardType());
			actionBar.addTab(actionBar.newTab().setText(typeName).setTabListener(new ActionBar.TabListener() {
				BankFragment	frag;
				List<BankBean>	finalList	= list;

				@Override
				public void onTabReselected(Tab arg0, FragmentTransaction fragmentTransaction) {

				}

				@Override
				public void onTabSelected(Tab arg0, FragmentTransaction fragmentTransaction) {
					if (frag == null) {
						frag = new BankFragment();
						Bundle b = new Bundle();
						b.putSerializable(BankFragment.IntentKey_Banks, (Serializable) finalList);
						frag.setArguments(b);

						frag.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								BankBean bankBean = (BankBean) parent.getAdapter().getItem(position);
								Intent intent = new Intent();
								intent.putExtra(IntentKey_Bank, bankBean);
								setResult(RESULT_OK, intent);
								finish();
							}

						});
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

	protected void loadDatas(final boolean showProgress) {

		ResponseHandler<GetBanksResponse> responseHandler = new ResponseHandler<GetBanksResponse>() {
			@Override
			public void onStart() {
				if (showProgress) {
					beginEmptyProgress("");
				}
			}

			@Override
			public void onFinish(GetBanksResponse response, String error) {
				if (showProgress) {
					stopEmptyProgress(null);
				}
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntities(BankEntity.class, response.getBankInfos(), 0);
					if (!isDestroyedApi8()) {
						initTabs();
					}
				}
			}
		};
		GetBanksRequest request = new GetBanksRequest();
		HttpPacketClient.postPacketAsynchronous(request, GetBanksResponse.class, responseHandler, true);
	}

}
