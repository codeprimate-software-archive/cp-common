/*
 * Identifiable.java (c) 16 July 2005
 *
 * The Identifiable interface is implemented by classes that uniquely identify their object instances (such as
 * the Bean interface in the com.cp.common.beans package) using a Comparable identifier, such as an Integer or Long
 * value.
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.4.13
 * @see java.lang.Comparable
 */

package com.cp.common.lang;

public interface Identifiable<T extends Comparable<T>> {

  /**
   * Returns an identifier value for the object implementing this interface to uniquely identify this object
   * from objects of the same type.
   * @return an object of type T the uniquely identifies objects of the same type implementing this interface.
   * @see Identifiable#setId(Comparable)
   */
  public T getId();

  /**
   * Sets the identifier value uniquely identifying this object from other objects of the same type that implement
   * this interface.
   * @param id an object of type T whose value uniquely identifies object of the same type implementing this interface.
   * @see Identifiable#getId()
   */
  public void setId(T id);

}
