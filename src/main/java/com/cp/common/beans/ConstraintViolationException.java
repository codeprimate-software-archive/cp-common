/*
 * ConstraintViolationException.java (c) 23 March 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.12.31
 * @see java.lang.RuntimeException
 */

package com.cp.common.beans;

public class ConstraintViolationException extends RuntimeException {

  /**
   * Creates an instance of the ConstraintViolationException class with no message.
   */
  public ConstraintViolationException() {
  }

  /**
   * Creates an instance of the ConstraintViolationException class with a description of the problem.
   * @param message java.lang.String object description of the problem.
   */
  public ConstraintViolationException(final String message) {
    super(message);
  }

  /**
   * Creates an instance of the ConstraintViolationException class which was thrown because of another
   * exception specified by the cause parameter.
   * @param cause java.lang.Throwable object representing the reason the ConstraintViolationException
   * was thrown.
   */
  public ConstraintViolationException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates an instance of the ConstraintViolationException class with the default message.  This constructor
   * signifies that the ConstraintViolationException occurred as a result of another exception being thrown.
   * @param message java.lang.String object description of the problem.
   * @param cause java.lang.Throwable object representing the reason the ConstraintViolationException was thrown.
   */
  public ConstraintViolationException(final String message, final Throwable cause) {
    super(message,cause);
  }

}
