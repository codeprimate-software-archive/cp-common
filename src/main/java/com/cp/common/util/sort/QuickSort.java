/*
 * QuickSort.java (c) 12 May 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.8.25
 * @see java.util.Collections
 * @see java.util.Comparator
 * @see com.cp.common.lang.Sortable
 * @see com.cp.common.util.sort.Sorter
 * @see com.cp.common.util.sort.AbstractSorter
 */

package com.cp.common.util.sort;

import com.cp.common.lang.Sortable;
import com.cp.common.util.sort.*;
import com.cp.common.util.sort.InsertionSort;
import com.cp.common.util.SortAscendingComparator;
import java.util.Comparator;
import org.apache.log4j.Logger;

public class QuickSort extends com.cp.common.util.sort.AbstractSorter {

  private static final int DEFAULT_THRESHOLD = 10;

  private static final InsertionSort INSERTION_SORT = new InsertionSort();

  private static final Logger logger = Logger.getLogger(QuickSort.class);

  private int threshold;

  /**
   * Creates a QuickSort object to sort a collection of objects using
   * the Quick Sort algorithm.  Defaults to ascending order with a
   * threshold of 10 elements.
   */
  public QuickSort() {
    this(SortAscendingComparator.getInstance(), DEFAULT_THRESHOLD);
  }

  /**
   * Constructs a new instance of the QuickSort class initializing
   * the order of the sort to either ascending or descending order
   * as specified by the orderBy parameter for the elements in the
   * Sortable object.
   *
   * @param orderBy is a Comparator used in determining the order
   * of the sort.
   */
  public QuickSort(final Comparator orderBy) {
    this(orderBy, DEFAULT_THRESHOLD);
  }

  /**
   * Constructs a new instance of the QuickSort class initialized to
   * the threshold by which the QuickSort class relies on another
   * sorting algorithm to finish sorting the remaining elements of the
   * Sortable object.  The reason quickSort uses INSERTION_SORT is that
   * Quick Sort actually degrades in performance due to the recursive
   * calls on a small number of elements.
   *
   * @param threshold is integer value specifying the number of remaining
   * unsorted elements that must exist before Quick Sort will rely on the
   * other sorting algorithm.
   */
  public QuickSort(final int threshold) {
    this(SortAscendingComparator.getInstance(), threshold);
  }

  /**
   * Constructs a new instance of the QuickSort class initialized to the
   * specified sort order and threshold for this sort.
   *
   * @param orderBy is a Comparator used in determining the order
   * of the sort.
   * @param threshold is integer value specifying the number of remaining
   * unsorted elements that must exist before Quick Sort will rely on the
   * other sorting algorithm.
   */
  public QuickSort(final Comparator orderBy,
                   final int threshold) {
    super(orderBy);
    setThreshold(threshold);
    INSERTION_SORT.setOrderBy(orderBy);
  }

  /**
   * Returns the value of the threshold.
   */
  public int getThreshold() {
    return threshold;
  }

  /**
   * quickSort implements Hoare's "Quick Sort" algorithm for sorting
   * (in ascending or descending order) Sortable objects.  This method
   * implements the Quick Sort recursively. The method should be
   * called with beginIndex == 0 and endIndex == Sortable.size()-1.
   * NOTE: To speed up the sort even more, the recursive calls could
   * be incorporated into a thread (not that it would not matter much
   * on a single processor, but on multi-processor systems, this
   * routine would smoke).
   *
   * @param collection the Sortable object for which the class has been
   * designated to sort.
   * @param beginIndex is the begin index into the elememts of this
   * collection to start it's sort.
   * @param endIndex is the end index into the elements of this
   * collection to stop sorting.
   * @throws com.cp.common.util.sort.SortException if the sort operation fails.
   */
  public void quickSort(final Sortable collection,
                        int beginIndex,
                        int endIndex)
      throws com.cp.common.util.sort.SortException {
    logger.debug("beginIndex = " + beginIndex);
    logger.debug("endIndex = " + endIndex);

    if (beginIndex >= endIndex) {
      return;
    }

    if ((endIndex - beginIndex) < threshold) {
      INSERTION_SORT.insertionSort(collection, beginIndex, endIndex);
      return;
    }

    Object pivot = collection.get(beginIndex);

    int bindex = beginIndex + 1;
    int eindex = endIndex;

    while (bindex < eindex) {
      // Find a value greater than the pivot value.
      HIGH: while (bindex < eindex) {
        if (getOrderBy().compare(collection.get(bindex), pivot) > 0) {
          break HIGH;
        }
        ++bindex;
      }

      // Find a value less than the pivot value.
      LOW: while (eindex >= bindex) {
        if (getOrderBy().compare(collection.get(eindex), pivot) < 0) {
          break LOW;
        }
        --eindex;
      }

      if (bindex < eindex) {
        swap(collection, bindex, eindex);
      }
    }

    swap(collection, beginIndex, eindex); // Swap Pivot
    quickSort(collection, beginIndex, eindex-1); // Recurse
    quickSort(collection, eindex+1, endIndex); // Recurse
  }

  /**
   * Sets the minimum number of elements to apply the other sorting algorithm
   * to.
   *
   * @param threshold is an integer value specifying the number of elements
   * that must remain to apply with another sorting algorithm.
   */
  public final void setThreshold(final int threshold) {
    logger.debug("threshold = " + threshold);
    this.threshold = Math.abs(threshold);
  }

  /**
   * sort method is called to sort a collection of objects using the
   * Quick Sort algorithm.
   *
   * @param collection the Sortable object in which to sort using the
   * Quick Sort algorithm.
   * @throws com.cp.common.util.sort.SortException if the sort operation fails!
   * @see java.util.Collections#sort
   */
  public void sort(final Sortable collection) throws SortException {
    logger.debug("Sorting entire Sortable collection...");
    quickSort(collection, 0, collection.size()-1);
  }

}
