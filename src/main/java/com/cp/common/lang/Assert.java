/*
 * Assert.java (c) 13 December 2006
 *
 * The Assert class is a suitable replacement for the assert facility in the Java Programming Language.  It provides
 * class methods for asserting various conditions and state such as equality, identity and relational comparisons.
 * In addition, the caller can assert certain properties of Objects and values such as blankness, empty, containment,
 * and so on.
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.10
 * @see com.cp.common.lang.NumberUtil
 * @see com.cp.common.lang.ObjectUtil
 * @see com.cp.common.lang.StringUtil
 * @see com.cp.common.util.ArrayUtil
 * @see com.cp.common.util.CollectionUtil
 */

package com.cp.common.lang;

import com.cp.common.util.ArrayUtil;
import com.cp.common.util.CollectionUtil;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Assert {

  private static final Log log = LogFactory.getLog(Assert.class);

  /**
   * Asserts that the String argument is blank, which is either a null String, empty String or a String
   * containing only whitespace.
   * @param value the String argument asserted for blankness.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the String argument is not blank.
   * @see StringUtil#isNotBlank(String)
   */
  public static void blank(final String value, final String message) {
    if (StringUtil.isNotBlank(value)) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts that the String argument contains digits (numerical characters) only.
   * @param value the String argument asserted for digits only.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the String argument contains non-digits characters.
   * @see StringUtil#isDigitsOnly(String)
   */
  public static void digitsOnly(final String value, final String message) {
    if (!StringUtil.isDigitsOnly(value)) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the Object array is empty or null, or will throw an IllegalArgumentException if the assertion fails.
   * @param array the Object array being asserted as empty.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the Object array argument is not empty.
   * @see ArrayUtil#isNotEmpty(Object[])
   */
  public static void empty(final Object[] array, final String message) {
    if (ArrayUtil.isNotEmpty(array)) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the Collection is empty or null, or will throw an IllegalArgumentException if the assertion fails.
   * @param collection the Collection being asserted as empty.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the Collection argument is not empty.
   * @see CollectionUtil#isNotEmpty(java.util.Collection)
   */
  public static void empty(final Collection collection, final String message) {
    if (CollectionUtil.isNotEmpty(collection)) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the Map is empty or null, or will throw an IllegalArgumentException if the assertion fails.
   * @param map the Map being asserted for emptiness.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the Map argument is not empty.
   * @see CollectionUtil#isNotEmpty(java.util.Map)
   */
  public static void empty(final Map map, final String message) {
    if (CollectionUtil.isNotEmpty(map)) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the String argument is the empty String
   * @param value the String value asserted as the empty String.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the String argument is not the empty String.
   * @see StringUtil#isNotEmpty(String)
   */
  public static void empty(final String value, final String message) {
    if (StringUtil.isNotEmpty(value)) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts two Objects are equal or throw an IllegalArgumentException with message if the assertion fails.
   * @param obj1 an Object argument in the equality comparison.
   * @param obj2 an Object argument in the equality comparison.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the Object arguments are not equal.
   * @see ObjectUtil#equals(Object, Object) 
   */
  public static void equals(final Object obj1, final Object obj2, final String message) {
    if (!ObjectUtil.equals(obj1, obj2)) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the actual value is greater than the minimum value or throws an IllegalArgumentException with message
   * if the assertion fails.
   * @param value the Comparable value expected to be greater than the minimum value
   * @param min the minimum, exclusive value expected for the actual value.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the assertion fails and the actual value is less than
   * or equal to the minimum value.
   */
  public static <C extends Comparable<C>> void greaterThan(final C value, final C min, final String message) {
    if (value.compareTo(min) <= 0) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the actual value is greater than the minimum value and less than the maximum value or throws
   * an IllegalArgumentException with message if the assertion fails.
   * @param value the Comparable value expected to be greater than the minimum value and less than the maximum value.
   * @param min the minimum, exclusive value expected for the actual value.
   * @param max the maximum, exclusive value expected for the actual value.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the assertion fails and the actual value is less than
   * or equal to the minimum value or greater than equal to the maximum value.
   */
  public static <C extends Comparable<C>> void greaterThanAndLessThan(final C value,
                                                                      final C min,
                                                                      final C max,
                                                                      final String message)
  {
    if (value.compareTo(min) <= 0 || value.compareTo(max) >= 0) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the actual value is greater than the minimum value and less than or equal to the maximum value
   * or throws an IllegalArgumentException with message if the assertion fails.
   * @param value the Comparable value expected to be greater than the minimum value and less than or equal to
   * the maximum value.
   * @param min the minimum, exclusive value expected for the actual value.
   * @param max the maximum, inclusive value expected for the actual value.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the assertion fails and the actual value is less than
   * or equal to the minimum value or greater than equal to the maximum value.
   */
  public static <C extends Comparable<C>> void greaterThanAndLessThanEqual(final C value,
                                                                           final C min,
                                                                           final C max,
                                                                           final String message)
  {
    if (value.compareTo(min) <= 0 || value.compareTo(max) > 0) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the actual value is greater than or equal to the minimum value or throws an IllegalArgumentException
   * with message if the assertion fails.
   * @param value the Comparable value expected to greater than or equal to the minimum value.
   * @param min the minimum, inclusive value expected for the actual value.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the assertion fails and the greaterValue value is less than
   * the lesserValue value.
   */
  public static <C extends Comparable<C>> void greaterThanEqual(final C value, final C min, final String message) {
    if (value.compareTo(min) < 0) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the actual value is greater than or equal to the minimum value and less than the maximum value or throws
   * an IllegalArgumentException with message if the assertion fails.
   * @param value the Comparable value expected to greater than or equal to the minimum value and less than
   * maximum value.
   * @param min the minimum, inclusive value expected for the actual value.
   * @param max the maximum, exclusive value expected for the actual value.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the assertion fails and the actual value is less than
   * the minimum value or greater than equal to the maximum value.
   */
  public static <C extends Comparable<C>> void greaterThanEqualAndLessThan(final C value,
                                                                           final C min,
                                                                           final C max,
                                                                           final String message)
  {
    if (value.compareTo(min) < 0 || value.compareTo(max) >= 0) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the actual value is greater than or equal to the minimum value and less than or equal to the maximum value
   * or throws an IllegalArgumentException with message if the assertion fails.
   * @param value the Comparable value expected to greater than or equal to the minimum value and less than or equal to
   * the maximum value.
   * @param min the minimum, inclusive value expected for the actual value.
   * @param max the maximum, inclusive value expected for the actual value.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the assertion fails and the actual value is less than
   * the minimum value or greater than the maximum value.
   */
  public static <C extends Comparable<C>> void greaterThanEqualAndLessThanEqual(final C value,
                                                                                final C min,
                                                                                final C max,
                                                                                final String message)
  {
    if (value.compareTo(min) < 0 || value.compareTo(max) > 0) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the actual Class type is an instance of the expected Class type or throws an IllegalArgumentException
   * with message if the assertion fails.
   * @param actual the actual Class type.
   * @param expected the expected Class type.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the actual Class type is not an instance of
   * the expected Class type.
   * @see ObjectUtil#isNull(Object)
   */
  public static void isAssignableFrom(final Class actual, final Class expected, final String message) {
    if (ObjectUtil.isNull(actual) || !expected.isAssignableFrom(actual)) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the condition is false or throws an IllegalArgumentException with message if the assertion fails.
   * @param condition a boolean value expected to be false.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the condition is true.
   */
  public static void isFalse(final boolean condition, final String message) {
    if (condition) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the Class type of the Object value is an instance of the expected Class type, or throws an
   * IllegalArgumentException with message if the assertion fails.
   * @param value the Object value who's Class type is expected to be an instance of the expected Class type.
   * @param expected the expected Class type.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the Object value's Class type is not an instance of
   * the expected Class type.
   * @see ObjectUtil#isNull(Object)
   */
  public static void isInstanceOf(final Object value, final Class expected, final String message) {
    if (ObjectUtil.isNull(value) || !expected.isInstance(value)) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the Object argument is null or throws an IllegalArgumentException with message if the assertion fails.
   * @param obj the Object argument expected to be null.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the Object argument is not null.
   * @see ObjectUtil#isNotNull(Object) 
   */
  public static void isNull(final Object obj, final String message) {
    if (ObjectUtil.isNotNull(obj)) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the condition is true or throws an IllegalArgumentException with message if the assertion fails.
   * @param condition a boolean value expected to be true.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the condition is false.
   */
  public static void isTrue(final boolean condition, final String message) {
    if (!condition) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the actual value is less than the maximum value or throws an IllegalArgumentException with message
   * if the assertion fails.
   * @param value the actual Comparable value expected to less than the maximum value.
   * @param max the maximum, exclusive value expected for the actual value.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the assertion fails and the actual value is greater than
   * or equal to the maximum value.
   */
  public static <C extends Comparable<C>> void lessThan(final C value, final C max, final String message) {
    if (value.compareTo(max) >= 0) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the actual value is less than the lower bound or greater than the upper bound in a range of values,
   * or throws an IllegalArgumentException with message if the assertion fails.
   * @param value the actual Comparable value expected to less than the lower bound or greater than the upper bound.
   * @param lowerBound the lower value in the range expected to exclude the actual value.
   * @param upperBound the upper value in the range expected to exclude the actual value.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the assertion fails and the actual value falls within the range
   * defined by the lower and upper bounds.
   */
  public static <C extends Comparable<C>> void lessThanOrGreaterThan(final C value,
                                                                     final C lowerBound,
                                                                     final C upperBound,
                                                                     final String message) {
    if (value.compareTo(lowerBound) >= 0 && value.compareTo(upperBound) <= 0) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the actual value is less than the lower bound or greater than equal to the upper bound in a range
   * of values, or throws an IllegalArgumentException with message if the assertion fails.
   * @param value the actual Comparable value expected to less than the lower bound or greater than equal to
   * the upper bound.
   * @param lowerBound the lower value in the range expected to exclude the actual value.
   * @param upperBound the upper value in the range expected to inclusively exclude the actual value.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the assertion fails and the actual value falls within the range
   * defined by the lower and upper bounds.
   */
  public static <C extends Comparable<C>> void lessThanOrGreaterThanEqual(final C value, 
                                                                          final C lowerBound,
                                                                          final C upperBound,
                                                                          final String message)
  {
    if (value.compareTo(lowerBound) >= 0 && value.compareTo(upperBound) < 0) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the actual value is less than or equal to the maximum value, or throws an IllegalArgumentException
   * with message if the assertion fails.
   * @param value the actual Comparable value expected to be less than or equal to the maximum value.
   * @param max the maximum, inclusive value expected of the actual value.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the assertion fails and the actual value is greater than
   * the maximum value.
   */
  public static <C extends Comparable<C>> void lessThanEqual(final C value, final C max, final String message) {
    if (value.compareTo(max) > 0) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the actual value is less than equal to the lower bound or greater than the upper bound, or throws an
   * IllegalArgumentException with message if the assertion fails.
   * @param value the actual Comparable value expected to be less than or equal to the maximum value.
   * @param lowerBound the lower value in the range expected to inclusively exclude the actual value.
   * @param upperBound the upper value in the range expected to exclude the actual value.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the assertion fails and the actual value is greater than
   * the lower bound and less than equal to the upper bound in the range.
   */
  public static <C extends Comparable<C>> void lessThanEqualOrGreaterThan(final C value, 
                                                                          final C lowerBound,
                                                                          final C upperBound,
                                                                          final String message)
  {
    if (value.compareTo(lowerBound) > 0 && value.compareTo(upperBound) <= 0) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the actual value is less than equal to the lower bound or greater than equal to the upper bound,
   * or throws an IllegalArgumentException with message if the assertion fails.
   * @param value the actual Comparable value expected to be less than or equal to the maximum value.
   * @param lowerBound the lower value in the range expected to inclusively exclude the actual value.
   * @param upperBound the upper value in the range expected to exclude the actual value.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the assertion fails and the actual value is greater than
   * the lower bound and less than equal to the upper bound in the range.
   */
  public static <C extends Comparable<C>> void lessThanEqualOrGreaterThanEqual(final C value, 
                                                                               final C lowerBound,
                                                                               final C upperBound,
                                                                               final String message)
  {
    if (value.compareTo(lowerBound) > 0 && value.compareTo(upperBound) < 0) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the String value contains letters only or throws an IllegalArgumentException with message if the
   * assertion fails.
   * @param value the String argument expected to contain letters only.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the String argument contains non-letter characters.
   * @see StringUtil#isLettersOnly(String)
   */
  public static void lettersOnly(final String value, final String message) {
    if (!StringUtil.isLettersOnly(value)) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the numerical value is negative or throws an IllegalArgumentException with message if the assertion fails.
   * @param value the numerical value expected to be negative.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the value is not negative (such as zero or positive).
   * @see NumberUtil#isNegative(double)
   */
  public static void negative(final Number value, final String message) {
    if (!NumberUtil.isNegative(value.doubleValue())) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts that the String argument is not blank, which is a String value that is neither null, the empty String
   * or a String containing only whitespace.
   * @param value the String argument asserted for non-blankness.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the String argument is blank.
   * @see StringUtil#isBlank(String)
   */
  public static void notBlank(final String value, final String message) {
    if (StringUtil.isBlank(value)) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the Object array argument is not null and not empty, or throws an IllegalArgumentException with message
   * if the assertion fails.
   * @param array the Object array expected to be not empty.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the Object array is empty.
   * @see ArrayUtil#isEmpty(Object[])
   */
  public static void notEmpty(final Object[] array, final String message) {
    if (ArrayUtil.isEmpty(array)) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the Collection argument is not null and not empty, or throws an IllegalArgumentException with message
   * if the assertion fails.
   * @param collection the Collection expected to be not empty.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the Collection is empty.
   * @see CollectionUtil#isEmpty(java.util.Collection)
   */
  public static void notEmpty(final Collection collection, final String message) {
    if (CollectionUtil.isEmpty(collection)) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the Map argument is not null and not empty, or throws an IllegalArgumentException with message
   * if the assertion fails.
   * @param map the Map expected to be not empty.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the Map is empty.
   * @see CollectionUtil#isEmpty(java.util.Map)
   */
  public static void notEmpty(final Map map, final String message) {
    if (CollectionUtil.isEmpty(map)) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the String argument is not the null and is not the empty String, or throws an IllegalArgumentException
   * with message if the assertion fails.
   * @param value the String value expected to not be empty.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the String argument is the empty String.
   * @see StringUtil#isEmpty(String)
   */
  public static void notEmpty(final String value, final String message) {
    if (StringUtil.isEmpty(value)) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts two Objects are not equal or throws an IllegalArgumentException with message if the assertion fails.
   * @param obj1 an Object argument in the equality comparison.
   * @param obj2 an Object argument in the equality comparison.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the Object arguments are equal.
   * @see ObjectUtil#equals(Object, Object)
   */
  public static void notEqual(final Object obj1, final Object obj2, final String message) {
    if (ObjectUtil.equals(obj1, obj2)) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the Object argument is not null or throws a NullPointerException with message if the Object is null.
   * @param obj the Object argument expected to be non-null.
   * @param message the message contained in the NullPointerException thrown by this method.
   * @throws NullPointerException with message if the Object argument is null.
   * @see ObjectUtil#isNull(Object)
   */
  public static void notNull(final Object obj, final String message) {
    if (ObjectUtil.isNull(obj)) {
      log.warn(message);
      throw new NullPointerException(message);
    }
  }

  /**
   * Asserts the Object argument is not null or throws the given RuntimeException if the Object is null.
   * @param obj the Object argument expected to be non-null.
   * @param e the RuntimeException thrown by this method of the Object argument is null.
   * @throws RuntimeException if the Object is null.
   * @see ObjectUtil#isNull(Object)
   */
  public static void notNull(final Object obj, final RuntimeException e) {
    if (ObjectUtil.isNull(obj)) {
      log.warn(e.getMessage());
      throw e;
    }
  }

  /**
   * Asserts two Objects are the not the same Object, or not identical using the identity comparison, or throws an
   * IllegalArgumentException with message if the assertion fails.
   * @param obj1 an Object in the identity comparison.
   * @param obj2 an Object in the identity comparison.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the Objects are the same.
   * @see ObjectUtil#isSame(Object, Object)
   */
  public static void notSame(final Object obj1, final Object obj2, final String message) {
    if (ObjectUtil.isSame(obj1, obj2)) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the numerical value is positive or throws an IllegalArgumentException with message if the assertion fails.
   * @param value the numerical value expected to be positive.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the value is not positive (such as zero or negative).
   * @see NumberUtil#isPositive(double)
   */
  public static void positive(final Number value, final String message) {
    if (!NumberUtil.isPositive(value.doubleValue())) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts two Objects are the same Object, or identical using the identity comparison, or throws an
   * IllegalArgumentException if the assertion fails.
   * @param obj1 an Object in the identity comparison.
   * @param obj2 an Object in the identity comparison.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the Objects are not the same.
   * @see ObjectUtil#isNotSame(Object, Object)
   */
  public static void same(final Object obj1, final Object obj2, final String message) {
    if (ObjectUtil.isNotSame(obj1, obj2)) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts the condition is true or throws an IllegalStateException if the assertion fails.
   * @param condition the state of the condition expected to be true.
   * @param message the message contained in the IllegalStateException thrown by this method.
   * @throws IllegalStateException with message if the condition is false.
   */
  public static void state(final boolean condition, final String message) {
    if (!condition) {
      log.warn(message);
      throw new IllegalStateException(message);
    }
  }

  /**
   * Asserts the condition is true or throws the given RuntimeException if the condition is false.
   * @param condition the state of the condition expected to be true.
   * @param e the RuntimeException thrown by this method if the condition is not true.
   */
  public static void state(final boolean condition, final RuntimeException e) {
    if (!condition) {
      log.warn(e.getMessage());
      throw e;
    }
  }

  /**
   * Asserts the value is zero or throws an IllegalArgumentException with message if the assertion fails.
   * @param value the numerical value expected to be zero.
   * @param message the message contained in the IllegalArgumentException thrown by this method.
   * @throws IllegalArgumentException with message if the value is not zero (such as positive or negative).
   * @see NumberUtil#isZero(double)
   */
  public static void zero(final Number value, final String message) {
    if (!NumberUtil.isZero(value.doubleValue())) {
      log.warn(message);
      throw new IllegalArgumentException(message);
    }
  }

}
