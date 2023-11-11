/*
 * AbstractMethodMatcherTest.java (c) 10 June 2010
 *
 * Copyright (c) 2010, Codeprimate LLC
 * All Rights Reserved
 * @author jblum
 * @version 2010.06.10
 * @see com.cp.common.lang.reflect.AbstractMethodMatcher
 * @see junit.framework.TestCase
 */

package com.cp.common.lang.reflect;

import java.lang.reflect.Method;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractMethodMatcherTest extends TestCase {

  public AbstractMethodMatcherTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractMethodMatcherTest.class);
    //suite.addTest(new AbstractMethodMatcherTest("testName"));
    return suite;
  }

  public void testAccept() throws Exception {
    final TestMethodMatcher methodMatcher = new TestMethodMatcher();

    assertFalse(methodMatcher.isMatchesCalled());
    assertFalse(methodMatcher.accept(null));
    assertTrue(methodMatcher.isMatchesCalled());
  }

  protected static final class TestMethodMatcher extends AbstractMethodMatcher {

    private boolean matchesCalled;

    public boolean isMatchesCalled() {
      final boolean tempMatchesCalled = matchesCalled;
      this.matchesCalled = false;
      return tempMatchesCalled;
    }

    public boolean matches(final Method method) {
      this.matchesCalled = true;
      return false;
    }
  }

}
