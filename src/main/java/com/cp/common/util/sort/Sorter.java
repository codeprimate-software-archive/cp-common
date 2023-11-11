/*
 * Sorter.java (c) 12 May 2002
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2003.10.21
 */

package com.cp.common.util.sort;

import com.cp.common.lang.Sortable;
import java.util.Comparator;

public interface Sorter {

  /**
   * Returns the Comparator that determines the order of the elements
   * in the Sortable collection.
   *
   * @return a java.util.Comparator defining the sort order.
   */
  public Comparator getOrderBy();

  /**
   * Sets the order of the sort as defined by the Comparator.
   *
   * @param orderBy is a java.util.Comparator class that defines
   * the order in which the Comparable elements of this
   * Sortable collection are ordered.
   */
  public void setOrderBy(Comparator orderBy);

  /**
   * Sorts the specified Sortable collection of Comparable elements.
   *
   * @param collection is a com.cp.common.lang.Sortable collection
   * of comparable objects to sort.
   * @throws com.cp.common.util.sort.SortException if the sort operation
   * fails!
   * @see java.util.Collections.sort
   */
  public void sort(Sortable collection) throws SortException;

}
