/*
 * GraphicsUtil.java (c) 17 July 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.4.9
 */

package com.cp.common.swing;

import com.cp.common.awt.ImageUtil;
import com.cp.common.lang.ObjectUtil;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import org.apache.log4j.Logger;

public class GraphicsUtil extends ImageUtil {

  private static final Logger logger = Logger.getLogger(GraphicsUtil.class);

  private static final Color DEFAULT_COLOR = Color.black;
  private static final RenderingHints ANTIALIASED_RENDERING = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
  private static final Stroke DEFAULT_STROKE = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);

  /**
   * Protected constructor used to allow this class to be extended, but to disallow any instances of the class
   * from being created.  This is a utility class that has no internal state, therefore creating an instance
   * would be a waste of memory resources.
   */
  protected GraphicsUtil() {
  }

  /**
   * Draws two left arrows in the default color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to draw the double left arrows.
   * @return an Image with two left arrows drawn within the bounded area.
   */
  public static Image drawDoubleLeftArrow(final Dimension boundingBox) {
    return drawDoubleLeftArrow(boundingBox, DEFAULT_COLOR);
  }

  /**
   * Draws two left arrows in the specified color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to draw the double left arrows.
   * @param arrowColor a Color object specifying the color of the arrow.
   * @return an Image with two left arrows drawn within the bounded area.
   */
  public static Image drawDoubleLeftArrow(final Dimension boundingBox, final Color arrowColor) {
    final Image imageBuffer = new BufferedImage(boundingBox.width, boundingBox.height, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D g2 = (Graphics2D) imageBuffer.getGraphics();

    g2.setPaint(arrowColor);
    g2.setRenderingHints(ANTIALIASED_RENDERING);
    g2.setStroke(DEFAULT_STROKE);
    g2.draw(new Line2D.Double(0.0, (boundingBox.getHeight() / 2.0), (boundingBox.getWidth() / 2.0), 0.0));
    g2.draw(new Line2D.Double(0.0, (boundingBox.getHeight() / 2.0), (boundingBox.getWidth() / 2.0), boundingBox.getHeight()));
    g2.draw(new Line2D.Double((boundingBox.getWidth() / 2.0), (boundingBox.getHeight() / 2.0), boundingBox.getWidth(), 0.0));
    g2.draw(new Line2D.Double((boundingBox.getWidth() / 2.0), (boundingBox.getHeight() / 2.0), boundingBox.getWidth(), boundingBox.getHeight()));

    return imageBuffer;
  }

  /**
   * Draws two right arrows in the default color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to draw the double right arrows.
   * @return an Image with two right arrows drawn within the bounded area.
   */
  public static Image drawDoubleRightArrow(final Dimension boundingBox) {
    return drawDoubleRightArrow(boundingBox, DEFAULT_COLOR);
  }

  /**
   * Draws two right arrows in the specified color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to draw the double right arrows.
   * @param arrowColor a Color object specifying the color of the arrow.
   * @return an Image with two right arrows drawn within the bounded area.
   */
  public static Image drawDoubleRightArrow(final Dimension boundingBox, final Color arrowColor) {
    final Image imageBuffer = new BufferedImage(boundingBox.width, boundingBox.height, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D g2 = (Graphics2D) imageBuffer.getGraphics();

    g2.setPaint(arrowColor);
    g2.setRenderingHints(ANTIALIASED_RENDERING);
    g2.setStroke(DEFAULT_STROKE);
    g2.draw(new Line2D.Double(0.0, 0.0, (boundingBox.getWidth() / 2.0), (boundingBox.getHeight() / 2.0)));
    g2.draw(new Line2D.Double(0.0, boundingBox.getHeight(), (boundingBox.getWidth() / 2.0), (boundingBox.getHeight() / 2.0)));
    g2.draw(new Line2D.Double((boundingBox.getWidth() / 2.0), 0.0, boundingBox.getWidth(), (boundingBox.getHeight() / 2.0)));
    g2.draw(new Line2D.Double((boundingBox.getWidth() / 2.0), boundingBox.getHeight(), boundingBox.getWidth(), (boundingBox.getHeight() / 2.0)));

    return imageBuffer;
  }

  /**
   * Draws an arrow pointing down in the default color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to draw the down arrow.
   * @return an Image with an arrow pointing down drawn within the bounded area.
   */
  public static Image drawDownArrow(final Dimension boundingBox) {
    return drawDownArrow(boundingBox, DEFAULT_COLOR);
  }

  /**
   * Draws an arrow pointing down in the specified color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to draw the down arrow.
   * @param arrowColor a Color object specifying the color of the arrow.
   * @return an Image with an arrow pointing down drawn within the bounded area.
   */
  public static Image drawDownArrow(final Dimension boundingBox, final Color arrowColor) {
    final Image imageBuffer = new BufferedImage(boundingBox.width, boundingBox.height, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D g2 = (Graphics2D) imageBuffer.getGraphics();

    g2.setPaint(arrowColor);
    g2.setRenderingHints(ANTIALIASED_RENDERING);
    g2.setStroke(DEFAULT_STROKE);
    g2.draw(new Line2D.Double(0.0, (boundingBox.getHeight() / 2.0), (boundingBox.getWidth() / 2.0), boundingBox.getHeight()));
    g2.draw(new Line2D.Double(boundingBox.getWidth(), (boundingBox.getHeight() / 2.0), (boundingBox.getWidth() / 2.0), boundingBox.getHeight()));

    return imageBuffer;
  }

  /**
   * Draws an arrow pointing left in the default color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to draw the left arrow.
   * @return an Image with an arrow pointing left drawn within the bounded area.
   */
  public static Image drawLeftArrow(final Dimension boundingBox) {
    return drawLeftArrow(boundingBox, DEFAULT_COLOR);
  }

  /**
   * Draws an arrow pointing left in the specified color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to draw the left arrow.
   * @param arrowColor a Color object specifying the color of the arrow.
   * @return an Image with an arrow pointing left drawn within the bounded area.
   */
  public static Image drawLeftArrow(final Dimension boundingBox, final Color arrowColor) {
    final Image imageBuffer = new BufferedImage(boundingBox.width, boundingBox.height, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D g2 = (Graphics2D) imageBuffer.getGraphics();

    g2.setPaint(arrowColor);
    g2.setRenderingHints(ANTIALIASED_RENDERING);
    g2.setStroke(DEFAULT_STROKE);
    g2.draw(new Line2D.Double(0, (boundingBox.getHeight() / 2.0), (boundingBox.getWidth() / 2.0), 0));
    g2.draw(new Line2D.Double(0, (boundingBox.getHeight() / 2.0), (boundingBox.getWidth() / 2.0), boundingBox.getHeight()));

    return imageBuffer;
  }

  /**
   * Draws an arrow pointing right in the default color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to draw the right arrow.
   * @return an Image with an arrow pointing right drawn within the bounded area.
   */
  public static Image drawRightArrow(final Dimension boundingBox) {
    return drawRightArrow(boundingBox, DEFAULT_COLOR);
  }

  /**
   * Draws an arrow pointing right in the speicfied color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to draw the right arrow.
   * @param arrowColor a Color object specifying the color of the arrow.
   * @return an Image with an arrow pointing right drawn within the bounded area.
   */
  public static Image drawRightArrow(final Dimension boundingBox, final Color arrowColor) {
    final Image imageBuffer = new BufferedImage(boundingBox.width, boundingBox.height, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D g2 = (Graphics2D) imageBuffer.getGraphics();

    g2.setPaint(arrowColor);
    g2.setRenderingHints(ANTIALIASED_RENDERING);
    g2.setStroke(DEFAULT_STROKE);
    g2.draw(new Line2D.Double((boundingBox.getWidth() / 2.0), 0, boundingBox.getWidth(), (boundingBox.getHeight() / 2.0)));
    g2.draw(new Line2D.Double((boundingBox.getWidth() / 2.0), boundingBox.getHeight(), boundingBox.getWidth(), (boundingBox.getHeight() / 2.0)));

    return imageBuffer;
  }

  /**
   * Draws an arrow pointing up in the default color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to draw the up arrow.
   * @return an Image with an arrow pointing up drawn within the bounded area.
   */
  public static Image drawUpArrow(final Dimension boundingBox) {
    return drawUpArrow(boundingBox, DEFAULT_COLOR);
  }

  /**
   * Draws an arrow pointing up in the default color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to draw the up arrow.
   * @param arrowColor a Color object specifying the color of the arrow.
   * @return an Image with an arrow pointing up drawn within the bounded area.
   */
  public static Image drawUpArrow(final Dimension boundingBox, final Color arrowColor) {
    final Image imageBuffer = new BufferedImage(boundingBox.width, boundingBox.height, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D g2 = (Graphics2D) imageBuffer.getGraphics();

    g2.setPaint(arrowColor);
    g2.setRenderingHints(ANTIALIASED_RENDERING);
    g2.setStroke(DEFAULT_STROKE);
    g2.draw(new Line2D.Double(0, (boundingBox.getHeight() / 2.0), (boundingBox.getWidth() / 2.0), 0));
    g2.draw(new Line2D.Double(boundingBox.getWidth(), (boundingBox.getHeight() / 2.0), (boundingBox.getWidth() / 2.0), 0));

    return imageBuffer;
  }

  /**
   * Fills two left arrows in the default color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to paint two left arrows.
   * @return an Image of two arrows pointing left filled in the bounded area.
   */
  public static Image fillDoubleLeftArrow(final Dimension boundingBox) {
    return fillDoubleLeftArrow(boundingBox, DEFAULT_COLOR);
  }

  /**
   * Fills two left arrows in the specified color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to paint two left arrows.
   * @param arrowColor a Color object specifying the color of the arrow.
   * @return an Image of two arrows pointing left filled in the bounded area.
   */
  public static Image fillDoubleLeftArrow(final Dimension boundingBox, final Color arrowColor) {
    final Image imageBuffer = new BufferedImage(boundingBox.width, boundingBox.height, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D g2 = (Graphics2D) imageBuffer.getGraphics();

    final GeneralPath path = new GeneralPath();
    path.moveTo(0, boundingBox.height / 2);
    path.lineTo(boundingBox.width / 2, 0);
    path.lineTo(boundingBox.width / 2, boundingBox.height / 2);
    path.lineTo(boundingBox.width, 0);
    path.lineTo(boundingBox.width, boundingBox.height);
    path.lineTo(boundingBox.width / 2, boundingBox.height / 2);
    path.lineTo(boundingBox.width / 2, boundingBox.height);
    path.closePath();
    //path.transform(AffineTransform.getTranslateInstance(getXOffset(viewableArea, boundingBox), getYOffset(viewableArea, boundingBox)));

    g2.setPaint(arrowColor);
    g2.setRenderingHints(ANTIALIASED_RENDERING);
    g2.fill(path);

    return imageBuffer;
  }

  /**
   * Fills two right arrows in the default color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to paint two right arrows.
   * @return an Image of two arrows pointing right filled in the bounded area.
   */
  public static Image fillDoubleRightArrow(final Dimension boundingBox) {
    return fillDoubleRightArrow(boundingBox, DEFAULT_COLOR);
  }

  /**
   * Fills two right arrows in the specified color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to paint two right arrows.
   * @param arrowColor a Color object specifying the color of the arrow.
   * @return an Image of two arrows pointing right filled in the bounded area.
   */
  public static Image fillDoubleRightArrow(final Dimension boundingBox, final Color arrowColor) {
    final Image imageBuffer = new BufferedImage(boundingBox.width, boundingBox.height, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D g2 = (Graphics2D) imageBuffer.getGraphics();

    final GeneralPath path = new GeneralPath();
    path.moveTo(0, boundingBox.height);
    path.lineTo(0, 0);
    path.lineTo(boundingBox.width / 2, boundingBox.height / 2);
    path.lineTo(boundingBox.width / 2, 0);
    path.lineTo(boundingBox.width, boundingBox.height / 2);
    path.lineTo(boundingBox.width / 2, boundingBox.height);
    path.lineTo(boundingBox.width / 2, boundingBox.height / 2);
    path.closePath();
    //path.transform(AffineTransform.getTranslateInstance(getXOffset(viewableArea, boundingBox), getYOffset(viewableArea, boundingBox)));

    g2.setPaint(arrowColor);
    g2.setRenderingHints(ANTIALIASED_RENDERING);
    g2.fill(path);

    return imageBuffer;
  }

  /**
   * Fills an arrow pointing down in the default color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to paint a down arrow.
   * @return an Image with an arrow pointing down filled within the bounded area.
   */
  public static Image fillDownArrow(final Dimension boundingBox) {
    return fillDownArrow(boundingBox, DEFAULT_COLOR);
  }

  /**
   * Fills an arrow pointing down in the specified color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to paint a down arrow.
   * @param arrowColor a Color object specifying the color of the arrow.
   * @return an Image with an arrow pointing down filled within the bounded area.
   */
  public static Image fillDownArrow(final Dimension boundingBox, final Color arrowColor) {
    final Image imageBuffer = new BufferedImage(boundingBox.width, boundingBox.height, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D g2 = (Graphics2D) imageBuffer.getGraphics();

    final GeneralPath path = new GeneralPath();
    path.moveTo(0.0f, 0.0f);
    path.lineTo(boundingBox.width, 0.0f);
    path.lineTo(boundingBox.width / 2, boundingBox.height);
    path.closePath();
    //path.transform(AffineTransform.getTranslateInstance(getXOffset(viewableArea, boundingBox), getYOffset(viewableArea, boundingBox)));

    g2.setPaint(arrowColor);
    g2.setRenderingHints(ANTIALIASED_RENDERING);
    g2.fill(path);

    return imageBuffer;
  }

  /**
   * Fills an arrow pointing left in the default color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to paint a left arrow.
   * @return an Image with an arrow pointing left filled within the bounded area.
   */
  public static Image fillLeftArrow(final Dimension boundingBox) {
    return fillLeftArrow(boundingBox, Color.black);
  }

  /**
   * Fills an arrow pointing left in the specified color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to paint a left arrow.
   * @param arrowColor a Color object specifying the color of the arrow.
   * @return an Image with an arrow pointing left filled within the bounded area.
   */
  public static Image fillLeftArrow(final Dimension boundingBox, final Color arrowColor) {
    final Image imageBuffer = new BufferedImage(boundingBox.width, boundingBox.height, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D g2 = (Graphics2D) imageBuffer.getGraphics();

    final GeneralPath path = new GeneralPath();
    path.moveTo(0.0f, boundingBox.height / 2);
    path.lineTo(boundingBox.width, 0.0f);
    path.lineTo(boundingBox.width, boundingBox.height);
    path.closePath();
    //path.transform(AffineTransform.getTranslateInstance(getXOffset(viewableArea, boundingBox), getYOffset(viewableArea, boundingBox)));

    g2.setPaint(arrowColor);
    g2.setRenderingHints(ANTIALIASED_RENDERING);
    g2.fill(path);

    return imageBuffer;
  }

  /**
   * Fills an arrow pointing right in the default color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to paint a right arrow.
   * @return an Image with an arrow pointing right filled within the bounded area.
   */
  public static Image fillRightArrow(final Dimension boundingBox) {
    return fillRightArrow(boundingBox, Color.black);
  }

  /**
   * Fills an arrow pointing right in the specified color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to paint a right arrow.
   * @param arrowColor a Color object specifying the color of the arrow.
   * @return an Image with an arrow pointing right filled within the bounded area.
   */
  public static Image fillRightArrow(final Dimension boundingBox, final Color arrowColor) {
    final Image imageBuffer = new BufferedImage(boundingBox.width, boundingBox.height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = (Graphics2D) imageBuffer.getGraphics();

    final GeneralPath path = new GeneralPath();
    path.moveTo(0.0f, 0.0f);
    path.lineTo(boundingBox.width, boundingBox.height / 2);
    path.lineTo(0.0f, boundingBox.height);
    path.closePath();
    //path.transform(AffineTransform.getTranslateInstance(getXOffset(viewableArea, boundingBox), getYOffset(viewableArea, boundingBox)));

    g2.setPaint(arrowColor);
    g2.setRenderingHints(ANTIALIASED_RENDERING);
    g2.fill(path);

    return imageBuffer;
  }

  /**
   * Fills an arrow pointing up in the default color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to paint a up arrow.
   * @return an Image with an arrow pointing up filled within the bounded area.
   */
  public static Image fillUpArrow(final Dimension boundingBox) {
    return fillUpArrow(boundingBox, Color.black);
  }

  /**
   * Fills an arrow pointing up in the specified color contained within the bounding box.
   * @param boundingBox a Dimension object specifying the area in which to paint a up arrow.
   * @param arrowColor a Color object specifying the color of the arrow.
   * @return an Image with an arrow pointing up filled within the bounded area.
   */
  public static Image fillUpArrow(final Dimension boundingBox, final Color arrowColor) {
    final Image imageBuffer = new BufferedImage(boundingBox.width, boundingBox.height, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D g2 = (Graphics2D) imageBuffer.getGraphics();

    final GeneralPath path = new GeneralPath();
    path.moveTo(0.0f, boundingBox.height);
    path.lineTo(boundingBox.width / 2, 0.0f);
    path.lineTo(boundingBox.width, boundingBox.height);
    path.closePath();
    //path.transform(AffineTransform.getTranslateInstance(getXOffset(viewableArea, boundingBox), getYOffset(viewableArea, boundingBox)));

    g2.setPaint(arrowColor);
    g2.setRenderingHints(ANTIALIASED_RENDERING);
    g2.fill(path);

    return imageBuffer;
  }

  /**
   * Converts the Rectangle object into a Dimension object.
   * @param rectangle the Rectangle used to convert to a Dimension object.
   * @return a Dimension object set to the width and height of the Rectangle.
   */
  public static Dimension getDimension(final RectangularShape rectangle) {
    return new Dimension((int) rectangle.getWidth(), (int) rectangle.getHeight());
  }

  /**
   * Returns a "Diskette" image icon for a button or label UI component, used to represent a save action.
   * @param viewableArea a Dimension object specifying the area in which to the paint the Icon.
   * @return an Icon representation of a "Diskette".
   */
  public static Icon getDisketteIcon(final Dimension viewableArea) {
    final Image bufferedImage = new BufferedImage(viewableArea.width, viewableArea.height, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D g2 = (Graphics2D) bufferedImage.getGraphics();

    final GeneralPath floppySkin = new GeneralPath();
    floppySkin.moveTo(0.0f, 0.0f);
    floppySkin.lineTo(0.0f, 46.0f);
    floppySkin.lineTo(4.0f, 50.0f);
    floppySkin.lineTo(50.0f, 50.0f); // lower right corner
    floppySkin.lineTo(50.0f, 8.0f);
    floppySkin.lineTo(42.0f, 8.0f);
    floppySkin.lineTo(42.0f, 24.0f);
    floppySkin.lineTo(8.0f, 24.0f);
    floppySkin.lineTo(8.0f, 0.0f);
    floppySkin.closePath();

    final GeneralPath filmArea = new GeneralPath();
    filmArea.moveTo(12.0f, 50.0f);
    filmArea.lineTo(12.0f, 32.0f);
    filmArea.lineTo(42.0f, 32.0f);
    filmArea.lineTo(42.0f, 50.0f);
    filmArea.closePath();

    final GeneralPath filmCover = new GeneralPath();
    filmCover.moveTo(12.0f, 50.0f);
    filmCover.lineTo(12.0f, 32.0f);
    filmCover.lineTo(30.0f, 32.0f);
    filmCover.lineTo(30.0f, 50.0f);
    filmCover.closePath();

    final GeneralPath writeProtect = new GeneralPath();
    writeProtect.moveTo(0.0f, 0.0f);
    writeProtect.lineTo(50.0f, 0.0f);
    writeProtect.lineTo(50.0f, 8.0f);
    writeProtect.lineTo(42.0f, 8.0f);
    writeProtect.lineTo(42.0f, 0.0f);
    writeProtect.closePath();

    GeneralPath diskette = new GeneralPath(floppySkin);
    diskette.append(filmArea, false);
    diskette.append(filmCover, false);
    diskette.append(writeProtect, false);

    Rectangle2D disketteBounds = diskette.getBounds2D();
    Dimension disketteArea = getDimension(disketteBounds);
    final double scaleFactor = getScalingFactor(disketteArea, viewableArea);
    final AffineTransform scaleInstance = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);

    diskette.transform(scaleInstance);
    floppySkin.transform(scaleInstance);
    filmArea.transform(scaleInstance);
    filmCover.transform(scaleInstance);
    writeProtect.transform(scaleInstance);
    disketteBounds = diskette.getBounds2D();
    disketteArea = getDimension(disketteBounds);

    final int xOffset = getXOffset(viewableArea, disketteArea);
    final int yOffset = getYOffset(viewableArea, disketteArea);
    final AffineTransform translateInstance = AffineTransform.getTranslateInstance(xOffset, yOffset);

    diskette.transform(translateInstance);
    floppySkin.transform(translateInstance);
    filmArea.transform(translateInstance);
    filmCover.transform(translateInstance);
    writeProtect.transform(translateInstance);
    disketteBounds = diskette.getBounds2D();
    disketteArea = getDimension(disketteBounds);

    final Point2D point1 = new Point2D.Double(disketteBounds.getX(), disketteBounds.getY());
    final Point2D point2 = new Point2D.Double(point1.getX() + disketteArea.getWidth(), point1.getY() + disketteArea.getHeight());

    // paint the diskette
    g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
    g2.setPaint(Color.black);
    g2.draw(floppySkin);
    g2.setPaint(new GradientPaint(point1, new Color(128, 128, 0), point2, new Color(107, 107, 4), true));
    g2.fill(floppySkin);
    g2.setPaint(Color.black);
    g2.fill(filmCover);
    g2.draw(filmArea);
    g2.draw(writeProtect);

    return new ImageIcon(bufferedImage);
  }

  /**
   * Returns a square Dimension object with the width and height set to the largest dimension of the specified
   * Dimension object parameter.
   * @param area a Dimension object who's width of height is used as a basis for constructing the returned
   * Dimension object.
   * @return a Dimension object who's width and height is based on the largest dimension of the Dimension object
   * parameter.
   */
  public static Dimension getMaximumSquareArea(final Dimension area) {
    final int maxDim = Math.max(area.width, area.height);
    if (logger.isDebugEnabled()) {
      logger.debug("maxDim (" + maxDim + ")");
    }

    return new Dimension(maxDim, maxDim);
  }

  /**
   * Returns a square Dimension object with the width and height set to the smallest dimension of the specified
   * Dimension object parameter.
   * @param area a Dimension object who's width of height is used as a basis for constructing the returned
   * Dimension object.
   * @return a Dimension object who's width and height is based on the smallest dimension of the Dimension object
   * parameter.
   */
  public static Dimension getMinimumSquareArea(final Dimension area) {
    final int minDim = Math.min(area.width, area.height);
    if (logger.isDebugEnabled()) {
      logger.debug("minDim (" + minDim + ")");
    }

    return new Dimension(minDim, minDim);
  }

  /**
   * Returns a "New Document" image icon for a button or a label UI component.
   * @param viewableArea a Dimension object specifying the area in which to the paint the Icon.
   * @return an Icon representation of "New Document".
   */
  public static Icon getNewDocumentIcon(final Dimension viewableArea) {
    final Image bufferedImage = new BufferedImage(viewableArea.width, viewableArea.height, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D g2 = (Graphics2D) bufferedImage.getGraphics();

    final GeneralPath newDocument = new GeneralPath();
    newDocument.moveTo(0.0f, 0.0f);
    newDocument.lineTo(0.0f, 40.0f);
    newDocument.lineTo(35.0f, 40.0f);
    newDocument.lineTo(35.0f, 10.0f);
    newDocument.lineTo(25.0f, 0.0f);
    newDocument.lineTo(25.0f, 10.0f);
    newDocument.lineTo(35.0f, 10.0f);
    newDocument.lineTo(25.0f, 0.0f);
    newDocument.closePath();

    Rectangle2D documentBounds = newDocument.getBounds2D();
    Dimension documentArea = getDimension(documentBounds);
    final double scale = getScalingFactor(documentArea, viewableArea);
    final AffineTransform scaleInstance = AffineTransform.getScaleInstance(scale, scale);

    newDocument.transform(scaleInstance);
    documentBounds = newDocument.getBounds2D();
    documentArea = getDimension(documentBounds);

    final int xOffset = getXOffset(viewableArea, documentArea);
    final int yOffset = getYOffset(viewableArea, documentArea);
    final AffineTransform translateInstance = AffineTransform.getTranslateInstance(xOffset, yOffset);

    newDocument.transform(translateInstance);
    documentBounds = newDocument.getBounds2D();
    documentArea = getDimension(documentBounds);

    final Point2D point1 = new Point2D.Double(documentBounds.getX() + documentArea.getWidth() / 2.0, documentBounds.getY());
    final Point2D point2 = new Point2D.Double(point1.getX(), documentBounds.getY() + documentArea.getHeight());

    g2.setPaint(new GradientPaint(point1, Color.white, point2, new Color(212, 208, 200), true));
    g2.fill(newDocument);
    g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    g2.setPaint(Color.black);
    g2.draw(newDocument);

    return new ImageIcon(bufferedImage);
  }

  /**
   * Returns an "Open Folder" image icon used as an Icon on a button or label UI component.
   * @param viewableArea a Dimension object specifying the size of the viewable area in which to paint the Icon.
   * @return an Icon object representing the Open Folder icon.
   */
  public static Icon getOpenFolderIcon(final Dimension viewableArea) {
    final Image bufferedImage = new BufferedImage(viewableArea.width, viewableArea.height, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D g2 = (Graphics2D) bufferedImage.getGraphics();

    final GeneralPath folderBack = new GeneralPath();
    folderBack.moveTo(0.0f, 0.0f);
    folderBack.lineTo(40.0f, 0.0f);
    folderBack.lineTo(40.0f, 10.0f);
    folderBack.lineTo(120.0f, 10.0f);
    folderBack.lineTo(120.0f, 100.0f);
    folderBack.lineTo(0.0f, 100.0f);
    folderBack.closePath();

    final GeneralPath folderFront = new GeneralPath();
    folderFront.moveTo(0.0f, 100.0f);
    folderFront.lineTo(40.0f, 15.0f);
    folderFront.lineTo(80.0f, 15.0f);
    folderFront.lineTo(80.0f, 25.0f);
    folderFront.lineTo(160.0f, 25.0f);
    folderFront.lineTo(120.0f, 100.0f);
    folderFront.closePath();

    GeneralPath folder = new GeneralPath(folderBack);
    folder.append(folderFront, false);

    Rectangle2D folderBounds = folder.getBounds2D();
    Dimension folderArea = getDimension(folderBounds);
    final double scale = getScalingFactor(folderArea, viewableArea);
    final AffineTransform scaleInstance = AffineTransform.getScaleInstance(scale, scale);

    folderBack.transform(scaleInstance);
    folderFront.transform(scaleInstance);
    folder = new GeneralPath(folderBack);
    folder.append(folderFront, false);
    folderBounds = folder.getBounds2D();
    folderArea = getDimension(folderBounds);

    final int xOffset = getXOffset(viewableArea, folderArea);
    final int folderBackYOffset = getYOffset(viewableArea, folderArea);
    final int folderFrontYOffset = folderBackYOffset +
      (int) Math.abs(folderBack.getBounds2D().getHeight() - folderFront.getBounds2D().getHeight());

    folderBack.transform(AffineTransform.getTranslateInstance(xOffset, folderBackYOffset));
    folderFront.transform(AffineTransform.getTranslateInstance(xOffset, folderFrontYOffset));
    folderBounds = folder.getBounds2D();

    final Point2D folderBackPoint1 = new Point2D.Double(folderBounds.getX() + folderBounds.getWidth() / 2.0, folderBounds.getY());
    final Point2D folderBackPoint2 = new Point2D.Double(folderBackPoint1.getX(), folderBackPoint1.getY() + folderBounds.getHeight());
    final Point2D folderFrontPoint1 = new Point2D.Double(folderBounds.getX(), folderBounds.getY());
    final Point2D folderFrontPoint2 = new Point2D.Double(folderFrontPoint1.getX() + folderBounds.getWidth(),
      folderFrontPoint1.getY() + folderBounds.getHeight());

    // paint the folder back
    g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
    g2.setPaint(Color.black);
    g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
    g2.draw(folderBack);
    g2.setPaint(new GradientPaint(folderBackPoint1, new Color(139, 109, 8), folderBackPoint2, Color.black));
    g2.fill(folderBack);

    // paint the folder front
    g2.setPaint(Color.black);
    g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
    g2.draw(folderFront);
    g2.setPaint(new GradientPaint(folderFrontPoint1, Color.white, folderFrontPoint2, new Color(210, 168, 28)));
    g2.fill(folderFront);

    return new ImageIcon(bufferedImage);
  }

  /**
   * Converts a Dimension object into a rectangular shape with the same dimensions (width and height) of the
   * Dimension object and a default location of (0.0, 0.0).
   * @param dim the Dimension object used to determine the size of the RectangularShape.
   * @return a RectangularShape object constructed with the dimensions of the Dimension object at the default
   * location (0.0, 0.0).
   */
  public static RectangularShape getRectangle(final Dimension dim) {
    return getRectangle(new Point2D.Double(0.0, 0.0), dim);
  }

  /**
   * Converts a Dimension object into a rectangular shape with the same dimensions (width and height) of the
   * Dimension object at the specified location.
   * @param location a Point2D object specifying the location of the RectangularShape object in application coordinates.
   * @param dim the Dimension object used to determine the size of the RectangularShape.
   * @return a RectangularShape object constructed with the dimensions of the Dimension object at the default
   * location (0.0, 0.0).
   */
  public static RectangularShape getRectangle(final Point2D location, final Dimension dim) {
    return new Rectangle2D.Double(location.getX(), location.getY(), dim.width, dim.getHeight());
  }

  /**
   * Returns a scale value to transform the object having actual size to the specified desired size.  The scale
   * is determined by the smallest percentage of difference in width and height between the actual and desired size.
   * @param actualSize the actual size of the object.
   * @param preferredSize the desired size of the object.
   * @return a double value specifying the scaling factor used for the transformation from the actual size to
   * the desired size.
   */
  public static double getScalingFactor(final Dimension actualSize, final Dimension preferredSize) {
    final double xfactor = (actualSize.getWidth() / preferredSize.getWidth());
    final double yfactor = (actualSize.getHeight() / preferredSize.getHeight());

    if (logger.isDebugEnabled()) {
      logger.debug("xfactor (" + xfactor + ")");
      logger.debug("yfactor (" + yfactor + ")");
    }

    if (xfactor > yfactor) {
      return (preferredSize.getWidth() / actualSize.getWidth());
    }
    else {
      return (preferredSize.getHeight() / actualSize.getHeight());
    }
  }

  /**
   * Creates a transparent icon to be used as filler in a toolbar or menu.
   * @param viewableArea a Dimension object specifying the size of the viewable area in which to paint the Icon.
   * @return an Icon object representing the transparent icon.
   */
  public static Icon getTransparentIcon(final Dimension viewableArea) {
    return new ImageIcon(new BufferedImage(viewableArea.width, viewableArea.height, BufferedImage.TYPE_INT_ARGB));
  }

  /**
   * Returns the viewable area of a JComponent object in which graphics primitives can be painted.  This area is
   * considered the face of the component.  This method should only be called after the component is rendered, or
   * the size of the component, determined by the preferred size and component's layout manager, is set with a
   * call to pack in cases where the component is not yet rendered.
   * @param component the JComponent for which the viewble area is determined.
   * @return a Dimension object specifying the dimensions (width and height) of the viewable area.
   */
  public static Dimension getViewableArea(final JComponent component) {
    final Dimension size = component.getSize();

    int width =  size.width;
    int height = size.height;

    final Insets insets = component.getInsets();
    if (ObjectUtil.isNotNull(insets)) {
      width -= (insets.left + insets.right);
      height -= (insets.top + insets.bottom);
    }

    return new Dimension(width, height);
  }

  /**
   * Returns the X offset that positions the viewableArea to be horizontally centered about the boundedArea.
   * @param boundedArea a Dimension object used to define the region around the viewableArea.
   * @param viewableArea a Dimension object contained within the bounded area.
   * @return an integer specifying the X offset.
   */
  public static int getXOffset(final Dimension viewableArea, final Dimension boundedArea) {
    return (int) ((viewableArea.getWidth() - boundedArea.getWidth()) / 2.0);
  }

  /**
   * Returns the Y offset that positions the viewableArea to be vertically centered about the boundedArea.
   * @param viewableArea a Dimension object used to define the region around the bounded area.
   * @param boundedArea a Dimension object contained within the viewable area.
   * @return an integer specifying the Y offset.
   */
  public static int getYOffset(final Dimension viewableArea, final Dimension boundedArea) {
    return (int) ((viewableArea.getHeight() - boundedArea.getHeight()) / 2.0);
  }

}
