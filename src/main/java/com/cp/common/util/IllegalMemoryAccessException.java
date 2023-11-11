/*
 * IllegalMemoryAccessException.java (c) 22 October 2003
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.12.31
 * @see java.lang.RuntimeException
 */

package com.cp.common.util;

public class IllegalMemoryAccessException extends RuntimeException {

  /**
   * Creates an instance of the IllegalMemoryAccessException class.
   */
  public IllegalMemoryAccessException() {
  }

  /**
   * Creates an instance of the IllegalMemoryAccessException class initialized with a description of the problem.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   */
  public IllegalMemoryAccessException(final String message) {
    super(message);
  }

  /**
   * Creates an instance of the IllegalMemoryAccessException class initialized with the specified Throwable,
   * which is also the reason, or cause, for this Exception to be thrown.
   * @param cause a Throwable object indicating the the reason this IllegalMemoryAccessException was thrown.
   */
  public IllegalMemoryAccessException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates an instance of the IllegalMemoryAccessException class initialized with a message desribing
   * the exceptional condition and a reason, or cause, for this Exception to be thrown.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   * @param cause a Throwable object indicating the the reason this IllegalMemoryAccessException was thrown.
   */
  public IllegalMemoryAccessException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
