package com.e1858.building.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CancelCause implements Serializable {
	public String	ID;	//ID
	public String	cause;	//原因

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

}
