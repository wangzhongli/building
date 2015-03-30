package com.e1858.building;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.os.Handler;

import com.common.utils.MLog;
import com.common.utils.SDCardUtil;
import com.common.utils.SharedPreferencesUtils;
import com.common.utils.StringUtils;
import com.common.utils.ToastUtil;
import com.e1858.building.bean.UserInfo;
import com.e1858.building.entity.EntityOpenHelper;
import com.e1858.building.login.LoginActivity;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.receiver.NetworkMonitor;
import com.e1858.building.utils.DialogUtil;
import com.e1858.building.utils.ImageCacheUtil;
import com.e1858.building.wallet.RealNameAuthActivity;
import com.hg.android.entitycache.EntityCacheHelper;

public class MainApplication extends Application {
	private NetworkMonitor			networkMonitor;
	private static final String		TAG					= "MainApplication";
	private boolean					networkAvailable	= true;
	private boolean					loginOK				= false;
	private Handler					currentHandler;
	private static MainApplication	mInstance			= null;
	private String					userName;
	private String					password;
	private UserInfo				userInfo;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		EntityCacheHelper.initInstance(mInstance, EntityOpenHelper.class);
		HttpPacketClient.getInstaince().setApiServer(AppUrlConfig.api.BASE_URL);
		HttpPacketClient.getInstaince().setFileServer(AppUrlConfig.api.UPLOAD_URL);

		if (null == networkMonitor) {
			networkMonitor = new NetworkMonitor(this);
			this.getApplicationContext().registerReceiver(networkMonitor,
					new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		}
		init();
	}

	public static MainApplication getInstance() {
		return mInstance;
	}

	private void init() {
		if (SDCardUtil.externalMemoryAvailable()) {
			Constant.DIRCONFIG.ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
					+ Constant.DIRCONFIG.ROOT_NAME + File.separator;
			Constant.DIRCONFIG.CRASH_PATH = Constant.DIRCONFIG.ROOT_PATH + "error/";
			Constant.DIRCONFIG.DATABASE_PATH = Constant.DIRCONFIG.ROOT_PATH + "database/";
			Constant.DIRCONFIG.CACHE_PATH = Constant.DIRCONFIG.ROOT_PATH + "cache/";
		}
		ImageCacheUtil.init(mInstance);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		if (null != networkMonitor) {
			this.getApplicationContext().unregisterReceiver(networkMonitor);
		}
	}

	public boolean isNetworkAvailable() {
		return networkAvailable;
	}

	public void setNetworkAvailable(boolean networkAvailable) {
		this.networkAvailable = networkAvailable;
	}

	public boolean isLoginOK() {
		return loginOK;
	}

	public void setLoginOK(boolean loginOK) {
		this.loginOK = loginOK;
	}

	public Handler getCurrentHandler() {
		return currentHandler;
	}

	public void setCurrentHandler(Handler currentHandler) {
		this.currentHandler = currentHandler;
	}

	/**
	 * 判断缓存是否存在
	 * 
	 * @param cachefile
	 * @return
	 */
	private boolean isExistDataCache(String cachefile) {
		boolean exist = false;
		File data = getFileStreamPath(cachefile);
		if (data.exists())
			exist = true;
		return exist;
	}

	public boolean verifyLoggedInAndGoToLogin(Activity activity) {
		if ((!StringUtils.isEmpty((String) (SharedPreferencesUtils.getSpParam(mInstance, Constant.USERID, ""))))
				&& (!StringUtils.isEmpty((String) (SharedPreferencesUtils.getSpParam(mInstance, Constant.KEY, ""))))) {
			return true;
		} else {
			Intent intent = new Intent(activity, LoginActivity.class);
			activity.startActivity(intent);
			return false;
		}
	}

	public boolean isPassAuth(final Activity activity) {
		String file = SharedPreferencesUtils.getSpParam(mInstance, Constant.USERID, "") + "_#";
		UserInfo user = (UserInfo) readObject(file);
		if (Constant.AUTH.SUCCESS == user.getRealNameAuthState()) {
			MLog.e(TAG, "auth通过");
			return true;
		} else if (Constant.AUTH.AUTHING == user.getRealNameAuthState()) {
			ToastUtil.show(getInstance(), "实名认证正在审核中，请耐心等待");
			return false;
		} else if (Constant.AUTH.NOT_UPDATE == user.getRealNameAuthState()) {
			DialogUtil.dialogOpen(activity, "温馨提示", "对不起，只有通过实名认证后才能抢单！", "实名认证",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(activity, RealNameAuthActivity.class);
							activity.startActivity(intent);
						}
					});

			return false;
		} else if (Constant.AUTH.AUTH_FAIL == user.getRealNameAuthState()) {
			ToastUtil.show(getInstance(), "实名认证失败，请重新认证");
			Intent intent = new Intent(activity, RealNameAuthActivity.class);
			activity.startActivity(intent);
			return false;
		}
		return false;
	}

	public boolean isPassAuth() {
		String file = SharedPreferencesUtils.getSpParam(mInstance, Constant.USERID, "") + "_#";
		UserInfo user = (UserInfo) readObject(file);
		if (Constant.AUTH.SUCCESS == user.getRealNameAuthState()) {
			MLog.e(TAG, "auth通过");
			return true;
		}
		return false;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * 保存对象
	 * 
	 * @param ser
	 * @param file
	 * @throws IOException
	 */
	public boolean saveObject(Serializable ser, String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = openFileOutput(file, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			try {
				oos.close();
			}
			catch (Exception e) {}
			try {
				fos.close();
			}
			catch (Exception e) {}
		}
	}

	/**
	 * 读取对象
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public Serializable readObject(String file) {
		if (!isExistDataCache(file))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable) ois.readObject();
		}
		catch (FileNotFoundException e) {}
		catch (Exception e) {
			e.printStackTrace();
			//反序列化失败 - 删除缓存文件
			if (e instanceof InvalidClassException) {
				File data = getFileStreamPath(file);
				data.delete();
			}
		}
		finally {
			try {
				ois.close();
			}
			catch (Exception e) {}
			try {
				fis.close();
			}
			catch (Exception e) {}
		}
		return null;
	}

}
