/*
 * XInternalFrame.java (c) 17 April 2002
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.4.6
 * @see javax.swing.JInternalFrame
 * @see com.cp.common.swing.Desktop
 * @see com.cp.common.swing.XDesktopPane
 */

package com.cp.common.swing;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.KeyStroke;

public abstract class XInternalFrame extends JInternalFrame {

  private static final boolean DEFAULT_CLOSABLE = true;
  private static final boolean DEFAULT_ICONIFIABLE = true;
  private static final boolean DEFAULT_MAXIMIZABLE = true;
  private static final boolean DEFAULT_RESIZABLE = true;

  private static final String NEXT_FRAME_ACTION_KEY = "NEXT_INTERNAL_FRAME_ACTION_KEY";
  private static final String PREVIOUS_FRAME_ACTION_KEY = "PREVIOUS_INTERNAL_FRAME_ACTION_KEY";

  private XInternalFrame next;
  private XInternalFrame previous;

  /**
   * Creates an instance of the XInternalFrame class with references to the next and previous internal frames
   * in the sequence of internal frames within the desktop in which this internal frame resides.
   * @param next is an XInternalFrame object referring to the next internal frame in the sequence after this
   * internal frame within the desktop
   * @param previous is an XInternalFrame object referring to the previous internal frame in the sequence before this
   * internal frame within the desktop
   * @param windowTitle a String value specifying the title to display in this internal frame's title bar.
   * @param resizable a boolean value indicating whether this internal frame can be resized by the user.
   * @param closable a boolean value indicating whether the user can close this internal frame.
   * @param maximizable a boolean value indicating whether the user can maximize this internal frame.
   * @param iconifiable a boolean value indicating whether the internal frame can iconified.
   */
  public XInternalFrame(final XInternalFrame next,
                        final XInternalFrame previous,
                        final String windowTitle,
                        final boolean resizable,
                        final boolean closable,
                        final boolean maximizable,
                        final boolean iconifiable) {
    super(windowTitle, resizable, closable, maximizable, iconifiable);
    setNextPrevious(next, previous);
    //installKeyboardActions();
    installKeyListener();
  }

  /**
   * Creates an instance of the XInternalFrame class with references to the next and previous internal frames
   * in the sequence of internal frames within the desktop in which this internal frame resides.
   * @param next is an XInternalFrame object referring to the next internal frame in the sequence after this
   * internal frame within the desktop
   * @param previous is an XInternalFrame object referring to the previous internal frame in the sequence before this
   * internal frame within the desktop
   * @param windowTitle a String value specifying the title to display in this internal frame's title bar.
   */
  public XInternalFrame(final XInternalFrame next,
                        final XInternalFrame previous,
                        final String windowTitle) {
    this(next, previous, windowTitle, DEFAULT_RESIZABLE, DEFAULT_CLOSABLE, DEFAULT_MAXIMIZABLE, DEFAULT_ICONIFIABLE);
  }

  // @deprecated
  private void installKeyboardActions() {
    final ActionMap actionMap = getActionMap();

    actionMap.put(NEXT_FRAME_ACTION_KEY, new AbstractAction() {
      public void actionPerformed(final ActionEvent event) {
        System.out.println("NEXT");
        ((XDesktopPane) getParent()).next();
      }
    });

    actionMap.put(PREVIOUS_FRAME_ACTION_KEY, new AbstractAction() {
      public void actionPerformed(final ActionEvent event) {
        System.out.println("PREVIOUS ACTION");
        ((XDesktopPane) getParent()).previous();
      }
    });

    final InputMap keyMap = getInputMap(JComponent.WHEN_FOCUSED);
    keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.VK_ALT, false), NEXT_FRAME_ACTION_KEY);
    keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.VK_ALT, false), PREVIOUS_FRAME_ACTION_KEY);
  }

  // @deprecated
  private void installKeyListener() {
    addKeyListener(new KeyAdapter() {
      public void keyPressed(final KeyEvent event) {
        System.out.println("TEST");
        if (event.getModifiers() == KeyEvent.VK_ALT) {
          if (event.getKeyCode() == KeyEvent.VK_LEFT) {
            ((XDesktopPane) getParent()).previous();
          }
          else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
            ((XDesktopPane) getParent()).next();
          }
        }
      }
    });
  }

  /**
   * Returns the next XInternalFrame in the sequence of internal frames after this internal frame within the desktop.
   * @return an XInternalFrame object referring to the internal frame after this internal frame within the desktop.
   */
  public XInternalFrame getNext() {
    return next;
  }

  /**
   * Returns the previous XInternalFrame in the sequence of internal frames before this internal frame within the desktop.
   * @return an XInternalFrame object referring to the internal frame before this internal frame within the desktop.
   */
  public XInternalFrame getPrevious() {
    return previous;
  }

  /**
   * Sets the next XInternalFrame in the sequence of internal frames after this internal frame within the desktop.
   * @param next is an XInternalFrame object referring to the next internal frame after this internal frame within
   * the desktop.
   */
  public void setNext(final XInternalFrame next) {
    this.next = next;
  }

  /**
   * This method is called to set both the next & previous XInternalFrame objects in the sequence of internal frames
   * before and after this internal frame within the desktop.
   * @param next is an XInternalFrame object referring to the next internal frame after this internal frame within
   * the desktop.
   * @param previous is an XInternalFrame object referring to the previous internal frame before this internal frame
   * within the desktop.
   */
  public final void setNextPrevious(final XInternalFrame next, final XInternalFrame previous) {
    this.next = next;
    this.previous = previous;
  }

  /**
   * Sets the previous XInternalFrame in the sequence of internal frames before this internal frame within the desktop.
   * @param previous is an XInternalFrame object referring to the previous internal frame before this internal frame
   * within the desktop.
   */
  public void setPrevious(final XInternalFrame previous) {
    this.previous = previous;
  }

}
