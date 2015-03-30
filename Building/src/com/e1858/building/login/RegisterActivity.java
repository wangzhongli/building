package com.e1858.building.login;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.common.utils.SIMCardUtil;
import com.common.utils.SharedPreferencesUtils;
import com.common.utils.StringUtils;
import com.common.utils.ToastUtil;
import com.e1858.building.AppManager;
import com.e1858.building.AppUrlConfig;
import com.e1858.building.Constant;
import com.e1858.building.GeTuiManager;
import com.e1858.building.MainApplication;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.base.WebViewActivity;
import com.e1858.building.bean.ServiceTypeBean;
import com.e1858.building.httppackage.GetSMSVerifyRequest;
import com.e1858.building.httppackage.GetSMSVerifyResponse;
import com.e1858.building.httppackage.GetServiceTypesRequest;
import com.e1858.building.httppackage.GetServiceTypesResponse;
import com.e1858.building.httppackage.GetServiceWorkerTypesRequest;
import com.e1858.building.httppackage.GetServiceWorkerTypesResponse;
import com.e1858.building.httppackage.LoginRequest;
import com.e1858.building.httppackage.LoginResponse;
import com.e1858.building.httppackage.RegistRequest;
import com.e1858.building.httppackage.RegistResponse;
import com.e1858.building.net.HttpDefine;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.order.RobListActivity;
import com.e1858.building.utils.Encrypt;
import com.e1858.building.utils.RequestUtils;
import com.e1858.building.utils.ResponseUtils;
import com.hg.android.utils.HGUtils;
import com.hg.android.widget.CityDBManager.AreaEntity;
import com.hg.android.widget.CityPickDialog;
import com.hg.android.widget.CityPickDialog.OnMultiPickedListener;

public class RegisterActivity extends BaseActivity {

