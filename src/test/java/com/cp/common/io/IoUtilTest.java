/*
 * IoUtilTest.java (c) 15 February 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.2.15
 * @see com.cp.common.io.IoUtil
 * @see junit.framework.TestCase
 */

package com.cp.common.io;

import com.cp.common.util.SystemException;
import java.io.Closeable;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class IoUtilTest extends TestCase {

  private final Mockery context = new Mockery();

  public IoUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(IoUtilTest.class);
    //suite.addTest(new IoUtilTest("testName"));
    return suite;
  }

  public void testClose() throws Exception {
    final Closeable c = context.mock(Closeable.class);

    context.checking(new Expectations() {{
      oneOf(c).close();
    }});

    try {
      IoUtil.close(c);
    }
    catch (Throwable t) {
      fail("Calling the close method on the Closeable object threw an unexpected Throwable (" + t.getMessage() + ")!");
    }

    context.assertIsSatisfied();
  }

  public void testCloseThrowsException() throws Exception {
    final Closeable c = context.mock(Closeable.class);

    context.checking(new Expectations() {{
      oneOf(c).close();
      will(throwException(new IOException("Failed to close!")));
    }});

    try {
      IoUtil.close(c);
      fail("Calling the close method on the Closeable object should have thrown a SystemException!");
    }
    catch (SystemException e) {
      assertEquals("Failed to close Closeable!", e.getMessage());
      assertTrue(e.getCause() instanceof IOException);
      assertEquals("Failed to close!", e.getCause().getMessage());
    }
    catch (Throwable t) {
      fail("Calling the close method on the Closeable object threw an unexpected Throwable (" + t.getMessage() + ")!");
    }

    context.assertIsSatisfied();
  }

}
