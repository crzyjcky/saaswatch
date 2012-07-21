package com.saaswatch.agent.server.service;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import com.saaswatch.agent.server.service.v0.MemoryResource;

public class SaaSWatchAgentApplication extends Application{
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();

	public SaaSWatchAgentApplication() {
		
		singletons.add(new MemoryResource());
		singletons.add(new JacksonJaxbJsonProvider());
	}

	@Override
	public Set<Class<?>> getClasses() {
		
		return empty;
	}

	@Override
	public Set<Object> getSingletons() {
		
		return singletons;
	}
}