	private EditText				input_mobile;
	private EditText				input_verify;
	private EditText				input_password;
	private EditText				input_password_en;
	private EditText				input_worker_name;
	private EditText				input_real_name;
	private TextView				input_service_type;
	private TextView				input_service_area;
	private Button					get_verify_btn;
	private TimeCount				time;
	private boolean					isFinished			= false;
	private CheckBox				agreement_checkbox;
	private List<ServiceTypeBean>	alltypes;
	private List<ServiceTypeBean>	selectedTypes		= new ArrayList<ServiceTypeBean>();
	private String					serviceProvince		= "";								//服务区域 省
	private String					serviceCity			= "";								//服务区域 城市
	private List<String>			serviceDistricts	= null;							//服务类型
	List<String>					serviceWorkersTypes;									//工人工种集合
	List<String>					WorkersTypes;											//工人工种集合
	private TextView				input_service_worker_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initView();
	}

	private void initView() {
		input_mobile = (EditText) this.findViewById(R.id.input_mobile);
		input_verify = (EditText) this.findViewById(R.id.input_verify);
		input_password = (EditText) this.findViewById(R.id.input_password);
		input_password_en = (EditText) this.findViewById(R.id.input_password_en);
		input_worker_name = (EditText) this.findViewById(R.id.input_worker_name);
		input_real_name = (EditText) this.findViewById(R.id.input_real_name);
		input_service_type = (TextView) this.findViewById(R.id.input_service_type);
		input_service_area = (TextView) this.findViewById(R.id.input_service_area);
		input_service_worker_type = (TextView) this.findViewById(R.id.input_service_worker_type);
		agreement_checkbox = (CheckBox) this.findViewById(R.id.agreement_checkbox);
		get_verify_btn = (Button) this.findViewById(R.id.get_verify_btn);
		this.findViewById(R.id.register_btn).setOnClickListener(this);
		this.findViewById(R.id.agreement_textview).setOnClickListener(this);
		input_service_worker_type.setOnClickListener(this);
		input_service_area.setOnClickListener(this);
		input_service_type.setOnClickListener(this);
		time = new TimeCount(90000, 1000);//构造CountDownTimer对象
	}

	private void register() {

		String mobile = input_mobile.getText().toString().trim();
		String verify = input_verify.getText().toString().trim();
		String password = input_password.getText().toString().trim();
		String password_en = input_password_en.getText().toString().trim();
		String worker_name = input_worker_name.getText().toString().trim();
		String real_name = input_real_name.getText().toString().trim();
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
		if (StringUtils.isEmpty(worker_name)) {
			showToast("请输入工人名称");
			return;
		}
		if (StringUtils.isEmpty(real_name)) {
			showToast("请输入真实姓名");
			return;
		}
		if (HGUtils.isListEmpty(selectedTypes)) {
			showToast("请选择服务类型");
			return;
		}
		if (HGUtils.isListEmpty(getWorkersTypes())) {
			showToast("请选择工人工种");
			return;
		}

		if (StringUtils.isEmpty(serviceProvince) && StringUtils.isEmpty(serviceCity)
				&& HGUtils.isListEmpty(serviceDistricts)) {
			showToast("请选择服务区域");
			return;
		}
		if (!agreement_checkbox.isChecked()) {
			showToast(Constant.ToastMessage.AGREEMENT);
			return;
		}
		registerToServer(mobile, verify, password, worker_name, real_name, selectedTypes, serviceProvince, serviceCity,
				serviceDistricts, getWorkersTypes());
	}

	public void registerToServer(final String mobile, String verify, final String password, String worker_name,
			String real_name, List<ServiceTypeBean> selectedTypes, String serviceProvince, String serviceCity,
			List<String> serviceDistricts, List<String> workTypes) {
		ResponseHandler<RegistResponse> responseHandler = new ResponseHandler<RegistResponse>() {
			@Override
			public void onStart() {
				openProgressDialog("请稍后...");
			}

			@Override
			public void onFinish(RegistResponse response, String error) {
				closeProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
//					//加载完成以后
					application.setUserName(mobile);
					application.setPassword(password);
					showToast("注册成功");
					login(mobile, password);
//					startActivity(RobListActivity.class);
				}
			}
		};

		RegistRequest registRequest = new RegistRequest();
		registRequest.setMobile(mobile);
		registRequest.setVerify(verify);
		registRequest.setPassword(Encrypt.MD5(password));
		registRequest.setWorkerName(worker_name);
		registRequest.setWorkerRealName(real_name);
		registRequest.setServiceTypes(selectedTypes);
		registRequest.setServiceProvince(serviceProvince);
		registRequest.setServiceCity(serviceCity);
		registRequest.setServiceDistricts(serviceDistricts);
		registRequest.setServiceWorkersTypes(workTypes);
		HttpPacketClient.postPacketAsynchronous(registRequest, RegistResponse.class, responseHandler, true);
	}

	public void login(String username, String password) {
		ResponseHandler<LoginResponse> responseHandler = new ResponseHandler<LoginResponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(LoginResponse response, String error) {
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					if (HttpDefine.RESPONSE_SUCCESS == response.getRet()) {
						MainApplication.getInstance().setUserInfo(response.getUserInfo());
						MainApplication.getInstance().saveObject(response.getUserInfo(),
								response.getUserInfo().getUserID() + "_#");
						SharedPreferencesUtils.setSpParam(mContext, Constant.KEY, response.getKey());
						SharedPreferencesUtils
								.setSpParam(mContext, Constant.USERID, response.getUserInfo().getUserID());
						if (MainApplication.getInstance().getUserName() != null) {}
						SharedPreferencesUtils.setSpParam(mContext, Constant.USERNAME, MainApplication.getInstance()
								.getUserName());
						if (MainApplication.getInstance().getPassword() != null) {}
						SharedPreferencesUtils.setSpParam(mContext, Constant.PASSWORD, MainApplication.getInstance()
								.getPassword());
//						AppManager.getAppManager().finishActivity(LoginActivity.class);
						finish();
						Intent intent = new Intent(mContext, RobListActivity.class);
						mContext.startActivity(intent);
						RequestUtils.loadUserInfo();
						GeTuiManager.setClientID();
						AppManager.getAppManager().finishActivity(RegisterActivity.this);
					}
				}
			}
		};

		LoginRequest login = new LoginRequest();
		login.setUsername(username);
		login.setPassword(Encrypt.MD5(password));
		HttpPacketClient.postPacketAsynchronous(login, LoginResponse.class, responseHandler, true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_btn://注册
			register();
			break;
		case R.id.input_service_area://选择服务区域
			selectServiceArea();
			break;
		case R.id.input_service_type://选择服务类型
			selectServiceType();
			break;
		case R.id.input_service_worker_type://选择工人工种
			selectWorkerType();
			break;
		case R.id.agreement_textview://注册协议阅读
			// 显示协议
			Intent intent = new Intent(getActivity(), WebViewActivity.class);
			intent.putExtra(WebViewActivity.IntentKey_URL, AppUrlConfig.api.PROTOCOL_URL);
			intent.putExtra(WebViewActivity.IntentKey_Title, "注册协议");
			startActivity(intent);
			break;

		default:
			break;
		}
		super.onClick(v);
	}

	private void selectWorkerType() {

		if (HGUtils.isListEmpty(serviceWorkersTypes)) {
			GetServiceWorkerTypesRequest request = new GetServiceWorkerTypesRequest();
			ResponseHandler<GetServiceWorkerTypesResponse> responseHandler = new ResponseHandler<GetServiceWorkerTypesResponse>() {

				@Override
				public void onStart() {
					showProgressDialog(getActivity(), "正在加载");
				}

				@Override
				public void onFinish(GetServiceWorkerTypesResponse response, String error) {
					hideProgressDialog();
					if (ResponseUtils.checkResponseAndToastError(response, error)) {
						serviceWorkersTypes = response.getServiceWorkersTypes();
						if (!HGUtils.isListEmpty(serviceWorkersTypes)) {
							selectWorkerType();
						}
					}
				}
			};
			HttpPacketClient
					.postPacketAsynchronous(request, GetServiceWorkerTypesResponse.class, responseHandler, true);
		} else {
			CharSequence[] items = new CharSequence[serviceWorkersTypes.size()];
			final boolean[] checkedItems = new boolean[serviceWorkersTypes.size()];
			for (int i = 0; i < items.length; i++) {
				items[i] = serviceWorkersTypes.get(i);
			}

			new AlertDialog.Builder(getActivity())
					.setMultiChoiceItems(items, checkedItems, new OnMultiChoiceClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which, boolean isChecked) {
							checkedItems[which] = isChecked;
						}
					}).setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							List<String> selectedWorkerTypes = new ArrayList<String>();
							for (int i = 0; i < checkedItems.length; i++) {
								if (checkedItems[i]) {
									selectedWorkerTypes.add(serviceWorkersTypes.get(i));
								}
							}
							input_service_worker_type.setText(StringUtils.join("|", selectedWorkerTypes));
							setWorkersTypes(selectedWorkerTypes);
						}
					}).setNegativeButton("取消", null).setTitle("选择工人工种").show();
		}

	}

	public List<String> getWorkersTypes() {
		return WorkersTypes;
	}

	public void setWorkersTypes(List<String> workersTypes) {
		WorkersTypes = workersTypes;
	}

	/**
	 * 服务类型
	 * void
	 */
	private void selectServiceType() {
		if (HGUtils.isListEmpty(alltypes)) {
			GetServiceTypesRequest request = new GetServiceTypesRequest();
			ResponseHandler<GetServiceTypesResponse> responseHandler = new ResponseHandler<GetServiceTypesResponse>() {
				@Override
				public void onStart() {
					showProgressDialog(getActivity(), "正在加载");
				}

				@Override
				public void onFinish(GetServiceTypesResponse response, String error) {
					hideProgressDialog();
					if (ResponseUtils.checkResponseAndToastError(response, error)) {
						alltypes = response.getServiceTypes();
						if (!HGUtils.isListEmpty(alltypes)) {
							selectServiceType();
						}
					}
				}
			};
			HttpPacketClient.postPacketAsynchronous(request, GetServiceTypesResponse.class, responseHandler, true);
		} else {
			CharSequence[] items = new CharSequence[alltypes.size()];
			final boolean[] checkedItems = new boolean[alltypes.size()];
			for (int i = 0; i < items.length; i++) {
				items[i] = alltypes.get(i).getName();
				if (HGUtils.isListEmpty(selectedTypes)) {
					checkedItems[i] = false;
				} else {
					checkedItems[i] = selectedTypes.contains(alltypes.get(i));
				}
			}
			new AlertDialog.Builder(getActivity())
					.setMultiChoiceItems(items, checkedItems, new OnMultiChoiceClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which, boolean isChecked) {
							checkedItems[which] = isChecked;
						}
					}).setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							for (int i = 0; i < checkedItems.length; i++) {
								if (checkedItems[i]) {
									if (!selectedTypes.contains(alltypes.get(i)))
										selectedTypes.add(alltypes.get(i));
								} else {
									selectedTypes.remove(alltypes.get(i));
								}
							}
							StringBuilder typeText = new StringBuilder();
							for (int i = 0, count = selectedTypes.size(); i < count; i++) {
								typeText.append(selectedTypes.get(i).getName());
								if (i < count - 1) {
									typeText.append(",");
								}
							}
							input_service_type.setText(typeText);
						}
					}).setNegativeButton("取消", null).setTitle("请选择服务类型").show();
		}
	}

	private void selectServiceArea() {
		new CityPickDialog(getActivity(), new OnMultiPickedListener() {
			@Override
			public void onMultiPicked(AreaEntity province, AreaEntity city, List<AreaEntity> districts) {
				String districtsText = StringUtils.join("|", districts);
				String text = city.getName() + districtsText;
				input_service_area.setText(text);
				serviceProvince = province.getCode();
				serviceCity = city.getCode();
				List<String> districtCodes = new ArrayList<String>();
				if (!HGUtils.isListEmpty(districts)) {
					for (AreaEntity code : districts) {
						districtCodes.add(code.getCode());
					}
				}
				serviceDistricts = districtCodes;
			}
		}).show();
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
				openProgressDialog("获取验证码中...");
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
		verify.setActionType(Constant.ACTION_TYPE.REGISTE);
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
