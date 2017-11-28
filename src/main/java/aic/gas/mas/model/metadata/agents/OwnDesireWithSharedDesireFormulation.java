package aic.gas.mas.model.metadata.agents;

import aic.gas.mas.model.knowledge.WorkingMemory;
import aic.gas.mas.model.metadata.DesireKey;
import aic.gas.mas.model.metadata.DesireParameters;
import aic.gas.mas.model.metadata.agents.configuration.CommonConfiguration;
import aic.gas.mas.model.metadata.agents.configuration.ConfigurationWithSharedDesire;
import aic.gas.mas.model.planing.DesireForOthers;
import aic.gas.mas.utils.MyLogger;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Concrete implementation of own desire with desire to share with others
 */
public class OwnDesireWithSharedDesireFormulation extends DesireFormulation implements
    OwnInternalDesireFormulation<DesireForOthers> {

  final Map<DesireKey, DesireKey> sharedDesireKeyByKey = new HashMap<>();
  final Map<DesireKey, Integer> countOfAgentsToCommitByKey = new HashMap<>();

  @Override
  public Optional<DesireForOthers> formDesire(DesireKey key, WorkingMemory memory) {
    if (supportsDesireType(key)) {
      DesireForOthers forOthers = new DesireForOthers(key,
          memory, getDecisionInDesire(key), getDecisionInIntention(key),
          sharedDesireKeyByKey.get(key), countOfAgentsToCommitByKey.get(key),
          getReactionInDesire(key), getReactionInIntention(key));
      return Optional.of(forOthers);
    }
    return Optional.empty();
  }

  @Override
  public boolean supportsDesireType(DesireKey desireKey) {
    return supportsType(desireKey);
  }

  /**
   * Add configuration for desire
   */
  public void addDesireFormulationConfiguration(DesireKey key,
      ConfigurationWithSharedDesire configuration) {
    addDesireFormulationConfiguration(key, (CommonConfiguration) configuration);
    sharedDesireKeyByKey.put(key, configuration.getSharedDesireKey());
    countOfAgentsToCommitByKey.put(key, configuration.getCounts());
  }

  /**
   * Concrete implementation of own desire with desire to share with others and possibility to
   * create instance based on parent
   */
  public static class Stacked extends OwnDesireWithSharedDesireFormulation implements
      OwnInternalDesireFormulationStacked<DesireForOthers> {

    private final Map<DesireKey, OwnDesireWithSharedDesireFormulation> stack = new HashMap<>();

    @Override
    public Optional<DesireForOthers> formDesire(DesireKey parentKey, DesireKey key,
        WorkingMemory memory, DesireParameters parentsDesireParameters) {
      OwnDesireWithSharedDesireFormulation formulation = stack.get(parentKey);
      if (formulation != null) {
        if (formulation.supportsDesireType(key)) {
          DesireForOthers forOthers = new DesireForOthers(key,
              memory, formulation.getDecisionInDesire(key), formulation.getDecisionInIntention(key),
              formulation.sharedDesireKeyByKey.get(key),
              formulation.countOfAgentsToCommitByKey.get(key), parentsDesireParameters,
              formulation.getReactionInDesire(key), formulation.getReactionInIntention(key));
          return Optional.of(forOthers);
        }
      }
      return formDesire(key, memory);
    }

    @Override
    public boolean supportsDesireType(DesireKey parent, DesireKey key) {
      if (stack.get(parent) == null || !stack.get(parent).supportsDesireType(key)) {
        MyLogger.getLogger().warning(parent.getName() + " is not associated with " + key.getName());
        return supportsType(key);
      }
      return true;
    }

    /**
     * Add configuration for desire
     */
    public void addDesireFormulationConfiguration(DesireKey parent, DesireKey key,
        ConfigurationWithSharedDesire configuration) {
      OwnDesireWithSharedDesireFormulation formulation = stack
          .computeIfAbsent(parent, desireKey -> new OwnDesireWithSharedDesireFormulation());
      formulation.addDesireFormulationConfiguration(key, configuration);
    }
  }

}
