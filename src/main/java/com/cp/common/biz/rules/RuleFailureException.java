/*
 * RuleFailureException.java (c) 27 June 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.6.27
 * @see java.lang.RuntimeException
 */

package com.cp.common.biz.rules;

public class RuleFailureException extends RuntimeException {

  /**
   * Creates an instance of the FailedRuleException class.
   */
  public RuleFailureException() {
  }

  /**
   * Creates an instance of the FailedRuleException class initialized with a description of the problem.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   */
  public RuleFailureException(final String message) {
    super(message);
  }

  /**
   * Creates an instance of the FailedRuleException class initialized with the specified Throwable, which is also the
   * reason, or cause, for this Exception to be thrown.
   * @param cause a Throwable object indicating the the reason this FailedRuleException was thrown.
   */
  public RuleFailureException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates an instance of the FailedRuleException class initialized with a message desribing the exceptional condition
   * and a reason, or cause, for this Exception to be thrown.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   * @param cause a Throwable object indicating the the reason this FailedRuleException was thrown.
   */
  public RuleFailureException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
