/*
 * MergeSort.java (c) 26 April 2004
 *
 * The Merge Sort is a O(n log n) algorithm.
 *
 * http://linux.wku.edu/~lamonml/algor/sort/merge.html
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

public class MergeSort extends com.cp.common.util.sort.AbstractSorter {

  private static final Logger logger = Logger.getLogger(MergeSort.class);

  /**
   * Creates an instance of the MergeSort class to sort the content of a Sortable Collection using the
   * Merge Sort algorithm.  The sort will order content in the Sortable Collection in ascending order
   * according to element order.
   */
  public MergeSort() {
    this(SortAscendingComparator.getInstance());
  }

  /**
   * Creates an instance of the MergeSort class to sort the content of a Sortable Collection using the
   * Merge Sort algorithm.  The order of the sort will be defined by the specified Comparator.
   * @param orderBy the Comparator used to determine the order of the elements in the Sortable Collection
   * when sorted by the Merge Sort algorithm.
   */
  public MergeSort(final Comparator orderBy) {
    super(orderBy);
  }

  /**
   * Performs a merge of the sorted elements in the Sortable Collection to the temporary array.
   * @param collection the Sortable Collection to sort using the Merge Sort algorithm.
   * @param tempArray the temporary array holding the intermediary results of the merge and sort operations.
   * @param left the left most element in the Sortable Collection to include in the merge operation.
   * @param mid the middle element between the left and right elements.
   * @param right the right most element in the Sortable Collection to include in the merge operation.
   * @throws com.cp.common.util.sort.SortException if the sort operation fails.
   * @throws com.cp.common.util.sort.SortException
   */
  private void merge(final Sortable collection, final Object[] tempArray, int left, int mid, int right)
      throws com.cp.common.util.sort.SortException {
    try {
      int leftEnd = mid - 1;
      int tempPosition = left;
      int numElements = right - left + 1;

      while ((left <= leftEnd) && (mid <= right)) {
        if (getOrderBy().compare(collection.get(left), collection.get(mid)) <= 0) {
          tempArray[tempPosition++] = collection.get(left++);
        }
        else {
          tempArray[tempPosition++] = collection.get(mid++);
        }
      }

      for ( ; left <= leftEnd; left++) {
        tempArray[tempPosition++] = collection.get(left);
      }

      for ( ; mid <= right; mid++) {
        tempArray[tempPosition++] = collection.get(mid);
      }

      for (int i = numElements; --i >= 0; ) {
        collection.set(tempArray[right], right);
        right--;
      }
    }
    catch (Exception e) {
      logger.error("Failed to sort Sortable Collection (" + collection.getClass().getName()
        + ") with the Merge Sort algorithm!", e);
      throw new SortException("Failed to sort Sortable Collection (" + collection.getClass().getName()
        + ") with the Merge Sort algorithm!", e);
    }
  }

  /**
   * Sorts the specified Sortable Collection using the Merge Sort algorithm.
   * @param collection the Sortable Collection to sort using the Merge Sort algorithm.
   * @param left the left most element in the Sortable Collection to include in the sort operation.
   * @param right the right most element in the Sortable Collection to include in the sort operation.
   * @throws SortException if the sort operation fails.
   */
  private void mergeSort(final Sortable collection, final Object[] tempArray, final int left, final int right)
      throws SortException {
    if (logger.isDebugEnabled()) {
      logger.debug("left (" + left + ")");
      logger.debug("right (" + right + ")");
    }

    if (right > left) {
      final int mid = (left + right) / 2;

      if (logger.isDebugEnabled()) {
        logger.debug("mid (" + mid + ")");
      }

      mergeSort(collection, tempArray, left, mid);
      mergeSort(collection, tempArray, mid + 1, right);
      merge(collection, tempArray, left, mid + 1, right);
    }
  }

  /**
   * Sorts the specified Sortable Collection using the Merge Sort algorithm.
   * @param collection the Sortable Collection to sort using the Merge Sort algorithm.
   * @throws SortException if the sort operation fails.
   */
  public void sort(final Sortable collection) throws SortException {
    if (logger.isDebugEnabled()) {
      logger.debug("Sorting a Sortable Collection of type (" + collection.getClass().getName()
        + ") using the Merge Sort algorithm!");
    }
    mergeSort(collection, new Object[collection.size()], 0, collection.size() - 1);
  }

}
