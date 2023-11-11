/*
 * Mutable.java (c) 1 February 2003
 *
 * The Mutable interface is meant to be implemented by classes that wish to control the mutability of otherwise
 * mutable instances at runtime.  The mutable behavior of an object who's class implements this interface is
 * controlled by the setMutable method and queried by the isMutable method.
 *
 * A class can be made immutable by declaring it final (not-extendable) and having no setters that allow the object's
 * internal state to be modified.  However, an immutable class may contain methods to allow some mutation of the
 * existing immutable object's value to occur, but those methods will return a new instance.  One such example includes
 * the String class in the Java Platform.
 *
 * It is desirable to allow class instances (objects) to toggle this behavior at runtime.  Please note, that a class
 * implementing this interface simply does not automatically acquire or inherit the ability to enforce immutability on
 * otherwise immutable instances.  It is expected that the class implementer will add the necessary logic in the
 * setter methods to enforce immutability of object instances at runtime.
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.7
 */

package com.cp.common.lang;

public interface Mutable {

  public static final boolean MUTABLE = true;
  public static final boolean IMMUTABLE = false;

  /**
   * Determines whether the object instance is mutable or read-only.
   * @return a boolean value indicating whether this object is mutable instance or read-only.
   */
  public boolean isMutable();

  /**
   * Sets the internal state of an object instance at runtime to an immutable or mutable state.
   * @param mutable a boolean value indicating whether the object instance is mutable or read-only.
   */
  public void setMutable(boolean mutable);

}
