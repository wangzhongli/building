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
import com.e1858.building.holder.OrderCompleteHolder;
import com.e1858.building.httppackage.GetCompleteOrdersRequest;
import com.e1858.building.httppackage.GetCompleteOrdersResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.ResponseUtils;
import com.hg.android.entitycache.EntityCacheHelper;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;
import com.j256.ormlite.stmt.QueryBuilder;

public class OrderCompleteActivity extends NotReserveActivity {
	String						Tag	= Constant.OrderStatus.COMPLETE + "";
	private ComSimpleAdapter	comAdapter;

	@Override
	protected void loadOrders(final int offset) {
		MLog.i(TAG, "加载订单完成列表");
		ResponseHandler<GetCompleteOrdersResponse> responseHandler = new ResponseHandler<GetCompleteOrdersResponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(GetCompleteOrdersResponse response, String error) {
				listView.onRefreshComplete();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntities(OrderEntity.class, response.getOrderInfos(),
							offset, Tag);
				}
			}
		};

		GetCompleteOrdersRequest request = new GetCompleteOrdersRequest();
		request.setOffset(offset);
		request.setCount(Constant.FetchCount);
		HttpPacketClient.postPacketAsynchronous(request, GetCompleteOrdersResponse.class, responseHandler, true);
	}

	@Override
	public String setEmptyText() {

		return "没有已完成的订单";
	}

	@Override
	public OrmLiteIteratorAdapterExt<OrderEntity> setSimpleAdapter() {
		comAdapter = new ComSimpleAdapter(mContext);
		return comAdapter;
	}

	class ComSimpleAdapter extends OrmLiteIteratorAdapterExt<OrderEntity> {

		public ComSimpleAdapter(Context context) {
			super(context);
		}

		@Override
		public void buildQueryBuilder(QueryBuilder<OrderEntity, ?> queryBuilder) throws SQLException {
			queryBuilder.where().eq("tag", Tag);
		}

		@Override
		public View newView(Context context, OrderEntity entity, ViewGroup parent) {
			View convertView = View.inflate(mContext, R.layout.complete_content_layout, null);
			OrderCompleteHolder holder = new OrderCompleteHolder(context, convertView, R.layout.complete_content_layout);
			convertView.setTag(holder);
			return convertView;
		}

		@Override
		public void bindView(View view, Context context, OrderEntity entity, final int position) {
			final OrderInfo orderInfo = entity.getBean();
			OrderCompleteHolder holder = (OrderCompleteHolder) view.getTag();
			holder.bindDate(orderInfo, adapter);
		}
	}

}
