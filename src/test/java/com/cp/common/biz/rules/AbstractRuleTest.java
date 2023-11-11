/*
 * AbstractRuleTest.java (c) 5 July 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.29
 * @see com.cp.common.biz.rules.AbstractRule
 * @see junit.framework.TestCase
 */

package com.cp.common.biz.rules;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Visitable;
import com.cp.common.util.Visitor;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractRuleTest extends TestCase {

  public AbstractRuleTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractRuleTest.class);
    //suite.addTest(new AbstractRuleTest("testName"));
    return suite;
  }

  public void testInstantiation() throws Exception {
    AbstractRule<Object, String> rule = null;

    assertNull(rule);

    try {
      rule = new TestRule("Test, testing, tested!");
    }
    catch (Throwable t) {
      fail("Instanting an instance of the AbstractRule class with a non-null reason threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("Test, testing, tested!", rule.getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());
  }

  public void testInstantiationWithNonNullReasonAndNonDefaultValuesForExpectedOutcomeAndThrowExceptionOnFailure() 
    throws Exception
  {
    AbstractRule<Object, String> rule = null;

    assertNull(rule);

    try {
      rule = new TestRule("because", false, true);
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the AbstractRule class with a non-null reason and non-default values for expectedOutcome and throwExceptionOnFailure threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertFalse(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());
  }

  public void testInstantiationWithNullReason() throws Exception {
    AbstractRule<Object, String> rule = null;

    assertNull(rule);

    try {
      rule = new TestRule(null, false, true);
      fail("Instantiating an instance of the AbstractRule class with a null Reason should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The reason this rule (" + TestRule.class.getName() + ") may fail cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the AbstractRule class with a null Reason threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertNull(rule);
  }

  public void testGetActualOutcomeWhenFails() throws Exception {
    final RuleContext context = new TestRuleContext();
    AbstractRule<Object, String> rule = new TestRule("because", false, true, false);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());
    assertFalse(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertFalse(rule.passed());
    assertFalse(rule.getActualOutcome());

    rule = new TestRule("why", true, false, false);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertFalse(rule.getExpectedOutcome());
    assertEquals("why", rule.getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());
    assertFalse(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertFalse(rule.passed());
    assertTrue(rule.getActualOutcome());
  }

  public void testGetActualOutcomeWhenPasses() throws Exception {
    final RuleContext context = new TestRuleContext();
    AbstractRule<Object, String> rule = new TestRule("because", true, true, true);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());
    assertTrue(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertTrue(rule.passed());
    assertTrue(rule.getActualOutcome());

    rule = new TestRule("why", false, false, true);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertFalse(rule.getExpectedOutcome());
    assertEquals("why", rule.getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());
    assertTrue(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertTrue(rule.passed());
    assertFalse(rule.getActualOutcome());
  }

  public void testGetActualOutcomeWhenRuleEvaluationExceptionIsThrown() throws Exception {
    final AbstractRule<Object, String> rule = new TestRule("because",
      new NullPointerException("Context cannot be null!"), true);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());

    try {
      rule.evaluate(new TestRuleContext());
      fail("Calling evaluate on a rule when a RuleEvaluationException is thrown should have failed!");
    }
    catch (RuleEvaluationException e) {
      assertEquals("Rule (" + rule.getClass().getName() + ") threw an unexpected Throwable!", e.getMessage());
      assertTrue(e.getCause() instanceof NullPointerException);
      assertEquals("Context cannot be null!", e.getCause().getMessage());
    }
    catch (Throwable t) {
      fail("Calling evaluate on a rule when a RuleEvaluationException is thrown threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertFalse(rule.isEvaluated());
    assertFalse(rule.getActualOutcome());
  }

  public void testGetActualOutcomeWhenRuleFailureExceptionIsThrown() throws Exception {
    AbstractRule<Object, String> rule = new TestRule("because", false, true, true);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());

    try {
      rule.evaluate(new TestRuleContext());
      fail("Calling evaluate on a rule configured to throw a RuntimeException when actual does not match expected should have thrown a RuleFailureException!");
    }
    catch (RuleFailureException e) {
      assertEquals("The evaluation of rule (" + rule.getClass().getName() + ") failed because '"
        + rule.getReason() + "'!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling evaluate on a rule configured to throw a RuntimeException when actual does not match expected threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertTrue(rule.isEvaluated());
    assertFalse(rule.passed());
    assertFalse(rule.getActualOutcome());

    rule = new TestRule("why", true, false, true);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertFalse(rule.getExpectedOutcome());
    assertEquals("why", rule.getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());

    try {
      rule.evaluate(new TestRuleContext());
      fail("Calling evaluate on a rule configured to throw a RuntimeException when actual does not match expected should have thrown a RuleFailureException!");
    }
    catch (RuleFailureException e) {
      assertEquals("The evaluation of rule (" + rule.getClass().getName() + ") failed because '"
        + rule.getReason() + "'!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling evaluate on a rule configured to throw a RuntimeException when actual does not match expected threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertTrue(rule.isEvaluated());
    assertFalse(rule.passed());
    assertTrue(rule.getActualOutcome());
  }

  public void testIsEvaluated() throws Exception {
    final AbstractRule<Object, String> rule = new TestRule("because");

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());
    assertFalse(rule.evaluate(new TestRuleContext()));
    assertTrue(rule.isEvaluated());
  }

  public void testIsEvaluatedWhenRuleEvaluationExceptionIsThrown() throws Exception {
    final AbstractRule<Object, String> rule = new TestRule("because", new IllegalArgumentException("Illegal Argument!"));

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());

    try {
      rule.evaluate(new TestRuleContext());
      fail("Calling evaluate on a rule that throws a RuleEvaluationException should have failed!");
    }
    catch (RuleEvaluationException e) {
      //// expected behavior!
    }
    catch (Throwable t) {
      fail("Calling evaluate on a rule that throws a RuleEvaluationException threw an uexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertFalse(rule.isEvaluated());
  }

  public void testIsEvaluatedWhenRuleFailureExceptionIsThrown() throws Exception {
    final AbstractRule<Object, String> rule = new TestRule("because", false, true, true);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());

    try {
      rule.evaluate(new TestRuleContext());
      fail("Calling evaluate on a rule configured to throw a RuntimeException when actual did not match expected should have thrown a RuleFailureException!");
    }
    catch (RuleFailureException e) {
      assertEquals("The evaluation of rule (" + rule.getClass().getName() + ") failed because '"
        + rule.getReason() + "'!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling evaluate on a rule configured to throw a RuntimeException when actual did not match expected threw an uexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertTrue(rule.isEvaluated());
  }

  public void testGetReasonWhenFails() throws Exception {
    final AbstractRule<Object, String> rule = new TestRule("because");

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());
    assertFalse(rule.evaluate(new TestRuleContext()));
    assertTrue(rule.isEvaluated());
    assertFalse(rule.passed());
    assertEquals("because", rule.getReason());
  }

  public void testGetReasonWhenNotEvaluated() throws Exception {
    final AbstractRule<Object, String> rule = new TestRule("because");

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());

    try {
      rule.getReason();
      fail("Calling getReason when the rule has not been evaluated yet should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The rule (" + rule.getClass().getName() + ") has not been evaluated!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getReason when the rule has not been evaluated yet threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertFalse(rule.isEvaluated());
  }

  public void testGetReasonWhenPasses() throws Exception {
    final AbstractRule<Object, String> rule = new TestRule("because", true, true, true);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());
    assertTrue(rule.evaluate(new TestRuleContext()));
    assertTrue(rule.isEvaluated());
    assertTrue(rule.passed());
    assertNull(rule.getReason());
  }

  public void testGetReasonWhenRuleFailureExceptionIsThrown() throws Exception {
    final AbstractRule<Object, String> rule = new TestRule("because", false, true, true);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());

    try {
      rule.evaluate(new TestRuleContext());
      fail("Calling evaluate on a rule configured to throw a RuntimeException when actual did not match expected should have thrown a RuleFailureException!");
    }
    catch (RuleFailureException e) {
      assertEquals("The evaluation of rule (" + rule.getClass().getName() + ") failed because '"
        + rule.getReason() + "'!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling evaluate on a rule configured to throw a RuntimeException when actual did not match expected threw an uexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertTrue(rule.isEvaluated());
    assertFalse(rule.passed());
    assertEquals("because", rule.getReason());
  }

  public void testGetRuleCallback() throws Exception {
    final AbstractRule<Object, String> rule = new TestRule("because");
    final RuleCallback<Object> ruleCallback = new TestRuleCallback<Object>(rule, new TestRuleContext());

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());
    assertNotNull(rule.getRuleCallback());
    assertNotSame(ruleCallback, rule.getRuleCallback());

    rule.setRuleCallback(null);

    assertNotNull(rule.getRuleCallback());
    assertNotSame(ruleCallback, rule.getRuleCallback());

    rule.setRuleCallback(ruleCallback);

    assertNotNull(rule.getRuleCallback());
    assertSame(ruleCallback, rule.getRuleCallback());
  }

  public void testAccept() throws Exception {
    final AbstractRule<Object, String> rule = new TestRule("because");
    final MockVisitor visitor = new MockVisitor();

    assertNull(visitor.getRule());

    rule.accept(visitor);

    assertSame(rule, visitor.getRule());
  }

  public void testEvaluateCallsRuleCallbackWhenFails() throws Exception {
    final AbstractRule<Object, String> rule = new TestRule("because", false, true, false);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());

    final RuleContext context = new TestRuleContext();

    final TestRuleCallback<Object> ruleCallback = new TestRuleCallback<Object>(rule, context);
    rule.setRuleCallback(ruleCallback);

    assertEquals(ruleCallback, rule.getRuleCallback());
    assertFalse(ruleCallback.isDoAfterCalled());
    assertFalse(ruleCallback.isDoBeforeCalled());
    assertFalse(ruleCallback.isDoIfFailCalled());
    assertFalse(ruleCallback.isDoIfPassCalled());
    assertFalse(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertFalse(rule.passed());
    assertTrue(ruleCallback.isDoAfterCalled());
    assertTrue(ruleCallback.isDoBeforeCalled());
    assertTrue(ruleCallback.isDoIfFailCalled());
    assertFalse(ruleCallback.isDoIfPassCalled());
  }

  public void testEvaluateCallsRuleCallbackWhenPasses() throws Exception {
    final AbstractRule<Object, String> rule = new TestRule("because", true, true, true);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());

    final RuleContext context = new TestRuleContext();

    final TestRuleCallback<Object> ruleCallback = new TestRuleCallback<Object>(rule, context);
    rule.setRuleCallback(ruleCallback);

    assertEquals(ruleCallback, rule.getRuleCallback());
    assertFalse(ruleCallback.isDoAfterCalled());
    assertFalse(ruleCallback.isDoBeforeCalled());
    assertFalse(ruleCallback.isDoIfFailCalled());
    assertFalse(ruleCallback.isDoIfPassCalled());
    assertTrue(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertTrue(rule.passed());
    assertTrue(ruleCallback.isDoAfterCalled());
    assertTrue(ruleCallback.isDoBeforeCalled());
    assertFalse(ruleCallback.isDoIfFailCalled());
    assertTrue(ruleCallback.isDoIfPassCalled());
  }

  public void testEvaluateCallsRuleCallbackWhenRuleEvaluationExceptionIsThrown() throws Exception {
    final AbstractRule<Object, String> rule = new TestRule("because", new IllegalStateException("Illegal State!"));

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());

    final RuleContext context = new TestRuleContext();

    final TestRuleCallback<Object> ruleCallback = new TestRuleCallback<Object>(rule, context);
    rule.setRuleCallback(ruleCallback);

    assertEquals(ruleCallback, rule.getRuleCallback());
    assertFalse(ruleCallback.isDoAfterCalled());
    assertFalse(ruleCallback.isDoBeforeCalled());
    assertFalse(ruleCallback.isDoIfFailCalled());
    assertFalse(ruleCallback.isDoIfPassCalled());

    try {
      rule.evaluate(context);
      fail("Calling evaluate on a rule when a RuleEvaluationException is thrown should have failed!");
    }
    catch (RuleEvaluationException e) {
      assertEquals("Rule (" + rule.getClass().getName() + ") threw an unexpected Throwable!", e.getMessage());
      assertTrue(e.getCause() instanceof IllegalStateException);
      assertEquals("Illegal State!", e.getCause().getMessage());
    }
    catch (Throwable t) {
      fail("Calling evaluate on a rule when a RuleEvaluationException is thrown threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertFalse(rule.isEvaluated());
    assertFalse(ruleCallback.isDoAfterCalled());
    assertTrue(ruleCallback.isDoBeforeCalled());
    assertFalse(ruleCallback.isDoIfFailCalled());
    assertFalse(ruleCallback.isDoIfPassCalled());
  }

  public void testEvaluateCallsRuleCallbackWhenRuleFailureExceptionIsThrown() throws Exception {
    final AbstractRule<Object, String> rule = new TestRule("because", true, true);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());

    final RuleContext context = new TestRuleContext();

    final TestRuleCallback<Object> ruleCallback = new TestRuleCallback<Object>(rule, context);
    rule.setRuleCallback(ruleCallback);

    assertEquals(ruleCallback, rule.getRuleCallback());
    assertFalse(ruleCallback.isDoAfterCalled());
    assertFalse(ruleCallback.isDoBeforeCalled());
    assertFalse(ruleCallback.isDoIfFailCalled());
    assertFalse(ruleCallback.isDoIfPassCalled());

    try {
      rule.evaluate(context);
      fail("Calling evaluate on a rule configured to throw a RuntimeException when actual does not match expected should have thrown a RuleFailureExeption!");
    }
    catch (RuleFailureException e) {
      assertEquals("The evaluation of rule (" + rule.getClass().getName() + ") failed because '"
        + rule.getReason() + "'!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling evaluate on a rule configured to throw a RuntimeException when actual does not match expected threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertTrue(rule.isEvaluated());
    assertFalse(rule.passed());
    assertTrue(ruleCallback.isDoAfterCalled());
    assertTrue(ruleCallback.isDoBeforeCalled());
    assertTrue(ruleCallback.isDoIfFailCalled());
    assertFalse(ruleCallback.isDoIfPassCalled());
  }

  public void testEvaluateFails() throws Exception {
    final RuleContext context = new TestRuleContext();
    AbstractRule<Object, String> rule = new TestRule("because", false, true, false);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());
    assertFalse(rule.evaluate(context));
    assertFalse(rule.getActualOutcome());
    assertTrue(rule.isEvaluated());
    assertFalse(rule.passed());
    assertEquals("because", rule.getReason());

    rule = new TestRule("why", true, false, false);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertFalse(rule.getExpectedOutcome());
    assertEquals("why", rule.getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());
    assertFalse(rule.evaluate(context));
    assertTrue(rule.getActualOutcome());
    assertTrue(rule.isEvaluated());
    assertFalse(rule.passed());
    assertEquals("why", rule.getReason());
  }

  public void testEvaluatePasses() throws Exception {
    final RuleContext context = new TestRuleContext();
    AbstractRule<Object, String> rule = new TestRule("because", true, true, true);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());
    assertTrue(rule.evaluate(context));
    assertTrue(rule.getActualOutcome());
    assertTrue(rule.isEvaluated());
    assertTrue(rule.passed());
    assertNull(rule.getReason());

    rule = new TestRule("why", false, false, true);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertFalse(rule.getExpectedOutcome());
    assertEquals("why", rule.getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());
    assertTrue(rule.evaluate(context));
    assertFalse(rule.getActualOutcome());
    assertTrue(rule.isEvaluated());
    assertTrue(rule.passed());
    assertNull(rule.getReason());
  }

  public void testFailed() throws Exception {
    final AbstractRule<Object, String> rule = new TestRule("because");

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());
    assertFalse(rule.evaluate(new TestRuleContext()));
    assertTrue(rule.isEvaluated());
    assertTrue(rule.failed());
    assertFalse(rule.passed());
  }

  public void testFailedWhenNotEvaluated() throws Exception {
    final AbstractRule<Object, String> rule = new TestRule("because");

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());

    try {
      rule.failed();
      fail("Calling failed on a rule that has not been evaluated yet should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The rule (" + rule.getClass().getName() + ") has not been evaluated!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling failed on a rule that has not been evaluated yet threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertFalse(rule.isEvaluated());
  }

  public void testPassed() throws Exception {
    final AbstractRule<Object, String> rule = new TestRule("because", true, true, false);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());
    assertTrue(rule.evaluate(new TestRuleContext()));
    assertTrue(rule.isEvaluated());
    assertFalse(rule.failed());
    assertTrue(rule.passed());
  }

  public void testPassedWhenNotEvaluated() throws Exception {
    final AbstractRule<Object, String> rule = new TestRule("because", true, true, false);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());

    try {
      rule.passed();
      fail("Calling passed on a rule that has not been evaluated yet should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The rule (" + rule.getClass().getName() + ") has not been evaluated!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling passed on a rule that has not been evaluated yet threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertFalse(rule.isEvaluated());
  }

  public void testPassedFailedWhenRuleFailureExeptionIsThrown() throws Exception {
    final AbstractRule<Object, String> rule = new TestRule("because", false, true, true);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());

    try {
      rule.evaluate(new TestRuleContext());
      fail("Calling evaluate on a rule configured to throw a RuntimeException when actual does not match expected should have thrown a RuleFailureException!");
    }
    catch (RuleFailureException e) {
      assertEquals("The evaluation of rule (" + rule.getClass().getName() + ") failed because '"
        + rule.getReason() + "'!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling evaluate on a rule configured to throw a RuntimeException when actual does not match expected threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertTrue(rule.isEvaluated());
    assertTrue(rule.failed());
    assertFalse(rule.passed());
  }

  public void testReset() throws Exception {
    final AbstractRule<Object, String> rule = new TestRule("because");

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", rule.getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());
    assertFalse(rule.evaluate(new TestRuleContext()));
    assertTrue(rule.isEvaluated());
    assertFalse(rule.passed());

    rule.reset();

    assertFalse(rule.isEvaluated());
  }

  private static final class MockVisitor implements Visitor {

    private Rule rule;

    public Rule getRule() {
      return rule;
    }

    public void visit(final Visitable visitableObject) {
      if (visitableObject instanceof Rule) {
        this.rule = (Rule) visitableObject;
      }
    }
  }

  private static final class TestRule extends AbstractRule<Object, String> {

    private static final boolean DEFAULT_ACTUAL_OUTCOME = false;

    private final boolean actualOutcome;

    private final Exception e;

    public TestRule(final String reason) {
      this(reason, DEFAULT_ACTUAL_OUTCOME, DEFAULT_EXPECTED_OUTCOME, DEFAULT_THROW_EXCEPTION_ON_FAILURE);
    }

    public TestRule(final String reason, final boolean expectedOutcome) {
      this(reason, DEFAULT_ACTUAL_OUTCOME, expectedOutcome, DEFAULT_THROW_EXCEPTION_ON_FAILURE);
    }

    public TestRule(final String reason, final boolean expectedOutcome, final boolean throwExceptionOnFailure) {
      this(reason, DEFAULT_ACTUAL_OUTCOME, expectedOutcome, throwExceptionOnFailure);
    }

    public TestRule(final String reason, 
                    final boolean actualOutcome,
                    final boolean expectedOutcome,
                    final boolean throwExceptionOnFailure)
    {
      super(reason, expectedOutcome, throwExceptionOnFailure);
      this.actualOutcome = actualOutcome;
      this.e = null;
    }

    public TestRule(final String reason, final Exception e) {
      super(reason);
      this.actualOutcome = DEFAULT_ACTUAL_OUTCOME;
      this.e = e;
    }

    public TestRule(final String reason, final Exception e, final boolean actualOutcome) {
      super(reason);
      this.actualOutcome = actualOutcome;
      this.e = e;
    }

    protected boolean doEvaluate(final Object context) throws Exception {
      if (ObjectUtil.isNotNull(e)) {
        throw e;
      }
      return actualOutcome;
    }
  }

  private static final class TestRuleCallback<CTX> implements RuleCallback<CTX> {

    private boolean calledDoAfter = false;
    private boolean calledDoBefore = false;
    private boolean calledDoIfFail = false;
    private boolean calledDoIfPass = false;

    private final Object context;

    private final Rule rule;

    public TestRuleCallback(final Rule rule, final Object context) {
      Assert.notNull(rule, "The rule cannot be null!");
      Assert.notNull(context, "The rule context cannot be null!");
      this.rule = rule;
      this.context = context;
    }

    public void doAfter(final CTX context) {
      assertTrue(rule.isEvaluated());
      assertSame(this.context, context);
      calledDoAfter = true;
    }

    public void doBefore(final CTX context) {
      assertFalse(rule.isEvaluated());
      assertSame(this.context, context);
      calledDoBefore = true;
    }

    public void doIfFail(final CTX context) {
      assertTrue(rule.isEvaluated());
      assertFalse(rule.passed());
      assertSame(this.context, context);
      calledDoIfFail = true;
    }

    public void doIfPass(final CTX context) {
      assertTrue(rule.isEvaluated());
      assertTrue(rule.passed());
      assertSame(this.context, context);
      calledDoIfPass = true;
    }

    public boolean isDoAfterCalled() {
      return calledDoAfter;
    }

    public boolean isDoBeforeCalled() {
      return calledDoBefore;
    }

    public boolean isDoIfFailCalled() {
      return calledDoIfFail;
    }
    public boolean isDoIfPassCalled() {
      return calledDoIfPass;
    }

    @Override
    public String toString() {
      final StringBuffer buffer = new StringBuffer("{calledDoAfter = ");
      buffer.append(isDoAfterCalled());
      buffer.append(", calledDoBefore = ").append(isDoBeforeCalled());
      buffer.append(", calledDoIfFail = ").append(isDoIfFailCalled());
      buffer.append(", calledDoIfPass = ").append(isDoIfPassCalled());
      buffer.append("}:").append(getClass().getName());
      return buffer.toString();
    }
  }

  private static final class TestRuleContext implements RuleContext {
  }

}
