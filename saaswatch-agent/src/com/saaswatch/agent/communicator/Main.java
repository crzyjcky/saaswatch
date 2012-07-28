package com.saaswatch.agent.communicator;

import java.io.IOException;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AgentTxMBeanServer agentTxMBeanServer = new AgentTxMBeanServer();
		
		agentTxMBeanServer.sendDataPackets();
		
		waitForEnterPressed();
	}
	
    private static void waitForEnterPressed() {
    	
    	System.out.println("Press to continue...");
    	try {
    	    System.in.read();
    	} catch (IOException e) {
    	    // TODO Auto-generated catch block
    	    e.printStackTrace();
    	    System.exit(1);
    	}
    }

}
