/*
 * AbstractSearcher.java (c) 5 March 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.10.25
 * @see com.cp.common.util.search.Searcher
 * @see com.cp.common.util.search.SearchFilter
 */

package com.cp.common.util.search;

import com.cp.common.lang.ObjectUtil;
import org.apache.log4j.Logger;

public abstract class AbstractSearcher<F extends SearchFilter> implements Searcher {

  private static final Logger logger = Logger.getLogger(AbstractSearcher.class);

  private F filter;

  /**
   * Creates an instance of the AbstractSearcher class initialized with
   * the specified SearchFilter object.
   * @param filter the SearchCriteria used to filter elements of a
   * Searchable collection.
   */
  public AbstractSearcher(final F filter) {
    setSearchFilter(filter);
  }

  /**
   * Returns the specified search filter criteria used by a
   * Searcher implementation to match elements in a Searchable
   * collection.
   *
   * @return an instance of the SearchFilter interface used to
   * filter elements of a Searchable Collection.
   */
  public F getSearchFilter() {
    return filter;
  }

  /**
   * Sets the search filtering criteria when looking for elements
   * in the Searchable collection.
   *
   * @param filter is a SearchFilter impelementation specifying
   * criteria in order to qualify elements of a Searchable
   * Collections during a search operation.
   * @throws java.lang.NullPointerException if the filter parameter
   * is null.
   */
  protected final void setSearchFilter(final F filter) {
    if (ObjectUtil.isNull(filter)) {
      logger.warn("The search filter cannot be null!");
      throw new NullPointerException("The search filter cannot be null!");
    }
    this.filter = filter;
  }

}
