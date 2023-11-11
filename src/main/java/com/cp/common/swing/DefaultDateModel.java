/*
 * DefaultDateModel (c) 12 September 2004
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.6
 * @see com.cp.common.lang.ObjectUtil
 * @see com.cp.common.swing.JDateField.DataModel
 * @see com.cp.common.util.AbstractDataModel
 * @see com.cp.common.util.ArrayUtil
 * @see com.cp.common.util.DateUtil
 * @see java.util.Calendar
 * @see java.util.Date
 */

package com.cp.common.swing;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.AbstractDataModel;
import com.cp.common.util.ArrayUtil;
import com.cp.common.util.DateUtil;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

class DefaultDateModel extends AbstractDataModel implements JDateField.DataModel {

  private static final Logger logger = Logger.getLogger(DefaultDateModel.class);

  private static final int[] NUMBER_OF_DAYS_IN_MONTH = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

  protected static final List NUMBER_OF_DAYS_IN_MONTH_LIST = Collections.unmodifiableList(ArrayUtil.asList(NUMBER_OF_DAYS_IN_MONTH));

  private int currentDay;

  private Calendar calendar;

  /**
   * Creates a default instance of the DefaultDateModel class.
   */
  DefaultDateModel() {
  }

  /**
   * Creates an instance of the DefaultDateModel class initialized to the specified Calendar.
   * @param calendar the Calendar used to initialize an instance of the DefaultDateModel class.
   */
  DefaultDateModel(final Calendar calendar) {
    this.calendar = DateUtil.copy(calendar);
    currentDay = calendar.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Creates an instance of the DefaultDateModel class initialized to the specified Date.
   * @param date the Date used to initialize an instance of the DefaultDateModel class.
   */
  DefaultDateModel(final Date date) {
    this(DateUtil.getCalendar(date));
  }

  /**
   * Returns the value of the Calendar property.  Note, this method makes a defensive copy of the Calendar
   * property value before the value is returned.
   * @return a Calendar object specifying the date value of the Calendar property.
   */
  public Calendar getCalendar() {
    return (ObjectUtil.isNull(calendar) ? null : DateUtil.copy(calendar));
  }

  /**
   * Sets the value of the Calendar property.  Note, this method makes a defensive copy of the Calendar
   * parameter before the property is set.
   * @param calendar the Calendar object used to set the Calendar property.
   * @throws java.lang.IllegalArgumentException if the value of the Calendar object is not valid.
   */
  public void setCalendar(final Calendar calendar) {
    if (logger.isDebugEnabled()) {
      logger.debug("setting calendar (" + calendar + ")");
    }
    try {
      final Calendar calendarCopy = (ObjectUtil.isNull(calendar) ? null : DateUtil.copy(calendar));
      final PropertyChangeEvent event = new PropertyChangeEvent(this, "calendar", getCalendar(), calendar);
      fireVetoableChange(event);
      this.calendar = calendarCopy;
      firePropertyChange(event);
    }
    catch (PropertyVetoException e) {
      logger.warn("The calender (" + calendar + ") is not a legal date!", e);
      throw new IllegalArgumentException("The calendar (" + calendar + ") is not a legal date!");
    }
  }

  /**
   * Returns the last modified current day of the month.
   * @return a integer value specifying the current day of the month.  Possible values are in the range of
   * 1 to 31, depending on month.  For example, if the month is February, then the maximum integer value
   * returned is 28.
   */
  private int getCurrentDay() {
    return currentDay;
  }

  /**
   * Sets the last modified current day of the month.
   * @param currentDay an integer value specifying the current day of the month.
   */
  protected final void setCurrentDay(final int currentDay) {
    logger.debug("currentDay = " + currentDay);
    this.currentDay = currentDay;
  }

  /**
   * Returns the value of the Date property.  This method delegates to the getCalendar method.
   * @return the value of the Calendar property as a Date instance.
   * @see DefaultDateModel#getCalendar
   */
  public Date getDate() {
    return getCalendar().getTime();
  }

  /**
   * Sets the value of the Date property.  This method delegates to the setCalendar method.
   * @param date the Date object used to set the Calendar property.
   * @see DefaultDateModel#setCalendar
   */
  public void setDate(final Date date) {
    setCalendar(DateUtil.getCalendar(date));
  }

  /**
   * Returns the data value (date) represented by this model.
   * @return an Object value containing the data represented by this data model.
   */
  public Object getValue() {
    return getCalendar();
  }

  /**
   * Set the data value (date) to be represented by this model.
   * @param value an Object value specifying the data to be represented by this data model.
   */
  public void setValue(final Object value) {
    if (ObjectUtil.isNotNull(value)) {
      if (value instanceof Calendar) {
        setCalendar((Calendar) value);
      }
      else if (value instanceof Date) {
        setDate((Date) value);
      }
      else {
        logger.warn("Value (" + value.getClass() + ") is not valid value type to set on this model.  Please specify"
          + " either a Date or Calendar.");
        throw new IllegalArgumentException("Value (" + value.getClass() + ") is not valid value type to set on this"
          + " model.  Please specify either a Date or Calendar.");
      }
    }
    else {
      setCalendar(null);
    }
  }

  /**
   * Decrements the current day to the previous day of the month, or to the last day of the previous month
   * if the current day is the 1st of the current month, or the last day of the previous month of the previous
   * year if the current day is the 1st of January.
   * @param calendar the Calendar object in which to set the date field values.
   */
  protected final void decrementDay(final Calendar calendar) {
    final int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
    if (currentDay > 1) {
      calendar.roll(Calendar.DAY_OF_MONTH, false);
    }
    else {
      decrementMonth(calendar);

      final int currentMonth = calendar.get(Calendar.MONTH);
      final int currentYear = calendar.get(Calendar.YEAR);
      final int numDaysInMonth = NUMBER_OF_DAYS_IN_MONTH[currentMonth]
        + (isFebruary(currentMonth) && DateUtil.isLeapYear(currentYear) ? 1 : 0);

      calendar.set(Calendar.DAY_OF_MONTH, numDaysInMonth);
    }
    setCurrentDay(calendar.get(Calendar.DAY_OF_MONTH));
  }

  /**
   * Decrements the current month to the previous month in the year, or to December of the previous year
   * if the current month is January.  This method will also adjust the current day of the month if the previous month
   * is February and the current day is greater than 28 and the year is not a leap year, or the previous month is
   * February and the day is greater than 29 and the year is a leap year, or the previous month is any month with
   * 30 days and the current day of month is greater than 30; in all situations above, the method will set the
   * current day of the month to the last day in the previous month of the year and keep track of the last modified
   * current day of the month.
   * @param calendar the Calendar object in which to set the date field values.
   */
  protected final void decrementMonth(final Calendar calendar) {
    final int currentMonth = calendar.get(Calendar.MONTH);
    if (currentMonth > Calendar.JANUARY) {
      calendar.set(Calendar.DAY_OF_MONTH, 1);
      calendar.roll(Calendar.MONTH, false);

      final int numDaysInMonth = NUMBER_OF_DAYS_IN_MONTH[DateUtil.getPreviousMonth(currentMonth)]
        + (isFebruary(DateUtil.getPreviousMonth(currentMonth)) && DateUtil.isLeapYear(calendar.get(Calendar.YEAR)) ? 1 : 0);

      calendar.set(Calendar.DAY_OF_MONTH, Math.min(getCurrentDay(), numDaysInMonth));
    }
    else {
      calendar.set(Calendar.MONTH, Calendar.DECEMBER);
      decrementYear(calendar);
    }
  }

  /**
   * Decrements the current year to the previous year.  This method will also adjust the current day of the month if
   * the current month of the year is February.  It will set the current day of the month in February to the last day
   * in February if the current day of the month is greater than 28 and the year is not a a leap year, or if the
   * current day of the month is greater than 29 and the year is a leap year respectively.
   * @param calendar the Calendar object in which to set the date field values.
   */
  protected final void decrementYear(final Calendar calendar) {
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.roll(Calendar.YEAR, false);

    if (isFebruary(calendar.get(Calendar.MONTH)) && getCurrentDay() > 28) {
      final int numDaysInMonth = 28 + (DateUtil.isLeapYear(calendar.get(Calendar.YEAR)) ? 1 : 0);
      calendar.set(Calendar.DAY_OF_MONTH, Math.min(getCurrentDay(), numDaysInMonth));
    }
    else {
      calendar.set(Calendar.DAY_OF_MONTH, getCurrentDay());
    }
  }

  /**
   * Determines the correct value for the day of month based on the current month and year as specified by
   * the Calendar object.
   * @param calendar the Calendar object in which to adjust the day of month based the current values for
   * month and year.
   */
  protected final void determineDay(final Calendar calendar) {
    final int currentMonth = calendar.get(Calendar.MONTH);
    final int currentYear = calendar.get(Calendar.YEAR);
    final int numDaysInMonth = NUMBER_OF_DAYS_IN_MONTH[currentMonth] +
      (isFebruary(currentMonth) && DateUtil.isLeapYear(currentYear) ? 1 : 0);
    calendar.set(Calendar.DAY_OF_MONTH, Math.min(getCurrentDay(), numDaysInMonth));
  }

  /**
   * Increments the current day to the next day in the month, or the first day of the next month if the current
   * day is the last day in the current month, or to the first day of the month in the following year if the
   * current day of the month is December 31st.
   * @param calendar the Calendar object in which to set the date field values.
   */
  protected final void incrementDay(final Calendar calendar) {
    final int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
    if (currentDay < 28) {
      calendar.roll(Calendar.DAY_OF_MONTH, true);
    }
    else {
      final int currentMonth = calendar.get(Calendar.MONTH);
      final int currentYear = calendar.get(Calendar.YEAR);
      final int numDaysInMonth = NUMBER_OF_DAYS_IN_MONTH[currentMonth]
        + (isFebruary(currentMonth) && DateUtil.isLeapYear(currentYear) ? 1 : 0);
      if (currentDay < numDaysInMonth) {
        calendar.roll(Calendar.DAY_OF_MONTH, true);
      }
      else {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        setCurrentDay(1);
        incrementMonth(calendar);
      }
    }
    setCurrentDay(calendar.get(Calendar.DAY_OF_MONTH));
  }

  /**
   * Increments the current month to the next month in the year, or to January of the following year if the
   * current month is December.  This method will also adjust the current day of the month if the next month
   * is February and the current day is greater than 28 and the year is not a leap year, or the next month is
   * February and the day is greater than 29 and the year is a leap year, or the next month is any month with
   * 30 days and the current day of month is greater than 30; in all situations above, the method will set the
   * current day of the month to the last day in the next month of the year and keep track of the last modified
   * current day of the month.
   * @param calendar the Calendar object in which to set the date field values.
   */
  protected final void incrementMonth(final Calendar calendar) {
    final int currentMonth = calendar.get(Calendar.MONTH);
    if (currentMonth < Calendar.DECEMBER) {
      calendar.set(Calendar.DAY_OF_MONTH, 1);
      calendar.roll(Calendar.MONTH, true);

      final int numDaysInMonth = NUMBER_OF_DAYS_IN_MONTH[DateUtil.getNextMonth(currentMonth)]
        + (isFebruary(DateUtil.getNextMonth(currentMonth)) && DateUtil.isLeapYear(calendar.get(Calendar.YEAR)) ? 1 : 0);

      calendar.set(Calendar.DAY_OF_MONTH, Math.min(getCurrentDay(), numDaysInMonth));
    }
    else {
      calendar.set(Calendar.MONTH, Calendar.JANUARY);
      incrementYear(calendar);
    }
  }

  /**
   * Increments the current year to the next year.  This method will also adjust the current day of the month if
   * the current month of the year is February.  It will set the current day of the month in February to the last day
   * in February if the current day of the month is greater than 28 and the year is not a a leap year, or if the
   * current day of the month is greater than 29 and the year is a leap year respectively.
   * @param calendar the Calendar object in which to set the date field values.
   */
  protected final void incrementYear(final Calendar calendar) {
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.roll(Calendar.YEAR, true);

    if (isFebruary(calendar.get(Calendar.MONTH)) && getCurrentDay() > 28) {
      final int numDaysInMonth = 28 + (DateUtil.isLeapYear(calendar.get(Calendar.YEAR)) ? 1 : 0);
      calendar.set(Calendar.DAY_OF_MONTH, Math.min(getCurrentDay(), numDaysInMonth));
    }
    else {
      calendar.set(Calendar.DAY_OF_MONTH, getCurrentDay());
    }
  }

  /**
   * Determines whehter the specified month is February.  Note, months are zero based.
   * @param month a Calendar constant referring to the month.
   * @return a boolean value indicating whether the specified month is February.
   */
  protected static boolean isFebruary(final int month) {
    if (logger.isDebugEnabled()) {
      logger.debug("month (" + month + ")");
    }
    return (month == Calendar.FEBRUARY);
  }

  /**
   * Determines if the specified day for the current month and year (determined by Calendar.getIntance())
   * is a valid.
   * February, non-leap year: 1 - 28 days
   * February in leap year: 1 - 29 days
   * January, March, May, July, August, October, December: 1 - 31 days.
   * April, June, September, November: 1 - 30 days
   * @param day an integer value between 1 and the maximum number of days for the current month and year
   * as determined by Calendar.getInstance.
   * @return a boolean value indicating true if the day is valid for the current month and year.
   */
  public static boolean isValidDay(final int day) {
    final Calendar calendar = Calendar.getInstance();
    return isValidDay(day, calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
  }

  /**
   * Determines if the specified day and month is valid for the current year (determined by Calendar.getInstance()).
   * February, non-leap year: 1 - 28 days
   * February in leap year: 1 - 29 days
   * January, March, May, July, August, October, December: 1 - 31 days.
   * April, June, September, November: 1 - 30 days
   * @param day an integer value between 1 and the maximum number of days for the specified month and current year,
   * determined by Calendar.getInstance.
   * @param month an integer value between 1 and 12.
   * @return a boolean value indicating if the specified day is valid for the specified month and current year.
   */
  public static boolean isValidDay(final int day, final int month) {
    return isValidDay(day, month, Calendar.getInstance().get(Calendar.YEAR));
  }

  /**
   * Determines whether the specified day for the specified month during the specified year is valid.
   * @param day an integer value between 1 and the maximum number of days for the specified month and year.
   * @param month an integer value between 1 and 12.
   * @param year an integer value between 0 and Integer.MAX_VALUE.
   * @return a boolean value indicating if the specified day is valid for the specified month and year.
   */
  public static boolean isValidDay(final int day, int month, final int year) {
    if (logger.isDebugEnabled()) {
      logger.debug("day = " + day);
      logger.debug("month = " + month);
      logger.debug("year = " + year);
    }

    // normalize the month if the month is zero based
    month = (month == 0 ? normalizeMonth(month) : month);

    if (!isValidYear(year)) {
      logger.warn(year + " is not a valid year!");
      throw new IllegalArgumentException(year + " is not a valid year!");
    }

    if (!isValidMonth(month)) {
      logger.warn(month + " is not a valid month!");
      throw new IllegalArgumentException(month + " is not a valid month!");
    }

    final int numDaysInMonth = NUMBER_OF_DAYS_IN_MONTH[month - 1] +
      (isFebruary(month - 1) && DateUtil.isLeapYear(year) ? 1 : 0);

    return (day >= 1 && day <= numDaysInMonth);
  }

  /**
   * Determines whether the specified month is between 1 and 12.
   * @param month an integer value specifying a month.
   * @return a boolean value indicating if the month is valid (true if the month is between 1 and 12,
   * false otherwise).
   */
  public static boolean isValidMonth(final int month) {
    logger.debug("month = " + month);
    return (month >= 1 && month <= 12);
  }

  /**
   * Determines whehter the specified year is between 0 and Integer.MAX_VALUE.
   * @param year an integer value specifying the year.
   * @return a boolean value indicating if the year is valid (true if the year is between 0 and Integer.MAX_VALUE,
   * false otherwise).
   */
  public static boolean isValidYear(final int year) {
    logger.debug("year = " + year);
    return (year >= 0 && year <= Integer.MAX_VALUE);
  }

  /**
   * Normalizes the month value.  Shifts the month value by 1 if the month value is zero based.  Note, the
   * Calendar constants for January through December are zero based.
   * @param month an integer value specifying the month.
   * @return a integer value for the month that has been normalized.
   */
  static int normalizeMonth(final int month) {
    return (month + 1);
  }

  /**
   * Increments or decrements the respective date field, specified by the Calendar constant, one unit in the units
   * of the specified date field.
   * @param calendarField a Calendar constant specifying the date field to roll.
   * @param up a boolean value determining whether to increment or decrement the specified date field.
   * @throws java.lang.IllegalArgumentException if the calendarField is not a valid date field,
   * or Calendar constant.
   */
  public void roll(final int calendarField, final boolean up) {
    if (logger.isDebugEnabled()) {
      logger.debug("calendarField = " + calendarField);
      logger.debug("up = " + up);
    }

    final Calendar theCalendar = getCalendar();

    switch (calendarField) {
      case Calendar.DAY_OF_MONTH:
        rollDay(theCalendar, up);
        break;
      case Calendar.MONTH:
        rollMonth(theCalendar, up);
        break;
      case Calendar.YEAR:
        rollYear(theCalendar, up);
        break;
      default:
        logger.warn("(" + calendarField + ") is not a valid Calendar field!");
        throw new IllegalArgumentException("(" + calendarField + ") is not a valid Calendar field!");
    }

    setCalendar(theCalendar);
  }

  /**
   * Increments or decrements, according to the up boolean parameter, the day of month by 1 unit.
   * @param calendar the Calendar object who's day of month value will be modified.
   * @param up a boolean value determining whether to increment or decrement the day of month.
   */
  private void rollDay(final Calendar calendar, final boolean up) {
    if (up) {
      incrementDay(calendar);
    }
    else {
      decrementDay(calendar);
    }
  }

  /**
   * Increments or decrements, according to the up boolean parameter, the month of year by 1 unit.
   * @param calendar the Calendar object who's month value will be modified.
   * @param up a boolean value determining whether to increment or decrement the month.
   */
  private void rollMonth(final Calendar calendar, final boolean up) {
    if (up) {
      incrementMonth(calendar);
    }
    else {
      decrementMonth(calendar);
    }
  }

  /**
   * Increments or decrements, according to the up boolean parameter, the year by 1 unit.
   * @param calendar the Calendar object who's day of month value will be modified.
   * @param up a boolean value determining whether to increment or decrement the year.
   */
  private void rollYear(final Calendar calendar, final boolean up) {
    if (up) {
      incrementYear(calendar);
    }
    else {
      decrementYear(calendar);
    }
  }

  /**
   * Sets the date field, specified by the Calendar constant, to the corresponding value.
   * @param calendarField a Calendar constant referring to the date field to set with the value.
   * @param value an integer value for the day, month or year as determined by the Calendar
   * constant.
   * @throws java.lang.IllegalArgumentException if the calendarField is not a valid date field,
   * or Calendar constant.
   */
  public void set(final int calendarField, final int value) {
    if (logger.isDebugEnabled()) {
      logger.debug("calendarField = " + calendarField);
      logger.debug("value = " + value);
    }

    final Calendar theCalendar = getCalendar();

    switch (calendarField) {
      case Calendar.DAY_OF_MONTH:
        setDay(theCalendar, value);
        break;
      case Calendar.MONTH:
        setMonth(theCalendar, value);
        break;
      case Calendar.YEAR:
        setYear(theCalendar, value);
        break;
      default:
        logger.warn("(" + calendarField + ") is not a valid Calendar field!");
        throw new IllegalArgumentException("(" + calendarField + ") is not a valid Calendar field!");
    }

    setCalendar(theCalendar);
  }

  /**
   * Sets the day of month on the specified Calendar object to the corresponding value.
   * Note, this method is allowed to be overridden by subclasses to support calendar functionality.
   * @param calendar the Calendar object in which to set the day of month.
   * @param value an integer value for the day of month.
   */
  protected void setDay(final Calendar calendar, int value) {
    final int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
    final int currentMonth = calendar.get(Calendar.MONTH);
    final int currentYear = calendar.get(Calendar.YEAR);
    final int numDaysInMonth = NUMBER_OF_DAYS_IN_MONTH[currentMonth]
      + (isFebruary(currentMonth) && DateUtil.isLeapYear(currentYear) ? 1 : 0);
    final int newValue = currentDay * 10 + value;

    value = (value == 0 ? 1 : value);
    calendar.set(Calendar.DAY_OF_MONTH, (newValue <= numDaysInMonth ? newValue : value));
    setCurrentDay(calendar.get(Calendar.DAY_OF_MONTH));
  }

  /**
   * Sets the month on the specified Calendar object to the corresponding value.
   * @param calendar the Calendar object in which to set the month.
   * @param value an integer value for the month.  Valid values are between 1 and 12.
   */
  private void setMonth(final Calendar calendar, int value) {
    final int currentMonth = calendar.get(Calendar.MONTH);
    final int newValue = normalizeMonth(currentMonth) * 10 + value;

    // set day of month to the 1st so the Calendr does not roll if the current day of month is more than
    // the number of days in the new value for month
    value = (value == 0 ? 1 : value);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.MONTH, (newValue < 13 ? (newValue - 1) : (value - 1)));
    determineDay(calendar);
  }

  /**
   * Sets the year on the specified Calendar object to the corresponding value.
   * @param calendar the Calendar object in which to set the year.
   * @param value an integer value for the year.
   */
  private void setYear(final Calendar calendar, int value) {
    value %= 10; // ensure that the number key pressed does not exceed 9
    value = (value == 0 ? 1 : value);
    // set day of month to the 1st so the Calendr does not roll the month if the current day of month is more than
    // the number of days in the current mont for the new year
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.YEAR, value * 1000);
    determineDay(calendar);
  }

  /**
   * Returns a String representation of the DefaultDateModel class.
   * @return a String containing the state of this class.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{calendar = ");
    buffer.append(getCalendar());
    buffer.append(", currentDay = ").append(getCurrentDay());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
