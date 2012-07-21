package com.saaswatch.agent;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Scanner;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXConnectorServerMBean;
import javax.management.remote.JMXServiceURL;

import com.saaswatch.agent.probe.MemoryProbe;
import com.saaswatch.agent.probe.OperatingSystemProbe;
import com.saaswatch.agent.server.RESTServer;
import com.sun.jdmk.discovery.DiscoveryResponder;
import com.sun.jdmk.discovery.DiscoveryResponderMBean;

public class Agent {
	//private static final String AGENT_ID = "abcdf@5673";

	private static final int EXIT_ERROR = 1;
	private static final int EXIT_NORMAL = 0;

	private static final String DOMAIN = "SaaSWatchAgent";
	private static final String CONNECTOR_PROTOCOL = "jmxmp";

	private MBeanServer mBeanServer;
	private DiscoveryResponderMBean discoveryResponderMBean;
	private JMXConnectorServerMBean jmxConnectorServerMBean;
	
	MemoryProbe memoryProbe;
	OperatingSystemProbe operatingSystemProbe;
	
	RESTServer restServer;

	public Agent() throws MalformedURLException, IOException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException, MalformedObjectNameException, NullPointerException {
		
		// initialize
		mBeanServer = MBeanServerFactory.createMBeanServer(DOMAIN);
		jmxConnectorServerMBean = JMXConnectorServerFactory
				.newJMXConnectorServer(new JMXServiceURL(CONNECTOR_PROTOCOL,
						null, 0), null, mBeanServer);
		discoveryResponderMBean = new DiscoveryResponder();
		discoveryResponderMBean.setUserData(AgentConstant.ID.toString().getBytes());
		
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());

		mBeanServer.registerMBean(discoveryResponderMBean, new ObjectName(DOMAIN,
				"name", "discoveryResponder"));
		mBeanServer.registerMBean(jmxConnectorServerMBean, new ObjectName(DOMAIN,
				"name", "jmxConnectorServer"));		
		
		
		discoveryResponderMBean.start();
		jmxConnectorServerMBean.start();
		
		
		memoryProbe = MemoryProbe.create();
		operatingSystemProbe = OperatingSystemProbe.create();
		
		RESTServer restServer = new RESTServer();
		restServer.start();
		
	}

	// helper
	private static void pause() {
		System.out.println("Press ENTER to shutdown");
		Scanner scanner = new Scanner(System.in);
		scanner.useDelimiter("\n");
		scanner.next();
	}

	private void processError(Exception exception) {
		exception.printStackTrace();
		System.exit(EXIT_ERROR);
	}

	public void shutdown() {
		System.out.println("Shutting down");
		discoveryResponderMBean.stop();

		restServer.stop();
		
		try {
			jmxConnectorServerMBean.stop();
		} catch (IOException e) {
			processError(e);
		}
		
		memoryProbe.stop();
	}

	private class ShutdownHook extends Thread {
		public void run() {
			System.out.println("Shutting down hook");
			discoveryResponderMBean.stop();
			
			restServer.stop();
			
			try {
				jmxConnectorServerMBean.stop();
			} catch (IOException e) {
				processError(e);
			}
			
			memoryProbe.stop();
		}
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws NullPointerException 
	 * @throws MalformedURLException 
	 * @throws MalformedObjectNameException 
	 * @throws NotCompliantMBeanException 
	 * @throws MBeanRegistrationException 
	 * @throws InstanceAlreadyExistsException 
	 */
	public static void main(String[] args) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException, MalformedObjectNameException, MalformedURLException, NullPointerException, IOException {
		System.out.println("start agent " + AgentConstant.ID);
		
		Agent agent = new Agent();
		pause();
		agent.shutdown();
	}

}
