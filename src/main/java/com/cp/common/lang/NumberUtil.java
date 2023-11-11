/*
 * NumberUtil.java (c) 13 September 2003
 *
 * Copyright (c) 2003, Codeprimate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.8.29
 * @see java.lang.Number
 * @see com.cp.common.lang.BooleanUtil
 * @see com.cp.common.lang.CharacterUtil
 * @see com.cp.common.lang.ObjectUtil
 * @see com.cp.common.lang.StringUtil
 * @see java.lang.Number
 * @see java.lang.Byte
 * @see java.lang.Short
 * @see java.lang.Integer
 * @see java.lang.Long
 * @see java.lang.Float
 * @see java.lang.Double
 * @see java.lang.String
 * @see java.math.BigDecimal
 * @see java.math.BigInteger
 */

package com.cp.common.lang;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Pattern;

public final class NumberUtil {

  public static final Integer ONE = 1;
  public static final Integer ZERO = 0;

  public static final String BINARY_NUMBER_PATTERN = "(0|1)+";
  public static final String DECIMAL_NUMBER_PATTERN = "-?\\d+(\\.[\\d]+)?";
  public static final String HEXIDECIMAL_NUMBER_PATTERN = "-?(0x|0X|#){1}[0-9a-fA-F]+";
  public static final String OCTAL_NUMBER_PATTERN = "-?0([1-7]+0*)*";

  /**
   * Default private constructor to prevent instantiation of the NumberUtil utility class.
   */
  private NumberUtil() {
  }

  /**
   * Returns the byte primitive value for the corresponding Byte wrapper object if not null, the number zero
   * if the wrapper object is null.
   * @param value the Byte wrapper object
   * @return a byte primitive value for the Byte wrapper object if not null, 0 otherwise.
   * @see java.lang.Byte#byteValue()
   */
  public static byte byteValue(final Byte value) {
    return (ObjectUtil.isNull(value) ? 0 : value);
  }

  /**
   * Returns the short primitive value for the corresponding Short wrapper object if not null, the number zero
   * if the wrapper object is null.
   * @param value the Short wrapper object
   * @return a short primitive value for the Short wrapper object if not null, 0 otherwise.
   * @see java.lang.Short#shortValue()
   */
  public static short shortValue(final Short value) {
    return (ObjectUtil.isNull(value) ? 0 : value);
  }

  /**
   * Returns the int primitive value for the corresponding Integer wrapper object if not null, the number zero
   * if the wrapper object is null.
   * @param value the Integer wrapper object
   * @return a int primitive value for the Integer wrapper object if not null, 0 otherwise.
   * @see java.lang.Integer#intValue()
   */
  public static int intValue(final Integer value) {
    return (ObjectUtil.isNull(value) ? 0 : value);
  }

  /**
   * Returns the long primitive value for the corresponding Long wrapper object if not null, the number zero
   * if the wrapper object is null.
   * @param value the Long wrapper object
   * @return a long primitive value for the Long wrapper object if not null, 0 otherwise.
   * @see java.lang.Long#longValue()
   */
  public static long longValue(final Long value) {
    return (ObjectUtil.isNull(value) ? 0l : value);
  }

  /**
   * Returns the float primitive value for the corresponding Float wrapper object if not null, the number zero
   * if the wrapper object is null.
   * @param value the Float wrapper object
   * @return a float primitive value for the Float wrapper object if not null, 0 otherwise.
   * @see java.lang.Float#floatValue()
   */
  public static float floatValue(final Float value) {
    return (ObjectUtil.isNull(value) ? 0.0f : value);
  }

  /**
   * Returns the double primitive value for the corresponding Double wrapper object if not null, the number zero
   * if the wrapper object is null.
   * @param value the Double wrapper object
   * @return a double primitive value for the Double wrapper object if not null, 0 otherwise.
   * @see java.lang.Double#doubleValue()
   */
  public static double doubleValue(final Double value) {
    return (ObjectUtil.isNull(value) ? 0.0d : value);
  }

  /**
   * Determines the radix (or base) of the number value specified as a String.
   * @param number a String value specifying the number to determine a radix (base) for.
   * @return an integer value specifying the radix (or base) of the specified String number value or a -1 if the
   * radix (or base) of the number cannot be determined.
   */
  static int determineRadix(String number) {
    int radix = (isHexidecimalNumber(number) ? 16 : -1);
    radix = (isDecimalNumber(number) ? 10 : radix);
    radix = (isOctalNumber(number) ? 8 : radix);
    radix = (isBinaryNumber(number) ? 2 : radix);
    return radix;
  }

