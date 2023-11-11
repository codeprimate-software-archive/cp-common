/*
 * LessThanEqualToOrGreaterThanEqualToRelationalRule.java (c) 14 July 2009
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

public class LessThanEqualToOrGreaterThanEqualToRelationalRule<VALUE extends Comparable<VALUE>, REASON> extends RelationalRule<VALUE, REASON> {

  public LessThanEqualToOrGreaterThanEqualToRelationalRule(final VALUE lessThanEqualToValue,
                                                           final VALUE greaterThanEqualToValue,
                                                           final REASON reason)
  {
    this(lessThanEqualToValue, greaterThanEqualToValue, reason, DEFAULT_EXPECTED_OUTCOME, DEFAULT_THROW_EXCEPTION_ON_FAILURE);
  }

  public LessThanEqualToOrGreaterThanEqualToRelationalRule(final VALUE lessThanEqualToValue,
                                                           final VALUE greaterThanEqualToValue,
                                                           final REASON reason,
                                                           final boolean expectedOutcome) {
    this(lessThanEqualToValue, greaterThanEqualToValue, reason, expectedOutcome, DEFAULT_THROW_EXCEPTION_ON_FAILURE);
  }

  public LessThanEqualToOrGreaterThanEqualToRelationalRule(final VALUE lessThanEqualToValue,
                                                           final VALUE greaterThanEqualToValue,
                                                           final REASON reason,
                                                           final boolean expectedOutcome,
                                                           final boolean throwExceptionOnFailure)
  {
    super(RelationalOperator.getLessThanEqualToOrGreaterThanEqualTo(lessThanEqualToValue, greaterThanEqualToValue),
      reason, expectedOutcome, throwExceptionOnFailure);
  }

}
