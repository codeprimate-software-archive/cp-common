/*
 * NullRendererTest.java (c) 25 October 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.10.25
 */

package com.cp.common.log4j;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class NullRendererTest extends TestCase {

  public NullRendererTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(NullRendererTest.class);
    //suite.addTest(new NullRendererTest("testName"));
    return suite;
  }

  public void testDoRenderer() throws Exception {
    final NullRenderer nullRenderer = new NullRenderer();

    assertEquals("null", nullRenderer.doRender(null));
    assertEquals("null", nullRenderer.doRender("NULL"));
    assertEquals("null", nullRenderer.doRender("TEST"));
  }

}
