/*
 * IteratorToEnumeration.java (c) 15 November 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.7.28
 * @see com.cp.common.util.EnumerationToIterator
 * @see java.util.Enumeration
 * @see java.util.Iterator
 */

package com.cp.common.util;

import com.cp.common.lang.Assert;
import java.util.Enumeration;
import java.util.Iterator;

public class IteratorToEnumeration<E> implements Enumeration<E> {

  private final Iterator<E> it;

  /**
   * Creates an instance of the IteratorToEnumeration class to translate operations
   * of the Iterator interface into operations of the Enumeration interface.
   * @param it the Iterator instance to treat as an Enumeration.
   */
  public IteratorToEnumeration(final Iterator<E> it) {
    Assert.notNull(it, "The Iterator cannot be null!");
    this.it = it;
  }

  /**
   * Determines whether more elements exist in the iteration.
   * @return a boolean value idicating whether the other elements exist in
   * the iteration and will be returned by the nextElement method.
   */
  public boolean hasMoreElements() {
    return it.hasNext();
  }

  /**
   * Returns the next element in the iteration.
   * @return an Object representing the next element of the iteration.
   */
  public E nextElement() {
    return it.next();
  }

}
