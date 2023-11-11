/*
 * CPSearchFactory.java (c) 24 October 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.8.14
 */

package com.codeprimate.util.search;

import com.cp.common.util.search.AbstractSearchFactory;
import com.cp.common.util.search.SearchFilter;
import com.cp.common.util.search.Searcher;

public class CPSearchFactory extends AbstractSearchFactory {

  /**
   * Gets an instance of the Indexed Search algorithm.
   * @param filter the SearchFilter used to identify the item being searched.
   * @return a Searcher that implements the Indexed Search algorithm.
   */
  public com.cp.common.util.search.Searcher getIndexedSearch(final SearchFilter filter) {
    return new com.codeprimate.util.search.IndexedSearch(filter);
  }

}
