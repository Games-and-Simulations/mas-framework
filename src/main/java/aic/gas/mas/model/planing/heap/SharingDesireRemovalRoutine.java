package aic.gas.mas.model.planing.heap;

import aic.gas.mas.model.ResponseReceiverInterface;
import aic.gas.mas.model.planing.SharedDesireForAgents;
import aic.gas.mas.utils.MyLogger;

/**
 * Routine for sharing desire removal from mediator
 */
class SharingDesireRemovalRoutine implements ResponseReceiverInterface<Boolean> {

  private final Object lockMonitor = new Object();
  private Boolean unregistered = false;

  boolean unregisterSharedDesire(SharedDesireForAgents sharedDesire, HeapOfTrees heapOfTrees) {

    //share desire and wait for response of registration
    synchronized (lockMonitor) {
      if (heapOfTrees.getAgent().getDesireMediator().unregisterDesire(sharedDesire, this)) {
        try {
          lockMonitor.wait();
        } catch (InterruptedException e) {
          MyLogger.getLogger()
              .warning(this.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
        }

        //is desire register, if so, make intention out of it
        if (unregistered) {
          heapOfTrees.removeSharedDesireForOtherAgents(sharedDesire);
          return true;
        } else {
          MyLogger.getLogger()
              .warning(this.getClass().getSimpleName() + ": desire for others was not registered.");
        }
      }
    }
    return false;
  }

  @Override
  public void receiveResponse(Boolean response) {

    //notify waiting method to decide commitment
    synchronized (lockMonitor) {
      this.unregistered = response;
      lockMonitor.notify();
    }
  }

}
