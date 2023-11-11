/*
 * DataModel (c) 16 September 2004
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.6
 * @see com.cp.common.swing.DefaultDateModel
 * @see com.cp.common.swing.JTimeField.DataModel
 * @see java.util.Calendar
 * @see java.util.Date
 */

package com.cp.common.swing;

import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;

final class DefaultTimeModel extends DefaultDateModel implements JTimeField.DataModel {

  private static final Logger logger = Logger.getLogger(DefaultTimeModel.class);

  /**
   * Creates an instance of the DataModel class to model time.
   */
  DefaultTimeModel() {
  }

  /**
   * Creates an instance of the DataModel class to model time initialized to the specified Calendar object.
   * @param calendar a Calendar object specifying the time to initialize the model.
   */
  DefaultTimeModel(final Calendar calendar) {
    super(calendar);
  }

  /**
   * Creates an instance of the DataModel class to model time initialized to the specified Date object.
   * @param date a Date object specifying the time to initialize the model.
   */
  DefaultTimeModel(final Date date) {
    super(date);
  }

  /**
   * Decrements the hour of day by 1 unit.
   * @param calendar the Calendar object in which the hour of day will be adjusted.
   */
  protected final void decrementHour(final Calendar calendar) {
    final int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
    if (currentHour > 0) {
      calendar.roll(Calendar.HOUR_OF_DAY, false);
    }
    else {
      decrementDay(calendar);
      calendar.set(Calendar.HOUR_OF_DAY, 23);
    }
  }

  /**
   * Decrements the minute in the hour by 1 unit.
   * @param calendar the Calendar object in which the minute of the hour will be adjusted.
   */
  protected final void decrementMinute(final Calendar calendar) {
    final int currentMinute = calendar.get(Calendar.MINUTE);
    if (currentMinute > 0) {
      calendar.roll(Calendar.MINUTE, false);
    }
    else {
      decrementHour(calendar);
      calendar.set(Calendar.MINUTE, 59);
    }
  }

  /**
   * Decrements the second in a minute by 1 unit.
   * @param calendar the Calendar object in which the second in the minute will be adjusted.
   */
  protected final void decrementSecond(final Calendar calendar) {
    final int currentSecond = calendar.get(Calendar.SECOND);
    if (currentSecond > 0) {
      calendar.roll(Calendar.SECOND, false);
    }
    else {
      decrementMinute(calendar);
      calendar.set(Calendar.SECOND, 59);
    }
  }

  /**
   * Returns the time from the Calendar object represented by this DataModel class.
   * @return a Calendar signifying the time for this time model.
   */
  public Calendar getTime() {
    return getCalendar();
  }

  /**
   * Sets the time property to the specified Calendar object containing time information.
   * @param time the Calendar object used to keep track of the current time.
   */
  public void setTime(final Calendar time) {
    setCalendar(time);
  }

  /**
   * Increments the hour in a day by 1 unit.
   * @param calendar the Calendar object in which the hour of day will be adjusted.
   */
  protected final void incrementHour(final Calendar calendar) {
    final int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
    if (currentHour < 23) {
      calendar.roll(Calendar.HOUR_OF_DAY, true);
    }
    else {
      incrementDay(calendar);
      calendar.set(Calendar.HOUR_OF_DAY, 0);
    }
  }

  /**
   * Increments the minute in an hour by 1 unit.
   * @param calendar the Calendar object in which the minute in an hour will be adjusted.
   */
  protected final void incrementMinute(final Calendar calendar) {
    final int currentMinute = calendar.get(Calendar.MINUTE);
    if (currentMinute < 59) {
      calendar.roll(Calendar.MINUTE, true);
    }
    else {
      incrementHour(calendar);
      calendar.set(Calendar.MINUTE, 0);
    }
  }

  /**
   * Increments the second in a minute by 1 unit.
   * @param calendar the Calendar object in which the second in a minute will be adjusted.
   */
  protected final void incrementSecond(final Calendar calendar) {
    final int currentSecond = calendar.get(Calendar.SECOND);
    if (currentSecond < 59) {
      calendar.roll(Calendar.SECOND, true);
    }
    else {
      incrementMinute(calendar);
      calendar.set(Calendar.SECOND, 0);
    }
  }

  /**
   * Determines whether the specified hour is valid.
   * @param hour an integer value specifying the hour.
   * @return a boolean value indicating if the hour is valid.  Returns true if 0 <= hour < 24,
   * false otherwise.
   */
  public static boolean isValidHour(final int hour) {
    logger.debug("hour = " + hour);
    return (hour >= 0 && hour < 24);
  }

  /**
   * Determines whether the specified minute is valid.
   * @param minute an integer value specifying the minute.
   * @return a boolean value indicating if the minute is valid.  Returns true if 0 <= minute < 60,
   * false otherwise.
   */
  public static boolean isValidMinute(final int minute) {
    logger.debug("minute = " + minute);
    return (minute >= 0 && minute < 60);
  }

