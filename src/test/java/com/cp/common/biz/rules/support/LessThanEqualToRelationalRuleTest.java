/*
 * LessThanEqualToRelationalRuleTest.java (c) 22 July 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.22
 * @see com.cp.common.biz.rules.support.LessThanEqualToRelationalRule
 * @see junit.framework.TestCase
 */

package com.cp.common.biz.rules.support;

import com.cp.common.biz.rules.RelationalRule;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class LessThanEqualToRelationalRuleTest extends TestCase {

  public LessThanEqualToRelationalRuleTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(LessThanEqualToRelationalRuleTest.class);
    //suite.addTest(new LessThanEqualToRelationalRuleTest("testEvaluate"));
    return suite;
  }

  public void testEvaluate() throws Exception {
    final RelationalRule<Integer, String> rule = new LessThanEqualToRelationalRule<Integer, String>(2, "Because");

    assertTrue(rule.evaluate(1));
    assertTrue(rule.evaluate(2));
    assertFalse(rule.evaluate(3));
    assertFalse(rule.evaluate(4));
    assertFalse(rule.evaluate(5));
  }

}
