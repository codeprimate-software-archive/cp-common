/*
 * ExceptionUtil.java (c) 2 November 2008
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John Blum
 * @version 2010.4.7
 * @see java.lang.Throwable
 * @see java.lang.reflect.InvocationTargetException
 */

package com.cp.common.lang;

import java.lang.reflect.InvocationTargetException;

public final class ExceptionUtil {

  /**
   * Default private constructor to prevent instantiation of the ExceptionUtil utility class.
   */
  private ExceptionUtil() {
  }

  /**
   * Gets the underlying cause of an InvocationTargetException if the Throwable is indeed an instance of the
   * InvocationTargetException class, otherwise this method will just return the Throwable parameter argument.
   * @param t the Throwable being evaluated in order to determine if it is an instance of InvocationTargetException.
   * @return the cause of the Throwable if it is an instance of InvocationTargetException, otherwise returns Throwable.
   */
  public static Throwable getCauseIfInvocationTargetException(final Throwable t) {
    return (t instanceof InvocationTargetException ? t.getCause() : t);
  }

  /**
   * Gets the root cause of a potential chain of Throwable objects.
   * @param t the Throwable object constituting the outermost link in a chain of Throwable objects.
   * @return the underlying root cause of the Throwable object passed into this method.
   * @throws NullPointerException if the Throwable object is null.
   */
  public static Throwable getRootCause(Throwable t) {
    Assert.notNull(t, "The Throwable object cannot be null!");

    while (ObjectUtil.isNotNull(t.getCause())) {
      t = t.getCause();
    }

    return t;
  }

}
