/*
 * SelectionSort.java (c) 1 May 2004
 *
 * The Selection Sort is a O(n^2) algorithm.
 *
 * http://linux.wku.edu/~lamonml/algor/sort/selection.html
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

public class SelectionSort extends com.cp.common.util.sort.AbstractSorter {

  private static final Logger logger = Logger.getLogger(SelectionSort.class);

  /**
   * Creates an instance of the SelectionSort class to sort the content of a Sortable Collection using
   * the Selection Sort algorithm.  The sort will order content in the Sortable Collection in ascending
   * order according to element order.
   */
  public SelectionSort() {
    this(SortAscendingComparator.getInstance());
  }

  /**
   * Creates an instance of the SelectionSort class to sort the content of a Sortable Collection using
   * the Selection Sort algorithm.  The order of the sort will be defined by the specified Comparator.
   * @param orderBy the Comparator used to determine the order of the elements in the Sortable Collection
   * when sorted by the Selection Sort algorithm.
   */
  public SelectionSort(final Comparator orderBy) {
    super(orderBy);
  }

  /**
   * Sorts the specified Sortable Collection using the Selection Sort algorithm.
   * @param collection the Sortable Collection to sort using the Selection Sort algorithm.
   * @throws com.cp.common.util.sort.SortException if the sort operation fails.
   */
  public void sort(final Sortable collection) throws SortException {
    try {
      for (int i = 0, size = collection.size(); i < (size - 1); i++) {
        int min = i;
        for (int j = (i + 1); j < size; j++) {
          if (getOrderBy().compare(collection.get(j), collection.get(min)) < 0) {
            min = j;
          }
        }
        if (min != i) {
          swap(collection, i, min);
        }
      }
    }
    catch (Exception e) {
      logger.error("Failed to sort Sortable Collection (" + collection.getClass().getName()
        + ") with the Selection Sort algorithm!", e);
      throw new SortException("Failed to sort Sortable Collection (" + collection.getClass().getName()
        + ") with the Selection Sort algorithm!", e);
    }
  }

}
