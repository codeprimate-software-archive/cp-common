/*
 * BoundedAreaGrayFilter.java (c) 17 April 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.5.29
 * @see java.awt.image.RGBImageFilter
 */

package com.cp.common.awt;

import com.cp.common.lang.Assert;
import java.awt.Shape;
import java.awt.image.RGBImageFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class BoundedAreaGrayFilter extends RGBImageFilter {

  private static final boolean DEFAULT_FILL_COLORED = true;

  private static final double RED_LUMINATION = 0.199;
  private static final double GREEN_LUMINATION = 0.387;
  private static final double BLUE_LUMINATION = 0.014;

  private boolean fillColored = DEFAULT_FILL_COLORED;

  protected final Log logger = LogFactory.getLog(getClass());

  private Shape boundedArea;

  /**
   * Constructs an instance of the BoundedAreaGrayFilter class to gray within or outside
   * of a polygon shaped area.
   *
   * @param boundedArea java.awt.Shape specifying the area that will contain color or
   * be grayed out.
   */
  public BoundedAreaGrayFilter(final Shape boundedArea) {
    this(boundedArea, DEFAULT_FILL_COLORED);
  }

  /**
   * Constructs an instance of the BoundedAreaGrayFilter class to gray within or outside
   * of a polygon shaped area.
   *
   * @param boundedArea java.awt.Shape specifying the area that will contain color or
   * be grayed out.
   * @param fillColored is a boolean value indicating true for the area inside the
   * polygon to be colored, false to be grayed out.
   */
  public BoundedAreaGrayFilter(final Shape boundedArea, final boolean fillColored) {
    setBoundedArea(boundedArea);
    this.fillColored = fillColored;
  }

  /**
   * Returns the bounded area as a Shape object.
   * @return a Shape object representing the bounded area.
   */
  public Shape getBoundedArea() {
    return boundedArea;
  }

  /**
   * Sets the polygon area that will either be filled with color or grayed out.
   * @param boundedArea the polygon defining the area around which pixels
   * are colored or grayed out.
   */
  private void setBoundedArea(final Shape boundedArea) {
    Assert.notNull(boundedArea, "The bounded area cannot be null!");
    this.boundedArea = boundedArea;
  }

  /**
   * Returns a boolean value indicating true if the bounded area is to be filled
   * with color, false if the bounded area will be grayed out.
   * @return a boolean value indicating the fill (color or gray).
   */
  public boolean isFillColored() {
    return fillColored;
  }

  /**
   * Package-private method to compute the grayscale value of the specified pixel.
   * @param pixel the current pixel color as a 4 byte integer value.
   * @return an integer 4 byte value for the new color of the pixel at (x, y).
   */
  int filterPixel(final int pixel) {
    final int red = (pixel & 0x00ff0000) >> 16;
    final int green = (pixel & 0x0000ff00) >> 8;
    final int blue = pixel & 0x000000ff;
    final int lumination = (int) (RED_LUMINATION * red + GREEN_LUMINATION * green + BLUE_LUMINATION * blue);
    logger.debug("lumination (" + lumination + ")");

    // Return the lumination value as the value of each RGB component.
    // The Alpha (transparency) component should always be set (not transparent).
    return (0xff << 24) | (lumination << 16) | (lumination << 8) | lumination;
  }

  /**
   * Converts color pixels to grayscale if and only if the point, defined by x and y coordinates, are not contained
   * within the bounded area. The algorithm matches the NTSC specification.
   * @param x is the x coordinate in the image area.
   * @param y is the y coordinate in the image area.
   * @param pixel is the current pixel color as a 4 byte integer value.
   * @return an integer 4 byte value for the new color of the pixel at (x, y).
   * @see BoundedAreaGrayFilter#filterPixel(int)
   */
  public int filterRGB(final int x, final int y, final int pixel) {
    if (getBoundedArea().contains(x, y) && isFillColored()) {
      logger.debug("fill colored");
      return pixel;
    }
    else {
      // Get the average RGB intensity.
      logger.debug("gray out");
      return filterPixel(pixel);
    }
  }

}
