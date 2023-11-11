/*
 * EqualToRelationalRuleTest.java (c) 18 July 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.22
 * @see com.cp.common.biz.rules.support.EqualToRelationalRuleTest
 * @see junit.framework.TestCase
 */

package com.cp.common.biz.rules.support;

import com.cp.common.biz.rules.RelationalRule;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class EqualToRelationalRuleTest extends TestCase {

  public EqualToRelationalRuleTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(EqualToRelationalRuleTest.class);
    //suite.addTest(new EqualToRelationalRuleTest("testEvaluate"));
    return suite;
  }

  public void testEvaluate() throws Exception {
    final RelationalRule<Integer, String> rule = new EqualToRelationalRule<Integer, String>(3, "Because");

    assertFalse(rule.evaluate(1));
    assertFalse(rule.evaluate(2));
    assertTrue(rule.evaluate(3));
    assertFalse(rule.evaluate(4));
    assertFalse(rule.evaluate(5));
    assertFalse(rule.evaluate(-3));
    assertFalse(rule.evaluate(33));
    assertFalse(rule.evaluate(333));
  }

}
