/*
 * WriteOnlyException.java (c) 7 November 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.12.31
 * @see java.lang.RuntimeException
 * @see com.cp.common.util.ReadOnlyException
 */

package com.cp.common.util;

public class WriteOnlyException extends RuntimeException {

  /**
   * Creates an instance of the WriteOnlyException class.
   */
  public WriteOnlyException() {
  }

  /**
   * Creates an instance of the WriteOnlyException class initialized with a description of the problem.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   */
  public WriteOnlyException(final String message) {
    super(message);
  }

  /**
   * Creates an instance of the WriteOnlyException class initialized with the specified Throwable,
   * which is also the reason, or cause, for this Exception to be thrown.
   * @param cause a Throwable object indicating the the reason this WriteOnlyException was thrown.
   */
  public WriteOnlyException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates an instance of the WriteOnlyException class initialized with a message desribing
   * the exceptional condition and a reason, or cause, for this Exception to be thrown.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   * @param cause a Throwable object indicating the the reason this WriteOnlyException was thrown.
   */
  public WriteOnlyException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