  /**
   * Parses the String argument specifying a number value into a byte.
   * @param number a String containing a byte number value to be parsed.
   * @return a byte primitive-typed value for the specified String argument containing a number value.
   * @see NumberUtil#determineRadix(String)
   * @see NumberUtil#parseByte(String, int)
   */
  public static byte parseByte(String number) {
    return parseByte(number, determineRadix(number));
  }

  /**
   * Parses the String argument specifying a number value as a signed byte value in the given radix (base).
   * @param number a String containing a byte number value to be parsed into the given radix (base).
   * @param radix an integer value specifying the base of the number represented by the String argument.
   * @return a byte primitive-typed value for the specified String argument containing a number value
   * in the given radix (base).
   * @see NumberUtil#parseByte(String)
   */
  public static byte parseByte(String number, final int radix) {
    return (StringUtil.isNotEmpty(number) ? Byte.parseByte(number.trim(), radix) : 0);
  }

  /**
   * Parses the String argument specifying a number value into a short.
   * @param number a String containing a short number value to be parsed.
   * @return a short primitive-typed value for the specified String argument containing a number value.
   * @see NumberUtil#determineRadix(String)
   * @see NumberUtil#parseShort(String, int)
   */
  public static short parseShort(String number) {
    return parseShort(number, determineRadix(number));
  }

  /**
   * Parses the String argument specifying a number value as a signed short value in the given radix (base).
   * @param number a String containing a short number value to be parsed into the given radix (base).
   * @param radix an integer value specifying the base of the number represented by the String argument.
   * @return a short primitive-typed value for the specified String argument containing a number value
   * in the given radix (base).
   * @see NumberUtil#parseShort(String)
   */
  public static short parseShort(String number, final int radix) {
    return (StringUtil.isNotEmpty(number) ? Short.parseShort(number.trim(), radix) : 0);
  }

  /**
   * Parses the String argument specifying a number value into an integer.
   * @param number a String containing an integer number value to be parsed.
   * @return an integer primitive-typed value for the specified String argument containing a number value.
   * @see NumberUtil#determineRadix(String)
   * @see NumberUtil#parseInt(String, int)
   */
  public static int parseInt(String number) {
    return parseInt(number, determineRadix(number));
  }

  /**
   * Parses the String argument specifying a number value as a signed integer value in the given radix (base).
   * @param number a String containing an integer number value to be parsed into the given radix (base).
   * @param radix an integer value specifying the base of the number represented by the String argument.
   * @return an integer primitive-typed value for the specified String argument containing a number value
   * in the given radix (base).
   * @see NumberUtil#parseInt(String)
   */
  public static int parseInt(String number, final int radix) {
    return (StringUtil.isNotEmpty(number) ? Integer.parseInt(number.trim(), radix) : 0);
  }

  /**
   * Parses the String argument specifying a number value into a long.
   * @param number a String containing a long number value to be parsed.
   * @return a long primitive-typed value for the specified String argument containing a number value.
   * @see NumberUtil#determineRadix(String)
   * @see NumberUtil#parseLong(String, int)
   */
  public static long parseLong(String number) {
    return parseLong(number, determineRadix(number));
  }

  /**
   * Parses the String argument specifying a number value as a signed long value in the given radix (base).
   * @param number a String containing a long number value to be parsed into the given radix (base).
   * @param radix an integer value specifying the base of the number represented by the String argument.
   * @return a long primitive-typed value for the specified String argument containing a number value
   * in the given radix (base).
   * @see NumberUtil#parseLong(String)
   */
  public static long parseLong(String number, final int radix) {
    return (StringUtil.isNotEmpty(number) ? Long.parseLong(number.trim(), radix) : 0l);
  }

  /**
   * Parses the String argument specifying a number value into a float.
   * @param number a String containing a float number value to be parsed.
   * @return a float primitive-typed value for the specified String argument containing a number value.
   */
  public static float parseFloat(final String number) {
    return (StringUtil.isNotEmpty(number) ? Float.parseFloat(number.trim()) : 0.0f);
  }

  /**
   * Parses the String argument specifying a number value into a double.
   * @param number a String containing a double number value to be parsed.
   * @return a double primitive-typed value for the specified String argument containing a number value.
   */
  public static double parseDouble(final String number) {
    return (StringUtil.isNotEmpty(number) ? Double.parseDouble(number.trim()) : 0.0d);
  }

