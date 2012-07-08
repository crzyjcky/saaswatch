package com.saaswatch.domain;

public class Agent {

	public static final int ONLINE = 0;
	public static final int OFFLINE = 1;
	public static final int CRASHED = 2;

	private String agentId;
	private String host;
	private String mBeanServerId;
	private int state;
	
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getMBeanServerId() {
		return mBeanServerId;
	}

	public void setMBeanServerId(String mBeanServerId) {
		this.mBeanServerId = mBeanServerId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
