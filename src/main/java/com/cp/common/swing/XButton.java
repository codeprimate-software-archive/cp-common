/*
 * XButton.java (c) 17 April 2001
 *
 * This class (component) extends the javax.swing.JButton component in the
 * Swing library (JFC, javax.swing.*) to implement a borderless button.
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.1.17
 */

package com.cp.common.swing;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;

public class XButton extends JButton {

  /**
   * Default constructor.  Creates a button with no set text or icon.
   */
  public XButton() {
    init();
  }

  /**
   * Creates a button where properties are taken from the Action supplied.
   * @param a is an Action object defining the behavior, or action, of this button when clicked.
   */
  public XButton(final Action a) {
    super(a);
    init();
  }

  /**
   * Creates a button with an icon.
   * @param icon is an image to be displayed on the button.
   */
  public XButton(final Icon icon) {
    super(icon);
    init();
  }

  /**
   * Creates a button with text.
   * @param text is a String value containing the text to be displayed on the button.
   */
  public XButton(final String text) {
    super(text);
    init();
  }

  /**
   * Creates a button with initial text and an icon.
   * @param icon is an image to be displayed on the button.
   * @param text is a String value containing the text to be displayed on the button.
   */
  public XButton(final String text, final Icon icon) {
    super(text, icon);
    init();
  }

  /**
   * Initializes this button with event handlers and it's initial border.
   */
  private void init() {
    addFocusListener(new FocusHandler());
    addMouseListener(new MouseHandler());
    setBorder(BorderFactory.createRaisedBevelBorder());
    setBorderPainted(false);
  }

  /**
   * This method is called to enable or disable this button.
   * @param enable is a boolean value indicating true if the button should be enabled, false otherwise.
   */
  public void setEnabled(final boolean enable) {
    super.setEnabled(enable);
    setBorderPainted(false);
  }

  private final class FocusHandler extends FocusAdapter {

    public void focusLost(FocusEvent fe) {
      if (isEnabled()) {
        setBorderPainted(false);
      }
    }
  }

  private final class MouseHandler extends MouseAdapter {

    public void mouseEntered(MouseEvent me) {
      if (isEnabled()) {
        setBorderPainted(true);
      }
    }

    public void mouseExited(MouseEvent me) {
      if (isEnabled()) {
        setBorderPainted(false);
      }
    }

    public void mousePressed(MouseEvent me) {
      if (isEnabled()) {
        setBorder(BorderFactory.createLoweredBevelBorder());
      }
    }

    public void mouseReleased(MouseEvent me) {
      if (isEnabled()) {
        setBorder(BorderFactory.createRaisedBevelBorder());
      }
    }
  }

}
