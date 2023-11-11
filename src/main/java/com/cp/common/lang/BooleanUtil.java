/*
 * BooleanUtil.java (c) 13 September 2003
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.4.6
 * @see java.lang.Boolean
 * @see com.cp.common.lang.CharacterUtil
 * @see com.cp.common.lang.NumberUtil
 * @see com.cp.common.lang.ObjectUtil
 * @see com.cp.common.lang.StringUtil
 * @see java.lang.Boolean
 */

package com.cp.common.lang;

import java.util.Set;
import java.util.TreeSet;

public final class BooleanUtil {

  private static final Set<String> trueStringValueSet = new TreeSet<String>();

  static {
    trueStringValueSet.add("true");
    trueStringValueSet.add("1");
    trueStringValueSet.add("on");
    trueStringValueSet.add("one");
    trueStringValueSet.add("y");
    trueStringValueSet.add("yes");
  }

  /**
   * Default private constructor to prevent instantiation of the BooleanUtil utility class.
   */
  private BooleanUtil() {
  }

  /**
   * Performs a logical and operation on the specified big Boolean values.  The method returns true if and only if
   * neither of the big Boolean values are null and both evaluate to true.
   * @param operand1 a Boolean value in the and operation.
   * @param operand2 a Boolean value in the and operation.
   * @return a boolean value indicating the result of evaluating the big Booleans using the and operator.
   * @see BooleanUtil#or(Boolean, Boolean)
   * @see BooleanUtil#xor(Boolean, Boolean)
   */
  public static boolean and(final Boolean operand1, final Boolean operand2) {
    return (valueOf(operand1) && valueOf(operand2));
  }

  /**
   * Gets the negated value of the specified Boolean wrapper object.  If the value is Boolean.TRUE then Boolean.FALSE
   * is returned.  If the value is null or Boolean.FALSE then Boolean.TRUE is returned.
   * @param value the Boolean wrapper object to negate.
   * @return the opposite big Boolean value of value.
   * @see BooleanUtil#valueOf(Boolean)
   */
  public static Boolean negate(final Boolean value) {
    return !valueOf(value);
  }

  /**
   * Performs a logical or operation on the specified big Boolean values.  The method returns true if and only if
   * at least one of the big Boolean values are not null and evaluates to true.
   * @param operand1 a Boolean value in the or operation.
   * @param operand2 a Boolean value in the or operation.
   * @return a boolean value indicating the result of evaluating the big Booleans using the or operator.
   * @see BooleanUtil#and(Boolean, Boolean)
   * @see BooleanUtil#xor(Boolean, Boolean)
   */
  public static boolean or(final Boolean operand1, final Boolean operand2) {
    return (valueOf(operand1) || valueOf(operand2));
  }

  /**
   * Gets the equivalent Boolean wrapper object for the specified primitive boolean value.
   * @param value the primitive boolean value to convert to big Boolean.
   * @return a Boolean wrapper object containing the value of the primitive boolean value.
   * @see BooleanUtil#toInteger(Boolean, int, int)
   * @see BooleanUtil#toString(Boolean, String, String)
   */
  public static Boolean toBoolean(final boolean value) {
    return (value ? Boolean.TRUE : Boolean.FALSE);
  }

  /**
   * Get an integer representation of the specified big Boolean value.  Returns the specified trueValue int when
   * the big Boolean value evaluates to true and returns the specified falseValue int when the big Boolean value
   * evaluates to false.
   * @param value the Boolean wrapper object being converted to a corresponding integer value.
   * @param trueValue the integer value when the big Boolean evaluates to true.
   * @param falseValue the integer value when the big Boolean evaluates to false.
   * @return an integer representation of the specified big Boolean value.
   * @see BooleanUtil#valueOf(Boolean)
   * @see BooleanUtil#toBoolean(boolean)
   * @see BooleanUtil#toString(Boolean, String, String)
   */
  public static int toInteger(final Boolean value, final int trueValue, final int falseValue) {
    return (valueOf(value) ? trueValue : falseValue);
  }

  /**
   * Get a String representation of the specified big Boolean value.  Returns the specified trueValue String when
   * the big Boolean value evaluates to true and returns the specified falseValue String when the big Boolean value
   * evaluates to false.
   * @param value the Boolean wrapper object being converted to a corresponding String value.
   * @param trueValue the String value when the big Boolean evaluates to true.
   * @param falseValue the String value when the big Boolean evaluates to false.
   * @return a String representation of the specified big Boolean value.
   * @see BooleanUtil#valueOf(Boolean)
   * @see BooleanUtil#toBoolean(boolean)
   * @see BooleanUtil#toInteger(Boolean, int, int)
   */
  public static String toString(final Boolean value, final String trueValue, final String falseValue) {
    return (valueOf(value) ? trueValue : falseValue);
  }

