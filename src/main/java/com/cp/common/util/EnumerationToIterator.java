/*
 * EnumerationToIterator.java (c) 15 November 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.7.28
 * @see com.cp.common.util.IteratorToEnumeration
 * @see java.util.Enumeration
 * @see java.util.Iterator
 */

package com.cp.common.util;

import com.cp.common.lang.Assert;
import java.util.Enumeration;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EnumerationToIterator<E> implements Iterator<E> {

  private static final Log logger = LogFactory.getLog(EnumerationToIterator.class);

  private final Enumeration<E> enumx;

  /**
   * Constructs an instance of the EnumerationToIterator class to translate operations
   * of the Enumeration interface into operations of the Iterator interface.
   * @param enumx an Enumeration to treat as a Iterator.
   */
  public EnumerationToIterator(final Enumeration<E> enumx) {
    Assert.notNull(enumx, "The Enumeration cannot be null!");
    this.enumx = enumx;
  }

  /**
   * Determines whether the enumeration has a next element.
   * @return a boolean value indicating whether the EnumerationToIterator will return
   * another element from the enumeration.
   */
  public boolean hasNext() {
    return enumx.hasMoreElements();
  }

  /**
   * Returns the next element in the enumeration.
   * @return the next Object in the enumeration.
   */
  public E next() {
    return enumx.nextElement();
  }

  /**
   * Removes the last element returned by next from the numeration.
   * @throws java.lang.UnsupportedOperationException this method is not
   * supported by this Iterator.
   */
  public void remove() {
    logger.warn("remove operation is not supported!");
    throw new UnsupportedOperationException("Operation Not Supported!");
  }

}
