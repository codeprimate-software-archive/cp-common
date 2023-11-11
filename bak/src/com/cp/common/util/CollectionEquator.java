/*
 * CollectionEquator.java (c) 2 October 2002
 *
 * This class is an implemenation of the Equator interface
 * used to compare the contents of one java.util.Collection
 * to another using the equality relation.  The individual
 * elements of the Collection are compared for equality
 * using an implementation of the Equator interface.  If a
 * Equator implementation for comparing elements in a
 * Collection is not specified, an instance of the
 * DefaultEquator class is used.
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.4
 * @see com.cp.common.util.Equator
 * @see com.cp.common.util.DefaultEquator
 */

package com.cp.common.util;

import com.cp.common.lang.Assert;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import org.apache.log4j.Logger;

public class CollectionEquator implements Equator {

  private static final Logger logger = Logger.getLogger(CollectionEquator.class);

  private final Equator elementEquator;

  /**
   * Instantiates an instance of the CollectionEquator class
   * to compare two Collections for equality.
   */
  public CollectionEquator() {
    this(null);
  }

  /**
   * Instantiates an instance of the CollectionEquator class
   * with the specified Equator object used to compare
   * elements of the Collection object for equality.
   */
  public CollectionEquator(final Equator elementEquator) {
    if (logger.isDebugEnabled()) {
      logger.debug("elementEquator (" + elementEquator + ")");
    }
    this.elementEquator = (elementEquator == null ? DefaultEquator.getInstance() : elementEquator);
  }

  /**
   * Compares the contents of the two Collection objects for equality.  The two Collection objects are equal
   * if and only if the Collection objects contain the same number of elements and elements in the first
   * Collection object equal elemente in the other Collection object as defined by elementEquator.
   * @param collection1 is the Collection operand being compared for equality to collection2.
   * @param collection2 is the Collection operand being compared for equality to collection1.
   * @return a boolean value of true if collection1 equals collection2 defined by the element Equator and
   * whether the Collection objects are the same size.
   * @throws java.lang.ClassCastException if either parameter is not of type java.util.Collection.
   * @throws java.util.ConcurrentModificationException if the Collection objects are modified during the operation
   * of the areEqual method.
   * @throws java.lang.NullPointerException if either Collection object parameter is null.
   */
  public boolean areEqual(final Object collection1, final Object collection2) {
    Assert.notNull(collection1, "The first collection cannot be null!");
    Assert.notNull(collection2, "The second collection cannot be null!");

    // Throws java.lang.ClassCastException if the runtime type of the Object parameters are not Collection objects.
    final Collection c1 = (Collection) collection1;
    final Collection c2 = (Collection) collection2;

    if (logger.isDebugEnabled()) {
      logger.debug("collection 1 (" + collection1 + ")");
      logger.debug("collection 2 (" + collection2 + ")");
    }

    final int size2 = c2.size();

    if (logger.isDebugEnabled()) {
      logger.debug("size of collection 2 (" + size2 + ")");
    }

    // Simple check, obviously the two Collection objects would
    // not be equal if they did not have the same number of
    // elements.
    if (c1.size() != size2) {
      if (logger.isDebugEnabled()) {
        logger.debug("collection sizes are unequal; collection 1 size (" + c1.size()
          + ") and collection 2 size (" + size2 + ")");
      }
      return false;
    }

    for (Iterator it = c1.iterator(); it.hasNext();) {
      // Make sure the second Collection object has not been
      // concurrently modified by another thread.
      if (c2.size() != size2) {
        logger.warn("Concurrent modification of the Collection objects has occurred!");
        throw new ConcurrentModificationException("Concurrent modification of the Collection objects has occurred!");
      }

      if (!contains(c2, it.next())) {
        return false;
      }
    }

    return true;
  }

  /**
   * Determines whether the specified Collection contains the specified element according to
   * the element Equator instance.
   * @param collection Collection object used in dertermining if the element is a member.
   * @param element is the object being tested for membership in the Collection as defined by the element Equator.
   * @return a boolean value indicating if the element is a member of the Collection.
   */
  private boolean contains(final Collection collection, final Object element) {
    for (Object collectionElement : collection) {
      if (elementEquator.areEqual(collectionElement, element))
        return true;
    }
    return false;
  }

}
