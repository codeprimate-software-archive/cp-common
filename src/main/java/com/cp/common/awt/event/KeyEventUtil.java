/*
 * KeyEventUtil.java (c) 17 April 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.5.29
 * @see java.awt.event.KeyEvent
 */

package com.cp.common.awt.event;

import java.awt.event.KeyEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class KeyEventUtil {

  private static final Log logger = LogFactory.getLog(KeyEventUtil.class);

  /**
   * Private default constructor to support non-instantiability.
   */
  private KeyEventUtil() {
  }

  /**
   * Determines whether the key pressed by the user was an alphabetic key (such as the letters a..z or A..Z).
   * Note, the isAlphabetic method could have been implemented using the following code...
   *
   * <code>return (keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z);</code>
   *
   * However, if the implementation of KeyEvent changes in that ascii characters do not have sequential keyCodes,
   * then the implementation above would fail!
   * @param keyCode is a unique integer value representing the keyboard key pressed (key code).
   * @return a boolean value of true if the key pressed was alphabetic, false otherwise.
   */
  public static boolean isAlphabetic(final int keyCode) {
    logger.debug("keyCode (" + keyCode + ")");

    switch (keyCode) {
      case KeyEvent.VK_A:
      case KeyEvent.VK_B:
      case KeyEvent.VK_C:
      case KeyEvent.VK_D:
      case KeyEvent.VK_E:
      case KeyEvent.VK_F:
      case KeyEvent.VK_G:
      case KeyEvent.VK_H:
      case KeyEvent.VK_I:
      case KeyEvent.VK_J:
      case KeyEvent.VK_K:
      case KeyEvent.VK_L:
      case KeyEvent.VK_M:
      case KeyEvent.VK_N:
      case KeyEvent.VK_O:
      case KeyEvent.VK_P:
      case KeyEvent.VK_Q:
      case KeyEvent.VK_R:
      case KeyEvent.VK_S:
      case KeyEvent.VK_T:
      case KeyEvent.VK_U:
      case KeyEvent.VK_V:
      case KeyEvent.VK_W:
      case KeyEvent.VK_X:
      case KeyEvent.VK_Y:
      case KeyEvent.VK_Z:
        return true;
      default:
        return false;
    }
  }

  /**
   * Determines whether the key pressed by the user is alphabetic or numeric.
   * @param keyCode is a unique integer value representing the keyboard key pressed (key code).
   * @return a boolean value of true if the keyboard key pressed was alphanumeric, false otherwise.
   */
  public static boolean isAlphaNumeric(final int keyCode) {
    return (isAlphabetic(keyCode) || isNumeric(keyCode));
  }

  /**
   * Determines whether the key code matches the back space or the delete key.
   * @param keyCode the code uniquely identifying the keyboard key.
   * @return a boolean value indicating if the either the back space or delete key was pressed.
   */
  public static boolean isBackSpaceOrDeleteKey(final int keyCode) {
    logger.debug("keyCode (" + keyCode + ")");
    switch (keyCode) {
      case KeyEvent.VK_BACK_SPACE:
      case KeyEvent.VK_DELETE:
        return true;
      default:
        return false;
    }
  }

  /**
   * Determines whether one of the 9 number keys above the letter keys on a standard US keyboard has been pressed
   * by the user. Note, the isNumberKey method could have been implemented using the following code...
   *
   * <code>return (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9);</code>
   *
   * However, if the implementation of KeyEvent changes in that numbers do not have sequential keyCodes, then
   * the implementation above would fail!
   * @param keyCode is a unique integer value representing the keyboard key pressed (key code).
   * @return a boolean value of true if the key pressed was a number, false otherwise.
   */
  public static boolean isNumberKey(final int keyCode) {
    logger.debug("keyCode (" + keyCode + ")");

    switch (keyCode) {
      case KeyEvent.VK_0:
      case KeyEvent.VK_1:
      case KeyEvent.VK_2:
      case KeyEvent.VK_3:
      case KeyEvent.VK_4:
      case KeyEvent.VK_5:
      case KeyEvent.VK_6:
      case KeyEvent.VK_7:
      case KeyEvent.VK_8:
      case KeyEvent.VK_9:
        return true;
      default:
        return false;
    }
  }

  /**
   * Determines whether the user pressed a numeric key on the keyboard (either one of the standard keys, or a
   * numeric key on the numeric key pad).
   * @param keyCode is a unique integer value representing the keyboard key pressed (key code).
   * @return a boolean value of true if the key pressed was numeric, false otherwise.
   */
  public static boolean isNumeric(final int keyCode) {
    return (isNumberKey(keyCode) || isNumPadKey(keyCode));
  }

  /**
   * Determines whether one of the number key pad keys have been pressed by the user.
   * Note, the isNumPadKey method could have been implemented using the following code...
   *
   * <code>return (keyCode >= KeyEvent.VK_NUMPAD0 && keyCode <= KeyEvent.VK_NUMPAD9);</code>
   *
   * However, if the implementation of KeyEvent changes in that numbers do not have sequential keyCodes, then
   * the implementation above would fail!
   * @param keyCode is a unique integer value representing the keyboard key pressed (key code).
   * @return a boolean value of true if the number key pad key was pressed, false otherwise.
   */
  public static boolean isNumPadKey(final int keyCode) {
    logger.debug("keyCode (" + keyCode + ")");

    switch (keyCode) {
      case KeyEvent.VK_NUMPAD0:
      case KeyEvent.VK_NUMPAD1:
      case KeyEvent.VK_NUMPAD2:
      case KeyEvent.VK_NUMPAD3:
      case KeyEvent.VK_NUMPAD4:
      case KeyEvent.VK_NUMPAD5:
      case KeyEvent.VK_NUMPAD6:
      case KeyEvent.VK_NUMPAD7:
      case KeyEvent.VK_NUMPAD8:
      case KeyEvent.VK_NUMPAD9:
        return true;
      default:
        return false;
    }
  }

}
