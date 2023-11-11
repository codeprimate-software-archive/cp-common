/*
 * LogicalOperatorTest.java (c) 25 January 2008
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.4.14
 * @see com.cp.common.lang.LogicalOperator
 * @see junit.framework.TestCase
 */

package com.cp.common.lang;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class LogicalOperatorTest extends TestCase {

  public LogicalOperatorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(LogicalOperatorTest.class);
    //suite.addTest(new LogicalOperatorTest("testName"));
    return suite;
  }

  public void testAndLogicalOperator() throws Exception {
    assertTrue(LogicalOperator.getAnd().op(true, true));
    assertFalse(LogicalOperator.getAnd().op(true, false));
    assertFalse(LogicalOperator.getAnd().op(false, true));
    assertFalse(LogicalOperator.getAnd().op(false, false));
  }

  public void testNotLogicalOperator() throws Exception {
    assertFalse(LogicalOperator.getNot(LogicalOperator.AND).op(true, true));
    assertTrue(LogicalOperator.getNot(LogicalOperator.AND).op(true, false));
    assertTrue(LogicalOperator.getNot(LogicalOperator.AND).op(false, true));
    assertTrue(LogicalOperator.getNot(LogicalOperator.AND).op(false, false));
    assertFalse(LogicalOperator.getNot(LogicalOperator.OR).op(true, true));
    assertFalse(LogicalOperator.getNot(LogicalOperator.OR).op(true, false));
    assertFalse(LogicalOperator.getNot(LogicalOperator.OR).op(false, true));
    assertTrue(LogicalOperator.getNot(LogicalOperator.OR).op(false, false));
  }

  public void testNotLogicalOperatorDoubleNegative() throws Exception {
    assertTrue(LogicalOperator.getNot(LogicalOperator.getNot(LogicalOperator.AND)).op(true, true));
    assertFalse(LogicalOperator.getNot(LogicalOperator.getNot(LogicalOperator.AND)).op(true, false));
    assertFalse(LogicalOperator.getNot(LogicalOperator.getNot(LogicalOperator.AND)).op(false, true));
    assertFalse(LogicalOperator.getNot(LogicalOperator.getNot(LogicalOperator.AND)).op(false, false));
    assertTrue(LogicalOperator.getNot(LogicalOperator.getNot(LogicalOperator.OR)).op(true, true));
    assertTrue(LogicalOperator.getNot(LogicalOperator.getNot(LogicalOperator.OR)).op(true, false));
    assertTrue(LogicalOperator.getNot(LogicalOperator.getNot(LogicalOperator.OR)).op(false, true));
    assertFalse(LogicalOperator.getNot(LogicalOperator.getNot(LogicalOperator.OR)).op(false, false));
  }

  public void testOrLogicalOperator() throws Exception {
    assertTrue(LogicalOperator.getOr().op(true, true));
    assertTrue(LogicalOperator.getOr().op(true, false));
    assertTrue(LogicalOperator.getOr().op(false, true));
    assertFalse(LogicalOperator.getOr().op(false, false));
  }

}
