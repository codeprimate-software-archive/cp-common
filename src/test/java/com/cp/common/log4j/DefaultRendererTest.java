/*
 * DefaultRendererTest.java (c) 10 November 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.11.10
 * @see com.cp.common.log4j.DefaultRenderer
 * @see junit.framework.TestCase
 */

package com.cp.common.log4j;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DefaultRendererTest extends TestCase {

  public DefaultRendererTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DefaultRendererTest.class);
    return suite;
  }

  public void testDoRenderer() throws Exception {
    assertNull(DefaultRenderer.INSTANCE.doRender(null));
    assertEquals("true", DefaultRenderer.INSTANCE.doRender(Boolean.TRUE));
    assertEquals("A", DefaultRenderer.INSTANCE.doRender('A'));
    assertEquals("0", DefaultRenderer.INSTANCE.doRender(0));
    assertEquals(String.valueOf(Math.PI), DefaultRenderer.INSTANCE.doRender(Math.PI));
    assertEquals("test", DefaultRenderer.INSTANCE.doRender("test"));
  }

}
