package com.saaswatch.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.saaswatch.domain.Agent;
import com.saaswatch.dto.AgentsDTO;
import com.sun.jdmk.discovery.DiscoveryClient;
import com.sun.jdmk.discovery.DiscoveryResponse;

public class AgentResourceController {

	private DiscoveryClient discoveryClient = new DiscoveryClient();
	
	
	public AgentResourceController() {
		
		try {
			discoveryClient.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public AgentsDTO getAgentsDTO() {
		List<Agent> agents = new ArrayList<Agent>();
		
		List<DiscoveryResponse> discoveryResponses = discoveryClient.findCommunicators();
		
		int len = discoveryResponses.size();

		for (int i = 0; i < len; i++) {
			DiscoveryResponse discoveryResponse = discoveryResponses.get(i);

			Agent agent = new Agent();
			agent.setAgentId(new String(discoveryResponse.getUserData()));
			agent.setHost(discoveryResponse.getHost());
			agent.setMBeanServerId(discoveryResponse.getMBeanServerId());
			agents.add(agent);
		}
		
		AgentsDTO agentsDTO = new AgentsDTO();
		agentsDTO.setAgents(agents);
		
		return agentsDTO;
	}
}