  /**
   * Gets the equivalent primitive boolean value for the specified Boolean wrapper object.  If the Boolean
   * wrapper object is null then this method returns false.
   * @param value the Boolean wrapper object for which to obtain an equivalent boolean primitive value.
   * @return an equivalent boolean primitive value for the Boolean wrapper object.  Returns false if the
   * Boolean wrapper object is null.
   * @see BooleanUtil#valueOf(int)
   * @see BooleanUtil#valueOf(String)
   */
  public static boolean valueOf(final Boolean value) {
    return Boolean.TRUE.equals(value);
  }

  /**
   * Gets a boolean value for the specified integer value by returning false if and only if the integer value is zero.
   * @param value the integer value to be converted as a boolean.
   * @return a boolean value of true for a non-zero number, false for a value of zero.
   * @see BooleanUtil#toBoolean(boolean)
   * @see BooleanUtil#valueOf(Boolean)
   * @see BooleanUtil#valueOf(String)
   */
  public static boolean valueOf(final int value) {
    return (value != 0);
  }

  /**
   * Gets a boolean value for the specified String value by returning true if and only if the String value is one
   * of the following value ["true", "1", "on", "one", "yes"], ignoring case.
   * @param value the String value to be converted as a boolean.
   * @return a boolean value for the specified String value.
   * @see BooleanUtil#toBoolean(boolean)
   * @see BooleanUtil#valueOf(Boolean)
   * @see BooleanUtil#valueOf(int)
   */
  public static boolean valueOf(final String value) {
    return trueStringValueSet.contains(StringUtil.toLowerCase(value));
  }

  /**
   * Performs a logical exclusive or operation on the specified big Boolean values.  The method returns true
   * if and only if one and only one of the big Boolean values are not null and evaluates to true.
   * @param operand1 a Boolean value in the exclusive or operation.
   * @param operand2 a Boolean value in the exclusive or operation.
   * @return a boolean value indicating the result of evaluating the big Booleans using the exclusive or operator.
   * @see BooleanUtil#and(Boolean, Boolean)
   * @see BooleanUtil#or(Boolean, Boolean)
   */
  public static boolean xor(final Boolean operand1, final Boolean operand2) {
    return (valueOf(operand1) != valueOf(operand2));
  }

  /**
   * Determines whether a given array of conditions all evaluate to false.
   * @param conditions an array of boolean values.
   * @return a boolean value of true if and only if all conditional values evaluate to false.
   * @see BooleanUtil#isAllTrue(boolean...) 
   */
  public static boolean isAllFalse(final boolean... conditions) {
    boolean result = false;

    for (final boolean condition : conditions) {
      result |= condition;
    }

    return !result;
  }

  /**
   * Determines whether a given array of conditions all evaluate to true.
   * @param conditions an array of boolean values.
   * @return a boolean value of true if and only if all conditional values evaluate to true.
   * @see BooleanUtil#isAllFalse(boolean...)
   */
  public static boolean isAllTrue(final boolean... conditions) {
    boolean result = true;

    for (final boolean condition : conditions) {
      result &= condition;
    }

    return result;
  }

  /**
   * Determines whether within a given array of conditions any evaluate to false.
   * @param conditions an array of boolean values.
   * @return a boolean value of true if and only if at least one conditional value evaluates to false.
   * @see BooleanUtil#isAnyTrue(boolean...)
   */
  public static boolean isAnyFalse(final boolean... conditions) {
    boolean result = true;

    for (final boolean condition : conditions) {
      result &= condition;
    }

    return !result;
  }

  /**
   * Determines whether within a given array of conditions any evaluate to true.
   * @param conditions an array of boolean values.
   * @return a boolean value of true if and only if at least one conditional value evaluates to true.
   * @see BooleanUtil#isAnyFalse(boolean...)
   */
  public static boolean isAnyTrue(final boolean... conditions) {
    boolean result = false;

    for (final boolean condition : conditions) {
      result |= condition;
    }

    return result;
  }

  /**
   * Determines whether within a given array of conditions one and only one evaluates to false.
   * @param conditions an array of boolean values.
   * @return a boolean value of true if and only if one and only one conditional value evaluates to false.
   * @see BooleanUtil#isExclusivelyTrue(boolean...)
   */
  public static boolean isExclusivelyFalse(final boolean... conditions) {
    boolean result = true;

    for (final boolean condition : conditions) {
      if (!condition && !result) {
        return false;
      }

      result &= condition;
    }

    return !result;
  }

  /**
   * Determines whether within a given array of conditions one and only one evaluates to true.
   * @param conditions an array of boolean values.
   * @return a boolean value of true if and only if one and only one conditional value evaluates to true.
   * @see BooleanUtil#isExclusivelyFalse(boolean...)
   */
  public static boolean isExclusivelyTrue(final boolean... conditions) {
    boolean result = false;

    for (final boolean condition : conditions) {
      if (condition && result) {
        return false;
      }

      result |= condition;
    }

    return result;
  }

}
