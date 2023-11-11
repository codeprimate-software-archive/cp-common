/*
 * LayoutManagerUtilTest.java (c) 29 May 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.5.29
 * @see com.cp.common.awt.LayoutManagerUtil
 */

package com.cp.common.awt;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class LayoutManagerUtilTest extends TestCase {

  public LayoutManagerUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(LayoutManagerUtilTest.class);
    return suite;
  }

  public void testGetGridBagConstraints() throws Exception {
    final Insets expectedInsets = new Insets(5, 5, 5, 5);
    final GridBagConstraints constraints = LayoutManagerUtil.getConstraints(100, 100, 200, 400, 25, 50,
      GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, expectedInsets, 10, 10);

    assertNotNull(constraints);
    assertEquals(100, constraints.gridx);
    assertEquals(100, constraints.gridy);
    assertEquals(200, constraints.gridwidth);
    assertEquals(400, constraints.gridheight);
    assertEquals(25.0, constraints.weightx);
    assertEquals(50.0, constraints.weighty);
    assertEquals(GridBagConstraints.NORTHWEST, constraints.anchor);
    assertEquals(GridBagConstraints.BOTH, constraints.fill);
    assertEquals(expectedInsets, constraints.insets);
    assertEquals(10, constraints.ipadx);
    assertEquals(10, constraints.ipady);
  }

  public void testSetGridBagConstraints() throws Exception {
    final Insets expectedInsets = new Insets(20, 10, 20, 10);

    final GridBagConstraints constraints = new GridBagConstraints(0, 0, 100, 100, 50, 50, GridBagConstraints.CENTER,
      GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0);

    assertNotNull(constraints);
    assertEquals(0, constraints.gridx);
    assertEquals(0, constraints.gridy);
    assertEquals(100, constraints.gridwidth);
    assertEquals(100, constraints.gridheight);
    assertEquals(50.0, constraints.weightx);
    assertEquals(50.0, constraints.weighty);
    assertEquals(GridBagConstraints.CENTER, constraints.anchor);
    assertEquals(GridBagConstraints.NONE, constraints.fill);
    assertEquals(new Insets(5, 5, 5, 5), constraints.insets);
    assertEquals(0, constraints.ipadx);
    assertEquals(0, constraints.ipady);

    LayoutManagerUtil.setConstraints(constraints, 100, 100, 200, 400, 25, 75, GridBagConstraints.SOUTHEAST,
      GridBagConstraints.BOTH, expectedInsets, 5, 5);

    assertNotNull(constraints);
    assertEquals(100, constraints.gridx);
    assertEquals(100, constraints.gridy);
    assertEquals(200, constraints.gridwidth);
    assertEquals(400, constraints.gridheight);
    assertEquals(25.0, constraints.weightx);
    assertEquals(75.0, constraints.weighty);
    assertEquals(GridBagConstraints.SOUTHEAST, constraints.anchor);
    assertEquals(GridBagConstraints.BOTH, constraints.fill);
    assertEquals(expectedInsets, constraints.insets);
    assertEquals(5, constraints.ipadx);
    assertEquals(5, constraints.ipady);
  }

}
