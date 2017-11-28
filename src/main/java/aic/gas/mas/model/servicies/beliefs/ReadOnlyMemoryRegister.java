package aic.gas.mas.model.servicies.beliefs;

import aic.gas.mas.model.agents.Agent;
import aic.gas.mas.model.knowledge.ReadOnlyMemory;
import aic.gas.mas.model.metadata.AgentTypeID;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Concrete implementation of MemoryRegister. Instance of class is intended as read only as it is
 * shared among agents.
 */
public class ReadOnlyMemoryRegister extends MemoryRegister {

  private final Map<AgentTypeID, Map<Integer, ReadOnlyMemory>> beliefsInSystem;
  private final Map<Integer, ReadOnlyMemory> beliefsInSystemByAgents;

  ReadOnlyMemoryRegister(Map<Agent, ReadOnlyMemory> dataByOriginator) {
    super(dataByOriginator);
    this.beliefsInSystem = dataByOriginator.values().stream()
        .collect(Collectors.groupingBy(o -> o.getAgentType().getAgentTypeID(),
            Collectors.toMap(ReadOnlyMemory::getAgentId, Function.identity())));
    this.beliefsInSystemByAgents = dataByOriginator.values().stream()
        .collect(Collectors.toMap(ReadOnlyMemory::getAgentId, Function.identity()));
  }

  /**
   * Get ReadOnlyMemory by agent id
   */
  public Optional<ReadOnlyMemory> getReadOnlyMemoryForAgent(int agentId) {
    return Optional.ofNullable(beliefsInSystemByAgents.get(agentId));
  }

  /**
   * Get set of memories by agent type
   */
  public Stream<ReadOnlyMemory> getReadOnlyMemoriesForAgentType(AgentTypeID agentType) {
    return beliefsInSystem.getOrDefault(agentType, new HashMap<>()).values().stream();
  }

  /**
   * Get all beliefs in system
   */
  public Stream<ReadOnlyMemory> getReadOnlyMemories() {
    return beliefsInSystemByAgents.values().stream();
  }

}
