package com.e1858.building.bean;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class PhotoType implements Serializable {
	public int			type;	//1.卖家签单拍照2.商品拍照3.货物包装拍照4.订单单号拍照5.物流订单拍照
	public List<String>	urls;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

}
