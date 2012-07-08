package com.saaswatch.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import com.saaswatch.service.v0.AgentResource;

public class SaaSWatchServerApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();

	public SaaSWatchServerApplication() {
		
		singletons.add(new AgentResource());
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
