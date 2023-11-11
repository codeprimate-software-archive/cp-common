/*
 * DefaultRule.java (c) 1 July 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.28
 * @see com.cp.common.biz.rules.AbstractRule
 */

package com.cp.common.biz.rules;

public final class DefaultRule extends AbstractRule<Object, String> {

  public static final String DEFAULT_REASON = "The actual outcome (%ACTUAL_OUTCOME%) did not match expected outcome (%EXPECTED_OUTCOME%)!";

  private final boolean actualOutcome;

  /**
   * Instantiates an instance of the DefaultRule class initialized with a reason for indicating why when the evaluation
   * of this rule fails.
   * @param reason an Object indicating the reason this rule failed (meaning the actual outcome of evaluating this rule
   * did not match the expected outcome).
   * @throws NullPointerException if the specified reason is null.
   */
  public DefaultRule(final String reason) {
    this(reason, DEFAULT_EXPECTED_OUTCOME, DEFAULT_EXPECTED_OUTCOME, DEFAULT_THROW_EXCEPTION_ON_FAILURE);
  }

  /**
   * Instantiates an instance of the DefaultRule class initialized with a reason for indicating why when the evaluation
   * of this rule fails.  In addition, the expected outcome (pass/true or fail/false) is specified to indicate the
   * expected result upon evaluating this rule.
   * @param reason an Object indicating the reason this rule failed (meaning the actual outcome of evaluating this rule
   * did not match the expected outcome).
   * @param expectedOutcome a boolean value indicating whether the actual outcome of evaluating this rule should result
   * in passing or failing.
   * @throws NullPointerException if the specified reason is null.
   */
  public DefaultRule(final String reason, final boolean expectedOutcome) {
    this(reason, expectedOutcome, expectedOutcome, DEFAULT_THROW_EXCEPTION_ON_FAILURE);
  }

  /**
   * Instantiates an instance of the DefaultRule class initialized with a reason for indicating why when the evaluation
   * of this rule fails.  In addition, the expected outcome (pass/true or fail/false) is specified to indicate the
   * expected result upon evaluating this rule.  Finally, a boolean value indicating whether a RuleFailureException
   * should be thrown if the evaluation of this rule results in failure.
   * @param reason an Object indicating the reason this rule failed (meaning the actual outcome of evaluating this rule
   * did not match the expected outcome).
   * @param expectedOutcome a boolean value indicating whether the actual outcome of evaluating this rule should result
   * in passing or failing.
   * @param throwExceptionOnFailure a boolean value indicating whether an RuleFailureException should be thrown if the
   * the rule fails upon evaluation.
   * @throws NullPointerException if the specified reason is null.
   */
  public DefaultRule(final String reason, final boolean expectedOutcome, final boolean throwExceptionOnFailure) {
    this(reason, expectedOutcome, expectedOutcome, throwExceptionOnFailure);
  }

  /**
   * Instantiates an instance of the DefaultRule class initialized with a reason for indicating why when the evaluation
   * of this rule fails.  In addition, the expected outcome (pass/true or fail/false) is specified to indicate the
   * expected result upon evaluating this rule.  Finally, a boolean value indicating whether a RuleFailureException
   * should be thrown if the evaluation of this rule results in failure.
   * @param reason an Object indicating the reason this rule failed (meaning the actual outcome of evaluating this rule
   * did not match the expected outcome).
   * @param actualOutcome a boolean value indicating the result of calling doEvaluate.
   * @param expectedOutcome a boolean value indicating whether the actual outcome of evaluating this rule should result
   * in passing or failing.
   * @param throwExceptionOnFailure a boolean value indicating whether an RuleFailureException should be thrown if the
   * the rule fails upon evaluation.
   * @throws NullPointerException if the specified reason is null.
   */
  public DefaultRule(final String reason,
                     final boolean actualOutcome,
                     final boolean expectedOutcome,
                     final boolean throwExceptionOnFailure)
  {
    super(reason, expectedOutcome, throwExceptionOnFailure);
    this.actualOutcome = actualOutcome;
  }

  /**
   * Gets the reason why the rule failed (where the actual outcome after evaluation did not match the expected outcome).
   * The rule returns null if the rule passed.
   * @return a generic Object representing the reason this rule failed or null if the rule passed.
   * @throws IllegalStateException if the rule has not yet been evaluated.
   */
  @Override
  public String getReason() {
    String reason = super.getReason();

    if (DEFAULT_REASON.equals(reason)) {
      reason = reason.replaceAll("%ACTUAL_OUTCOME%", String.valueOf(getActualOutcome()));
      reason = reason.replaceAll("%EXPECTED_OUTCOME%", String.valueOf(getExpectedOutcome()));
    }

    return reason;
  }

  /**
   * Method to specify the actual criteria of this rule as declared by the subclass.
   * @param context the RuleContext containing the information to be evaluated by this rule and it's criteria.
   * @return a boolean value indicating whether the information contained within the RuleContext satisfies the criteria
   * of this rule upon being evaluated.
   * @throws Exception if the subclass implementation of doEvaluate encounters a problem.
   */
  @Override
  protected final boolean doEvaluate(final Object context) {
    return actualOutcome;
  }

}
