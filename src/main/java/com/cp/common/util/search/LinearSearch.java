/*
 * LinearSearch.java (c) 12 May 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.10.25
 * @see com.cp.common.lang.Searchable
 * @see com.cp.common.util.search.Searcher
 * @see com.cp.common.util.search.AbstractSearcher
 * @see com.cp.common.util.search.SearchFilter
 */

package com.cp.common.util.search;

import com.cp.common.lang.Searchable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.log4j.Logger;

public class LinearSearch extends AbstractSearcher<SearchFilter> {

  private static final Logger logger = Logger.getLogger(LinearSearch.class);

  /**
   * Constructs a new LinearSearch object with the specified
   * filter.  The filter is used to determine whether objecs
   * in the collection match the search criteria.  The
   * LinearSearch object traverses the collection linearly
   * testing each object for membership, and then adding it
   * to the overall set of results if the criteria holds.
   *
   * @param filter com.cp.common.util.search.SearchFilter used to
   * determine set membership of the objects in the collection
   * matching the search criteria.
   */
  public LinearSearch(final SearchFilter filter) {
    super(filter);
  }

  /**
   * Searches the given collection for objects the match
   * the criteria specified by the SearchFilter object.
   *
   * @param collection the Searchable collection to search.
   */
  public Object search(final Searchable collection) throws SearchException {
    final Collection searchResults = new LinkedList();

    for (Iterator it = collection.iterator(); it.hasNext(); ) {
      final Object element = it.next();

      if (getSearchFilter().matches(element)) {
        try {
          searchResults.add(element);
        }
        catch (Exception e) {
          logger.error("Search Failed!", e);
          throw new SearchException(e);
        }
      }
    }

    return searchResults;
  }

}
