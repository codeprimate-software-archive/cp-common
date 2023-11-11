/*
 * AbstractDesktopAction.java (c) 19 March 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.3.23
 * @see javax.swing.AbstractAction
 * @see javax.swing.Action
 * @see com.cp.common.swing.CascadeAction
 * @see com.cp.common.swing.SplitAction
 * @see com.cp.common.swing.TileAction
 */

package com.cp.common.swing;

import com.cp.common.lang.ObjectUtil;
import java.beans.PropertyVetoException;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import org.apache.log4j.Logger;

public abstract class AbstractDesktopAction extends AbstractAction {

  private static final Logger logger = Logger.getLogger(AbstractDesktopAction.class);

  private JDesktopPane desktop;

  /**
   * Creates an instance of the AbtractDesktopAction class initilized with the specified desktop.
   * @param desktop the JDesktopPane object acted upon on by this Action class.
   */
  public AbstractDesktopAction(final JDesktopPane desktop) {
    setDesktop(desktop);
  }

  /**
   * Creates an instance of the AbtractDesktopAction class initilized with the specified desktop and name.
   * @param desktop the JDesktopPane object acted upon by this Action class.
   * @param name a String value naming the Action and used to set the button or menu item label.
   */
  public AbstractDesktopAction(final JDesktopPane desktop, final String name) {
    super(name);
    setDesktop(desktop);
  }

  /**
   * Creates an instance of the AbtractDesktopAction class initilized with the specified desktop, name and icon.
   * @param desktop the JDesktopPane object acted upon by this Action class.
   * @param name a String value naming the Action and used to set the button or menu item label.
   * @param icon an Icon object specifying the button or menu item icon representing this Action.
   */
  public AbstractDesktopAction(final JDesktopPane desktop, final String name, final Icon icon) {
    super(name, icon);
    setDesktop(desktop);
  }

  /**
   * Returns the desktop associated with this Action.
   * @return an instance of the JDesktopPane component for which this Action object acts upon.
   */
  public JDesktopPane getDesktop() {
    return desktop;
  }

  /**
   * Sets the desktop component object associated with this Action.
   * @param desktop the JDesktopPane instance associated with this Action.
   */
  private void setDesktop(final JDesktopPane desktop) {
    if (ObjectUtil.isNull(desktop)) {
      logger.warn("The desktop cannot be null!");
      throw new NullPointerException("The desktop cannot be null!");
    }
    this.desktop = desktop;
  }

  /**
   * Restores the internal frame from an iconfied or maximized state.
   * @param frame the internal frame to restore.
   * @throws PropertyVetoException if the act of deiconifying or minimizing the internal frame is vetoed.
   */
  protected void restoreFrame(final JInternalFrame frame) throws PropertyVetoException {
    if (frame.isIcon()) {
      frame.setIcon(false);
    }
    if (frame.isMaximum()) {
      frame.setMaximum(false);
    }
  }

}
