package com.saaswatch.agent.util;

import java.io.Serializable;
import java.sql.Blob;
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
	private final String DATABASE = "jdbc:h2:~/saaswatch";//"jdbc:h2:~/jobqueue";//"jdbc:h2:~/buffer";
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
		String clearSQL = "TRUNCATE TABLE BUFFER";
		
		List<DataPacket> dataPackets = new ArrayList<DataPacket>();
		
		try {
			
			PreparedStatement queryPreparedStatement = conn.prepareStatement(querySQL);
			
			// start transaction
			conn.setAutoCommit(false);
			
			ResultSet rs = queryPreparedStatement.executeQuery();
			
			while (rs.next())
			{
		
				System.out.println(rs.getString(1));
				
				DataPacket dataPacket = new DataPacket();
				
				dataPacket.setType(rs.getString(COLUMN_DATA_TYPE));
				dataPacket.setPayload((Serializable) rs.getBlob(COLUMN_DATA));
				
				dataPackets.add(dataPacket);
			}
			
			PreparedStatement clearPreparedStatement = conn.prepareStatement(clearSQL);
			clearPreparedStatement.executeUpdate();
			
		} catch (SQLException e) {

			processError(e);
		} finally {
			
			try {
				
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				
				processError(e);
			} 
		}
		
		return dataPackets;
	}
	
	@Override
	public void add(DataPacket dataPacket) {
		
		String insertSQL = "INSERT INTO BUFFER(DATA_TYPE, DATA) VALUES(?, ?)";
		
		try {
			
			PreparedStatement insertPreparedStatement = conn.prepareStatement(insertSQL);
			
			insertPreparedStatement.setString(COLUMN_DATA_TYPE, dataPacket.getType());
			insertPreparedStatement.setBlob(COLUMN_DATA, (Blob) dataPacket.getPayload());
			
			insertPreparedStatement.executeUpdate();
		} catch (SQLException e) {

			processError(e);
		}
	}
	
	private void processError(Exception e) {
		e.printStackTrace();
		System.exit(EXIT_ERROR);
	}
}
