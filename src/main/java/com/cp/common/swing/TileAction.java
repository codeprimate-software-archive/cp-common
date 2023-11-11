/*
 * TileAction.java (c) 19 March 2005
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

public final class TileAction extends AbstractDesktopAction {

  private static final Logger logger = Logger.getLogger(TileAction.class);

  /**
   * Creates an instance of the TileAction class initilized with the specified desktop.
   * @param desktop the JDesktopPane object acted upon by this Action class.
   */
  public TileAction(final JDesktopPane desktop) {
    super(desktop);
  }

  /**
   * Creates an instance of the TileAction class initilized with the specified desktop and name.
   * @param desktop the JDesktopPane object acted upon by this Action class.
   * @param name a String value naming the Action and used to set the button or menu item label.
   */
  public TileAction(final JDesktopPane desktop, final String name) {
    super(desktop, name);
  }

  /**
   * Creates an instance of the TileAction class initilized with the specified desktop, name and icon.
   * @param desktop the JDesktopPane object acted upon by this Action class.
   * @param name a String value naming the Action and used to set the button or menu item label.
   * @param icon an Icon object specifying the button or menu item icon representing this Action.
   */
  public TileAction(final JDesktopPane desktop, final String name, final Icon icon) {
    super(desktop, name, icon);
  }

  /**
   * Method called to carry out the action of tiling the internal frames in the desktop.
   * @param event the ActionEvent capturing the event details of the tile operation.
   */
  public void actionPerformed(final ActionEvent event) {
    final JInternalFrame[] internalFrames = getDesktop().getAllFrames();
    if (logger.isDebugEnabled()) {
      logger.debug("number of internal frames (" + internalFrames.length + ")");
    }

    if (ArrayUtil.isNotEmpty(internalFrames)) {
      final int frameCount = internalFrames.length;
      int cols = (int) Math.sqrt(frameCount);
      int rows = (frameCount / cols);

      if ((cols * rows) < frameCount) {
        cols++;
        if ((cols * rows) < frameCount) {
          rows++;
        }
      }

      if (logger.isDebugEnabled()) {
        logger.debug("frameCount (" + frameCount + ")");
        logger.debug("cols (" + cols + ")");
        logger.debug("rows (" + rows + ")");
      }

      final Dimension desktopSize = getDesktop().getSize();
      final int frameWidth = (int) (desktopSize.getWidth() / cols);
      final int frameHeight = (int) (desktopSize.getHeight() / rows);

      if (logger.isDebugEnabled()) {
        logger.debug("desktopSize (" + desktopSize + ")");
        logger.debug("frameWidth (" + frameWidth + ")");
        logger.debug("frameHeight (" + frameHeight + ")");
      }

      int x = 0;
      int y = 0;

      for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
        for (int colIndex = 0; colIndex < cols && (rowIndex * cols + colIndex) < internalFrames.length; colIndex++) {
          final JInternalFrame frame = internalFrames[rowIndex * cols + colIndex];
          if (!frame.isClosed() && frame.isVisible()) {
            try {
              restoreFrame(frame);
              frame.reshape(x, y, frameWidth, frameHeight);
              //getDesktop().getDesktopManager().resizeFrame(frame, x, y, frameWidth, frameHeight);
              x += frameWidth;
            }
            catch (PropertyVetoException e) {
              logger.warn("The tiling of internal frame (" + frame.getTitle() + ") was vetoed!", e);
            }
          }
        }
        y += frameHeight;
        x = 0;
      }
    }
  }

}