  /**
   * Decodes the specified String argument containing a number value into a BigInteger value.
   * @param number a String containing a number value to be parsed as a BigInteger.
   * @return a BigInteger value for the specified String argument containing a numeric value.
   * @throws IllegalArgumentException if the specified String argument is null, empty or is just whitespace characters.
   */
  static Number decodeBigInteger(String number) {
    Assert.notEmpty(number, "A number must be specified!");

    final boolean negative = number.startsWith("-");

    int index = (negative ? 1 : 0); // determine sign
    int radix = determineRadix(number); // determine base

    // treat a String of ones and zeros as a decimal number, not a binary number
    radix = (radix == 2 ? 10 : radix);
    index += (radix == 16 ? (number.substring(index).startsWith("#") ? 1 : 2) : (radix == 8 ? 1 : 0));

    final BigInteger value = new BigInteger(number.substring(index), radix);

    return (negative ? value.negate() : value);
  }

  /**
   * Returns the value of the String argument containing a number value as an subclass instance of the Number class.
   * @param number the String value containing the number to be parsed an wrapped in a Number subclass instance
   * (such as Byte, Short, Integer, Long, Float, Double, etc).
   * @param numberClass the target subclass instance of Number to return the value as.
   * @return a Number subclass wrapper object for the String containing the number value.
   * @see NumberUtil#decodeBigInteger(String)
   */
  public static Number valueOf(String number, final Class<? extends Number> numberClass) {
    Assert.notNull(numberClass, "The type of number cannot be null!");

    if (StringUtil.isNotEmpty(number)) {
      number = number.trim();

      if (numberClass.equals(Byte.class)) {
        return (isHexidecimalNumber(number) || isOctalNumber(number)) ? Byte.decode(number) : Byte.valueOf(number);
      }
      else if (numberClass.equals(Short.class)) {
        return (isHexidecimalNumber(number) || isOctalNumber(number)) ? Short.decode(number) : Short.valueOf(number);
      }
      else if (numberClass.equals(Integer.class)) {
        return (isHexidecimalNumber(number) || isOctalNumber(number)) ? Integer.decode(number) : Integer.valueOf(number);
      }
      else if (numberClass.equals(Long.class)) {
        return (isHexidecimalNumber(number) || isOctalNumber(number)) ? Long.decode(number) : Long.valueOf(number);
      }
      else if (numberClass.equals(Float.class)) {
        return Float.valueOf(number);
      }
      else if (numberClass.equals(Double.class)) {
        return Double.valueOf(number);
      }
      else if (numberClass.equals(BigDecimal.class)) {
        return new BigDecimal(number);
      }
      else if (numberClass.equals(BigInteger.class)) {
        return decodeBigInteger(number);
      }
      else {
        throw new IllegalArgumentException("The specified number class (" + numberClass.getName() + "is unsupported!");
      }
    }

    return null;
  }

  /**
   * Determines whether the specified number represented by the String value is a binary number.
   * @param number a String value specifying the number.
   * @return a boolean value indicating whether the number represented by the String value is a binary number.
   * @see NumberUtil#isDecimalNumber(String)
   * @see NumberUtil#isHexidecimalNumber(String)
   * @see NumberUtil#isOctalNumber(String)
   */
  public static boolean isBinaryNumber(final String number) {
    return (StringUtil.isNotEmpty(number) && Pattern.compile(BINARY_NUMBER_PATTERN).matcher(number.trim()).matches());
  }

  /**
   * Determines whether the specified number represented by the String value is a decimal number.
   * @param number a String value specifying the number.
   * @return a boolean value indicating whether the number represented by the String value is a decimal number.
   * @see NumberUtil#isBinaryNumber(String)
   * @see NumberUtil#isHexidecimalNumber(String)
   * @see NumberUtil#isOctalNumber(String)
   */
  public static boolean isDecimalNumber(final String number) {
    return (StringUtil.isNotEmpty(number) && Pattern.compile(DECIMAL_NUMBER_PATTERN).matcher(number.trim()).matches());
  }

  /**
   * Determines whether the actual numerical value is equal in value to the expected numerical value.
   * @param expectedValue the expected numerical value in the equality comparison.
   * @param actualValue the actual numerical value in the equality comparison.
   * @return a boolean value indicating whether the actual numerical value is equal in value to the expected
   * numerical value.
   */
  public static boolean isEqualTo(final double expectedValue, final double actualValue) {
    return (expectedValue == actualValue);
  }

  /**
   * Determines whether the actual numerical value is equal in value to the expected numerical value.
   * @param expectedValue the expected numerical value in the equality comparison.
   * @param actualValue the actual numerical value in the equality comparison.
   * @return a boolean value indicating whether the actual numerical value is equal in value to the expected
   * numerical value.
   */
  public static boolean isEqualTo(final int expectedValue, final int actualValue) {
    return (expectedValue == actualValue);
  }

