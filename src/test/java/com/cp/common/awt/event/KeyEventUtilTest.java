/*
 * KeyEventUtilTest.java (c) 25 October 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.5.29
 * @see com.cp.common.awt.event.KeyEventUtil
 */

package com.cp.common.awt.event;

import java.awt.event.KeyEvent;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class KeyEventUtilTest extends TestCase {

  public KeyEventUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(KeyEventUtilTest.class);
    //suite.addTest(new KeyEventUtilTest("testName"));
    return suite;
  }

  public void testIsAlphabetic() throws Exception {
    assertTrue(KeyEventUtil.isAlphabetic(KeyEvent.VK_J));
    assertTrue(KeyEventUtil.isAlphabetic(KeyEvent.VK_O));
    assertTrue(KeyEventUtil.isAlphabetic(KeyEvent.VK_H));
    assertTrue(KeyEventUtil.isAlphabetic(KeyEvent.VK_N));
    assertTrue(KeyEventUtil.isAlphabetic(KeyEvent.VK_B));
    assertTrue(KeyEventUtil.isAlphabetic(KeyEvent.VK_L));
    assertTrue(KeyEventUtil.isAlphabetic(KeyEvent.VK_U));
    assertTrue(KeyEventUtil.isAlphabetic(KeyEvent.VK_M));
    assertFalse(KeyEventUtil.isAlphabetic(KeyEvent.VK_0));
    assertFalse(KeyEventUtil.isAlphabetic(KeyEvent.VK_1));
    assertFalse(KeyEventUtil.isAlphabetic(KeyEvent.VK_5));
    assertFalse(KeyEventUtil.isAlphabetic(KeyEvent.VK_NUMPAD0));
    assertFalse(KeyEventUtil.isAlphabetic(KeyEvent.VK_NUMPAD1));
    assertFalse(KeyEventUtil.isAlphabetic(KeyEvent.VK_NUMPAD5));
    assertFalse(KeyEventUtil.isAlphabetic(KeyEvent.VK_F15));
    assertFalse(KeyEventUtil.isAlphabetic(KeyEvent.VK_NUM_LOCK));
    assertFalse(KeyEventUtil.isAlphabetic(KeyEvent.VK_MINUS));
  }

  public void testIsAlphaNumeric() throws Exception {
    assertTrue(KeyEventUtil.isAlphaNumeric(KeyEvent.VK_A));
    assertTrue(KeyEventUtil.isAlphaNumeric(KeyEvent.VK_0));
    assertTrue(KeyEventUtil.isAlphaNumeric(KeyEvent.VK_Z));
    assertTrue(KeyEventUtil.isAlphaNumeric(KeyEvent.VK_0));
    assertTrue(KeyEventUtil.isAlphaNumeric(KeyEvent.VK_1));
    assertTrue(KeyEventUtil.isAlphaNumeric(KeyEvent.VK_9));
    assertTrue(KeyEventUtil.isAlphaNumeric(KeyEvent.VK_NUMPAD0));
    assertTrue(KeyEventUtil.isAlphaNumeric(KeyEvent.VK_NUMPAD1));
    assertTrue(KeyEventUtil.isAlphaNumeric(KeyEvent.VK_NUMPAD9));
    assertFalse(KeyEventUtil.isAlphaNumeric(KeyEvent.VK_EXCLAMATION_MARK));
    assertFalse(KeyEventUtil.isAlphaNumeric(KeyEvent.VK_F2));
    assertFalse(KeyEventUtil.isAlphaNumeric(KeyEvent.VK_AT));
  }

  public void testIsBackSpaceOrDeleteKey() throws Exception {
    assertTrue(KeyEventUtil.isBackSpaceOrDeleteKey(KeyEvent.VK_BACK_SPACE));
    assertTrue(KeyEventUtil.isBackSpaceOrDeleteKey(KeyEvent.VK_DELETE));
    assertFalse(KeyEventUtil.isBackSpaceOrDeleteKey(KeyEvent.VK_SPACE));
    assertFalse(KeyEventUtil.isBackSpaceOrDeleteKey(KeyEvent.VK_UNDEFINED));
  }

  public void testIsNumberKey() throws Exception {
    assertTrue(KeyEventUtil.isNumberKey(KeyEvent.VK_0));
    assertTrue(KeyEventUtil.isNumberKey(KeyEvent.VK_1));
    assertTrue(KeyEventUtil.isNumberKey(KeyEvent.VK_2));
    assertTrue(KeyEventUtil.isNumberKey(KeyEvent.VK_4));
    assertTrue(KeyEventUtil.isNumberKey(KeyEvent.VK_8));
    assertFalse(KeyEventUtil.isNumberKey(KeyEvent.VK_NUMPAD0));
    assertFalse(KeyEventUtil.isNumberKey(KeyEvent.VK_NUMPAD1));
    assertFalse(KeyEventUtil.isNumberKey(KeyEvent.VK_NUMPAD7));
    assertFalse(KeyEventUtil.isNumberKey(KeyEvent.VK_O));
    assertFalse(KeyEventUtil.isNumberKey(KeyEvent.VK_I));
    assertFalse(KeyEventUtil.isNumberKey(KeyEvent.VK_S));
    assertFalse(KeyEventUtil.isNumberKey(KeyEvent.VK_HOME));
    assertFalse(KeyEventUtil.isNumberKey(KeyEvent.VK_NUMBER_SIGN));
    assertFalse(KeyEventUtil.isNumberKey(KeyEvent.VK_PLUS));
  }

  public void testIsNumericKey() throws Exception {
    assertTrue(KeyEventUtil.isNumeric(KeyEvent.VK_0));
    assertTrue(KeyEventUtil.isNumeric(KeyEvent.VK_1));
    assertTrue(KeyEventUtil.isNumeric(KeyEvent.VK_2));
    assertTrue(KeyEventUtil.isNumeric(KeyEvent.VK_NUMPAD0));
    assertTrue(KeyEventUtil.isNumeric(KeyEvent.VK_NUMPAD1));
    assertTrue(KeyEventUtil.isNumeric(KeyEvent.VK_NUMPAD3));
    assertFalse(KeyEventUtil.isNumeric(KeyEvent.VK_I));
    assertFalse(KeyEventUtil.isNumeric(KeyEvent.VK_O));
    assertFalse(KeyEventUtil.isNumeric(KeyEvent.VK_S));
    assertFalse(KeyEventUtil.isNumeric(KeyEvent.VK_PERIOD));
    assertFalse(KeyEventUtil.isNumeric(KeyEvent.VK_MINUS));
    assertFalse(KeyEventUtil.isNumeric(KeyEvent.VK_DIVIDE));
  }

  public void testIsNumPadKey() throws Exception {
    assertTrue(KeyEventUtil.isNumPadKey(KeyEvent.VK_NUMPAD0));
    assertTrue(KeyEventUtil.isNumPadKey(KeyEvent.VK_NUMPAD1));
    assertTrue(KeyEventUtil.isNumPadKey(KeyEvent.VK_NUMPAD2));
    assertTrue(KeyEventUtil.isNumPadKey(KeyEvent.VK_NUMPAD4));
    assertTrue(KeyEventUtil.isNumPadKey(KeyEvent.VK_NUMPAD8));
    assertFalse(KeyEventUtil.isNumPadKey(KeyEvent.VK_0));
    assertFalse(KeyEventUtil.isNumPadKey(KeyEvent.VK_1));
    assertFalse(KeyEventUtil.isNumPadKey(KeyEvent.VK_5));
    assertFalse(KeyEventUtil.isNumPadKey(KeyEvent.VK_I));
    assertFalse(KeyEventUtil.isNumPadKey(KeyEvent.VK_O));
    assertFalse(KeyEventUtil.isNumPadKey(KeyEvent.VK_S));
    assertFalse(KeyEventUtil.isNumPadKey(KeyEvent.VK_MULTIPLY));
    assertFalse(KeyEventUtil.isNumPadKey(KeyEvent.VK_ENTER));
    assertFalse(KeyEventUtil.isNumPadKey(KeyEvent.VK_SLASH));
  }

}
