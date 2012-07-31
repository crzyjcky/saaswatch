package com.saaswatch.agent.server.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "memory")
public class MemoryDTO implements Serializable {

	private long heapCommitted;
	private long heapInit;
	private long heapMax;
	private long heapUsed;

	private long nonHeapCommitted;
	private long nonHeapInit;
	private long nonHeapMax;
	private long nonHeapUsed;
	
	public long getHeapCommitted() {
		return heapCommitted;
	}
	public void setHeapCommitted(long heapCommitted) {
		this.heapCommitted = heapCommitted;
	}
	public long getHeapInit() {
		return heapInit;
	}
	public void setHeapInit(long heapInit) {
		this.heapInit = heapInit;
	}
	public long getHeapMax() {
		return heapMax;
	}
	public void setHeapMax(long heapMax) {
		this.heapMax = heapMax;
	}
	public long getHeapUsed() {
		return heapUsed;
	}
	public void setHeapUsed(long heapUsed) {
		this.heapUsed = heapUsed;
	}
	public long getNonHeapCommitted() {
		return nonHeapCommitted;
	}
	public void setNonHeapCommitted(long nonHeapCommitted) {
		this.nonHeapCommitted = nonHeapCommitted;
	}
	public long getNonHeapInit() {
		return nonHeapInit;
	}
	public void setNonHeapInit(long nonHeapInit) {
		this.nonHeapInit = nonHeapInit;
	}
	public long getNonHeapMax() {
		return nonHeapMax;
	}
	public void setNonHeapMax(long nonHeapMax) {
		this.nonHeapMax = nonHeapMax;
	}
	public long getNonHeapUsed() {
		return nonHeapUsed;
	}
	public void setNonHeapUsed(long nonHeapUsed) {
		this.nonHeapUsed = nonHeapUsed;
	}
}
