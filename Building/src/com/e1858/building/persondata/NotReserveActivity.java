package com.e1858.building.persondata;

import java.sql.SQLException;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.common.utils.MLog;
import com.common.utils.ScreenUtil;
import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.base.BaseListActivity;
import com.e1858.building.bean.OrderInfo;
import com.e1858.building.entity.EntityOpenHelper;
import com.e1858.building.entity.OrderEntity;
import com.e1858.building.holder.NotReserveHolder;
import com.e1858.building.httppackage.GetNotReserveOrdersReponse;
import com.e1858.building.httppackage.GetNotReserveOrdersRequest;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.ResponseUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.hg.android.entitycache.EntityCacheHelper;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;
import com.j256.ormlite.stmt.QueryBuilder;

public class NotReserveActivity extends BaseListActivity {

	TextView										moreTextView;
	LinearLayout									loadProgressBar;
	public OrmLiteIteratorAdapterExt<OrderEntity>	adapter;
	String											Tag	= Constant.OrderStatus.NOTRESERVE + "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = setSimpleAdapter();
		setAdapter(adapter);
		adapter.load(getSupportLoaderManager(), EntityOpenHelper.class, OrderEntity.class);
		setEmptyView(setEmptyText(), R.color.login_text_color);
		listView.getRefreshableView().setDividerHeight(ScreenUtil.px2dip(mContext, 100));
		listView.getRefreshableView().setDivider(
				new ColorDrawable(mContext.getResources().getColor(R.color.bg_color_e6e9f2)));
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
	}

	public String setEmptyText() {
		return "没有等待预约订单";
	}

	@Override
	public void onResume() {
		loadOrders(0);
		super.onResume();
	}

	public OrmLiteIteratorAdapterExt<OrderEntity> setSimpleAdapter() {
		SimpleAdapter adapter = new SimpleAdapter(mContext);
		return adapter;
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	protected void loadOrders(final int offset) {
		MLog.i(TAG, "加载待预约列表");
		ResponseHandler<GetNotReserveOrdersReponse> responseHandler = new ResponseHandler<GetNotReserveOrdersReponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(GetNotReserveOrdersReponse response, String error) {
				listView.onRefreshComplete();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntities(OrderEntity.class, response.getOrderInfos(),
							offset, Tag);
				}
			}
		};

		GetNotReserveOrdersRequest request = new GetNotReserveOrdersRequest();
		request.setOffset(offset);
		request.setCount(Constant.FetchCount);
		HttpPacketClient.postPacketAsynchronous(request, GetNotReserveOrdersReponse.class, responseHandler, true);
	}

	class SimpleAdapter extends OrmLiteIteratorAdapterExt<OrderEntity> {

		public SimpleAdapter(Context context) {
			super(context);
		}

		@Override
		public void buildQueryBuilder(QueryBuilder<OrderEntity, ?> queryBuilder) throws SQLException {
			queryBuilder.where().eq("tag", Tag);
		}

		@Override
		public View newView(Context context, OrderEntity entity, ViewGroup parent) {
			View convertView = View.inflate(mContext, R.layout.content_layout, null);
			NotReserveHolder holder = new NotReserveHolder(context, convertView);
			convertView.setTag(holder);
			return convertView;
		}

		@Override
		public void bindView(View view, Context context, final OrderEntity entity, final int position) {
			final OrderInfo orderInfo = entity.getBean();
			NotReserveHolder holder = (NotReserveHolder) view.getTag();
			holder.bindDate(orderInfo, adapter);
		}
	}
}
