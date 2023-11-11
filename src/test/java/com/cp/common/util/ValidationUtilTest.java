/*
 * ValidationUtilTest.java (c) 15 September 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.9.15
 * @see com.cp.common.util.ValidationUtil
 */

package com.cp.common.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ValidationUtilTest extends TestCase {

  public ValidationUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ValidationUtilTest.class);
    //suite.addTest(new ValidationUtilTest("testName"));
    return suite;
  }

  public void testIsValidSsn() throws Exception {
    assertTrue(ValidationUtil.isValidSsn("333-22-4444"));
    assertFalse(ValidationUtil.isValidSsn("333224444"));
    assertFalse(ValidationUtil.isValidSsn("12-34-5678"));
    assertFalse(ValidationUtil.isValidSsn("123-4-5678"));
    assertFalse(ValidationUtil.isValidSsn("123-45-678"));
    assertFalse(ValidationUtil.isValidSsn("12-3-456"));
    assertFalse(ValidationUtil.isValidSsn("   -  -    "));
    assertFalse(ValidationUtil.isValidSsn("--"));
    assertFalse(ValidationUtil.isValidSsn("@!1-%7_*7(2"));
  }

}
