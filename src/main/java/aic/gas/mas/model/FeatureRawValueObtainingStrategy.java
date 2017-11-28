package aic.gas.mas.model;

/**
 * Contract to convert object to feature value
 */
public interface FeatureRawValueObtainingStrategy<V> {

  double returnRawValue(V v);
}
