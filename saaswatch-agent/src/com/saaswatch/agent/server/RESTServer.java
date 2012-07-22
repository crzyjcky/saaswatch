package com.saaswatch.agent.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

public class RESTServer implements Runnable {
	
	private static final int PORT = 9091;
	
	Server server;
	
	
	public static RESTServer create() {
		RESTServer server = new RESTServer();
		Thread thread = new Thread(server);
		thread.start();
		return server;
	}
	
	
	private RESTServer() {
	}
	
	public void run() {
		server = new Server(PORT);
		
		ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		servletContextHandler.setContextPath("/");
		ServletHolder servletHolder = new ServletHolder(new HttpServletDispatcher());
		servletHolder.setInitParameter("javax.ws.rs.Application", "com.saaswatch.agent.server.service.SaaSWatchAgentApplication");
		
		servletContextHandler.addServlet(servletHolder, "/*");
		server.setHandler(servletContextHandler);
		
		try {
			server.start();
			server.join();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	
	public void stop() {
		
	}
}
