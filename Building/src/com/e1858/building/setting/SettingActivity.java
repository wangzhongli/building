package com.e1858.building.setting;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.e1858.building.AppUrlConfig;
import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.base.WebViewActivity;
import com.hg.android.utils.DataCleanHelper;
import com.hg.android.utils.HGUtils;
import com.hg.android.widget.CityDBManager.AreaEntity;
import com.hg.android.widget.CityPickDialog;
import com.hg.android.widget.CityPickDialog.OnCityPickedListener;
import com.igexin.sdk.PushManager;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateStatus;

public class SettingActivity extends BaseActivity {

	TextView	textView_city;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	void updateCity() {
		//定位的城市
		String city_location = getParamFromSp(Constant.CITY_LOCATION, "郑州市");
		//用户选择的城市。默认的是定位的城市
		String city = getParamFromSp(Constant.CITY, city_location);
		String province_location = getParamFromSp(Constant.PROVINCE_LOCATION, "河南省");
		String province = getParamFromSp(Constant.PROVINCE, province_location);
		if (province != null && city != null && province.equalsIgnoreCase(city)) {
			textView_city.setText(city);
		} else {
			textView_city.setText(province + city);
		}
	}

	private void initView() {
		{
			View listitem = findViewById(R.id.listitem_city);
			listitem.setOnClickListener(onClickListener);
			((TextView) listitem.findViewById(R.id.textView_title)).setText("切换城市");
			textView_city = (TextView) listitem.findViewById(R.id.textView_detail);
			updateCity();
		}
		{
			View listitem = findViewById(R.id.listitem_cache);
			listitem.setOnClickListener(onClickListener);
			((TextView) listitem.findViewById(R.id.textView_title)).setText("清理缓存");
		}
		{
			View listitem = findViewById(R.id.listitem_400);
			listitem.setOnClickListener(onClickListener);
			((TextView) listitem.findViewById(R.id.textView_title)).setText("全国免费客服电话");
			((TextView) listitem.findViewById(R.id.textView_detail)).setText(Constant.Service_Phone);
		}
		{
			View listitem = findViewById(R.id.listitem_push);
			((TextView) listitem.findViewById(R.id.textView_title)).setText("接收推送消息");
			View button_switch = listitem.findViewById(R.id.button_switch);
			button_switch.setOnClickListener(onClickListener);
			button_switch.setSelected(getParamFromSp(Constant.PUSHSWITCH, Boolean.TRUE));
		}
		{
			View listitem = findViewById(R.id.listitem_update);
			listitem.setOnClickListener(onClickListener);
			((TextView) listitem.findViewById(R.id.textView_title)).setText("检查更新");
			((TextView) listitem.findViewById(R.id.textView_detail)).setText("当前版本:"
					+ HGUtils.getAppVersionName(getActivity()));
		}
		{
			View listitem = findViewById(R.id.listitem_feedback);
			listitem.setOnClickListener(onClickListener);
			((TextView) listitem.findViewById(R.id.textView_title)).setText("意见反馈");
		}
		{
			View listitem = findViewById(R.id.listitem_about);
			listitem.setOnClickListener(onClickListener);
			((TextView) listitem.findViewById(R.id.textView_title)).setText("关于我们");
		}
	}

	OnClickListener	onClickListener	= new OnClickListener() {
										@Override
										public void onClick(View v) {
											switch (v.getId()) {
											case R.id.listitem_city:
												onClickCity();
												break;
											case R.id.listitem_cache:
												onClickCache();
												break;
											case R.id.listitem_400:
												onClick400();
												break;
											case R.id.button_switch:
												onClickPush(v);
												break;
											case R.id.listitem_update:
												onClickUpdate();
												break;
											case R.id.listitem_feedback:
												onClickFeedback();
												break;
											case R.id.listitem_about:
												onClickAbout();
												break;
											}
										}
									};

	void onClickCity() {
		new CityPickDialog(getActivity(), new OnCityPickedListener() {

			@Override
			public void onPicked(AreaEntity province, AreaEntity city, String text) {
				setParamToSp(Constant.CITY, city.getName());
				setParamToSp(Constant.PROVINCE, province.getName());
				updateCity();
			}
		}).show();
	}

	/**
	 * 
	 */
	protected void onClickAbout() {
		Intent intent = new Intent(getActivity(), WebViewActivity.class);
		intent.putExtra(WebViewActivity.IntentKey_URL, AppUrlConfig.api.ABOUT_URL);
		intent.putExtra(WebViewActivity.IntentKey_Title, "关于我们");
		startActivity(intent);
	}

	/**
	 * 
	 */
	protected void onClickFeedback() {
		FeedbackAgent agent = new FeedbackAgent(this);
		agent.startFeedbackActivity();
	}

	/**
	 * 
	 */
	protected void onClickUpdate() {
		final ProgressDialog dialog = ProgressDialog.show(this, null, "正在检查更新");
		dialog.setCancelable(true);
		UmengUpdateAgent.setDefault();
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new com.umeng.update.UmengUpdateListener() {

			@Override
			public void onUpdateReturned(int updateStatus, com.umeng.update.UpdateResponse updateInfo) {
				dialog.dismiss();
				switch (updateStatus) {
				case UpdateStatus.Yes: // has update
					UmengUpdateAgent.showUpdateDialog(getActivity(), updateInfo);
					break;
				case UpdateStatus.No: // has no update
					Toast.makeText(getActivity(), "当前是最新版本", Toast.LENGTH_SHORT).show();
					break;
				case UpdateStatus.NoneWifi: // none wifi
					Toast.makeText(getActivity(), "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
					break;
				case UpdateStatus.Timeout: // time out
					Toast.makeText(getActivity(), "检查版本超时", Toast.LENGTH_SHORT).show();
					break;
				}
			}

		});
		UmengUpdateAgent.forceUpdate(getActivity());
	}

	/**
	 * 
	 */
	protected void onClick400() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + Constant.Service_Phone));
		startActivity(intent);
	}

	/**
	 * 
	 */
	protected void onClickPush(View pushSwitch) {
		pushSwitch.setSelected(!pushSwitch.isSelected());
		setParamToSp(Constant.PUSHSWITCH, pushSwitch.isSelected());
		if (pushSwitch.isSelected()) {
			PushManager.getInstance().turnOnPush(getApplicationContext());
		} else {
			PushManager.getInstance().turnOffPush(getApplicationContext());
		}
	}

	/**
	 * 
	 */
	protected void onClickCache() {

		openProgressDialog("正在清除缓存");
		new Thread() {
			@Override
			public void run() {
				DataCleanHelper.cleanInternalCache(getActivity());
				DataCleanHelper.cleanExternalCache(getActivity());
				if (handler == null) {
					return;
				}
				handler.post(new Runnable() {

					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(getActivity(), "缓存已清空", Toast.LENGTH_SHORT).show();
					}
				});
			}
		}.start();
	}
}
