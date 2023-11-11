/*
 * CalendarModel.java (c) 1 April 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.6
 * @see com.cp.common.util.DataModel
 * @see java.util.Calendar
 */

package com.cp.common.swing;

import com.cp.common.util.DataModel;
import java.util.Calendar;

public interface CalendarModel extends DataModel, JDateField.DataModel {

  /**
   * Returns the value of the Calendar property.  Note, this method makes a defensive copy of the Calendar
   * property value before the value is returned.
   * @return a Calendar object specifying the date value of the Calendar property.
   */
  public Calendar getCalendar();

  /**
   * Sets the value of the Calendar property.  Note, this method makes a defensive copy of the Calendar
   * parameter before the property is set.
   * @param calendar the Calendar object used to set the Calendar property.
   * @throws java.lang.IllegalArgumentException if the value of the Calendar object is not valid.
   */
  public void setCalendar(Calendar calendar);

  /**
   * Returns an integer value indicating the first day of the month as a week day (for example, as in TGIF,
   * Calendar.FRIDAY), regardless of the month or year.
   * @param dayOfMonth an integer value specifying the day of month.  For example, today's date is April 4th, 2005,
   * therefore the day of month is 4.
   * @param dayOfWeek a Calendar constant indicating the day of week that the day of month falls on.  For example,
   * today is Monday, April 4th, 2005, therefore, the day of week is Calendar.MONDAY.
   * @return an integer value specifying the day of week as a Calendar constant, for example Calendar.THURSDAY,
   * indicating the first day of the month.
   */
  public int getFirstDayOfMonth(int dayOfMonth, int dayOfWeek);

  /**
   * Peforms a destructive read of the monthYearChanged property value.  This property value indicates if either the
   * month or the year has changed since the last calendar property update, the last read operation on this property,
   * which ever happened last.  If just the day of the calendar changes, then the value of the property will be false.
   * @return a boolean value indicating whether the month or year changed since the last read on this property value.
   */
  public boolean getMonthYearChanged();

  /**
   * Reads the current value of the monthYearChanged property.  This property value indicates if either the
   * month or the year has changed since the last calendar property update.  If just the day of the calendar changes,
   * then the value of the property will be false.
   * @return a boolean value indicating whether the month or year changed since the calendar update.
   */
  public boolean isMonthYearChanged();

  /**
   * Returns the number of days in the specified month, expressed as a Calendar constant, for the current year.
   * @param month a Calendar constant (for example, Calendar.MAY) specifying the month of the current year.
   * @return an integer value specifying the number of days in the specified month for the current year.
   */
  public int getNumberOfDaysInMonth(int month);

  /**
   * Returns the number of days for the specified month, expressed as a Calendar constant, for the specified year.
   * @param month a Calendar constant (for example, Calendar.MAY) specifying the month of the specified year.
   * @param year an integer value specifying the year used in determining the number of days in the specified month.
   * Note, year only matters for February in leap years.
   * @return an integer value specifying the number of days in the specified month of the specified year.
   */
  public int getNumberOfDaysInMonth(int month, int year);

  /**
   * Decrements the calendar's month.
   */
  public void decrementMonth();

  /**
   * Decrements the calendar's year.
   */
  public void decrementYear();

  /**
   * Increments the calendar's month.
   */
  public void incrementMonth();

  /**
   * Increments the calendar's year.
   */
  public void incrementYear();

  /**
   * Sets the date field, specified by the Calendar constant, to the corresponding value.
   * @param calendarField a Calendar constant referring to the date field to set with the value.
   * @param value an integer value for the day, month or year as determined by the Calendar
   * constant.
   * @throws java.lang.IllegalArgumentException if the calendarField is not a valid date field,
   * or Calendar constant.
   */
  public void set(int calendarField, int value);

}
