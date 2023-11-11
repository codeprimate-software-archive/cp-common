/*
 * GreaterThanEqualToAndLessThanRelationalRule.java (c) 14 July 2009
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

public class GreaterThanEqualToAndLessThanRelationalRule<VALUE extends Comparable<VALUE>, REASON> extends RelationalRule<VALUE, REASON> {

  public GreaterThanEqualToAndLessThanRelationalRule(final VALUE greaterThanEqualToValue,
                                                     final VALUE lessThan,
                                                     final REASON reason)
  {
    this(greaterThanEqualToValue, lessThan, reason, DEFAULT_EXPECTED_OUTCOME, DEFAULT_THROW_EXCEPTION_ON_FAILURE);
  }

  public GreaterThanEqualToAndLessThanRelationalRule(final VALUE greaterThanEqualToValue,
                                                     final VALUE lessThanValue,
                                                     final REASON reason,
                                                     final boolean expectedOutcome)
  {
    this(greaterThanEqualToValue, lessThanValue, reason, expectedOutcome, DEFAULT_THROW_EXCEPTION_ON_FAILURE);
  }

  public GreaterThanEqualToAndLessThanRelationalRule(final VALUE greaterThanEqualToValue,
                                                     final VALUE lessThanValue,
                                                     final REASON reason,
                                                     final boolean expectedOutcome,
                                                     final boolean throwExceptionOnFailure)
  {
    super(RelationalOperator.getGreaterThanEqualToAndLessThan(greaterThanEqualToValue, lessThanValue), reason,
      expectedOutcome, throwExceptionOnFailure);
  }

}
