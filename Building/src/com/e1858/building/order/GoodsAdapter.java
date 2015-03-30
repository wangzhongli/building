package com.e1858.building.order;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.e1858.building.R;
import com.e1858.building.bean.GoodsInfo;
import com.e1858.building.holder.ViewHolder;
import com.e1858.building.utils.ImageCacheUtil;
import com.e1858.building.view.WebPictureBrowser;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GoodsAdapter extends BaseAdapter {
	public List<GoodsInfo>	goodsInfo;
	public Context			context;

	public GoodsAdapter(List<GoodsInfo> goodsInfo, Context context) {
		super();
		this.goodsInfo = goodsInfo;
		this.context = context;
	}

	@Override
	public int getCount() {
		return goodsInfo == null ? 0 : goodsInfo.size();
	}

	@Override
	public GoodsInfo getItem(int position) {
		return goodsInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.order_info_goods_item, parent, false);
		}
		TextView roborder_item_tv_goods_type = ViewHolder.get(convertView, R.id.roborder_item_tv_goods_type);
		TextView roborder_item_tv_goods_name = ViewHolder.get(convertView, R.id.roborder_item_tv_goods_name);
		TextView roborder_item_tv_goods_num = ViewHolder.get(convertView, R.id.roborder_item_tv_goods_num);
		TextView roborder_item_tv_goods_guige = ViewHolder.get(convertView, R.id.roborder_item_tv_goods_guige);
		ImageView order_info_item_iv = ViewHolder.get(convertView, R.id.order_info_item_iv);
		final GoodsInfo goodsInfo = getItem(position);
		roborder_item_tv_goods_type.setText(goodsInfo.getGoodsType() + "");
		roborder_item_tv_goods_name.setText(goodsInfo.getGoodsName() + "");
		roborder_item_tv_goods_num.setText(goodsInfo.getGoodsNum() + "");
		roborder_item_tv_goods_guige.setText(goodsInfo.getGoodsStandard() + "");
		ImageLoader.getInstance().displayImage(goodsInfo.getImageUrl(), order_info_item_iv,
				ImageCacheUtil.defaultOptions(), new ImageCacheUtil.AnimateFirstDisplayListener());
		order_info_item_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, WebPictureBrowser.class);
				ArrayList<String> lists = new ArrayList<String>();
				lists.add(goodsInfo.getImageUrl());
				intent.putStringArrayListExtra(WebPictureBrowser.IntentKey_Urls, lists);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	@Override
	public boolean isEmpty() {
		return goodsInfo == null;
	}

}
