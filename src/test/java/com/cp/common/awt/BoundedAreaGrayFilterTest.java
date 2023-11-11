/*
 * BoundedAreaGrayFilterTest.java (c) 27 May 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.5.29
 * @see com.cp.common.awt.BoundedAreaGrayFilter
 */

package com.cp.common.awt;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class BoundedAreaGrayFilterTest extends TestCase {

  public BoundedAreaGrayFilterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(BoundedAreaGrayFilterTest.class);
    return suite;
  }

  protected Shape getShape() {
    final GeneralPath box = new GeneralPath();
    box.moveTo(100.0f, 100.0f);
    box.lineTo(100.0f, 300.0f);
    box.lineTo(300.0f, 300.0f);
    box.lineTo(300.0f, 100.0f);
    box.closePath();
    return box;
  }

  public void testInstantiation() throws Exception {
    final Shape shape = getShape();
    BoundedAreaGrayFilter filter = null;

    assertNull(filter);

    try {
      filter = new BoundedAreaGrayFilter(shape, false);
    }
    catch (Throwable e) {
      fail("Instantiating an instance of the BoundedAreaGrayFilter class threw an unexpected Throwable ("
        + e.getMessage() + ")!");
    }

    assertNotNull(filter);
    assertEquals(shape, filter.getBoundedArea());
    assertFalse(filter.isFillColored());
  }

  public void testSetBoundedArea() throws Exception {
    try {
      new BoundedAreaGrayFilter(null, true);
      fail("Calling the BoundedAreaGrayFilter constructor with a null Shape should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bounded area cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling the BoundedAreaGrayFilter constructor with a null Shape threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testFilterRGB() throws Exception {
    final int pixel = 0xff228811;
    final Shape shape = getShape();
    BoundedAreaGrayFilter filter = new BoundedAreaGrayFilter(shape);

    assertNotNull(filter);
    assertEquals(shape, filter.getBoundedArea());
    assertTrue(filter.isFillColored());
    assertEquals(pixel, filter.filterRGB(200, 200, pixel));
    assertEquals(filter.filterPixel(pixel), filter.filterRGB(50, 150, pixel));

    filter = new BoundedAreaGrayFilter(shape, false);

    assertNotNull(filter);
    assertEquals(shape, filter.getBoundedArea());
    assertFalse(filter.isFillColored());
    assertEquals(filter.filterPixel(pixel), filter.filterRGB(200, 200, pixel));
    assertEquals(filter.filterPixel(pixel), filter.filterRGB(50, 150, pixel));
  }

}
