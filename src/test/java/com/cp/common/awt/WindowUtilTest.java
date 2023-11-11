/*
 * WindowUtilTest.java (c) 29 May 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.5.29
 * @see com.cp.common.awt.WindowUtilTest
 */

package com.cp.common.awt;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class WindowUtilTest extends TestCase {

  public WindowUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(WindowUtilTest.class);
    return suite;
  }

  public void testGetDesktopLocation() throws Exception {
    final Dimension windowSize = new Dimension(400, 300);

    final Point expectedDesktopLocation = new Point(120, 90);
    final Point actualDesktopLocation = WindowUtil.getDesktopLocation(windowSize);

    assertNotNull(actualDesktopLocation);
    assertEquals(expectedDesktopLocation, actualDesktopLocation);
  }

  public void testGetDialogLocation() throws Exception {
    final Container container = new Container() {
      @Override
      public Point getLocationOnScreen() {
        return new Point(200, 100);
      }

      @Override
      public Dimension getSize() {
        return new Dimension(800, 600);
      }
    };

    final Dimension dialogSize = new Dimension(400, 300);

    final Point expectedDialogLocation = new Point(400, 250);
    final Point actualDialogLocation = WindowUtil.getDialogLocation(container, dialogSize);

    assertNotNull(actualDialogLocation);
    assertEquals(expectedDialogLocation, actualDialogLocation);
  }

}
