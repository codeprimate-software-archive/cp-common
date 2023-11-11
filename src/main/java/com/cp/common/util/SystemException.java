/*
 * SystemException.java (c) 30 December 2006
 *
 * The SystemException is meant to signify an exceptional condition that occurs during the normal execution
 * of application logic that is out of the control of the programmer.  The RuntimeException may represent a
 * condition where a service is unavailable or a resource is depleted.  SystemException and subclasses may
 * be handled but usually represent external conditions outside of the control of the application.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.12.30
 * @see com.cp.common.util.ApplicationException
 * @see java.lang.RuntimeException
 */

package com.cp.common.util;

public class SystemException extends RuntimeException {

  /**
   * Creates an instance of the SystemException class.
   */
  public SystemException() {
  }

  /**
   * Creates an instance of the SystemException class initialized with a description of the problem.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   */
  public SystemException(final String message) {
    super(message);
  }

  /**
   * Creates an instance of the SystemException class initialized with the specified Throwable,
   * which is also the reason, or cause, for this Exception to be thrown.
   * @param cause a Throwable object indicating the the reason this SystemException was thrown.
   */
  public SystemException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates an instance of the SystemException class initialized with a message desribing
   * the exceptional condition and a reason, or cause, for this Exception to be thrown.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   * @param cause a Throwable object indicating the the reason this SystemException was thrown.
   */
  public SystemException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
