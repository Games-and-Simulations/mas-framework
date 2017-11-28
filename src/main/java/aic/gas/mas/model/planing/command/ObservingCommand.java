package aic.gas.mas.model.planing.command;

import aic.gas.mas.model.knowledge.WorkingMemory;

/**
 * Contract for observing command classes
 */
public interface ObservingCommand<E> {

  /**
   * Method to be called by Executor to execute observation command by updating memory by data from
   * environment
   */
  boolean observe(WorkingMemory memory, E environment);
}
