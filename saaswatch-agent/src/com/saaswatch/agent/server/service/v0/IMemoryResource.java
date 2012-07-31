package com.saaswatch.agent.server.service.v0;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/memoryResource/v0/")
public interface IMemoryResource {

	@GET
	@Path("/memory")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getMemory();

}