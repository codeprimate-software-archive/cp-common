/*
 * DefaultEquator (c) 2 October 2002
 *
 * Default implementation of the Equator interface.
 * Calls the Object.equals method on the object paremeters passed into the areEqual method in to test for equality.
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.4
 * @see com.cp.common.util.Equator
 */

package com.cp.common.util;

public final class DefaultEquator implements Equator {

  private static final DefaultEquator INSTANCE = new DefaultEquator();

  /**
   * Default private constructor to enforce the non-instantiability property
   * of the Singleton instance.
   */
  private DefaultEquator() {
  }

  /**
   * Default implementation of the areEqual method testing for equality by
   * calling obj1.equals(obj2).  However, the method checks the parameters
   * for null references.  If obj1 is null, than obj2 will equals obj1 if
   * and only if obj2 is null.
   */  
  public boolean areEqual(final Object obj1, final Object obj2 ) {
    return (obj1 == null ? obj2 == null : obj1.equals(obj2));
  }

  /**
   * Returns the single instance of the DefaultEquator class to it's caller.
   */
  public static final DefaultEquator getInstance() {
    return INSTANCE;
  }

}
