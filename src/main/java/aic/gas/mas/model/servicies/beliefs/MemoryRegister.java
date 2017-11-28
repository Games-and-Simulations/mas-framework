package aic.gas.mas.model.servicies.beliefs;

import aic.gas.mas.model.agents.Agent;
import aic.gas.mas.model.knowledge.ReadOnlyMemory;
import aic.gas.mas.model.servicies.Register;
import java.util.Map;

/**
 * MemoryRegister contains knowledge (internal beliefs) received from agents
 */
abstract class MemoryRegister extends Register<ReadOnlyMemory> {

  MemoryRegister(Map<Agent, ReadOnlyMemory> dataByOriginator) {
    super(dataByOriginator);
  }
}
