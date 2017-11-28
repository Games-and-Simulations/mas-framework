package aic.gas.mas.model.servicies.beliefs;

import aic.gas.mas.model.agents.Agent;
import aic.gas.mas.model.knowledge.ReadOnlyMemory;
import aic.gas.mas.model.servicies.WorkingRegister;
import aic.gas.mas.service.MASFacade;
import java.util.HashMap;
import java.util.Map;

/**
 * Concrete implementation of MemoryRegister. This class is intended as working register - register
 * keeps up to date information about agents' internal memories and is intended for mediator use
 * only.
 */
public class WorkingMemoryRegister extends MemoryRegister implements
    WorkingRegister<ReadOnlyMemoryRegister> {

  private final Map<Agent, Integer> decayMap = new HashMap<>();

  public WorkingMemoryRegister() {
    super(new HashMap<>());
  }

  @Override
  public ReadOnlyMemoryRegister makeSnapshot() {
    forget();
    Map<Agent, ReadOnlyMemory> copy = new HashMap<>();
    dataByOriginator.forEach(copy::put);
    return new ReadOnlyMemoryRegister(copy);
  }

  /**
   * Method erases no longer relevant information
   */
  private void forget() {
    decayMap.forEach((v, integer) -> decayMap.put(v, integer + 1));
    decayMap.keySet().removeIf(
        v -> decayMap.get(v) >= MASFacade.howManyCyclesStayAgentsMemoryInRegisterWithoutUpdate);
  }

  /**
   * Add memory of agent in register
   */
  public boolean addAgentsMemory(ReadOnlyMemory readOnlyMemory, Agent owner) {
    dataByOriginator.put(owner, readOnlyMemory);
    decayMap.put(owner, 1);
    return true;
  }

  /**
   * Remove memory of agent in register
   */
  public boolean removeAgentsMemory(Agent owner) {
    dataByOriginator.remove(owner);
    decayMap.remove(owner);
    return true;
  }
}
