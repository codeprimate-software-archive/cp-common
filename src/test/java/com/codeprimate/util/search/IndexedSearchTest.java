/*
 * IndexedSearchTest.java (c) 25 August 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.8.25
 */

package com.codeprimate.util.search;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class IndexedSearchTest extends TestCase {

  public IndexedSearchTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(IndexedSearchTest.class);
    //suite.addTest(new IndexedSearchTest("testName"));
    return suite;
  }

  public void test() throws Exception {
    fail("Not Implemented!");
  }

}
