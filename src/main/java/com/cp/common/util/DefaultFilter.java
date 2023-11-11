/*
 * DefaultFilter.java (c) 8 8 2009 
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.8.8
 */

package com.cp.common.util;

public class DefaultFilter<T> implements Filter<T> {

  private final boolean toAcceptOrNotToAccept;

  /**
   * Constructs an instance of the DefaultFilter class initialized with a boolean value indicating whether this Filter
   * should accept all values or reject all values.
   * @param toAcceptOrNotToAccept a boolean value indicating a default response to accept.
   */
  public DefaultFilter(final boolean toAcceptOrNotToAccept) {
    this.toAcceptOrNotToAccept = toAcceptOrNotToAccept;
  }

  /**
   * Determines whether the specified object satisfies the criteria of this filter.
   * @param obj the Object tested against the criteria of this filter.
   * @return a boolean value if the specified object passes the criteria of this filter.
   */
  public boolean accept(final T obj) {
    return toAcceptOrNotToAccept;
  }

}
