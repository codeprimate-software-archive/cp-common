/*
 * XDesktopManager.java (c) 12 February 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.3.23
 */

package com.cp.common.swing;

import com.cp.common.lang.ObjectUtil;
import java.beans.PropertyVetoException;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.DefaultDesktopManager;
import javax.swing.JInternalFrame;
import org.apache.log4j.Logger;

public final class XDesktopManager extends DefaultDesktopManager {

  private static final Logger logger = Logger.getLogger(XDesktopManager.class);

  private final XDesktopPane desktop;

  /**
   * Creates an instance of the XDesktopManager class to handle custom functionality and behavior of the
   * XDesktopPane class.
   * @param desktop a reference to the XDesktopPane class that this desktop manager class serves.
   */
  public XDesktopManager(final XDesktopPane desktop) {
    if (ObjectUtil.isNull(desktop)) {
      logger.warn("The XDesktopPane cannot be null!");
      throw new NullPointerException("The XDesktopPane cannot be null!");
    }
    this.desktop = desktop;
  }

  /**
   * Returns the XDesktopPane class governed by this desktop manager class.
   * @return the XDesktopPane class that is associated with this desktop manager.
   */
  private XDesktopPane getDesktop() {
    return desktop;
  }

  /**
   * Overridden method for setting the active internal frame as the current frame of the desktop.
   * @param frame the internal frame of the desktop being activated.
   * @see XDesktopPane#setCurrentFrame
   */
  public void activateFrame(final JInternalFrame frame) {
    super.activateFrame(frame);
    getDesktop().setCurrentFrame((XInternalFrame) frame);
  }

  /**
   * Restores all iconified internal frames currently open in the desktop to their non-iconified state.
   */
  public void deiconifyAll() {
    for (Iterator it = Arrays.asList(getDesktop().getAllFrames()).iterator(); it.hasNext(); ) {
      final JInternalFrame internalFrame = (JInternalFrame) it.next();
      if (logger.isDebugEnabled()) {
        logger.debug("deiconifying internal frame (" + internalFrame.getTitle() + ")");
      }
      try {
        internalFrame.setIcon(false);
      }
      catch (PropertyVetoException e) {
        logger.warn("Deiconifying internal frame (" + internalFrame.getName() + ") was vetoed!", e);
      }
    }
  }

  /**
   * Iconifies all internal frames open on the desktop govnered by this manager.
   */
  public void iconifyAll() {
    for (Iterator it = Arrays.asList(getDesktop().getAllFrames()).iterator(); it.hasNext(); ) {
      final JInternalFrame internalFrame = (JInternalFrame) it.next();
      if (logger.isDebugEnabled()) {
        logger.debug("iconify internal frame (" + internalFrame.getTitle() + ")");
      }
      try {
        internalFrame.setIcon(true);
      }
      catch (PropertyVetoException e) {
        logger.warn("Iconifying internal frame (" + internalFrame.getName() + ") was vetoed!", e);
      }
    }
  }

  /**
   * Maximizes all internal frames open on the desktop by stretching the internal frame's width and height
   * to the largest possible extent to fill the desktop.
   */
  public void maximizeAll() {
    for (Iterator it = Arrays.asList(getDesktop().getAllFrames()).iterator(); it.hasNext(); ) {
      final JInternalFrame internalFrame = (JInternalFrame) it.next();
      if (logger.isDebugEnabled()) {
        logger.debug("maximizing internal frame (" + internalFrame.getTitle() + ")");
      }
      try {
        internalFrame.setMaximum(true);
      }
      catch (PropertyVetoException e) {
        logger.warn("Maximizing the internal frame (" + internalFrame.getName() + ") was vetoed!", e);
      }
    }
  }

  /**
   * Minimizes all internal frames open on the desktop by setting their width and height to the original
   * values and returng the internal frames to their original location.
   */
  public void minimizeAll() {
    for (Iterator it = Arrays.asList(getDesktop().getAllFrames()).iterator(); it.hasNext(); ) {
      final JInternalFrame internalFrame = (JInternalFrame) it.next();
      if (logger.isDebugEnabled()) {
        logger.debug("minimizing internal frame (" + internalFrame.getTitle() + ")");
      }
      try {
        internalFrame.setMaximum(false);
      }
      catch (PropertyVetoException e) {
        logger.warn("Minimizing internal frame (" + internalFrame.getName() + ")");
      }
    }
  }

  /**
   * Restores all internal frames within this desktop to their natural state (not maximized or iconified).
   */
  public void restoreAll() {
    deiconifyAll();
    minimizeAll();
  }

}
