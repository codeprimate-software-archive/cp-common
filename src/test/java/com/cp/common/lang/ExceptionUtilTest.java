/*
 * ExceptionUtilTest.java (c) 2 November 2008
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John Blum
 * @version 2010.4.7
 * @see com.cp.common.lang.ExceptionUtil
 * @see junit.framework.TestCase
 */

package com.cp.common.lang;

import com.cp.common.util.ConfigurationException;
import java.lang.reflect.InvocationTargetException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ExceptionUtilTest extends TestCase {

  public ExceptionUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ExceptionUtilTest.class);
    //suite.addTest(new ExceptionUtilTest("testName"));
    return suite;
  }

  public void testGetCauseIfInvocationTargetException() throws Exception {
    assertNull(ExceptionUtil.getCauseIfInvocationTargetException(null));

    final Exception nullPointer = new NullPointerException("test");

    assertEquals(nullPointer, ExceptionUtil.getCauseIfInvocationTargetException(nullPointer));
    assertEquals(nullPointer, ExceptionUtil.getCauseIfInvocationTargetException(new InvocationTargetException(nullPointer)));

    final Exception configurationError = new ConfigurationException(nullPointer);

    assertEquals(configurationError, ExceptionUtil.getCauseIfInvocationTargetException(configurationError));
    assertEquals(configurationError, ExceptionUtil.getCauseIfInvocationTargetException(new InvocationTargetException(configurationError)));
  }

  public void testGetRootCause() throws Exception {
    final Exception nullPointer = new NullPointerException("test");
    final Exception configurationError = new ConfigurationException(nullPointer);
    final Exception e = new InvocationTargetException(configurationError);

    assertEquals(nullPointer, ExceptionUtil.getRootCause(e));
    assertEquals(nullPointer, ExceptionUtil.getRootCause(nullPointer));
  }

  public void testGetRootCauseWithNullThrowable() throws Exception {
    Throwable rootCause = null;

    try {
      rootCause = ExceptionUtil.getRootCause(null);
      fail("Calling getRootCause with a null Throwable object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Throwable object cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getRootCause with a null Throwable object threw an unexpected Throwable (" + t + ")!");
    }

    assertNull(rootCause);
  }

}
