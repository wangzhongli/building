package com.e1858.building.persondata;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.common.utils.StringUtils;
import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.base.BaseListActivity;
import com.e1858.building.entity.EntityOpenHelper;
import com.e1858.building.entity.PictureInfoEntity;
import com.e1858.building.httppackage.GetOrderPicturesRequest;
import com.e1858.building.httppackage.GetOrderPicturesResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.ImageCacheUtil;
import com.e1858.building.utils.ResponseUtils;
import com.e1858.building.view.WebPictureBrowser;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.hg.android.entitycache.EntityCacheHelper;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;
import com.j256.ormlite.stmt.QueryBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GetPhotoActivity extends BaseListActivity {
	public static final String	ORDERID	= "orderID";
	public static final String	Tag		= "photo_id";
	private String				orderID;
	private PhotoAdapter		adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (null != getIntent()) {
			orderID = getIntent().getStringExtra(ORDERID);
		}
		adapter = new PhotoAdapter(mContext);
		adapter.load(getSupportLoaderManager(), EntityOpenHelper.class, PictureInfoEntity.class);
		setAdapter(adapter);
		setEmptyView("没有照片，赶快上传吧", R.color.login_text_color);
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadPhoto(0);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadPhoto(adapter == null ? 0 : adapter.getCount());
			}
		});
		loadPhoto(0);
	}

	protected void loadPhoto(final int offset) {
		if (StringUtils.isEmpty(orderID)) {
			listView.onRefreshComplete();
			return;
		}
		ResponseHandler<GetOrderPicturesResponse> responseHandler = new ResponseHandler<GetOrderPicturesResponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(GetOrderPicturesResponse response, String error) {
				listView.onRefreshComplete();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntities(PictureInfoEntity.class,
							response.getPictureInfos(), offset, orderID);
				}
			}
		};

		GetOrderPicturesRequest request = new GetOrderPicturesRequest();
		request.setOrderID(orderID);
		request.setOffset(offset);
		request.setCount(Constant.FetchCount);
		HttpPacketClient.postPacketAsynchronous(request, GetOrderPicturesResponse.class, responseHandler, true);
	}

	class PhotoAdapter extends OrmLiteIteratorAdapterExt<PictureInfoEntity> {

		public PhotoAdapter(Context context) {
			super(context);
		}

		@Override
		public void buildQueryBuilder(QueryBuilder<PictureInfoEntity, ?> queryBuilder) throws SQLException {
			queryBuilder.where().eq("tag", orderID);
			queryBuilder.orderBy("sortIndex", true);
		}

		@Override
		public View newView(Context context, PictureInfoEntity entity, ViewGroup parent) {
			View view = View.inflate(context, R.layout.listitem_update_pic, null);
			ViewHolder holder = new ViewHolder();
			holder.imageView = (ImageView) view.findViewById(R.id.imageView);
			holder.textView = (TextView) view.findViewById(R.id.textView);
			view.setTag(holder);
			return view;
		}

		@Override
		public void bindView(View view, Context context, final PictureInfoEntity entity, int position) {
			ViewHolder holder = (ViewHolder) view.getTag();
			ImageLoader.getInstance().displayImage(entity.getBean().getPictureUrl(), holder.imageView,
					ImageCacheUtil.defaultOptions(), new ImageCacheUtil.AnimateFirstDisplayListener());
			holder.imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, WebPictureBrowser.class);
					ArrayList<String> lists = new ArrayList<String>();
					lists.add(entity.getBean().getPictureUrl());
					intent.putStringArrayListExtra(WebPictureBrowser.IntentKey_Urls, lists);
					mContext.startActivity(intent);
				}
			});
			String time = entity.getBean().getUploadTime();
			if (time.length() > 3) {
				time = time.substring(0, time.length() - 3);
			}
			holder.textView.setText("上传时间: " + time);
		}

		public class ViewHolder {
			ImageView	imageView;
			TextView	textView;
		}
	}
}
