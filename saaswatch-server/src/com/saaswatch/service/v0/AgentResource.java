package com.saaswatch.service.v0;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.saaswatch.controller.AgentResourceController;
import com.saaswatch.domain.Agent;
import com.saaswatch.dto.AgentsDTO;

@Path("/agentResource/v0/")
public class AgentResource implements IAgentResource {

	private AgentResourceController controller;
	
	public AgentResource() {
		controller = new AgentResourceController();
	}
	
	@Override
	@GET
	@Path("/agents")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getAgents() {
		
		AgentsDTO agentsDTO = controller.getAgentsDTO();
		
		return Response.ok(agentsDTO).build();
	}
}
