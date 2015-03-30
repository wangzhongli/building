package com.e1858.building.httppackage;

import java.util.List;

import com.e1858.building.bean.PacketResp;
import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class GetServiceWorkerTypesResponse extends PacketResp {
	List<String>	serviceWorkersTypes;

	public GetServiceWorkerTypesResponse() {
		command = HttpDefine.GETSERVICEWORKERTYPES_RESP;
	}

	public List<String> getServiceWorkersTypes() {
		return serviceWorkersTypes;
	}

	public void setServiceWorkersTypes(List<String> serviceWorkersTypes) {
		this.serviceWorkersTypes = serviceWorkersTypes;
	}

}
