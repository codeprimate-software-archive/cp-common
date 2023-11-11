/*
 * Searchable.java (c) 22 April 2001
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.10.24
 * @see com.cp.common.util.search.Searcher
 */

package com.cp.common.lang;

import java.util.Iterator;

public interface Searchable<T> {

  /**
   * Returns the element in this Searchable object at the specified index.
   * @param index is an integer index into the Searchable object's collection of objects.
   * @return a java.lang.Object element at the specified index.
   */
  public T get(int index);

  /**
   * Returns an iterator over the Searchable object's collection of elements.
   * @return an interator to iterate the elements of the Searchable object's collection.
   */
  public Iterator<T> iterator();

  /**
   * Returns the number of elements in the Searchable object's collection of objects.
   * @return a integer value of the number of objects in this collection.
   */
  public int size();

}
