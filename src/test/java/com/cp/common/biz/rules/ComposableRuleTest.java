/*
 * ComposableRuleTest.java (c) 12 July 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.29
 * @see com.cp.common.biz.rules.ComposableRule
 * @see junit.framework.TestCase
 */

package com.cp.common.biz.rules;

import com.cp.common.lang.Assert;
import com.cp.common.lang.LogicalOperator;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Visitable;
import com.cp.common.util.Visitor;
import java.util.Stack;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ComposableRuleTest extends TestCase {

  public ComposableRuleTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ComposableRuleTest.class);
    //suite.addTest(new ComposableRuleTest("testName"));
    return suite;
  }

  public void testInstantiate() throws Exception {
    final Object context = new Object();

    final Rule<Object, String> leftRule = new TestRule<Object, String>(context, "because");
    final Rule<Object, String> rightRule = new TestRule<Object, String>(context, "why");

    ComposableRule<Object, String> composableRule = null;

    assertNull(composableRule);

    try {
      composableRule = new ComposableRule<Object, String>(leftRule, rightRule, null,
        "Expected did not match actual!", false, true);
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the ComposableRule class with non-null left and right Rule instances and a valid reason threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertNotNull(composableRule);
    assertFalse(composableRule.getActualOutcome());
    assertFalse(composableRule.isEvaluated());
    assertFalse(composableRule.getExpectedOutcome());
    assertEquals("Expected did not match actual!", composableRule.getReasonOfFailure());
    assertTrue(composableRule.isThrowExceptionOnFailure());
    assertSame(leftRule, composableRule.getLeftRule());
    assertEquals(LogicalOperator.AND, composableRule.getOp());
    assertSame(rightRule, composableRule.getRightRule());
  }

  public void testInstantiateWithNullLeftRule() throws Exception {
    final Rule<Object, String> rule = new TestRule<Object, String>(new Object(), "because");

    ComposableRule<Object, String> composableRule = null;

    assertNull(composableRule);

    try {
      composableRule = new ComposableRule<Object, String>(null, rule, LogicalOperator.AND,
        "Expected did not match actual!", true, false);
      fail("Instantiating an instance of the ComposableRule class with a null left Rule instance should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The rule in the left node of the binary decision tree cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the ComposableRule class with a null left Rule instance threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertNull(composableRule);
  }

  public void testInstantiateWithNullReason() throws Exception {
    final Object context = new Object();

    final Rule<Object, String> leftRule = new TestRule<Object, String>(context, "because");
    final Rule<Object, String> rightRule = new TestRule<Object, String>(context, "why");

    ComposableRule<Object, String> composableRule = null;

    assertNull(composableRule);

    try {
      composableRule = new ComposableRule<Object, String>(leftRule, rightRule, LogicalOperator.AND, null, false, true);
      fail("Instantiating an instance of the ComposableRule class with a null reason should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The reason this rule (" + ComposableRule.class.getName() + ") may fail cannot be null!",
        e.getMessage());
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the ComposableRule class with a null reason threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertNull(composableRule);
  }

  public void testInstantiateWithNullRightRule() throws Exception {
    final Rule<Object, String> rule = new TestRule<Object, String>(new Object(), "because");

    ComposableRule<Object, String> composableRule = null;

    assertNull(composableRule);

    try {
      composableRule = new ComposableRule<Object, String>(rule, null, LogicalOperator.AND,
        "Expected did not match actual!", true, false);
      fail("Instantiating an instance of the ComposableRule class with a null right Rule instance should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The rule in the right node of the binary decision tree cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the ComposableRule class with a null right Rule instance threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertNull(composableRule);
  }

  public void testGetOp() throws Exception {
    final Object context = new Object();

    final Rule<Object, String> leftRule = new TestRule<Object, String>(context, "because");
    final Rule<Object, String> rightRule = new TestRule<Object, String>(context, "why");

    ComposableRule<Object, String> rule = new ComposableRule<Object, String>(leftRule, rightRule, null,
      "because, because, because", true, false);

    assertNotNull(rule);
    assertEquals(LogicalOperator.AND, rule.getOp());

    rule = new ComposableRule<Object, String>(leftRule, rightRule, LogicalOperator.OR,
      "Expected did not match actual!", false, true);

    assertNotNull(rule);
    assertEquals(LogicalOperator.OR, rule.getOp());
  }

  public void testAccept() throws Exception {
    final Object context = new Object();

    final MockVisitor visitor = new MockVisitor();

    final Rule<Object, String> leftRule = new TestRule<Object, String>(context, "because");
    final Rule<Object, String> rightRule = new TestRule<Object, String>(context, "why");
    final Rule<Object, String> rule = ComposableRule.compose(leftRule, rightRule, LogicalOperator.AND, "just because");

    assertNotNull(rule);
    assertTrue(rule instanceof ComposableRule);
    assertTrue(visitor.getStackOfRules().isEmpty());

    rule.accept(visitor);

    assertFalse(visitor.getStackOfRules().isEmpty());
    assertEquals(rightRule, visitor.getStackOfRules().pop());
    assertEquals(leftRule, visitor.getStackOfRules().pop());
    assertEquals(rule, visitor.getStackOfRules().pop());
  }

  public void testCompose() throws Exception {
    final Object context = new Object();

    final Rule<Object, String> leftRule = new TestRule<Object, String>(context, "because");
    final Rule<Object, String> rightRule = new TestRule<Object, String>(context, "why");

    Rule<Object, String> rule = ComposableRule.compose(null, null, LogicalOperator.AND,
      "Expected did not match actual!", false, true);

    assertNull(rule);

    rule = ComposableRule.compose(leftRule, null, LogicalOperator.AND, "Expected did not match actual!", false, true);

    assertNotNull(rule);
    assertFalse(rule instanceof ComposableRule);
    assertEquals(leftRule, rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", ((AbstractRule<Object, String>) rule).getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());

    rule = ComposableRule.compose(null, rightRule, LogicalOperator.AND, "Expected did not match actual!", false, true);

    assertNotNull(rule);
    assertFalse(rule instanceof ComposableRule);
    assertEquals(rightRule, rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("why", ((AbstractRule<Object, String>) rule).getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());

    rule = ComposableRule.compose(leftRule, rightRule, LogicalOperator.OR, "Expected did not match actual!", false, true);

    assertNotNull(rule);
    assertTrue(rule instanceof ComposableRule);
    assertNotSame(leftRule, rule);
    assertNotSame(rightRule, rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertFalse(rule.getExpectedOutcome());
    assertEquals("Expected did not match actual!", ((AbstractRule<Object, String>) rule).getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());
    assertEquals(leftRule, ((ComposableRule<Object, String>) rule).getLeftRule());
    assertEquals(LogicalOperator.OR, ((ComposableRule<Object, String>) rule).getOp());
    assertEquals(rightRule, ((ComposableRule<Object, String>) rule).getRightRule());
  }

  public void testComplexCompose() throws Exception {
    final Object context = new Object();

    final Rule<Object, String> rule0 = new TestRule<Object, String>(context, "Zero!", false, true, false);
    final Rule<Object, String> rule1 = new TestRule<Object, String>(context, "One!", false, true, false);
    final Rule<Object, String> rule2 = new TestRule<Object, String>(context, "Two!", true, true, true);
    final Rule<Object, String> rule8 = new TestRule<Object, String>(context, "Eight!", false, false, true);

    Rule<Object, String> leftBranch = ComposableRule.compose(rule0, rule1, LogicalOperator.OR, "Left0|1");
    leftBranch = ComposableRule.compose(rule2, leftBranch, LogicalOperator.AND, "Left2&01");
    leftBranch = ComposableRule.compose(leftBranch, rule8, LogicalOperator.OR, "Left012|8");

    final Rule<Object, String> rule3 = new TestRule<Object, String>(context, "Three!", true, true, false);
    final Rule<Object, String> rule4 = new TestRule<Object, String>(context, "Four!", false, true, false);
    final Rule<Object, String> rule5 = new TestRule<Object, String>(context, "Five!", false, true, false);
    final Rule<Object, String> rule6 = new TestRule<Object, String>(context, "Six!", false, false, false);
    final Rule<Object, String> rule7 = new TestRule<Object, String>(context, "Seven!", true, false, false);

    Rule<Object, String> rightBranch = ComposableRule.compose(rule3, rule4, LogicalOperator.OR, "Right3|4");
    rightBranch = ComposableRule.compose(rightBranch, rule5, LogicalOperator.AND, "Right34&5");

    final Rule<Object, String> rightRightBranch = ComposableRule.compose(rule6, rule7, LogicalOperator.OR, "RightRight6|7");
    rightBranch = ComposableRule.compose(rightBranch,  rightRightBranch, LogicalOperator.OR, "Right345|67");

    final Rule<Object, String> rule = ComposableRule.compose(leftBranch, rightBranch, LogicalOperator.AND, "because", true, true);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("because", ((AbstractRule) rule).getReasonOfFailure());
    assertTrue(rule.isThrowExceptionOnFailure());
    assertFalse(rule0.isEvaluated());
    assertFalse(rule1.isEvaluated());
    assertFalse(rule2.isEvaluated());
    assertFalse(rule3.isEvaluated());
    assertFalse(rule4.isEvaluated());
    assertFalse(rule5.isEvaluated());
    assertFalse(rule6.isEvaluated());
    assertFalse(rule7.isEvaluated());
    assertFalse(rule8.isEvaluated());
    assertTrue(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertTrue(rule.getActualOutcome());
    assertTrue(rule.passed());
    assertNull(rule.getReason());
    assertTrue(rule0.isEvaluated());
    assertFalse(rule0.passed());
    assertTrue(rule1.isEvaluated());
    assertFalse(rule1.passed());
    assertTrue(rule2.isEvaluated());
    assertTrue(rule2.passed());
    assertTrue(rule3.isEvaluated());
    assertTrue(rule3.passed());
    assertTrue(rule4.isEvaluated());
    assertFalse(rule4.passed());
    assertTrue(rule5.isEvaluated());
    assertFalse(rule5.passed());
    assertTrue(rule6.isEvaluated());
    assertTrue(rule6.passed());
    assertTrue(rule7.isEvaluated());
    assertFalse(rule7.passed());
    assertTrue(rule8.isEvaluated());
    assertTrue(rule8.passed());
  }

  public void testComplexComposeThrowsRuleEvaluationException() throws Exception {
    final Object context = new Object();

    final Rule<Object, String> rule0 = new TestRule<Object, String>(context, "because 0", false, true, false);
    final Rule<Object, String> rule1 = new TestRule<Object, String>(context, "because 1", new IllegalStateException("Not Configured!"));
    final Rule<Object, String> rule2 = new TestRule<Object, String>(context, "because 2", false, false, true);

    Rule<Object, String> rule = ComposableRule.compose(rule1, rule2, LogicalOperator.AND, "Expected did not match actual!");
    rule = ComposableRule.compose(rule0, rule, LogicalOperator.OR, "Expected (true); but was (false)!");

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("Expected (true); but was (false)!", ((ComposableRule) rule).getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());
    assertFalse(rule0.isEvaluated());
    assertFalse(rule1.isEvaluated());
    assertFalse(rule2.isEvaluated());

    try {
      rule.evaluate(context);
      fail("Calling evaluate on the ComposableRule class where rule 1 will throw a RuleEvaluationException should have failed!");
    }
    catch (RuleEvaluationException e) {
      assertEquals("Rule (" + rule1.getClass().getName() + ") threw an unexpected Throwable!", e.getMessage());
      assertTrue(e.getCause() instanceof IllegalStateException);
      assertEquals("Not Configured!", e.getCause().getMessage());
    }
    catch (Throwable t) {
      fail("Calling evaluate on the ComposableRule class where rule 1 will throw a RuleEvaluationException threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertFalse(rule.isEvaluated());
    assertTrue(rule0.isEvaluated());
    assertFalse(rule0.passed());
    assertFalse(rule1.isEvaluated());
    assertFalse(rule2.isEvaluated());
  }

  public void testComplexComposeThrowsRuleFailureException() throws Exception {
    final Object context = new Object();

    final Rule<Object, String> rule0 = new TestRule<Object, String>(context, "because 0", false, true, false);
    final Rule<Object, String> rule1 = new TestRule<Object, String>(context, "because 1", false, true, true);
    final Rule<Object, String> rule2 = new TestRule<Object, String>(context, "because 2", false, false, true);

    Rule<Object, String> rule = ComposableRule.compose(rule1, rule2, LogicalOperator.AND, "Expected (true); but was (false)!");
    rule = ComposableRule.compose(rule0, rule, LogicalOperator.OR, "Expected did not match actual!");

    assertTrue(rule instanceof ComposableRule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("Expected did not match actual!", ((ComposableRule) rule).getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());
    assertFalse(rule0.isEvaluated());
    assertFalse(rule1.isEvaluated());
    assertFalse(rule2.isEvaluated());

    try {
      rule.evaluate(context);
      fail("Calling evaluate on the ComposableRule class with rule 1 set to throw a RuntimeException on failure should have thrown a RuleFailureException!");
    }
    catch (RuleFailureException e) {
      assertEquals("The evaluation of rule (" + rule1.getClass().getName() + ") failed because '"
        + rule1.getReason() + "'!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling evaluate on the ComposableRule class with rule 1 set to throw a RuntimeException on failure threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertTrue(rule.isEvaluated());
    assertTrue(rule0.isEvaluated());
    assertFalse(rule0.passed());
    assertTrue(rule1.isEvaluated());
    assertFalse(rule1.passed());
    assertFalse(rule2.isEvaluated());

    final Rule<Object, String> rule7 = new TestRule<Object, String>(context, "because 7", false, true, false);
    final Rule<Object, String> rule8 = new TestRule<Object, String>(context, "because 8", true, false, false);
    final Rule<Object, String> rule9 = new TestRule<Object, String>(context, "because 8", true, true, true);

    rule = ComposableRule.compose(rule7, rule8, LogicalOperator.OR, "Expected did not match actual!", true, true);
    rule = ComposableRule.compose(rule, rule9, LogicalOperator.AND, "Expected (true); but was (false)!", true, false);

    assertNotNull(rule);
    assertFalse(rule.getActualOutcome());
    assertFalse(rule.isEvaluated());
    assertTrue(rule.getExpectedOutcome());
    assertEquals("Expected (true); but was (false)!", ((ComposableRule) rule).getReasonOfFailure());
    assertFalse(rule.isThrowExceptionOnFailure());
    assertFalse(rule7.isEvaluated());
    assertFalse(rule8.isEvaluated());
    assertFalse(rule9.isEvaluated());

    try {
      rule.evaluate(context);
      fail("Calling evaluate on the ComposableRule class with the inner rule composition set to throw a RuntimeException on failure should have thrown a RuleFailureException!");
    }
    catch (RuleFailureException e) {
      assertEquals("The evaluation of rule (" + ComposableRule.class.getName()
        + ") failed because 'Expected did not match actual!'!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling evaluate on the ComposableRule class with the inner rule composition set to throw a RuntimeException on failure threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertTrue(rule.isEvaluated());
    assertTrue(rule7.isEvaluated());
    assertFalse(rule7.passed());
    assertTrue(rule8.isEvaluated());
    assertFalse(rule8.passed());
    assertFalse(rule9.isEvaluated());
  }

  public void testEvaluateUsingLogicalAnd() throws Exception {
    final Object context = new Object();

    Rule<Object, String> leftRule = new TestRule<Object, String>(context, "because", true, true, false);
    Rule<Object, String> rightRule = new TestRule<Object, String>(context, "why", false, false, false);
    Rule<Object, String> rule = ComposableRule.compose(leftRule, rightRule, LogicalOperator.AND,
      "Expected did not match actual!");

    assertTrue(rule instanceof ComposableRule);
    assertFalse(rule.isEvaluated());
    assertTrue(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertNull(rule.getReason());
    assertTrue(rule.passed());
    assertTrue(leftRule.isEvaluated());
    assertTrue(leftRule.passed());
    assertTrue(rightRule.isEvaluated());
    assertTrue(rightRule.passed());

    leftRule = new TestRule<Object, String>(context, "why", false, true, false);
    rule = ComposableRule.compose(leftRule, rightRule, LogicalOperator.AND, "Expected did not match actual!");

    assertTrue(rule instanceof ComposableRule);
    assertFalse(rule.isEvaluated());
    assertFalse(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertEquals("Expected did not match actual!", rule.getReason());
    assertFalse(rule.passed());
    assertTrue(leftRule.isEvaluated());
    assertFalse(leftRule.passed());
    assertTrue(rightRule.isEvaluated());
    assertTrue(rightRule.passed());

    leftRule = new TestRule<Object, String>(context, "because", true, true, false);
    rightRule = new TestRule<Object, String>(context, "why", true, false, false);
    rule = ComposableRule.compose(leftRule, rightRule, LogicalOperator.AND, "Expected did not match actual!");

    assertTrue(rule instanceof ComposableRule);
    assertFalse(rule.isEvaluated());
    assertFalse(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertEquals("Expected did not match actual!", rule.getReason());
    assertFalse(rule.passed());
    assertTrue(leftRule.isEvaluated());
    assertTrue(leftRule.passed());
    assertTrue(rightRule.isEvaluated());
    assertFalse(rightRule.passed());

    leftRule = new TestRule<Object, String>(context, "because", false, true, false);
    rightRule = new TestRule<Object, String>(context, "why", true, false, false);
    rule = ComposableRule.compose(leftRule, rightRule, LogicalOperator.AND, "Expected did not match actual!");

    assertTrue(rule instanceof ComposableRule);
    assertFalse(rule.isEvaluated());
    assertFalse(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertEquals("Expected did not match actual!", rule.getReason());
    assertFalse(rule.passed());
    assertTrue(leftRule.isEvaluated());
    assertFalse(leftRule.passed());
    assertTrue(rightRule.isEvaluated());
    assertFalse(rightRule.passed());
  }

  public void testEvaluateUsingLogicalNotWithLogicalAnd() throws Exception {
    final Object context = new Object();

    Rule<Object, String> leftRule = new TestRule<Object, String>(context, "because", false, true, false);
    Rule<Object, String> rightRule = new TestRule<Object, String>(context, "why", true, false, false);
    Rule<Object, String> rule = ComposableRule.compose(leftRule, rightRule, LogicalOperator.getNot(LogicalOperator.AND),
      "Expected did not match actual!");

    assertTrue(rule instanceof ComposableRule);
    assertFalse(rule.isEvaluated());
    assertTrue(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertNull(rule.getReason());
    assertTrue(rule.passed());
    assertTrue(leftRule.isEvaluated());
    assertFalse(leftRule.passed());
    assertTrue(rightRule.isEvaluated());
    assertFalse(rightRule.passed());

    leftRule = new TestRule<Object, String>(context, "why", true, true, false);
    rule = ComposableRule.compose(leftRule, rightRule, LogicalOperator.getNot(LogicalOperator.AND), "Expected did not match actual!");

    assertTrue(rule instanceof ComposableRule);
    assertFalse(rule.isEvaluated());
    assertTrue(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertNull(rule.getReason());
    assertTrue(rule.passed());
    assertTrue(leftRule.isEvaluated());
    assertTrue(leftRule.passed());
    assertTrue(rightRule.isEvaluated());
    assertFalse(rightRule.passed());

    leftRule = new TestRule<Object, String>(context, "because", false, true, false);
    rightRule = new TestRule<Object, String>(context, "why", false, false, false);
    rule = ComposableRule.compose(leftRule, rightRule, LogicalOperator.getNot(LogicalOperator.AND), "Expected did not match actual!");

    assertTrue(rule instanceof ComposableRule);
    assertFalse(rule.isEvaluated());
    assertTrue(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertNull(rule.getReason());
    assertTrue(rule.passed());
    assertTrue(leftRule.isEvaluated());
    assertFalse(leftRule.passed());
    assertTrue(rightRule.isEvaluated());
    assertTrue(rightRule.passed());

    leftRule = new TestRule<Object, String>(context, "because", true, true, false);
    rightRule = new TestRule<Object, String>(context, "why", false, false, false);
    rule = ComposableRule.compose(leftRule, rightRule, LogicalOperator.getNot(LogicalOperator.AND), "Expected did not match actual!");

    assertTrue(rule instanceof ComposableRule);
    assertFalse(rule.isEvaluated());
    assertFalse(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertEquals("Expected did not match actual!", rule.getReason());
    assertFalse(rule.passed());
    assertTrue(leftRule.isEvaluated());
    assertTrue(leftRule.passed());
    assertTrue(rightRule.isEvaluated());
    assertTrue(rightRule.passed());
  }

  public void testEvaluateUsingLogicalNotWithLogicalOr() throws Exception {
    final Object context = new Object();

    Rule<Object, String> leftRule = new TestRule<Object, String>(context, "because", false, true, false);
    Rule<Object, String> rightRule = new TestRule<Object, String>(context, "why", true, false, false);
    Rule<Object, String> rule = ComposableRule.compose(leftRule, rightRule, LogicalOperator.getNot(LogicalOperator.OR),
      "Expected did not match actual!");

    assertTrue(rule instanceof ComposableRule);
    assertFalse(rule.isEvaluated());
    assertTrue(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertNull(rule.getReason());
    assertTrue(rule.passed());
    assertTrue(leftRule.isEvaluated());
    assertFalse(leftRule.passed());
    assertTrue(rightRule.isEvaluated());
    assertFalse(rightRule.passed());

    leftRule = new TestRule<Object, String>(context, "because", true, true, false);
    rule = ComposableRule.compose(leftRule, rightRule, LogicalOperator.getNot(LogicalOperator.OR),
      "Expected did not match actual!");

    assertTrue(rule instanceof ComposableRule);
    assertFalse(rule.isEvaluated());
    assertFalse(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertEquals("Expected did not match actual!", rule.getReason());
    assertFalse(rule.passed());
    assertTrue(leftRule.isEvaluated());
    assertTrue(leftRule.passed());
    assertTrue(rightRule.isEvaluated());
    assertFalse(rightRule.passed());

    leftRule = new TestRule<Object, String>(context, "because", false, true, false);
    rightRule = new TestRule<Object, String>(context, "why", false, false, false);
    rule = ComposableRule.compose(leftRule, rightRule, LogicalOperator.getNot(LogicalOperator.OR),
      "Expected did not match actual!");

    assertTrue(rule instanceof ComposableRule);
    assertFalse(rule.isEvaluated());
    assertFalse(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertEquals("Expected did not match actual!", rule.getReason());
    assertFalse(rule.passed());
    assertTrue(leftRule.isEvaluated());
    assertFalse(leftRule.passed());
    assertTrue(rightRule.isEvaluated());
    assertTrue(rightRule.passed());

    leftRule = new TestRule<Object, String>(context, "because", true, true, false);
    rightRule = new TestRule<Object, String>(context, "why", false, false, false);
    rule = ComposableRule.compose(leftRule, rightRule, LogicalOperator.getNot(LogicalOperator.OR),
      "Expected did not match actual!");

    assertTrue(rule instanceof ComposableRule);
    assertFalse(rule.isEvaluated());
    assertFalse(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertEquals("Expected did not match actual!", rule.getReason());
    assertFalse(rule.passed());
    assertTrue(leftRule.isEvaluated());
    assertTrue(leftRule.passed());
    assertTrue(rightRule.isEvaluated());
    assertTrue(rightRule.passed());
  }

  public void testEvaluateUsingLogicalOr() throws Exception {
    final Object context = new Object();

    Rule<Object, String> leftRule = new TestRule<Object, String>(context, "because", true, true, false);
    Rule<Object, String> rightRule = new TestRule<Object, String>(context, "why", false, false, false);
    Rule<Object, String> rule = ComposableRule.compose(leftRule, rightRule, LogicalOperator.OR,
      "Expected did not match actual!");

    assertTrue(rule instanceof ComposableRule);
    assertFalse(rule.isEvaluated());
    assertTrue(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertNull(rule.getReason());
    assertTrue(rule.passed());
    assertTrue(leftRule.isEvaluated());
    assertTrue(leftRule.passed());
    assertTrue(rightRule.isEvaluated());
    assertTrue(rightRule.passed());

    leftRule = new TestRule<Object, String>(context, "why", false, true, false);
    rule = ComposableRule.compose(leftRule, rightRule, LogicalOperator.OR, "Expected did not match actual!");

    assertTrue(rule instanceof ComposableRule);
    assertFalse(rule.isEvaluated());
    assertTrue(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertNull(rule.getReason());
    assertTrue(rule.passed());
    assertTrue(leftRule.isEvaluated());
    assertFalse(leftRule.passed());
    assertTrue(rightRule.isEvaluated());
    assertTrue(rightRule.passed());

    leftRule = new TestRule<Object, String>(context, "why", true, true, false);
    rightRule = new TestRule<Object, String>(context, "why", true, false, false);
    rule = ComposableRule.compose(leftRule, rightRule, LogicalOperator.OR, "Expected did not match actual!");

    assertTrue(rule instanceof ComposableRule);
    assertFalse(rule.isEvaluated());
    assertTrue(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertNull(rule.getReason());
    assertTrue(rule.passed());
    assertTrue(leftRule.isEvaluated());
    assertTrue(leftRule.passed());
    assertTrue(rightRule.isEvaluated());
    assertFalse(rightRule.passed());

    leftRule = new TestRule<Object, String>(context, "because", false, true, false);
    rightRule = new TestRule<Object, String>(context, "why", true, false, false);
    rule = ComposableRule.compose(leftRule, rightRule, LogicalOperator.OR, "Expected did not match actual!");

    assertTrue(rule instanceof ComposableRule);
    assertFalse(rule.isEvaluated());
    assertFalse(rule.evaluate(context));
    assertTrue(rule.isEvaluated());
    assertEquals("Expected did not match actual!", rule.getReason());
    assertFalse(rule.passed());
    assertTrue(leftRule.isEvaluated());
    assertFalse(leftRule.passed());
    assertTrue(rightRule.isEvaluated());
    assertFalse(rightRule.passed());
  }

  private static final class MockVisitor implements Visitor {

    private final Stack<Rule> ruleStack = new Stack<Rule>();

    public Stack<Rule> getStackOfRules() {
      return ruleStack;
    }

    public void visit(final Visitable visitableObject) {
      if (visitableObject instanceof Rule) {
        ruleStack.push((Rule) visitableObject);
      }
    }
  }

  private static final class TestRule<CTX, REASON> extends AbstractRule<CTX, REASON> {

    private final boolean actualOutcome;

    private final CTX context;

    private final Exception e;

    public TestRule(final CTX context, final REASON reason) {
      this(context, reason, DEFAULT_EXPECTED_OUTCOME, DEFAULT_EXPECTED_OUTCOME, DEFAULT_THROW_EXCEPTION_ON_FAILURE);
    }

    public TestRule(final CTX context, final REASON reason, final boolean expectedOutcome) {
      this(context, reason, expectedOutcome, expectedOutcome, DEFAULT_THROW_EXCEPTION_ON_FAILURE);
    }

    public TestRule(final CTX context,
                    final REASON reason,
                    final boolean expectedOutcome,
                    final boolean throwExceptionOnFailure) {
      this(context, reason, expectedOutcome, expectedOutcome, throwExceptionOnFailure);
    }

    public TestRule(final CTX context,
                    final REASON reason,
                    final boolean actualOutcome,
                    final boolean expectedOutcome,
                    final boolean throwExceptionOnFailure) {
      super(reason, expectedOutcome, throwExceptionOnFailure);
      Assert.notNull(context, "The context evaluated by this rule cannot be null!");
      this.context = context;
      this.actualOutcome = actualOutcome;
      this.e = null;
    }

    public TestRule(final CTX context, final REASON reason, final Exception e) {
      super(reason);
      this.context = context;
      this.actualOutcome = DEFAULT_EXPECTED_OUTCOME;
      this.e = e;
    }

    @Override
    protected boolean doEvaluate(final CTX context) throws Exception {
      assertSame(this.context, context);

      if (ObjectUtil.isNotNull(e)) {
        throw e;
      }

      return actualOutcome;
    }
  }

}
