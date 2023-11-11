/*
 * HeapSort.java (c) 26 April 2004
 *
 * The Heap Sort is a O(n log n) algorithm.
 *
 * http://linux.wku.edu/~lamonml/algor/sort/heap.html
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.8.25
 */

package com.cp.common.util.sort;

import com.cp.common.lang.Sortable;
import com.cp.common.util.sort.*;
import com.cp.common.util.SortAscendingComparator;
import java.util.Comparator;
import org.apache.log4j.Logger;

public class HeapSort extends com.cp.common.util.sort.AbstractSorter {

  private static final Logger logger = Logger.getLogger(HeapSort.class);

  /**
   * Creates an instance of the HeapSort class to sort the content of a Sortable Collection using the
   * Heap Sort algorithm.  The sort will order content in the Sortable Collection in ascending order
   * according to element order.
   */
  public HeapSort() {
    this(SortAscendingComparator.getInstance());
  }

  /**
   * Creates an instance of the HeapSort class to sort the content of a Sortable Collection using the
   * Heap Sort algorithm.  The order of the sort will be defined by the specified Comparator.
   * @param orderBy the Comparator used to determine the order of the elements in the Sortable Collection
   * when sorted by the Heap Sort algorithm.
   */
  public HeapSort(final Comparator orderBy) {
    super(orderBy);
  }

  /**
   * Sorts the specified Sortable Collection using the Heap Sort algorithm.
   * @param collection the Sortable Collection to sort using the Heap Sort algorithm.
   * @throws com.cp.common.util.sort.SortException if the sort operation fails.
   */
  public void sort(final Sortable collection) throws SortException {
    for (int index = (collection.size() / 2); index >= 0; index--) {
      shiftDown(collection, index, (collection.size() - 1));
    }

    for (int index = (collection.size() - 1); index >= 1; index--) {
      swap(collection, 0, index);
      shiftDown(collection, 0, (index - 1));
    }
  }

  /**
   * Builds and reconstructs the heap.
   * @param collection the Sortable Collection on which the Heap Sort algorithm is applied.
   * @param root the index of the root element defining the starting element of the sort operation.
   * @param bottom the index of the bottom element included in the sort operation.
   * @throws SortException if the sort operation fails.
   */
  private void shiftDown(final Sortable collection, int root, final int bottom)
      throws SortException {
    if (logger.isDebugEnabled()) {
      logger.debug("root (" + root + ")");
      logger.debug("bottom (" + bottom + ")");
    }

    boolean done = false;

    while ((root * 2 <= bottom) && !done) {
      if (logger.isDebugEnabled()) {
        logger.debug("root (" + root + ")");
      }

      int maxChild = -1;

      if (root * 2 == bottom) {
        maxChild = root * 2;
      }
      else if (getOrderBy().compare(collection.get(root * 2), collection.get(root * 2 + 1)) > 0) {
        maxChild = root * 2;
      }
      else {
        maxChild = root * 2 + 1;
      }

      if (logger.isDebugEnabled()) {
        logger.debug("maxChild (" + maxChild + ")");
      }

      if (getOrderBy().compare(collection.get(root), collection.get(maxChild)) < 0) {
        swap(collection, root, maxChild);
        root = maxChild;
      }
      else {
        done = true;
      }
    }
  }

}
