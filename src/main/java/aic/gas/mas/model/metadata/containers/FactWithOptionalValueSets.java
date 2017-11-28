package aic.gas.mas.model.metadata.containers;

import aic.gas.mas.model.FeatureRawValueObtainingStrategy;
import aic.gas.mas.model.metadata.FactConverterID;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.Getter;

/**
 * Container for fact type and raw value obtaining strategy
 */
@Getter
public class FactWithOptionalValueSets<V> extends FactConverterID<V> {

  private final FeatureRawValueObtainingStrategy<Stream<Optional<Stream<V>>>> strategyToObtainValue;

  public FactWithOptionalValueSets(FactConverterID<V> factConverterID,
      FeatureRawValueObtainingStrategy<Stream<Optional<Stream<V>>>> strategyToObtainValue) {
    super(factConverterID.getID(), factConverterID.getFactKey());
    this.strategyToObtainValue = strategyToObtainValue;
  }
}
