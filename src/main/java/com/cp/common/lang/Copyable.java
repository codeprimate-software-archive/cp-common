/*
 * Copyable.java (c) 12 May 2002
 *
 * The Copyable interface is meant to replace the java.lang.Cloneable interface by forcing classes that implement
 * this interface to obey it's contract of implementing the copy operation.  Classes implementing the
 * java.lang.Cloneable interface are not required to implement a clone operation since the interface does not
 * declare a clone method.  java.lang.Cloneable is merely a marker interface.
 *
 * Copyright (c) 2002, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.4.7
 * @see java.lang.Cloneable
 */

package com.cp.common.lang;

public interface Copyable {

  /**
   * Creates an exact duplicate of the object who's class implements this interface. Look to the class's implementation
   * to determine whether the copy operation is "deep" or "shallow".
   * @return an of object of the designated class type which is a clone of this object who's class implements
   * this interface.
   */
  public Object copy();

}
