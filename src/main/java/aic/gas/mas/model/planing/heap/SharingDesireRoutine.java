package aic.gas.mas.model.planing.heap;

import aic.gas.mas.model.ResponseReceiverInterface;
import aic.gas.mas.model.planing.SharedDesireInRegister;
import aic.gas.mas.utils.MyLogger;

/**
 * Routine for sharing desire with mediator
 */
class SharingDesireRoutine implements ResponseReceiverInterface<Boolean> {

  private final Object lockMonitor = new Object();
  private Boolean registered = false;

  boolean sharedDesire(SharedDesireInRegister sharedDesire, HeapOfTrees heapOfTrees) {
    synchronized (lockMonitor) {
      if (heapOfTrees.getAgent().getDesireMediator().registerDesire(sharedDesire, this)) {
        try {
          lockMonitor.wait();
        } catch (InterruptedException e) {
          MyLogger.getLogger()
              .warning(this.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
        }

        //is desire register, if so, make intention out of it
        if (registered) {
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
      this.registered = response;
      lockMonitor.notify();
    }
  }
}
