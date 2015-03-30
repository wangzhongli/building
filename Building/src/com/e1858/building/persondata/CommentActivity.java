package com.e1858.building.persondata;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.bean.CommentInfo;
import com.e1858.building.httppackage.GetCommentInfoReponse;
import com.e1858.building.httppackage.GetCommentInfoRequest;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.ResponseUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hg.android.widget.CircularImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressLint("ResourceAsColor")
public class CommentActivity extends BaseActivity {
	public static final String		ORDERID	= "orderID";
	private String					orderID;
	private List<CommentInfo>		commentInfos;
	private DisplayImageOptions		options;
	protected PullToRefreshListView	listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (null != getIntent()) {
			orderID = getIntent().getStringExtra(ORDERID);
			loadComms(orderID);
		}
		setContentView(R.layout.pulltorefresh_listview);
		listView = (PullToRefreshListView) this.findViewById(R.id.pf_listView);
		listView.setMode(Mode.BOTH);
		listView.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
		listView.getRefreshableView().setDivider(new ColorDrawable(Color.TRANSPARENT));
		View empty = View.inflate(mContext, R.layout.empty_text, null);
		TextView tv = (TextView) empty.findViewById(R.id.empty_tv);
		tv.setTextColor(Color.WHITE);
		tv.setText("暂时没有评论信息");
		listView.setEmptyView(empty);
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.personal_head_icon_def)
				.showImageForEmptyUri(R.drawable.personal_head_icon_def)
				.showImageOnFail(R.drawable.personal_head_icon_def).cacheInMemory(true).cacheOnDisk(true)
				.considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		listView.setMode(Mode.DISABLED);
	}

	public void loadComms(String orderID) {
		ResponseHandler<GetCommentInfoReponse> responseHandler = new ResponseHandler<GetCommentInfoReponse>() {

			@Override
			public void onStart() {
				openProgressDialog("请稍后...");
			}

			@Override
			public void onFinish(GetCommentInfoReponse response, String error) {
				closeProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					commentInfos = response.getCommentInfos();
					CommAdapter adapter = new CommAdapter(commentInfos, mContext);
					listView.setAdapter(adapter);
				}
			}
		};

		GetCommentInfoRequest request = new GetCommentInfoRequest();
		request.setOrderID(orderID);
		HttpPacketClient.postPacketAsynchronous(request, GetCommentInfoReponse.class, responseHandler, true);
	}

	class CommAdapter extends BaseAdapter {
		public List<CommentInfo>	list;
		public Context				mContext;

		public CommAdapter(List<CommentInfo> list, Context mContext) {
			this.list = list;
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public CommentInfo getItem(int position) {
			return list.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (null == convertView) {
				holder = new ViewHolder();
				convertView = View.inflate(mContext, R.layout.comment_item_layout, null);
				holder.head_portrait = (CircularImage) convertView.findViewById(R.id.head_portrait);
				holder.com_item_tv_name = (TextView) convertView.findViewById(R.id.com_item_tv_name);
				holder.com_item_tv_dec = (TextView) convertView.findViewById(R.id.com_item_tv_dec);
				holder.com_item_tv_time = (TextView) convertView.findViewById(R.id.com_item_tv_time);
				holder.com_item_iv_star = (ImageView) convertView.findViewById(R.id.com_item_iv_star);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			CommentInfo commentInfo = list.get(position);
			ImageLoader.getInstance().displayImage(commentInfo.getHeadPortrait(), holder.head_portrait, options);
			holder.com_item_tv_name.setText(commentInfo.getName());
			holder.com_item_tv_dec.setText(commentInfo.getCommentDec());
			holder.com_item_tv_time.setText(commentInfo.getCommentTime());
			holder.com_item_iv_star.setBackgroundResource(star(commentInfo.getStar()));
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		class ViewHolder {
			CircularImage	head_portrait;
			TextView		com_item_tv_name;
			TextView		com_item_tv_dec;
			TextView		com_item_tv_time;
			ImageView		com_item_iv_star;
		}
	}

	private int star(int star) {
		int id = 0;
		switch (star) {
		case 0:
			id = R.drawable.m_pic_star_0;
			break;
		case 1:
			id = R.drawable.m_pic_star_1;
			break;
		case 2:
			id = R.drawable.m_pic_star_2;
			break;
		case 3:
			id = R.drawable.m_pic_star_3;
			break;
		case 4:
			id = R.drawable.m_pic_star_4;
			break;
		case 5:
			id = R.drawable.m_pic_star_5;
			break;

		default:
			break;
		}
		return id;
	}
}
