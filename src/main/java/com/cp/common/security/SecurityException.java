/*
 * SecurityException.java (c) 11 October 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.10.11
 * @see java.lang.Exception
 */

package com.cp.common.security;

public class SecurityException extends Exception {

  /**
   * Creates an instance of the SecurityException class.
   */
  public SecurityException() {
  }

  /**
   * Creates an instance of the SecurityException class initialized with a description of the problem.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   */
  public SecurityException(final String message) {
    super(message);
  }

  /**
   * Creates an instance of the SecurityException class initialized with the specified Throwable,
   * which is also the reason, or cause, for this Exception to be thrown.
   * @param cause a Throwable object indicating the the reason this SecurityException was thrown.
   */
  public SecurityException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates an instance of the SecurityException class initialized with a message desribing
   * the exceptional condition and a reason, or cause, for this Exception to be thrown.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   * @param cause a Throwable object indicating the the reason this SecurityException was thrown.
   */
  public SecurityException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
