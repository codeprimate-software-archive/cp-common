/*
 * ComposableRule.java (c) 1 July 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.29
 * @see com.cp.common.biz.rules.AbstractRule
 */

package com.cp.common.biz.rules;

import com.cp.common.lang.Assert;
import com.cp.common.lang.LogicalOperator;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.Visitor;

public class ComposableRule<CTX, REASON> extends AbstractRule<CTX, REASON> {

  protected static final LogicalOperator DEFAULT_OP = LogicalOperator.AND;

  private final LogicalOperator op;

  private final Rule<CTX, REASON> leftRule;
  private final Rule<CTX, REASON> rightRule;

  /**
   * Constructs an instance of the ComposableRule class initialized with left and right Rule instances constituting
   * the left and right nodes in the binary decision tree formed by this rule composition respectively.  In addition
   * a logical operator is specified in order to be used for the combination of the rules in a logical operation.
   * Finally, a reason is provided if the evaluation of the rules in this composition fails as well as the expected
   * outcome for the evaluation along with an indicator for whether a RuleFailureException should be thrown when at
   * least one in the rule composition fails.
   * @param leftRule the left Rule instance/node in the binary decision tree/rule composition.
   * @param rightRule the right Rule instance/node in the binary decision tree/rule compisition.
   * @param op the LogicalOperator used to combine the results of the evalution of each individual rule in the
   * rule composition.
   * @param reason an Object specifying the reason the evaluation of the rules in thie composition resulted in
   * a failure.
   * @param expectedOutcome a boolean value indicating the expected outcome for the evaluation of all the rules
   * in this rule composition.
   * @param throwExceptionOnFailure a boolean value indicating whether a RuleFailureException should be thrown if
   * the evaluation of any rule in the rule composition results in a failure (false).
   * @throws NullPointerException if either the left or right rule in the composition are null, or the reason is null.
   */
  protected ComposableRule(final Rule<CTX, REASON> leftRule,
                           final Rule<CTX, REASON> rightRule,
                           final LogicalOperator op,
                           final REASON reason,
                           final boolean expectedOutcome,
                           final boolean throwExceptionOnFailure) {
    super(reason, expectedOutcome, throwExceptionOnFailure);
    Assert.notNull(leftRule, "The rule in the left node of the binary decision tree cannot be null!");
    Assert.notNull(rightRule, "The rule in the right node of the binary decision tree cannot be null!");
    this.leftRule = leftRule;
    this.rightRule = rightRule;
    this.op = ObjectUtil.getDefaultValue(op, DEFAULT_OP);
  }

  /**
   * Factory method used to combine two Rules into a binary decision tree using the specified logical operator.
   * In addition, a reason is provided indicating why the Rules might fail when evaluated.
   * @param leftRule the left Rule instance/node in the binary decision tree/rule composition.
   * @param rightRule the right Rule instance/node in the binary decision tree/rule compisition.
   * @param op the LogicalOperator used to combine the results of the evalution of each individual rule in the
   * rule composition.
   * @param reason an Object specifying the reason the evaluation of the rules in thie composition resulted in
   * a failure.
   * @return a Rule composition consisting of the left and right Rules respectively.  If either Rule is null,
   * then the other Rule is returned.  If both Rules are null, then null is returned.
   * @see ComposableRule#compose(Rule, Rule, com.cp.common.lang.LogicalOperator, Object, boolean, boolean)
   * @see ComposableRule#compose(Object, boolean, boolean, com.cp.common.lang.LogicalOperator, Rule[])
   */
  public static <CTX, REASON> Rule<CTX, REASON> compose(final Rule<CTX, REASON> leftRule, 
                                                        final Rule<CTX, REASON> rightRule,
                                                        final LogicalOperator op,
                                                        final REASON reason)
  {
    return compose(leftRule, rightRule, op, reason, DEFAULT_EXPECTED_OUTCOME, DEFAULT_THROW_EXCEPTION_ON_FAILURE);
  }

