/*
 * DataAccessException.java (c) 7 December 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.12.7
 */

package com.cp.common.dao;

public class DataAccessException extends RuntimeException {

  /**
   * Creates an instance of the DataAccessException class.
   */
  public DataAccessException() {
  }

  /**
   * Creates an instance of the DataAccessException class initialized with a description of the problem.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   */
  public DataAccessException(final String message) {
    super(message);
  }

  /**
   * Creates an instance of the DataAccessException class initialized with the specified Throwable, which is also the
   * reason, or cause, for this Exception to be thrown.
   * @param cause a Throwable object indicating the the reason this DataAccessException was thrown.
   */
  public DataAccessException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates an instance of the DataAccessException class initialized with a message desribing the exceptional condition
   * and a reason, or cause, for this Exception to be thrown.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   * @param cause a Throwable object indicating the the reason this DataAccessException was thrown.
   */
  public DataAccessException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
