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
import com.e1858.building.holder.NotAcceptHolder;
import com.e1858.building.httppackage.GetNotAcceptOrdersReponse;
import com.e1858.building.httppackage.GetNotAcceptOrdersRequest;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.ResponseUtils;
import com.hg.android.entitycache.EntityCacheHelper;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;
import com.j256.ormlite.stmt.QueryBuilder;

/**
 * date: 2015-1-21 下午3:57:05
 * 
 * @author wangzhongli
 */
public class NotAcceptActivity extends NotReserveActivity {
	String						Tag	= Constant.OrderStatus.NOTACCEPT + "";
	private NotAccSimpleAdapter	notAccadapter;

	@Override
	protected void loadOrders(final int offset) {
		MLog.i(TAG, "加载订待提货列表");
		ResponseHandler<GetNotAcceptOrdersReponse> responseHandler = new ResponseHandler<GetNotAcceptOrdersReponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(GetNotAcceptOrdersReponse response, String error) {
				listView.onRefreshComplete();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntities(OrderEntity.class, response.getOrderInfos(),
							offset, Tag);
				}
			}
		};

		GetNotAcceptOrdersRequest request = new GetNotAcceptOrdersRequest();
		request.setOffset(offset);
		request.setCount(Constant.FetchCount);
		HttpPacketClient.postPacketAsynchronous(request, GetNotAcceptOrdersReponse.class, responseHandler, true);
	}

	@Override
	public String setEmptyText() {
		return "没有待提货的订单";
	}

	@Override
	public OrmLiteIteratorAdapterExt<OrderEntity> setSimpleAdapter() {
		notAccadapter = new NotAccSimpleAdapter(mContext);
		return notAccadapter;
	}

	class NotAccSimpleAdapter extends OrmLiteIteratorAdapterExt<OrderEntity> {

		public NotAccSimpleAdapter(Context context) {
			super(context);
		}

		@Override
		public void buildQueryBuilder(QueryBuilder<OrderEntity, ?> queryBuilder) throws SQLException {
			queryBuilder.where().eq("tag", Tag);
		}

		@Override
		public View newView(Context context, OrderEntity entity, ViewGroup parent) {
			View convertView = View.inflate(mContext, R.layout.not_accept_content_layout, null);
			NotAcceptHolder holder = new NotAcceptHolder(mContext, convertView);
			convertView.setTag(holder);
			return convertView;
		}

		@Override
		public void bindView(View view, Context context, OrderEntity entity, final int position) {
			final OrderInfo orderInfo = entity.getBean();
			NotAcceptHolder holder = (NotAcceptHolder) view.getTag();
			holder.bindDate(orderInfo, notAccadapter);
		}
	}

}