  /**
   * Factory method used to combine two Rules into a binary decision tree using the specified logical operator.
   * In addition, a reason is provided indicating why the Rules might fail when evaluated as well as what the
   * expected outcome should be for the evaluation of the Rules.
   * @param leftRule the left Rule instance/node in the binary decision tree/rule composition.
   * @param rightRule the right Rule instance/node in the binary decision tree/rule compisition.
   * @param op the LogicalOperator used to combine the results of the evalution of each individual rule in the
   * rule composition.
   * @param reason an Object specifying the reason the evaluation of the rules in thie composition resulted in
   * a failure.
   * @param expectedOutcome a boolean value indicating the expected outcome for the evaluation of all the rules
   * in this rule composition.
   * @return a Rule composition consisting of the left and right Rules respectively.  If either Rule is null,
   * then the other Rule is returned.  If both Rules are null, then null is returned.
   * @see ComposableRule#compose(Rule, Rule, com.cp.common.lang.LogicalOperator, Object, boolean, boolean)
   * @see ComposableRule#compose(Object, boolean, boolean, com.cp.common.lang.LogicalOperator, Rule[])
   */
  public static <CTX, REASON> Rule<CTX, REASON> compose(final Rule<CTX, REASON> leftRule,
                                                        final Rule<CTX, REASON> rightRule,
                                                        final LogicalOperator op,
                                                        final REASON reason,
                                                        final boolean expectedOutcome)
  {
    return compose(leftRule, rightRule, op, reason, expectedOutcome, DEFAULT_THROW_EXCEPTION_ON_FAILURE);
  }

  /**
   * Factory method used to combine two Rules into a binary decision tree using the specified logical operator.
   * In addition, a reason is provided indicating why the Rules might fail when evaluated as well as what the
   * expected outcome should be for the evaluation of the Rules.  Finally, if either Rule in the composition fails,
   * a boolean indicator is specified to signal whether a RuleFailureException should be thrown upon completion of the
   * evaluation of the rules in this composition.
   * @param leftRule the left Rule instance/node in the binary decision tree/rule composition.
   * @param rightRule the right Rule instance/node in the binary decision tree/rule compisition.
   * @param op the LogicalOperator used to combine the results of the evalution of each individual rule in the
   * rule composition.
   * @param reason an Object specifying the reason the evaluation of the rules in thie composition resulted in
   * a failure.
   * @param expectedOutcome a boolean value indicating the expected outcome for the evaluation of all the rules
   * in this rule composition.
   * @param throwExceptionOnFailure a boolean value indicating whether a RuleFailureException should be thrown if
   * the evaluation of any rule in the rule composition results in a failure (false).
   * @return a Rule composition consisting of the left and right Rules respectively.  If either Rule is null,
   * then the other Rule is returned.  If both Rules are null, then null is returned.
   * @see ComposableRule#compose(Object, boolean, boolean, com.cp.common.lang.LogicalOperator, Rule[])
   */
  public static <CTX, REASON> Rule<CTX, REASON> compose(final Rule<CTX, REASON> leftRule,
                                                        final Rule<CTX, REASON> rightRule,
                                                        final LogicalOperator op,
                                                        final REASON reason,
                                                        final boolean expectedOutcome,
                                                        final boolean throwExceptionOnFailure)
  {
    return (ObjectUtil.isNull(leftRule) ? rightRule : (ObjectUtil.isNull(rightRule) ? leftRule
      : new ComposableRule<CTX, REASON>(leftRule, rightRule, op, reason, expectedOutcome, throwExceptionOnFailure)));
  }

  /**
   * Factory method used to combine a collection of Rules into a binary decision tree using the specified
   * logical operator. In addition, a reason is provided indicating why the Rules in the rule composition may fail
   * upon evaluation.
   * @param reason an Object specifying the reason the evaluation of the rules in thie composition resulted in
   * a failure.
   * @param op the LogicalOperator used to combine the results of the evalution of each individual rule in the
   * rule composition.
   * @param rules an array of Rule instances to combine into the rule composition represented as a binary decision tree.
   * @return a Rule composition consisting of the rules in the object array combined using the specified
   * logical operator.  If the Rule array consists of only one Rule, then that Rule is returned.  If the Rule array
   * is empty, then null is returned.
   * @see ComposableRule#compose(Object, boolean, boolean, com.cp.common.lang.LogicalOperator, Rule[])
   * @see ComposableRule#compose(Rule, Rule, com.cp.common.lang.LogicalOperator, Object, boolean, boolean)
   */
  public static <CTX, REASON> Rule<CTX, REASON> compose(final REASON reason,
                                                        final LogicalOperator op,
                                                        final Rule<CTX, REASON>... rules)
  {
    return compose(reason, DEFAULT_EXPECTED_OUTCOME, DEFAULT_THROW_EXCEPTION_ON_FAILURE, op, rules);
  }

