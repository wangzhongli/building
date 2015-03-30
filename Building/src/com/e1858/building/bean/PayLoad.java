package com.e1858.building.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class PayLoad implements Serializable {

	@Expose
	private String	ID		= "";
	@Expose
	private String	title	= "";
	@Expose
	private String	content	= "";
	@Expose
	private int		typeID	= -1;	//透传类型  1服务单派工通知    2预约提醒    3上门提醒

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getTypeID() {
		return typeID;
	}

	public void setTypeID(int typeID) {
		this.typeID = typeID;
	}

}
