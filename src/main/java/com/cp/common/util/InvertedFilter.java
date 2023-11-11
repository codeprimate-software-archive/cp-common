/*
 * InvertedFilter.java (c) 29 January 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.1.29
 * @see com.cp.common.util.Filter
 */

package com.cp.common.util;

import com.cp.common.lang.Assert;

public class InvertedFilter<T> implements Filter<T> {

  private final Filter<T> filter;

  /**
   * Creates an instance of the InvertedFilter class initialized to the specified filter.
   * @param filter the Filter implementation being negated.
   */
  public InvertedFilter(final Filter<T> filter) {
    Assert.notNull(filter, "The filter object cannot be null!");
    this.filter = filter;
  }

  /**
   * Gets the Filter object referenced by this InvertedFilter class.
   * @return a Filter object reference who's result is negated.
   */
  protected Filter<T> getFilter() {
    return filter;
  }

  /**
   * Determines whether the specified object satisfies the criteria of this filter.
   * @param obj the Object tested against the criteria of this filter.
   * @return a boolean value if the specified object passes the criteria of this filter.
   */
  public boolean accept(final T obj) {
    return !getFilter().accept(obj);
  }

}
