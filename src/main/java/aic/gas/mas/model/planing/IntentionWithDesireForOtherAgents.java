package aic.gas.mas.model.planing;

import aic.gas.mas.model.agents.Agent;
import aic.gas.mas.model.metadata.DesireKey;
import lombok.Getter;

/**
 * Intention with desire for other agents - what this intention want them to achieve
 */
public class IntentionWithDesireForOtherAgents extends Intention<DesireForOthers> {

  @Getter
  private final SharedDesireForAgents sharedDesire;

  IntentionWithDesireForOtherAgents(DesireForOthers originalDesire, Agent agent,
      CommitmentDeciderInitializer removeCommitment,
      int limitOnNumberOfAgentsToCommit, DesireKey sharedDesireKey,
      ReactionOnChangeStrategy reactionOnChangeStrategy) {
    super(originalDesire, removeCommitment, reactionOnChangeStrategy);
    this.sharedDesire = new SharedDesireForAgents(sharedDesireKey, agent,
        limitOnNumberOfAgentsToCommit);
  }

  /**
   * Returns clone of desire as instance of desire to share
   */
  public SharedDesireInRegister makeDesireToShare() {
    return new SharedDesireInRegister(sharedDesire.desireParameters,
        sharedDesire.originatedFromAgent, sharedDesire.limitOnNumberOfAgentsToCommit);
  }

}
