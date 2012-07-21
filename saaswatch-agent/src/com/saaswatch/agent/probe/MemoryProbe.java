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

import com.saaswatch.agent.model.MemoryModel;

public class MemoryProbe {

	private static final int EXIT_ERROR = 1;
	private static final int EXIT_NORMAL = 0;

	private final int DURATION = 1000; // ms
	
	private static final String JMX_SERVICE_URL = "service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi";

	JMXServiceURL jmxServiceURL;
	JMXConnector jmxConnector;
	MBeanServerConnection mBeanServerConnection;

	MemoryMXBean mxBean;
	
	MemoryModel memoryModel = MemoryModel.getInstance();
	
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

					long nonHeapCommitted = nonHeapMemoryUsage.getCommitted();
					long nonHeapInit = nonHeapMemoryUsage.getInit();
					long nonHeapMax = nonHeapMemoryUsage.getMax();
					long nonHeapUsed = nonHeapMemoryUsage.getUsed();
					
					long heapCommitted = heapMemoryUsage.getCommitted();
					long heapInit = heapMemoryUsage.getInit();
					long heapMax = heapMemoryUsage.getMax();
					long heapUsed = heapMemoryUsage.getUsed();

					
					System.out.println("nonHeapCommited:" + nonHeapCommitted + " nonHeapInit:" + nonHeapInit
							+ " nonHeapMax:" + nonHeapMax + " nonHeapUsed:" + nonHeapUsed + " heapCommited:" + heapCommitted + " heapInit:" + heapInit
							+ " heapMax:" + heapMax + " heapUsed:" + heapUsed);
					
					
					memoryModel.setNonHeapCommitted(nonHeapCommitted);
					memoryModel.setNonHeapInit(nonHeapInit);
					memoryModel.setNonHeapMax(nonHeapMax);
					memoryModel.setNonHeapUsed(nonHeapUsed);
					
					memoryModel.setHeapCommitted(heapCommitted);
					memoryModel.setHeapInit(heapInit);
					memoryModel.setHeapMax(heapMax);
					memoryModel.setHeapUsed(heapUsed);
					
					
					try {
						Thread.sleep(DURATION);
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
