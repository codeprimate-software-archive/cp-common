/*
 * ImageUtil.java (c) 7 November 2001
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.7.4
 * @see com.cp.common.swing.GraphicsUtil
 * @see java.awt.Image
 */

package com.cp.common.awt;

import com.cp.common.lang.Assert;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ImageUtil {

  private static final Log logger = LogFactory.getLog(ImageUtil.class);

  protected static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();

  /**
   * Default constructor used by sublcasses to extend the functionality of the
   * ImageUtil class.  The ImageUtil class is a stateless utility thus instances
   * need not be created.
   */
  protected ImageUtil() {
  }

  /**
   * Generates two arrow images of the given size both pointing to the left.
   *
   * @param boundingBox java.awt.Dimension object specifying the bounding box
   * containing the two left arrows.
   * @return a java.awt.Image object of the double left arrows.
   */
  public static Image getDoubleLeftArrowImage(final Dimension boundingBox) {
    validateBoundingBox(boundingBox);

    final int width = (int) boundingBox.getWidth();
    logger.debug("width = " + width);

    final int height = (int) boundingBox.getHeight();
    logger.debug("height = " + height);

    final int halfWidth = width / 2;
    logger.debug("halfWidth = " + halfWidth);

    final int halfHeight = height / 2;
    logger.debug("halfHeight = " + halfHeight);

    final Polygon shape = new Polygon();
    shape.addPoint(0, halfHeight);
    shape.addPoint(halfWidth, 0);
    shape.addPoint(halfWidth, halfHeight);
    shape.addPoint(width, 0);
    shape.addPoint(width, height);
    shape.addPoint(halfWidth, halfHeight);
    shape.addPoint(halfWidth, height);

    if (logger.isDebugEnabled()) {
      logger.debug("shape (" + shape + ")");
    }

    final int[] pixels = PixelGenerator.getPixels(shape.getBounds(), shape);

    return TOOLKIT.createImage(new MemoryImageSource(width, height, pixels, 0, width));
  }

  /**
   * Generates two arrow images of the given size both pointing to the right.
   *
   * @param boundingBox java.awt.Dimension object specifying the bounding box
   * containing the two right arrows.
   * @return a java.awt.Image object of the double right arrows.
   */
  public static Image getDoubleRightArrowImage(final Dimension boundingBox) {
    validateBoundingBox(boundingBox);

    final int width = (int) boundingBox.getWidth();
    logger.debug("width = " + width);

    final int height = (int) boundingBox.getHeight();
    logger.debug("height = " + height);

    final int halfWidth = width / 2;
    logger.debug("halfWidth = " + halfWidth);

    final int halfHeight = height / 2;
    logger.debug("halfHeight = " + halfHeight);

    final Polygon shape = new Polygon();
    shape.addPoint(0, height);
    shape.addPoint(0, 0);
    shape.addPoint(halfWidth, halfHeight);
    shape.addPoint(halfWidth, 0);
    shape.addPoint(width, halfHeight);
    shape.addPoint(halfWidth, height);
    shape.addPoint(halfWidth, halfHeight);

    if (logger.isDebugEnabled()) {
      logger.debug("shape (" + shape + ")");
    }

    final int[] pixels = PixelGenerator.getPixels(shape.getBounds(), shape);

    return TOOLKIT.createImage(new MemoryImageSource(width, height, pixels, 0, width));
  }

  /**
   * Generates an arrow image of the given size pointing down.
   *
   * @param boundingBox java.awt.Dimension object specifying the bounding box
   * containing the down arrow.
   * @return a java.awt.Image object of the down arrow.
   */
  public static Image getDownArrowImage(final Dimension boundingBox) {
    validateBoundingBox(boundingBox);

    final int width = (int) boundingBox.getWidth();
    logger.debug("width = " + width);

    final int height = (int) boundingBox.getHeight();
    logger.debug("height = " + height);

    final int halfWidth = width / 2;
    logger.debug("halfWidth = " + halfWidth);

    final Polygon shape = new Polygon();
    shape.addPoint(0, 0);
    shape.addPoint(width, 0);
    shape.addPoint(halfWidth, height);

    if (logger.isDebugEnabled()) {
      logger.debug("shape (" + shape + ")");
    }

    final int[] pixels = PixelGenerator.getPixels(shape.getBounds(), shape);

    return TOOLKIT.createImage(new MemoryImageSource(width, height, pixels, 0, width));
  }

  /**
   * Loads an image from the specified file in the file system.
   * @param imageFile the File object referring to the file containing the image.
   * @param mediaTracker the Component used by the MediaTracker to track the progress
   * of loading the image from the file.
   * @return an Image object containing the content of the image.
   * @throws FileNotFoundException if the image file cannot be found.
   */
  public static Image getImage(final File imageFile, final Component mediaTracker) throws FileNotFoundException {
    if (logger.isDebugEnabled()) {
      logger.debug("Loading image from file (" + imageFile + ")");
    }

    if (imageFile.exists()) {
      try {
        return getImage(imageFile.toURI().toURL(), mediaTracker);
      }
      catch (MalformedURLException e) {
        logger.warn("The file (" + imageFile + ") cannot be found!");
        throw new FileNotFoundException("The file (" + imageFile + ") cannot be found!");
      }
    }
    else {
      logger.warn("The file (" + imageFile + ") cannot be found!");
      throw new FileNotFoundException("The file (" + imageFile + ") cannot be found!");
    }
  }

  /**
   * Loads the image identified by the uniform resource locator.
   * @param url the Uniform Resource Locator specifying the location of the image to load.
   * @param mediaTracker the Component used by the MediaTracker to track the progress
   * of loading the image from the file.
   * @return an Image object containing the content of the image.
   */
  public static Image getImage(final URL url, final Component mediaTracker) {
    if (logger.isDebugEnabled()) {
      logger.debug("Loading image from URL (" + url + ")");
    }

    final Image img = Toolkit.getDefaultToolkit().getImage(url);
    final MediaTracker tracker = new MediaTracker(mediaTracker);

    tracker.addImage(img, 0);

    try {
      tracker.waitForAll();
    }
    catch (InterruptedException e) {
      logger.warn("Failed to wait while loading image (" + url.toExternalForm() + ")!");
    }

    return img;
  }

  /**
   * Gets the dimensions (width and height) of the specified image.
   * @param img the Image object for which the size is returned.
   * @param imgObserver the observer of the image.
   * @return a Dimension object specifying the size, both width and height, of the specified image.
   */
  public static Dimension getImageSize(final Image img, final ImageObserver imgObserver) {
    return new Dimension(img.getWidth(imgObserver), img.getHeight(imgObserver));
  }

  /**
   * Generates an arrow image of the given size pointing left.
   *
   * @param boundingBox java.awt.Dimension object specifying the bounding box
   * containing the left arrow.
   * @return a java.awt.Image object of the left arrow.
   */
  public static Image getLeftArrowImage(final Dimension boundingBox) {
    validateBoundingBox(boundingBox);

    final int width = (int) boundingBox.getWidth();
    logger.debug("width = " + width);

    final int height = (int) boundingBox.getHeight();
    logger.debug("height = " + height);

    final int halfHeight = height / 2;
    logger.debug("halfHeight = " + halfHeight);

    final Polygon shape = new Polygon();
    shape.addPoint(0, halfHeight);
    shape.addPoint(width, 0);
    shape.addPoint(width, height);

    if (logger.isDebugEnabled()) {
      logger.debug("shape (" + shape + ")");
    }

    final int[] pixels = PixelGenerator.getPixels(shape.getBounds(), shape);

    return TOOLKIT.createImage(new MemoryImageSource(width, height, pixels, 0, width));
  }

  /**
   * Generates an arrow image of the given size pointing right.
   *
   * @param boundingBox java.awt.Dimension object specifying the bounding box
   * containing the right arrow.
   * @return a java.awt.Image object of the right arrow.
   */
  public static Image getRightArrowImage(final Dimension boundingBox) {
    validateBoundingBox(boundingBox);

    final int width = (int) boundingBox.getWidth();
    logger.debug("width = " + width);

    final int height = (int) boundingBox.getHeight();
    logger.debug("height = " + height);

    final int halfHeight = height / 2;
    logger.debug("halfHeight = " + halfHeight);

    final Polygon shape = new Polygon();
    shape.addPoint(width, halfHeight);
    shape.addPoint(0, 0);
    shape.addPoint(0, height);

    if (logger.isDebugEnabled()) {
      logger.debug("shape (" + shape + ")");
    }

    final int[] pixels = PixelGenerator.getPixels(shape.getBounds(), shape);

    return TOOLKIT.createImage(new MemoryImageSource(width, height, pixels, 0, width));
  }

  /**
   * Generates an arrow image of the given size pointing up.
   *
   * @param boundingBox: java.awt.Dimension object specifying the bounding box
   * containing the up arrow.
   * @return a java.awt.Image object of the up arrow.
   */
  public static Image getUpArrowImage(final Dimension boundingBox) {
    validateBoundingBox(boundingBox);

    final int width = (int) boundingBox.getWidth();
    logger.debug("width = " + width);

    final int height = (int) boundingBox.getHeight();
    logger.debug("height = " + height);

    final int halfWidth = width / 2;
    logger.debug("halfWidth = " + halfWidth);

    final Polygon shape = new Polygon();
    shape.addPoint(0, height);
    shape.addPoint(halfWidth, 0);
    shape.addPoint(width, height);

    if (logger.isDebugEnabled()) {
      logger.debug("shape (" + shape + ")");
    }

    final int[] pixels = PixelGenerator.getPixels(shape.getBounds(), shape);

    return TOOLKIT.createImage(new MemoryImageSource(width, height, pixels, 0, width));
  }

  /**
   * Verifies that the boudning box is not null and the area of the box is
   * greater than zero.
   * @param boundingBox the smallest area containing the images drawn by this
   * utility class.
   */
  static void validateBoundingBox(final Dimension boundingBox) {
    if (logger.isDebugEnabled()) {
      logger.debug("boundingBox (" + boundingBox + ")");
    }

    Assert.notNull(boundingBox, "The bounded box defining the area containing the imnage cannot be null!");
    Assert.greaterThan(boundingBox.width * boundingBox.height, 0, "The area of the bounding box cannot be negative or zero!");
  }

}
