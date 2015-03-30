package com.e1858.building.httppackage;

import com.e1858.building.net.HttpDefine;

/**
 * 日期: 2015年1月6日 下午5:23:19
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
@SuppressWarnings("serial")
public class EditHeadPortraitRequest extends AutoFillPacketRequest {

	public String	headPortrait;

	/**
	 * @param command
	 */
	public EditHeadPortraitRequest() {
		super(HttpDefine.EDITHEADPORTRAIT);
	}

	public String getHeadPortrait() {
		return headPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}

}