  /**
   * Determines whether the given integer value is an even number.
   * @param value an integer value.
   * @return a boolean value indicating whether or not the specified integer value is an even number.
   * @see NumberUtil#isOdd(int)
   */
  public static boolean isEven(final int value) {
    return (Math.abs(value) % 2 == 0);
  }

  /**
   * Determines whether the actual numerical value is greater than the expected numerical value.
   * @param expectedValue the expected numerical value used as the lower bound in determining whether the
   * actual numerical value will be greater.
   * @param actualValue the actual numerical value being evaluated in the greater than comparison.
   * @return a boolean value indicating whether the actual numerical value is greater than the expected numerical value.
   */
  public static boolean isGreaterThan(final double expectedValue, final double actualValue) {
    return (actualValue > expectedValue);
  }

  /**
   * Determines whether the actual numerical value is greater than the expected numerical value.
   * @param expectedValue the expected numerical value used as the lower bound in determining whether the
   * actual numerical value will be greater.
   * @param actualValue the actual numerical value being evaluated in the greater than comparison.
   * @return a boolean value indicating whether the actual numerical value is greater than the expected numerical value.
   */
  public static boolean isGreaterThan(final int expectedValue, final int actualValue) {
    return (actualValue > expectedValue);
  }

  /**
   * Determines whether the actual numerical value is greater than the expected low numerical value and less than the
   * expected high numercial value
   * @param expectedLowValue the expected low numerical value used as the lower bound in determining whether the
   * actual numerical value will be greater.
   * @param expectedHighValue the expected high numerical value used as the upper bound in determining whether the
   * actual numerical value will be less.
   * @param actualValue the actual numerical value being evaluated in the greater than and less than comparison.
   * @return a boolean value indicating whether the actual numerical value is greater than the expected low
   * numerical value and less than the expected high numerical value.
   */
  public static boolean isGreaterThanAndLessThan(final double expectedLowValue,
                                                 final double expectedHighValue,
                                                 final double actualValue)
  {
    return (actualValue > expectedLowValue && actualValue < expectedHighValue);
  }

  /**
   * Determines whether the actual numerical value is greater than the expected low numerical value and less than the
   * expected high numercial value
   * @param expectedLowValue the expected low numerical value used as the lower bound in determining whether the
   * actual numerical value will be greater.
   * @param expectedHighValue the expected high numerical value used as the upper bound in determining whether the
   * actual numerical value will be less.
   * @param actualValue the actual numerical value being evaluated in the greater than and less than comparison.
   * @return a boolean value indicating whether the actual numerical value is greater than the expected low
   * numerical value and less than the expected high numerical value.
   */
  public static boolean isGreaterThanAndLessThan(final int expectedLowValue,
                                                 final int expectedHighValue,
                                                 final int actualValue)
  {
    return (actualValue > expectedLowValue && actualValue < expectedHighValue);
  }

  /**
   * Determines whether the actual numerical value is greater than the expected low numerical value and less than
   * equal to the expected high numercial value
   * @param expectedLowValue the expected low numerical value used as the lower bound in determining whether the
   * actual numerical value will be greater.
   * @param expectedHighValue the expected high numerical value used as the upper bound in determining whether the
   * actual numerical value will be less or equal.
   * @param actualValue the actual numerical value being evaluated in the greater than and less than equal to
   * comparison.
   * @return a boolean value indicating whether the actual numerical value is greater than the expected low
   * numerical value and less than equal to the expected high numerical value.
   */
  public static boolean isGreaterThanAndLessThanEqualTo(final double expectedLowValue,
                                                        final double expectedHighValue,
                                                        final double actualValue)
  {
    return (actualValue > expectedLowValue && actualValue <= expectedHighValue);
  }

  /**
   * Determines whether the actual numerical value is greater than the expected low numerical value and less than
   * equal to the expected high numercial value
   * @param expectedLowValue the expected low numerical value used as the lower bound in determining whether the
   * actual numerical value will be greater.
   * @param expectedHighValue the expected high numerical value used as the upper bound in determining whether the
   * actual numerical value will be less or equal.
   * @param actualValue the actual numerical value being evaluated in the greater than and less than equal to
   * comparison.
   * @return a boolean value indicating whether the actual numerical value is greater than the expected low
   * numerical value and less than equal to the expected high numerical value.
   */
  public static boolean isGreaterThanAndLessThanEqualTo(final int expectedLowValue,
                                                        final int expectedHighValue,
                                                        final int actualValue)
  {
    return (actualValue > expectedLowValue && actualValue <= expectedHighValue);
  }

