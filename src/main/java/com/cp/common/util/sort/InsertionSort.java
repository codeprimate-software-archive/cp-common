/*
 * InsertionSort.java (c) 12 May 2002
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
import com.cp.common.util.SortAscendingComparator;
import java.util.Comparator;
import org.apache.log4j.Logger;

public class InsertionSort extends com.cp.common.util.sort.AbstractSorter {

  private static final Logger logger = Logger.getLogger(InsertionSort.class);

  /**
   * Constructs a new InsertionSort object to sort a Sortable collection
   * of objects in ascending order using the insertion sort algorithm.
   */
  public InsertionSort() {
    this(SortAscendingComparator.getInstance());
    logger.debug("Sort Ascending!");
  }

  /**
   * Constructs a new instance of the InsertionSort class initialized with
   * the specified Comparator determing element order.
   *
   * @param orderBy is a Comparator used in determining the order of the
   * sort.
   */
  public InsertionSort(final Comparator orderBy) {
    super(orderBy);
  }

  /**
   * This method implements the "insertion sort" algorithm to sort a collection
   * of objects.  This class is used by the QuickSort algorith to sort the
   * remaining elements less than the sortThreshold.
   *
   * @param beginIndex is the begin row index in the collection where the
   * insertion sort starts sorting elements.
   * @param endIndex is the end row index in the collection where the insertion
   * sort stops sorting elements.
   * @throws com.cp.common.util.sort.SortException
   */
  public void insertionSort(final Sortable collection,
                            final int beginIndex,
                            final int endIndex)
      throws com.cp.common.util.sort.SortException {
    logger.debug("beginIndex = " + beginIndex);
    logger.debug("endIndex = " + endIndex);

    try {
      for (int i = beginIndex+1; i <= endIndex; i++) {
        Object x = collection.get(i);
        Object y = null;

        int j = i - 1;

        while (j >= beginIndex && getOrderBy().compare(x, (y = collection.get(j))) < 0) {
          collection.set(y, j+1);
          j--;
        }

        collection.set(x, j+1);
      }
    }
    catch (Exception e) {
      logger.error("Insertion Sort Failed!", e);
      throw new com.cp.common.util.sort.SortException("Failed to sort Sortable collection with Insertion Sort!", e);
    }
  }

  /**
   * sort method is called to sort a collection of objects using the
   * insertion sort algorithm.
   *
   * @param collection the Sortable object in which to sort using the
   * insertion sort algorithm.
   * @throws com.cp.common.util.sort.SortException if the collection
   * could not be sorted.
   * @see java.util.Collections#sort
   */
  public void sort(final Sortable collection) throws com.cp.common.util.sort.SortException {
    logger.debug("Soring entire Sortable Collection...");
    if (logger.isDebugEnabled()) {
      logger.debug("collection: " + collection);
    }
    insertionSort(collection, 0, collection.size()-1);
  }

}
