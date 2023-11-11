/*
 * ObjectImmutableException.java (c) 17 August 2003
 *
 * This Exception class specifies a runtime exception if a programmer tries to modify a immutable object.
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.12.31
 * @see java.lang.RuntimeException
 * @see com.cp.common.lang.Mutable
 */

package com.cp.common.lang;

// TODO determine whether this class should be a checked or unchecked Exception!
public class ObjectImmutableException extends RuntimeException {

  /**
   * Creates an instance of the ObjectImmutableException class.
   */
  public ObjectImmutableException() {
  }

  /**
   * Creates an instance of the ObjectImmutableException class initialized with a description of the problem.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   */
  public ObjectImmutableException(final String message) {
    super(message);
  }

  /**
   * Creates an instance of the ObjectImmutableException class initialized with the specified Throwable,
   * which is also the reason, or cause, for this Exception to be thrown.
   * @param cause a Throwable object indicating the the reason this ObjectImmutableException was thrown.
   */
  public ObjectImmutableException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates an instance of the ObjectImmutableException class initialized with a message desribing
   * the exceptional condition and a reason, or cause, for this Exception to be thrown.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   * @param cause a Throwable object indicating the the reason this ObjectImmutableException was thrown.
   */
  public ObjectImmutableException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
