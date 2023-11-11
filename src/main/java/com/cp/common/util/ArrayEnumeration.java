/*
 * ArrayEnumeration.java (c) 27 July 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.27
 * @see com.cp.common.util.ArrayIterator
 * @see java.util.Enumeration
 */

package com.cp.common.util;

import com.cp.common.lang.Assert;
import java.util.Enumeration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ArrayEnumeration<T> implements Enumeration<T> {

  private int index = 0;

  protected final Log logger = LogFactory.getLog(getClass());

  private final T[] objectArray;

  /**
   * Constructs an instance of the ArrayEnumeration class initialized with the specified generic-typed Object array.
   * @param objectArray a generic-type Object array containing the elements for the Enumeration.
   */
  public ArrayEnumeration(final T[] objectArray) {
    Assert.notNull(objectArray, "The object array cannot be null!");
    this.objectArray = objectArray;
  }

  /**
   * Determines whether the Enumeration (or underlying Object array) has more elements to traverse.
   * @return a boolean value indicating whether there are more elements in the Enumeration to traverse.
   */
  public boolean hasMoreElements() {
    return (index < objectArray.length);
  }

  /**
   * Gets the next element in the Enumeration.
   * @return the next element in the Enumeration.
   */
  public T nextElement() {
    return objectArray[index++];
  }

}
