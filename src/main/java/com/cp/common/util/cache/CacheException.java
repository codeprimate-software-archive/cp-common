/*
 * CacheException.java (c) 19 January 2004
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.12.31
 * @see java.lang.Exception
 */

package com.cp.common.util.cache;

public class CacheException extends Exception {

  /**
   * Creates an instance of the CacheException class.
   */
  public CacheException() {
  }

  /**
   * Creates an instance of the CacheException class initialized with a description of the problem.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   */
  public CacheException(final String message) {
    super(message);
  }

  /**
   * Creates an instance of the CacheException class initialized with the specified Throwable,
   * which is also the reason, or cause, for this Exception to be thrown.
   * @param cause a Throwable object indicating the the reason this CacheException was thrown.
   */
  public CacheException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates an instance of the CacheException class initialized with a message desribing
   * the exceptional condition and a reason, or cause, for this Exception to be thrown.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   * @param cause a Throwable object indicating the the reason this CacheException was thrown.
   */
  public CacheException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
