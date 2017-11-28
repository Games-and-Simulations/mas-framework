package aic.gas.mas.service;

/**
 * Contract to be implement by each terminable service
 */
public interface TerminableService {

  /**
   * Tell service to terminate
   */
  void terminate();
}
