package aic.gas.mas.model.planing.heap;

import aic.gas.mas.model.metadata.DesireKey;
import java.util.List;
import java.util.Optional;

/**
 * Contract for each node representing desire
 */
public interface DesireNodeInterface<V extends Node<?> & IntentionNodeInterface> {

  /**
   * Make commitment to this desire and replace itself by intention
   */
  Optional<V> makeCommitment(List<DesireKey> madeCommitmentToTypes,
      List<DesireKey> didNotMakeCommitmentToTypes,
      List<DesireKey> typesAboutToMakeDecision);

  /**
   * Get desire key associated with desire
   */
  DesireKey getAssociatedDesireKey();

}
