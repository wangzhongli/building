package com.e1858.building.password;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.common.utils.SIMCardUtil;
import com.common.utils.SharedPreferencesUtils;
import com.common.utils.StringUtils;
import com.common.utils.ToastUtil;
import com.e1858.building.AppManager;
import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.bean.UserInfo;
import com.e1858.building.httppackage.FindPasswordRequest;
import com.e1858.building.httppackage.FindPasswordResponse;
import com.e1858.building.httppackage.GetSMSVerifyRequest;
import com.e1858.building.httppackage.GetSMSVerifyResponse;
import com.e1858.building.net.HttpDefine;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.Encrypt;
import com.e1858.building.utils.ResponseUtils;

/**
 * 忘记密码
 * 
 * @author momo
 *         2014-12-30下午12:59:39
 */
public class ForgetActivity extends BaseActivity {
	private EditText	input_mobile;
	private EditText	input_verify;
	private EditText	input_password;
	private EditText	input_password_en;
	private TimeCount	time;
	private boolean		isFinished	= false;
	private Button		get_verify_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget);
		initView();
	}

	private void initView() {
		input_mobile = (EditText) this.findViewById(R.id.input_mobile);
		input_verify = (EditText) this.findViewById(R.id.input_verify);
		input_password = (EditText) this.findViewById(R.id.input_password);
		input_password_en = (EditText) this.findViewById(R.id.input_password_en);
		get_verify_btn = (Button) this.findViewById(R.id.get_verify_btn);
		time = new TimeCount(90000, 1000);//构造CountDownTimer对象
	}

	/**
	 * 获取验证码
	 * void
	 */
	public void getVerify(View view) {
		if (StringUtils.isEmpty(input_mobile.getText().toString())) {
			ToastUtil.show(mContext, "手机号不能为空");
			return;
		}
		if (!StringUtils.isPhone(input_mobile.getText().toString())) {
			ToastUtil.show(mContext, "请输入有效的手机号");
			return;
		}
		ResponseHandler<GetSMSVerifyResponse> responseHandler = new ResponseHandler<GetSMSVerifyResponse>() {
			@Override
			public void onStart() {
				openProgressDialog("正在获取验证码...");
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
						ToastUtil.show(mContext, response.getError());
					}
				}
			}
		};

		GetSMSVerifyRequest verify = new GetSMSVerifyRequest();
		verify.setDeviceToken(SIMCardUtil.getDeviceId(mContext));
		verify.setActionType(Constant.ACTION_TYPE.RESET_PASSWORD);
		verify.setMobile(input_mobile.getText().toString());
		HttpPacketClient.postPacketAsynchronous(verify, GetSMSVerifyResponse.class, responseHandler, true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.save) {
			saveNewPassword();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 提交新的密码到服务修改操作
	 * void
	 */
	private void saveNewPassword() {
		String mobile = input_mobile.getText().toString().trim();
		String verify = input_verify.getText().toString().trim();
		String password = input_password.getText().toString().trim();
		String password_en = input_password_en.getText().toString().trim();
		if (StringUtils.isEmpty(mobile)) {
			showToast("手机号不能为空！");
			return;
		}
		if (!StringUtils.isPhone(mobile)) {
			showToast(Constant.ToastMessage.MOBILE_REGP_ERROR);
			return;
		}
		if (StringUtils.isEmpty(verify)) {
			showToast(Constant.ToastMessage.EMPTY_VERIFY);
			return;
		}
		if (StringUtils.isEmpty(password)) {
			showToast(Constant.ToastMessage.EMPTY_PASSWORD);
			return;
		}
		if (!StringUtils.isPassword(password) || StringUtils.getStringLength(password) < 6
				|| StringUtils.getStringLength(password) > 20) {
			showToast(Constant.ToastMessage.PASSWORD_LACK_LENGTH);
			return;
		}
		if (StringUtils.isEmpty(password_en)) {
			showToast(Constant.ToastMessage.CONFIRM_PASSWORD);
			return;
		}
		if (!password_en.equals(password)) {
			showToast(Constant.ToastMessage.NO_EQUAL_PASSWORD);
			input_password.setText("");
			input_password_en.setText("");
			return;
		}

		forgetPwd(mobile, verify, password);
	}

	private void forgetPwd(String mobile, String verify, String password) {

		ResponseHandler<FindPasswordResponse> responseHandler = new ResponseHandler<FindPasswordResponse>() {
			@Override
			public void onStart() {
				openProgressDialog("请稍后...");
			}

			@Override
			public void onFinish(FindPasswordResponse response, String error) {
				closeProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					SharedPreferencesUtils.setSpParam(mContext, Constant.KEY, "");
					SharedPreferencesUtils.setSpParam(mContext, Constant.USERID, "");
					SharedPreferencesUtils.setSpParam(mContext, Constant.PASSWORD, "");
					UserInfo userInfo = new UserInfo();
					application.setUserName("");
					application.setPassword("");
					application.setUserInfo(userInfo);
					showToast(Constant.ToastMessage.RESETPASS_SUCCESS);
					AppManager.getAppManager().finishActivity();
				}
			}
		};

		FindPasswordRequest findPasswordRequest = new FindPasswordRequest();
		findPasswordRequest.setPhoneNum(mobile);
		findPasswordRequest.setVerify(verify);
		findPasswordRequest.seteNewPassword(Encrypt.MD5(password));
		HttpPacketClient.postPacketAsynchronous(findPasswordRequest, FindPasswordResponse.class, responseHandler, true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.save, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* 定义一个倒计时的内部类 */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {
			//计时完毕时触发
			get_verify_btn.setText("获取验证码");
			get_verify_btn.setEnabled(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {//计时过程显示
			if (isFinished) {
				time.cancel();
				get_verify_btn.setText("获取验证码");
				get_verify_btn.setEnabled(true);
			} else {
				get_verify_btn.setEnabled(false);
				get_verify_btn.setText(millisUntilFinished / 1000 + "秒");
			}

		}
	}
}
