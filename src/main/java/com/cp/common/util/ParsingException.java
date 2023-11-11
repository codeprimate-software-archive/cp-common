/*
 * ParsingException.java (c) 26 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.12.26
 * @see java.lang.RuntimeException
 */

package com.cp.common.util;

public class ParsingException extends RuntimeException {

  /**
   * Creates an instance of the ParsingException class.
   */
  public ParsingException() {
  }

  /**
   * Creates an instance of the ParsingException class initialized with a description of the problem.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   */
  public ParsingException(final String message) {
    super(message);
  }

  /**
   * Creates an instance of the ParsingException class initialized with the specified Throwable,
   * which is also the reason, or cause, for this Exception to be thrown.
   * @param cause a Throwable object indicating the the reason this ParsingException was thrown.
   */
  public ParsingException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates an instance of the ParsingException class initialized with a message desribing
   * the exceptional condition and a reason, or cause, for this Exception to be thrown.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   * @param cause a Throwable object indicating the the reason this ParsingException was thrown.
   */
  public ParsingException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
