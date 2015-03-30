package com.e1858.building.order;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.common.utils.StringUtils;
import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.base.BaseListActivity;
import com.e1858.building.bean.LogisticInfo;
import com.e1858.building.holder.ViewHolder;
import com.e1858.building.httppackage.GetLogisticInfosRequest;
import com.e1858.building.httppackage.GetLogisticInfosResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.ResponseUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

public class LogisticInfosActivity extends BaseListActivity {
	public static final String	ORDERID			= "orderID";
	private String				orderID;
	private LogisticAdapter		adapter;
	List<LogisticInfo>			logisticInfos	= new ArrayList<LogisticInfo>();

	public static void action(Context context, String orderId) {
		Intent intent = new Intent(context, LogisticInfosActivity.class);
		intent.putExtra(ORDERID, orderId);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (null != getIntent()) {
			orderID = getIntent().getStringExtra(ORDERID);
		}
		loadLogistics(0);
		setEmptyView("抱歉！暂时没有物流信息！", R.color.login_text_color);
		listView.setMode(Mode.DISABLED);
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadLogistics(0);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadLogistics(adapter == null ? 0 : adapter.getCount());
			}
		});
		listView.getRefreshableView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				listView.getRefreshableView().setItemChecked(position, true);
			}
		});
	}

	protected void loadLogistics(final int offset) {
		if (StringUtils.isEmpty(orderID)) {
			listView.onRefreshComplete();
			return;
		}
		ResponseHandler<GetLogisticInfosResponse> responseHandler = new ResponseHandler<GetLogisticInfosResponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(GetLogisticInfosResponse response, String error) {
				listView.onRefreshComplete();

//				for (int i = 0; i <= 5; i++) {
//					LogisticInfo info = new LogisticInfo();
//					info.setContent("郑州市金水区文化路已经派件");
//					info.setTime("2015年2月28日09:24:54");
//					logisticInfos.add(info);
//				}
//				adapter = new LogisticAdapter(logisticInfos, mContext);
//				setAdapter(adapter);

				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					logisticInfos = response.getLogisticInfos();
					adapter = new LogisticAdapter(logisticInfos, mContext);
					setAdapter(adapter);
				}
			}
		};

		GetLogisticInfosRequest request = new GetLogisticInfosRequest();
		request.setOrderID(orderID);
		request.setOffset(offset);
		request.setCount(Constant.FetchCount);
		HttpPacketClient.postPacketAsynchronous(request, GetLogisticInfosResponse.class, responseHandler, true);
	}

	public class LogisticAdapter extends BaseAdapter {

		List<LogisticInfo>	logisticInfos;
		Context				context;

		public LogisticAdapter(List<LogisticInfo> logisticInfos, Context context) {
			super();
			this.logisticInfos = logisticInfos;
			this.context = context;
		}

		@Override
		public int getCount() {
			return logisticInfos.size() == 0 ? 0 : logisticInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup viewGroup) {
			if (null == convertView) {
				convertView = View.inflate(mContext, R.layout.item_logistic, null);
			}
			TextView logistic_info_time = ViewHolder.get(convertView, R.id.logistic_info_time);
			TextView logistic_info_content = ViewHolder.get(convertView, R.id.logistic_info_content);
			ImageView imageView_icon = ViewHolder.get(convertView, R.id.imageView_icon);
			View sepTop = ViewHolder.get(convertView, R.id.sepTop);
			if (position == 0) {
				sepTop.setVisibility(View.INVISIBLE);
				logistic_info_content.setTextColor(getResources().getColor(R.color.text_color_49aa48));
				logistic_info_time.setTextColor(getResources().getColor(R.color.text_color_49aa48));
				imageView_icon.setImageResource(R.drawable.mall_order_status_green);
			} else {
				logistic_info_content.setTextColor(getResources().getColor(R.color.notes_textColor));
				logistic_info_time.setTextColor(getResources().getColor(R.color.notes_textColor));
				imageView_icon.setImageResource(R.drawable.mall_order_status_gray);
			}
			if (position == logisticInfos.size() - 1) {
				ViewHolder.get(convertView, R.id.sepBottom_30).setVisibility(View.GONE);
			} else {
				ViewHolder.get(convertView, R.id.sepBottom_0).setVisibility(View.GONE);
			}
			LogisticInfo info = logisticInfos.get(position);
			logistic_info_time.setText(info.getTime() + "");
			logistic_info_content.setText(info.getContent() + "");
//			updateBackground(position, convertView);
			return convertView;
		}

	}

	@SuppressLint("NewApi")
	public void updateBackground(int position, View view) {
		int backgroundId;
		if (listView.getRefreshableView().isItemChecked(position + 1)) {
			backgroundId = R.color.gray;
		} else {
			backgroundId = R.color.transparent;
		}
		view.setBackgroundResource(backgroundId);
	}

}