  /**
   * Factory method used to combine a collection of Rules into a binary decision tree using the specified
   * logical operator. In addition, a reason is provided indicating why the Rules in the rule composition may fail
   * upon evaluation as well as the expected outcome of evaluating all rules in the compositio.
   * @param reason an Object specifying the reason the evaluation of the rules in thie composition resulted in
   * a failure.
   * @param expectedOutcome a boolean value indicating the expected outcome for the evaluation of all the rules
   * in this rule composition.
   * @param op the LogicalOperator used to combine the results of the evalution of each individual rule in the
   * rule composition.
   * @param rules an array of Rule instances to combine into the rule composition represented as a binary decision tree.
   * @return a Rule composition consisting of the rules in the object array combined using the specified
   * logical operator.  If the Rule array consists of only one Rule, then that Rule is returned.  If the Rule array
   * is empty, then null is returned.
   * @see ComposableRule#compose(Object, boolean, boolean, com.cp.common.lang.LogicalOperator, Rule[])
   * @see ComposableRule#compose(Rule, Rule, com.cp.common.lang.LogicalOperator, Object, boolean, boolean)
   */
  public static <CTX, REASON> Rule<CTX, REASON> compose(final REASON reason,
                                                        final boolean expectedOutcome,
                                                        final LogicalOperator op,
                                                        final Rule<CTX, REASON>... rules)
  {
    return compose(reason, expectedOutcome, DEFAULT_THROW_EXCEPTION_ON_FAILURE, op, rules);
  }

  /**
   * Factory method used to combine a collection of Rules into a binary decision tree using the specified
   * logical operator. In addition, a reason is provided indicating why the Rules in the rule composition may fail
   * upon evaluation as well as the expected outcome of evaluating all rules in the compositio.  Finally, if either
   * Rule in the composition fails, a boolean indicator is specified to signal whether a RuleFailureException should
   * be thrown upon completion of the evaluation of the rules in this composition.
   * @param reason an Object specifying the reason the evaluation of the rules in thie composition resulted in
   * a failure.
   * @param expectedOutcome a boolean value indicating the expected outcome for the evaluation of all the rules
   * in this rule composition.
   * @param throwExceptionOnFailure a boolean value indicating whether a RuleFailureException should be thrown if
   * the evaluation of any rule in the rule composition results in a failure (false).
   * @param op the LogicalOperator used to combine the results of the evalution of each individual rule in the
   * rule composition.
   * @param rules an array of Rule instances to combine into the rule composition represented as a binary decision tree.
   * @return a Rule composition consisting of the rules in the object array combined using the specified
   * logical operator.  If the Rule array consists of only one Rule, then that Rule is returned.  If the Rule array
   * is empty, then null is returned.
   * @see ComposableRule#compose(Rule, Rule, com.cp.common.lang.LogicalOperator, Object, boolean, boolean)
   */
  public static <CTX, REASON> Rule<CTX, REASON> compose(final REASON reason,
                                                        final boolean expectedOutcome,
                                                        final boolean throwExceptionOnFailure,
                                                        final LogicalOperator op,
                                                        final Rule<CTX, REASON>... rules)
  {
    Rule<CTX, REASON> currentRule = null;

    for (final Rule<CTX, REASON> rule : rules) {
      currentRule = compose(currentRule, rule, op, reason, expectedOutcome, throwExceptionOnFailure);
    }

    return currentRule;
  }

  /**
   * Gets the left node Rule instance in the binary decision tree of the rule composition.
   * @return a Rule object referring to the left node in the binary decision tree, rule composition.
   * @see com.cp.common.biz.rules.ComposableRule#getRightRule()
   */
  protected Rule<CTX, REASON> getLeftRule() {
    return leftRule;
  }

  /**
   * Gets the LogicalOperator instanse used to combine both trhe left and right Rules in the rule composition
   * during the evaluation process.
   * @return a LogicalOperator used to combine the evaluations of both the left and right Rules in the rule composition.
   */
  protected LogicalOperator getOp() {
    return op;
  }

  /**
   * Gets the right node Rule instance in the binary decision tree of the rule composition.
   * @return a Rule object referring to the right node in the binary decision tree, rule composition.
   * @see com.cp.common.biz.rules.ComposableRule#getLeftRule()
   */
  protected Rule<CTX, REASON> getRightRule() {
    return rightRule;
  }

  /**
   * Accepts visitation by a Visitor object visiting a collection, composition or configuration of rules in order to
   * apply some operation to the rules.
   * @param visitor a Visitor object applying some operation to the rules in a collection, composition or configuration.
   */
  @Override
  public void accept(final Visitor visitor) {
    visitor.visit(this);
    visitor.visit(getLeftRule());
    visitor.visit(getRightRule());
  }

  /**
   * Performs the evaluation of both the left and right Rules in the rule composition combined using the
   * logical operator of this rule composition.
   * @param context the Object being evaluated by the rules of this rule composition.
   * @return a boolean value indicating whether the evaluation of the left and right Rules in the rule composition as
   * combined by the logical operation were successful or not.
   */
  @Override
  protected boolean doEvaluate(final CTX context) {
    return getOp().op(getLeftRule().evaluate(context), getRightRule().evaluate(context));
  }

}
