/*
 * RelationalRule.java (c) 11 July 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.14
 * @see com.cp.common.biz.rules.AbstractRule
 */

package com.cp.common.biz.rules;

import com.cp.common.lang.Assert;
import com.cp.common.lang.RelationalOperator;

public class RelationalRule<VALUE extends Comparable<VALUE>, REASON> extends AbstractRule<VALUE, REASON> {

  private final RelationalOperator<VALUE> op;

  /**
   * Constructs an instance of the RelationalRule class initialized with a relational operator and reason for
   * indicating why when the evaluation of this rule fails.
   * @param op the RelationalOperator instance used in the evaluation of the criteria of this rule.
   * @param reason an Object indicating the reason this rule failed (meaning the actual outcome of evaluating this rule
   * did not match the expected outcome).
   * @throws NullPointerException if the specified relational operator or reason is null.
   */
  public RelationalRule(final RelationalOperator<VALUE> op, final REASON reason) {
    this(op, reason, DEFAULT_EXPECTED_OUTCOME, DEFAULT_THROW_EXCEPTION_ON_FAILURE);
  }

  /**
   * Constructs an instance of the RelationalRule class initialized with a relational operator and reason for
   * indicating why when the evaluation of this rule fails.  In addition, the expected outcome (pass/true or
   * fail/false) is specified to indicate the expected result upon evaluating this rule. 
   * @param op the RelationalOperator instance used in the evaluation of the criteria of this rule.
   * @param reason an Object indicating the reason this rule failed (meaning the actual outcome of evaluating this rule
   * did not match the expected outcome).
   * @param expectedOutcome a boolean value indicating whether the actual outcome of evaluating this rule should result
   * in passing or failing.
   * @throws NullPointerException if the specified relational operator or reason is null.
   */
  public RelationalRule(final RelationalOperator<VALUE> op, final REASON reason, final boolean expectedOutcome) {
    this(op, reason, expectedOutcome, DEFAULT_THROW_EXCEPTION_ON_FAILURE);
  }

  /**
   * Constructs an instance of the RelationalRule class initialized with a relational operator and reason for
   * indicating why when the evaluation of this rule fails.  In addition, the expected outcome (pass/true or
   * fail/false) is specified to indicate the expected result upon evaluating this rule.  Finally, a boolean value
   * indicating whether a RuleFailureException should be thrown if the evaluation of this rule results in failure.
   * @param op the RelationalOperator instance used in the evaluation of the criteria of this rule.
   * @param reason an Object indicating the reason this rule failed (meaning the actual outcome of evaluating this rule
   * did not match the expected outcome).
   * @param expectedOutcome a boolean value indicating whether the actual outcome of evaluating this rule should result
   * in passing or failing.
   * @param throwExceptionOnFailure a boolean value indicating whether an RuleFailureException should be thrown if the
   * the rule fails upon evaluation.
   * @throws NullPointerException if the specified relational operator or reason is null.
   */
  public RelationalRule(final RelationalOperator<VALUE> op,
                        final REASON reason,
                        final boolean expectedOutcome,
                        final boolean throwExceptionOnFailure)
  {
    super(reason, expectedOutcome, throwExceptionOnFailure);
    Assert.notNull(op, "The relational operator for this Rule cannot be null!");
    this.op = op;
  }

  /**
   * Gets the relational operator used in the evaluation of the criteria of this rule.
   * @return a RelationalOperator used in the evaluation of the criteria of this rule.
   */
  protected RelationalOperator<VALUE> getOp() {
    return op;
  }

  /**
   * Performs the evaluation of the conditions of this rule against the specified value to determine whether the
   * specified value meets the criteria of this rule.
   * @param context the Object value used in the evaluation of this rule.
   * @return a boolean value indicating whether the specified Object value satisified the conditions/criteria during
   * the evaluation of this rule.
   * @throws Exception if an error occurs during the evaluation of this rule.
   */
  @Override
  protected boolean doEvaluate(final VALUE context) throws Exception {
    return getOp().accept(context);
  }

}
