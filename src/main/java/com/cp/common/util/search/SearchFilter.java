/*
 * SearchFilter.java (c) 17 April 2002
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.10.25
 * @see java.io.FileFilter
 * @see com.cp.common.util.search.Searcher
 */

package com.cp.common.util.search;

public interface SearchFilter {

  /**
   * Equates the object from the Searchable collection to the criteria
   * of this filter determining if there is a match.
   *
   * @param obj the Object contained in the Searchable collection in
   * question of whether it satisfies the search filter criteria.
   * @return a boolean value of true if the collection object matched
   * the criteria, false otherwise.
   */
  public boolean matches(Object obj) throws SearchException;

}
