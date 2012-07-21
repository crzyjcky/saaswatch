package com.saaswatch.agent.server.controller;

import com.saaswatch.agent.model.MemoryModel;
import com.saaswatch.agent.server.dto.MemoryDTO;

public class MemoryResourceController {
	
	MemoryModel memoryModel = MemoryModel.getInstance();
	
	public MemoryDTO getNonHeapMemoryDTO() {
		
		MemoryDTO memoryDTO = new MemoryDTO();
		
		memoryDTO.setCommitted(memoryModel.getNonHeapCommitted());
		memoryDTO.setInit(memoryModel.getNonHeapInit());
		memoryDTO.setMax(memoryModel.getNonHeapMax());
		memoryDTO.setUsed(memoryModel.getNonHeapUsed());
		
		return memoryDTO;
	}
	
	public MemoryDTO getHeapMemoryDTO() {
		MemoryDTO memoryDTO = new MemoryDTO();
		
		memoryDTO.setCommitted(memoryModel.getHeapCommitted());
		memoryDTO.setInit(memoryModel.getHeapInit());
		memoryDTO.setMax(memoryModel.getHeapMax());
		memoryDTO.setUsed(memoryModel.getHeapUsed());
		
		return memoryDTO;
	}
}
