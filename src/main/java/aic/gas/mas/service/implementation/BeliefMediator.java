package aic.gas.mas.service.implementation;

import aic.gas.mas.model.QueuedItemInterfaceWithResponse;
import aic.gas.mas.model.ResponseReceiverInterface;
import aic.gas.mas.model.agents.Agent;
import aic.gas.mas.model.knowledge.ReadOnlyMemory;
import aic.gas.mas.model.servicies.beliefs.IReadOnlyMemoryRegister;
import aic.gas.mas.model.servicies.beliefs.IWorkingMemoryRegister;
import aic.gas.mas.model.servicies.beliefs.MemoryRegister;
import aic.gas.mas.service.AMediatorTemplate;

/**
 * KnowledgeMediator instance enables agents to share knowledge of agents by keeping each agent's
 * internal knowledge. Class defines method to access queue.
 */
public class BeliefMediator extends
    AMediatorTemplate<IReadOnlyMemoryRegister, IWorkingMemoryRegister> {

  public BeliefMediator() {
    super(new MemoryRegister());
  }

  /**
   * Method to add item to queue with code to register knowledge
   */
  public boolean registerBelief(ReadOnlyMemory readOnlyMemory, Agent owner,
      ResponseReceiverInterface<Boolean> responseReceiver) {
    return addToQueue(new QueuedItemInterfaceWithResponse<Boolean>() {
      @Override
      public Boolean executeCode() {
        return workingRegister.addAgentsMemory(readOnlyMemory, owner);
      }

      @Override
      public ResponseReceiverInterface<Boolean> getReceiverOfResponse() {
        return responseReceiver;
      }
    });
  }

  /**
   * Method to add item to queue with code to remove agent
   */
  public boolean removeAgent(Agent owner, ResponseReceiverInterface<Boolean> responseReceiver) {
    return addToQueue(new QueuedItemInterfaceWithResponse<Boolean>() {
      @Override
      public Boolean executeCode() {
        return workingRegister.removeAgentsMemory(owner);
      }

      @Override
      public ResponseReceiverInterface<Boolean> getReceiverOfResponse() {
        return responseReceiver;
      }
    });
  }

}
