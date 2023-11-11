/*
 * SortDescendingComparator.java (c) 9 February 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.8.26
 * @see java.lang.Comparable
 * @see java.util.Comparator
 */

package com.cp.common.util;

import java.util.Comparator;
import org.apache.log4j.Logger;

public final class SortDescendingComparator implements Comparator<Comparable> {

  private static final Logger logger = Logger.getLogger(SortDescendingComparator.class);

  private static final SortDescendingComparator INSTANCE = new SortDescendingComparator();

  /**
   * Private constructor to enforce non-instantiability in support of the Single design pattern.
   */
  private SortDescendingComparator() {
  }

  /**
   * Compares the second object to first object to determine descending order. This Comparator class
   * sorts according to the object's natural order in descending order.
   * @param c1 the Object compared to Object two.
   * @param c2 the Object being compared with the first Object parameter to determine if this object
   * is less than, equal to, or greater than the first object parameter.
   * @return a negative value if Object 2 is less than Object 1, 0 if they are equal, and a positive
   * value if Object 2 is greater than Object 1.
   */
  public int compare(final Comparable c1, final Comparable c2) {
    if (logger.isDebugEnabled()) {
      logger.debug("c1 (" + c1 + ")");
      logger.debug("c2 (" + c2 + ")");
    }
    return c2.compareTo(c1);
  }

  /**
   * Returns the singleton instance of this Comparator object.
   * @return the only instance of the SortDescendingComparator class.
   */
  public static SortDescendingComparator getInstance() {
    return INSTANCE;
  }

  /**
   * Returns a String representation of this Comparator.
   * @return a String representation of this Comparator.
   */
  public String toString() {
    return "Sort Descending";
  }

}
