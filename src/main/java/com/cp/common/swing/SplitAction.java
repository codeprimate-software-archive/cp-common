/*
 * SplitAction.java (c) 19 March 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.3.27
 * @see com.cp.common.swing.AbstractDesktopAction
 * @see javax.swing.AbstractAction
 */

package com.cp.common.swing;

import com.cp.common.util.ArrayUtil;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import javax.swing.Icon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;
import org.apache.log4j.Logger;

public final class SplitAction extends AbstractDesktopAction {

  private static final Logger logger = Logger.getLogger(SplitAction.class);

  private static final int DEFAULT_ORIENTATION = SwingConstants.VERTICAL;

  private int orientation;

  /**
   * Creates an instance of the SplitAction class initilized with the specified desktop.
   * @param desktop the JDesktopPane object acted upon by this Action class.
   * @param orientation the orientation of the split, either horizontal or vertical.
   */
  public SplitAction(final JDesktopPane desktop, final int orientation) {
    super(desktop);
    setOrientation(orientation);
  }

  /**
   * Creates an instance of the SplitAction class initilized with the specified desktop and name.
   * @param desktop the JDesktopPane object acted upon by this Action class.
   * @param orientation the orientation of the split, either horizontal or vertical.
   * @param name a String value naming the Action and used to set the button or menu item label.
   */
  public SplitAction(final JDesktopPane desktop,  final int orientation, final String name) {
    super(desktop, name);
    setOrientation(orientation);
  }

  /**
   * Creates an instance of the SplitAction class initilized with the specified desktop, name and icon.
   * @param desktop the JDesktopPane object acted upon by this Action class.
   * @param orientation the orientation of the split, either horizontal or vertical.
   * @param name a String value naming the Action and used to set the button or menu item label.
   * @param icon an Icon object specifying the button or menu item icon representing this Action.
   */
  public SplitAction(final JDesktopPane desktop, final int orientation, final String name, final Icon icon) {
    super(desktop, name, icon);
    setOrientation(orientation);
  }

  /**
   * Method called to carry out the action of splitting the internal frames in the desktop.
   * @param event the ActionEvent capturing the event details of the split operation.
   */
  public void actionPerformed(final ActionEvent event) {
    final JInternalFrame[] internalFrames = getDesktop().getAllFrames();
    if (logger.isDebugEnabled()) {
      logger.debug("number of internal frames (" + internalFrames.length + ")");
    }

    if (ArrayUtil.isNotEmpty(internalFrames)) {
      final Dimension frameSize = getFrameSize(getDesktop());
      int x = 0;
      int y = 0;

      for (int index = 0; index < internalFrames.length; index++) {
        final JInternalFrame frame = internalFrames[index];
        if (!frame.isClosed() && frame.isVisible()) {
          try {
            restoreFrame(frame);
            frame.reshape(x, y, frameSize.width, frameSize.height);
            //getDesktop().getDesktopManager().resizeFrame(frame, x, y, frameSize.width, frameSize.height);

            if (getOrientation() == SwingConstants.HORIZONTAL) {
              y += frameSize.height;
            }
            else {
              x += frameSize.width;
            }
          }
          catch (PropertyVetoException e) {
            logger.warn("The splitting of internal frame (" + frame.getTitle() + ") was vetoed!", e);
          }
        }
      }
    }
  }

  /**
   * Determines size of an internal frame when splitting all internal frames in the desktop.
   * @param desktop the desktop component used in factoring the size of the internal frames when splitting them
   * to fill the desktop.
   * @return a Dimension object specifying the internal frame size when splitting them across the desktop.
   */
  private Dimension getFrameSize(final JDesktopPane desktop) {
    final int frameCount = desktop.getAllFrames().length;
    final Dimension desktopSize = desktop.getSize();

    int width = 0;
    int height = 0;

    if (getOrientation() == SwingConstants.HORIZONTAL) {
      width = desktopSize.width;
      height = (int) (desktopSize.getHeight() / frameCount);
    }
    else {
      height = desktopSize.height;
      width = (int) (desktopSize.getWidth() / frameCount);
    }

    if (logger.isDebugEnabled()) {
      logger.debug("width (" + width + ")");
      logger.debug("height (" + height + ")");
    }

    return new Dimension(width, height);
  }

  /**
   * Returns the orientation used to split the internal frames across the desktop.
   * @return a integer value specifying either the HORIZONTAL or VERTICAL split.
   */
  public int getOrientation() {
    return orientation;
  }

  /**
   * Sets the orientation used to split internal frames across the desktop.
   * @param orientation an integer value specifying either a HORIZONTAL or VERTICAL split.
   * @throws IllegalArgumentException if the value of the orientation is invalid.  The value for orientation must
   * be one of SwingConstants.HORIZONTAL or SwingConstants.VERTICAL.
   */
  private void setOrientation(final int orientation) {
    switch (orientation) {
      case SwingConstants.HORIZONTAL:
      case SwingConstants.VERTICAL:
        this.orientation = orientation;
        break;
      default:
        logger.warn("The orientation (" + orientation + ") is invalid!");
        throw new IllegalArgumentException("The orientation (" + orientation + ") is invalid!");
    }
  }

}
