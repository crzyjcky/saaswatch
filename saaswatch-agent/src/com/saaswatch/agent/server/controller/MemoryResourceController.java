package com.saaswatch.agent.server.controller;

import com.saaswatch.agent.model.MemoryModel;
import com.saaswatch.agent.server.dto.MemoryDTO;

public class MemoryResourceController {
	
	MemoryModel memoryModel = MemoryModel.getInstance();
	
	public MemoryDTO getMemoryDTO() {
		MemoryDTO memoryDTO = new MemoryDTO();
		
		memoryDTO.setNonHeapCommitted(memoryModel.getNonHeapCommitted());
		memoryDTO.setNonHeapInit(memoryModel.getNonHeapInit());
		memoryDTO.setNonHeapMax(memoryModel.getNonHeapMax());
		memoryDTO.setNonHeapUsed(memoryModel.getNonHeapUsed());
		
		memoryDTO.setHeapCommitted(memoryModel.getHeapCommitted());
		memoryDTO.setHeapInit(memoryModel.getHeapInit());
		memoryDTO.setHeapMax(memoryModel.getHeapMax());
		memoryDTO.setHeapUsed(memoryModel.getHeapUsed());
		
		return memoryDTO;
	}
}
