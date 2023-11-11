/*
 * EventSourceTest.java (c) 5 September 2009
 *
 * Copyright (c) 2003, Codeprimate
 * All Rights Reserved
 * @author jblum
 * @version 2009.9.5
 * @see com.cp.common.util.event.EventSource
 * @see junit.framework.TestCase
 */

package com.cp.common.util.event;

import java.util.EventObject;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class EventSourceTest extends TestCase {

  private final Mockery context = new Mockery();

  public EventSourceTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(EventSourceTest.class);
    //suite.addTest(new EventSourceTest("testName"));
    return suite;
  }

  public void testAddListener() throws Exception {
    final EventHandler mockEventHandler = context.mock(EventHandler.class);
    final EventSource eventSource = new TestEventSource();

    assertTrue(eventSource.addListener(mockEventHandler));
  }

  public void testAddNullListener() throws Exception {
    final EventSource eventSource = new TestEventSource();

    try {
      eventSource.addListener(null);
      fail("Calling addListener with a null EventHandler object (listener) should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("Cannot add a null event handler (listener) to this event source ("
      + eventSource.getClass().getName() + ")!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling addListener with a null EventHandler object (listener) threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testNotifyListeners() throws Exception {
    final EventSource eventSource = new TestEventSource();
    final EventObject event = new EventObject(eventSource);
    final EventHandler mockEventHandler = context.mock(EventHandler.class);

    context.checking(new Expectations() {{
      oneOf(mockEventHandler).handle(with(event));
    }});

    assertTrue(eventSource.addListener(mockEventHandler));

    eventSource.notifyListeners(event);

    context.assertIsSatisfied();
  }

  public void testRemoveListener() throws Exception {
    final EventSource eventSource = new TestEventSource();
    final EventHandler mockEventHandler = context.mock(EventHandler.class);

    assertFalse(eventSource.removeListener(mockEventHandler));
    assertTrue(eventSource.addListener(mockEventHandler));
    assertFalse(eventSource.removeListener(null));
    assertTrue(eventSource.removeListener(mockEventHandler));
  }

  private static final class TestEventSource extends EventSource {
  }

}
