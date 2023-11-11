/*
 * FailedToWritePropertyException.java (c) 2 March 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.3.3
 * @see java.lang.RuntimeException
 */

package com.cp.common.beans.util;

public class FailedToWritePropertyException extends RuntimeException {

  /**
   * Creates an instance of the FailedToWritePropertyException class.
   */
  public FailedToWritePropertyException() {
  }

  /**
   * Creates an instance of the FailedToWritePropertyException class initialized with a description of the problem.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   */
  public FailedToWritePropertyException(final String message) {
    super(message);
  }

  /**
   * Creates an instance of the FailedToWritePropertyException class initialized with the specified Throwable,
   * which is also the reason, or cause, for this Exception to be thrown.
   * @param cause a Throwable object indicating the the reason this FailedToWritePropertyException was thrown.
   */
  public FailedToWritePropertyException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates an instance of the FailedToWritePropertyException class initialized with a message desribing
   * the exceptional condition and a reason, or cause, for this Exception to be thrown.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   * @param cause a Throwable object indicating the the reason this FailedToWritePropertyException was thrown.
   */
  public FailedToWritePropertyException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
