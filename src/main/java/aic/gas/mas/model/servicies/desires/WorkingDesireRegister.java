package aic.gas.mas.model.servicies.desires;

import aic.gas.mas.model.agents.Agent;
import aic.gas.mas.model.planing.SharedDesire;
import aic.gas.mas.model.planing.SharedDesireForAgents;
import aic.gas.mas.model.planing.SharedDesireInRegister;
import aic.gas.mas.model.servicies.WorkingRegister;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Concrete implementation of DesireRegister. This class is intended as working register - register
 * keeps up to date information about desires and is intended for mediator use only.
 */
public class WorkingDesireRegister extends DesireRegister implements
    WorkingRegister<ReadOnlyDesireRegister> {

  public WorkingDesireRegister() {
    super(new HashMap<>());
  }

  public ReadOnlyDesireRegister makeSnapshot() {
    Map<Agent, Map<SharedDesire, SharedDesireInRegister>> copy = new HashMap<>();
    dataByOriginator.forEach((agent, sharedDesireSharedDesireInRegisterMap) -> {
      Map<SharedDesire, SharedDesireInRegister> content = new HashMap<>();
      sharedDesireSharedDesireInRegisterMap.forEach((sharedDesire, sharedDesireInRegister) -> {
        SharedDesireInRegister desireCopy = sharedDesireInRegister.getCopy();
        content.put(desireCopy, desireCopy);
      });
      copy.put(agent, content);
    });
    return new ReadOnlyDesireRegister(copy);
  }

  /**
   * Try to add desire to register. Returns true if desire is registered in register
   */
  public boolean addedDesire(SharedDesireInRegister desireForOthers) {
    Map<SharedDesire, SharedDesireInRegister> desiresByAgent = dataByOriginator
        .computeIfAbsent(desireForOthers.getOriginatedFromAgent(), agent -> new HashMap<>());
    if (!desiresByAgent.containsKey(desireForOthers)) {
      desiresByAgent.put(desireForOthers, desireForOthers);
    }
    return true;
  }

  /**
   * Remove agent from register
   */
  public boolean removeAgent(Agent agentToRemove) {
    dataByOriginator.remove(agentToRemove);
    return true;
  }

  /**
   * Removes desire from register and returns status of this operation
   */
  public boolean removedDesire(SharedDesire desireForOthers) {
    if (dataByOriginator.containsKey(desireForOthers.getOriginatedFromAgent())) {
      Map<SharedDesire, SharedDesireInRegister> desiresByAgent = dataByOriginator
          .get(desireForOthers.getOriginatedFromAgent());
      desiresByAgent.remove(desireForOthers);
      if (desiresByAgent.isEmpty()) {
        dataByOriginator.remove(desireForOthers.getOriginatedFromAgent());
      }
    }
    return true;
  }

  /**
   * Tries to commit agent to desire. If it is successful returns DesireFromAnotherAgent
   */
  public Optional<SharedDesireForAgents> commitToDesire(Agent agentWhoWantsToCommitTo,
      SharedDesireForAgents desireForOthersHeWantsToCommitTo) {
    if (dataByOriginator.containsKey(desireForOthersHeWantsToCommitTo.getOriginatedFromAgent())) {
      SharedDesireInRegister desire = dataByOriginator
          .get(desireForOthersHeWantsToCommitTo.getOriginatedFromAgent())
          .getOrDefault(desireForOthersHeWantsToCommitTo, null);
      if (desire != null) {

        //try to commit agent and return copy of current instance
        desire.commitToDesire(agentWhoWantsToCommitTo);
        return Optional.of(desire.getCopyOfSharedDesireForAgents());
      }
    }
    return Optional.empty();
  }

  /**
   * Tries to remove commitment of agent to desire.
   */
  public boolean removeCommitmentToDesire(Agent agentWhoWantsToRemoveCommitment,
      SharedDesireForAgents desireHeWantsToRemoveCommitmentTo) {
    if (dataByOriginator.containsKey(desireHeWantsToRemoveCommitmentTo.getOriginatedFromAgent())) {
      SharedDesireInRegister desire = dataByOriginator
          .get(desireHeWantsToRemoveCommitmentTo.getOriginatedFromAgent())
          .getOrDefault(desireHeWantsToRemoveCommitmentTo, null);
      if (desire != null) {
        return desire.removeCommitment(agentWhoWantsToRemoveCommitment);
      }
    }
    return true;
  }

}
