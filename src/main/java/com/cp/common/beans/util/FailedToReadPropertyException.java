/*
 * FailedToReadPropertyException.java (c) 2 March 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.3.3
 * @see java.lang.RuntimeException
 */

package com.cp.common.beans.util;

public class FailedToReadPropertyException extends RuntimeException {

  /**
   * Creates an instance of the FailedToReadPropertyException class with no message.
   */
  public FailedToReadPropertyException() {
  }

  /**
   * Creates an instance of the FailedToReadPropertyException class with a description of the problem.
   * @param message java.lang.String object description of the problem.
   */
  public FailedToReadPropertyException(final String message) {
    super(message);
  }

  /**
   * Creates an instance of the FailedToReadPropertyException class which was thrown because of another
   * exception specified by the cause parameter.
   * @param cause java.lang.Throwable object representing the reason the FailedToReadPropertyException
   * was thrown.
   */
  public FailedToReadPropertyException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates an instance of the FailedToReadPropertyException class with the default message.  This constructor
   * signifies that the FailedToReadPropertyException occurred as a result of another exception being thrown.
   * @param message java.lang.String object description of the problem.
   * @param cause java.lang.Throwable object representing the reason the FailedToReadPropertyException was thrown.
   */
  public FailedToReadPropertyException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
