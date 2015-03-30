package com.e1858.building.wallet;

import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.e1858.building.R;
import com.e1858.building.base.BaseFragment;
import com.e1858.building.bean.BankBean;
import com.e1858.building.utils.ImageCacheUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 日期: 2015年1月7日 下午7:25:34
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class BankFragment extends BaseFragment {
	public static final String	IntentKey_Banks	= "IntentKey_Banks";
	MyAdapter					adapter;
	PullToRefreshListView		listView;
	List<BankBean>				banks;
	private OnItemClickListener	onItemClickListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pulltorefresh_listview, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		banks = (List<BankBean>) getArguments().getSerializable(IntentKey_Banks);
		initViews();
	}

	public PullToRefreshListView getListView() {
		return listView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	void initViews() {
		listView = (PullToRefreshListView) getView().findViewById(R.id.pf_listView);
		listView.setBackgroundColor(getResources().getColor(R.color.listview_bg_dark));
		listView.setMode(Mode.DISABLED);
		listView.setOnItemClickListener(this.onItemClickListener);
		listView.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
		listView.setAdapter(adapter = new MyAdapter());
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	////////
	private class MyAdapter extends BaseAdapter {

		/*
		 */
		@Override
		public int getCount() {
			return banks == null ? 0 : banks.size();
		}

		/*
		 */
		@Override
		public BankBean getItem(int position) {
			return banks.get(position);
		}

		/*
		 */
		@Override
		public long getItemId(int position) {
			return 0;
		}

		/*
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getActivity(), R.layout.listitem_bank, null);
			}
			ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
			TextView textView = (TextView) convertView.findViewById(R.id.textView);
			ImageLoader.getInstance().displayImage(getItem(position).getCardUrl(), imageView,
					ImageCacheUtil.emptyOptions());
			textView.setText(getItem(position).getBankName());
			return convertView;
		}

	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
		if (getListView() != null) {
			getListView().setOnItemClickListener(onItemClickListener);
		}
	}
}
