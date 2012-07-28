package com.saaswatch.agent.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class H2Queue {

	public static final String HEAP_MEMORY = "heapMemory";
	//public static final String MEMORY_TYPE = "memory";
	
	
	Connection conn;
	
	public H2Queue() {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Object> retrieveAndClearAll() {
		return null;
	}
	
	public void add(Object object) {
		
	}

}
