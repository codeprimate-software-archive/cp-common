/*
 * Weekday.java (c) 5 April 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2009.5.5
 * @see com.cp.common.util.DateUtil
 * @see java.util.Calendar
 */

package com.cp.common.util;

import java.util.Calendar;

public enum Weekday {
  SUNDAY(Calendar.SUNDAY, 1, "Sunday"),
  MONDAY(Calendar.MONDAY, 2, "Monday"),
  TUESDAY(Calendar.TUESDAY, 3, "Tuesday"),
  WEDNESDAY(Calendar.WEDNESDAY, 4, "Wednesday"),
  THURSDAY(Calendar.THURSDAY, 5, "Thursday"),
  FRIDAY(Calendar.FRIDAY, 6, "Friday"),
  SATURDAY(Calendar.SATURDAY, 7, "Saturday");

  private final int calendarConstant;
  private final int dayOfWeek;

  private final String description;

  /**
   * Default constructor for the Weekday enumerated type creating an instance initialized with the corresponding
   * java.util.Calendar weekday constant, the numeric day of week and a description for the weekday.
   * @param calendarConstant an integer value specifying the java.util.Calendar weekday constant.
   * @param dayOfWeek an integer value specifying the numeric day of the week.
   * @param description a String value describing the Weekday enumerated value.
   */
  Weekday(final int calendarConstant, final int dayOfWeek, final String description) {
    this.calendarConstant = calendarConstant;
    this.dayOfWeek = dayOfWeek;
    this.description = description;
  }

  /**
   * Gets the Weekday enumerated value for the specified java.util.Calendar weekday constant.
   * @param calendarConstant an integer value specifying the java.util.Calendar weekday constant.
   * @return a Weekday enumerated value for the specified java.util.Calendar weekday constant or null
   * if no Weekday enumered value matches the java.util.Calendar weekday constant.
   */
  public static Weekday getWeekdayForCalendarConstant(final int calendarConstant) {
    for (final Weekday weekday : values()) {
      if (weekday.getCalendarConstant() == calendarConstant) {
        return weekday;
      }
    }

    return null;
  }

  /**
   * Gets the Weekday enumerated value for the specified numeric day of week value.
   * @param dayOfWeek an integer value specifying a numeric day of week value.
   * @return a Weekday enumerated value for the specified numeric day of week value or null
   * if no Weekday enumered value matches the given numeric day of week value.
   */
  public static Weekday getWeekdayForDayOfWeek(final int dayOfWeek) {
    for (final Weekday weekday : values()) {
      if (weekday.getDayOfWeek() == dayOfWeek) {
        return weekday;
      }
    }

    return null;
  }

  /**
   * Gets the java.util.Calendar weekday constant corresponding to this Weekday enumerated value.
   * @return an integer value specifying the java.util.Calendar weekday constant.
   */
  public int getCalendarConstant() {
    return calendarConstant;
  }

  /**
   * Gets the numeric day of week value corresponding to this Weekday enumerated value.
   * @return an integer value specifying the numeric day of week.
   */
  public int getDayOfWeek() {
    return dayOfWeek;
  }

  /**
   * Gets a String value representing the name of the Weekday enumerated value.
   * @return a String value specifying the name of the Weekday enumerated value.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets a boolean value indicating whether this Weekday enumerated value is a day during the week.
   * @return a boolean value indicating this Weekday enumerated value is a day during the week.
   */
  public boolean isWeekday() {
    return DateUtil.isWeekday(getCalendarConstant());
  }

  /**
   * Gets a boolean value indicating whether this Weekday enumerated value is a weekend day.
   * @return a boolean value indicating this Weekday enumerated value is a weekend day.
   */
  public boolean isWeekend() {
    return DateUtil.isWeekend(getCalendarConstant());
  }

  /**
   * Returns a String describing this Weekday enumerated value.
   * @return a String value describing this Weekday enumerated value.
   */
  @Override
  public String toString() {
    return getDescription();
  }

}
