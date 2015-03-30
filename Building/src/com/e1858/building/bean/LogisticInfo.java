package com.e1858.building.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LogisticInfo implements Serializable {
	private String	content;
	private String	time;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
