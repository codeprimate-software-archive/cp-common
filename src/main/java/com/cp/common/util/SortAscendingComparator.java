/*
 * SortAscendingComparator.java (c) 9 February 2003
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

public final class SortAscendingComparator implements Comparator<Comparable> {

  private static final Logger logger = Logger.getLogger(SortAscendingComparator.class);

  private static final SortAscendingComparator INSTANCE = new SortAscendingComparator();

  /**
   * Private constructor to enforce non-instantiability in support of the Single design pattern.
   */
  private SortAscendingComparator() {
  }

  /**
   * Compares the first object to second object to determine ascending order. This Comparator class
   * implementation sorts according to the object's natural order in ascending order.
   * @param c1 the Object being compared with the second Object parameter to determine if this object
   * is less than, equal to, or greater than the second object parameter.
   * @param c2 the Object compared to Object one.
   * @return a negative value if Object 1 is less than Object 2, 0 if they are equal, and a positive
   * value if Object 1 is greater than Object 2.
   */
  public int compare(final Comparable c1, final Comparable c2 ) {
    if (logger.isDebugEnabled()) {
      logger.debug("c1 (" + c1 + ")");
      logger.debug("c2 (" + c2 + ")");
    }
    return c1.compareTo(c2);
  }

  /**
   * Returns the singleton instance of this Comparator object.
   * @return the only instance of the SortAscendingComparator class.
   */
  public static SortAscendingComparator getInstance() {
    return INSTANCE;
  }

  /**
   * Returns a String representation of this Comparator.
   * @return a String representation of this Comparator.
   */
  public String toString() {
    return "Sort Ascending";
  }

}
