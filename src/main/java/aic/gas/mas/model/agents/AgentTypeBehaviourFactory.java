package aic.gas.mas.model.agents;

import aic.gas.mas.model.metadata.DesireKey;
import aic.gas.mas.model.metadata.DesireParameters;
import aic.gas.mas.model.planing.DesireForOthers;
import aic.gas.mas.model.planing.DesireFromAnotherAgent;
import aic.gas.mas.model.planing.OwnDesire;
import aic.gas.mas.model.planing.SharedDesireForAgents;
import java.util.Optional;

/**
 * Interface for each agent to form desires
 */
interface AgentTypeBehaviourFactory {

  /**
   * Forms OwnDesire WithAbstractIntention
   */
  OwnDesire.WithAbstractIntention formOwnDesireWithAbstractIntention(DesireKey desireKey);

  /**
   * Forms OwnDesire WithAbstractIntention with parent node
   */
  OwnDesire.WithAbstractIntention formOwnDesireWithAbstractIntention(DesireKey desireKey,
      DesireKey parentDesireKey,
      DesireParameters parentsDesireParameters);

  /**
   * Forms OwnDesire with reasoning command
   */
  OwnDesire.Reasoning formOwnDesireWithReasoningCommand(DesireKey desireKey);

  /**
   * Forms OwnDesire with reasoning command with parent node
   */
  OwnDesire.Reasoning formOwnDesireWithReasoningCommand(DesireKey desireKey,
      DesireKey parentDesireKey,
      DesireParameters parentsDesireParameters);

  /**
   * Forms OwnDesire with reasoning command
   */
  OwnDesire.Acting formOwnDesireWithActingCommand(DesireKey desireKey);

  /**
   * Forms OwnDesire with reasoning command with parent node
   */
  OwnDesire.Acting formOwnDesireWithActingCommand(DesireKey desireKey, DesireKey parentDesireKey,
      DesireParameters parentsDesireParameters);

  /**
   * Forms DesireForOthers
   */
  DesireForOthers formDesireForOthers(DesireKey desireKey);

  /**
   * Forms DesireForOthers with parent node
   */
  DesireForOthers formDesireForOthers(DesireKey desireKey, DesireKey parentDesireKey,
      DesireParameters parentsDesireParameters);

  /**
   * Forms DesireFromAnotherAgent WithAbstractIntention
   */
  Optional<DesireFromAnotherAgent.WithAbstractIntention> formDesireFromOtherAgentWithAbstractIntention(
      SharedDesireForAgents desireForAgents);

  /**
   * Forms DesireFromAnotherAgent WithAbstractIntention
   */
  Optional<DesireFromAnotherAgent.WithIntentionWithPlan> formDesireFromOtherAgentWithIntentionWithPlan(
      SharedDesireForAgents desireForAgents);

}
