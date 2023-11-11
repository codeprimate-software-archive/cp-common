/*
 * ApplicationException.java (c) 30 December 2006
 *
 * The ApplicationException class is meant to signify an exceptional condition that occurs during the
 * execution of a business process.  This Exception could indicate a violation to a business rule or
 * indicate that not all the required information is available for processing.  ApplicationException
 * and subclasses represent recoverable conditions and must be handled (caught) in code.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.12.30
 * @see com.cp.common.util.SystemException
 * @see java.lang.Exception
 */

package com.cp.common.util;

public class ApplicationException extends Exception {

  /**
   * Creates an instance of the ApplicationException class.
   */
  public ApplicationException() {
  }

  /**
   * Creates an instance of the ApplicationException class initialized with a description of the problem.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   */
  public ApplicationException(final String message) {
    super(message);
  }

  /**
   * Creates an instance of the ApplicationException class initialized with the specified Throwable,
   * which is also the reason, or cause, for this Exception to be thrown.
   * @param cause a Throwable object indicating the the reason this ApplicationException was thrown.
   */
  public ApplicationException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates an instance of the ApplicationException class initialized with a message desribing
   * the exceptional condition and a reason, or cause, for this Exception to be thrown.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   * @param cause a Throwable object indicating the the reason this ApplicationException was thrown.
   */
  public ApplicationException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
