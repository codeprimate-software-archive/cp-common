/*
 * SortActionComparator.java (c) 3 May 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.5
 */

package com.cp.common.struts.sorting;

import com.cp.common.lang.ObjectUtil;
import java.util.Comparator;
import org.apache.log4j.Logger;

public abstract class SortActionComparator implements Comparator {

  private static final Logger logger = Logger.getLogger(SortActionComparator.class);

  private boolean reverse = false;

  private String sortKey = null;

  /**
   * Determines whether to order the elements of the Sortable Collection in descending order.
   * @return a boolean value indicating whether the sort should occur in reverse, or descending, order.
   */
  public boolean isReverse() {
    return reverse;
  }

  /**
   * Sets whether to order the elements of the Sortable Collection in descending order.
   * @param reverse a boolean value indicating whether the sort should occur in reverse, or descending, order.
   */
  public void setReverse(final boolean reverse) {
    logger.debug("reverse = " + reverse);
    this.reverse = reverse;
  }

  /**
   * Returns the key, or Bean property used to order the elements of the Sortable Collection.
   * @return a String value indicating the key or Bean property used to determine the order of the elements in the
   * Sortable Collection.
   */
  public String getSortKey() {
    return sortKey;
  }

  /**
   * Sets the key, or Bean property used to order the elements of the Sortable Collection.
   * @param sortKey a String value indicating the key or Bean property used to determine the order of the elements
   * in the Sortable Collection.
   */
  public void setSortKey(final String sortKey) {
    logger.debug("sortKey (" + sortKey + ")");
    this.sortKey = sortKey;
  }

  /**
   * Detemines wheter the specified Object is equal to this SortActionComparator.
   * @param obj the Object being tested for equality with this SortActionComparator.
   * @return a boolean value indicating if the Object parameter is equal to this
   * SortActionComparator.
   */
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof SortActionComparator)) {
      return false;
    }

    final SortActionComparator that = (SortActionComparator) obj;

    return (isReverse() == that.isReverse())
      && ObjectUtil.equals(getSortKey(), that.getSortKey());
  }

  /**
   * Computes a hash value for this SortActionCompartor.
   * @return a hash value for this SortActionComparator.
   */
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + Boolean.valueOf(isReverse()).hashCode();
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getSortKey());
    return hashValue;
  }

  /**
   * Returns a String description of this SortActionComparator.
   * @return a String representation of this SortActionComparator.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{reverse = ");
    buffer.append(isReverse());
    buffer.append(", sortKey = ").append(getSortKey());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
