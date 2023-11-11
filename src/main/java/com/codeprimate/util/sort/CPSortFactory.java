/*
 * CPSortFactory.java (c) 24 October 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.8.13
 */

package com.codeprimate.util.sort;

import com.cp.common.util.sort.AbstractSortFactory;
import com.cp.common.util.sort.Sorter;
import java.util.Comparator;
import org.apache.log4j.Logger;

public class CPSortFactory extends AbstractSortFactory {

  private static final Logger logger = Logger.getLogger(CPSortFactory.class);

  /**
   * Gets an instance of the Multithreaded Quick Sort algorithm, order determined by the Comparator.
   * @param orderBy a Comparater used to order of the elements of the Sortable collection.
   * @return a Sorter that implements the Multithreaded Quick Sort algorithm.
   */
  public com.cp.common.util.sort.Sorter getMultithreadedQuickSort(final Comparator orderBy) {
    return new com.codeprimate.util.sort.MultithreadedQuickSort(orderBy);
  }

  /**
   * Gets an instance of the Tree Sort algorithm, order determined by the Comparator.
   * @param orderBy a Comparater used to order the elements of the Sortable collection.
   * @return a Sorter that implements the Tree Sort algorithm.
   */
  public com.cp.common.util.sort.Sorter getTreeSort(final Comparator orderBy) {
    return new com.codeprimate.util.sort.TreeSort(orderBy);
  }

}
