/*
 * NoSuchPropertyException.java (c) 15 December 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.12.31
 * @see java.lang.NoSuchFieldException
 * @see java.lang.NoSuchMethodException
 * @see java.lang.RuntimeException
 */

package com.cp.common.beans.util;

public class NoSuchPropertyException extends RuntimeException {

  /**
   * Creates an instance of the NoSuchPropertyException class.
   */
  public NoSuchPropertyException() {
  }

  /**
   * Creates an instance of the NoSuchPropertyException class initialized with a description of the problem.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   */
  public NoSuchPropertyException(final String message) {
    super(message);
  }

  /**
   * Creates an instance of the NoSuchPropertyException class initialized with the specified Throwable,
   * which is also the reason, or cause, for this Exception to be thrown.
   * @param cause a Throwable object indicating the the reason this NoSuchPropertyException was thrown.
   */
  public NoSuchPropertyException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates an instance of the NoSuchPropertyException class initialized with a message desribing
   * the exceptional condition and a reason, or cause, for this Exception to be thrown.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   * @param cause a Throwable object indicating the the reason this NoSuchPropertyException was thrown.
   */
  public NoSuchPropertyException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
