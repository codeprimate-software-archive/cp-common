/*
 * GreaterThanAndLessThanRelationalRuleTest.java (c) 22 July 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.22
 * @see com.cp.common.biz.rules.support.GreaterThanAndLessThanRelationalRule
 * @see junit.framework.TestCase
 */

package com.cp.common.biz.rules.support;

import com.cp.common.biz.rules.RelationalRule;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class GreaterThanAndLessThanRelationalRuleTest extends TestCase {

  public GreaterThanAndLessThanRelationalRuleTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(GreaterThanAndLessThanRelationalRuleTest.class);
    //suite.addTest(new GreaterThanAndLessThanRelationalRuleTest("testEvaluate"));
    return suite;
  }

  public void testEvaluate() throws Exception {
    final RelationalRule<Integer, String> rule = new GreaterThanAndLessThanRelationalRule<Integer, String>(2, 4, "Because");

    assertFalse(rule.evaluate(1));
    assertFalse(rule.evaluate(2));
    assertTrue(rule.evaluate(3));
    assertFalse(rule.evaluate(4));
    assertFalse(rule.evaluate(5));
  }

}