  /**
   * Determines whether the actual numerical value is greater than equal to the expected numerical value.
   * @param expectedValue the expected numerical value used as the lower bound in determining whether the
   * actual numerical value will be greater or equal.
   * @param actualValue the actual numerical value being evaluated in the greater than equal to comparison.
   * @return a boolean value indicating whether the actual numerical value is greater than equal to the
   * expected numerical value.
   */
  public static boolean isGreaterThanEqualTo(final double expectedValue, final double actualValue) {
    return (actualValue >= expectedValue);
  }

  /**
   * Determines whether the actual numerical value is greater than equal to the expected numerical value.
   * @param expectedValue the expected numerical value used as the lower bound in determining whether the
   * actual numerical value will be greater or equal.
   * @param actualValue the actual numerical value being evaluated in the greater than equal to comparison.
   * @return a boolean value indicating whether the actual numerical value is greater than equal to the
   * expected numerical value.
   */
  public static boolean isGreaterThanEqualTo(final int expectedValue, final int actualValue) {
    return (actualValue >= expectedValue);
  }

  /**
   * Determines whether the actual numerical value is greater than equal to the expected low numerical value
   * and less than the expected high numercial value
   * @param expectedLowValue the expected low numerical value used as the lower bound in determining whether the
   * actual numerical value will be greater or equal.
   * @param expectedHighValue the expected high numerical value used as the upper bound in determining whether the
   * actual numerical value will be less.
   * @param actualValue the actual numerical value being evaluated in the greater than equal to and less than
   * comparison.
   * @return a boolean value indicating whether the actual numerical value is greater than equal to the expected low
   * numerical value and less than the expected high numerical value.
   */
  public static boolean isGreaterThanEqualToAndLessThan(final double expectedLowValue,
                                                        final double expectedHighValue,
                                                        final double actualValue)
  {
    return (actualValue >= expectedLowValue && actualValue < expectedHighValue);
  }

  /**
   * Determines whether the actual numerical value is greater than equal to the expected low numerical value
   * and less than the expected high numercial value
   * @param expectedLowValue the expected low numerical value used as the lower bound in determining whether the
   * actual numerical value will be greater or equal.
   * @param expectedHighValue the expected high numerical value used as the upper bound in determining whether the
   * actual numerical value will be less.
   * @param actualValue the actual numerical value being evaluated in the greater than equal to and less than
   * comparison.
   * @return a boolean value indicating whether the actual numerical value is greater than equal to the expected low
   * numerical value and less than the expected high numerical value.
   */
  public static boolean isGreaterThanEqualToAndLessThan(final int expectedLowValue,
                                                        final int expectedHighValue,
                                                        final int actualValue)
  {
    return (actualValue >= expectedLowValue && actualValue < expectedHighValue);
  }

  /**
   * Determines whether the actual numerical value is greater than equal to the expected low numerical value
   * and less than equal to the expected high numercial value
   * @param expectedLowValue the expected low numerical value used as the lower bound in determining whether the
   * actual numerical value will be greater or equal.
   * @param expectedHighValue the expected high numerical value used as the upper bound in determining whether the
   * actual numerical value will be less or equal.
   * @param actualValue the actual numerical value being evaluated in the greater than equal to and less than equal to
   * comparison.
   * @return a boolean value indicating whether the actual numerical value is greater than equal to the expected low
   * numerical value and less than equal to the expected high numerical value.
   */
  public static boolean isGreaterThanEqualToAndLessThanEqualTo(final double expectedLowValue,
                                                               final double expectedHighValue,
                                                               final double actualValue)
  {
    return (actualValue >= expectedLowValue && actualValue <= expectedHighValue);
  }

  /**
   * Determines whether the actual numerical value is greater than equal to the expected low numerical value
   * and less than equal to the expected high numercial value
   * @param expectedLowValue the expected low numerical value used as the lower bound in determining whether the
   * actual numerical value will be greater or equal.
   * @param expectedHighValue the expected high numerical value used as the upper bound in determining whether the
   * actual numerical value will be less or equal.
   * @param actualValue the actual numerical value being evaluated in the greater than equal to and less than equal to
   * comparison.
   * @return a boolean value indicating whether the actual numerical value is greater than equal to the expected low
   * numerical value and less than equal to the expected high numerical value.
   */
  public static boolean isGreaterThanEqualToAndLessThanEqualTo(final int expectedLowValue,
                                                               final int expectedHighValue,
                                                               final int actualValue)
  {
    return (actualValue >= expectedLowValue && actualValue <= expectedHighValue);
  }

