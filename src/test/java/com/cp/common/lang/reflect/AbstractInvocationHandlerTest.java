/*
 * AbstractInvocationHandlerTest.java (c) 9 June 2010
 *
 * Copyright (c) 2010, Codeprimate LLC
 * All Rights Reserved
 * @author jblum
 * @version 2010.6.10
 * @see com.cp.common.lang.reflect.AbstractInvocationHandler
 * @see junit.framework.TestCase
 */

package com.cp.common.lang.reflect;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractInvocationHandlerTest extends TestCase {

  public AbstractInvocationHandlerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractInvocationHandlerTest.class);
    //suite.addTest(new AbstractInvocationHandlerTest("testName"));
    return suite;
  }

  public void testInstantiation() throws Exception {
    final Object target = "test";
    AbstractInvocationHandler handler = null;

    assertNull(handler);

    try {
      handler = new TestInvocationHandler(target);
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the AbstractInvocationHandler class with a non-null target object threw an unexpected Throwable ("
        + t + ")!");
    }

    assertNotNull(handler);
    assertEquals(target, handler.getTarget());
    assertEquals(String.class, handler.getTargetClass());
  }

  public void testInstantiationWithNullTargetObject() throws Exception {
    try {
      new TestInvocationHandler(null);
      fail("Instantiating an instance of the AbstractInvocationHandler class with a null target object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The target object of the method invocation cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the AbstractInvocationHandler class with a null target object threw an unexpected Throwable ("
        + t + ")!");
    }
  }

  public void testInvokeClassMethod() throws Exception {
    try {
      final Timestamp target = new Timestamp();
      final AbstractInvocationHandler invocationHandler = new TestInvocationHandler(target);

      final long classTime = Timestamp.getClassTime();
      final long newClassTime = (Long) invocationHandler.invoke(target, Timestamp.class.getMethod("getClassTime", (Class[]) null), null);

      assertFalse(classTime == newClassTime);
    }
    catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

  public void testInvokeInstanceMethod() throws Exception {
    try {
      final Timestamp target = new Timestamp();
      final AbstractInvocationHandler invocationHandler = new TestInvocationHandler(target);

      final long instanceTime = target.getInstanceTime();
      final long newInstanceTime = (Long) invocationHandler.invoke(target, target.getClass().getMethod("getInstanceTime", (Class[]) null), null);

      assertEquals(instanceTime, newInstanceTime);
    }
    catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

  protected static final class TestInvocationHandler extends AbstractInvocationHandler {

    public TestInvocationHandler(final Object target) {
      super(target);
    }
  }

  public static final class Timestamp {

    private final long snapshot;

    public Timestamp() {
      this.snapshot = System.nanoTime();
    }

    public static long getClassTime() {
      return System.nanoTime();
    }

    public long getInstanceTime() {
      return snapshot;
    }
  }

}
