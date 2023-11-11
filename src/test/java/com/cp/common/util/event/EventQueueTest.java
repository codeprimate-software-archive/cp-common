/*
 * EventQueueTest.java (c) 5 September 2009
 *
 * Copyright (c) 2003, Codeprimate
 * All Rights Reserved
 * @author jblum
 * @version 2009.9.5
 * @see com.cp.common.util.event.EventQueue
 * @see junit.framework.TestCase
 */

package com.cp.common.util.event;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class EventQueueTest extends TestCase {

  public EventQueueTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(EventQueueTest.class);
    //suite.addTest(new EventQueueTest("testName"));
    return suite;
  }

  public void testIsEmpty() throws Exception {
    final EventQueue queue = new EventQueue();

    assertTrue(queue.isEmpty());

    queue.push(new Object());

    assertFalse(queue.isEmpty());
  }

  public void testSetMaxSize() throws Exception {
    final EventQueue queue = new EventQueue();

    assertEquals(EventQueue.DEFAULT_MAX_SIZE, queue.getMaxSize());
    assertEquals(EventQueue.DEFAULT_THRESHOLD, queue.getThreshold());

    queue.setMaxSize(Integer.MAX_VALUE);

    assertEquals(Integer.MAX_VALUE, queue.getMaxSize());
  }

  public void testSetMaxSizeWithValueLessThanThreshold() throws Exception {
    final EventQueue queue = new EventQueue();

    assertEquals(EventQueue.DEFAULT_MAX_SIZE, queue.getMaxSize());
    assertEquals(EventQueue.DEFAULT_THRESHOLD, queue.getThreshold());

    try {
      queue.setMaxSize(256);
      fail("Calling setMaxSize with a value less than threshold (" + queue.getThreshold()
        + ") should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The maximum size of the event queue (256) must be greater than the threshold ("
        + queue.getThreshold() + ")!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling setMaxSize with a value less than threshold (" + queue.getThreshold()
        + ") threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testSetThreshold() throws Exception {
    final EventQueue queue = new EventQueue();

    assertEquals(EventQueue.DEFAULT_THRESHOLD, queue.getThreshold());

    queue.setThreshold(0);

    assertEquals(0, queue.getThreshold());

    queue.setThreshold(10);

    assertEquals(10, queue.getThreshold());

    queue.setThreshold(-1);

    assertEquals(0, queue.getThreshold());
  }

  public void testSetThresholdWithValueGreaterThanMaxSize() throws Exception {
    final EventQueue queue = new EventQueue();

    assertEquals(EventQueue.DEFAULT_MAX_SIZE, queue.getMaxSize());
    assertEquals(EventQueue.DEFAULT_THRESHOLD, queue.getThreshold());

    try {
      queue.setThreshold(Integer.MAX_VALUE);
      fail("Calling setThreshold with Integer.MAX_VALUE should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The threshold (" + Integer.MAX_VALUE + ") must be less than the maximum size of the event queue ("
        + queue.getMaxSize() + ")!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling setThreshold with Integer.MAX_VALUE should threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

}
