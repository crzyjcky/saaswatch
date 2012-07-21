package com.saaswatch.agent.server.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "memory")
public class MemoryDTO {
	
	private long committed;
	private long init;
	private long max;
	private long used;
	
	public long getCommitted() {
		return committed;
	}
	public void setCommitted(long committed) {
		this.committed = committed;
	}
	public long getInit() {
		return init;
	}
	public void setInit(long init) {
		this.init = init;
	}
	public long getMax() {
		return max;
	}
	public void setMax(long max) {
		this.max = max;
	}
	public long getUsed() {
		return used;
	}
	public void setUsed(long used) {
		this.used = used;
	}
	
	
}
