/*
 * TimeUnit.java (c) 21 February 2003
 *
 * The TimeUnit class is a type-safe enum that defines constants
 * representing various units of time.  Each unit of time is
 * defined in terms of the one before it (or that has the shorter
 * duration).  This class can be used to convert to different
 * units of time from a given unit of time.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.8.10
 * @see java.lang.Comparable
 * @see java.io.Serializable
 */

package com.cp.common.util;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import java.io.Serializable;

public final class TimeUnit implements Comparable, Serializable {

  // Basic units in which all other units are measured.
  public static final TimeUnit MILLISECOND = new TimeUnit("Millisecond");
  public static final TimeUnit SECOND = new TimeUnit("Second", 1000, MILLISECOND);
  public static final TimeUnit MINUTE = new TimeUnit("Minute", 60, SECOND);
  public static final TimeUnit HOUR = new TimeUnit("Hour", 60, MINUTE);
  public static final TimeUnit DAY = new TimeUnit("Day", 24, HOUR);

  // Weeks of a Month in terms of number of Days
  // NOTE that a week represents the safest, largest unit of time for which no adjusting occurs.
  // A year for example can be a leap year and therefore is adjusted by one day.  Anything that is
  // defined in terms of years will be affected.  Months have no reference point as to which month
  // should be considered "thee" month in order to define the largest, safest unit of time.
  public static final TimeUnit WEEK = new TimeUnit("Week", 7, DAY);

  // Months of a Year in terms of number of Days
  public static final TimeUnit JANUARY = new TimeUnit("January", 31, DAY);
  public static final TimeUnit FEBRUARY = new TimeUnit("February", 28, DAY);
  public static final TimeUnit MARCH = new TimeUnit("March", 31, DAY);
  public static final TimeUnit APRIL = new TimeUnit("April", 30, DAY);
  public static final TimeUnit MAY = new TimeUnit("May", 31, DAY);
  public static final TimeUnit JUNE = new TimeUnit("June", 30, DAY);
  public static final TimeUnit JULY = new TimeUnit("July", 31, DAY);
  public static final TimeUnit AUGUST = new TimeUnit("August", 31, DAY);
  public static final TimeUnit SEPTEMBER = new TimeUnit("September", 30, DAY);
  public static final TimeUnit OCTOBER = new TimeUnit("October", 31, DAY);
  public static final TimeUnit NOVEMBER = new TimeUnit("November", 30, DAY);
  public static final TimeUnit DECEMBER = new TimeUnit("December", 31, DAY);
  // Covers February for the Leap Year
  public static final TimeUnit EXTENDED_FEBRUARY = new TimeUnit("February", 29, DAY);

  // Year in terms of number of Days
  public static final TimeUnit YEAR = new TimeUnit("Year", 365, DAY);
  // Leap Year defined for every 4th, even year in which the year contains 1 extra day.
  public static final TimeUnit LEAP_YEAR = new TimeUnit("Leap Year", 366, DAY);

  // Special TimeUnits - note that these are not entirely accurate as they do not account for leap years.
  public static final TimeUnit DECADE = new TimeUnit("Decade", 10, YEAR);
  public static final TimeUnit SCORE = new TimeUnit("Score", 20, YEAR);
  public static final TimeUnit CENTURY = new TimeUnit("Century", 100, YEAR);
  public static final TimeUnit MILLENNIUM = new TimeUnit("Millenium", 1000, YEAR);
  public static final TimeUnit OTHER_MILLENNIUM = new TimeUnit("Millenium", 10, CENTURY);

  private final int inTermsOfUnitValue;

  private final String description;

  private final TimeUnit inTermsOfUnit;

  /**
   * Create an instance of the TimeUnit class with the specified description
   * @param description user friendly description of the time unit.
   */
  private TimeUnit(final String description) {
    this(description, 1, null);
  }

  /**
   * Creates an instance of the TimeUnit class to define a unit of time.
   * @param description user friendly description of the time unit.
   * @param inTermsOfUnitValue equivalent to X number of in terms of unit.
   * @param inTermsOfUnit the base time unit in which this unit is derived.
   */
  private TimeUnit(final String description, final int inTermsOfUnitValue, final TimeUnit inTermsOfUnit) {
    this.description = description;
    this.inTermsOfUnitValue = inTermsOfUnitValue;
    this.inTermsOfUnit = inTermsOfUnit;
  }

