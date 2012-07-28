package com.saaswatch.agent.util;

import java.util.ArrayList;
import java.util.List;

import com.saaswatch.agent.communicator.datapacketformat.DataPacket;
import com.saaswatch.agent.server.dto.MemoryDTO;

public class AgentTxBuffer implements IAgentTxBuffer {

	public static IAgentTxBuffer instance;
	
	private AgentTxBuffer() {
		
	}
	
	public static synchronized IAgentTxBuffer getInstance() {
		
		if (instance == null) {
			instance = new AgentTxBuffer();
		}
		
		return instance;
	}
	
	@Override
	public List<DataPacket> getAndClear() {
		
		// test data
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
		
		return dataPackets;
	}
	
	@Override
	public void add(DataPacket dataPacket) {
		
	}
}
