package com.saaswatch.agent.probe;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.net.MalformedURLException;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class MemoryProbe {

	private static final int EXIT_ERROR = 1;
	private static final int EXIT_NORMAL = 0;

	private static final String JMX_SERVICE_URL = "service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi";

	JMXServiceURL jmxServiceURL;
	JMXConnector jmxConnector;
	MBeanServerConnection mBeanServerConnection;

	MemoryMXBean mxBean;
	
	private static volatile boolean isStop;

	private MemoryProbe() {
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
					ManagementFactory.MEMORY_MXBEAN_NAME, MemoryMXBean.class);
		} catch (IOException e) {
			processError(e);
		}
	}

	public static MemoryProbe create() {
		MemoryProbe memoryProbe = new MemoryProbe();
		memoryProbe.initialize();
		return memoryProbe;
	}

	private void initialize() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!isStop) {
					MemoryUsage heapMemoryUsage = mxBean.getHeapMemoryUsage();
					MemoryUsage nonHeapMemoryUsage = mxBean.getNonHeapMemoryUsage();

					long nonHeapCommited = nonHeapMemoryUsage.getCommitted();
					long nonHeapInit = nonHeapMemoryUsage.getInit();
					long nonHeapMax = nonHeapMemoryUsage.getMax();
					long nonHeapUsed = nonHeapMemoryUsage.getUsed();
					
					long heapCommited = heapMemoryUsage.getCommitted();
					long heapInit = heapMemoryUsage.getInit();
					long heapMax = heapMemoryUsage.getMax();
					long heapUsed = heapMemoryUsage.getUsed();

					System.out.println("nonHeapCommited:" + nonHeapCommited + " nonHeapInit:" + nonHeapInit
							+ " nonHeapMax:" + nonHeapMax + " nonHeapUsed:" + nonHeapUsed + " heapCommited:" + heapCommited + " heapInit:" + heapInit
							+ " heapMax:" + heapMax + " heapUsed:" + heapUsed);
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
