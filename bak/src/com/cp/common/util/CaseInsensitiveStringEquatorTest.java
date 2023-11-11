/*
 * CaseInsensitiveStringEquatorTest.java (c) 20 October 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2003.10.21
 */

package com.cp.common.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CaseInsensitiveStringEquatorTest extends TestCase {

  public CaseInsensitiveStringEquatorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(CaseInsensitiveStringEquatorTest.class);
    //suite.addTest(new CaseInsensitiveStringEquatorTest("testName"));
    return suite;
  }

  public void testCaseInsensitiveStringEquator() throws Exception {
    assertTrue(CaseInsensitiveStringEquator.getInstance().areEqual(null, null));
    assertTrue(CaseInsensitiveStringEquator.getInstance().areEqual("Test", "Test"));
    assertTrue(CaseInsensitiveStringEquator.getInstance().areEqual("TEST", "TEST"));
    assertTrue(CaseInsensitiveStringEquator.getInstance().areEqual("test", "test"));
    assertTrue(CaseInsensitiveStringEquator.getInstance().areEqual("test", "TEST"));
    assertTrue(CaseInsensitiveStringEquator.getInstance().areEqual("Test", "TeSt"));
    assertTrue(CaseInsensitiveStringEquator.getInstance().areEqual("10010", "10010"));
    assertTrue(CaseInsensitiveStringEquator.getInstance().areEqual("21", "21"));
    assertTrue(CaseInsensitiveStringEquator.getInstance().areEqual("$(@)", "$(@)"));
    assertTrue(CaseInsensitiveStringEquator.getInstance().areEqual("Testing 1.. 2.. 3.. ", "Testing 1.. 2.. 3.. "));
    assertTrue(CaseInsensitiveStringEquator.getInstance().areEqual("**@#10_-'Nm_+=|", "**@#10_-'Nm_+=|"));
    assertFalse(CaseInsensitiveStringEquator.getInstance().areEqual(null, "null"));
    assertFalse(CaseInsensitiveStringEquator.getInstance().areEqual("BadTest", "test"));
    assertFalse(CaseInsensitiveStringEquator.getInstance().areEqual("123", "12345"));
    assertFalse(CaseInsensitiveStringEquator.getInstance().areEqual("12", "twelve"));
    assertFalse(CaseInsensitiveStringEquator.getInstance().areEqual("***", "@@@"));
    assertFalse(CaseInsensitiveStringEquator.getInstance().areEqual("jade@home.com", "right@home.net"));
  }

}
