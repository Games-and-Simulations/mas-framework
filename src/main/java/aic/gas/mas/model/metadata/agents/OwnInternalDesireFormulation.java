package aic.gas.mas.model.metadata.agents;

import aic.gas.mas.model.knowledge.WorkingMemory;
import aic.gas.mas.model.metadata.DesireKey;
import aic.gas.mas.model.planing.Intention;
import aic.gas.mas.model.planing.InternalDesire;
import java.util.Optional;

/**
 * Contract for desire formulations - which forms desires from agent's memory
 */
interface OwnInternalDesireFormulation<T extends InternalDesire<? extends Intention<T>>> {

  /**
   * Form desire of given key with data initialized from memory
   */
  Optional<T> formDesire(DesireKey key, WorkingMemory memory);
}
