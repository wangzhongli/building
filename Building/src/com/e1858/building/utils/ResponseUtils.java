package com.e1858.building.utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.e1858.building.MainApplication;
import com.e1858.building.bean.PacketResp;

public class ResponseUtils {
	public static boolean checkResponseAndToastError(PacketResp response, String error) {

		String errorText = checkResponseAndReturnError(response, error);
		if (TextUtils.isEmpty(errorText)) {
			return true;
		}
		Toast.makeText(MainApplication.getInstance(), errorText, Toast.LENGTH_SHORT).show();
		return false;
	}

	public static String checkResponseAndReturnError(PacketResp response, String error) {

		if (response != null && response.isSuccess()) {
			return null;
		} else {
			if (response != null && !TextUtils.isEmpty(response.getError())) {
				return response.getError();
			} else if (!TextUtils.isEmpty(error)) {
				return "服务器连接失败，请稍后重试！";
			} else {
				if (!"服务器返回数据格式错误".equals(error)) {//不让显示这个错误.....就这样判断了吧,本来就不是正经的做法
					return "未知错误";
				} else {
					return error;
				}
			}
		}
	}

}