  /**
   * Determines whether the specified second is valid.
   * @param second an integer value specifying the second.
   * @return a boolean value indicating if the second is valid.  Returns true if 0 <= second < 60,
   * false otherwise.
   */
  public static boolean isValidSecond(final int second) {
    logger.debug("second = " + second);
    return (second >= 0 && second < 60);
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

    final Calendar calendar = getCalendar();

    switch (calendarField) {
      case Calendar.HOUR_OF_DAY:
        rollHour(calendar, up);
        break;
      case Calendar.MINUTE:
        rollMinute(calendar, up);
        break;
      case Calendar.SECOND:
        rollSecond(calendar, up);
        break;
      default:
        super.roll(calendarField, up);
    }

    setCalendar(calendar);
  }

  /**
   * Increments or decrements, according to the up boolean parameter, the hour of day by 1 unit.
   * @param calendar the Calendar object who's hour of day value will be modified.
   * @param up a boolean value determining whether to increment or decrement the hour of day.
   */
  private void rollHour(final Calendar calendar, final boolean up) {
    if (up) {
      incrementHour(calendar);
    }
    else {
      decrementHour(calendar);
    }
  }

  /**
   * Increments or decrements, according to the up boolean parameter, the minute in an hour by 1 unit.
   * @param calendar the Calendar object who's minute in an hour value will be modified.
   * @param up a boolean value determining whether to increment or decrement the minute in an hour.
   */
  private void rollMinute(final Calendar calendar, final boolean up) {
    if (up) {
      incrementMinute(calendar);
    }
    else {
      decrementMinute(calendar);
    }
  }

  /**
   * Increments or decrements, according to the up boolean parameter, the second in a minute by 1 unit.
   * @param calendar the Calendar object who's second in a minute value will be modified.
   * @param up a boolean value determining whether to increment or decrement the second in a minute.
   */
  private void rollSecond(final Calendar calendar, final boolean up) {
    if (up) {
      incrementSecond(calendar);
    }
    else {
      decrementSecond(calendar);
    }
  }

  public void toggleAmPm() {
    final Calendar calendar = getCalendar();
    if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
      calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 12);
      calendar.set(Calendar.AM_PM, Calendar.PM);
    }
    else {
      calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 12);
      calendar.set(Calendar.AM_PM, Calendar.AM);
    }
    setCalendar(calendar);
  }

  /**
   * Sets the date field, specified by the Calendar constant, to the corresponding value.
   * @param calendarField a Calendar constant referring to the date field to set with the value.
   * @param value an integer value for the second, minute, hour, day, month or year as determined
   * by the Calendar constant.
   * @throws java.lang.IllegalArgumentException if the calendarField is not a valid date field,
   * or Calendar constant.
   */
  public void set(final int calendarField, final int value) {
    if (logger.isDebugEnabled()) {
      logger.debug("calendarField = " + calendarField);
      logger.debug("value = " + value);
    }

    final Calendar calendar = getCalendar();

    switch (calendarField) {
      case Calendar.HOUR_OF_DAY:
        setHour(calendar, value);
        break;
      case Calendar.MINUTE:
        setMinute(calendar, value);
        break;
      case Calendar.SECOND:
        setSecond(calendar, value);
        break;
      default:
        super.set(calendarField, value);
    }

    setCalendar(calendar);
  }

  /**
   * Sets the hour of day on the specified Calendar object to the corresponding value.
   * @param calendar the Calendar object in which to set the hour of day.
   * @param value an integer value for the hour of day.
   */
  private void setHour(final Calendar calendar, int value) {
    if (isValidHour(value)) {
      int currentHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
      currentHourOfDay = currentHourOfDay * 10 + value;

      if (isValidHour(currentHourOfDay)) {
        value = currentHourOfDay;
      }

      calendar.set(Calendar.HOUR_OF_DAY, value);
    }
    else {
      logger.warn(value + " is not a valid hour!");
      throw new IllegalArgumentException(value + " is not a valid hour!");
    }
  }

  /**
   * Sets the minute in an hour on the specified Calendar object to the corresponding value.
   * @param calendar the Calendar object in which to set the minute in an hour.
   * @param value an integer value for the minute in an hour.
   */
  private void setMinute(final Calendar calendar, int value) {
    if (isValidMinute(value)) {
      int currentMinute = calendar.get(Calendar.MINUTE);
      currentMinute = currentMinute * 10 + value;

      if (isValidMinute(currentMinute)) {
        value = currentMinute;
      }

      calendar.set(Calendar.MINUTE, value);
    }
    else {
      logger.warn(value + " is not a valid minute!");
      throw new IllegalArgumentException(value + " is not a valid minute!");
    }
  }

  /**
   * Sets the second in a minute on the specified Calendar object to the corresponding value.
   * @param calendar the Calendar object in which to set the second in a minute.
   * @param value an integer value for the second in a minute.
   */
  private void setSecond(final Calendar calendar, int value) {
    if (isValidSecond(value)) {
      int currentSecond = calendar.get(Calendar.SECOND);
      currentSecond = currentSecond * 10 + value;

      if (isValidSecond(currentSecond)) {
        value = currentSecond;
      }

      calendar.set(Calendar.SECOND, value);
    }
    else {
      logger.warn(value + " is not a valid second!");
      throw new IllegalArgumentException(value + " is not a valid second!");
    }
  }

}
