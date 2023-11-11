/*
 * MalformedAnnotationDeclarationException.java (c) 7 September 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.9.7
 * @see java.lang.RuntimeException
 */

package com.cp.common.beans.annotation;

public class MalformedAnnotationDeclarationException extends RuntimeException {

  /**
   * Creates an instance of the MalformedAnnotationDeclarationException class.
   */
  public MalformedAnnotationDeclarationException() {
  }

  /**
   * Creates an instance of the MalformedAnnotationDeclarationException class initialized with a description of the
   * problem.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   */
  public MalformedAnnotationDeclarationException(final String message) {
    super(message);
  }

  /**
   * Creates an instance of the MalformedAnnotationDeclarationException class initialized with the specified Throwable,
   * which is also the reason, or cause, for this Exception to be thrown.
   * @param cause a Throwable object indicating the the reason this MalformedAnnotationDeclarationException was thrown.
   */
  public MalformedAnnotationDeclarationException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates an instance of the MalformedAnnotationDeclarationException class initialized with a message desribing the
   * exceptional condition and a reason, or cause, for this Exception to be thrown.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   * @param cause a Throwable object indicating the the reason this MalformedAnnotationDeclarationException was thrown.
   */
  public MalformedAnnotationDeclarationException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
