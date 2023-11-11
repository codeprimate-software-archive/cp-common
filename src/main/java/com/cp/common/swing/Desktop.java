/*
 * Desktop.java (c) 22 July 2001
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.3.23
 * @see com.cp.common.swing.XDesktopPane
 * @see com.cp.common.swing.XInternalFrame
 */

package com.cp.common.swing;

import javax.swing.Action;

public interface Desktop {

  /**
   * Closes the currently visible, active internal frame on the desktop that has focus.
   * @see Desktop#closeAll
   */
  public void close();

  /**
   * Closes all internal frames on the desktop.
   * @see Desktop#close
   */
  public void closeAll();

  /**
   * Restores all iconified internal frames currently open in this desktop to their non-iconified state.
   */
  public void deiconifyAll();

  /**
   * Returns an Action to organize all internal frames contained within this desktop in a cascading fashion.
   * @return an Action object to cascade the internal frames on the desktop.
   */
  public Action getCascadeAction();

  /**
   * Returns an Action to organize all internal frames in the desktop by splitting all open, visible internal frames
   * horizontally or veritcally.
   * @param orientation is a integer specifying the orientation of the split.  Acceptable values for orientation are
   * SwingUtilities.HORIZONTAL and SwingUtilities.VERTICAL.
   * @return an Action object to split the internal frames on the desktop.
   */
  public Action getSplitAction(int orientation);

  /**
   * Returns an Action to organize the internal frames within this desktop by tiling all open, visible internal frames.
   * @return an Action object to tile the internal frames on the desktop.
   */
  public Action getTileAction();

  /**
   * Iconifies all internal frames open on this desktop.
   */
  public void iconifyAll();

  /**
   * Maximizes all internal frames open on this desktop by stretching the internal frame's width and height
   * to the largest possible extent.
   */
  public void maximizeAll();

  /**
   * Minimizes all internal frames open on this desktop by setting their width and height to the original
   * values and returng the internal frames to their original location.
   */
  public void minimizeAll();

  /**
   * Sets the active internal frame to the next open internal frame on this desktop.  The internal frame will be
   * made visible and have focus.
   */
  public void next();

  /**
   * Adds the internal frame to the desktop, making it visible and setting focus to the internal frame.
   * @param internalFrame the XInternalFrame object representing the internal frame to add to this desktop.
   */
  public void open(XInternalFrame internalFrame);

  /**
   * Adds the internal frame to the desktop, making it visible and setting focus to the internal frame.
   * @param internalFrame the XInternalFrame object representing the internal frame to add to this desktop.
   * @param layer an Integer value specifying the layer within the container to add the specified internal frame.
   */
  public void open(XInternalFrame internalFrame, Integer layer);

  /**
   * Sets the active internal frame to the previous open internal frame on this desktop.  The internal frame will be
   * made visible and have focus.
   */
  public void previous();

  /**
   * Restores all internal frames within this desktop to their natural state (not maximized or iconified).
   */
  public void restoreAll();

}
