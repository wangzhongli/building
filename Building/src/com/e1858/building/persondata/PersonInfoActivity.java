package com.e1858.building.persondata;

import java.io.File;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.common.utils.FileUtils;
import com.common.utils.MLog;
import com.common.utils.SharedPreferencesUtils;
import com.common.utils.StringUtils;
import com.common.utils.ToastUtil;
import com.e1858.building.Constant;
import com.e1858.building.MainApplication;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.bean.UserInfo;
import com.e1858.building.httppackage.EditHeadPortraitRequest;
import com.e1858.building.httppackage.EditHeadPortraitResponse;
import com.e1858.building.httppackage.GetMyProfileRequest;
import com.e1858.building.httppackage.GetMyProfileResponse;
import com.e1858.building.httppackage.ModifyMyProfileRequest;
import com.e1858.building.httppackage.ModifyMyProfileResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.net.HttpPacketClient.SimpleResponseHandler;
import com.e1858.building.net.UploadJson;
import com.e1858.building.password.PasswordManagerActivity;
import com.e1858.building.utils.ImageCacheUtil;
import com.e1858.building.utils.ResponseUtils;
import com.e1858.building.wallet.RealNameAuthActivity;
import com.google.gson.Gson;
import com.hg.android.app.CropImageActivity;
import com.hg.android.utils.TakeCropHelper;
import com.hg.android.widget.CircularImage;
import com.hg.android.widget.CityDBManager;
import com.hg.android.widget.CityDBManager.AreaEntity;
import com.hg.android.widget.CityPickDialog;
import com.hg.android.widget.CityPickDialog.OnPickedListener;
import com.hg.android.widget.EditTextDialog;
import com.hg.android.widget.EditTextDialog.OnFinishedListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 日期: 2015年1月6日 下午5:24:22
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */

public class PersonInfoActivity extends BaseActivity {
	Gson						gson								= new Gson();
	private CircularImage		imageView_head;
	private TextView			textView_username;
	private TextView			textView_nickname;
	private TextView			textView_realname;
	private TextView			textView_phone;
	private TextView			textView_address;
	private TextView			textView_city;
	private UserInfo			userInfo;
	private DisplayImageOptions	options;
	TakeCropHelper				takeCropHelper;
	public static final int		CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE	= 100;
	public static final int		PICK_IMAGE_ACTIVITY_REQUEST_CODE	= 200;