  /**
   * Determines whether the specified number represented by the String value is a hexidecimal number.
   * @param number a String value specifying the number.
   * @return a boolean value indicating whether the number represented by the String value is a hexidecimal number.
   * @see NumberUtil#isBinaryNumber(String)
   * @see NumberUtil#isDecimalNumber(String)
   * @see NumberUtil#isOctalNumber(String)
   */
  public static boolean isHexidecimalNumber(final String number) {
    return (StringUtil.isNotEmpty(number) && Pattern.compile(HEXIDECIMAL_NUMBER_PATTERN).matcher(
      number.trim()).matches());
  }

  /**
   * Determines whether the actual numerical value is less than the expected numerical value.
   * @param expectedValue the expected numerical value used as the upper bound in determining whether the
   * actual numerical value will be less.
   * @param actualValue the actual numerical value being evaluated in the less than comparison.
   * @return a boolean value indicating whether the actual numerical value is less than the expected numerical value.
   */
  public static boolean isLessThan(final double expectedValue, final double actualValue) {
    return (actualValue < expectedValue);
  }

  /**
   * Determines whether the actual numerical value is less than the expected numerical value.
   * @param expectedValue the expected numerical value used as the upper bound in determining whether the
   * actual numerical value will be less.
   * @param actualValue the actual numerical value being evaluated in the less than comparison.
   * @return a boolean value indicating whether the actual numerical value is less than the expected numerical value.
   */
  public static boolean isLessThan(final int expectedValue, final int actualValue) {
    return (actualValue < expectedValue);
  }

  /**
   * Determines whether the actual numerical value is less than the expected high numerical value or greater than
   * the expected low numerical value.
   * @param expectedHighValue the expected high numerical value used as the upper bound in determining whether the
   * actual numerical value will be less.
   * @param expectedLowValue the expected low numerical value used as the lower bound in determining whether the
   * actual numerical value will be greater.
   * @param actualValue the actual numerical value being evaluated in the less than or greater than comparison.
   * @return a boolean value indicating whether the actual numerical value is less than the expected high
   * numerical value or greater than the expected low numerical value. 
   */
  public static boolean isLessThanOrGreaterThan(final double expectedHighValue,
                                                final double expectedLowValue,
                                                final double actualValue)
  {
    return (actualValue < expectedHighValue || actualValue > expectedLowValue);
  }

  /**
   * Determines whether the actual numerical value is less than the expected high numerical value or greater than
   * the expected low numerical value.
   * @param expectedHighValue the expected high numerical value used as the upper bound in determining whether the
   * actual numerical value will be less.
   * @param expectedLowValue the expected low numerical value used as the lower bound in determining whether the
   * actual numerical value will be greater.
   * @param actualValue the actual numerical value being evaluated in the less than or greater than comparison.
   * @return a boolean value indicating whether the actual numerical value is less than the expected high
   * numerical value or greater than the expected low numerical value.
   */
  public static boolean isLessThanOrGreaterThan(final int expectedHighValue,
                                                final int expectedLowValue,
                                                final int actualValue)
  {
    return (actualValue < expectedHighValue || actualValue > expectedLowValue);
  }

  /**
   * Determines whether the actual numerical value is less than the expected high numerical value or greater than
   * equal to the expected low numerical value.
   * @param expectedHighValue the expected high numerical value used as the upper bound in determining whether the
   * actual numerical value will be less.
   * @param expectedLowValue the expected low numerical value used as the lower bound in determining whether the
   * actual numerical value will be greater or equal.
   * @param actualValue the actual numerical value being evaluated in the less than or greater than equal to comparison.
   * @return a boolean value indicating whether the actual numerical value is less than the expected high
   * numerical value or greater than equal to the expected low numerical value.
   */
  public static boolean isLessThanOrGreaterThanEqualTo(final double expectedHighValue,
                                                       final double expectedLowValue,
                                                       final double actualValue)
  {
    return (actualValue < expectedHighValue || actualValue >= expectedLowValue);
  }

