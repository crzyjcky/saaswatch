package com.saaswatch.agent.communicator;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

public class AgentTxMBeanServer {

	private static final String OBJECT_NAME = "AgentTx:name=dataPackets";
	
    private MBeanServer mbs;
    private AgentTx agentTxBean;
    
    public AgentTxMBeanServer() {

    	mbs = ManagementFactory.getPlatformMBeanServer();
    	
    	agentTxBean = new AgentTx();
    	
    	try {
    		
    		ObjectName objectName = new ObjectName(OBJECT_NAME);
			mbs.registerMBean(agentTxBean, objectName);
		} catch (MalformedObjectNameException e) {
			
			e.printStackTrace();
		} catch (NullPointerException e) {

			e.printStackTrace();
		} catch (InstanceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (MBeanRegistrationException e) {

			e.printStackTrace();
		} catch (NotCompliantMBeanException e) {

			e.printStackTrace();
		}
    }
    
    public void sendDataPackets() {
    	
    	agentTxBean.sendDataPackets();
    }
}
