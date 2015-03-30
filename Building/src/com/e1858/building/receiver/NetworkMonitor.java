package com.e1858.building.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Handler;
import android.os.Message;

import com.common.utils.ToastUtil;
import com.e1858.building.Constant;
import com.e1858.building.MainApplication;
import com.e1858.building.net.MessageWhat;
import com.e1858.building.net.ThreadPool;
import com.e1858.building.R;

public class NetworkMonitor extends BroadcastReceiver {
	private Handler			handler;
	private MainApplication	application;
	private NetworkInfo		networkInfo_WIFI;
	private NetworkInfo		networkInfo_Mobile;
	private State			state_WIFI;
	private State			state_Mobile;

	public NetworkMonitor(MainApplication application) {
		this.application = application;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

			handler = application.getCurrentHandler();
			if (networkInfo != null && networkInfo.isAvailable()) {
				application.setNetworkAvailable(true);

				if (application.isLoginOK()) {
					ToastUtil.show(application, application.getResources().getString(R.string.network_success));
					ThreadPool.execute(new Runnable() {
						public void run() {
							try {
								Thread.sleep(Constant.NETWORK_AVAILABLE_SOCKET_OPEN_INTERVAL);
							}
							catch (InterruptedException e) {}

//							application.getSocketSession().open();
						}
					});
				}
				if (null != handler) {
					Message msg = handler.obtainMessage(MessageWhat.NETWORK_OPENED, networkInfo);
					handler.sendMessage(msg);
				}
			} else {

				ToastUtil.show(context, context.getResources().getString(R.string.network_fail));
				application.setNetworkAvailable(false);
				if (null != handler) {
					Message msg = handler.obtainMessage(MessageWhat.NETWORK_CLOSED, networkInfo);
					handler.sendMessage(msg);
				}
			}

			networkInfo_WIFI = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			networkInfo_Mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			state_WIFI = networkInfo_WIFI.getState();
			if (networkInfo_Mobile != null) {
				state_Mobile = networkInfo_Mobile.getState();
			}

			if (State.CONNECTED == state_WIFI) {
				if (null != handler) {
					Message msg = handler.obtainMessage(MessageWhat.NETWORK_WIFI_OPENED, networkInfo_WIFI);
					handler.sendMessage(msg);
				}
			} else {
				if (null != handler) {
					Message msg = handler.obtainMessage(MessageWhat.NETWORK_WIFI_CLOSED, networkInfo_WIFI);
					handler.sendMessage(msg);
				}
			}

			if (State.CONNECTED == state_Mobile) {
				if (State.CONNECTED != state_WIFI && application.isLoginOK()) {
					ToastUtil.show(application, application.getResources().getString(R.string.network_mobile));
				}
				if (null != handler) {
					Message msg = handler.obtainMessage(MessageWhat.NETWORK_MOBILE_OPENED, networkInfo_Mobile);
					handler.sendMessage(msg);
				}
			} else {
				if (null != handler) {
					Message msg = handler.obtainMessage(MessageWhat.NETWORK_MOBILE_CLOSED, networkInfo_Mobile);
					handler.sendMessage(msg);
				}
			}
		}
	}
}
