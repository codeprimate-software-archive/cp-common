/*
 * SortType.java (c) 31 March 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.8.25
 */

package com.cp.common.util.sort;

import com.cp.common.lang.ObjectUtil;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.log4j.Logger;

public final class SortType {

  private static final Logger logger = Logger.getLogger(SortType.class);

  public static final String BUBBLE_SORT_DESCRIPTION = "Bubble Sort";
  public static final String HEAP_SORT_DESCRIPTION = "Heap Sort";
  public static final String INSERTION_SORT_DESCRIPTION = "Insertion Sort";
  public static final String MERGE_SORT_DESCRIPTION = "Merge Sort";
  public static final String MULTITHREADED_QUICK_SORT_DESCRIPTION = "Multithreaded Quick Sort";
  public static final String QUICK_SORT_DESCRIPTION = "Quick Sort";
  public static final String SELECTION_SORT_DESCRIPTION = "Selection Sort";
  public static final String SHELL_SORT_DESCRIPTION = "Shell Sort";
  public static final String TREE_SORT_DESCRIPTION = "Tree Sort";

  public static final SortType BUBBLE_SORT = new SortType(0, BUBBLE_SORT_DESCRIPTION);
  public static final SortType HEAP_SORT = new SortType(2, HEAP_SORT_DESCRIPTION);
  public static final SortType INSERTION_SORT = new SortType(3, INSERTION_SORT_DESCRIPTION);
  public static final SortType MERGE_SORT = new SortType(4, MERGE_SORT_DESCRIPTION);
  public static final SortType MULTITHREADED_QUICK_SORT = new SortType(5, MULTITHREADED_QUICK_SORT_DESCRIPTION);
  public static final SortType QUICK_SORT = new SortType(6, QUICK_SORT_DESCRIPTION);
  public static final SortType SELECTION_SORT = new SortType(7, SELECTION_SORT_DESCRIPTION);
  public static final SortType SHELL_SORT = new SortType(8, SHELL_SORT_DESCRIPTION);
  public static final SortType TREE_SORT = new SortType(9, TREE_SORT_DESCRIPTION);

  private static final Set<SortType> SORT_TYPE_SET = new HashSet<SortType>();

  static {
    SORT_TYPE_SET.add(BUBBLE_SORT);
    SORT_TYPE_SET.add(HEAP_SORT);
    SORT_TYPE_SET.add(INSERTION_SORT);
    SORT_TYPE_SET.add(MERGE_SORT);
    SORT_TYPE_SET.add(MULTITHREADED_QUICK_SORT);
    SORT_TYPE_SET.add(QUICK_SORT);
    SORT_TYPE_SET.add(SELECTION_SORT);
    SORT_TYPE_SET.add(SHELL_SORT);
    SORT_TYPE_SET.add(TREE_SORT);
  }

  private final int id;
  private final String description;

  /**
   * Default private constructor used to enforce non-instantiability of the SortType class.
   * @param id the unique identifier of the Sort algorithm.
   * @param description a description of the Sorting algorithm.
   */
  private SortType(final int id, final String description) {
    this.id = id;
    this.description = description;
  }

  /**
   * Returns a SortType enumeration based on the unique identifier.
   * @param id a unique identifier for the Sorting algorithm.
   * @return a SortType enumeration based on the algorithm's unique identifier,
   * or null if no SortType enum matches the id.
   */
  public static SortType getSortType(final int id) {
    if (logger.isDebugEnabled()) {
      logger.debug("id (" + id + ")");
    }
    for (Iterator it = SORT_TYPE_SET.iterator(); it.hasNext(); ) {
      final SortType sortType = (SortType) it.next();
      if (sortType.getId() == id) {
        return sortType;
      }
    }
    return null;
  }

  /**
   * Returns a SortType enumeration based on the algorithm description.
   * @param description a description of the Sorting algorithm.
   * @return a SortType enumeration based on the algorithm's description,
   * or null if no SortType enum matches the description.
   */
  public static SortType getSortType(final String description) {
    if (logger.isDebugEnabled()) {
      logger.debug("description (" + description + ")");
    }
    for (Iterator it = SORT_TYPE_SET.iterator(); it.hasNext(); ) {
      final SortType sortType = (SortType) it.next();
      if (sortType.getDescription().equals(description)) {
        return sortType;
      }
    }
    return null;
  }

  /**
   * Return the description of the Sorting algorithm.
   * @return a String describing the Sorting algorithm.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Return a unique identifier of the Sorting algorithm.
   * @return a integer value representing the unique identifier of the Sorting
   * algorithm.
   */
  public int getId() {
    return id;
  }

  /**
   * Determines whether the specified Object is equal to this SortType enum.
   * @param obj the Object being tested to equality with this SortType.
   * @return a boolean value indicating whether the specified Object is equal
   * to this SortType.
   */
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof SortType)) {
      return false;
    }

    final SortType that = (SortType) obj;

    return getId() == that.getId()
      && ObjectUtil.equals(getDescription(), that.getDescription());
  }

  /**
   * Computes the hash value of this SortType.
   * @return a integer value representing the hash value for this SortType.
   */
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + new Integer(getId()).hashCode();
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getDescription());
    return hashValue;
  }

  /**
   * Returns a String representation of this SortType containing state information.
   * @return a String representation of this SortType.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{id = ");
    buffer.append(getId());
    buffer.append(", description = ").append(getDescription());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
