package aic.gas.mas.model.agents;

import aic.gas.mas.model.ResponseReceiverInterface;
import aic.gas.mas.model.knowledge.Memory;
import aic.gas.mas.model.knowledge.WorkingMemory;
import aic.gas.mas.model.metadata.AgentType;
import aic.gas.mas.model.metadata.AgentTypeMakingObservations;
import aic.gas.mas.model.metadata.DesireKey;
import aic.gas.mas.model.metadata.DesireParameters;
import aic.gas.mas.model.planing.DesireForOthers;
import aic.gas.mas.model.planing.DesireFromAnotherAgent;
import aic.gas.mas.model.planing.OwnDesire;
import aic.gas.mas.model.planing.SharedDesireForAgents;
import aic.gas.mas.model.planing.command.ActCommand;
import aic.gas.mas.model.planing.command.ObservingCommand;
import aic.gas.mas.model.planing.command.ReasoningCommand;
import aic.gas.mas.model.planing.heap.HeapOfTrees;
import aic.gas.mas.model.planing.heap.visitors.CommandExecutor;
import aic.gas.mas.model.planing.heap.visitors.CommitmentDecider;
import aic.gas.mas.model.planing.heap.visitors.CommitmentRemovalDecider;
import aic.gas.mas.service.MASFacade;
import aic.gas.mas.service.implementation.BeliefMediator;
import aic.gas.mas.service.implementation.DesireMediator;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Template for agent. Main routine of agent runs in its own thread.
 */
