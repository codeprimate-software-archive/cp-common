/*
 * ObjectNotFoundException.java (c) 17 August 2003
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.12.31
 * @see java.lang.ClassNotFoundException
 * @see java.lang.Exception
 */

package com.cp.common.lang;

public class ObjectNotFoundException extends Exception {

  /**
   * Creates an instance of the ObjectNotFoundException class.
   */
  public ObjectNotFoundException() {
  }

  /**
   * Creates an instance of the ObjectNotFoundException class initialized with a description of the problem.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   */
  public ObjectNotFoundException(final String message) {
    super(message);
  }

  /**
   * Creates an instance of the ObjectNotFoundException class initialized with the specified Throwable,
   * which is also the reason, or cause, for this Exception to be thrown.
   * @param cause a Throwable object indicating the the reason this ObjectNotFoundException was thrown.
   */
  public ObjectNotFoundException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates an instance of the ObjectNotFoundException class initialized with a message desribing
   * the exceptional condition and a reason, or cause, for this Exception to be thrown.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   * @param cause a Throwable object indicating the the reason this ObjectNotFoundException was thrown.
   */
  public ObjectNotFoundException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
