/*
 * Month.java (c) 5 April 2009
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

public enum Month {
  JANUARY(Calendar.JANUARY, 1, "January"),
  FEBRUARY(Calendar.FEBRUARY, 2, "February"),
  MARCH(Calendar.MARCH, 3, "March"),
  APRIL(Calendar.APRIL, 4, "April"),
  MAY(Calendar.MAY, 5, "May"),
  JUNE(Calendar.JUNE, 6, "June"),
  JULY(Calendar.JULY, 7, "July"),
  AUGUST(Calendar.AUGUST, 8, "August"),
  SEPTEMBER(Calendar.SEPTEMBER, 9, "September"),
  OCTOBER(Calendar.OCTOBER, 10, "October"),
  NOVEMBER(Calendar.NOVEMBER, 11, "November"),
  DECEMBER(Calendar.DECEMBER, 12, "December");

  private final int calendarConstant;
  private final int monthOfYear;

  private final String description;

  /**
   * Creates an enumerated Month constant initialized with the corresponding Calendar month constant, the numeric
   * month out of the year and a description of the month.
   * @param calendarConstant an integer value specifying the java.util.Calendar month constant.
   * @param monthOfYear the numeric value for the month within a Calendar year.
   * @param description a String name for the Calendar month.
   */
  Month(final int calendarConstant, final int monthOfYear, final String description) {
    this.calendarConstant = calendarConstant;
    this.monthOfYear = monthOfYear;
    this.description = description;
  }

  /**
   * Gets the enumerated Month value for the specified java.util.Calendar month constant.
   * @param calendarConstant an integer value specifying the java.util.Calendar month constant.
   * @return the enumerated Month value corresponding to the java.util.Calendar month constant or null
   * if no Month enumerated value matches the java.util.Calendar month constant.
   */
  public static Month getMonthForCalendarConstant(final int calendarConstant) {
    for (final Month month : values()) {
      if (month.getCalendarConstant() == calendarConstant) {
        return month;
      }
    }

    return null;
  }

  /**
   * Gets the enumerated Month value for the specified numeric value indicating the month within the year.
   * @param monthOfYear an integer value specifying the numeric month within the calendar year.
   * @return the enumerated Month value corresponding to the numeric month with the calendar year or null
   * if no Month enumerated value matches the numeric month within the year value.
   */
  public static Month getMonthForMonthOfYear(final int monthOfYear) {
    for (final Month month : values()) {
      if (month.getMonthOfYear() == monthOfYear) {
        return month;
      }
    }

    return null;
  }

  /**
   * Gets the corresponding java.util.Calendar month constant for this Month enumerated value.
   * @return an integer value specifying the java.util.Calendar month constant corresponding to this
   * Month enumerated value.
   */
  public final int getCalendarConstant() {
    return calendarConstant;
  }

  /**
   * Gets the name of the calendar Month enumerated value.
   * @return a String name for this Month enumerated value.
   */
  public final String getDescription() {
    return description;
  }

  /**
   * Gets the numeric value for this month within the calendar year.
   * @return an integer value specifying the numeric value of this month within the calendar year.
   */
  public final int getMonthOfYear() {
    return monthOfYear;
  }

  /**
   * Gets the number of days in this calendar month for the given year.
   * @param year an integer value specifying the calendar year.
   * @return an integer value specifying the number of days in this calendar month for the given year.
   */
  public final int getNumberOfDays(final int year) {
    return DateUtil.getNumberOfDaysInMonth(getCalendarConstant(), year);
  }

  /**
   * Returns a String describing this calendar Month enumerated value.
   * @return a String value describing this calendar Month enumerated value.
   */
  @Override
  public String toString() {
    return getDescription();
  }

}
