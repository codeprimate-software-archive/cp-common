/*
 * BubbleSort.java (c) 25 April 2004
 *
 * The Bubble Sort is a O(n^2) algorithm.
 *
 * http://www.cs.princeton.edu/~ah/alg_anim/gawain-4.0/BubbleSort.html
 * http://linux.wku.edu/~lamonml/algor/sort/bubble.html
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

public class BubbleSort extends com.cp.common.util.sort.AbstractSorter {

  private static final Logger logger = Logger.getLogger(BubbleSort.class);

  /**
   * Creates an instance of the BubbleSort class to sort the content of a Sortable Collection using the
   * Bubble Sort algorithm.  The sort will order content in the Sortable Collection in ascending order
   * according to element order.
   */
  public BubbleSort() {
    this(SortAscendingComparator.getInstance());
  }

  /**
   * Creates an instance of the BubbleSort class to sort the content of a Sortable Collection using the
   * Bubble Sort algorithm.  The order of the sort will be defined by the specified Comparator.
   * @param orderBy the Comparator used to determine the order of the elements in the Sortable Collection
   * when sorted by the Bubble Sort algorithm.
   */
  public BubbleSort(final Comparator orderBy) {
    super(orderBy);
  }

  /**
   * Sorts the specified Sortable Collection using the Bubble Sort algorithm.
   * @param collection the Sortable Collection to sort using the Bubble Sort algorithm.
   * @throws com.cp.common.util.sort.SortException if the sort operation fails.
   */
  public void sort(final Sortable collection) throws com.cp.common.util.sort.SortException {
    final int collectionSize = collection.size();
    for (int pass = (collectionSize - 1); pass >= 0; pass--) {
      for (int index = 1; index <= pass; index++) {
        final Object currentElement = collection.get(index);
        if (logger.isDebugEnabled()) {
          logger.debug("currentElement (" + currentElement + ")");
        }

        final Object previousElement = collection.get(index - 1);
        if (logger.isDebugEnabled()) {
          logger.debug("previousElement (" + previousElement + ")");
        }

        if (getOrderBy().compare(previousElement, currentElement) > 0) {
          if (logger.isDebugEnabled()) {
            logger.debug("Swapping element at index (" + index + ") with element at index (" + (index - 1) + ")!");
          }
          swap(collection, index, index - 1);
        }
      }
    }
  }

}