  /**
   * Compares this TimeUnit with another TimeUnit to determine the order of the two units.
   */
  public int compareTo(final Object obj) {
    final TimeUnit timeUnit = (TimeUnit) obj;
    final long value = getTotalUnitValue() - timeUnit.getTotalUnitValue();
    return (value < 0 ? -1 : (value > 0 ? 1 : 0));
  }

  /**
   * Returns a user friendly display of the value of the specified time unit in terms of weeks and milliseconds.
   * @param numberOfUnits is the number of units of the specified "sourceTimeUnit" unit of time, such as 30.
   * @param sourceTimeUnit is the unit of time of the specified numberOfUnits, such as minutes.  Therefore, combining
   * numberOfUnits and sourceTimeUnit, you would have 30 minutes.
   * @return a String representation of the numberOfUnits of sourceTimeUnits in terms of weeks down through milleseconds.
   */
  public static String convertTo(final long numberOfUnits, final TimeUnit sourceTimeUnit) {
    return convertTo(numberOfUnits, sourceTimeUnit, null, null);
  }

  /**
   * Returns a user friendly display of the value of the specified time unit in terms of another set of time units
   * upTo a specified unit of time and not smaller than the downTo unit of time.
   * @param numberOfUnits is the number of units of the specified "sourceTimeUnit" unit of time, such as 30.
   * @param sourceTimeUnit is the unit of time of the specified numberOfUnits, such as minutes.  Therefore, combining
   * numberOfUnits and sourceTimeUnit, you would have 30 minutes.
   * @param upTo is the greatest unit of time in which you want the numberOfUnits in sourceTimeUnit unit of time
   * to be displayed.
   * @param downTo is the smallest unit of time in which you want the numberOfUnits in sourceTimeUnit unit of time
   * to be displayed.
   * @return a String representation of the numberOfUnits of sourceTimeUnits in terms of upTo through downTo time units.
   * @throws java.lang.IllegalArgumentException if the upTo unit of time is larger than the downTo unit of time.
   */
  public static String convertTo(final long numberOfUnits,
                                 final TimeUnit sourceTimeUnit,
                                 TimeUnit upTo,
                                 TimeUnit downTo)
  {
    Assert.notNull(sourceTimeUnit, "The TimeUnit of the number of units  to convert must be specified!");

    upTo = ObjectUtil.getDefaultValue(upTo, WEEK);
    downTo = ObjectUtil.getDefaultValue(downTo, MILLISECOND);

    Assert.greaterThanEqual(upTo.compareTo(downTo), 0, "The \"upTo\" TimeUnit must be larger than the \"downTo\" TimeUnit!");

    TimeUnit currentTimeUnit = upTo;
    long theUnitValue = sourceTimeUnit.getTotalUnitValue(numberOfUnits);
    final StringBuffer buffer = new StringBuffer();

    while (currentTimeUnit != null && currentTimeUnit.compareTo(downTo) >= 0 && theUnitValue > 0) {
      long currentUnitValue = currentTimeUnit.getTotalUnitValue();
      if (theUnitValue >= currentUnitValue) {
        long value = theUnitValue / currentUnitValue;
        buffer.append(value);
        buffer.append(" ");
        buffer.append(currentTimeUnit.getDescription());
        buffer.append(value > 1 ? "s " : " ");
        theUnitValue %= currentUnitValue;
      }
      currentTimeUnit = currentTimeUnit.getInTermsOfUnit();
    }

    if (theUnitValue > 0) {
      long value = theUnitValue / sourceTimeUnit.getTotalUnitValue();
      buffer.append(value);
      buffer.append(" ");
      buffer.append(sourceTimeUnit.getDescription());
      buffer.append(value > 1 ? "s " : " ");
    }

    return buffer.toString();
  }

  /**
   * Gets the description of this TimeUnit.
   * @return a String value describing this TimeUnit.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the unit of time in which this TimeUnit is defined.
   * @return a TimeUnit object specifying the unit of time this TimeUnit is defined in terms of.
   */
  public TimeUnit getInTermsOfUnit() {
    return inTermsOfUnit;
  }

