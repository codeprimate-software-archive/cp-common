/*
 * DefaultEquatorTest.java (c) 20 October 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2003.10.20
 */

package com.cp.common.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DefaultEquatorTest extends TestCase {

  public DefaultEquatorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DefaultEquatorTest.class);
    //suite.addTest(new DefaultEquatorTest("testName"));
    return suite;
  }

  public void testDefaultEquator() throws Exception {
    assertTrue(DefaultEquator.getInstance().areEqual(null, null));
    assertTrue(DefaultEquator.getInstance().areEqual(new Boolean(true), Boolean.TRUE));
    assertTrue(DefaultEquator.getInstance().areEqual("Test", "Test"));
    assertTrue(DefaultEquator.getInstance().areEqual(new Character('A'), new Character('A')));
    assertTrue(DefaultEquator.getInstance().areEqual(new Integer(2), new Integer(2)));
    assertTrue(DefaultEquator.getInstance().areEqual(new Double(3.14159), new Double(3.14159)));
    assertFalse(DefaultEquator.getInstance().areEqual(Boolean.TRUE, Boolean.FALSE));
    assertFalse(DefaultEquator.getInstance().areEqual("null", null));
    assertFalse(DefaultEquator.getInstance().areEqual("A", new Character('A')));
    assertFalse(DefaultEquator.getInstance().areEqual(new Integer(8), new Short((short) 8)));
    assertFalse(DefaultEquator.getInstance().areEqual(new Double(3.0e8), new Float(1.21e2)));
  }

}
