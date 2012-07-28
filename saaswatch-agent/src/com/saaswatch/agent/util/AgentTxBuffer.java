package com.saaswatch.agent.util;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.saaswatch.agent.communicator.datapacketformat.DataPacket;

public class AgentTxBuffer implements IAgentTxBuffer {

	private final String DRIVER_CLASS_NAME = "org.h2.Driver";
	private final String DATABASE = "jdbc:h2:~/test";
	private final String USER_NAME = "sa";
	private final String PASSWORD = "";
	
	private final int COLUMN_DATA_TYPE = 1;
	private final int COLUMN_DATA = 2;
	
	private final int EXIT_ERROR = 1;
	
	public static IAgentTxBuffer instance;
	
	private Connection conn;
	
	private AgentTxBuffer() {
		
		try {
			
			Class.forName(DRIVER_CLASS_NAME);
		} catch (ClassNotFoundException e) {

			processError(e);
		}
		
		try {
			
			conn = DriverManager.getConnection(DATABASE, USER_NAME, PASSWORD);
		} catch (SQLException e) {
			
			processError(e);
		}
	}
	
	public static synchronized IAgentTxBuffer getInstance() {
		
		if (instance == null) {
			
			instance = new AgentTxBuffer();
		}
		
		return instance;
	}
	
	@Override
	public List<DataPacket> getAndClear() {
		
		String querySQL = "SELECT DATA_TYPE, DATA FROM BUFFER";
		PreparedStatement queryPreparedStatement;
		ResultSet rs;
		
		String clearSQL = "TRUNCATE TABBLE BUFFER";
		PreparedStatement clearPreparedStatement;
		
		List<DataPacket> dataPackets = new ArrayList<DataPacket>();
		
		try {
			
			queryPreparedStatement = conn.prepareStatement(querySQL);
			
			conn.setAutoCommit(false);
			
			rs = queryPreparedStatement.executeQuery();
			
			while (rs.next())
			{
				DataPacket dataPacket = new DataPacket();
				
				dataPacket.setType(rs.getString(COLUMN_DATA_TYPE));
				dataPacket.setPayload((Serializable) rs.getBlob(COLUMN_DATA));
				
				dataPackets.add(dataPacket);
			}
			
			clearPreparedStatement = conn.prepareStatement(clearSQL);
			clearPreparedStatement.execute();
			
		} catch (SQLException e) {

			processError(e);
		} finally {
			
			try {
				
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				
				processError(e);
			} 
		}
		
		// test data
		/*
		List<DataPacket> dataPackets = new ArrayList<DataPacket>();
		
		MemoryDTO memoryDTO = new MemoryDTO();
		memoryDTO.setCommitted(1);
		memoryDTO.setInit(2);
		memoryDTO.setMax(3);
		memoryDTO.setUsed(4);
		
		DataPacket dataPacket = new DataPacket();
		dataPacket.setType("MEMORY");
		dataPacket.setPayload(memoryDTO);
		
		dataPackets.add(dataPacket);
		// simulate 2 elements
		dataPackets.add(dataPacket);
		*/
		
		return dataPackets;
	}
	
	@Override
	public void add(DataPacket dataPacket) {
		
	}
	
	private void processError(Exception e) {
		e.printStackTrace();
		System.exit(EXIT_ERROR);
	}
}
