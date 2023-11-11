/*
 * RuleCallback.java (c) 29 May 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.14
 * @see com.cp.common.biz.rules.Rule
 * @see com.cp.common.biz.rules.RuleContext
 */

package com.cp.common.biz.rules;

public interface RuleCallback<CTX> {

  /**
   * Operation called after the evaluation of a Rule.
   * @param context an Object referring to the information evaluated by the Rule.
   */
  public void doAfter(CTX context);

  /**
   * Operation called before the evaluation of a Rule.
   * @param context an Object referring to the information evaluated by the Rule.
   */
  public void doBefore(CTX context);

  /**
   * Operation called only if the evaluation of the associated Rule fails.
   * @param context an Object referring to the information evaluated by the Rule.
   */
  public void doIfFail(CTX context);

  /**
   * Operation called only if the evaluation of the associated Rule passes.
   * @param context an Object referring to the information evaluated by the Rule.
   */
  public void doIfPass(CTX context);

}
