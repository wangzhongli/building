package com.e1858.building.httppackage;

import java.util.List;

import com.e1858.building.bean.PhotoType;
import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class ServiceDoneUpPicRequest extends AutoFillPacketRequest {

	private String			orderID	= "";
	private int				flag;			//标记1.提货完成上传照片	2.订单完成上传照片

	private List<PhotoType>	photoTypes;

	public ServiceDoneUpPicRequest() {
		super(HttpDefine.SERVICEDONEUPPIC);
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public List<PhotoType> getPhotoTypes() {
		return photoTypes;
	}

	public void setPhotoTypes(List<PhotoType> photoTypes) {
		this.photoTypes = photoTypes;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