  /**
   * Gets the number of units of time in which this TimeUnit is based on (or defined as) with respect to the other
   * unit of time. For example, if this TimeUnit represents 1 hour then 1 hour is equivalent to 60 minutes.
   * Therefore, this method returns 60.
   * @return an integer value specifying the unit value of this TimeUnit in terms of defined TimeUnit.
   */
  public int getInTermsOfUnitValue() {
    return inTermsOfUnitValue;
  }

  /**
   * Gets the value for this TimeUnit in terms of the target unit of time.
   * @param targetTimeUnit the target TimeUnit to determine this TimeUnit's value of.
   * @return a long value indicating this TimeUnit's worth in terms of the specified TimeUnit.
   */
  public long getInTermsOf(final TimeUnit targetTimeUnit) {
    return getInTermsOf(1, targetTimeUnit);
  }

  /**
   * Gets the value for a specified number of these TimeUnits in terms of the target unit of time.
   * @param numberOfThisUnit an integer value specifying the number of units of this TimeUnit.
   * @param targetTimeUnit the target TimeUnit to determine this TimeUnit's value of.
   * @return a long value indicating a number of these TimeUnit's worth in terms of the specified TimeUnit.
   */
  public long getInTermsOf(final int numberOfThisUnit, final TimeUnit targetTimeUnit) {
    if (targetTimeUnit == this) {
      return numberOfThisUnit;
    }

    if (compareTo(targetTimeUnit) < 0) {
      final long upCastValue = targetTimeUnit.getInTermsOf(this);
      return (numberOfThisUnit > upCastValue ? (numberOfThisUnit / upCastValue) : 0);
    }

    if (targetTimeUnit.equals(inTermsOfUnit)) {
      return numberOfThisUnit * inTermsOfUnitValue;
    }

    return numberOfThisUnit * inTermsOfUnitValue * inTermsOfUnit.getInTermsOf(targetTimeUnit);
  }

  /**
   * Gets the total unit value of this TimeUnit in milliseconds.
   * @return a long value specifying this TimeUnit's worth in milliseconds.
   *
   */
  private long getTotalUnitValue() {
    return inTermsOfUnitValue * (inTermsOfUnit == null ? 1 : inTermsOfUnit.getTotalUnitValue());
  }

  /**
   * Gets the total unit value for a given number of these TimeUnits in milliseconds.
   * @param numberOfThisUnit an integer value specify the number of units of this TimeUnit.
   * @return a long value specifying the value of this TimeUnit's worth in terms of the TimeUnit it is defined as.
   */
  final long getTotalUnitValue(final long numberOfThisUnit) {
    return numberOfThisUnit * getTotalUnitValue();
  }

  /**
   * Determines whether this TimeUnit is equal to some other Object.
   * @param obj the Object used in the equality comparison.
   * @return a boolean value indicating whether the specified object is equal to this TimeUnit value.
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof TimeUnit)) {
      return false;
    }

    final TimeUnit timeUnit = (TimeUnit) obj;

    return getInTermsOfUnit() == timeUnit.getInTermsOfUnit()
      && getInTermsOfUnitValue() == timeUnit.getInTermsOfUnitValue();
  }

  /**
   * Computes the hash code value of this TimeUnit instance.
   * @return an integer value of the computed hash value for this TimeUnit instance.
   */
  @Override
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + getInTermsOfUnitValue();
    hashValue = 37 * hashValue + (getInTermsOfUnit() == null ? 0
      : getInTermsOfUnit().hashCode());
    return hashValue;
  }

  /**
   * Get a String value describing the internal state of this TimeUnit.
   * @return a String value describing the state of this TimeUnit.
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("A ");
    buffer.append(getDescription());
    buffer.append(" is defined in terms of ");
    buffer.append(inTermsOfUnitValue);
    buffer.append(" ");
    buffer.append(inTermsOfUnit.getDescription());
    buffer.append(inTermsOfUnitValue > 1 ? "s" : "");
    return buffer.toString();
  }

}
