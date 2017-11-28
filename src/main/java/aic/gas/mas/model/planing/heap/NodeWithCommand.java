package aic.gas.mas.model.planing.heap;

import aic.gas.mas.model.planing.IntentionCommand;
import aic.gas.mas.model.planing.InternalDesire;
import aic.gas.mas.model.planing.command.CommandForIntention;

/**
 * Contract for nodes with command
 */
interface NodeWithCommand<T extends CommandForIntention<? extends IntentionCommand<? extends InternalDesire<? extends IntentionCommand<?, ?>>, T>>> {

  /**
   * Return command associated with node
   */
  T getCommand();

}
