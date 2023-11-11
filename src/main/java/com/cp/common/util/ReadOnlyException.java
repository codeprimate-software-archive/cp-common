/*
 * ReadOnlyException.java (c) 7 November 2004
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.12.31
 * @see java.lang.RuntimeException
 * @see com.cp.common.util.WriteOnlyException
 */

package com.cp.common.util;

public class ReadOnlyException extends RuntimeException {

  /**
   * Creates an instance of the ReadOnlyException class.
   */
  public ReadOnlyException() {
  }

  /**
   * Creates an instance of the ReadOnlyException class initialized with a description of the problem.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   */
  public ReadOnlyException(final String message) {
    super(message);
  }

  /**
   * Creates an instance of the ReadOnlyException class initialized with the specified Throwable,
   * which is also the reason, or cause, for this Exception to be thrown.
   * @param cause a Throwable object indicating the the reason this ReadOnlyException was thrown.
   */
  public ReadOnlyException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates an instance of the ReadOnlyException class initialized with a message desribing
   * the exceptional condition and a reason, or cause, for this Exception to be thrown.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   * @param cause a Throwable object indicating the the reason this ReadOnlyException was thrown.
   */
  public ReadOnlyException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
