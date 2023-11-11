/*
 * ShellSort.java (c) 25 April 2004
 *
 * The Shell Sort is the most efficient O(n^2) algorithm.
 *
 * http://linux.wku.edu/~lamonml/algor/sort/shell.html
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.8.25
 */

package com.cp.common.util.sort;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Sortable;
import com.cp.common.util.sort.*;
import com.cp.common.util.SortAscendingComparator;
import java.util.Comparator;
import org.apache.log4j.Logger;

public class ShellSort extends com.cp.common.util.sort.AbstractSorter {

  private static final Logger logger = Logger.getLogger(ShellSort.class);

  private static final int DEFAULT_INCREMENT = 7;

  private final Integer callerIncrement;

  /**
   * Creates an instance of the ShellSort class to sort the content of a Sortable Collection using the
   * Shell Sort algorithm.  The sort will order content in the Sortable Collection in ascending order
   * according to element order.
   */
  public ShellSort() {
    this(SortAscendingComparator.getInstance(), null);
  }

  /**
   * Creates an instance of the ShellSort class to sort the content of a Sortable Collection using the
   * Shell Sort algorithm.  The order of the sort will be defined by the specified Comparator.
   * @param orderBy the Comparator used to determine the order of the elements in the Sortable Collection
   * when sorted by the Shell Sort algorithm.
   */
  public ShellSort(final Comparator orderBy) {
    this(orderBy, null);
  }

  /**
   * Creates an instance of the ShellSort class to sort the content of a Sortable Collection using the
   * Shell Sort algorithm, having the specified increment to determine set size.
   * @param increment an integer value specifying the increment, or the set size.
   */
  public ShellSort(final Integer increment) {
    this(SortAscendingComparator.getInstance(), increment);
  }

  /**
   * Creates an instance of the ShellSort class to sort the content of a Sortable Collection using the
   * Shell Sort algorithm, having the specified increment to determine set size.  The order of the sort
   * will be defined by the specified Comparator.
   * @param orderBy the Comparator used to determine the order of the elements in the Sortable Collection
   * when sorted by the Shell Sort algorithm.
   * @param increment an integer value specifying the increment, or the set size.
   */
  public ShellSort(final Comparator orderBy, final Integer increment) {
    super(orderBy);
    callerIncrement = increment;
  }

  /**
   * Returns the increment, used in determine the set size, specified by either the caller or by
   * using the Sortable Collection to logically determine the optimal size of the set.
   * @param collection the Sortable Collection to used in logically calculating the optimal size of
   * the set.
   * @return an integer value specifying the increment, or the set size.
   */
  private int getIncrement(final Sortable collection) {
    return (ObjectUtil.isNotNull(callerIncrement) ? callerIncrement.intValue() : DEFAULT_INCREMENT);
  }

  /**
   * Sorts the specified Sortable Collection using the Shell Sort algorithm.
   * @param collection the Sortable Collection to sort using the Shell Sort algorithm.
   * @throws com.cp.common.util.sort.SortException if the sort operation fails.
   */
  public void sort(final Sortable collection) throws SortException {
    try {
      for (int increment = getIncrement(collection); increment > 0; increment /= 2) {
        for (int i = 0, collectionSize = collection.size(); i < collectionSize; i++) {
          int j = i;
          final Object temp = collection.get(i);

          while ((j >= increment) && (getOrderBy().compare(collection.get(j - increment), temp) > 0)) {
            if (logger.isDebugEnabled()) {
              logger.debug("Collection(" + (j - increment) + ") is (" + collection.get(j - increment)
                + "); setting Collection element @ index (" + j + ")");
            }
            collection.set(collection.get(j - increment), j);
            j -= increment;
          }

          if (logger.isDebugEnabled()) {
            logger.debug("Collection(" + i + ") is (" + temp + "); setting Collection element @ index (" + j + ")");
          }

          collection.set(temp, j);
        }
      }
    }
    catch (Exception e) {
      logger.error("Failed to sort Sortable Collection (" + collection.getClass().getName()
        + ") with the Shell Sort algorithm!", e);
      throw new SortException("Failed to sort Sortable Collection (" + collection.getClass().getName()
        + ") with the Shell Sort algorithm!", e);
    }
  }

}