  /**
   * Determines whether the actual numerical value is less than the expected high numerical value or greater than
   * equal to the expected low numerical value.
   * @param expectedHighValue the expected high numerical value used as the upper bound in determining whether the
   * actual numerical value will be less.
   * @param expectedLowValue the expected low numerical value used as the lower bound in determining whether the
   * actual numerical value will be greater or equal.
   * @param actualValue the actual numerical value being evaluated in the less than or greater than equal to comparison.
   * @return a boolean value indicating whether the actual numerical value is less than the expected high
   * numerical value or greater than equal to the expected low numerical value.
   */
  public static boolean isLessThanOrGreaterThanEqualTo(final int expectedHighValue,
                                                       final int expectedLowValue,
                                                       final int actualValue)
  {
    return (actualValue < expectedHighValue || actualValue >= expectedLowValue);
  }

  /**
   * Determines whether the actual numerical value is less equal to than the expected numerical value.
   * @param expectedValue the expected numerical value used as the upper bound in determining whether the
   * actual numerical value will be less or equal.
   * @param actualValue the actual numerical value being evaluated in the less than equal to comparison.
   * @return a boolean value indicating whether the actual numerical value is less than equal to the
   * expected numerical value.
   */
  public static boolean isLessThanEqualTo(final double expectedValue, final double actualValue) {
    return (actualValue <= expectedValue);
  }

  /**
   * Determines whether the actual numerical value is less equal to than the expected numerical value.
   * @param expectedValue the expected numerical value used as the upper bound in determining whether the
   * actual numerical value will be less or equal.
   * @param actualValue the actual numerical value being evaluated in the less than equal to comparison.
   * @return a boolean value indicating whether the actual numerical value is less than equal to the
   * expected numerical value.
   */
  public static boolean isLessThanEqualTo(final int expectedValue, final int actualValue) {
    return (actualValue <= expectedValue);
  }

  /**
   * Determines whether the actual numerical value is less than equal to the expected high numerical value
   * or greater than the expected low numerical value.
   * @param expectedHighValue the expected high numerical value used as the upper bound in determining whether the
   * actual numerical value will be less or equal.
   * @param expectedLowValue the expected low numerical value used as the lower bound in determining whether the
   * actual numerical value will be greater.
   * @param actualValue the actual numerical value being evaluated in the less than equal to or greater than comparison.
   * @return a boolean value indicating whether the actual numerical value is less than equal to the expected high
   * numerical value or greater than the expected low numerical value.
   */
  public static boolean isLessThanEqualToOrGreaterThan(final double expectedHighValue,
                                                       final double expectedLowValue,
                                                       final double actualValue)
  {
    return (actualValue <= expectedHighValue || actualValue > expectedLowValue);
  }

  /**
   * Determines whether the actual numerical value is less than equal to the expected high numerical value
   * or greater than the expected low numerical value.
   * @param expectedHighValue the expected high numerical value used as the upper bound in determining whether the
   * actual numerical value will be less or equal.
   * @param expectedLowValue the expected low numerical value used as the lower bound in determining whether the
   * actual numerical value will be greater.
   * @param actualValue the actual numerical value being evaluated in the less than equal to or greater than comparison.
   * @return a boolean value indicating whether the actual numerical value is less than equal to the expected high
   * numerical value or greater than the expected low numerical value.
   */
  public static boolean isLessThanEqualToOrGreaterThan(final int expectedHighValue,
                                                       final int expectedLowValue,
                                                       final int actualValue)
  {
    return (actualValue <= expectedHighValue || actualValue > expectedLowValue);
  }

  /**
   * Determines whether the actual numerical value is less than equal to the expected high numerical value
   * or greater than equal to the expected low numerical value.
   * @param expectedHighValue the expected high numerical value used as the upper bound in determining whether the
   * actual numerical value will be less or equal.
   * @param expectedLowValue the expected low numerical value used as the lower bound in determining whether the
   * actual numerical value will be greater or equal.
   * @param actualValue the actual numerical value being evaluated in the less than equal to or greater than equal to
   * comparison.
   * @return a boolean value indicating whether the actual numerical value is less than equal to the expected high
   * numerical value or greater than equal to the expected low numerical value.
   */
  public static boolean isLessThanEqualToOrGreaterThanEqualTo(final double expectedHighValue,
                                                              final double expectedLowValue,
                                                              final double actualValue)
  {
    return (actualValue <= expectedHighValue || actualValue >= expectedLowValue);
  }

  /**
   * Determines whether the actual numerical value is less than equal to the expected high numerical value
   * or greater than equal to the expected low numerical value.
   * @param expectedHighValue the expected high numerical value used as the upper bound in determining whether the
   * actual numerical value will be less or equal.
   * @param expectedLowValue the expected low numerical value used as the lower bound in determining whether the
   * actual numerical value will be greater or equal.
   * @param actualValue the actual numerical value being evaluated in the less than equal to or greater than equal to
   * comparison.
   * @return a boolean value indicating whether the actual numerical value is less than equal to the expected high
   * numerical value or greater than equal to the expected low numerical value.
   */
  public static boolean isLessThanEqualToOrGreaterThanEqualTo(final int expectedHighValue,
                                                              final int expectedLowValue,
                                                              final int actualValue)
  {
    return (actualValue <= expectedHighValue || actualValue >= expectedLowValue);
  }

