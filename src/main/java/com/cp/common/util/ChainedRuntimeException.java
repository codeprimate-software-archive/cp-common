/*
 * ChainedRuntimeException.java (c) 19 February 2002
 *
 * Previously named NestedRuntimeException.
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.8.13
 * @see java.lang.RuntimeException
 * @see com.cp.common.util.ChainedException
 * @since Java 1.4
 */

package com.cp.common.util;

import com.cp.common.lang.ObjectUtil;
import java.io.PrintStream;
import java.io.PrintWriter;

public class ChainedRuntimeException extends RuntimeException {

  private final Throwable cause;

  /**
   * Creates an instance of the ChainedRuntimeException class with no message or cause.
   */
  public ChainedRuntimeException() {
    cause = null;
  }

  /**
   * Creates an instance of the ChainedRuntimeException class with a description of the problem.
   * @param message a java.lang.String description of the problem.
   */
  public ChainedRuntimeException(final String message) {
    super(message);
    cause = null;
  }

  /**
   * Creates an instance of the ChainedRuntimeException class which was thrown as a result of another
   * exception specified by the cause parameter.
   * <p/>
   * NOTE: The cause paramter should be a instance of a RuntimeException.  If the source exception thrown
   * is a checked Exception, then the exception should be wrapped with an instance of the ChainedException
   * class.
   * @param cause a java.lang.Throwable object representing the reason the ChainedRuntimeException was
   * thrown.
   */
  public ChainedRuntimeException(final Throwable cause) {
    this.cause = cause;
  }

  /**
   * Creates an instance of the ChainedRuntimeException class with the specified message.  This constructor
   * signifies that the ChainedRuntimeException occurred as a result of another exception being thrown.
   * <p/>
   * NOTE: The cause paramter should be a instance of a RuntimeException.  If the source exception thrown is
   * a checked Exception, then the exception should be wrapped with an instance of the NextedException class.
   * @param message a java.lang.String description of the problem.
   * @param cause a java.lang.Throwable object representing the reason the ChainedRuntimeException was thrown.
   */
  public ChainedRuntimeException(final String message, final Throwable cause) {
    super(message);
    this.cause = cause;
  }

  /**
   * Returns the reason why the ChainedRuntimeException was thrown.
   * @return a java.lang.Throwable object representing the cause of ChainedException being thrown.
   */
  public Throwable getCause() {
    return cause;
  }

  /**
   * Returns the String representation of the ChainedRuntimeException's stack trace elements for printing to either a
   * PrintStream or PrintWriter object.
   * @return a String representation of the ChainedRuntimeException's stack trace elements.
   */
  private String getStackTraceString() {
    final StringBuffer buffer = new StringBuffer(getClass().getName());

    buffer.append(": ").append(getMessage());
    buffer.append("\n");

    for (StackTraceElement element : getStackTrace()) {
      buffer.append("\t").append(element).append("\n");
    }

    return buffer.toString();
  }

  /**
   * Prints to the PrintStream object a stack trace of the nested method calls to the source
   * of the problem, where the ChainedRuntimeException was thrown.
   * @param ps a java.io.PrintStream object in which to print the stack trace.
   */
  @Override
  public void printStackTrace(final PrintStream ps) {
    ps.println(getStackTraceString());

    if (ObjectUtil.isNotNull(getCause())) {
      ps.print("Caused by: ");
      getCause().printStackTrace(ps);
    }
  }

  /**
   * Writes to the PrintWriter object a stack trace of the nested method calls to the source
   * of the problem, where the ChainedRuntimeException was thrown.
   * @param pw a java.io.PrintWriter object in which to write the stack trace.
   */
  @Override
  public void printStackTrace(final PrintWriter pw) {
    pw.println(getStackTraceString());

    if (ObjectUtil.isNotNull(getCause())) {
      pw.print("Caused by: ");
      getCause().printStackTrace(pw);
    }
  }

}
