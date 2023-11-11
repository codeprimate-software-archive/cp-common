/*
 * ProcessRendererTest.java (c) 11 February 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.17
 * @see com.cp.common.beans.DefaultProcess
 * @see com.cp.common.beans.Process
 * @see com.cp.common.log4j.ProcessRenderer
 */

package com.cp.common.log4j;

import com.cp.common.beans.DefaultProcess;
import com.cp.common.beans.Process;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.or.ObjectRenderer;

public class ProcessRendererTest extends TestCase {

  public ProcessRendererTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ProcessRendererTest.class);
    //suite.addTest(new ProcessRendererTest("testName"));
    return suite;
  }

  private Process getProcess(final String processName) {
    return new DefaultProcess(processName);
  }

  public void testDoRenderer() throws Exception {
    final ObjectRenderer processRenderer = new ProcessRenderer();

    assertEquals("consoleApplication", processRenderer.doRender(getProcess("consoleApplication")));
    assertEquals("webApplication", processRenderer.doRender(getProcess("webApplication")));
    assertEquals("batchProcess", processRenderer.doRender(getProcess("batchProcess")));
  }

  public void testNonProcessRenderer() throws Exception {
    final ObjectRenderer processRenderer = new ProcessRenderer();

    assertEquals("TEST", processRenderer.doRender("TEST"));
    assertEquals("null", processRenderer.doRender(null));
  }

}
