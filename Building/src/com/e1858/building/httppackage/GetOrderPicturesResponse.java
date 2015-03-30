package com.e1858.building.httppackage;

import java.util.List;

import com.e1858.building.bean.PacketResp;
import com.e1858.building.bean.PictureInfo;
import com.e1858.building.net.HttpDefine;

public class GetOrderPicturesResponse extends PacketResp {
	private List<PictureInfo>	pictureInfos;

	public GetOrderPicturesResponse() {
		command = HttpDefine.GETORDERPICTURE_RESP;
	}

	public List<PictureInfo> getPictureInfos() {
		return pictureInfos;
	}

	public void setPictureInfos(List<PictureInfo> pictureInfos) {
		this.pictureInfos = pictureInfos;
	}

}
