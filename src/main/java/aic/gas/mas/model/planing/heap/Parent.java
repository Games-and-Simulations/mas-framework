package aic.gas.mas.model.planing.heap;

import aic.gas.mas.model.metadata.DesireKey;
import java.util.List;
import java.util.Optional;

/**
 * Contract for parent - it has at least one children
 */
public interface Parent<V extends Node<?> & DesireNodeInterface, K extends Node<?> & IntentionNodeInterface & VisitorAcceptor> {

  /**
   * Get nodes for desires
   */
  List<V> getNodesWithDesire();

  /**
   * Get nodes for intentions
   */
  List<K> getNodesWithIntention();

  /**
   * Return desire key
   */
  Optional<DesireKey> getDesireKeyAssociatedWithParent();

}
