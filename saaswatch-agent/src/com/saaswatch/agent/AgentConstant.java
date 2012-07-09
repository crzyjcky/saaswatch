package com.saaswatch.agent;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public enum AgentConstant {
	ID;
	
	private final int EXIT_ERROR = 1;
	private final int EXIT_NORMAL = 0;
	
	private String value;
	
	private AgentConstant() {
	}
	
	public String toString() {
		String value = null;
		
		switch (this) {
			case ID:
				value = getId();
				break;
		}
		
		return value;
	}
	
	// this is getting MAC address. If we want to get Amazon instance Id,
	// we have to change this method.
	private String getId() {
		
		if (value != null) {
			return value;
		}
		
		InetAddress localHost = null;
		
		try {
			localHost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			processError(e);
		}
		
		String ip = localHost.getHostAddress();
		
		NetworkInterface network = null;
		try {
			network = NetworkInterface.getByInetAddress(localHost);
		} catch (SocketException e) {
			processError(e);
		}
		
		byte[] mac = null;
		try {
			mac = network.getHardwareAddress();
		} catch (SocketException e) {
			processError(e);
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
		}

		return sb.toString();
	}
	
	private void processError(Exception e) {
		e.printStackTrace();
		System.exit(EXIT_ERROR);
	}
}
