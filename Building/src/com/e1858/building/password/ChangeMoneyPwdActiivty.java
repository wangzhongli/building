package com.e1858.building.password;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.common.utils.SIMCardUtil;
import com.common.utils.StringUtils;
import com.common.utils.ToastUtil;
import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.httppackage.ChangeMoneyPasswordResponse;
import com.e1858.building.httppackage.ChangeMoneyPasswordRquest;
import com.e1858.building.httppackage.GetSMSVerifyRequest;
import com.e1858.building.httppackage.GetSMSVerifyResponse;
import com.e1858.building.net.HttpDefine;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.Encrypt;
import com.e1858.building.utils.ResponseUtils;

public class ChangeMoneyPwdActiivty extends BaseActivity {
	private EditText	input_password_old;
	private EditText	input_password_new;
	private EditText	input_password_confirm;
	private EditText	input_mobile;
	private EditText	input_verify;
	private Button		get_verify_btn;
	private TimeCount	time;
	private boolean		isFinished	= false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_money_pwd);
		input_password_old = (EditText) this.findViewById(R.id.input_password_old);
		input_password_new = (EditText) this.findViewById(R.id.input_password_new);
		input_password_confirm = (EditText) this.findViewById(R.id.input_password_confirm);
		input_mobile = (EditText) this.findViewById(R.id.input_mobile);
		input_verify = (EditText) this.findViewById(R.id.input_verify);
		get_verify_btn = (Button) this.findViewById(R.id.get_verify_btn);
		time = new TimeCount(90000, 1000);

	}

	/**
	 * 提交修改密码
	 * 
	 * @param vvoid
	 */
	public void postPwd(View v) {
		if (StringUtils.isEmpty(input_mobile.getText().toString().trim())) {
			ToastUtil.show(mContext, "手机号不能为空");
			return;
		}
		if (!StringUtils.isPhone(input_mobile.getText().toString().trim())) {
			ToastUtil.show(mContext, "请输入有效的手机号");
			return;
		}
		if (StringUtils.isEmpty(input_verify.getText().toString().trim())) {
			ToastUtil.show(mContext, "验证码不能为空");
			return;
		}
//		if (StringUtils.isEmpty(input_password_old.getText().toString().trim())) {
//			ToastUtil.show(mContext, "旧密码不能为空");
//			return;
//		}
		if (StringUtils.isEmpty(input_password_new.getText().toString().trim())) {
			ToastUtil.show(mContext, "新密码不能为空");
			return;
		}
		if (!input_password_new.getText().toString().equals(input_password_confirm.getText().toString())) {
			showToast(Constant.ToastMessage.NO_EQUAL_PASSWORD);
			input_password_confirm.setText("");
			return;
		}
		ResponseHandler<ChangeMoneyPasswordResponse> responseHandler = new ResponseHandler<ChangeMoneyPasswordResponse>() {
			@Override
			public void onStart() {
				showProgressDialog(getActivity(), "正在修改密码");
			}

			@Override
			public void onFinish(ChangeMoneyPasswordResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error) && !isDestroyedApi8()) {
					application.setPassword(input_password_new.getText().toString());
					showToast("修改钱包密码成功");
					finish();
				}
			}
		};

		ChangeMoneyPasswordRquest request = new ChangeMoneyPasswordRquest();
		request.setVerify(input_verify.getText().toString().trim());
		request.seteNewPassword(Encrypt.MD5(input_password_new.getText().toString().trim()));
		request.setOldPassword(Encrypt.MD5(input_password_old.getText().toString().trim()));
		request.setMobile(input_mobile.getText().toString().trim());
		HttpPacketClient.postPacketAsynchronous(request, ChangeMoneyPasswordResponse.class, responseHandler, true);
	}

	/**
	 * 获取验证码
	 * 
	 * @param vvoid
	 */
	public void getVerify(View v) {

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
						ToastUtil.show(mContext, response.getError());
					}
				}
			}
		};

		GetSMSVerifyRequest verify = new GetSMSVerifyRequest();
		verify.setDeviceToken(SIMCardUtil.getDeviceId(mContext));
		verify.setActionType(Constant.ACTION_TYPE.CHANGE_MONEY_PASSWORD);
		verify.setMobile(input_mobile.getText().toString());
		HttpPacketClient.postPacketAsynchronous(verify, GetSMSVerifyResponse.class, responseHandler, true);
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
