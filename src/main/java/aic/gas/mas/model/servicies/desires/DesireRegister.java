package aic.gas.mas.model.servicies.desires;

import aic.gas.mas.model.agents.Agent;
import aic.gas.mas.model.planing.SharedDesire;
import aic.gas.mas.model.planing.SharedDesireInRegister;
import aic.gas.mas.model.servicies.Register;
import java.util.Map;

/**
 * DesireRegister contains desires received from agents
 */
abstract class DesireRegister extends Register<Map<SharedDesire, SharedDesireInRegister>> {

  DesireRegister(Map<Agent, Map<SharedDesire, SharedDesireInRegister>> dataByOriginator) {
    super(dataByOriginator);
  }
}
