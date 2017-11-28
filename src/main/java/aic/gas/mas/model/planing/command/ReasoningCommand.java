package aic.gas.mas.model.planing.command;

import aic.gas.mas.model.planing.IntentionCommand;

/**
 * Template for command with reasoning to make
 */
public abstract class ReasoningCommand extends CommandForIntention<IntentionCommand.OwnReasoning> {

  protected ReasoningCommand(IntentionCommand.OwnReasoning intention) {
    super(intention);
  }
}
