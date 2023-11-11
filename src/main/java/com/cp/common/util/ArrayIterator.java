/*
 * ArrayIterator.java (c) 3 December 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.7.27
 * @see com.cp.common.util.ArrayEnumeration
 * @see java.util.Iterator
 */

package com.cp.common.util;

import com.cp.common.lang.Assert;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class ArrayIterator<T> implements Iterator<T> {

  private int index = 0;

  protected final Log logger = LogFactory.getLog(getClass());

  private final T[] objectArray;

  /**
   * Creates an instance of the ArrayIterator class to iterate the elements of the specified Object array.
   * @param objectArray the Object array who's elements will be iterated by this Iterator.
   */
  public ArrayIterator(final T[] objectArray) {
    Assert.notNull(objectArray, "The object array cannot be null!");
    this.objectArray = objectArray;
  }

  /**
   * Determines whether there is another element to visit in the iteration of the Object array.
   * @return a boolean value indicating whether the iteration has another element.
   */
  public boolean hasNext() {
    return (index < objectArray.length);
  }

  /**
   * Returns the next element in the iteration.
   * @return an Object specifying the next element in the iteration.
   */
  public T next() {
    return objectArray[index++];
  }

  /**
   * Removes the currently visited element in the underlying Object array constituting this iteration.
   */
  public void remove() {
    throw new UnsupportedOperationException("Not Implemented!");
  }

}
