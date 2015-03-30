package com.e1858.building.mycase;

import java.io.Serializable;
import java.sql.SQLException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.base.BaseFragment;
import com.e1858.building.entity.CaseEntity;
import com.e1858.building.entity.EntityOpenHelper;
import com.e1858.building.httppackage.GetMyCasesRequest;
import com.e1858.building.httppackage.GetMyCasesResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.ImageCacheUtil;
import com.e1858.building.utils.ResponseUtils;
import com.e1858.building.view.WebPictureBrowser;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hg.android.entitycache.EntityCacheHelper;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;
import com.j256.ormlite.stmt.QueryBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CaseListFragment extends BaseFragment {

	public static final String	IntentKey_ServiceTypeID	= "IntentKey_ServiceTypeID";
	MyAdapter					adapter;
	PullToRefreshListView		listView;
	String						serviceTypeID;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pulltorefresh_listview, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		serviceTypeID = getArguments().getString(IntentKey_ServiceTypeID);
		initViews();
		loadCases(0);
	}

	@Override
	public void onDestroyView() {
		if (adapter != null) {
			adapter.unload();
		}
		super.onDestroyView();
	}

	void initViews() {
		listView = (PullToRefreshListView) getView().findViewById(R.id.pf_listView);
		listView.setMode(Mode.BOTH);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				CaseEntity entity = (CaseEntity) arg0.getAdapter().getItem(arg2);
				Intent intent = new Intent(getActivity(), WebPictureBrowser.class);
				intent.putExtra(WebPictureBrowser.IntentKey_Index, 0);
				intent.putExtra(WebPictureBrowser.IntentKey_Urls, (Serializable) entity.getBean().getPictureUrls());
				intent.putExtra(WebPictureBrowser.IntentKey_Remark, entity.getBean().getRemark());
				startActivity(intent);
			}
		});
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadCases(0);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadCases(adapter == null ? 0 : adapter.getCount());
			}
		});
		listView.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
		listView.setAdapter(adapter = new MyAdapter(getActivity()));
		adapter.load(getLoaderManager(), EntityOpenHelper.class, CaseEntity.class);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	protected void loadCases(final int offset) {

		ResponseHandler<GetMyCasesResponse> responseHandler = new ResponseHandler<GetMyCasesResponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(GetMyCasesResponse response, String error) {
				listView.onRefreshComplete();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntities(CaseEntity.class, response.getCases(), offset,
							serviceTypeID);
				}
			}
		};

		GetMyCasesRequest request = new GetMyCasesRequest();
		request.setServiceTypeID(serviceTypeID);
		request.setOffset(offset);
		request.setCount(Constant.FetchCount);
		HttpPacketClient.postPacketAsynchronous(request, GetMyCasesResponse.class, responseHandler, true);
	}

	////////
	private class MyAdapter extends OrmLiteIteratorAdapterExt<CaseEntity> {
		public MyAdapter(Context context) {
			super(context);
		}

		@Override
		public View newView(Context context, CaseEntity entity, ViewGroup parent) {
			View view = View.inflate(context, R.layout.listitem_mycase, null);
			ViewHolder holder = new ViewHolder();
			holder.imageView = (ImageView) view.findViewById(R.id.imageView);
			holder.textView = (TextView) view.findViewById(R.id.textView);
			view.setTag(holder);
			return view;
		}

		@Override
		public void bindView(View view, Context context, CaseEntity entity, int position) {
			ViewHolder viewHolder = (ViewHolder) view.getTag();
			ImageLoader.getInstance().displayImage(entity.getBean().getPictureUrls().get(0), viewHolder.imageView,
					ImageCacheUtil.defaultOptions(), new ImageCacheUtil.AnimateFirstDisplayListener());
			String time = entity.getBean().getCreateTime();
			if (time.length() > 3) {
				time = time.substring(0, time.length() - 3);
			}
			viewHolder.textView.setText("上传时间: " + time);
		}

		@Override
		public void buildQueryBuilder(QueryBuilder<CaseEntity, ?> queryBuilder) throws SQLException {
			queryBuilder.where().eq("tag", serviceTypeID);
			queryBuilder.orderBy("sortIndex", true);
		}

		public class ViewHolder {
			ImageView	imageView;
			TextView	textView;
		}
	}
}
