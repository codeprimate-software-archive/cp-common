/*
 * ClassRendererTest.java (c) 11 February 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.2.11
 * @see com.cp.common.log4j.ClassRenderer
 */

package com.cp.common.log4j;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.or.ObjectRenderer;

public class ClassRendererTest extends TestCase {

  public ClassRendererTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ClassRendererTest.class);
    //suite.addTest(new ClassRendererTest("testName"));
    return suite;
  }

  public void testDoRender() throws Exception {
    final ObjectRenderer objectRenderer = new ClassRenderer();

    assertEquals("null", objectRenderer.doRender(null));
    assertEquals("java.lang.Boolean", objectRenderer.doRender(Boolean.TRUE));
    assertEquals("java.lang.Character", objectRenderer.doRender('@'));
    assertEquals("java.lang.Integer", objectRenderer.doRender(2));
    assertEquals("java.lang.Double", objectRenderer.doRender(3.14159));
    assertEquals("java.lang.String", objectRenderer.doRender("test"));
  }

}
