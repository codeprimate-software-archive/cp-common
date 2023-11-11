/*
 * RuleEvaluationException.java (c) 12 June 2009
 *
 * The RuleEvaluationException indicates a unrecoverable error condition has occurred during the evaluation
 * of a rule, and that the actual outcome of the evaluation could not be determined. 
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.17
 * @see java.lang.RuntimeException
 */

package com.cp.common.biz.rules;

public class RuleEvaluationException extends RuntimeException {

  /**
   * Creates an instance of the RuleException class.
   */
  public RuleEvaluationException() {
  }

  /**
   * Creates an instance of the RuleException class initialized with a description of the problem.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   */
  public RuleEvaluationException(final String message) {
    super(message);
  }

  /**
   * Creates an instance of the RuleException class initialized with the specified Throwable, which is also the reason,
   * or cause, for this Exception to be thrown.
   * @param cause a Throwable object indicating the the reason this RuleException was thrown.
   */
  public RuleEvaluationException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates an instance of the RuleException class initialized with a message desribing the exceptional condition and a
   * reason, or cause, for this Exception to be thrown.
   * @param message a String value describing the nature of the problem and reason this Exception was thrown.
   * @param cause a Throwable object indicating the the reason this RuleException was thrown.
   */
  public RuleEvaluationException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
