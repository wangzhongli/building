package com.e1858.building;

import com.common.utils.MLog;
import com.common.utils.StringUtils;
import com.e1858.building.httppackage.SetgetuiClientIdRequest;
import com.e1858.building.httppackage.SetgetuiClientIdResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.ResponseUtils;
import com.igexin.sdk.PushManager;

public class GeTuiManager {
	public static void setClientID() {
		ResponseHandler<SetgetuiClientIdResponse> responseHandler = new ResponseHandler<SetgetuiClientIdResponse>() {
			@Override
			public void onStart() {}

			@Override
			public void onFinish(SetgetuiClientIdResponse response, String error) {
				if (ResponseUtils.checkResponseAndToastError(response, error)) {}
			}
		};

		SetgetuiClientIdRequest request = new SetgetuiClientIdRequest();
		request.setClientID(StringUtils.isEmpty(PushManager.getInstance().getClientid(MainApplication.getInstance())) ? "000000000000000000000000000"
				: PushManager.getInstance().getClientid(MainApplication.getInstance()));
		MLog.e("GeTuiManager Clientid", PushManager.getInstance().getClientid(MainApplication.getInstance()) + "");
		request.setDeviceToken("");
		request.setDeviceType(1);
		HttpPacketClient.postPacketAsynchronous(request, SetgetuiClientIdResponse.class, responseHandler, true);
	}
}
