/*
 * CaseInsensitiveStringEquator.java (c) 15 January 2003
 *
 * The StringIgnoreCaseEquator is an implemenation of the Equator
 * interface that expects String arguments as it's parameters to
 * the areEqual method, and compares the Strings for equality
 * ignoring case.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.4
 * @see com.cp.common.util.Equator
 */

package com.cp.common.util;

public final class CaseInsensitiveStringEquator implements Equator {

  private static final CaseInsensitiveStringEquator INSTANCE = new CaseInsensitiveStringEquator();

  /**
   * Default private constructor to enforce the non-instantiability property of a
   * Singleton instance.
   */
  private CaseInsensitiveStringEquator() {
  }

  /**
   * Compares the two objects for equality.  The implementation of this method
   * assumes that the object parameters are of type String, and performs the
   * cast accordingly.  The method then proceeds to perform the comparison by
   * using the equalsIgnoreCase on the first String object parameter.
   *
   * @param obj1 is the first String used in the comparison. 
   * @param obj2 is the second String used in the comparison.
   * @return a boolean indicating whether the two String objects are
   * equal, ignoring their case.
   * @throws java.lang.ClassCastException if the Object parameters are
   * not String types.
   */
  public boolean areEqual(final Object obj1, final Object obj2 ) {
    final String str1 = (String) obj1;
    final String str2 = (String) obj2;
    return (str1 == null ? str2 == null : str1.equalsIgnoreCase(str2));
  }

  /**
   * Factory method for returning the single and only instance of this Equator type.
   */
  public static final CaseInsensitiveStringEquator getInstance() {
    return INSTANCE;
  }

}
