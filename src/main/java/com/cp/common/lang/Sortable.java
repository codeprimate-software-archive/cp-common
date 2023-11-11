/*
 * Sortable.java (c) 12 May 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.10.24
 * @see com.cp.common.util.sort.Sorter
 */

package com.cp.common.lang;

public interface Sortable<T> {

  /**
   * Returns the element in this Sortable object at the specified index.
   * @param index is an integer index into the Sortable object's collection of objects.
   * @return a java.lang.Object element at the specified index.
   */
  public T get(int index);

  /**
   * Sets the specified object to be the element at the specified index in the Sortable object's
   * collection of objects.
   * @param object java.lang.Object element to place in the collection objects.
   * @param index is an integer index of where to place the object element in the collection of objects.
   * @throws java.lang.Exception
   */
  public void set(T object, int index) throws Exception;

  /**
   * Returns the number of elements in the Sotable object's collection of objects.
   * @return a integer value of the number of objects in this collection.
   */
  public int size();

}
