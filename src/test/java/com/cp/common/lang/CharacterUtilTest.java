/*
 * CharacterUtilTest.java (c) 13 September 2003
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.4.7
 * @see com.cp.common.lang.CharacterUtil
 * @see junit.framework.TestCase
 */

package com.cp.common.lang;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CharacterUtilTest extends TestCase {

  public CharacterUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(CharacterUtilTest.class);
    //suite.addTest(new CharacterUtilTest("testName"));
    return suite;
  }

  public void testToCharacter() throws Exception {
    assertEquals(new Character('A'), CharacterUtil.toCharacter('A'));
  }

  public void testValueOf() throws Exception {
    assertEquals('\0', CharacterUtil.valueOf(null));
    assertEquals('a', CharacterUtil.valueOf(new Character('a')));
    assertEquals('A', CharacterUtil.valueOf(new Character('A')));
    assertEquals('7', CharacterUtil.valueOf(new Character('7')));
    assertEquals('0', CharacterUtil.valueOf(new Character('0'))); // Number Zero
    assertEquals('O', CharacterUtil.valueOf(new Character('O'))); // Capital Letter O
    assertEquals('@', CharacterUtil.valueOf(new Character('@')));
  }

  public void testIsBlank() throws Exception {
    assertTrue(CharacterUtil.isBlank(null));
    assertTrue(CharacterUtil.isBlank('\0'));
    assertTrue(CharacterUtil.isBlank(' '));
    assertFalse(CharacterUtil.isBlank('0'));
    assertFalse(CharacterUtil.isBlank('O'));
    assertFalse(CharacterUtil.isBlank('_'));
    assertFalse(CharacterUtil.isBlank('A'));
    assertFalse(CharacterUtil.isBlank('2'));
    assertFalse(CharacterUtil.isBlank('@'));
    assertFalse(CharacterUtil.isBlank('\''));
  }

  public void testIsDigit() throws Exception {
    assertTrue(CharacterUtil.isDigit('0'));
    assertTrue(CharacterUtil.isDigit('1'));
    assertTrue(CharacterUtil.isDigit('2'));
    assertTrue(CharacterUtil.isDigit('3'));
    assertTrue(CharacterUtil.isDigit('4'));
    assertTrue(CharacterUtil.isDigit('5'));
    assertTrue(CharacterUtil.isDigit('6'));
    assertTrue(CharacterUtil.isDigit('7'));
    assertTrue(CharacterUtil.isDigit('8'));
    assertTrue(CharacterUtil.isDigit('9'));
    assertFalse(CharacterUtil.isDigit(null));
    assertFalse(CharacterUtil.isDigit('\0'));
    assertFalse(CharacterUtil.isDigit('_'));
    assertFalse(CharacterUtil.isDigit('O'));
    assertFalse(CharacterUtil.isDigit('I'));
    assertFalse(CharacterUtil.isDigit('l'));
    assertFalse(CharacterUtil.isDigit('@'));
    assertFalse(CharacterUtil.isDigit('*'));
    assertFalse(CharacterUtil.isDigit('N'));
  }

  public void testIsLetter() throws Exception {
    assertTrue(CharacterUtil.isLetter('a'));
    assertTrue(CharacterUtil.isLetter('z'));
    assertTrue(CharacterUtil.isLetter('A'));
    assertTrue(CharacterUtil.isLetter('Z'));
    assertTrue(CharacterUtil.isLetter('O'));
    assertTrue(CharacterUtil.isLetter('I'));
    assertTrue(CharacterUtil.isLetter('l'));
    assertFalse(CharacterUtil.isLetter(null));
    assertFalse(CharacterUtil.isLetter('\0'));
    assertFalse(CharacterUtil.isLetter('_'));
    assertFalse(CharacterUtil.isLetter('0'));
    assertFalse(CharacterUtil.isLetter('1'));
    assertFalse(CharacterUtil.isLetter('@'));
    assertFalse(CharacterUtil.isLetter('$'));
    assertFalse(CharacterUtil.isLetter('-'));
  }

  public void testIsNotBlank() throws Exception {
    assertFalse(CharacterUtil.isNotBlank(null));
    assertFalse(CharacterUtil.isNotBlank('\0'));
    assertFalse(CharacterUtil.isNotBlank(' '));
    assertTrue(CharacterUtil.isNotBlank('0'));
    assertTrue(CharacterUtil.isNotBlank('O'));
    assertTrue(CharacterUtil.isNotBlank('_'));
    assertTrue(CharacterUtil.isNotBlank('A'));
    assertTrue(CharacterUtil.isNotBlank('2'));
    assertTrue(CharacterUtil.isNotBlank('@'));
    assertTrue(CharacterUtil.isNotBlank('\''));
  }

}
