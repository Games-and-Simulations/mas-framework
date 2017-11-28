package aic.gas.mas.model;

import aic.gas.mas.model.metadata.DesireKey;

/**
 * Contract defining method to be implemented by each class which wants enable user to get desire
 * key associated with class
 */
public interface DesireKeyIdentificationInterface {

  /**
   * Returns DesireKey associated with this instance
   */
  DesireKey getDesireKey();

}
