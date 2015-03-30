package com.e1858.building.utils;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

public class LocationManager implements AMapLocationListener {
	private static LocationManager	instance;
	private LocationManagerProxy	mLocationManagerProxy;
	private OnloacationListener		listener;
	private Context					context;

	private LocationManager() {}

	private LocationManager(Context context) {
		this.context = context;
	}

	public static LocationManager getInstanse(Context context) {

		if (null == instance) {
			instance = new LocationManager(context);
		}
		return instance;
	}

	public void initLocation(OnloacationListener listener) {
		this.listener = listener;
		mLocationManagerProxy = LocationManagerProxy.getInstance(context);
		mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 2000, 15, this);
		listener.startLocation();
	}

	private void stopLocation() {
		if (mLocationManagerProxy != null) {
			mLocationManagerProxy.removeUpdates(this);
			mLocationManagerProxy.destory();
		}
		mLocationManagerProxy = null;
	}

	@Override
	public void onLocationChanged(Location location) {}

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}

	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		if (aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0) {
			listener.endLocation(aMapLocation);
			stopLocation();
		} else {
			listener.errorLocation("定位失败，请重试！");
		}
	}

	/**
	 * 定位监听接口
	 * date: 2015-1-22 上午10:05:50
	 * 
	 * @author wangzhongli
	 */
	public interface OnloacationListener {
		void startLocation();

		void endLocation(AMapLocation aMapLocation);

		void errorLocation(String error);
	}
}
