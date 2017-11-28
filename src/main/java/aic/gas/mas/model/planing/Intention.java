package aic.gas.mas.model.planing;

import aic.gas.mas.model.DesireKeyIdentificationInterface;
import aic.gas.mas.model.FactContainerInterface;
import aic.gas.mas.model.knowledge.ReadOnlyMemory;
import aic.gas.mas.model.metadata.AgentType;
import aic.gas.mas.model.metadata.DesireKey;
import aic.gas.mas.model.metadata.DesireParameters;
import aic.gas.mas.model.metadata.FactKey;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.Getter;

/**
 * Class describing template for intention. Intention instance represents metadata and high level
 * abstraction of what agent has committed to achieve to. It may contain other desires related to
 * this intention to be consider.
 */
public abstract class Intention<T extends InternalDesire<?>> implements FactContainerInterface,
    DesireKeyIdentificationInterface, OnChangeActor, OnDestructionActor {

  protected final CommitmentDecider removeCommitment;
  private final T originalDesire;
  @Getter
  private final Optional<ReactionOnChangeStrategy> reactionOnChangeStrategy;

  Intention(T originalDesire, CommitmentDeciderInitializer removeCommitment,
      ReactionOnChangeStrategy reactionOnChangeStrategy) {
    this.originalDesire = originalDesire;
    this.removeCommitment = removeCommitment
        .initializeCommitmentDecider(originalDesire.desireParameters);
    this.reactionOnChangeStrategy = Optional.ofNullable(reactionOnChangeStrategy);
  }

  public DesireParameters getParametersOfDesire() {
    return originalDesire.desireParameters;
  }

  @Override
  public DesireKey getDesireKey() {
    return originalDesire.getDesireKey();
  }

  public <V> Optional<V> returnFactValueForGivenKey(FactKey<V> factKey) {
    return originalDesire.returnFactValueForGivenKey(factKey);
  }

  public <V, S extends Stream<V>> Optional<S> returnFactSetValueForGivenKey(FactKey<V> factKey) {
    return originalDesire.returnFactSetValueForGivenKey(factKey);
  }

  /**
   * Returns fact value from desire parameters
   */
  public <V> Optional<V> returnFactValueForGivenKeyInDesireParameters(FactKey<V> factKey) {
    return originalDesire.returnFactValueForGivenKeyInParameters(factKey);
  }

  @Override
  public void actOnRemoval() {
    actOnChange(originalDesire.memory, originalDesire.desireParameters);
  }

  public boolean shouldRemoveCommitment(List<DesireKey> madeCommitmentToTypes,
      List<DesireKey> didNotMakeCommitmentToTypes,
      List<DesireKey> typesAboutToMakeDecision) {

    //TODO - HACK! does not change return value and enables reaction
    return removeCommitment
        .shouldCommit(madeCommitmentToTypes, didNotMakeCommitmentToTypes, typesAboutToMakeDecision,
            originalDesire.memory) && actOnChange(originalDesire.memory,
        originalDesire.desireParameters);
  }

  public boolean shouldRemoveCommitment(List<DesireKey> madeCommitmentToTypes,
      List<DesireKey> didNotMakeCommitmentToTypes,
      List<DesireKey> typesAboutToMakeDecision, int numberOfCommittedAgents) {

    //TODO - HACK! does not change return value and enables reaction
    return removeCommitment
        .shouldCommit(madeCommitmentToTypes, didNotMakeCommitmentToTypes, typesAboutToMakeDecision,
            originalDesire.memory, numberOfCommittedAgents) && actOnChange(originalDesire.memory,
        originalDesire.desireParameters);
  }

  /**
   * Returns fact value set from desire parameters
   */
  public <V, S extends Stream<V>> Optional<S> returnFactSetValueForGivenKeyInDesireParameters(
      FactKey<V> factKey) {
    return originalDesire.returnFactSetValueForGivenKeyInParameters(factKey);
  }

  /**
   * Returns fact value for desire parameters of parenting intention
   */
  public <V> Optional<V> returnFactValueOfParentIntentionForGivenKey(FactKey<V> factKey) {
    return originalDesire.returnFactValueOfParentIntentionForGivenKey(factKey);
  }

  /**
   * Returns fact value set for desire parameters of parenting intention
   */
  public <V, S extends Stream<V>> Optional<S> returnFactSetValueOfParentIntentionForGivenKey(
      FactKey<V> factKey) {
    return originalDesire.returnFactSetValueOfParentIntentionForGivenKey(factKey);
  }

  public Optional<ReadOnlyMemory> getReadOnlyMemoryForAgent(int agentId) {
    return originalDesire.getReadOnlyMemoryForAgent(agentId);
  }

  public Stream<ReadOnlyMemory> getReadOnlyMemoriesForAgentType(AgentType agentType) {
    return originalDesire.getReadOnlyMemoriesForAgentType(agentType);
  }

  public Stream<ReadOnlyMemory> getReadOnlyMemories() {
    return originalDesire.getReadOnlyMemories();
  }

  public int getAgentId() {
    return originalDesire.getAgentId();
  }

  public boolean isFactKeyForValueInMemory(FactKey<?> factKey) {
    return originalDesire.isFactKeyForValueInMemory(factKey);
  }

  public boolean isFactKeyForSetInMemory(FactKey<?> factKey) {
    return originalDesire.isFactKeyForSetInMemory(factKey);
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) {
          return true;
      }
      if (!(o instanceof Intention)) {
          return false;
      }

    Intention<?> intention = (Intention<?>) o;
    return originalDesire.equals(intention.originalDesire);
  }

  @Override
  public int hashCode() {
    return originalDesire.hashCode();
  }
}
