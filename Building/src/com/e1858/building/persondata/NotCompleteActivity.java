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
import com.e1858.building.holder.NotCompleteHolder;
import com.e1858.building.httppackage.GetNotCompleteOrdersRequest;
import com.e1858.building.httppackage.GetNotCompleteOrdersResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.ResponseUtils;
import com.hg.android.entitycache.EntityCacheHelper;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;
import com.j256.ormlite.stmt.QueryBuilder;

public class NotCompleteActivity extends NotReserveActivity {
	String						Tag	= Constant.OrderStatus.NOTCOMPLETE + "";
	private NotComSimpleAdapter	notComAdapter;

	@Override
	protected void loadOrders(final int offset) {
		MLog.i(TAG, "加载待完成列表");
		ResponseHandler<GetNotCompleteOrdersResponse> responseHandler = new ResponseHandler<GetNotCompleteOrdersResponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(GetNotCompleteOrdersResponse response, String error) {
				listView.onRefreshComplete();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntities(OrderEntity.class, response.getOrderInfos(),
							offset, Tag);
				}
			}
		};

		GetNotCompleteOrdersRequest request = new GetNotCompleteOrdersRequest();
		request.setOffset(offset);
		request.setCount(Constant.FetchCount);
		HttpPacketClient.postPacketAsynchronous(request, GetNotCompleteOrdersResponse.class, responseHandler, true);
	}

	@Override
	public String setEmptyText() {

		return "没有确认完成的订单";
	}

	@Override
	public OrmLiteIteratorAdapterExt<OrderEntity> setSimpleAdapter() {
		notComAdapter = new NotComSimpleAdapter(mContext);
		return notComAdapter;
	}

	class NotComSimpleAdapter extends OrmLiteIteratorAdapterExt<OrderEntity> {

		public NotComSimpleAdapter(Context context) {
			super(context);
		}

		@Override
		public void buildQueryBuilder(QueryBuilder<OrderEntity, ?> queryBuilder) throws SQLException {
			queryBuilder.where().eq("tag", Tag);
		}

		@Override
		public View newView(Context context, OrderEntity entity, ViewGroup parent) {
			View convertView = View.inflate(mContext, R.layout.not_complete_content_layout, null);
			NotCompleteHolder holder = new NotCompleteHolder(context, convertView, R.layout.not_complete_content_layout);
			convertView.setTag(holder);
			return convertView;
		}

		@Override
		public void bindView(View view, Context context, OrderEntity entity, final int position) {
			final OrderInfo orderInfo = entity.getBean();
			NotCompleteHolder holder = (NotCompleteHolder) view.getTag();
			holder.bindDate(orderInfo, adapter);
		}
	}
}
