/*
 * DefaultFilterTest.java (c) 8 August 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.8.8
 * @see com.cp.common.util.DefaultFilter
 * @see junit.framework.TestCase
 */

package com.cp.common.util;

import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DefaultFilterTest extends TestCase {

  public DefaultFilterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DefaultFilterTest.class);
    //suite.addTest(new DefaultFilterTest("testName"));
    return suite;
  }

  public void testAccept() throws Exception {
    final Filter<Object> filter = new DefaultFilter<Object>(true);

    assertTrue(filter.accept(true));
    assertTrue(filter.accept('A'));
    assertTrue(filter.accept(Calendar.getInstance()));
    assertTrue(filter.accept(0));
    assertTrue(filter.accept(1));
    assertTrue(filter.accept(Math.PI));
    assertTrue(filter.accept("test"));
    assertTrue(filter.accept("TEST"));
  }

  public void testReject() throws Exception {
    final Filter<Object> filter = new DefaultFilter<Object>(false);

    assertFalse(filter.accept(true));
    assertFalse(filter.accept('A'));
    assertFalse(filter.accept(Calendar.getInstance()));
    assertFalse(filter.accept(0));
    assertFalse(filter.accept(1));
    assertFalse(filter.accept(Math.PI));
    assertFalse(filter.accept("test"));
    assertFalse(filter.accept("TEST"));
  }

}
