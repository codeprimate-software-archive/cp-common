/*
 * BinarySearchFilter.java (c) 18 September 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.10.25
 * @see com.cp.common.util.search.BinarySearch
 * @see com.cp.common.util.search.Searcher
 * @see com.cp.common.util.search.SearchFilter
 */

package com.cp.common.util.search;

public interface BinarySearchFilter extends SearchFilter {

  /**
   * Compares the object from the Searchable collection to the
   * set of criteria of this filter determing if the object
   * comes before or after the objects in the Searchable
   * collection that closely match the criteria.
   * @param obj the Object in comparison to the search filter
   * criteria.
   * @return a negative value if the object in the Searchable
   * collection should come before objects matching the criteria,
   * 0 if the object matches the criteria, or a positive value
   * if the object should come after objects matching the
   * criteria.
   */
  public int compare(Object obj) throws SearchException;

}
