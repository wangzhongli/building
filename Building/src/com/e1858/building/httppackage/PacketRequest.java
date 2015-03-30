package com.e1858.building.httppackage;

import com.e1858.building.bean.Packet;
import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public abstract class PacketRequest extends Packet {
	@Expose
	private String	key	= "";

	public PacketRequest(int command) {
		this.command = command;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
