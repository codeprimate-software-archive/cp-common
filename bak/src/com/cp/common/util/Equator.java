/*
 * Equator.java (c) 2 October 2002
 *
 * Modeled after the java.util.Comparator interface for comparing
 * and ordering objects, the Equator interface is used to do
 * custom equality testing on objects.  An implementation of this
 * class can be used instead to test objects for equality rather
 * than using the standard equivalence relationship defined by
 * the Object.equals(:Object):boolean method.  Or, perhaps the
 * object you wish to equate to another object does not provide
 * a sufficient implementation of equals, or does not override
 * the equals method in the Object class.  Thus, to support the
 * business case for equality comparisons, Equator can be
 * implemented to compare the attributes and properties of an
 * object that distinguish it from another object within the
 * business system.
 *
 * About the Comparator Interface:
 * Although we could have, the Comparator interface was not
 * used to implement Equator functionality since doing so would
 * overload the meaning of Comparator and possibly violate
 * Comparator's contract (which is to impose a "total ordering"
 * on some collection of objects).  If the natural ordering of
 * certain objects (i.e. business objects) is undefined, and
 * the Comparator was implemented to return 0 when objects are
 * equal and return a non-zero "trivial" value when objects
 * are not equal, mistakeningly using this Comparator
 * implementation in Collection objects for ordering could
 * result in indeterministic behavior, and ultimately failure
 * of the business system.
 *
 * Therefore, in conclusion, to prevent any form of confussion
 * amongst developers and the context in which objects should
 * be used, the Equator interface was defined.  This makes all
 * comparison operations explicit as distinguished by the
 * interface.
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2003.10.20
 * @see java.util.Comparator
 */

package com.cp.common.util;

public interface Equator {

  /**
   * Compares two objects for equality and returns
   * a boolean value of true if they are equal,
   * false if they are not equal.
   *
   * @param obj1 operand being compared for equality
   * with obj2.
   * @param obj2 operand being compared for equality
   * with obj1.
   * @return a boolean value indicating whether the
   * specified objects are equal to one another.
   */
  public boolean areEqual(Object obj1, Object obj2 );

}
