/*
 * BeanNotFoundException.java (c) 18 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.12.18
 * @see java.lang.RuntimeException
 */

package com.cp.common.beans.factory;

public class BeanNotFoundException extends RuntimeException {

  /**
   * Creates an instance of the BeanNotFoundException class.
   */
  public BeanNotFoundException() {
  }

  /**
   * Creates an instance of the BeanNotFoundException class initialized with a description of the problem.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   */
  public BeanNotFoundException(final String message) {
    super(message);
  }

  /**
   * Creates an instance of the BeanNotFoundException class initialized with the specified Throwable,
   * which is also the reason, or cause, for this Exception to be thrown.
   * @param cause a Throwable object indicating the the reason this BeanNotFoundException was thrown.
   */
  public BeanNotFoundException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates an instance of the BeanNotFoundException class initialized with a message desribing
   * the exceptional condition and a reason, or cause, for this Exception to be thrown.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   * @param cause a Throwable object indicating the the reason this BeanNotFoundException was thrown.
   */
  public BeanNotFoundException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
