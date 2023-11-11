/*
 * WindowUtil.java (c) 23 April 2002
 *
 * The WindowUtil class is used by applications/applets to center
 * frames and dialog boxes about the desktop environment and
 * application frames respectively.  The WindowUtil class is also
 * a utility class appropriat for any other window operations
 * that are not standard in the Java Platform API.
 *
 * An instance of the WindowUtil class cannot be created since
 * this is a utility class.  The class is also effectively final
 * and therefore, cannot be subclassed.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.5.29
 * @see java.awt.Toolkit
 */

package com.cp.common.awt;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class WindowUtil {

  public static final Dimension DEFAULT_SCREEN_SIZE = new Dimension(640, 480);

  private static final Log logger = LogFactory.getLog(WindowUtil.class);

  public static final Point DEFAULT_DESKTOP_LOCATION = new Point(0, 0);

  /**
   * Default private constructor for the WindowUtil class enforcing
   * non-instantiability.  A utility class by definition consists only
   * of methods and class level functionality.
   */
  private WindowUtil() {
  }

  /**
   * Returns the screen location specified as a java.awt.Point object that positions the frame of the application
   * in the center of the user's desktop in a windows environment.  Note that the positions signifies the top left
   * corner of the application window.
   * @param windowSize is a Dimension object specifying the width and height of the application frame.
   * @return a Point specifying the center location in the desktop environement to position the application frame.
   * @see WindowUtil#getDialogLocation(java.awt.Container, java.awt.Dimension)
   * @see WindowUtil#getScreenSize()
   * @see java.awt.Window#setLocationRelativeTo
   */
  public static Point getDesktopLocation(final Dimension windowSize) {
    final Dimension screenSize = getScreenSize();

    if (logger.isDebugEnabled()) {
      logger.debug("screen size (" + screenSize + ")");
      logger.debug("window size (" + windowSize + ")");
    }

    return new Point((int) ((screenSize.getWidth() - windowSize.getWidth()) / 2.0),
        (int) ((screenSize.getHeight() - windowSize.getHeight()) / 2.0));
  }

  /**
   * Returns a screen location specified as a java.awt.Point object that centers a dialog box about
   * the application frame.
   * @param frame is a java.awt.Container object refering to the application frame which spawned the dialog box.
   * @param dialogSize is a Dimension object specifying the width and height of the dialog box.
   * @return a java.awt.Point object specifying the location of the upper left corner of the dialog box which
   * cernters the box about the application frame.
   * @see WindowUtil#getDesktopLocation(java.awt.Dimension)
   * @see java.awt.Window#setLocationRelativeTo
   */
  public static Point getDialogLocation(final Container frame, final Dimension dialogSize) {
    final Point frameLocation = frame.getLocationOnScreen();

    if (logger.isDebugEnabled()) {
      logger.debug("location of frame on screen (" + frameLocation + ")");
    }

    final Dimension frameSize = frame.getSize();

    if (logger.isDebugEnabled()) {
      logger.debug("frame size (" + frameSize + ")");
    }

    final int xCenter = frameSize.width / 2;
    final int yCenter = frameSize.height / 2;
    final int xOffset = -(dialogSize.width / 2);
    final int yOffset = -(dialogSize.height / 2);

    return new Point(frameLocation.x + (xCenter + xOffset), frameLocation.y + (yCenter + yOffset));
  }

  /**
   * Determines the size or resolution of the screen/desktop of the machine running this Java VM.
   * @return a Dimension object indicating the size or resolution of the user's screen/desktop of the machine
   * running this Java VM.
   */
  private static Dimension getScreenSize() {
    return (Boolean.getBoolean("java.awt.headless") ? DEFAULT_SCREEN_SIZE
      : Toolkit.getDefaultToolkit().getScreenSize());
  }

}
