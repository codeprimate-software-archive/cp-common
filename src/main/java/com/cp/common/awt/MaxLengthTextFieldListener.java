/*
 * MaxLengthTextFieldListener.java (c) 17 April 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.5.29
 */

package com.cp.common.awt;

import com.cp.common.awt.event.KeyEventUtil;
import com.cp.common.lang.Assert;
import java.awt.TextComponent;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MaxLengthTextFieldListener extends KeyAdapter {

  private int maxLength;

  protected final Log logger = LogFactory.getLog(getClass());

  /**
   * Creates an instance of the MaxLengthTextFieldListener class to monitor
   * text components for text entered and bound the number of characters
   * entered to maxLength.
   *
   * @param maxLength is an integer value specifying the maximum number
   * characters allowed to be entered in the text component.
   */
  public MaxLengthTextFieldListener(final int maxLength) {
    setMaxLength(maxLength);
  }

  /**
   * Determines whether the number of characters in the text component is
   * greater than or equal to the maximum length of input characters allowed
   * to be entered into the text component by this listener.
   * @param length the length being compared to maxLength.
   * @return a boolean value indicating whether length is greater than or
   * equal to maxLength.
   */
  protected boolean isGreaterThanEqualToMaxLength(final int length) {
    return (length >= getMaxLength());
  }

  /**
   * Returns the maximum number of characters allowed to be entered in the
   * text component.
   * @return an integer value specifying the maximum number of characters
   * allowed to be entered in the text component.
   */
  public int getMaxLength() {
    return maxLength;
  }

  /**
   * Sets the maximum number of characters allowed to be entered in the
   * text component.
   * @param maxLength an integer value specifying the maximum number of
   * characters allowed to be entered into the text component.
   */
  public void setMaxLength(final int maxLength) {
    this.maxLength = validateMaxLenth(maxLength);
  }

  /**
   * Handles events associated with pressing keys in a TextComponent. Specifically,
   * the keyEvent method handles key pressed events in the text component constrained
   * to the number of characters entered from surpassing the maximum length defined
   * by this listener, which was recorded by this class when it was initialized.
   *
   * @param event is a java.awt.event.KeyEvent encapsulating the keyboard event.
   * TODO: work out the details of the user pressed keys!
   */
  public void keyPressed(final KeyEvent event) {
    final TextComponent textComponent = (TextComponent) event.getSource();

    if (logger.isDebugEnabled()) {
      logger.debug("text component (" + textComponent.getName() + ")");
    }

    if (!KeyEventUtil.isBackSpaceOrDeleteKey(event.getKeyCode())
      && isGreaterThanEqualToMaxLength(textComponent.getText().length())) {
      logger.debug("consuming key event for (" + event.getKeyChar() + ")");
      event.consume();
      Toolkit.getDefaultToolkit().beep();
    }
  }

  /**
   * Validates that the maxLenght parameter is a positive value.
   * @param maxLength the maximum number of characters allowed to be entered
   * into the text component monitored by this Listener.
   * @return a the maxLength value if valid otherwise this method throws an
   * IllegalArgumentException
   * @throws IllegalArgumentException if the maxLength value is invalid.
   */
  private int validateMaxLenth(final int maxLength) {
    Assert.greaterThanEqual(maxLength, 0, maxLength + " is not a valid maximum length constraint!");
    return maxLength;
  }

}
