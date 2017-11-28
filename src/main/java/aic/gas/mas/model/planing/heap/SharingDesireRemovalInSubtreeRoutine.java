package aic.gas.mas.model.planing.heap;

import aic.gas.mas.model.ResponseReceiverInterface;
import aic.gas.mas.model.planing.SharedDesireForAgents;
import aic.gas.mas.utils.MyLogger;
import java.util.HashSet;
import java.util.Set;

/**
 * Routine for shared desires removal from mediator
 */
public class SharingDesireRemovalInSubtreeRoutine implements ResponseReceiverInterface<Boolean> {

  private final Object lockMonitor = new Object();
  private Boolean unregistered = false;

  boolean unregisterSharedDesire(Set<SharedDesireForAgents> sharedDesires,
      HeapOfTrees heapOfTrees) {

    //share desire and wait for response of registration
    synchronized (lockMonitor) {
      if (heapOfTrees.getAgent().getDesireMediator()
          .unregisterDesires(new HashSet<>(sharedDesires), this)) {
        try {
          lockMonitor.wait();
        } catch (InterruptedException e) {
          MyLogger.getLogger()
              .warning(this.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
        }

        //is desire register, if so, make intention out of it
        if (unregistered) {
          heapOfTrees.removeSharedDesiresForOtherAgents(sharedDesires);
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