  /**
   * Determines whether the specified floating-point value is less than zero.
   * @param value a floating-point value used in determining it's sign.
   * @return a boolean value indicating whether the specified numerical value
   * is negative or not.
   */
  public static boolean isNegative(final double value) {
    return (value < 0.0);
  }

  /**
   * Determines whether the specified integer value is less than zero.
   * @param value a integer value used in determining it's sign.
   * @return a boolean value indicating whether the specified numerical value
   * is negative or not.
   */
  public static boolean isNegative(final int value) {
    return (value < 0);
  }

  /**
   * Determines whether the specified number represented by the String value is an octal number.
   * @param number a String value specifying the number.
   * @return a boolean value indicating whether the number represented by the String value is an octal number.
   * @see NumberUtil#isBinaryNumber(String)
   * @see NumberUtil#isDecimalNumber(String)
   * @see NumberUtil#isHexidecimalNumber(String) 
   */
  public static boolean isOctalNumber(final String number) {
    return (StringUtil.isNotEmpty(number) && Pattern.compile(OCTAL_NUMBER_PATTERN).matcher(number.trim()).matches());
  }

  /**
   * Determines whether the given integer value is an odd number.
   * @param value an integer value.
   * @return a boolean value indicating whether or not the specified integer value is an odd number.
   * @see NumberUtil#isEven(int)
   */
  public static boolean isOdd(final int value) {
    return (Math.abs(value) % 2 == 1);
  }

  /**
   * Determines whether the specified floating-point value is greater than zero.
   * @param value a floating-point value used in determining it's sign.
   * @return a boolean value indicating whether the specified numerical value
   * is positive or not.
   */
  public static boolean isPositive(final double value) {
    return (value > 0.0);
  }

  /**
   * Determines whether the specified integer value is greater than zero.
   * @param value a integer value used in determining it's sign.
   * @return a boolean value indicating whether the specified numerical value
   * is positive or not.
   */
  public static boolean isPositive(final int value) {
    return (value > 0);
  }

  /**
   * A prime number is an integer greater than 1 which has no factors besides 1 and itself. (For example,
   * 5 is divisible evenly by only 1 and 5, therefore it is prime).
   * The prime number algorithm implemented in this method is based on the following link...
   * http://www.lessonplanspage.com/MathPrimeVsCompositeNumbers7HS.htm
   * @param value the long value being tested as a prime number.
   * @return a boolean value indicating whether the long value is prime.
   */
  public static boolean isPrime(final long value) {
    if (value > 1) {
      // common prime numbers
      switch ((int) value) {
        case 2:
        case 3:
        case 5:
        case 7:
          return true;
      }

      final double squareRootOfValue = Math.sqrt(value);

      if (isWhole(squareRootOfValue)) {
        return false;
      }

      final String stringValue = String.valueOf(value);

      if (stringValue.endsWith("5")) {
        return false;
      }

      int sumOfDigitsInValue = 0;

      for (final char digit : stringValue.toCharArray()) {
        sumOfDigitsInValue += Integer.parseInt(String.valueOf(digit));
      }

      if (sumOfDigitsInValue % 3 == 0) {
        return false;
      }

      for (int divisor = 2; divisor < squareRootOfValue; divisor++) {
        if (value != divisor && value % divisor == 0) {
          return false;
        }
      }

      // the long value is prime!
      return true;
    }

    return false;
  }

  /**
   * Determines whether the specified double value is a whole number.
   * @param value a double value being testing as a whole number.
   * @return a boolean value indicating whether the double value is a whole number.
   */
  public static boolean isWhole(final double value) {
    return (Math.abs(value) == Math.floor(Math.abs(value)));
  }

  /**
   * Determines whether the specified floating-point value is zero.
   * @param value a floating-point value used in determining if it has no value.
   * @return a boolean value indicating whether the specified numerical value is zero.
   */
  public static boolean isZero(final double value) {
    return (value == 0.0);
  }

  /**
   * Determines whether the specified integer value is zero.
   * @param value a integer value used in determining if it has no value.
   * @return a boolean value indicating whether the specified numerical value is zero.
   */
  public static boolean isZero(final int value) {
    return (value == 0);
  }

}
