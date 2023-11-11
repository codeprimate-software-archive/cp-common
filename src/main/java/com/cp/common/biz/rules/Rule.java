/*
 * Rule.java (c) 29 May 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.29
 * @see com.cp.common.biz.rules.RuleCallback
 * @see com.cp.common.biz.rules.RuleContext
 */

package com.cp.common.biz.rules;

import com.cp.common.lang.Describable;
import com.cp.common.lang.Identifiable;
import com.cp.common.lang.Resettable;
import com.cp.common.lang.Visitable;

public interface Rule<CTX, REASON> extends Describable, Identifiable<Long>, Resettable, Visitable {

  /**
   * Gets the actual outcome from evaluating this rule.
   * @return a boolean value indicating the actual outcome of evaluating this rule.  The boolean value indicates
   * whether the information in the RuleContext satisfied the criteria of this rule.
   * @see com.cp.common.biz.rules.Rule#getExpectedOutcome()
   */
  public boolean getActualOutcome();

  /**
   * Determines whether this rule has been evaluated or not (ran).
   * @return a boolean value indicating whether this rule has been evaluated.
   */
  public boolean isEvaluated();

  /**
   * Gets the expected outcome of evaluating this rule.
   * @return a boolean value indicating whether the evaluation of this rule should result in passing or failing.
   * @see com.cp.common.biz.rules.Rule#getActualOutcome()
   */
  public boolean getExpectedOutcome();

  /**
   * Gets the reason why the rule failed (where the actual outcome after evaluation did not match the expected outcome).
   * @return a generic Object representing the reason this rule failed.
   */
  public REASON getReason();

  /**
   * Gets the RuleCallback object associated with this rule to invoke certain rule lifecycle event methods such as
   * doBefore and doAfter this rule has been evaluated.
   * @return a RuleCallback object who's rule lifecyle event methods are invoked before and after the evaluation
   * of this rule.
   */
  public RuleCallback<CTX> getRuleCallback();

  /**
   * Sets the RuleCallback object to be associated with this rule in order to invoke certain rule lifecycle event
   * methods such as doBefore and doAfter when this rule is evaluated.
   * @param callback a RuleCallback object who's rule lifecyle event methods are invoked before and after the
   * evaluation of this rule.
   */
  public void setRuleCallback(RuleCallback<CTX> callback);

  /**
   * Determines whether this rule has been configured to throw a RuleFailureException upon failure after this rule
   * has been evaluated.
   * @return a boolean value indicating whether this rule was configured to throw a RuleFailureException upon an
   * unsuccessful rule evaluation.
   */
  public boolean isThrowExceptionOnFailure();

  /**
   * Evaluates the criteria of this rule against the information provided in the RuleContext
   * @param context a RuleContext object who's information is evaluated against the criteria of this rule.
   * @return a boolean value indicating whether the information in the RuleContext satisfied the criteria of this rule.
   */
  public boolean evaluate(CTX context);

  /**
   * Determines whether this rule failed based on whether the actual outcome of evaluating this rule matched
   * the expected outcome.
   * @return a boolean value indicating whether the rule evaluation failed (meaning the actual outcome did not match
   * the expected outcome).
   * @see com.cp.common.biz.rules.Rule#passed()
   */
  public boolean failed();

  /**
   * Determines whether this rule passed based on whether the actual outcome of evaluating this rule matched
   * the expected outcome.
   * @return a boolean value indicating whether the rule evaluation passed (meaning the actual outcome matched
   * the expected outcome).
   * @see com.cp.common.biz.rules.Rule#failed()
   */
  public boolean passed();

}
