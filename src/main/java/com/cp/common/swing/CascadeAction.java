/*
 * CascadeAction.java (c) 19 March 2005
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
import org.apache.log4j.Logger;

public final class CascadeAction extends AbstractDesktopAction {

  private static final Logger logger = Logger.getLogger(CascadeAction.class);

  private static final int DEFAULT_COUNT = 10;

  /**
   * Creates an instance of the CascadeAction class initilized with the specified desktop.
   * @param desktop the JDesktopPane object acted upon by this Action class.
   */
  public CascadeAction(final JDesktopPane desktop) {
    super(desktop);
  }

  /**
   * Creates an instance of the CascadeAction class initilized with the specified desktop and name.
   * @param desktop the JDesktopPane object acted upon by this Action class.
   * @param name a String value naming the Action and used to set the button or menu item label.
   */
  public CascadeAction(final JDesktopPane desktop, final String name) {
    super(desktop, name);
  }

  /**
   * Creates an instance of the CascadeAction class initilized with the specified desktop, name and icon.
   * @param desktop the JDesktopPane object acted upon by this Action class.
   * @param name a String value naming the Action and used to set the button or menu item label.
   * @param icon an Icon object specifying the button or menu item icon representing this Action.
   */
  public CascadeAction(final JDesktopPane desktop, final String name, final Icon icon) {
    super(desktop, name, icon);
  }

  /**
   * Method called to carry out the action of arranging the internal frames in the desktop in a cascading fashion.
   * @param event the ActionEvent capturing the event details of the cascade operation.
   */
  public void actionPerformed(final ActionEvent event) {
    final JInternalFrame[] internalFrames = getDesktop().getAllFrames();
    if (logger.isDebugEnabled()) {
      logger.debug("number of internal frames (" + internalFrames.length + ")");
    }

    if (ArrayUtil.isNotEmpty(internalFrames)) {
      final int offset = getOffset(internalFrames[0]);
      if (logger.isDebugEnabled()) {
        logger.debug("offset (" + offset + ")");
      }

      final Dimension recommendedSize = getRecommendedSize(getDesktop());
      if (logger.isDebugEnabled()) {
        logger.debug("recommendedSize (" + recommendedSize + ")");
      }

      int x = 0;
      int y = 0;

      for (int index = 0, count = 1; index < internalFrames.length; index++, count++) {
        final JInternalFrame frame = internalFrames[index];
        if (!frame.isClosed() && frame.isVisible()) {
          try {
            restoreFrame(frame);
            frame.reshape(x, y, recommendedSize.width, recommendedSize.height);
            //getDesktop().getDesktopManager().resizeFrame(frame, x, y, recommendedSize.width, recommendedSize.height);
            frame.moveToFront();
            x += offset;
            y += offset;

            if ((y + recommendedSize.getHeight()) > getDesktop().getHeight() || count > DEFAULT_COUNT) {
              count = 1;
              y = 0;
            }

            if ((x + recommendedSize.getWidth()) > getDesktop().getWidth()) {
              x = 0;
              y = 0;
            }
          }
          catch (PropertyVetoException e) {
            logger.warn("The cascading of internal frame (" + frame.getTitle() + ") was vetoed!", e);
          }
        }

        try {
          internalFrames[internalFrames.length - 1].setSelected(true);
        }
        catch (PropertyVetoException e) {
          logger.warn("Selecting internal frame (" + internalFrames[internalFrames.length -1].getTitle() + ") was vetoed!", e);
        }
      }
    }
  }

  /**
   * Determines the offset used to position internal frames in a cascading fashion (over right and down).
   * @param frame the internal frame used as a baiss for determining the offset.
   * @return an interger value specifying the number of pixels to offset the location of internal frames
   * during the cascade operation.
   */
  private int getOffset(final JInternalFrame frame) {
    final Dimension frameSize = frame.getSize();
    final Dimension frameContentPaneSize = frame.getContentPane().getSize();

    if (logger.isDebugEnabled()) {
      logger.debug("frameSize (" + frameSize + ")");
      logger.debug("frameContentPaneSize (" + frameContentPaneSize + ")");
    }

    return (int) (frameSize.getHeight() - frameContentPaneSize.getHeight());
  }

  /**
   * Returns the "recommended" size of an internal frame when arranging all internal frames in the desktop in a
   * cascading fashion.
   * @param desktop the desktop component used in factoring the size of the internal frames when arranging them
   * in the cascading pattern.
   * @return a Dimension object specifying the internal frame size when arranging the frames in a cascading fashion.
   */
  private Dimension getRecommendedSize(final JDesktopPane desktop) {
    if (desktop instanceof XDesktopPane) {
      return ((XDesktopPane) desktop).getRecommendedFrameSize();
    }
    else {
      final Dimension desktopSize = desktop.getSize();

      if (logger.isDebugEnabled()) {
        logger.debug("desktopSize (" + desktopSize + ")");
      }

      final int frameHeight = (int) (desktopSize.getHeight() * 0.60);
      final int frameWidth = (int) (desktopSize.getWidth() * 0.60);

      if (logger.isDebugEnabled()) {
        logger.debug("frameHeight (" + frameHeight + ")");
        logger.debug("frameWidth (" + frameWidth + ")");
      }

      return new Dimension(frameWidth, frameHeight);
    }
  }

}
