/*
 * BeanInstantiationException.java (c) 3 March 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.3.3
 */

package com.cp.common.beans;

public class BeanInstantiationException extends RuntimeException {

  /**
   * Creates an instance of the BeanInstantiationException class with no message.
   */
  public BeanInstantiationException() {
  }

  /**
   * Creates an instance of the BeanInstantiationException class with a description of the problem.
   * @param message java.lang.String object description of the problem.
   */
  public BeanInstantiationException(final String message) {
    super(message);
  }

  /**
   * Creates an instance of the BeanInstantiationException class which was thrown because of another
   * exception specified by the cause parameter.
   * @param cause java.lang.Throwable object representing the reason the BeanInstantiationException
   * was thrown.
   */
  public BeanInstantiationException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates an instance of the BeanInstantiationException class with the default message.  This constructor
   * signifies that the BeanInstantiationException occurred as a result of another exception being thrown.
   * @param message java.lang.String object description of the problem.
   * @param cause java.lang.Throwable object representing the reason the BeanInstantiationException was thrown.
   */
  public BeanInstantiationException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
