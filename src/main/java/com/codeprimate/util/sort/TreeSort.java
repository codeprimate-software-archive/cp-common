/*
 * TreeSort.java (c) 25 August 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.8.25
 */

package com.codeprimate.util.sort;

import com.cp.common.lang.Sortable;
import com.cp.common.util.sort.AbstractSorter;
import com.cp.common.util.SortAscendingComparator;
import com.cp.common.util.sort.SortException;
import java.util.Comparator;
import java.util.Iterator;
import org.apache.commons.collections.SortedBag;
import org.apache.commons.collections.bag.TreeBag;
import org.apache.log4j.Logger;

public class TreeSort extends AbstractSorter {

  private static final Logger logger = Logger.getLogger(TreeSort.class);

  /**
   * Creates an instance of the TreeSort class to sort the content of a Sortable Collection using the
   * Tree Sort algorithm.  The sort will order content in the Sortable Collection in ascending order
   * according to element order.
   */
  public TreeSort() {
    this(SortAscendingComparator.getInstance());
  }

  /**
   * Creates an instance of the TreeSort class to sort the content of a Sortable Collection using the
   * Tree Sort algorithm.  The order of the sort will be defined by the specified Comparator.
   * @param orderBy the Comparator used to determine the order of the elements in the Sortable Collection
   * when sorted by the Tree Sort algorithm.
   */
  public TreeSort(final Comparator orderBy) {
    super(orderBy);
  }

  /**
   * Sorts the specified Sortable Collection using the Tree Sort algorithm.
   * @param collection the Sortable Collection to sort using the Tree Sort algorithm.
   * @throws com.cp.common.util.sort.SortException if the sort operation fails.
   */
  public void sort(final Sortable collection) throws com.cp.common.util.sort.SortException {
    try {
      final SortedBag orderedBag = new TreeBag(getOrderBy());

      for (int index = collection.size(); --index >= 0; ) {
        orderedBag.add(collection.get(index));
      }

      int index = 0;
      for (Iterator it = orderedBag.iterator(); it.hasNext(); ) {
        collection.set(it.next(), index++);
      }
    }
    catch (Exception e) {
      logger.error("Failed to sort Sortable Collection (" + collection.getClass().getName()
        + ") with the Tree Sort algorithm!", e);
      throw new com.cp.common.util.sort.SortException("Failed to sort Sortable Collection (" + collection.getClass().getName()
        + ") with the Tree Sort algorithm!", e);
    }
  }

}
