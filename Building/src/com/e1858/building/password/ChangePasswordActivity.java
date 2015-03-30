package com.e1858.building.password;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.common.utils.SharedPreferencesUtils;
import com.common.utils.StringUtils;
import com.e1858.building.AppManager;
import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.bean.UserInfo;
import com.e1858.building.httppackage.ChangePasswordReponse;
import com.e1858.building.httppackage.ChangePasswordRequest;
import com.e1858.building.login.LoginActivity;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.order.RobListActivity;
import com.e1858.building.utils.Encrypt;
import com.e1858.building.utils.ResponseUtils;

/**
 * 修改密码
 * 
 * @author momo
 *         2014-12-30下午2:57:07
 */
public class ChangePasswordActivity extends BaseActivity {
	private EditText	input_old_password;
	private EditText	input_password;
	private EditText	input_password_en;

//	private EditText	input_mobile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_pwd);
		input_old_password = (EditText) this.findViewById(R.id.input_old_password);
		input_password = (EditText) this.findViewById(R.id.input_password);
		input_password_en = (EditText) this.findViewById(R.id.input_password_en);
//		input_mobile = (EditText) this.findViewById(R.id.input_mobile);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.save) {
			saveNewPassword();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.save, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 提交新的密码到服务修改操作
	 * void
	 */
	private void saveNewPassword() {
		String old_password = input_old_password.getText().toString().trim();
		String password = input_password.getText().toString().trim();
		String password_en = input_password_en.getText().toString().trim();
//		String mobile = input_mobile.getText().toString().trim();
//		if (StringUtils.isEmpty(mobile)) {
//			showToast("手机号不能为空！");
//			return;
//		}
//		if (!StringUtils.isPhone(mobile)) {
//			showToast(Constant.ToastMessage.MOBILE_REGP_ERROR);
//			return;
//		}
		if (StringUtils.isEmpty(old_password)) {
			showToast("原密码不能为空！");
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
		if (old_password.equals(password)) {
			showToast(Constant.ToastMessage.EQUAL_OLDPASSWORD);
			input_password.setText("");
			input_password_en.setText("");
			return;
		}

		changePwd(old_password, password);
//		changePwd(mobile, old_password, password);
	}

	private void changePwd(String old_password, String password) {
		ResponseHandler<ChangePasswordReponse> responseHandler = new ResponseHandler<ChangePasswordReponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(ChangePasswordReponse response, String error) {
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					SharedPreferencesUtils.setSpParam(mContext, Constant.KEY, "");
					SharedPreferencesUtils.setSpParam(mContext, Constant.USERID, "");
					SharedPreferencesUtils.setSpParam(mContext, Constant.PASSWORD, "");
					UserInfo userInfo = new UserInfo();
					application.setUserName("");
					application.setPassword("");
					application.setUserInfo(userInfo);
					showToast(Constant.ToastMessage.RESETPASS_SUCCESS1);
					startActivity(RobListActivity.class);
					startActivity(LoginActivity.class);
					AppManager.getAppManager().finishAllActivity();
				}
			}
		};

		ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
		changePasswordRequest.setOldPassword(Encrypt.MD5(old_password));
		changePasswordRequest.seteNewPassword(Encrypt.MD5(password));
//		changePasswordRequest.setMobile(mobile);
		HttpPacketClient.postPacketAsynchronous(changePasswordRequest, ChangePasswordReponse.class, responseHandler,
				true);
	}
}
