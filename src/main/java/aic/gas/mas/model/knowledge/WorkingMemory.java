package aic.gas.mas.model.knowledge;

import aic.gas.mas.model.metadata.AgentType;
import aic.gas.mas.model.metadata.FactKey;
import aic.gas.mas.model.planing.heap.HeapOfTrees;
import aic.gas.mas.utils.MyLogger;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents agent's own memory
 */
public class WorkingMemory extends Memory<HeapOfTrees> {

  public WorkingMemory(HeapOfTrees heapOfTrees, AgentType agentType, int agentId,
      StrategyToGetSetOfMemoriesByAgentType strategyToGetSetOfMemoriesByAgentType,
      StrategyToGetMemoryOfAgent strategyToGetMemoryOfAgent,
      StrategyToGetAllMemories strategyToGetAllMemories) {
    super(heapOfTrees, agentType, agentId, strategyToGetSetOfMemoriesByAgentType,
        strategyToGetMemoryOfAgent, strategyToGetAllMemories);
  }


  /**
   * Return read only copy of working memory to be shared
   */
  public ReadOnlyMemory cloneMemory() {
    forget();
    return new ReadOnlyMemory(factParameterMap.entrySet().stream()
        .filter(factKeyFactEntry -> !factKeyFactEntry.getKey().isPrivate())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
        factSetParameterMap.entrySet().stream()
            .filter(factKeyFactEntry -> !factKeyFactEntry.getKey().isPrivate())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
        tree.getReadOnlyCopy(), agentType, agentId, strategyToGetSetOfMemoriesByAgentType,
        strategyToGetMemoryOfAgent, strategyToGetAllMemories);
  }

  /**
   * Method erases no longer relevant information
   */
  private void forget() {
    factParameterMap.values().forEach(Fact::forget);
    factSetParameterMap.values().forEach(FactSet::forget);
  }

  /**
   * Update fact value
   */
  public <V> void updateFact(FactKey<V> factKey, V value) {
    Fact<V> fact = (Fact<V>) factParameterMap.get(factKey);
    if (fact != null) {
      fact.addFact(value);
    } else {
      MyLogger.getLogger().warning(
          factKey.getName() + " is not present in " + agentType.getName() + " type definition.");
    }
  }

  /**
   * Erase fact value under given key
   */
  public <V> void eraseFactValueForGivenKey(FactKey<V> factKey) {
    Fact<V> fact = (Fact<V>) factParameterMap.get(factKey);
    if (fact != null) {
      fact.removeFact();
    } else {
      MyLogger.getLogger().warning(
          factKey.getName() + " is not present in " + agentType.getName() + " type definition.");
    }
  }

  /**
   * Update fact value
   */
  public <V> void updateFactSetByFact(FactKey<V> factKey, V value) {
    FactSet<V> factSet = (FactSet<V>) factSetParameterMap.get(factKey);
    if (factSet != null) {
      factSet.addFact(value);
    } else {
      MyLogger.getLogger().warning(
          factKey.getName() + " is not present in " + agentType.getName() + " type definition.");
    }
  }

  /**
   * Update fact value
   */
  public <V> void updateFactSetByFacts(FactKey<V> factKey, Set<V> values) {
    FactSet<V> factSet = (FactSet<V>) factSetParameterMap.get(factKey);
    if (factSet != null) {
      factSet.eraseSet();
      values.forEach(factSet::addFact);
    } else {
      MyLogger.getLogger().warning(
          factKey.getName() + " is not present in " + agentType.getName() + " type definition.");
    }
  }

  /**
   * Erase fact from set
   */
  public <V> void eraseFactFromFactSet(FactKey<V> factKey, V value) {
    FactSet<V> factSet = (FactSet<V>) factSetParameterMap.get(factKey);
    if (factSet != null) {
      factSet.removeFact(value);
    } else {
      MyLogger.getLogger().warning(
          factKey.getName() + " is not present in " + agentType.getName() + " type definition.");
    }
  }

  /**
   * Erase fact set under given key
   */
  public <V> void eraseFactSetForGivenKey(FactKey<V> factKey) {
    FactSet<V> factSet = (FactSet<V>) factSetParameterMap.get(factKey);
    if (factSet != null) {
      factSet.eraseSet();
    } else {
      MyLogger.getLogger().warning(
          factKey.getName() + " is not present in " + agentType.getName() + " type definition.");
    }
  }
}
