package com.e1858.building.httppackage;

import com.common.utils.SharedPreferencesUtils;
import com.e1858.building.Constant;
import com.e1858.building.MainApplication;

@SuppressWarnings("serial")
public abstract class AutoFillPacketRequest extends PacketRequest {

	public AutoFillPacketRequest(int command) {
		super(command);
		this.setKey(SharedPreferencesUtils.getSpParam(MainApplication.getInstance(), Constant.KEY, ""));
	}
}
