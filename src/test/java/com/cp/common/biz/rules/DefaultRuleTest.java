/*
 * DefaultRuleTest.java (c) 12 July 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.29
 * @see com.cp.common.biz.rules.DefaultRule
 * @see junit.framework.TestCase
 */

package com.cp.common.biz.rules;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DefaultRuleTest extends TestCase {

  public DefaultRuleTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DefaultRuleTest.class);
    //suite.addTest(new DefaultRuleTest("testName"));
    return suite;
  }

  public void testInstantiate() throws Exception {
    DefaultRule rule = null;

    assertNull(rule);

    try {
      rule = new DefaultRule("because", true, false, true);
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the DefaultRule class with non-null reason threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertFalse(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());
  }

  public void testInstantiateWithNullReason() throws Exception {
    DefaultRule rule = null;

    assertNull(rule);

    try {
      rule = new DefaultRule(null);
      fail("Instantiating an instance of the DefaultRule class with a null reason should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The reason this rule (" + DefaultRule.class.getName() + ") may fail cannot be null!",
        e.getMessage());
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the DefaultRule class with a null reason threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertNull(rule);
  }

  public void testEvaluateFails() throws Exception {
    Rule<Object, String> rule = new DefaultRule(DefaultRule.DEFAULT_REASON, false, true, false);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals(DefaultRule.DEFAULT_REASON, ((DefaultRule) rule).getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());
    assertFalse(rule.evaluate(new Object()));
    assertFalse(rule.getActualOutcome());
    assertTrue(rule.isEvaluated());
    assertEquals("The actual outcome (false) did not match expected outcome (true)!", rule.getReason());
    assertFalse(rule.passed());
    assertTrue(rule.failed());

    rule = new DefaultRule("because", true, false, false);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertFalse(rule.getExpectedOutcome());
    assertEquals("because", ((DefaultRule) rule).getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());
    assertFalse(rule.evaluate(new Object()));
    assertTrue(rule.getActualOutcome());
    assertTrue(rule.isEvaluated());
    assertEquals("because", rule.getReason());
    assertFalse(rule.passed());
    assertTrue(rule.failed());
  }

  public void testEvaluatePasses() throws Exception {
    Rule<Object, String> rule = new DefaultRule("because");

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", ((DefaultRule) rule).getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());
    assertTrue(rule.evaluate(new Object()));
    assertTrue(rule.getActualOutcome());
    assertTrue(rule.isEvaluated());
    assertNull(rule.getReason());
    assertTrue(rule.passed());
    assertFalse(rule.failed());

    rule = new DefaultRule(DefaultRule.DEFAULT_REASON, false, false, true);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertFalse(rule.getExpectedOutcome());
    assertEquals(DefaultRule.DEFAULT_REASON, ((DefaultRule) rule).getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());
    assertTrue(rule.evaluate(new Object()));
    assertFalse(rule.getActualOutcome());
    assertTrue(rule.isEvaluated());
    assertNull(rule.getReason());
    assertTrue(rule.passed());
    assertFalse(rule.failed());
  }

  public void testEvaluateThrowsRuleFailureException() throws Exception {
    Rule<Object, String> rule = new DefaultRule("because", false, true, true);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", ((DefaultRule) rule).getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());

    try {
      rule.evaluate(new Object());
      fail("Calling evaluate on DefaultRule configured to throw a RuntimeException on failure should have thrown a RuleFailureException!");
    }
    catch (RuleFailureException e) {
      assertEquals("The evaluation of rule (" + rule.getClass().getName() + ") failed because '"
        + rule.getReason() + "'!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling evaluate on DefaultRule configured to throw a RuntimeException on failure threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertFalse(rule.getActualOutcome());
    assertTrue(rule.isEvaluated());
    assertEquals("because", rule.getReason());
    assertFalse(rule.passed());
    assertTrue(rule.failed());

    rule = new DefaultRule("why", false, false, true);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertFalse(rule.getExpectedOutcome());
    assertEquals("why", ((DefaultRule) rule).getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());

    try {
      assertTrue(rule.evaluate(new Object()));
    }
    catch (Throwable t) {
      fail("Calling evaluate on DefaultRule configured to throw a RuntimeException on failure threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertFalse(rule.getActualOutcome());
    assertTrue(rule.isEvaluated());
    assertNull(rule.getReason());
    assertTrue(rule.passed());
    assertFalse(rule.failed());
  }

}
