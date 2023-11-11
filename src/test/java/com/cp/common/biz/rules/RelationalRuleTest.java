/*
 * RelationalRuleTest.java (c) 14 July 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.29
 * @see com.cp.common.biz.rules.RelationalRule
 * @see junit.framework.TestCase
 */

package com.cp.common.biz.rules;

import com.cp.common.lang.RelationalOperator;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class RelationalRuleTest extends TestCase {

  public RelationalRuleTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(RelationalRuleTest.class);
    //suite.addTest(new RelationalRuleTest("testName"));
    return suite;
  }

  public void testInstantiate() throws Exception {
    RelationalRule<Integer, String> rule = null;

    assertNull(rule);

    try {
      rule = new RelationalRule<Integer, String>(RelationalOperator.getEqualTo(0), "because", false, true);
    }
    catch (Throwable t) {
      fail("Constructing an instance of the RelationalRule class with a non-null relational operator and reason threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertFalse(rule.getExpectedOutcome());
    assertNotNull(rule.getOp());
    assertEquals("because", rule.getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());
  }

  public void testInstantiateWithNullReason() throws Exception {
    RelationalRule<Double, String> rule = null;

    assertNull(rule);

    try {
      rule = new RelationalRule<Double, String>(RelationalOperator.getEqualTo(0.0d), null);
      fail("Constructing an instance of the RelationalRule class with a null reason should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The reason this rule (" + RelationalRule.class.getName() + ") may fail cannot be null!",
        e.getMessage());
    }
    catch (Throwable t) {
      fail("Constructing an instance of the RelationalRule class with a null reason threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertNull(rule);
  }

  public void testInstantiateWithNullRelationalOperator() throws Exception {
    RelationalRule<Calendar, String> rule = null;

    assertNull(rule);

    try {
      rule = new RelationalRule<Calendar, String>(null, "because");
      fail("Constructing an instance of the RelationalRule class with a null relational operator should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The relational operator for this Rule cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Constructing an instance of the RelationalRule class with a null relational operator threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertNull(rule);
  }

  public void testEvaluate() throws Exception {
    final RelationalRule<String, String> rule = new RelationalRule<String, String>(
      RelationalOperator.getEqualTo("test"), "is not equal to");

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertNotNull(rule.getOp());
    assertEquals("is not equal to", rule.getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());
    assertTrue(rule.evaluate("test"));
    assertTrue(rule.getActualOutcome());
    assertTrue(rule.isEvaluated());
    assertNull(rule.getReason());
    assertTrue(rule.passed());
    assertFalse(rule.evaluate("TEST"));
    assertFalse(rule.getActualOutcome());
    assertTrue(rule.isEvaluated());
    assertEquals("is not equal to", rule.getReason());
    assertFalse(rule.passed());
    assertFalse(rule.evaluate("tester"));
    assertFalse(rule.getActualOutcome());
    assertTrue(rule.isEvaluated());
    assertEquals("is not equal to", rule.getReason());
    assertFalse(rule.passed());
    assertFalse(rule.evaluate("testing"));
    assertFalse(rule.getActualOutcome());
    assertTrue(rule.isEvaluated());
    assertEquals("is not equal to", rule.getReason());
    assertFalse(rule.passed());
    assertFalse(rule.evaluate("QA"));
    assertFalse(rule.getActualOutcome());
    assertTrue(rule.isEvaluated());
    assertEquals("is not equal to", rule.getReason());
    assertFalse(rule.passed());
    assertFalse(rule.evaluate("acceptance test"));
    assertFalse(rule.getActualOutcome());
    assertTrue(rule.isEvaluated());
    assertEquals("is not equal to", rule.getReason());
    assertFalse(rule.passed());
    assertFalse(rule.evaluate("functional test"));
    assertFalse(rule.getActualOutcome());
    assertTrue(rule.isEvaluated());
    assertEquals("is not equal to", rule.getReason());
    assertFalse(rule.passed());
    assertFalse(rule.evaluate("integration test"));
    assertFalse(rule.getActualOutcome());
    assertTrue(rule.isEvaluated());
    assertEquals("is not equal to", rule.getReason());
    assertFalse(rule.passed());
  }

  public void testEvaluateThrowsRuleEvaluationException() throws Exception {
    final RelationalRule<Integer, String> rule = new RelationalRule<Integer, String>(
      RelationalOperator.getEqualTo(2), "because", true, true);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());

    try {
      rule.evaluate(null);
      fail("Calling evaluate on RelationalRule with a null value should have thrown a RuleEvaluationException!");
    }
    catch (RuleEvaluationException e) {
      assertEquals("Rule (" + rule.getClass().getName() + ") threw an unexpected Throwable!", e.getMessage());
      assertTrue(e.getCause() instanceof NullPointerException);
      assertEquals("The actual value cannot be null!", e.getCause().getMessage());
    }
    catch (Throwable t) {
      fail("Calling evaluate on RelationalRule with a null value threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertFalse(rule.isEvaluated());
  }

  public void testEvaluateThrowsRuleFailureException() throws Exception {
    final RelationalRule<Double, String> rule = new RelationalRule<Double, String>(
      RelationalOperator.getEqualTo(Math.PI), "because", true, true);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());

    try {
      rule.evaluate(3.14159d);
      fail("Calling evaluate on RelationalRule with a non-equal value configured to throw a RuntimeException on failure should have thrown a RuleFailureException!");
    }
    catch (RuleFailureException e) {
      assertEquals("The evaluation of rule (" + rule.getClass().getName() + ") failed because '"
        + rule.getReason() + "'!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling evaluate on RelationalRule with a non-equal value configured to throw a RuntimeException on failure threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertFalse(rule.getActualOutcome());
    assertTrue(rule.isEvaluated());
    assertEquals("because", rule.getReason());
    assertFalse(rule.passed());
  }

}
