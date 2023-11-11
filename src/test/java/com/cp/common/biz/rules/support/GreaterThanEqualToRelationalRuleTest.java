/*
 * GreaterThanEqualToRelationalRuleTest.java (c) 22 July 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.22
 * @see com.cp.common.biz.rules.support.GreaterThanEqualToRelationalRule
 * @see junit.framework.TestCase
 */

package com.cp.common.biz.rules.support;

import com.cp.common.biz.rules.RelationalRule;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class GreaterThanEqualToRelationalRuleTest extends TestCase {

  public GreaterThanEqualToRelationalRuleTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(GreaterThanEqualToRelationalRuleTest.class);
    //suite.addTest(new GreaterThanEqualToRelationalRuleTest("testEvaluate"));
    return suite;
  }

  public void testEvaluate() throws Exception {
    final RelationalRule<Integer, String> rule = new GreaterThanEqualToRelationalRule<Integer, String>(2, "Because");

    assertFalse(rule.evaluate(1));
    assertTrue(rule.evaluate(2));
    assertTrue(rule.evaluate(3));
    assertTrue(rule.evaluate(4));
    assertTrue(rule.evaluate(5));
  }

}
