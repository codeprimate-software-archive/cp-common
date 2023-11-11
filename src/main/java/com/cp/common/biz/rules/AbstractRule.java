/*
 * AbstractRule.java (c) 27 June 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.29
 * @see com.cp.common.biz.rules.ComposableRule
 * @see com.cp.common.biz.rules.DefaultRule
 * @see com.cp.common.biz.rules.RelationalRule
 * @see com.cp.common.biz.rules.Rule
 * @see com.cp.common.biz.rules.RuleCallback
 * @see com.cp.common.biz.rules.RuleContext
 */

package com.cp.common.biz.rules;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.Visitor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractRule<CTX, REASON> implements Rule<CTX, REASON> {

  protected static final boolean DEFAULT_EXPECTED_OUTCOME = true;
  protected static final boolean DEFAULT_THROW_EXCEPTION_ON_FAILURE = false;

  private boolean actualOutcome = !DEFAULT_EXPECTED_OUTCOME;
  private boolean evaluated = false;
  private final boolean expectedOutcome;
  private final boolean throwExceptionOnFailure;

  protected final Log logger = LogFactory.getLog(getClass());

  private Long id;

  private final REASON reason;

  private final RuleCallback<CTX> defaultRuleCallback = new DefaultRuleCallback();
  private RuleCallback<CTX> ruleCallback;

  private String description;
  private String synopsis;

  /**
   * Instantiates an instance of the AbstractRule class initialized with a reason for indicating why when the evaluation
   * of this rule fails.
   * @param reason an Object indicating the reason this rule failed (meaning the actual outcome of evaluating this rule
   * did not match the expected outcome).
   * @throws NullPointerException if the specified reason is null.
   */
  public AbstractRule(final REASON reason) {
    this(reason, DEFAULT_EXPECTED_OUTCOME, DEFAULT_THROW_EXCEPTION_ON_FAILURE);
  }

  /**
   * Instantiates an instance of the AbstractRule class initialized with a reason for indicating why when the evaluation
   * of this rule fails.  In addition, the expected outcome (pass/true or fail/false) is specified to indicate the
   * expected result upon evaluating this rule.
   * @param reason an Object indicating the reason this rule failed (meaning the actual outcome of evaluating this rule
   * did not match the expected outcome).
   * @param expectedOutcome a boolean value indicating whether the actual outcome of evaluating this rule should result
   * in passing or failing.
   * @throws NullPointerException if the specified reason is null.
   */
  public AbstractRule(final REASON reason, final boolean expectedOutcome) {
    this(reason, expectedOutcome, DEFAULT_THROW_EXCEPTION_ON_FAILURE);
  }

  /**
   * Instantiates an instance of the AbstractRule class initialized with a reason for indicating why when the evaluation
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
  public AbstractRule(final REASON reason, final boolean expectedOutcome, final boolean throwExceptionOnFailure) {
    Assert.notNull(reason, "The reason this rule (" + getClass().getName() + ") may fail cannot be null!");
    this.reason = reason;
    this.expectedOutcome = expectedOutcome;
    this.throwExceptionOnFailure = throwExceptionOnFailure;
  }

  /**
   * Gets the actual outcome from evaluating this rule.
   * @return a boolean value indicating the actual outcome of evaluating this rule.  The boolean value indicates
   * whether the information in the RuleContext satisfied the criteria of this rule.
   * @see com.cp.common.biz.rules.AbstractRule#getExpectedOutcome()
   */
  public boolean getActualOutcome() {
    return actualOutcome;
  }

  /**
   * Sets the actual outcome (result) after this rule has been evaluated.
   * @param actualOutcome a boolean value indicating the result of evaluating this rule.  The boolean value indicates
   * whether the information in the RuleContext satisfied the criteria of this rule.
   */
  private void setActualOutcome(final boolean actualOutcome) {
    this.actualOutcome = actualOutcome;
  }

  /**
   * Gets a String value describing in detail the required criteria in addition to the expected outcome to satisfy
   * this rule upon evaluation.
   * @return a String value describing the required criteria and expected outcome of the evaluation this rule.
   * @see com.cp.common.biz.rules.AbstractRule#getSynopsis()
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the String value describing in detail the required criteria in addition to the expected outcome to satisfy
   * this rule upon evaluation.
   * @param description a String value describing the required criteria and expected outcome of the evaluation
   * this rule.
   */
  public void setDescription(final String description) {
    this.description = description;
  }

  /**
   * Determines whether this rule has been evaluated or not (ran).
   * @return a boolean value indicating whether this rule has been evaluated.
   */
  public boolean isEvaluated() {
    return evaluated;
  }

  /**
   * Sets whether this rule has been evaluated or not (ran).
   * @param evaluated a boolean value indicating whether this rule has been evaluated.
   */
  private void setEvaluated(final boolean evaluated) {
    this.evaluated = evaluated;
  }

  /**
   * Gets the expected outcome of evaluating this rule.
   * @return a boolean value indicating whether the evaluation of this rule should result in passing or failing.
   * @see com.cp.common.biz.rules.AbstractRule#getActualOutcome()
   */
  public boolean getExpectedOutcome() {
    return expectedOutcome;
  }

  /**
   * Gets a long numerical value uniquely identifying this rule in a collection, composition or configuration of rules.
   * @return a long value uniquely identifying this rule in a rule collection, composition or configuration.
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets a long numerical value to uniquely identify this rule in a collection, composition or configuration of rules.
   * @param id a long value uniquely identifying this rule in a rule collection, composition or configuration.
   */
  public void setId(final Long id) {
    this.id = id;
  }

  /**
   * Gets the reason why the rule failed (where the actual outcome after evaluation did not match the expected outcome).
   * The rule returns null if the rule passed.
   * @return a generic Object representing the reason this rule failed or null if the rule passed.
   * @throws IllegalStateException if the rule has not yet been evaluated.
   */
  public REASON getReason() {
    return (passed() ? null : reason);
  }

  /**
   * Returns an Object indicating the Reason why this rule may fail as initialized upon instantiation of this rule.
   * @return an Object indicating the reason this rule may fail.
   * @see AbstractRule#getReason()
   */
  REASON getReasonOfFailure() {
    return reason;
  }

  /**
   * Gets the RuleCallback object associated with this rule to invoke certain rule lifecycle event methods such as
   * doBefore and doAfter this rule has been evaluated.
   * @return a RuleCallback object who's rule lifecyle event methods are invoked before and after the evaluation
   * of this rule.
   */
  public RuleCallback<CTX> getRuleCallback() {
    return ObjectUtil.getDefaultValue(ruleCallback, defaultRuleCallback);
  }

  /**
   * Sets the RuleCallback object to be associated with this rule in order to invoke certain rule lifecycle event
   * methods such as doBefore and doAfter when this rule is evaluated.
   * @param ruleCallback a RuleCallback object who's rule lifecyle event methods are invoked before and after the
   * evaluation of this rule.
   */
  public void setRuleCallback(final RuleCallback<CTX> ruleCallback) {
    this.ruleCallback = ruleCallback;
  }

  /**
   * Gets a String value providing a brief overview to summarize the context in which the rule is used as well as
   * the intent for the evaluation of this rule.
   * @return a String value providing a brief overview summarizing the context, purpose and intent of this rule.
   * @see com.cp.common.biz.rules.AbstractRule#getDescription()
   */
  public String getSynopsis() {
    return synopsis;
  }

  /**
   * Sets the String value providing a brief overview to summarize the context in which the rule is used as well as
   * the intent for the evaluation of this rule.
   * @param synopsis a String value providing a brief overview summarizing the context, purpose and intent of this rule.
   */
  public void setSynopsis(final String synopsis) {
    this.synopsis = synopsis;
  }

  /**
   * Determines whether this rule has been configured to throw a RuleFailureException upon failure after this rule
   * has been evaluated.
   * @return a boolean value indicating whether this rule was configured to throw a RuleFailureException upon an
   * unsuccessful rule evaluation.
   */
  public boolean isThrowExceptionOnFailure() {
    return throwExceptionOnFailure;
  }

  /**
   * Accepts visitation by a Visitor object visiting a collection, composition or configuration of rules in order to
   * apply some operation to the rules.
   * @param visitor a Visitor object applying some operation to the rules in a collection, composition or configuration.
   */
  public void accept(final Visitor visitor) {
    visitor.visit(this);
  }

  /**
   * Invokes the doEvaluate method to evaluate the actual criteria of this rule as defined by the subclass.
   * @param context the RuleContext object containing the information to be evaluated by the criteria of this rule.
   * @throws RuleEvaluationException if an unexpected error or exception occurs during the evaluation of this rule.
   * @see AbstractRule#doEvaluate(Object)
   * @see AbstractRule#evaluate(Object)
   */
  private void callDoEvaluate(final CTX context) {
    try {
      setEvaluated(false);
      setActualOutcome(doEvaluate(context));
      setEvaluated(true);
    }
    catch (RuleEvaluationException e) {
      throw e;
    }
    catch (RuleFailureException e) {
      setEvaluated(true);
      callDoIfPassOrFail(context);
      getRuleCallback().doAfter(context);
      throw e;
    }
    catch (Throwable t) {
      logger.warn("Rule (" + getClass().getName() + ") threw an unexpected Throwable!", t);
      throw new RuleEvaluationException("Rule (" + getClass().getName() + ") threw an unexpected Throwable!", t);
    }
  }

  /**
   * Invokes the doIfPass or the doIfFail methods on the RuleCallback object associated with this rule depending upon
   * whether the evaluation of this rule passed or failed respectively.
   * @param context the RuleContext object containing the information to be evaluated by the criteria of this rule.
   * @throws IllegalStateException if the rule has not yet been evaluated.
   */
  private void callDoIfPassOrFail(final CTX context) {
    if (passed()) {
      getRuleCallback().doIfPass(context);
    }
    else {
      getRuleCallback().doIfFail(context);
    }
  }

  /**
   * Determines whether to throw a RuleFailureException if the evaluation of this rule fails and the
   * throwExceptionOnFailure property of this rule has been set.
   * @throws IllegalStateException if the rule has not yet been evaluated.
   * @see com.cp.common.biz.rules.AbstractRule#failed()
   * @see com.cp.common.biz.rules.AbstractRule#isThrowExceptionOnFailure()
   */
  private void callThrowExceptionOnFailure() {
    if (failed() && isThrowExceptionOnFailure()) {
      throw new RuleFailureException("The evaluation of rule (" + getClass().getName() + ") failed because '"
        + getReason() + "'!");
    }
  }

  /**
   * Method to specify the actual criteria of this rule as declared by the subclass.
   * @param context the RuleContext containing the information to be evaluated by this rule and it's criteria.
   * @return a boolean value indicating whether the information contained within the RuleContext satisfies the criteria
   * of this rule upon being evaluated.
   * @throws Exception if the subclass implementation of doEvaluate encounters a problem.
   */
  protected abstract boolean doEvaluate(CTX context) throws Exception;

  /**
   * Evaluates the criteria of this rule against the information provided in the RuleContext
   * @param context a RuleContext object who's information is evaluated against the criteria of this rule.
   * @return a boolean value indicating whether the information in the RuleContext satisfied the criteria of this rule.
   * @see com.cp.common.biz.rules.AbstractRule#callDoEvaluate(Object)
   * @see com.cp.common.biz.rules.AbstractRule#callDoIfPassOrFail(Object)
   * @see com.cp.common.biz.rules.AbstractRule#callThrowExceptionOnFailure()
   * @see com.cp.common.biz.rules.AbstractRule#doEvaluate(Object)
   */
  public final boolean evaluate(final CTX context) {
    getRuleCallback().doBefore(context);
    callDoEvaluate(context);
    callDoIfPassOrFail(context);
    getRuleCallback().doAfter(context);
    callThrowExceptionOnFailure();
    return passed();
  }

  /**
   * Determines whether this rule failed based on whether the actual outcome of evaluating this rule matched
   * the expected outcome.
   * @return a boolean value indicating whether the rule evaluation failed (meaning the actual outcome did not match
   * the expected outcome).
   * @throws IllegalStateException if the rule has not yet been evaluated.
   * @see com.cp.common.biz.rules.AbstractRule#passed()
   */
  public boolean failed() {
    return !passed();
  }

  /**
   * Determines whether this rule passed based on whether the actual outcome of evaluating this rule matched
   * the expected outcome.
   * @return a boolean value indicating whether the rule evaluation passed (meaning the actual outcome matched
   * the expected outcome).
   * @throws IllegalStateException if the rule has not yet been evaluated.
   * @see com.cp.common.biz.rules.AbstractRule#failed()
   */
  public boolean passed() {
    Assert.state(isEvaluated(), "The rule (" + getClass().getName() + ") has not been evaluated!");
    return (getActualOutcome() == getExpectedOutcome());
  }

  /**
   * Resets the state for the evaluation of this rule
   */
  public void reset() {
    setEvaluated(false);
  }

  /**
   * Gets a String representation of the internal state of this rule.
   * @return a String representation of the internal state of this rule.
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{actualOutcome = ");
    buffer.append(getActualOutcome());
    buffer.append(", description = ").append(getDescription());
    buffer.append(", evaluated = ").append(isEvaluated());
    buffer.append(", expectedOutcome = ").append(getExpectedOutcome());
    buffer.append(", id = ").append(getId());
    buffer.append(", reason = ").append(getReasonOfFailure());
    buffer.append(", synopsis = ").append(getSynopsis());
    buffer.append(", throwExceptionOnFailure = ").append(isThrowExceptionOnFailure());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

  /**
   * A default no operation RuleCallback object.
   */
  protected final class DefaultRuleCallback implements RuleCallback<CTX> {

    public void doAfter(final CTX context) {
    }

    public void doBefore(final CTX context) {
    }

    public void doIfFail(final CTX context) {
    }

    public void doIfPass(final CTX context) {
    }
  }

}
