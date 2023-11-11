/*
 * LessThanOrGreaterThanRelationalRule.java (c) 14 July 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.14
 * @see com.cp.common.biz.rules.RelationalRule
 */

package com.cp.common.biz.rules.support;

import com.cp.common.biz.rules.RelationalRule;
import com.cp.common.lang.RelationalOperator;

public class LessThanOrGreaterThanRelationalRule<VALUE extends Comparable<VALUE>, REASON> extends RelationalRule<VALUE, REASON> {

  public LessThanOrGreaterThanRelationalRule(final VALUE lessThanValue,
                                             final VALUE greaterThanValue,
                                             final REASON reason)
  {
    this(lessThanValue, greaterThanValue, reason, DEFAULT_EXPECTED_OUTCOME, DEFAULT_THROW_EXCEPTION_ON_FAILURE);
  }

  public LessThanOrGreaterThanRelationalRule(final VALUE lessThanValue,
                                             final VALUE greaterThanValue,
                                             final REASON reason,
                                             final boolean expectedOutcome)
  {
    this(lessThanValue, greaterThanValue, reason, expectedOutcome, DEFAULT_THROW_EXCEPTION_ON_FAILURE);
  }

  public LessThanOrGreaterThanRelationalRule(final VALUE lessThanValue,
                                             final VALUE greaterThanValue,
                                             final REASON reason,
                                             final boolean expectedOutcome,
                                             final boolean throwExceptionOnFailure)
  {
    super(RelationalOperator.getLessThanOrGreaterThan(lessThanValue, greaterThanValue), reason, expectedOutcome,
      throwExceptionOnFailure);
  }

}
