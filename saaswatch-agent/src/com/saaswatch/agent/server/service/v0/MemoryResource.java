package com.saaswatch.agent.server.service.v0;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.saaswatch.agent.server.controller.MemoryResourceController;
import com.saaswatch.agent.server.dto.MemoryDTO;

@Path("/memoryResource/v0/")
public class MemoryResource implements IMemoryResource {
	
	private MemoryResourceController controller;
	
	public MemoryResource() {
		controller = new MemoryResourceController();
	}
	
	@GET
	@Path("/nonHeapMemory")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getNonHeapMemory() {
		
		MemoryDTO memoryDTO = controller.getNonHeapMemoryDTO();
		
		return Response.ok(memoryDTO).build();
	}
	
	@GET
	@Path("/heapMemory")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getHeapMemory() {
		
		MemoryDTO memoryDTO = controller.getHeapMemoryDTO();
		
		return Response.ok(memoryDTO).build();
	}

}