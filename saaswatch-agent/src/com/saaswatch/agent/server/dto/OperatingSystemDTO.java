package com.saaswatch.agent.server.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "operatingSystem")
public class OperatingSystemDTO implements Serializable {

	private String arch;
	private int availableProcessors;
	private String name;

	// on windows machine, mxBean.getSystemLoadAverage() always return -1.0
	private double systemLoadAverage;
	private String version;
	
	public String getArch() {
		return arch;
	}
	public void setArch(String arch) {
		this.arch = arch;
	}
	public int getAvailableProcessors() {
		return availableProcessors;
	}
	public void setAvailableProcessors(int availableProcessors) {
		this.availableProcessors = availableProcessors;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getSystemLoadAverage() {
		return systemLoadAverage;
	}
	public void setSystemLoadAverage(double systemLoadAverage) {
		this.systemLoadAverage = systemLoadAverage;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
}
