package com.e1858.building.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Notice implements Serializable {
	public String	ID;		//通知ID
	public String	title;		//通知标题
	public String	content;	//通知内容
	public String	sendTime;	//通知发送时间
	public String	readTime;	//通知读取时间
	public int		type;		//1.公有通知2.私有通知

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

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getReadTime() {
		return readTime;
	}

	public void setReadTime(String readTime) {
		this.readTime = readTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
