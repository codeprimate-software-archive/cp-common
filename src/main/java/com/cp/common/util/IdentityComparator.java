/*
 * IdentityComparator.java (c) 17 September 2005
 *
 * This Comparator implementation will sort Objects according to their memory address.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.9.17
 */

package com.cp.common.util;

import java.util.Comparator;

public class IdentityComparator implements Comparator {

  private static final IdentityComparator INSTANCE = new IdentityComparator();

  /**
   * Default private constuctor of the IdentityComparator class.
   */
  private IdentityComparator() {
  }

  /**
   * Returns the singleton instance of the IdentityComparator class.
   * @return an instance of the IdentityComparator class.
   */
  public static IdentityComparator getInstance() {
    return INSTANCE;
  }

  /**
   * Comparaes two Objects using their identities (memory address) to order them.
   * @param obj1 the first Object in the comparison.
   * @param obj2 the second Object in the comparison.
   * @return a integer value of -1 if Object1's memory address is less than Object2's memory address,
   * 0 if the Object1 is the same object as Object2, or 1 if Object1's memory address is greater
   * than Object2's memory address.
   */
  public int compare(final Object obj1, final Object obj2) {
    return (obj1 == obj2 ? 0 : (obj1.hashCode() > obj2.hashCode() ? 1 : -1));
  }

  /**
   * Returns a String representation of this Comparator class.
   * @return a String representation of this Comparator class.
   */
  public String toString() {
    return getClass().getName();
  }

}
