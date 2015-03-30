package com.e1858.building.base;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.e1858.building.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

@SuppressLint("ResourceAsColor")
public class BaseListActivity extends BaseActivity {

	protected PullToRefreshListView	listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_pulltorefresh_listview);
		listView = (PullToRefreshListView) this.findViewById(R.id.pf_listView);
		listView.setMode(Mode.BOTH);
		listView.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
		listView.getRefreshableView().setDivider(new ColorDrawable(Color.TRANSPARENT));
	}

	protected void setAdapter(BaseAdapter adapter) {
		if (adapter == null)
			return;
		listView.setAdapter(adapter);
	}

	public void setBgColor(int id) {
		listView.setBackgroundResource(id);
	}

	protected void setEmptyView(String text, int colorId) {
		View empty = View.inflate(mContext, R.layout.empty_text, null);
		TextView tv = (TextView) empty.findViewById(R.id.empty_tv);
		tv.setTextColor(getResources().getColor(colorId));
		tv.setText(text);
		listView.setEmptyView(empty);
	}

	protected PullToRefreshListView getListView() {
		return listView;
	}

	protected void addHeardView(View view) {
		if (view == null)
			return;
		listView.getRefreshableView().addHeaderView(view);
	}

}
