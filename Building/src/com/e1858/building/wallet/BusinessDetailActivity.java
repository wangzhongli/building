package com.e1858.building.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.bean.BusinessBean;

/**
 * 日期: 2015年1月7日 下午5:05:41
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class BusinessDetailActivity extends BaseActivity {

	public static final String	IntentKey_BusinessInfo	= "IntentKey_BusinessInfo";
	static final int[]			ListItemIds				= { R.id.listitem_type, //
			R.id.listitem_amount,//
			R.id.listitem_acceptAccount,//
			R.id.listitem_receivedTime,//
			R.id.listitem_type,//
			R.id.listitem_serviceType,//
			R.id.listitem_tradeTime,//
			R.id.listitem_serialNumber,//
			R.id.listitem_state,//
														};
	TextView					textView_type;
	SparseArray<TextView>		textViews				= new SparseArray<TextView>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_businessinfo_detail);
		if (getIntent() == null) {
			finish();
			return;
		}

		BusinessBean businessInfo = (BusinessBean) getIntent().getSerializableExtra(IntentKey_BusinessInfo);
		if (businessInfo == null) {
			finish();
			return;
		}

		initView();
		updateView(businessInfo);
	}

	/**
 */
	private void initView() {
		String[] titles = getResources().getStringArray(R.array.businessinfo_detail_titles);
		textView_type = (TextView) findViewById(R.id.listitem_amount).findViewById(R.id.textView_title);
		for (int i = 0; i < ListItemIds.length; i++) {
			int id = ListItemIds[i];
			View listitem = findViewById(id);
			((TextView) listitem.findViewById(R.id.textView_title)).setText(titles[i]);
			textViews.put(id, (TextView) listitem.findViewById(R.id.textView_detail));
		}
	}

	/**
	 * @param userInfo
	 */
	private void updateView(BusinessBean info) {
		if (info == null) {
			return;
		}
		textView_type.setText(info.getType());

		textViews.get(R.id.listitem_acceptAccount).setText(
				info.getAcceptCardOwner() + "\n" + info.getAcceptCardBankName() + "\n" + info.getAcceptCardNumer());
		textViews.get(R.id.listitem_receivedTime).setText(info.getReceivedTime());
		textViews.get(R.id.listitem_type).setText(info.getType());
		textViews.get(R.id.listitem_serviceType).setText(info.getServiceType());
		textViews.get(R.id.listitem_tradeTime).setText(info.getTradeTime());
		textViews.get(R.id.listitem_serialNumber).setText(info.getSerialNumber());
		textViews.get(R.id.listitem_state).setText(info.getResult());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

}
