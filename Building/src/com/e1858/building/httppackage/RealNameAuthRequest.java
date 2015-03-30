package com.e1858.building.httppackage;

import java.util.List;

import com.e1858.building.net.HttpDefine;

/**
 * 日期: 2015年1月8日 下午2:55:46
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
@SuppressWarnings("serial")
public class RealNameAuthRequest extends AutoFillPacketRequest {
	public String		realName;		//	string	真实姓名	true
	public String		IDNumber;		//	string	身份证号码	true
	public List<String>	pictureUrls;	//	String数组	图片数组	true

	/**
	 * @param command
	 */
	public RealNameAuthRequest() {
		super(HttpDefine.REALNAMEAUTH);
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIDNumber() {
		return IDNumber;
	}

	public void setIDNumber(String iDNumber) {
		IDNumber = iDNumber;
	}

	public List<String> getPictureUrls() {
		return pictureUrls;
	}

	public void setPictureUrls(List<String> pictureUrls) {
		this.pictureUrls = pictureUrls;
	}

}
