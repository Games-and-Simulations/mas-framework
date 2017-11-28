package aic.gas.mas.model.metadata.containers;

import aic.gas.mas.model.FeatureRawValueObtainingStrategy;
import aic.gas.mas.model.metadata.FactConverterID;
import java.util.Optional;
import lombok.Getter;

/**
 * Container for fact type and raw value obtaining strategy
 */
@Getter
public class FactWithOptionalValue<V> extends FactConverterID<V> {

  private final FeatureRawValueObtainingStrategy<Optional<V>> strategyToObtainValue;

  public FactWithOptionalValue(FactConverterID<V> factConverterID,
      FeatureRawValueObtainingStrategy<Optional<V>> strategyToObtainValue) {
    super(factConverterID.getID(), factConverterID.getFactKey());
    this.strategyToObtainValue = strategyToObtainValue;
  }
}
