/*
 * NonLocalDateTest.java (c) 25 October 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2003.11.16
 */

package com.cp.common.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class NonLocalDateTest extends TestCase {

  public NonLocalDateTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(NonLocalDateTest.class);
    //suite.addTest(new NonLocalDateTest("testName"));
    return suite;
  }

  public void testNonLocalDate() throws Exception {
    fail("Not Implemented!");
  }

}
