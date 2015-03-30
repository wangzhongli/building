package com.e1858.building.wallet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.common.utils.ToastUtil;
import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.httppackage.RealNameAuthRequest;
import com.e1858.building.httppackage.RealNameAuthResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.net.HttpPacketClient.SimpleResponseHandler;
import com.e1858.building.net.UploadJson;
import com.e1858.building.utils.RequestUtils;
import com.e1858.building.utils.ResponseUtils;
import com.hg.android.utils.ImageUtils;
import com.hg.android.widget.ImagesGridEditView;

/**
 * 日期: 2015年1月8日 下午2:59:48
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class RealNameAuthActivity extends BaseActivity {
	protected static final String	BroadcastAction_RealNameAuthSubmitted	= "RealNameAuthActivity.BroadcastAction_RealNameAuthSuccess";
	protected static final int		RequestCode_Bank						= 123;

	EditText						editText_realName;
	EditText						editText_IDNumber;
	ImagesGridEditView				imagesGridEditView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_realnameauth);

		editText_realName = (EditText) findViewById(R.id.editText_realName);
		editText_IDNumber = (EditText) findViewById(R.id.editText_IDNumber);
		imagesGridEditView = (ImagesGridEditView) findViewById(R.id.imagesGridEditView);
		imagesGridEditView.setItemHeightScale(1.f);
		imagesGridEditView.setMaxCount(2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.realnameauth, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_accept) {
			onClickSubmit();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 
	 */
	private void onClickSubmit() {
		String realName = editText_realName.getText().toString();
		String IDNumber = editText_IDNumber.getText().toString();
		if (TextUtils.isEmpty(realName)) {
			ToastUtil.show(getActivity(), "请填写姓名");
			return;
		}
		if (TextUtils.isEmpty(IDNumber)) {
			ToastUtil.show(getActivity(), "请填写身份证号码");
			return;
		}
		if (IDNumber.length() < 15) {
			ToastUtil.show(getActivity(), "身份证号码格式错误");
			return;
		}

		if (imagesGridEditView.imagesCount() < 2) {
			ToastUtil.show(getActivity(), "身份证照片必须上传正反两面");
			return;
		}

		submitAuth();
	}

	private void submitAuth() {
		final String realName = editText_realName.getText().toString();
		final String IDNumber = editText_IDNumber.getText().toString();

		final ProgressDialog dialog = new ProgressDialog(getActivity());
		dialog.setMessage("正在处理");

		final List<String> imageFiles = imagesGridEditView.getImageUrls();
		final Thread thread = new Thread() {
			@Override
			public void run() {
				RealNameAuthRequest request = new RealNameAuthRequest();
				request.setRealName(realName);
				request.setIDNumber(IDNumber);

				List<String> imageUrls = new ArrayList<String>();
				boolean hasError = false;
				String error = null;
				for (String file : imageFiles) {
					if (isInterrupted()) {
						break;
					}
					String destJPG = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg")
							.getAbsolutePath();
					if (!ImageUtils.scaleImage(file, destJPG, Constant.PICTURE_MIN_SIDE_LENGTH,
							Constant.PICTURE_MAX_SIZE)) {
						hasError = true;
						error = "图片读取错误";
						break;
					}
					if (isInterrupted()) {
						break;
					}
					UploadJson uploadJson = HttpPacketClient.syncUpdaloadFile(destJPG);
					if (!TextUtils.isEmpty(uploadJson.getUrl())) {
						imageUrls.add(uploadJson.getUrl());
					} else {
						hasError = true;
						error = uploadJson.getError();
						break;
					}
				}

				if ((!hasError) && (!isInterrupted())) {
					request.setPictureUrls(imageUrls);
					ResponseHandler<RealNameAuthResponse> responseHandler = new SimpleResponseHandler<RealNameAuthResponse>();
					HttpPacketClient.postPacketSynchronous(request, RealNameAuthResponse.class, responseHandler, true);
					error = ResponseUtils.checkResponseAndReturnError(responseHandler.responseObject, error);
					hasError = !TextUtils.isEmpty(error);
				}

				final boolean finalHasError = hasError;
				final String finalError = error;
				handler.post(new Runnable() {

					@Override
					public void run() {
						dialog.dismiss();
						if (finalHasError) {
							Toast.makeText(getActivity(), finalError, Toast.LENGTH_LONG).show();
						} else {
							Intent intent = new Intent(BroadcastAction_RealNameAuthSubmitted);
							sendBroadcast(intent);
							ToastUtil.show(getActivity(), "实名认证申请已提交,请耐心等待");
							finish();

							RequestUtils.loadUserInfo();
						}
					}
				});
			}
		};

		thread.start();
		dialog.show();
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				thread.interrupt();
				Toast.makeText(getActivity(), "已取消", Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (imagesGridEditView.handleForRequestCode(requestCode)) {
			imagesGridEditView.onActivityResult(requestCode, resultCode, data);
		}
	}
}
