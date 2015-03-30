package com.e1858.building.login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.common.utils.SIMCardUtil;
import com.common.utils.SharedPreferencesUtils;
import com.common.utils.StringUtils;
import com.common.utils.ToastUtil;
import com.e1858.building.AppManager;
import com.e1858.building.Constant;
import com.e1858.building.GeTuiManager;
import com.e1858.building.MainApplication;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.httppackage.GetSMSVerifyRequest;
import com.e1858.building.httppackage.GetSMSVerifyResponse;
import com.e1858.building.httppackage.LoginRequest;
import com.e1858.building.httppackage.LoginResponse;
import com.e1858.building.httppackage.VerifyLoginRequest;
import com.e1858.building.httppackage.VerifyLoginResponse;
import com.e1858.building.net.HttpDefine;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.password.ForgetActivity;
import com.e1858.building.utils.Encrypt;
import com.e1858.building.utils.RequestUtils;
import com.e1858.building.utils.ResponseUtils;

public class LoginActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.addTab(actionBar.newTab().setText("账号登录").setTabListener(new ActionBar.TabListener() {

			@Override
			public void onTabReselected(Tab arg0, FragmentTransaction fragmentTransaction) {

			}

			@Override
			public void onTabSelected(Tab arg0, FragmentTransaction fragmentTransaction) {
				fragmentTransaction.replace(android.R.id.content, new FragmentNormal());
			}

			@Override
			public void onTabUnselected(Tab arg0, FragmentTransaction fragmentTransaction) {

			}
		}));
		actionBar.addTab(actionBar.newTab().setText("动态密码登录").setTabListener(new ActionBar.TabListener() {

			@Override
			public void onTabReselected(Tab arg0, FragmentTransaction fragmentTransaction) {

			}

			@Override
			public void onTabSelected(Tab arg0, FragmentTransaction fragmentTransaction) {
				fragmentTransaction.replace(android.R.id.content, new FragmentVerify());
			}

			@Override
			public void onTabUnselected(Tab arg0, FragmentTransaction fragmentTransaction) {

			}
		}));
	}

	@Override
	public void onClick(View v) {

		super.onClick(v);
	}

	/**
	 * 普通登陆
	 * 
	 * @author momo
	 *         2014-12-27上午11:01:03
	 */
	public static class FragmentNormal extends Fragment implements OnClickListener {

		private EditText		input_userName;
		private EditText		input_password;
		private Context			context;
		private ProgressDialog	progressDialog;

		@SuppressLint("InflateParams")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			context = getActivity();
			View layout = inflater.inflate(R.layout.activity_login_normal, container, false);
			input_userName = (EditText) layout.findViewById(R.id.input_userName);
			input_password = (EditText) layout.findViewById(R.id.input_password);
			Button login_btn = (Button) layout.findViewById(R.id.login_btn);
			Button jump_register = (Button) layout.findViewById(R.id.jump_register);
			Button forget_password = (Button) layout.findViewById(R.id.forget_password);
			login_btn.setOnClickListener(this);
			jump_register.setOnClickListener(this);
			forget_password.setOnClickListener(this);
			initLoginData();
			return layout;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.login_btn:
				normalLogin();
				break;
			case R.id.jump_register:
				startActivity(new Intent(getActivity(), RegisterActivity.class));
				break;
			case R.id.forget_password:
				Intent intent = new Intent(getActivity(), ForgetActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(Constant.USERNAME, input_userName.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
				break;

			default:
				break;
			}
		}

		private void initLoginData() {

			String userName = (String) SharedPreferencesUtils.getSpParam(getActivity(), Constant.USERNAME, "");
			String password = (String) SharedPreferencesUtils.getSpParam(getActivity(), Constant.PASSWORD, "");
			if (!StringUtils.isEmpty(userName)) {
				input_userName.setText(userName);
				input_password.setText(password);
				MainApplication.getInstance().setUserName(userName);
				MainApplication.getInstance().setPassword(password);
			}
		}

		private void normalLogin() {
			String userName = input_userName.getText().toString().trim();
			String password = input_password.getText().toString().trim();
			if (StringUtils.isEmpty(userName)) {
				ToastUtil.show(getActivity(), "手机号不能为空");
				return;
			}
			if (!StringUtils.isPhone(userName)) {
				ToastUtil.show(getActivity(), Constant.ToastMessage.MOBILE_REGP_ERROR);
				return;
			}
			if (StringUtils.isEmpty(password)) {
				ToastUtil.show(getActivity(), "密码不能为空");
				return;
			}
			if (!StringUtils.isPassword(password) || StringUtils.getStringLength(password) < 6
					|| StringUtils.getStringLength(password) > 20) {
				ToastUtil.show(getActivity(), Constant.ToastMessage.PASSWORD_LACK_LENGTH);
				return;
			}
			MainApplication.getInstance().setUserName(userName);
			MainApplication.getInstance().setPassword(password);
			login(userName, password);
		}

		public void openProgressDialog(final String message) {
			if (null == progressDialog) {
				progressDialog = new ProgressDialog(context);
			}
			progressDialog.setMessage(message);
			progressDialog.show();

		}

		public void closeProgressDialog() {
			if (null != progressDialog) {
				progressDialog.dismiss();
				progressDialog = null;
			}
		}

		public void login(String username, String password) {
			ResponseHandler<LoginResponse> responseHandler = new ResponseHandler<LoginResponse>() {
				@Override
				public void onStart() {
					openProgressDialog("登录中...");
				}

				@Override
				public void onFinish(LoginResponse response, String error) {
					closeProgressDialog();
					if (ResponseUtils.checkResponseAndToastError(response, error)) {
						if (HttpDefine.RESPONSE_SUCCESS == response.getRet()) {
							MainApplication.getInstance().setUserInfo(response.getUserInfo());
							MainApplication.getInstance().saveObject(response.getUserInfo(),
									response.getUserInfo().getUserID() + "_#");
							SharedPreferencesUtils.setSpParam(context, Constant.KEY, response.getKey());
							SharedPreferencesUtils.setSpParam(context, Constant.USERID, response.getUserInfo()
									.getUserID());
							if (MainApplication.getInstance().getUserName() != null) {}
							SharedPreferencesUtils.setSpParam(context, Constant.USERNAME, MainApplication.getInstance()
									.getUserName());
							if (MainApplication.getInstance().getPassword() != null) {}
							SharedPreferencesUtils.setSpParam(context, Constant.PASSWORD, MainApplication.getInstance()
									.getPassword());
							AppManager.getAppManager().finishActivity(LoginActivity.class);
//							Intent intent = new Intent(context, RobListActivity.class);
//							context.startActivity(intent);
							RequestUtils.loadUserInfo();
							GeTuiManager.setClientID();
						}
					}
				}
			};

			LoginRequest login = new LoginRequest();
			login.setUsername(username);
			login.setPassword(Encrypt.MD5(password));
			HttpPacketClient.postPacketAsynchronous(login, LoginResponse.class, responseHandler, true);
		}
	}

	/**
	 * 动态密码登录
	 * 
	 * @author momo
	 *         2014-12-27上午11:01:19
	 */
	public static class FragmentVerify extends Fragment implements OnClickListener {

		private EditText			input_userName;
		private EditText			verify_password;
		private Button				get_verify;
		private Context				context;
		private ProgressDialog		progressDialog;
		private static TimeCount	time;
		private static boolean		isFinished	= false;

		@SuppressLint("InflateParams")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			context = getActivity();
			View layout = inflater.inflate(R.layout.activity_login_verify, container, false);
			input_userName = (EditText) layout.findViewById(R.id.input_userName);
			verify_password = (EditText) layout.findViewById(R.id.input_password);
			get_verify = (Button) layout.findViewById(R.id.get_verify);
			Button login_btn = (Button) layout.findViewById(R.id.login_btn);
			Button jump_register = (Button) layout.findViewById(R.id.jump_register);
			Button forget_password = (Button) layout.findViewById(R.id.forget_password);
			login_btn.setOnClickListener(this);
			jump_register.setOnClickListener(this);
			forget_password.setOnClickListener(this);
			get_verify.setOnClickListener(this);
			time = new TimeCount(90000, 1000);//构造CountDownTimer对象
			return layout;
		}

		public void openProgressDialog(final String message) {
			if (null == progressDialog) {
				progressDialog = new ProgressDialog(context);
			}
			progressDialog.setMessage(message);
			progressDialog.show();

		}

		public void closeProgressDialog() {
			if (null != progressDialog) {
				progressDialog.dismiss();
				progressDialog = null;
			}
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.login_btn:
				verifyLogin();
				break;
			case R.id.jump_register:
				startActivity(new Intent(getActivity(), RegisterActivity.class));
				break;
			case R.id.forget_password:
				startActivity(new Intent(getActivity(), ForgetActivity.class));
				break;
			case R.id.get_verify:
				getVerify();
				break;

			default:
				break;
			}
		}

		/**
		 * 获取验证码
		 * void
		 */
		public void getVerify() {
			if (StringUtils.isEmpty(input_userName.getText().toString())) {
				ToastUtil.show(getActivity(), "手机号不能为空");
				return;
			}
			if (!StringUtils.isPhone(input_userName.getText().toString())) {
				ToastUtil.show(getActivity(), "请输入有效的手机号");
				return;
			}
			ResponseHandler<GetSMSVerifyResponse> responseHandler = new ResponseHandler<GetSMSVerifyResponse>() {
				@Override
				public void onStart() {
					openProgressDialog("请稍后...");
				}

				@Override
				public void onFinish(GetSMSVerifyResponse response, String error) {
					closeProgressDialog();
					if (ResponseUtils.checkResponseAndToastError(response, error)) {
						if (response.getRet() == HttpDefine.RESPONSE_SUCCESS) {
							// 发送手机验证
							time.start();//开始计时
							isFinished = false;
						} else {
							isFinished = true;
							time.cancel();
							ToastUtil.show(getActivity(), response.getError());
						}
					}
				}
			};

			GetSMSVerifyRequest verify = new GetSMSVerifyRequest();
			verify.setDeviceToken(SIMCardUtil.getDeviceId(getActivity()));
			verify.setActionType(Constant.ACTION_TYPE.VERIFY_LOGIN);
			verify.setMobile(input_userName.getText().toString());
			HttpPacketClient.postPacketAsynchronous(verify, GetSMSVerifyResponse.class, responseHandler, true);
		}

		/**
		 * 动态密码登陆
		 * void
		 */
		private void verifyLogin() {
			if (StringUtils.isEmpty(input_userName.getText().toString().trim())) {
				ToastUtil.show(getActivity(), "手机号不能为空");
				return;
			}
			if (!StringUtils.isPhone((input_userName.getText().toString().trim()))) {
				ToastUtil.show(getActivity(), Constant.ToastMessage.MOBILE_REGP_ERROR);
				return;
			}
			if (StringUtils.isEmpty(verify_password.getText().toString().trim())) {
				ToastUtil.show(getActivity(), "验证码不能为空");
				return;
			}

			ResponseHandler<VerifyLoginResponse> responseHandler = new ResponseHandler<VerifyLoginResponse>() {
				@Override
				public void onStart() {
					openProgressDialog("登录中...");
				}

				@Override
				public void onFinish(VerifyLoginResponse response, String error) {
					closeProgressDialog();
					if (ResponseUtils.checkResponseAndToastError(response, error)) {
						if (HttpDefine.RESPONSE_SUCCESS == response.getRet()) {
							MainApplication.getInstance().setUserInfo(response.getUserInfo());
							MainApplication.getInstance().saveObject(response.getUserInfo(),
									response.getUserInfo().getUserID() + "_#");
							SharedPreferencesUtils.setSpParam(context, Constant.KEY, response.getKey());
							SharedPreferencesUtils.setSpParam(context, Constant.USERID, response.getUserInfo()
									.getUserID());
//							SharedPreferencesUtils.setSpParam(context, Constant.USERNAME, MainApplication.getInstance()
//									.getUserName());
//							SharedPreferencesUtils.setSpParam(context, Constant.PASSWORD, MainApplication.getInstance()
//									.getPassword());
//							LoginManager.setClientID(handler, application.getCommunity().getID(), PushManager.getInstance()
//									.getClientid(getApplicationContext()), application.getUserID(), getSp().getString("key", ""),
//									Encrypt.MD5(getSp().getString("key", "") + Constant.TokenSalt));
							AppManager.getAppManager().finishActivity(LoginActivity.class);
							GeTuiManager.setClientID();
//							Intent intent = new Intent(context, RobListActivity.class);
//							context.startActivity(intent);
						}
					}
				}
			};

			VerifyLoginRequest verifyLoginRequest = new VerifyLoginRequest();
			verifyLoginRequest.setPhoneNum(input_userName.getText().toString().trim());
			verifyLoginRequest.setVerify(verify_password.getText().toString().trim());
			HttpPacketClient.postPacketAsynchronous(verifyLoginRequest, VerifyLoginResponse.class, responseHandler,
					true);

		}

		/* 定义一个倒计时的内部类 */
		class TimeCount extends CountDownTimer {
			public TimeCount(long millisInFuture, long countDownInterval) {
				super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
			}

			@Override
			public void onFinish() {
				//计时完毕时触发
				get_verify.setText("获取动态验证码");
				get_verify.setEnabled(true);
			}

			@Override
			public void onTick(long millisUntilFinished) {//计时过程显示
				if (isFinished) {
					time.cancel();
					get_verify.setText("获取动态验证码");
					get_verify.setEnabled(true);
				} else {
					get_verify.setEnabled(false);
					get_verify.setText(millisUntilFinished / 1000 + "秒");
				}

			}
		}
	}

}
