/*
 * ImageUtilTest.java (c) 29 May 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.5.29
 * @see com.cp.common.awt.ImageUtil
 */

package com.cp.common.awt;

import java.awt.Dimension;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ImageUtilTest extends TestCase {

  public ImageUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ImageUtilTest.class);
    return suite;
  }

  public void testValidateBoundingBox() throws Exception {
    try {
      ImageUtil.validateBoundingBox(new Dimension(100, 100));
    }
    catch (Throwable t) {
      fail("Validating a non-null, non-zero area bounding box threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testValidateNullBoundingBox() throws Exception {
    try {
      ImageUtil.validateBoundingBox(null);
      fail("Calling validateBoundingBox with a null Dimension object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bounded box defining the area containing the imnage cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling validateBoundingBox with a null Dimension object threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testValidateInvalidBoundingBox() throws Exception {
    try {
      ImageUtil.validateBoundingBox(new Dimension(100, 0));
      fail("Calling validateBoundingBox with an invalid Dimension object should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The area of the bounding box cannot be negative or zero!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling validateBoundingBox with an invalid Dimension object threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

}
