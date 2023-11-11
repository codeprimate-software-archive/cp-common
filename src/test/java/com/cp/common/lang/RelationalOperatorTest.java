/*
 * RelationalOperatorTest.java (c) 15 February 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.7.13
 * @see com.cp.common.lang.RelationalOperator
 * @see junit.framework.TestCase
 */

package com.cp.common.lang;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class RelationalOperatorTest extends TestCase {

  public RelationalOperatorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(RelationalOperatorTest.class);
    //suite.addTest(new RelationalOperatorTest("testName"));
    return suite;
  }

  public void testEqualTo() throws Exception {
    final RelationalOperator<Integer> equalToInteger = RelationalOperator.getEqualTo(2);

    assertTrue(equalToInteger.accept(2));
    assertFalse(equalToInteger.accept(-2));
    assertFalse(equalToInteger.accept(22));
    assertFalse(equalToInteger.accept(0));

    final RelationalOperator<String> equalToString = RelationalOperator.getEqualTo("test");

    assertTrue(equalToString.accept("test"));
    assertFalse(equalToString.accept("TEST"));
    assertFalse(equalToString.accept("tester"));
    assertFalse(equalToString.accept("testing"));
  }

  public void testGreaterThan() throws Exception {
    final RelationalOperator<Integer> greaterThanInteger = RelationalOperator.getGreaterThan(0);

    assertFalse(greaterThanInteger.accept(-1));
    assertFalse(greaterThanInteger.accept(0));
    assertTrue(greaterThanInteger.accept(1));

    final RelationalOperator<String> greaterThanString = RelationalOperator.getGreaterThan("bbb");

    assertFalse(greaterThanString.accept("aaa"));
    assertFalse(greaterThanString.accept("bbb"));
    assertTrue(greaterThanString.accept("ccc"));
  }

  public void testGreaterThanAndLessThan() throws Exception {
    final RelationalOperator<Integer> greaterThanAndLessThanInteger = RelationalOperator.getGreaterThanAndLessThan(1, 3);

    assertFalse(greaterThanAndLessThanInteger.accept(0));
    assertFalse(greaterThanAndLessThanInteger.accept(1));
    assertTrue(greaterThanAndLessThanInteger.accept(2));
    assertFalse(greaterThanAndLessThanInteger.accept(3));
    assertFalse(greaterThanAndLessThanInteger.accept(4));

    final RelationalOperator<String> greaterThanAndLessThanString = RelationalOperator.getGreaterThanAndLessThan("bbb", "ddd");

    assertFalse(greaterThanAndLessThanString.accept("aaa"));
    assertFalse(greaterThanAndLessThanString.accept("bbb"));
    assertTrue(greaterThanAndLessThanString.accept("ccc"));
    assertFalse(greaterThanAndLessThanString.accept("ddd"));
    assertFalse(greaterThanAndLessThanString.accept("eee"));
  }

  public void testGreaterThanAndLessThanEqualTo() throws Exception {
    final RelationalOperator<Integer> greaterThanAndLessThanEqualToInteger = RelationalOperator.getGreaterThanAndLessThanEqualTo(1, 3);

    assertFalse(greaterThanAndLessThanEqualToInteger.accept(0));
    assertFalse(greaterThanAndLessThanEqualToInteger.accept(1));
    assertTrue(greaterThanAndLessThanEqualToInteger.accept(2));
    assertTrue(greaterThanAndLessThanEqualToInteger.accept(3));
    assertFalse(greaterThanAndLessThanEqualToInteger.accept(4));

    final RelationalOperator<String> greaterThanAndLessThanEqualToString = RelationalOperator.getGreaterThanAndLessThanEqualTo("bbb", "ddd");

    assertFalse(greaterThanAndLessThanEqualToString.accept("aaa"));
    assertFalse(greaterThanAndLessThanEqualToString.accept("bbb"));
    assertTrue(greaterThanAndLessThanEqualToString.accept("ccc"));
    assertTrue(greaterThanAndLessThanEqualToString.accept("ddd"));
    assertFalse(greaterThanAndLessThanEqualToString.accept("eee"));
  }

  public void testGreaterThanEqualTo() throws Exception {
    final RelationalOperator<Integer> greaterThanEqualToInteger = RelationalOperator.getGreaterThanEqualTo(0);

    assertFalse(greaterThanEqualToInteger.accept(-1));
    assertTrue(greaterThanEqualToInteger.accept(0));
    assertTrue(greaterThanEqualToInteger.accept(1));

    final RelationalOperator<String> greaterThanEqualToString = RelationalOperator.getGreaterThanEqualTo("bbb");

    assertFalse(greaterThanEqualToString.accept("aaa"));
    assertTrue(greaterThanEqualToString.accept("bbb"));
    assertTrue(greaterThanEqualToString.accept("ccc"));
  }

  public void testGreaterThanEqualToAndLessThan() throws Exception {
    final RelationalOperator<Integer> greaterThanEqualToAndLessThanInteger = RelationalOperator.getGreaterThanEqualToAndLessThan(1, 3);

    assertFalse(greaterThanEqualToAndLessThanInteger.accept(0));
    assertTrue(greaterThanEqualToAndLessThanInteger.accept(1));
    assertTrue(greaterThanEqualToAndLessThanInteger.accept(2));
    assertFalse(greaterThanEqualToAndLessThanInteger.accept(3));
    assertFalse(greaterThanEqualToAndLessThanInteger.accept(4));

    final RelationalOperator<String> greaterThanEqualToAndLessThanString = RelationalOperator.getGreaterThanEqualToAndLessThan("bbb", "ddd");

    assertFalse(greaterThanEqualToAndLessThanString.accept("aaa"));
    assertTrue(greaterThanEqualToAndLessThanString.accept("bbb"));
    assertTrue(greaterThanEqualToAndLessThanString.accept("ccc"));
    assertFalse(greaterThanEqualToAndLessThanString.accept("ddd"));
    assertFalse(greaterThanEqualToAndLessThanString.accept("eee"));
  }

  public void testGreaterThanEqualToAndLessThanEqualTo() throws Exception {
    final RelationalOperator<Integer> greaterThanEqualToAndLessThanEqualToInteger = RelationalOperator.getGreaterThanEqualToAndLessThanEqualTo(1, 3);

    assertFalse(greaterThanEqualToAndLessThanEqualToInteger.accept(0));
    assertTrue(greaterThanEqualToAndLessThanEqualToInteger.accept(1));
    assertTrue(greaterThanEqualToAndLessThanEqualToInteger.accept(2));
    assertTrue(greaterThanEqualToAndLessThanEqualToInteger.accept(3));
    assertFalse(greaterThanEqualToAndLessThanEqualToInteger.accept(4));

    final RelationalOperator<String> greaterThanEqualToAndLessThanEqualToString = RelationalOperator.getGreaterThanEqualToAndLessThanEqualTo("bbb", "ddd");

    assertFalse(greaterThanEqualToAndLessThanEqualToString.accept("aaa"));
    assertTrue(greaterThanEqualToAndLessThanEqualToString.accept("bbb"));
    assertTrue(greaterThanEqualToAndLessThanEqualToString.accept("ccc"));
    assertTrue(greaterThanEqualToAndLessThanEqualToString.accept("ddd"));
    assertFalse(greaterThanEqualToAndLessThanEqualToString.accept("eee"));
  }

  public void testLessThan() throws Exception {
    final RelationalOperator<Integer> lessThanInteger = RelationalOperator.getLessThan(0);

    assertTrue(lessThanInteger.accept(-1));
    assertFalse(lessThanInteger.accept(0));
    assertFalse(lessThanInteger.accept(1));

    final RelationalOperator<String> lessThanString = RelationalOperator.getLessThan("bbb");

    assertTrue(lessThanString.accept("aaa"));
    assertFalse(lessThanString.accept("bbb"));
    assertFalse(lessThanString.accept("ccc"));
  }

  public void testLessThanOrGreaterThan() throws Exception {
    final RelationalOperator<Integer> lessThanOrGreaterThanInteger = RelationalOperator.getLessThanOrGreaterThan(1, 3);

    assertTrue(lessThanOrGreaterThanInteger.accept(0));
    assertFalse(lessThanOrGreaterThanInteger.accept(1));
    assertFalse(lessThanOrGreaterThanInteger.accept(2));
    assertFalse(lessThanOrGreaterThanInteger.accept(3));
    assertTrue(lessThanOrGreaterThanInteger.accept(4));

    final RelationalOperator<String> lessThanOrGreaterThanString = RelationalOperator.getLessThanOrGreaterThan("bbb", "ddd");

    assertTrue(lessThanOrGreaterThanString.accept("aaa"));
    assertFalse(lessThanOrGreaterThanString.accept("bbb"));
    assertFalse(lessThanOrGreaterThanString.accept("ccc"));
    assertFalse(lessThanOrGreaterThanString.accept("ddd"));
    assertTrue(lessThanOrGreaterThanString.accept("eee"));
  }

  public void testLessThanOrGreaterThanEqualTo() throws Exception {
    final RelationalOperator<Integer> lessThanOrGreaterThanEqualToInteger = RelationalOperator.getLessThanOrGreaterThanEqualTo(1, 3);

    assertTrue(lessThanOrGreaterThanEqualToInteger.accept(0));
    assertFalse(lessThanOrGreaterThanEqualToInteger.accept(1));
    assertFalse(lessThanOrGreaterThanEqualToInteger.accept(2));
    assertTrue(lessThanOrGreaterThanEqualToInteger.accept(3));
    assertTrue(lessThanOrGreaterThanEqualToInteger.accept(4));

    final RelationalOperator<String> lessThanOrGreaterThanEqualToString = RelationalOperator.getLessThanOrGreaterThanEqualTo("bbb", "ddd");

    assertTrue(lessThanOrGreaterThanEqualToString.accept("aaa"));
    assertFalse(lessThanOrGreaterThanEqualToString.accept("bbb"));
    assertFalse(lessThanOrGreaterThanEqualToString.accept("ccc"));
    assertTrue(lessThanOrGreaterThanEqualToString.accept("ddd"));
    assertTrue(lessThanOrGreaterThanEqualToString.accept("eee"));
  }

  public void testLessThanEqualTo() throws Exception {
    final RelationalOperator<Integer> lessThanEqualToInteger = RelationalOperator.getLessThanEqualTo(0);

    assertTrue(lessThanEqualToInteger.accept(-1));
    assertTrue(lessThanEqualToInteger.accept(0));
    assertFalse(lessThanEqualToInteger.accept(1));

    final RelationalOperator<String> lessThanEqualToString = RelationalOperator.getLessThanEqualTo("bbb");

    assertTrue(lessThanEqualToString.accept("aaa"));
    assertTrue(lessThanEqualToString.accept("bbb"));
    assertFalse(lessThanEqualToString.accept("ccc"));
  }

  public void testLessThanEqualToOrGreaterThan() throws Exception {
    final RelationalOperator<Integer> lessThanEqualToOrGreaterThanInteger = RelationalOperator.getLessThanEqualToOrGreaterThan(1, 3);

    assertTrue(lessThanEqualToOrGreaterThanInteger.accept(0));
    assertTrue(lessThanEqualToOrGreaterThanInteger.accept(1));
    assertFalse(lessThanEqualToOrGreaterThanInteger.accept(2));
    assertFalse(lessThanEqualToOrGreaterThanInteger.accept(3));
    assertTrue(lessThanEqualToOrGreaterThanInteger.accept(4));

    final RelationalOperator<String> lessThanEqualToGreaterThanString = RelationalOperator.getLessThanEqualToOrGreaterThan("bbb", "ddd");

    assertTrue(lessThanEqualToGreaterThanString.accept("aaa"));
    assertTrue(lessThanEqualToGreaterThanString.accept("bbb"));
    assertFalse(lessThanEqualToGreaterThanString.accept("ccc"));
    assertFalse(lessThanEqualToGreaterThanString.accept("ddd"));
    assertTrue(lessThanEqualToGreaterThanString.accept("eee"));
  }

  public void testLessThanEqualToOrGreaterThanEqualTo() throws Exception {
    final RelationalOperator<Integer> lessThanEqualToOrGreaterThanEqualToInteger = RelationalOperator.getLessThanEqualToOrGreaterThanEqualTo(1, 3);

    assertTrue(lessThanEqualToOrGreaterThanEqualToInteger.accept(0));
    assertTrue(lessThanEqualToOrGreaterThanEqualToInteger.accept(1));
    assertFalse(lessThanEqualToOrGreaterThanEqualToInteger.accept(2));
    assertTrue(lessThanEqualToOrGreaterThanEqualToInteger.accept(3));
    assertTrue(lessThanEqualToOrGreaterThanEqualToInteger.accept(4));

    final RelationalOperator<String> lessThanEqualToGreaterThanEqualToString = RelationalOperator.getLessThanEqualToOrGreaterThanEqualTo("bbb", "ddd");

    assertTrue(lessThanEqualToGreaterThanEqualToString.accept("aaa"));
    assertTrue(lessThanEqualToGreaterThanEqualToString.accept("bbb"));
    assertFalse(lessThanEqualToGreaterThanEqualToString.accept("ccc"));
    assertTrue(lessThanEqualToGreaterThanEqualToString.accept("ddd"));
    assertTrue(lessThanEqualToGreaterThanEqualToString.accept("eee"));
  }

}
