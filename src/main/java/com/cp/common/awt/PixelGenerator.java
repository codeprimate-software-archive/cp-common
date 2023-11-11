/*
 * PixelGenerator.java (c) 17 April 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.5.29
 */

package com.cp.common.awt;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Shape;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PixelGenerator {

  private static final Log logger = LogFactory.getLog(PixelGenerator.class);

  /**
   * Default constructor used by subclasses to extend the functionality of
   * this PixelGenerator class.
   */
  protected PixelGenerator() {
  }

  /**
   * Creates a flattened pixel array (one-dimensional array of pixel data) representing the
   * Shape object in black and the canvas as transparent.
   * @param canvasSize a java.awt.Dimension object representing the area of the canvas
   * on which the geometric shape will be drawn (note that areas of the canvas not
   * contained by the Shape obejct will be transparent).
   * @param geometricShape a java.awt.Shape object for which pixel data will be generated as
   * black pixels.
   * @return a one-dimensional array of integer values representing the value of each
   * pixel in the image.
   * @see PixelGenerator#getPixels(java.awt.Rectangle, java.awt.Shape) 
   */
  public static int[] getPixels(final Dimension canvasSize, final Shape geometricShape) {
    if (logger.isDebugEnabled()) {
      logger.debug("canvas size (" + canvasSize + ")");
      logger.debug("geometric shape (" + geometricShape + ")");
    }

    final int[] pixels = new int[canvasSize.width * canvasSize.height];

    for (int y = canvasSize.height; --y >= 0;) {
      for (int x = canvasSize.width; --x >= 0;) {
        pixels[y * canvasSize.width + x] = (geometricShape.contains(x, y) ? 0xff000000 : 0x00000000);
      }
    }

    return pixels;
  }

  /**
   * Creates a flattened pixel array (one-dimensional array of pixel data) representing
   * the Shape object in black and the bounding box as transparent.  This overloaded
   * getPixel method is implemented in terms of the getPixel(:Dimension, :Shape) method.
   * @param boundingBox: a java.awt.Rectangle object representing the bounding box
   * enclosing the Shape object Note that the bounding box will be transparent.
   * @param geometricShape a jav.awt.Shape object for which pixel data will be generated
   * (represented by the black pixels).
   * @return a one-dimensional array of integer values representing the value of each
   * pixel in the image.
   * @see PixelGenerator#getPixels(java.awt.Dimension, java.awt.Shape)
   */
  public static int[] getPixels(final Rectangle boundingBox, final Shape geometricShape) {
    return getPixels(new Dimension((int) boundingBox.getWidth(), (int) boundingBox.getHeight()), geometricShape);
  }

}
