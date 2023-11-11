/*
 * IndexedSearch.java (c) 24 October 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.10.26
 */

package com.codeprimate.util.search;

import com.cp.common.lang.Searchable;
import com.cp.common.util.search.AbstractSearcher;
import com.cp.common.util.search.SearchException;
import com.cp.common.util.search.SearchFilter;

public class IndexedSearch extends com.cp.common.util.search.AbstractSearcher {

  public IndexedSearch(final SearchFilter filter) {
    super(filter);
  }

  public Object search(final Searchable collection) throws com.cp.common.util.search.SearchException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

}
