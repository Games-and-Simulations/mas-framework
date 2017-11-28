package aic.gas.mas.model.planing.command;

import aic.gas.mas.model.planing.IntentionCommand;

/**
 * Contract for acting command creation strategy
 */
public interface CommandFormulationStrategy<V extends CommandForIntention<?>, T extends IntentionCommand<?, ?>> {

  /**
   * Form command from intention
   */
  V formCommand(T intention);
}
