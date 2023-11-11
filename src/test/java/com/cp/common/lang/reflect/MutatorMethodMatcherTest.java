/*
 * MutatorMethodMatcherTest.java (c) 10 June 2010
 *
 * Copyright (c) 2010, Codeprimate LLC
 * All Rights Reserved
 * @author jblum
 * @version 2010.06.10
 * @see com.cp.common.lang.reflect.MutatorMethodMatcher
 * @see junit.framework.TestCase
 */

package com.cp.common.lang.reflect;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MutatorMethodMatcherTest extends TestCase {

  public MutatorMethodMatcherTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(MutatorMethodMatcherTest.class);
    //suite.addTest(new MutatorMethodMatcherTest("testName"));
    return suite;
  }

  public void testMatchesWithNullMethod() throws Exception {
    try {
      MutatorMethodMatcher.INSTANCE.matches(null);
      fail("Calling matches with a null Method object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The method object cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling matches with a null Method object threw an unexpected Throwable (" + t + ")!");
    }
  }

}
