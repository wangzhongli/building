package com.e1858.building.notice;

import java.sql.SQLException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.common.utils.SharedPreferencesUtils;
import com.common.utils.StringUtils;
import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.base.BaseListActivity;
import com.e1858.building.entity.EntityOpenHelper;
import com.e1858.building.entity.NoticeEntity;
import com.e1858.building.httppackage.GetNoticesRequest;
import com.e1858.building.httppackage.GetNoticesResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.ResponseUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.hg.android.entitycache.EntityCacheHelper;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;
import com.j256.ormlite.stmt.QueryBuilder;

public class NoticeListActivity extends BaseListActivity {

	private NoticeAdapter	adapter;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new NoticeAdapter(this);
		adapter.load(getSupportLoaderManager(), EntityOpenHelper.class, NoticeEntity.class);
		setAdapter(adapter);
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadNotices(0);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadNotices(adapter == null ? 0 : adapter.getCount());
			}
		});
		setEmptyView("您没有消息提醒", R.color.white);
		setBgColor(R.color.listview_bg_dark);
		loadNotices(0);
	}

	public void loadNotices(final int offset) {
		ResponseHandler<GetNoticesResponse> responseHandler = new ResponseHandler<GetNoticesResponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(GetNoticesResponse response, String error) {
				listView.onRefreshComplete();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntities(NoticeEntity.class, response.getNotices(),
							offset, null);
				}
			}
		};

		GetNoticesRequest request = new GetNoticesRequest();
		request.setOffset(offset);
		request.setCount(Constant.FetchCount);
		request.setType(Constant.NOTICE_TYPE.TYPE_ALL);
		HttpPacketClient.postPacketAsynchronous(request, GetNoticesResponse.class, responseHandler, true);
	}

	class NoticeAdapter extends OrmLiteIteratorAdapterExt<NoticeEntity> {

		public NoticeAdapter(Context context) {
			super(context);
		}

		@Override
		public void buildQueryBuilder(QueryBuilder<NoticeEntity, ?> queryBuilder) throws SQLException {
			queryBuilder.orderBy("sortIndex", true);
		}

		@Override
		public View newView(Context context, NoticeEntity entity, ViewGroup parent) {
			View view = View.inflate(context, R.layout.listitem_notice, null);
			ViewHolder holder = new ViewHolder();
			holder.item_notice_title = (TextView) view.findViewById(R.id.item_notice_title);
			holder.item_notice_time = (TextView) view.findViewById(R.id.item_notice_time);
			view.setTag(holder);
			return view;
		}

		@Override
		public void bindView(View view, Context context, final NoticeEntity entity, int position) {
			ViewHolder holder = (ViewHolder) view.getTag();
			if (!StringUtils.isEmpty(entity.getBean().getReadTime())) {//已读
				holder.item_notice_title.setTextColor(context.getResources().getColor(R.color.text_color_9da9b9));
				SharedPreferencesUtils.setSpParam(context, Constant.NOTICE_NUM, 1);
			} else {
				holder.item_notice_title.setTextColor(context.getResources().getColor(R.color.white));
			}
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Bundle bu = new Bundle();
					bu.putString(NoticeActivity.NOTICE_ID, entity.getBean().getID());
					startActivity(NoticeActivity.class, bu);
				}
			});
			holder.item_notice_title.setText(entity.getBean().getTitle());
			holder.item_notice_time.setText(entity.getBean().getSendTime());
		}

		public class ViewHolder {
			TextView	item_notice_title;
			TextView	item_notice_time;
		}
	}
}
