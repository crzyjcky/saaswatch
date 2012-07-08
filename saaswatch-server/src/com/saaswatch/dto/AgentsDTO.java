package com.saaswatch.dto;

import java.util.ArrayList;
import java.util.List;

import com.saaswatch.domain.Agent;

public class AgentsDTO {
	private List<Agent> agents = new ArrayList<Agent>();

	public List<Agent> getAgents() {
		return agents;
	}

	public void setAgents(List<Agent> agents) {
		this.agents = agents;
	}
	
}
