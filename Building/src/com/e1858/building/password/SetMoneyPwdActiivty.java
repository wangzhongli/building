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
import com.e1858.building.MainApplication;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.httppackage.GetSMSVerifyRequest;
import com.e1858.building.httppackage.GetSMSVerifyResponse;
import com.e1858.building.httppackage.SetMoneyPasswordResponse;
import com.e1858.building.httppackage.SetMoneyPasswordRquest;
import com.e1858.building.net.HttpDefine;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.Encrypt;
import com.e1858.building.utils.RequestUtils;
import com.e1858.building.utils.ResponseUtils;
import com.e1858.building.wallet.RealNameAuthActivity;

public class SetMoneyPwdActiivty extends BaseActivity {
	private EditText	input_password;
	private EditText	input_password_en;
	private EditText	input_mobile;
	private EditText	input_verify;
	private EditText	input_real_name, input_id_card;
	private Button		get_verify_btn;
	private TimeCount	time;
	private boolean		isFinished	= false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_money_pwd);
		input_id_card = (EditText) this.findViewById(R.id.input_id_card);
		input_real_name = (EditText) this.findViewById(R.id.input_real_name);
		input_password = (EditText) this.findViewById(R.id.input_password);
		input_password_en = (EditText) this.findViewById(R.id.input_password_en);
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
		if (StringUtils.isEmpty(input_real_name.getText().toString().trim())) {
			ToastUtil.show(mContext, "真实姓名不能为空");
			return;
		}
		if (StringUtils.isEmpty(input_id_card.getText().toString().trim())) {
			ToastUtil.show(mContext, "身份证号不能为空");
			return;
		}
		if (!(input_id_card.getText().toString().trim().length() == 18 || input_id_card.getText().toString().trim()
				.length() == 15)) {
			ToastUtil.show(mContext, "身份证号不合法");
			return;
		}
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
		if (StringUtils.isEmpty(input_password.getText().toString().trim())) {
			ToastUtil.show(mContext, "密码不能为空");
			return;
		}
		if (StringUtils.isEmpty(input_password_en.getText().toString().trim())) {
			ToastUtil.show(mContext, "确认密码不能为空");
			return;
		}
		if (!input_password.getText().toString().trim().equals(input_password_en.getText().toString().trim())) {
			showToast(Constant.ToastMessage.NO_EQUAL_PASSWORD);
			input_password.setText("");
			input_password_en.setText("");
			return;
		}
		ResponseHandler<SetMoneyPasswordResponse> responseHandler = new ResponseHandler<SetMoneyPasswordResponse>() {
			@Override
			public void onStart() {
				openProgressDialog("请稍后...");
			}

			@Override
			public void onFinish(SetMoneyPasswordResponse response, String error) {
				closeProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					application.setPassword(input_password.getText().toString());
					RequestUtils.loadUserInfo();
					showToast("设置钱包密码成功");
					if (!MainApplication.getInstance().isPassAuth()) {
						startActivity(RealNameAuthActivity.class);
					}
					finish();
				}
			}
		};

		SetMoneyPasswordRquest request = new SetMoneyPasswordRquest();
		request.setVerify(input_verify.getText().toString().trim());
		request.seteNewPassword(Encrypt.MD5(input_password.getText().toString().trim()));
		request.setMobile(input_mobile.getText().toString().trim());
		request.setRealName(input_real_name.getText().toString().trim());
		request.setIdCardNumber(input_id_card.getText().toString().trim());
		HttpPacketClient.postPacketAsynchronous(request, SetMoneyPasswordResponse.class, responseHandler, true);
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
				showProgressDialog(getActivity(), "请稍后...");
			}

			@Override
			public void onFinish(GetSMSVerifyResponse response, String error) {
				hideProgressDialog();
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
		verify.setActionType(Constant.ACTION_TYPE.SET_MONEY_PASSWORD);
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
