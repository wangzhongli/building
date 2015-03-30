package com.e1858.building.persondata;

import java.sql.SQLException;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.common.utils.MLog;
import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.bean.OrderInfo;
import com.e1858.building.entity.OrderEntity;
import com.e1858.building.holder.OrderCancelHolder;
import com.e1858.building.httppackage.GetCanceledOrdersRequest;
import com.e1858.building.httppackage.GetCanceledOrdersResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.ResponseUtils;
import com.hg.android.entitycache.EntityCacheHelper;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;
import com.j256.ormlite.stmt.QueryBuilder;

public class OrderCancelActivity extends NotReserveActivity {
	private CanSimpleAdapter	canAdapter;
	String						Tag	= Constant.OrderStatus.CANCELED + "";

	protected void loadOrders(final int offset) {
		MLog.i(TAG, "加载取消订单列表");
		ResponseHandler<GetCanceledOrdersResponse> responseHandler = new ResponseHandler<GetCanceledOrdersResponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(GetCanceledOrdersResponse response, String error) {
				listView.onRefreshComplete();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntities(OrderEntity.class, response.getOrderInfos(),
							offset, Tag);
				}
			}
		};

		GetCanceledOrdersRequest request = new GetCanceledOrdersRequest();
		request.setOffset(offset);
		request.setCount(Constant.FetchCount);
		HttpPacketClient.postPacketAsynchronous(request, GetCanceledOrdersResponse.class, responseHandler, true);
	}

	@Override
	public String setEmptyText() {

		return "没有已经取消的订单";
	}

	@Override
	public OrmLiteIteratorAdapterExt<OrderEntity> setSimpleAdapter() {
		canAdapter = new CanSimpleAdapter(mContext);
		return canAdapter;
	}

	class CanSimpleAdapter extends OrmLiteIteratorAdapterExt<OrderEntity> {

		public CanSimpleAdapter(Context context) {
			super(context);
		}

		@Override
		public void buildQueryBuilder(QueryBuilder<OrderEntity, ?> queryBuilder) throws SQLException {
			queryBuilder.where().eq("tag", Tag);
		}

		@Override
		public View newView(Context context, OrderEntity entity, ViewGroup parent) {
			View convertView = View.inflate(mContext, R.layout.cancel_content_layout, null);
			OrderCancelHolder holder = new OrderCancelHolder(context, convertView);
			convertView.setTag(holder);
			return convertView;
		}

		@Override
		public void bindView(View view, Context context, OrderEntity entity, final int position) {
			final OrderInfo orderInfo = entity.getBean();
			OrderCancelHolder holder = (OrderCancelHolder) view.getTag();
			holder.bindDate(orderInfo, adapter);
		}
	}

}
