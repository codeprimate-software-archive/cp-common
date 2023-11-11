/*
 * CharacterUtil.java (c) 13 September 2003
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.4.7
 * @see java.lang.Character
 * @see com.cp.common.lang.BooleanUtil
 * @see com.cp.common.lang.NumberUtil
 * @see com.cp.common.lang.ObjectUtil
 * @see com.cp.common.lang.StringUtil
 * @see java.lang.Character
 */

package com.cp.common.lang;

public final class CharacterUtil {

  /**
   * Default private constructor to prevent instantiation of the CharacterUtil utility class.
   */
  private CharacterUtil() {
  }

  /**
   * Converts the primitive char value into a big Character wrapper object.
   * @param value the char value being converted to a big Character.
   * @return a big Character object containing the value of the specified char.
   * @see CharacterUtil#valueOf(Character)
   */
  public static Character toCharacter(final char value) {
    return new Character(value);
  }

  /**
   * Returns the primitive char value for the corresponding Character wrapper object if not null or '\0'
   * if the Character wrapper object is null.
   * @param value the Character wrapper object value.
   * @return the primitive char value of the Character wrapper object if not null or '\0' if null.
   * @see CharacterUtil#toCharacter(char)
   */
  public static char valueOf(final Character value) {
    return (ObjectUtil.isNull(value) ? '\0' : value);
  }

  /**
   * Determines whether the specified Character value is null or blank (whitespace).
   * @param value the Character being evaluated to determine if the char value is blank.
   * @return a boolean value indicating if the Character value is blank (null or whitespace).
   * @see CharacterUtil#isNotBlank(Character)
   */
  public static boolean isBlank(final Character value) {
    final char charValue = valueOf(value);
    return (charValue == '\0' || charValue == ' ');
  }

  /**
   * Determines whether the specified Character value is a numerical value (a digit 0..9).
   * @param value the Character being evaluated to determine if the char value is a number.
   * @return a boolean value indicating if the Character value is a number.
   * @see CharacterUtil#isDigit(Character)
   */
  public static boolean isDigit(final Character value) {
    return Character.isDigit(valueOf(value));
  }

  /**
   * Determines whether the specified Character value is a letter (a..z or A..Z).
   * @param value the Character being evaluated to determine if the char value is a letter.
   * @return a boolean value indicating if the Character value is a letter.
   * @see CharacterUtil#isDigit(Character)
   */
  public static boolean isLetter(final Character value) {
    return Character.isLetter(valueOf(value));
  }

  /**
   * Determines whether the specified Character value is not null and not blank (whitespace).
   * @param value the Character being evaluated to determine if the char value is not blank.
   * @return a boolean value indicating if the Character value is not blank (null or whitespace).
   * @see CharacterUtil#isBlank(Character)
   */
  public static boolean isNotBlank(final Character value) {
    return !isBlank(value);
  }

}