	public static String		recomm_pic_dir						= FileUtils.getSDPath() + File.separator + "e1858"
																			+ File.separator + "head" + File.separator;
	private static final String	PHOTO_FILE_NAME						= "temp_photo.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_info);
		takeCropHelper = new TakeCropHelper(getActivity(), true, new TakeCropHelper.OnFinishedListener() {
			@Override
			public void onFinished(String file) {
				ImageLoader.getInstance().displayImage("file://" + file, imageView_head);
				submitHeadImage(file);
			}
		});
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.personal_head_icon_def)
				.showImageForEmptyUri(R.drawable.personal_head_icon_def)
				.showImageOnFail(R.drawable.personal_head_icon_def).cacheInMemory(true).cacheOnDisk(true)
				.considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

		initView();
		updateView(MainApplication.getInstance().getUserInfo());
		loadUserInfo();
	}

	/**
	 * 
	 */
	private void initView() {
		View listitem_head = findViewById(R.id.listitem_head);
		imageView_head = (CircularImage) listitem_head.findViewById(R.id.imageView);
		listitem_head.setOnClickListener(onClickListener);

		View listitem_username = findViewById(R.id.listitem_username);
		((TextView) listitem_username.findViewById(R.id.textView_title)).setText("登陆账号");
		listitem_username.findViewById(R.id.imageView_arrow).setVisibility(View.INVISIBLE);
		textView_username = (TextView) listitem_username.findViewById(R.id.textView_detail);
		//listitem_username.setOnClickListener(onClickListener);

		View listitem_nickname = findViewById(R.id.listitem_nickname);
		((TextView) listitem_nickname.findViewById(R.id.textView_title)).setText("工人名称");
		textView_nickname = (TextView) listitem_nickname.findViewById(R.id.textView_detail);
		listitem_nickname.setOnClickListener(onClickListener);

		View listitem_realname = findViewById(R.id.listitem_realname);
		((TextView) listitem_realname.findViewById(R.id.textView_title)).setText("真实姓名");
		textView_realname = (TextView) listitem_realname.findViewById(R.id.textView_detail);
		//listitem_realname.setOnClickListener(onClickListener);
		listitem_realname.findViewById(R.id.imageView_arrow).setVisibility(View.INVISIBLE);

		View listitem_phone = findViewById(R.id.listitem_phone);
		((TextView) listitem_phone.findViewById(R.id.textView_title)).setText("手机号码");
		textView_phone = (TextView) listitem_phone.findViewById(R.id.textView_detail);
		//listitem_phone.setOnClickListener(onClickListener);
		listitem_phone.findViewById(R.id.imageView_arrow).setVisibility(View.INVISIBLE);

		View listitem_city = findViewById(R.id.listitem_city);
		((TextView) listitem_city.findViewById(R.id.textView_title)).setText("所在地区");
		textView_city = (TextView) listitem_city.findViewById(R.id.textView_detail);
		listitem_city.setOnClickListener(onClickListener);

		View listitem_address = findViewById(R.id.listitem_address);
		((TextView) listitem_address.findViewById(R.id.textView_title)).setText("详细地址");
		textView_address = (TextView) listitem_address.findViewById(R.id.textView_detail);
		listitem_address.setOnClickListener(onClickListener);

		View listitem_password = findViewById(R.id.listitem_password);
		((TextView) listitem_password.findViewById(R.id.textView_title)).setText("密码管理");
		listitem_password.setOnClickListener(onClickListener);

		View listitem_auth = findViewById(R.id.listitem_auth);
		((TextView) listitem_auth.findViewById(R.id.textView_title)).setText("实名认证");
		listitem_auth.setOnClickListener(onClickListener);
	}

	/**
	 * @param userInfo
	 */
	private void updateView(UserInfo userInfo) {
		if (userInfo == null) {
			return;
		}
		//克隆一下
		this.userInfo = gson.fromJson(gson.toJson(userInfo), UserInfo.class);
		ImageLoader.getInstance().displayImage(userInfo.getHeadPortrait(), imageView_head,
				ImageCacheUtil.headPortraitOptions());
		textView_nickname.setText(userInfo.getWorkerName());
		textView_username.setText(userInfo.getUserName());
		textView_realname.setText(userInfo.getRealName());
		textView_phone.setText(userInfo.getMobile());
		textView_address.setText(userInfo.getAddress());
		CityDBManager manager = CityDBManager.sharedInstance(getActivity());
		manager.openDatabase();
		AreaEntity city = manager.areaForCode(userInfo.getCity());
		AreaEntity district = manager.areaForCode(userInfo.getDistrict());
		manager.closeDatabase();
		textView_city.setText((city != null ? city.getName() : "") + (district != null ? district.getName() : ""));
	}

	private void clickOnPhoto() {
		new AlertDialog.Builder(getActivity()).setTitle("修改头像")
				.setItems(R.array.pick_photo_items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if (arg1 == 0) {
							openCamera();
						} else if (arg1 == 1) {
							openAblum();
						}
					}
				}).show();
	}

	/**
	 * 
	 */
	protected void clickOnPassword() {
		startActivity(new Intent(getActivity(), PasswordManagerActivity.class));
	}

	/**
	 * 
	 */
	protected void clickOnPhone() {
		OnFinishedListener l = new OnFinishedListener() {

			@Override
			public void onFinished(String text) {
				if (TextUtils.isEmpty(text)) {
					textView_phone.setText(text);
					userInfo.setMobile(text);
				} else if (StringUtils.isPhone(text)) {
					textView_phone.setText(text);
					userInfo.setMobile(text);
				} else {
					ToastUtil.show(getActivity(), "手机号码格式错误");
				}
			}
		};
		new EditTextDialog(getActivity(), "手机号码", "", textView_phone.getText(), l).show();
	}

	protected void clickOnCity() {
		new CityPickDialog(getActivity(), new OnPickedListener() {
			@Override
			public void onPicked(AreaEntity province, AreaEntity city, AreaEntity district, String text) {
				textView_city.setText((city != null ? city.getName() : null)
						+ (district != null ? district.getName() : null));
				userInfo.setProvince(province != null ? province.getCode() : "");
				userInfo.setCity(city != null ? city.getCode() : "");
				userInfo.setDistrict(district != null ? district.getCode() : "");
			}
		}).show();
	}

	/**
	 * 
	 */
	protected void clickOnAddress() {
		OnFinishedListener l = new OnFinishedListener() {

			@Override
			public void onFinished(String text) {
				textView_address.setText(text);
				userInfo.setAddress(text);
			}
		};
		new EditTextDialog(getActivity(), "详细地址", "请输入详细地址", textView_address.getText(), l).show();
	}

	/**
	 * 
	 */
	protected void clickOnRealname() {
		OnFinishedListener l = new OnFinishedListener() {

			@Override
			public void onFinished(String text) {
				textView_realname.setText(text);
				userInfo.setRealName(text);
			}
		};
		new EditTextDialog(getActivity(), "真实姓名", "请输入真实姓名", textView_realname.getText(), l).show();
	}

	/**
	 * 
	 */
	protected void clickOnNickname() {
		OnFinishedListener l = new OnFinishedListener() {

			@Override
			public void onFinished(String text) {
				textView_nickname.setText(text);
				userInfo.setWorkerName(text);
			}
		};
		new EditTextDialog(getActivity(), "工人名称", "请输入工人名称", textView_nickname.getText(), l).show();
	}

	/**
	 * 
	 */
	protected void clickOnUsername() {}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode != RESULT_OK) {
//			return;
//		}
//		takeCropHelper.onActivityResult(requestCode, resultCode, data);
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.personinfo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_accept) {
			submitMyProfile();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 
	 */
	void submitMyProfile() {
		ModifyMyProfileRequest request = new ModifyMyProfileRequest();
		request.setUserInfo(userInfo);
		ResponseHandler<ModifyMyProfileResponse> responseHandler = new ResponseHandler<ModifyMyProfileResponse>() {

			@Override
			public void onStart() {
				showProgressDialog(getActivity(), "正在保存");
			}

			@Override
			public void onFinish(ModifyMyProfileResponse response, String error) {
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					hideProgressDialog();
					ToastUtil.show(getActivity(), "保存成功");
					loadUserInfo();
//					finish();
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, ModifyMyProfileResponse.class, responseHandler, true);
	}

	/**
	 * 
	 */
	private void loadUserInfo() {
		GetMyProfileRequest request = new GetMyProfileRequest();
		ResponseHandler<GetMyProfileResponse> responseHandler = new ResponseHandler<GetMyProfileResponse>() {

			@Override
			public void onStart() {}

			@Override
			public void onFinish(GetMyProfileResponse response, String error) {
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					MainApplication.getInstance().setUserInfo(response.getUserInfo());
					MainApplication.getInstance().saveObject(response.getUserInfo(),
							response.getUserInfo().getUserID() + "_#");
					//保存头像地址
					setParamToSp(Constant.HEADURL, response.getUserInfo().getHeadPortrait());
					updateView(response.getUserInfo());
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, GetMyProfileResponse.class, responseHandler, true);
	}

	void submitHeadImage(final String file) {
		openProgressDialog("正在上传头像");
		new Thread() {
			@Override
			public void run() {
				UploadJson uploadJson = HttpPacketClient.syncUpdaloadFile(file);
				String toast = "上传成功";
				if (!TextUtils.isEmpty(uploadJson.getUrl())) {
					EditHeadPortraitRequest request = new EditHeadPortraitRequest();
					request.setHeadPortrait(uploadJson.getUrl());
					ResponseHandler<EditHeadPortraitResponse> responseHandler = new SimpleResponseHandler<EditHeadPortraitResponse>();
					HttpPacketClient.postPacketSynchronous(request, EditHeadPortraitResponse.class, responseHandler,
							true);
					if (responseHandler.responseObject == null || !responseHandler.responseObject.isSuccess()) {
						if (responseHandler.responseObject != null) {
							toast = responseHandler.responseObject.getError();
						} else {
							toast = responseHandler.error;
						}
					}
				} else {
					toast = uploadJson.getError();
				}
				final String finalToast = toast;
				handler.post(new Runnable() {
					@Override
					public void run() {
						ToastUtil.show(getActivity(), finalToast);
						closeProgressDialog();
					}
				});
			}
		}.start();

	}

	OnClickListener	onClickListener	= new OnClickListener() {

										@Override
										public void onClick(View view) {
											switch (view.getId()) {
											case R.id.listitem_head:
												clickOnPhoto();
												break;
											case R.id.listitem_username:
												clickOnUsername();
												break;
											case R.id.listitem_nickname:
												clickOnNickname();
												break;
											case R.id.listitem_realname:
												clickOnRealname();
												break;
											case R.id.listitem_phone:
												clickOnPhone();
												break;
											case R.id.listitem_city:
												clickOnCity();
												break;
											case R.id.listitem_address:
												clickOnAddress();
												break;
											case R.id.listitem_password:
												clickOnPassword();
												break;
											case R.id.listitem_auth:
												auth();
												break;
											}
										}
									};
	private File	temp;

	private void openCamera() {
		try {
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			// 判断存储卡是否可以用，可用进行存储
			if (hasSdcard()) {
				temp = new File(recomm_pic_dir);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(temp, PHOTO_FILE_NAME)));
			}
			startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		}
		catch (Exception e) {
			ToastUtil.show(this, "打开相机失败");
			e.printStackTrace();
		}
	}

	protected void auth() {
		String file = SharedPreferencesUtils.getSpParam(mContext, Constant.USERID, "") + "_#";
		UserInfo user = (UserInfo) MainApplication.getInstance().readObject(file);
		if (Constant.AUTH.SUCCESS == user.getRealNameAuthState()) {
			MLog.e(TAG, "auth通过");
			ToastUtil.show(mContext, "您的实名认证已通过，不需要重新认证！");
		} else if (Constant.AUTH.AUTHING == user.getRealNameAuthState()) {
			ToastUtil.show(mContext, "实名认证正在审核中，请耐心等待");
		} else if (Constant.AUTH.NOT_UPDATE == user.getRealNameAuthState()) {
			startActivity(RealNameAuthActivity.class);
		} else if (Constant.AUTH.AUTH_FAIL == user.getRealNameAuthState()) {
			ToastUtil.show(mContext, "实名认证失败，请重新认证");
			startActivity(RealNameAuthActivity.class);
		} else {

		}
	}

	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	private void openAblum() {
		try {
			Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(intent, PICK_IMAGE_ACTIVITY_REQUEST_CODE);
		}
		catch (Exception e) {
			e.printStackTrace();
			ToastUtil.show(this, "打开图库失败");
		}
//		Intent intent = new Intent(PersonInfoActivity.this, SelectPicActivity.class);
//		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case PICK_IMAGE_ACTIVITY_REQUEST_CODE://打开图库
				if (data != null) {
					// 得到图片的全路径
					Uri uri = data.getData();
					startPhotoZoomFromUri(uri);
				}
				break;
			case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE://打开照相机
				if (hasSdcard()) {
					File tempFile = new File(temp, PHOTO_FILE_NAME);
					startPhotoZoomFromUri(Uri.fromFile(tempFile));
				} else {
					ToastUtil.show(PersonInfoActivity.this, "未找到存储卡，无法存储照片！");
				}
				break;
			default:
				break;
			}
		} else if (resultCode == CropImageActivity.PICTURE_CORP_RESULT) {
			if (data != null) {
				String path = data.getStringExtra("cropImagePath");
				setPicToView(path);
			}
		}
	}

	private void setPicToView(String path) {
		MLog.e("PersonInfoActivity", path);
		submitHeadImage(path);
		path = "file://" + path;
		ImageLoader.getInstance().displayImage(path, imageView_head, options);
	}

	public void startPhotoZoomFromUri(Uri uri) {
		try {
			Intent intent = new Intent(PersonInfoActivity.this, CropImageActivity.class);
			intent.putExtra("bitmap", uri);
			intent.putExtra("bitmap_name", System.currentTimeMillis() + ".jpg");
			intent.putExtra("cropWidth", 300);
			intent.putExtra("cropHeight", 300);
			PersonInfoActivity.this.startActivityForResult(intent, CropImageActivity.PICTURE_CORP_RESULT);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
}
