package com.e1858.building.utils;

import android.content.Context;

import com.common.utils.SharedPreferencesUtils;
import com.common.utils.ToastUtil;
import com.e1858.building.Constant;
import com.e1858.building.MainApplication;
import com.e1858.building.bean.UserInfo;
import com.e1858.building.httppackage.GetMyProfileRequest;
import com.e1858.building.httppackage.GetMyProfileResponse;
import com.e1858.building.httppackage.SignAddressRequest;
import com.e1858.building.httppackage.SignAddressResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;

public class RequestUtils {

	public static void loadUserInfo() {
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
					SharedPreferencesUtils.setSpParam(MainApplication.getInstance(), Constant.USERID, response
							.getUserInfo().getUserID());
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, GetMyProfileResponse.class, responseHandler);
	}

	public static void loadUserInfo(final OnUseInfoFinishListener listener) {
		GetMyProfileRequest request = new GetMyProfileRequest();
		ResponseHandler<GetMyProfileResponse> responseHandler = new ResponseHandler<GetMyProfileResponse>() {

			@Override
			public void onStart() {}

			@Override
			public void onFinish(GetMyProfileResponse response, String error) {
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					MainApplication.getInstance().setUserInfo(response.getUserInfo());
					listener.OnUseInfoFinish(response.getUserInfo());
					MainApplication.getInstance().saveObject(response.getUserInfo(),
							response.getUserInfo().getUserID() + "_#");
					SharedPreferencesUtils.setSpParam(MainApplication.getInstance(), Constant.USERID, response
							.getUserInfo().getUserID());
				}
			}
		};
		HttpPacketClient.postPacketAsynchronous(request, GetMyProfileResponse.class, responseHandler);
	}

	/**
	 * 签到接口
	 * 
	 * @param orderID
	 * @param longitude
	 * @param latitude
	 * @param address
	 *            void
	 */
	public static void signAddress(final Context context, final String orderID, String longitude, String latitude,
			String address, int type, final OnFinishListener listener) {
		ResponseHandler<SignAddressResponse> responseHandler = new ResponseHandler<SignAddressResponse>() {
			@Override
			public void onStart() {
				showProgressDialog(context, "请稍后...");
			}

			@Override
			public void onFinish(SignAddressResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					ToastUtil.show(context, "恭喜您，签到完成！");
					listener.onSuccussFinish();
				}
			}
		};
		SignAddressRequest request = new SignAddressRequest();
		request.setOrderID(orderID);
		request.setLongitude(longitude);
		request.setLatitude(latitude);
		request.setAddress(address);
		request.setType(type);
		HttpPacketClient.postPacketAsynchronous(request, SignAddressResponse.class, responseHandler, true);
	}

	public interface OnFinishListener {
		public void onSuccussFinish();
	}

	public interface OnUseInfoFinishListener {
		public void OnUseInfoFinish(UserInfo userInfo);
	}
}
