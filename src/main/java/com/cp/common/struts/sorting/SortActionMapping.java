/*
 * SortActionMapping.java (c) 3 May 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.5.19
 */

package com.cp.common.struts.sorting;

import com.cp.common.struts.CPActionMapping;
import org.apache.log4j.Logger;

public class SortActionMapping extends CPActionMapping {

  private static final Logger logger = Logger.getLogger(SortActionMapping.class);

  private String comparator;
  private String sortAlgorithm;

  /**
   * Returns the class name of the Comparator instance.
   * @return the class name of the Comparator used to order elements of a Collection.
   */
  public String getComparator() {
    return comparator;
  }

  /**
   * Sets the specified class name of the type of Comparator used to order the elements of the Sortable Collection.
   * @param comparator the class name of the Comparator instance.
   */
  public void setComparator(final String comparator) {
    logger.debug("comparator (" + comparator + ")");
    this.comparator = comparator;
  }

  /**
   * Returns either the class name or the SortType description of the sort algorithm that will be used to sort the
   * elements of the Sortable Collection.
   * @return either the class name or SortType description of the sorting algorithm.
   */
  public String getSortAlgorithm() {
    return sortAlgorithm;
  }

  /**
   * Sets the specified sorting algorithm to either the name of the Sorter class or the SortType description.
   * @param sortAlgorithm the class name of the sorting algorithm, or the SortType description.
   */
  public void setSortAlgorithm(final String sortAlgorithm) {
    logger.debug("sortAlgorithm (" + sortAlgorithm + ")");
    this.sortAlgorithm = sortAlgorithm;
  }

  /**
   * Returns a String description of this SortActionMapping instance.
   * @return a String representation of this SortingActionMapping instance.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{comparator = ");
    buffer.append(getComparator());
    buffer.append(", sortAlgorithm = ").append(getSortAlgorithm());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
