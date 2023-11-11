/*
 * ComparableComparator.java (c) 1 November 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.1.11
 * @see java.lang.Comparable
 * @see java.util.Comparator
 * @see com.cp.common.util.SortAscendingComparator
 * @see com.cp.common.util.SortDescendingComparator.
 */

package com.cp.common.util;

import com.cp.common.lang.ObjectUtil;
import java.util.Comparator;
import org.apache.log4j.Logger;

public final class ComparableComparator<T extends Comparable<T>> implements Comparator<T> {

  private static final Logger logger = Logger.getLogger(ComparableComparator.class);

  /**
   * Returns the Singleton instance of this Comparator.
   * @return the Singleton.
   */
  public static <T extends Comparable<T>> ComparableComparator<T> getInstance() {
    return new ComparableComparator<T>();
  }

  /**
   * Compares two Objects for absolute ordering.  This method assumes that the
   * Object parameters implement the Comparable interface.  Objects that are null
   * come before Objects that are not null.
   * @param c1 an Object value compared with c2.
   * @param c2 an Object value compared with c1.
   * @return a negative value if Object 1 is null or less than Object 2, returns
   * a postive value if Object 2 is null or Object 1 is greater than Object 2,
   * otherwise if they are equal, returns zero.
   */
  public int compare(final T c1, final T c2) {
    if (logger.isDebugEnabled()) {
      logger.debug("c1 (" + c1 + ")");
      logger.debug("c2 (" + c2 + ")");
    }
    return (ObjectUtil.isNull(c1) ? -1 : (ObjectUtil.isNull(c2) ? 1 : c1.compareTo(c2)));
  }

  /**
   * Returns the String representation of this Comparator.
   * @return a String representation of this Comparator.
   */
  public String toString() {
    return getClass().getName();
  }

}
