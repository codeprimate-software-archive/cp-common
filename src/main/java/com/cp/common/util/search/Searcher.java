/*
 * Searcher.java (c) 12 May 2002
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.10.25
 & @see com.cp.common.lang.Searchable
 */

package com.cp.common.util.search;

import com.cp.common.lang.Searchable;

public interface Searcher {

  /**
   * Searches for a specified element, or a collection of elements,
   * determined by the Searcher implementation (search algorithm
   * used) to search the Searchable collection of objects.
   *
   * @param collection com.cp.common.lang.Searchable object collection
   * containing elements to search.
   * @return a java.lang.Object element if the element is found,
   * null otherwise, or a collection of objects found by the Searcher
   * implementation, or an empty collection if no object of the
   * Searchable collection is returned by the Searcher.
   */
  public Object search(Searchable collection) throws SearchException;

}
