/*
 * PixelGeneratorTest.java (c) 29 May 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.5.29
 * @see com.cp.common.awt.PixelGenerator
 */

package com.cp.common.awt;

import java.awt.Dimension;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PixelGeneratorTest extends TestCase {

  public PixelGeneratorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(PixelGeneratorTest.class);
    return suite;
  }

  protected void assertEquals(final int[] expected, final int[] actual) {
    assertEquals(expected.length, actual.length);

    int index = 0;

    for (final int expectedValue : expected) {
      assertEquals(expectedValue, actual[index++]);
    }
  }

  protected Shape getShape() {
    final GeneralPath shape = new GeneralPath();
    shape.moveTo(1.0f, 1.0f);
    shape.lineTo(1.0f, 3.0f);
    shape.lineTo(3.0f, 3.0f);
    shape.lineTo(3.0f, 1.0f);
    shape.closePath();
    return shape;
  }

  public void testGetPixels() throws Exception {
    final Dimension canvasSize = new Dimension(4, 4);
    final Shape geometricShape = getShape();

    final int[] expectedPixels = {
      0x00000000, 0x00000000, 0x00000000, 0x00000000,
      0x00000000, 0xFF000000, 0xFF000000, 0x00000000,
      0x00000000, 0xFF000000, 0xFF000000, 0x00000000,
      0x00000000, 0x00000000, 0x00000000, 0x00000000
    };

    final int[] actualPixels = PixelGenerator.getPixels(canvasSize, geometricShape);

    assertNotNull(actualPixels);
    assertEquals(expectedPixels, actualPixels);
  }

}
