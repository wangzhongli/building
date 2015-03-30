package com.e1858.building.httppackage;

import java.util.List;

import com.e1858.building.bean.LogisticInfo;
import com.e1858.building.bean.PacketResp;
import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class GetLogisticInfosResponse extends PacketResp {
	private List<LogisticInfo>	logisticInfos;

	public GetLogisticInfosResponse() {
		command = HttpDefine.GETLOGISTICINFOS_RESP;
	}

	public List<LogisticInfo> getLogisticInfos() {
		return logisticInfos;
	}

	public void setLogisticInfos(List<LogisticInfo> logisticInfos) {
		this.logisticInfos = logisticInfos;
	}
}
