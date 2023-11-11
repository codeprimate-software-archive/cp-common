/*
 * UserRendererTest.java (c) 25 October 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.17
 * @see com.cp.common.beans.DefaultUser
 * @see com.cp.common.beans.User
 * @see com.cp.common.log4j.UserRenderer
 */

package com.cp.common.log4j;

import com.cp.common.beans.DefaultUser;
import com.cp.common.beans.User;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.or.ObjectRenderer;

public class UserRendererTest extends TestCase {

  public UserRendererTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(UserRendererTest.class);
    //suite.addTest(new UserRendererTest("testName"));
    return suite;
  }

  private User getUser(final String username) {
    return new DefaultUser(username);
  }

  public void testDoRender() throws Exception {
    final ObjectRenderer userRenderer = new UserRenderer();

    assertEquals("root", userRenderer.doRender(getUser("root")));
    assertEquals("admin", userRenderer.doRender(getUser("admin")));
    assertEquals("blumj", userRenderer.doRender(getUser("blumj")));
  }

  public void testNonUserRendering() throws Exception {
    final ObjectRenderer userRenderer = new UserRenderer();

    assertEquals("TEST", userRenderer.doRender("TEST"));
    assertEquals("null", userRenderer.doRender(null));
  }

}
