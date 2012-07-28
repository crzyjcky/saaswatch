package com.saaswatch.agent.util;

import java.util.List;

import com.saaswatch.agent.communicator.datapacketformat.DataPacket;

public interface IAgentTxBuffer {

	public List<DataPacket> getAndClear();

	public void add(DataPacket dataPacket);

}