/*
 * Filter.java (c) 24 January 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.1.24
 */

package com.cp.common.util;

public interface Filter<T> {

  /**
   * Determines whether the specified object satisfies the criteria of this filter.
   * @param obj the Object tested against the criteria of this filter.
   * @return a boolean value if the specified object passes the criteria of this filter.
   */
  public boolean accept(T obj);

}
