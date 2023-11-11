/*
 * TimerTest.java (c) 15 January 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.3.18
 */

package com.cp.common.util;

import com.cp.common.test.util.TestUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TimerTest extends TestCase {

  public TimerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(TimerTest.class);
    //suite.addTest(new TimerTest("testName"));
    return suite;
  }

  public void testTimer() throws Exception {
    final Timer timer = new Timer("TestTimer");

    assertNotNull(Timer.getInstance("TestTimer"));
    assertEquals("TestTimer", Timer.getInstance("TestTimer").getName());

    timer.start();
    Thread.sleep(100);
    timer.stop();

    TestUtil.assertPositive(timer.diff());
    assertTrue(timer.getStopTime() > timer.getStartTime());
  }

}
