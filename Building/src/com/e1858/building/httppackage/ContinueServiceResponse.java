package com.e1858.building.httppackage;

import com.e1858.building.bean.PacketResp;
import com.e1858.building.net.HttpDefine;

/**
 * 功能：
 * 日期: 2015-2-4 上午11:22:22
 * 作者: wangzhongli
 */
@SuppressWarnings("serial")
public class ContinueServiceResponse extends PacketResp {

	public ContinueServiceResponse() {
		command = HttpDefine.CONTINUESERVICE_RESP;
	}

}
