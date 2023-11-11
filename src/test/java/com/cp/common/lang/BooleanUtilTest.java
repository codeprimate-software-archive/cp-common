/*
 * BooleanUtilTest.java (c) 13 September 2003
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.4.6
 * @see com.cp.common.lang.BooleanUtil
 * @see junit.framework.TestCase
 */

package com.cp.common.lang;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class BooleanUtilTest extends TestCase {

  public BooleanUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(BooleanUtilTest.class);
    //suite.addTest(new BooleanUtilTest("testName"));
    return suite;
  }

  public void testAnd() throws Exception {
    assertTrue(BooleanUtil.and(Boolean.TRUE, Boolean.TRUE));
    assertFalse(BooleanUtil.and(Boolean.FALSE, Boolean.FALSE));
    assertFalse(BooleanUtil.and(Boolean.FALSE, Boolean.TRUE));
    assertFalse(BooleanUtil.and(Boolean.FALSE, null));
    assertFalse(BooleanUtil.and(Boolean.TRUE, null));
    assertFalse(BooleanUtil.and(null, null));
  }

  public void testNegate() throws Exception {
    assertTrue(BooleanUtil.negate(null));
    assertTrue(BooleanUtil.negate(false));
    assertTrue(BooleanUtil.negate(Boolean.FALSE));
    assertFalse(BooleanUtil.negate(true));
    assertFalse(BooleanUtil.negate(Boolean.TRUE));
  }

  public void testOr() throws Exception {
    assertTrue(BooleanUtil.or(Boolean.TRUE, Boolean.TRUE));
    assertTrue(BooleanUtil.or(Boolean.TRUE, Boolean.FALSE));
    assertTrue(BooleanUtil.or(Boolean.TRUE, null));
    assertFalse(BooleanUtil.or(Boolean.FALSE, Boolean.FALSE));
    assertFalse(BooleanUtil.or(Boolean.FALSE, null));
    assertFalse(BooleanUtil.or(null, null));
  }

  public void testToBoolean() throws Exception {
    assertEquals(Boolean.TRUE, BooleanUtil.toBoolean(true));
    assertEquals(Boolean.FALSE, BooleanUtil.toBoolean(false));
  }

  public void testToInteger() throws Exception {
    assertEquals(1, BooleanUtil.toInteger(true, 1, 0));
    assertEquals(1, BooleanUtil.toInteger(Boolean.TRUE, 1, 0));
    assertEquals(0, BooleanUtil.toInteger(false, 1, 0));
    assertEquals(0, BooleanUtil.toInteger(Boolean.FALSE, 1, 0));
    assertEquals(0, BooleanUtil.toInteger(null, 1, 0));
  }

  public void testToString() throws Exception {
    assertEquals("tru", BooleanUtil.toString(true, "tru", "fals"));
    assertEquals("tru", BooleanUtil.toString(Boolean.TRUE, "tru", "fals"));
    assertEquals("fals", BooleanUtil.toString(false, "tru", "fals"));
    assertEquals("fals", BooleanUtil.toString(Boolean.FALSE, "tru", "fals"));
    assertEquals("fals", BooleanUtil.toString(null, "tru", "fals"));
  }

  public void testValueOfWithBoolean() throws Exception {
    assertFalse(BooleanUtil.valueOf((Boolean) null));
    assertFalse(BooleanUtil.valueOf(false));
    assertFalse(BooleanUtil.valueOf(Boolean.FALSE));
    assertFalse(BooleanUtil.valueOf(new Boolean(false)));
    assertTrue(BooleanUtil.valueOf(true));
    assertTrue(BooleanUtil.valueOf(Boolean.TRUE));
    assertTrue(BooleanUtil.valueOf(new Boolean(true)));
  }

  public void testValueOfWithInteger() throws Exception {
    assertTrue(BooleanUtil.valueOf(Integer.MIN_VALUE));
    assertTrue(BooleanUtil.valueOf(-1));
    assertFalse(BooleanUtil.valueOf(0));
    assertTrue(BooleanUtil.valueOf(1));
    assertTrue(BooleanUtil.valueOf(Integer.MAX_VALUE));
  }

  public void testValueOfWithString() throws Exception {
    assertTrue(BooleanUtil.valueOf("true"));
    assertTrue(BooleanUtil.valueOf("TRUE"));
    assertTrue(BooleanUtil.valueOf("True"));
    assertTrue(BooleanUtil.valueOf("1"));
    assertTrue(BooleanUtil.valueOf("on"));
    assertTrue(BooleanUtil.valueOf("ON"));
    assertTrue(BooleanUtil.valueOf("On"));
    assertTrue(BooleanUtil.valueOf("one"));
    assertTrue(BooleanUtil.valueOf("ONE"));
    assertTrue(BooleanUtil.valueOf("One"));
    assertTrue(BooleanUtil.valueOf("y"));
    assertTrue(BooleanUtil.valueOf("Y"));
    assertTrue(BooleanUtil.valueOf("yes"));
    assertTrue(BooleanUtil.valueOf("YES"));
    assertTrue(BooleanUtil.valueOf("Yes"));
    assertFalse(BooleanUtil.valueOf("false"));
    assertFalse(BooleanUtil.valueOf("0"));
    assertFalse(BooleanUtil.valueOf("-1"));
    assertFalse(BooleanUtil.valueOf("I"));
    assertFalse(BooleanUtil.valueOf("l"));
    assertFalse(BooleanUtil.valueOf("zero"));
    assertFalse(BooleanUtil.valueOf("off"));
    assertFalse(BooleanUtil.valueOf("no"));
    assertFalse(BooleanUtil.valueOf("n"));
  }

  public void testXor() throws Exception {
    assertTrue(BooleanUtil.xor(Boolean.TRUE, Boolean.FALSE));
    assertTrue(BooleanUtil.xor(Boolean.TRUE, null));
    assertFalse(BooleanUtil.xor(Boolean.FALSE, Boolean.FALSE));
    assertFalse(BooleanUtil.xor(Boolean.TRUE, Boolean.TRUE));
    assertFalse(BooleanUtil.xor(Boolean.FALSE, null));
    assertFalse(BooleanUtil.xor(null, null));
  }

  public void testIsAllFalse() throws Exception {
    assertTrue(BooleanUtil.isAllFalse(false));
    assertTrue(BooleanUtil.isAllFalse(false, false));
    assertTrue(BooleanUtil.isAllFalse(false, false, false));
    assertFalse(BooleanUtil.isAllFalse(false, false, true));
    assertFalse(BooleanUtil.isAllFalse(true, false, false));
    assertFalse(BooleanUtil.isAllFalse(true, false, true));
    assertFalse(BooleanUtil.isAllFalse(true, true, true));
  }

  public void testIsAllTrue() throws Exception {
    assertTrue(BooleanUtil.isAllTrue(true));
    assertTrue(BooleanUtil.isAllTrue(true, true));
    assertTrue(BooleanUtil.isAllTrue(true, true, true));
    assertFalse(BooleanUtil.isAllTrue(true, true, false));
    assertFalse(BooleanUtil.isAllTrue(false, true, true));
    assertFalse(BooleanUtil.isAllTrue(false, true, false));
    assertFalse(BooleanUtil.isAllTrue(false, false, false));
  }

  public void testIsAnyFalse() throws Exception {
    assertTrue(BooleanUtil.isAnyFalse(false));
    assertTrue(BooleanUtil.isAnyFalse(true, false));
    assertTrue(BooleanUtil.isAnyFalse(true, true, false));
    assertTrue(BooleanUtil.isAnyFalse(true, false, true));
    assertTrue(BooleanUtil.isAnyFalse(true, false, false));
    assertTrue(BooleanUtil.isAnyFalse(false, true, true));
    assertTrue(BooleanUtil.isAnyFalse(false, true, false));
    assertTrue(BooleanUtil.isAnyFalse(false, false, true));
    assertTrue(BooleanUtil.isAnyFalse(false, false, false));
    assertFalse(BooleanUtil.isAnyFalse(true));
    assertFalse(BooleanUtil.isAnyFalse(true, true));
    assertFalse(BooleanUtil.isAnyFalse(true, true, true));
  }

  public void testIsAnyTrue() throws Exception {
    assertTrue(BooleanUtil.isAnyTrue(true));
    assertTrue(BooleanUtil.isAnyTrue(false, true));
    assertTrue(BooleanUtil.isAnyTrue(false, false, true));
    assertTrue(BooleanUtil.isAnyTrue(false, true, false));
    assertTrue(BooleanUtil.isAnyTrue(false, true, true));
    assertTrue(BooleanUtil.isAnyTrue(true, false, false));
    assertTrue(BooleanUtil.isAnyTrue(true, false, true));
    assertTrue(BooleanUtil.isAnyTrue(true, true, false));
    assertTrue(BooleanUtil.isAnyTrue(true, true, true));
    assertFalse(BooleanUtil.isAnyTrue(false));
    assertFalse(BooleanUtil.isAnyTrue(false, false));
    assertFalse(BooleanUtil.isAnyTrue(false, false, false));
  }

  public void testIsExclusivelyFalse() throws Exception {
    assertTrue(BooleanUtil.isExclusivelyFalse(false));
    assertTrue(BooleanUtil.isExclusivelyFalse(false, true));
    assertTrue(BooleanUtil.isExclusivelyFalse(false, true, true));
    assertTrue(BooleanUtil.isExclusivelyFalse(true, false, true));
    assertTrue(BooleanUtil.isExclusivelyFalse(true, true, false));
    assertFalse(BooleanUtil.isExclusivelyFalse(true));
    assertFalse(BooleanUtil.isExclusivelyFalse(true, true));
    assertFalse(BooleanUtil.isExclusivelyFalse(true, false, false));
    assertFalse(BooleanUtil.isExclusivelyFalse(false, true, false));
    assertFalse(BooleanUtil.isExclusivelyFalse(false, false, true));
    assertFalse(BooleanUtil.isExclusivelyFalse(false, false, false));
  }

  public void testIsExclusivelyTrue() throws Exception {
    assertTrue(BooleanUtil.isExclusivelyTrue(true));
    assertTrue(BooleanUtil.isExclusivelyTrue(true, false));
    assertTrue(BooleanUtil.isExclusivelyTrue(true, false, false));
    assertTrue(BooleanUtil.isExclusivelyTrue(false, true, false));
    assertTrue(BooleanUtil.isExclusivelyTrue(false, false, true));
    assertFalse(BooleanUtil.isExclusivelyTrue(false));
    assertFalse(BooleanUtil.isExclusivelyTrue(false, false));
    assertFalse(BooleanUtil.isExclusivelyTrue(false, true, true));
    assertFalse(BooleanUtil.isExclusivelyTrue(true, false, true));
    assertFalse(BooleanUtil.isExclusivelyTrue(true, true, false));
    assertFalse(BooleanUtil.isExclusivelyTrue(true, true, true));
  }

}
