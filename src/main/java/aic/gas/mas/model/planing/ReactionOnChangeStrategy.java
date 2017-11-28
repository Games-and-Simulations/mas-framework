package aic.gas.mas.model.planing;

import aic.gas.mas.model.knowledge.WorkingMemory;
import aic.gas.mas.model.metadata.DesireParameters;

/**
 * Interface with template for reaction on change strategy
 */
public interface ReactionOnChangeStrategy {

  /**
   * Strategy to update beliefs
   */
  void updateBeliefs(WorkingMemory memory, DesireParameters desireParameters);

}
