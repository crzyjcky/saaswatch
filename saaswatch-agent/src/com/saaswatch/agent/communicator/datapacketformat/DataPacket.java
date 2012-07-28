package com.saaswatch.agent.communicator.datapacketformat;

import java.io.Serializable;

public class DataPacket implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1537267801714169805L;
	
	private String type;
	private Serializable payload;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Serializable getPayload() {
		return payload;
	}
	public void setPayload(Serializable payload) {
		this.payload = payload;
	}

}
