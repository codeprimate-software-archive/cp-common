/*
 * IllegalUseOfAnnotationException.java (c) 25 December 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.12.31
 * @see java.lang.RuntimeException
 */

package com.cp.common.beans.annotation;

public class IllegalUseOfAnnotationException extends RuntimeException {

  /**
   * Creates an instance of the IllegalUseOfAnnotationException class.
   */
  public IllegalUseOfAnnotationException() {
  }

  /**
   * Creates an instance of the IllegalUseOfAnnotationException class initialized with a description of the problem.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   */
  public IllegalUseOfAnnotationException(final String message) {
    super(message);
  }

  /**
   * Creates an instance of the IllegalUseOfAnnotationException class initialized with the specified Throwable,
   * which is also the reason, or cause, for this Exception to be thrown.
   * @param cause a Throwable object indicating the the reason this IllegalUseOfAnnotationException was thrown.
   */
  public IllegalUseOfAnnotationException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates an instance of the IllegalUseOfAnnotationException class initialized with a message desribing
   * the exceptional condition and a reason, or cause, for this Exception to be thrown.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   * @param cause a Throwable object indicating the the reason this IllegalUseOfAnnotationException was thrown.
   */
  public IllegalUseOfAnnotationException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
