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

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.saaswatch.agent.communicator.AgentTx;
import com.saaswatch.agent.communicator.AgentTxMBeanServer;
import com.saaswatch.agent.probe.MemoryProbe;
import com.saaswatch.agent.probe.OperatingSystemProbe;
import com.saaswatch.agent.server.RESTServer;
import com.sun.jdmk.discovery.DiscoveryResponder;
import com.sun.jdmk.discovery.DiscoveryResponderMBean;

public class Agent {
	// private static final String AGENT_ID = "abcdf@5673";

	private final int EXIT_ERROR = 1;
	private final int EXIT_NORMAL = 0;

	private final String DOMAIN = "SaaSWatchAgent";
	private final String CONNECTOR_PROTOCOL = "jmxmp";

	private final int MEMORY_PROBE_DURATION = 2;
	private final int OPERATING_SYSTEM_PROBE_DURATION = 2;
	
	private final int AGENT_TX_DURATION = 5;

	private MBeanServer mBeanServer;
	private DiscoveryResponderMBean discoveryResponderMBean;
	private JMXConnectorServerMBean jmxConnectorServerMBean;

	MemoryProbe memoryProbe;
	OperatingSystemProbe operatingSystemProbe;

	RESTServer restServer;

	SchedulerFactory schedulerFactory = new StdSchedulerFactory();
	Scheduler scheduler;

	public Agent() throws MalformedURLException, IOException,
			InstanceAlreadyExistsException, MBeanRegistrationException,
			NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException {

		// initialize
		mBeanServer = MBeanServerFactory.createMBeanServer(DOMAIN);
		jmxConnectorServerMBean = JMXConnectorServerFactory
				.newJMXConnectorServer(new JMXServiceURL(CONNECTOR_PROTOCOL,
						null, 0), null, mBeanServer);
		discoveryResponderMBean = new DiscoveryResponder();
		discoveryResponderMBean.setUserData(AgentConstant.ID.toString()
				.getBytes());

		Runtime.getRuntime().addShutdownHook(new ShutdownHook());

		mBeanServer.registerMBean(discoveryResponderMBean, new ObjectName(
				DOMAIN, "name", "discoveryResponder"));
		mBeanServer.registerMBean(jmxConnectorServerMBean, new ObjectName(
				DOMAIN, "name", "jmxConnectorServer"));

		RESTServer restServer = RESTServer.create();
		// restServer.start();

		discoveryResponderMBean.start();
		jmxConnectorServerMBean.start();

		memoryProbe = new MemoryProbe();
		operatingSystemProbe = new OperatingSystemProbe();

		// agent-server communication
		AgentTxMBeanServer agentTxMBeanServer = new AgentTxMBeanServer();

		// scheduling
		JobDetail memoryJobDetail = JobBuilder.newJob(MemoryProbe.class)
				.withIdentity("memoryProbeJob").build();

		Trigger memoryTrigger = TriggerBuilder
				.newTrigger()
				.withIdentity("memoryProbeTrigger")
				.startNow()
				.withSchedule(
						SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInSeconds(MEMORY_PROBE_DURATION)
								.repeatForever()).build();

		JobDetail operatingSystemJobDetail = JobBuilder
				.newJob(OperatingSystemProbe.class)
				.withIdentity("operatingSystemProbeJob").build();

		Trigger operatingSystemTrigger = TriggerBuilder
				.newTrigger()
				.withIdentity("operatingSystemProbeTrigger")
				.startNow()
				.withSchedule(
						SimpleScheduleBuilder
								.simpleSchedule()
								.withIntervalInSeconds(
										OPERATING_SYSTEM_PROBE_DURATION)
								.repeatForever()).build();

		JobDetail agentTxJobDetail = JobBuilder
				.newJob(AgentTx.class)
				.withIdentity("agentTxJob").build();

		Trigger agentTxTrigger = TriggerBuilder
				.newTrigger()
				.withIdentity("agentTxTrigger")
				.startNow()
				.withSchedule(
						SimpleScheduleBuilder
								.simpleSchedule()
								.withIntervalInSeconds(
										AGENT_TX_DURATION)
								.repeatForever()).build();

		try {

			scheduler = schedulerFactory.getScheduler();
			scheduler.start();

			scheduler.scheduleJob(memoryJobDetail, memoryTrigger);
			scheduler.scheduleJob(operatingSystemJobDetail, operatingSystemTrigger);
			scheduler.scheduleJob(agentTxJobDetail, agentTxTrigger);
		} catch (SchedulerException e) {

			e.printStackTrace();
		}

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

		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {

			e.printStackTrace();
		}
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

			try {
				scheduler.shutdown();
			} catch (SchedulerException e) {

				e.printStackTrace();
			}
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
	public static void main(String[] args)
			throws InstanceAlreadyExistsException, MBeanRegistrationException,
			NotCompliantMBeanException, MalformedObjectNameException,
			MalformedURLException, NullPointerException, IOException {
		System.out.println("start agent " + AgentConstant.ID);

		Agent agent = new Agent();
		pause();
		agent.shutdown();
	}

}
