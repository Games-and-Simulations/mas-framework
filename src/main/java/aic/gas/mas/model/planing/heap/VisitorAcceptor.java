package aic.gas.mas.model.planing.heap;

/**
 * Interface for each element of heapOfTrees to accept heapOfTrees visitor instance
 */
public interface VisitorAcceptor {

  /**
   * Accept instance of heapOfTrees visitor
   */
  void accept(TreeVisitorInterface treeVisitor);
}
