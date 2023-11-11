/*
 * AbstractSorter.java (c) 9 February 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Right Reserved
 * @author John J. Blum
 * @version 2004.4.30
 * @see java.util.Comparator
 * @see com.cp.common.util.sort.Sorter
 */

package com.cp.common.util.sort;

import com.cp.common.lang.Sortable;
import java.util.Comparator;
import org.apache.log4j.Logger;

public abstract class AbstractSorter implements Sorter {

  private static final Logger logger = Logger.getLogger(AbstractSorter.class);

  private Comparator orderByComparator;

  /**
   * Creates an instance of the AbstractSorter class initialized with the specified orderBy
   * Comparator object.
   * @param orderBy the Comparator used to order the elements of the Sortable collection.
   */
  public AbstractSorter(final Comparator orderBy) {
    setOrderBy(orderBy);
  }

  /**
   * Returns the Comparator that determines the order of the elements in the Sortable collection.
   * @return a java.util.Comparator defining the sort order.
   */
  public Comparator getOrderBy() {
    return orderByComparator;
  }

  /**
   * Sets the order of the sort as defined by the Comparator.
   *
   * @param orderBy is a java.util.Comparator that defines the order in which the Comparable elements
   * of this Sortable collection are ordered.
   * @throws java.lang.NullPointerException if the comparator
   * used in the order by is null.
   */
  public final void setOrderBy(final Comparator orderBy) {
    if (orderBy == null) {
      logger.warn("The orderBy Comparator is null!");
      throw new NullPointerException("The Comparator for the sort order cannot be null!");
    }
    logger.debug("orderBy: " + orderBy);
    this.orderByComparator = orderBy;
  }

  /**
   * swap is a commonly used routine by various sorting algorithms for interchanging two elements
   * in the collection.
   * @param index1 is the index of the first ordered element to be switched for the last element.
   * @param index2 is the index of the last ordered element to be switched for the first element.
   * @throws com.cp.common.util.sort.SortException if the swap opertion fails!
   * @see java.util.Collections.swap
   */
  protected void swap(final Sortable collection,
                      final int index1,
                      final int index2)
      throws SortException {
    try {
      logger.debug("Swapping elements at indexes " + index1 + " and " + index2);
      final Object tempObject = collection.get(index1);
      collection.set(collection.get(index2), index1);
      collection.set(tempObject, index2);
    }
    catch (Exception e) {
      logger.error("Failed to swap elements at indices (" + index1 + ") and (" + index2
        + ") in the Sortable collection (" + (collection.getClass().getName()) + ").", e);
      throw new com.cp.common.util.sort.SortException("Failed to swap elements at indices (" + index1 + ") and (" + index2
        + ") in the Sortable collection (" + (collection.getClass().getName()) + ").", e);
    }
  }

}
