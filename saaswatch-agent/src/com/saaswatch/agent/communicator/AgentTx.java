package com.saaswatch.agent.communicator;


import java.util.List;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.saaswatch.agent.communicator.datapacketformat.DataPacket;
import com.saaswatch.agent.util.AgentTxBuffer;
import com.saaswatch.agent.util.IAgentTxBuffer;

public class AgentTx extends NotificationBroadcasterSupport implements AgentTxMBean, Job {
	
	public static final String NOTIFICATION_TYPE = "SAASWATCH_AGENT_DATA_PACKETS";
	
    private long sequence;
	private IAgentTxBuffer agentTxBuffer;
	
	public AgentTx() {
		
		agentTxBuffer =  AgentTxBuffer.getInstance();
	}
	
	public void sendDataPackets() {

		List<DataPacket> dataPackets = agentTxBuffer.getAndClear();
		
		Notification notification = new Notification(NOTIFICATION_TYPE, this, sequence++, System.currentTimeMillis());		
	
		notification.setUserData(dataPackets);
		sendNotification(notification);
	}
	
	@Override
	public void getDataPacketsAsync() {
		
		sendDataPackets();
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		sendDataPackets();
	}
}
