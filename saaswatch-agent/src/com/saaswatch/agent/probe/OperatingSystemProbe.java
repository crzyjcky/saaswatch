package com.saaswatch.agent.probe;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.net.MalformedURLException;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class OperatingSystemProbe {

	private static final int EXIT_ERROR = 1;
	private static final int EXIT_NORMAL = 0;

	private static final String JMX_SERVICE_URL = "service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi";

	JMXServiceURL jmxServiceURL;
	JMXConnector jmxConnector;
	MBeanServerConnection mBeanServerConnection;

	OperatingSystemMXBean mxBean;
	
	private static volatile boolean isStop;

	private OperatingSystemProbe() {
		try {
			jmxServiceURL = new JMXServiceURL(JMX_SERVICE_URL);
		} catch (MalformedURLException e) {
			processError(e);
		}

		try {
			jmxConnector = JMXConnectorFactory.connect(jmxServiceURL);
		} catch (IOException e) {
			processError(e);
		}

		try {
			mBeanServerConnection = jmxConnector.getMBeanServerConnection();
		} catch (IOException e) {
			processError(e);
		}

		try {
			mxBean = ManagementFactory.newPlatformMXBeanProxy(
					mBeanServerConnection,
					ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
		} catch (IOException e) {
			processError(e);
		}
	}

	public static OperatingSystemProbe create() {
		OperatingSystemProbe memoryProbe = new OperatingSystemProbe();
		memoryProbe.initialize();
		return memoryProbe;
	}

	private void initialize() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!isStop) {
					
					String arch = mxBean.getArch();
					int availableProcessors = mxBean.getAvailableProcessors();
					String name = mxBean.getName();
					
					// on windows machine, mxBean.getSystemLoadAverage() always return -1.0
					double systemLoadAverage = mxBean.getSystemLoadAverage();
					String version = mxBean.getVersion();

					System.out.println("arch:" + arch + " availableProcessors:" + availableProcessors
							+ " name:" + name + " systemLoadAverage:" + systemLoadAverage + " version:" + version);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						processError(e);
					}
				}
			}	
		}).start();
	}

	private void processError(Exception exception) {
		exception.printStackTrace();
		System.exit(EXIT_ERROR);
	}
	
	public void stop() {
		isStop = true;
	}
}