@Slf4j
public abstract class Agent<E extends AgentType> implements AgentTypeBehaviourFactory,
    ResponseReceiverInterface<Boolean>, Runnable {

  @Getter
  protected final E agentType;
  protected final WorkingMemory beliefs;
  //lock
  final Object lockMonitor = new Object();
  @Getter
  private final int id;
  @Getter
  private final DesireMediator desireMediator;
  @Getter
  private final BeliefMediator beliefMediator;
  private final HeapOfTrees heapOfTrees = new HeapOfTrees(this);
  private final CommandExecutor commandExecutor = new CommandExecutor(heapOfTrees, this);
  private final CommitmentDecider commitmentDecider = new CommitmentDecider(heapOfTrees);
  private final CommitmentRemovalDecider commitmentRemovalDecider = new CommitmentRemovalDecider(
      heapOfTrees);
  private final Object isAliveLockMonitor = new Object();
  //to handle main routine of agent
  private boolean isAlive = true;
  private boolean removeAgentFromGlobalBeliefs = false;

  protected Agent(E agentType, MASFacade masFacade) {
    this.id = masFacade.getAgentsRegister().getFreeId();
    this.desireMediator = masFacade.getDesireMediator();
    this.beliefMediator = masFacade.getBeliefMediator();
    this.agentType = agentType;
    this.beliefs = new WorkingMemory(heapOfTrees, this.agentType, this.id,
        agentTypeID -> beliefMediator.getReadOnlyRegister()
            .getReadOnlyMemoriesForAgentType(agentTypeID),
        agentId -> beliefMediator.getReadOnlyRegister().getReadOnlyMemoryForAgent(agentId),
        () -> beliefMediator.getReadOnlyRegister().getReadOnlyMemories());
  }

  @Override
  public String toString() {
    return "Agent{" +
        "id=" + id +
        ", agentType=" + agentType.getName() +
        '}';
  }

  @Override
  public void run() {

    //run main routine in its own thread
    Worker worker = new Worker();
    worker.start();
    log.info("Agent has started.");
  }

  @Override
  public void receiveResponse(Boolean response) {
    //agent is removed from desire register
  }

  void doRoutine(Worker worker) {
    shareKnowledge(worker);
  }

  void shareKnowledge(Worker worker) {
    synchronized (lockMonitor) {
      if (beliefMediator.registerBelief(beliefs.cloneMemory(), this, worker)) {
        try {
          lockMonitor.wait();
        } catch (InterruptedException e) {
          log.error(worker.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
        }
      }
    }
  }

  private void removeAgent(boolean removeFromKnowledge) {
    desireMediator.removeAgentFromRegister(this, this);
    heapOfTrees.removeCommitmentToSharedDesires();
    if (removeFromKnowledge) {
      beliefMediator.removeAgent(this, this);
    }
  }

  /**
   * Execute reasoning command
   */
  public void executeCommand(ReasoningCommand command) {
    if (!MASFacade.REASONING_EXECUTOR.executeCommand(command, beliefs)) {
      log.error(this.getClass().getSimpleName() + ", " + agentType.getName()
          + " could not execute reasoning command");
    }
  }

  /**
   * Execute acting command
   */
  public abstract boolean sendCommandToExecute(ActCommand<?> command,
      ResponseReceiverInterface<Boolean> responseReceiver);

  /**
   * Get memory of agent
   */
  public Memory getBeliefs() {
    return beliefs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Agent)) {
      return false;
    }

    Agent agent = (Agent) o;

    return id == agent.id;
  }

  @Override
  public int hashCode() {
    return id;
  }

  @Override
  public OwnDesire.WithAbstractIntention formOwnDesireWithAbstractIntention(DesireKey desireKey) {
    return agentType.formOwnDesireWithAbstractIntention(desireKey, beliefs);
  }

  @Override
  public OwnDesire.WithAbstractIntention formOwnDesireWithAbstractIntention(DesireKey desireKey,
      DesireKey parentDesireKey,
      DesireParameters parentsDesireParameters) {
    return agentType.formOwnDesireWithAbstractIntention(parentDesireKey, desireKey, beliefs,
        parentsDesireParameters);
  }

  @Override
  public OwnDesire.Reasoning formOwnDesireWithReasoningCommand(DesireKey desireKey) {
    return agentType.formOwnReasoningDesire(desireKey, beliefs);
  }

  @Override
  public OwnDesire.Reasoning formOwnDesireWithReasoningCommand(DesireKey desireKey,
      DesireKey parentDesireKey,
      DesireParameters parentsDesireParameters) {
    return agentType
        .formOwnReasoningDesire(parentDesireKey, desireKey, beliefs, parentsDesireParameters);
  }

  @Override
  public OwnDesire.Acting formOwnDesireWithActingCommand(DesireKey desireKey) {
    return agentType.formOwnActingDesire(desireKey, beliefs);
  }

  @Override
  public OwnDesire.Acting formOwnDesireWithActingCommand(DesireKey desireKey,
      DesireKey parentDesireKey,
      DesireParameters parentsDesireParameters) {
    return agentType
        .formOwnActingDesire(parentDesireKey, desireKey, beliefs, parentsDesireParameters);
  }

  @Override
  public DesireForOthers formDesireForOthers(DesireKey desireKey) {
    return agentType.formDesireForOthers(desireKey, beliefs);
  }

  @Override
  public DesireForOthers formDesireForOthers(DesireKey desireKey, DesireKey parentDesireKey,
      DesireParameters parentsDesireParameters) {
    return agentType
        .formDesireForOthers(parentDesireKey, desireKey, beliefs, parentsDesireParameters);
  }

  @Override
  public Optional<DesireFromAnotherAgent.WithAbstractIntention> formDesireFromOtherAgentWithAbstractIntention(
      SharedDesireForAgents desireForAgents) {
    return agentType.formAnotherAgentsDesireWithAbstractIntention(desireForAgents, beliefs);
  }

  @Override
  public Optional<DesireFromAnotherAgent.WithIntentionWithPlan> formDesireFromOtherAgentWithIntentionWithPlan(
      SharedDesireForAgents desireForAgents) {
    return agentType.formAnotherAgentsDesireWithCommand(desireForAgents, beliefs);
  }

  /**
   * Method to be called when one want to terminate agent
   */
  public void terminateAgent(boolean removeAgentFromGlobalBeliefs) {
    synchronized (isAliveLockMonitor) {
      this.isAlive = false;
      this.removeAgentFromGlobalBeliefs = removeAgentFromGlobalBeliefs;
    }
  }

  /**
   * Extension of agent which also makes observations
   */
  public static abstract class MakingObservation<E> extends Agent<AgentTypeMakingObservations<E>> {

    protected MakingObservation(AgentTypeMakingObservations<E> agentType, MASFacade masFacade) {
      super(agentType, masFacade);
    }

    /**
     * Execute observing command
     */
    protected abstract boolean requestObservation(ObservingCommand<E> observingCommand,
        ResponseReceiverInterface<Boolean> responseReceiver);

    private void makeObservation(Worker worker) {
      synchronized (lockMonitor) {
        if (requestObservation(agentType.getObservingCommand(), worker)) {
          try {
            lockMonitor.wait();
          } catch (InterruptedException e) {
            log.error(worker.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
          }
        }
      }
    }

    @Override
    void doRoutine(Worker worker) {
      makeObservation(worker);
      shareKnowledge(worker);
    }

  }

  /**
   * Worker execute workflow of this agent.
   */
  class Worker extends Thread implements ResponseReceiverInterface<Boolean> {

    @Override
    public void run() {

      //init agent
      doRoutine(this);
      heapOfTrees.initTopLevelDesires(desireMediator.getReadOnlyRegister());

      while (true) {

        //check if agent is still alive
        synchronized (isAliveLockMonitor) {
          if (!isAlive) {
            break;
          }
        }
        //execute routine
        commitmentDecider.visitTree();
        commandExecutor.visitTree();
        commitmentRemovalDecider.visitTree();
        doRoutine(this);
        heapOfTrees.updateDesires(desireMediator.getReadOnlyRegister());
      }

      removeAgent(removeAgentFromGlobalBeliefs);
    }

    @Override
    public void receiveResponse(Boolean response) {

      //notify waiting method
      synchronized (lockMonitor) {
        if (!response) {
          log.error(this.getClass().getSimpleName() + " could not execute command");
        }
        lockMonitor.notifyAll();
      }
    }
  }

}
