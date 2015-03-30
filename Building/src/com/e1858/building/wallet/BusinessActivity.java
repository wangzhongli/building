package com.e1858.building.wallet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.bean.BusinessBean;
import com.e1858.building.entity.BusinessEntity;
import com.e1858.building.entity.EntityOpenHelper;
import com.e1858.building.httppackage.GetMyBusinessesRequest;
import com.e1858.building.httppackage.GetMyBusinessesResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.ResponseUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hg.android.entitycache.EntityCacheHelper;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;
import com.hg.android.utils.HGUtils;
import com.j256.ormlite.stmt.QueryBuilder;

/**
 * 日期: 2015年1月7日 下午4:42:07
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class BusinessActivity extends BaseActivity {

	MyAdapter				adapter;
	PullToRefreshListView	listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.pulltorefresh_listview);
		initViews();
	}

	@Override
	public void onDestroy() {
		if (adapter != null) {
			adapter.unload();
		}
		super.onDestroy();
	}

	void initViews() {
		listView = (PullToRefreshListView) findViewById(R.id.pf_listView);
		listView.setMode(Mode.BOTH);
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadOrders(0);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadOrders(adapter == null ? 0 : adapter.getCount());
			}
		});
		listView.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
		listView.getRefreshableView().setDivider(new ColorDrawable(Color.TRANSPARENT));
		listView.getRefreshableView().setDividerHeight(HGUtils.dip2px(getActivity(), 8));
		listView.setAdapter(adapter = new MyAdapter(this));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				BusinessEntity entity = (BusinessEntity) parent.getAdapter().getItem(position);
				Intent intent = new Intent(getActivity(), BusinessDetailActivity.class);
				intent.putExtra(BusinessDetailActivity.IntentKey_BusinessInfo, entity.getBean());
				startActivity(intent);
			}
		});

		adapter.load(getSupportLoaderManager(), EntityOpenHelper.class, BusinessEntity.class);
	}

	@Override
	public void onResume() {
		super.onResume();
		loadOrders(0);
	}

	protected void loadOrders(final int offset) {

		ResponseHandler<GetMyBusinessesResponse> responseHandler = new ResponseHandler<GetMyBusinessesResponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(GetMyBusinessesResponse response, String error) {
				listView.onRefreshComplete();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntities(BusinessEntity.class,
							response.getBusinessInfos(), offset);
				}
			}
		};

		GetMyBusinessesRequest request = new GetMyBusinessesRequest();
		request.setOffset(offset);
		request.setCount(Constant.FetchCount);
		HttpPacketClient.postPacketAsynchronous(request, GetMyBusinessesResponse.class, responseHandler, true);
	}

	////////
	private class MyAdapter extends OrmLiteIteratorAdapterExt<BusinessEntity> {

		public MyAdapter(Context context) {
			super(context);
		}

		@Override
		public View newView(Context context, BusinessEntity entity, ViewGroup parent) {
			View view = View.inflate(context, R.layout.listitem_business, null);
			ViewHolder holder = new ViewHolder();
			holder.textView_title = (TextView) view.findViewById(R.id.textView_title);
			holder.textView_date = (TextView) view.findViewById(R.id.textView_date);
			holder.textView_amount = (TextView) view.findViewById(R.id.textView_amount);
			holder.textView_state = (TextView) view.findViewById(R.id.textView_state);
			view.setTag(holder);
			return view;
		}

		@Override
		public void bindView(View view, Context context, BusinessEntity entity, int position) {
			ViewHolder holder = (ViewHolder) view.getTag();
			BusinessBean bean = entity.getBean();
			holder.textView_amount.setText("¥" + String.format("%.2f", bean.getMoney()));
			;
			holder.textView_title.setText(bean.getAcceptCardOwner() + "-" + bean.getType());
			holder.textView_date.setText(bean.getTradeTime());
			holder.textView_state.setText(bean.getResult());
		}

		@Override
		public void buildQueryBuilder(QueryBuilder<BusinessEntity, ?> queryBuilder) {
			queryBuilder.orderBy("sortIndex", true);
		}
	}

	class ViewHolder {
		TextView	textView_title;
		TextView	textView_date;
		TextView	textView_amount;
		TextView	textView_state;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}
