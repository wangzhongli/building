package com.e1858.building.splash;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.common.utils.MLog;
import com.common.utils.SharedPreferencesUtils;
import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.order.RobListActivity;
import com.e1858.building.utils.LocationManager;
import com.e1858.building.utils.LocationManager.OnloacationListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

public class WelcomeActivity extends Activity implements Runnable {

	protected static final String	TAG	= "WelcomeActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		actionBar.hide();
		setContentView(R.layout.activity_welcome);
		MobclickAgent.updateOnlineConfig(this);
		UmengUpdateAgent.setDefault();
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
		FeedbackAgent agent = new FeedbackAgent(this);
		agent.sync();
		new Thread(this).start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void run() {
		try {
			initLocation();
			Thread.sleep(1300);
			judgeVersion();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void judgeVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			int vercode = info.versionCode;
			int spVercode = (Integer) SharedPreferencesUtils.getSpParam(this, Constant.VERSION_CODE, 0);
			if (spVercode == vercode) {
//				if (application.verifyLoggedInAndGoToLogin(getActivity())) {
//					startActivity(RobListActivity.class);
//				}
//				startActivity(RobListActivity.class);
				Intent intent = new Intent(this, RobListActivity.class);
				startActivity(intent);
				this.finish();
			} else {
//				startActivity(RobListActivity.class);
				Intent intent = new Intent(this, SplashActivity.class);
				startActivity(intent);
				this.finish();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initLocation() {
		LocationManager.getInstanse(this).initLocation(new OnloacationListener() {

			@Override
			public void startLocation() {}

			@Override
			public void endLocation(AMapLocation aMapLocation) {
				//获取位置信息
				Double latitude = aMapLocation.getLatitude();
				Double longitude = aMapLocation.getLongitude();
				String province = aMapLocation.getProvince();//省
				String city = aMapLocation.getCity();//市
				String district = aMapLocation.getDistrict();//区
				String provider = aMapLocation.getProvider();//gps或者网络
				MLog.e(TAG, "latitude" + latitude + ",,,,," + "longitude" + longitude + "provider" + provider);
				MLog.e(TAG, "province" + province + "|" + "city" + city + "|" + "district" + district);
//				setParamToSp(Constant.LATITUDE, latitude.toString());
//				setParamToSp(Constant.LONGITUDE, longitude.toString());
//				setParamToSp(Constant.PROVINCE_LOCATION, province);
//				setParamToSp(Constant.CITY_LOCATION, city);
//				setParamToSp(Constant.DISTRICT, district);

				SharedPreferencesUtils.setSpParam(WelcomeActivity.this, Constant.LATITUDE, latitude.toString());
				SharedPreferencesUtils.setSpParam(WelcomeActivity.this, Constant.LONGITUDE, longitude.toString());
				SharedPreferencesUtils.setSpParam(WelcomeActivity.this, Constant.PROVINCE_LOCATION, province);
				SharedPreferencesUtils.setSpParam(WelcomeActivity.this, Constant.CITY_LOCATION, city);
				SharedPreferencesUtils.setSpParam(WelcomeActivity.this, Constant.DISTRICT, district);
			}

			@Override
			public void errorLocation(String error) {}
		});
	}
}
