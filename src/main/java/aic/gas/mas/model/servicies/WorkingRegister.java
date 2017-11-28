package aic.gas.mas.model.servicies;

/**
 * Interface to be implemented by each working registry to define method which make snapshot of this
 * registry
 */
public interface WorkingRegister<V extends Register<?>> {

  /**
   * Create snapshot - read only - with current state of affairs
   */
  V makeSnapshot();

}
