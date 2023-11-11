/*
 * BeanPropertySortActionComparator.java (c) 19 May 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.5.19
 */

package com.cp.common.struts.sorting;

import java.util.Comparator;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.log4j.Logger;

public class BeanPropertySortActionComparator extends SortActionComparator {

  private static final Logger logger = Logger.getLogger(BeanPropertySortActionComparator.class);

  /**
   * Performs a relation comparison of two Objects from a Sortable Collections based on their common
   * property defined by sortKey to determine their order.
   * @param o1 the first Object from the Sortable Collection.
   * @param o2 the second Object from the Sortable Collection.
   * @return an integer value of -1 if the o1 is less than o2, 1 if o1 is greater than o2 and 0 if
   * o1 and o2 are equal.  Reverses the sign of the return value on descending order.
   */
  public int compare(final Object o1, final Object o2) {
    final int multiplier = (isReverse() ? -1 : 1);
    logger.debug("multiplier = " + multiplier);
    return multiplier * getComparator().compare(o1, o2);
  }

  /**
   * Returns an instance of the BeanComparator class to sort elements of a Sortable Collection by their
   * common property defined by sortKey.
   * @return a instance of the BeanComparator class to sort elements of a Sortable Collection by their
   * common property defined by sortKey.
   */
  private Comparator getComparator() {
    return new BeanComparator(getSortKey());
  }

}
