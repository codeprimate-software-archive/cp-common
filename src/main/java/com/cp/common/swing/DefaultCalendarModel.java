/*
 * DefaultCalendarModel.java (c) 30 March 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.6
 * @see com.cp.common.lang.NumberUtil
 * @see com.cp.common.lang.ObjectUtil
 * @see com.cp.common.swing.CalendarModel
 * @see com.cp.common.swing.DefaultDateModel
 * @see com.cp.common.util.DateUtil
 * @see java.util.Calendar
 */

package com.cp.common.swing;

import com.cp.common.lang.NumberUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.DateUtil;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

final class DefaultCalendarModel extends DefaultDateModel implements CalendarModel {

  private static final Log logger = LogFactory.getLog(DefaultCalendarModel.class);

  private static final int[] DAYS_OF_THE_WEEK = {
    Calendar.SUNDAY,
    Calendar.MONDAY,
    Calendar.TUESDAY,
    Calendar.WEDNESDAY,
    Calendar.THURSDAY,
    Calendar.FRIDAY,
    Calendar.SATURDAY
  };

  private static final int[] MONTHS_OF_THE_YEAR = {
    Calendar.JANUARY,
    Calendar.FEBRUARY,
    Calendar.MARCH,
    Calendar.APRIL,
    Calendar.MAY,
    Calendar.JUNE,
    Calendar.JULY,
    Calendar.AUGUST,
    Calendar.SEPTEMBER,
    Calendar.OCTOBER,
    Calendar.NOVEMBER,
    Calendar.DECEMBER
  };

  private boolean monthYearChanged = false;

  private int theMonth = 0;
  private int theYear = 0;

  /**
   * Creates a default instance of the DefaultCalendarModel class initialized to the current date and time.
   */
  DefaultCalendarModel() {
    this(Calendar.getInstance());
  }

  /**
   * Creates an instance of the DefaultCalendarModel class initialized to the specified date and time.
   * @param calendar a Calendar object specifying the date and time used to initialize this instance of the DefaultCalendarModel.
   */
  DefaultCalendarModel(final Calendar calendar) {
    super(calendar);
    init();
  }

  /**
   * Stores the current month and year state of the DefaultCalendarModel, called by the constructor to initialize the
   * monthYearChanged model property.
   */
  private void init() {
    final Calendar currentCalendar = getCalendar();
    if (ObjectUtil.isNotNull(currentCalendar)) {
      theMonth = currentCalendar.get(Calendar.MONTH);
      theYear = currentCalendar.get(Calendar.YEAR);
    }
  }

  /**
   * Overridden DefaultDateModel.setCalendar method used to update the monthYearChanged property value of this model upon
   * calendar changes.
   * @param calendar the Calendar object represented by this DefaultCalendarModel, used to set the calendar property.
   */
  public final void setCalendar(final Calendar calendar) {
    super.setCalendar(calendar);
    updateMonthYearChange();
  }

  /**
   * Overridden DefaultDateModel.setDay method to set the current day of the month on the calendar and implement the desired
   * behavior for the JCalendar Swing UI component.
   * @param calendar the Calendar object in which to set the day of month.
   * @param value an integer value for the day of month.
   */
  protected final void setDay(final Calendar calendar, final int value) {
    calendar.set(Calendar.DAY_OF_MONTH, value);
    setCurrentDay(calendar.get(Calendar.DAY_OF_MONTH));
  }

  /**
   * Returns a week day name for the given day of week specified as a Calendar constant (for example, Calendar.FRIDAY).
   * @param weekDay an integer value Calendar constant specifying the day of week.
   * @return a String value description for the day of week.
   */
  private String getDayOfWeekDescription(final int weekDay) {
    switch (weekDay) {
      case Calendar.MONDAY:
        return "Monday";
      case Calendar.TUESDAY:
        return "Tuesday";
      case Calendar.WEDNESDAY:
        return "Wednesday";
      case Calendar.THURSDAY:
        return "Thursday";
      case Calendar.FRIDAY:
        return "Friday";
      case Calendar.SATURDAY:
        return "Saturday";
      default:
        return "Unknown";
    }
  }

  /**
   * Returns a integer value specifying the index for the day of the week.
   * @param dayOfWeek is a Calendar constant indicating the week day (for example, Calendar.FRIDAY).
   * @return a integer value specifying the index for the week day.
   */
  private int getDayOfWeekIndex(final int dayOfWeek) {
    for (int index = 0; index < DAYS_OF_THE_WEEK.length; index++) {
      if (DAYS_OF_THE_WEEK[index] == dayOfWeek) {
        return index;
      }
    }
    logger.warn("(" + getDayOfWeekDescription(dayOfWeek) + ") is not valid week day!");
    throw new IllegalArgumentException("(" + getDayOfWeekDescription(dayOfWeek) + ") is not valid week day!");
  }

  /**
   * Returns the name for the numerical month, which is based on the Calendar constants.
   * @param month an zero-based integer value specified as a Calendar constant referring to a particular month.
   * @return a String value description or name for the specified numerical month.
   */
  private String getMonthDescription(final int month) {
    if (logger.isDebugEnabled()) {
      logger.debug("month (" + month + ")");
    }
    switch (month) {
      case Calendar.JANUARY:
        return "January";
      case Calendar.FEBRUARY:
        return "February";
      case Calendar.MARCH:
        return "March";
      case Calendar.APRIL:
        return "April";
      case Calendar.MAY:
        return "May";
      case Calendar.JUNE:
        return "June";
      case Calendar.JULY:
        return "July";
      case Calendar.AUGUST:
        return "August";
      case Calendar.SEPTEMBER:
        return "September";
      case Calendar.OCTOBER:
        return "October";
      case Calendar.NOVEMBER:
        return "November";
      case Calendar.DECEMBER:
        return "December";
      default:
        return "Unknown";
    }
  }

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
  public int getFirstDayOfMonth(final int dayOfMonth, final int dayOfWeek) {
    if (logger.isDebugEnabled()) {
      logger.debug("dayOfMonth (" + dayOfMonth + ")");
      logger.debug("dayOfWeek (" + getDayOfWeekDescription(dayOfWeek) + ")");
    }

    // optimization note... if it is the first of the month, just return the day of the week.  note, however, that
    // first of the month determination algorithm would still come up with day of the week.
    if (dayOfMonth == 1) {
      return dayOfWeek;
    }

    final int difference = ((dayOfMonth % 7) - 1);

    if (logger.isDebugEnabled()) {
      logger.debug("difference (" + difference + ")");
    }

    int firstDayOfMonthIndex = (getDayOfWeekIndex(dayOfWeek) - difference);

    if (NumberUtil.isNegative(firstDayOfMonthIndex)) {
      firstDayOfMonthIndex += 7;
    }
    else if (firstDayOfMonthIndex == 7) {
      firstDayOfMonthIndex = 0;
    }

    if (logger.isDebugEnabled()) {
      logger.debug("firstDayOfMonthIndex (" + firstDayOfMonthIndex + ")");
    }

    return DAYS_OF_THE_WEEK[firstDayOfMonthIndex];
  }

  /**
   * Peforms a destructive read of the monthYearChanged property value.  This property value indicates if either the
   * month or the year has changed since the last calendar property update, the last read operation on this property,
   * which ever happened last.  If just the day of the calendar changes, then the value of the property will be false.
   * @return a boolean value indicating whether the month or year changed since the last read on this property value.
   */
  public final boolean getMonthYearChanged() {
    final boolean oldMonthYearChanged = isMonthYearChanged();
    setMonthYearChanged(false);
    return oldMonthYearChanged;
  }

  /**
   * Reads the current value of the monthYearChanged property.  This property value indicates if either the
   * month or the year has changed since the last calendar property update.  If just the day of the calendar changes,
   * then the value of the property will be false.
   * @return a boolean value indicating whether the month or year changed since the calendar update.
   */
  public final boolean isMonthYearChanged() {
    return monthYearChanged;
  }

  /**
   * Sets the value of the monthYearChanged property upon calendar change events.
   * @param monthYearChanged a boolean value indicating the value of the monthYearChanged property.
   */
  private void setMonthYearChanged(final boolean monthYearChanged) {
    this.monthYearChanged = monthYearChanged;
  }

  /**
   * Returns the number of days in the specified month, expressed as a Calendar constant, for the current year.
   * @param month a Calendar constant (for example, Calendar.MAY) specifying the month of the current year.
   * @return an integer value specifying the number of days in the specified month for the current year.
   */
  public int getNumberOfDaysInMonth(final int month) {
    return getNumberOfDaysInMonth(month, Calendar.getInstance().get(Calendar.YEAR));
  }

  /**
   * Returns the number of days for the specified month, expressed as a Calendar constant, for the specified year.
   * @param month a Calendar constant (for example, Calendar.MAY) specifying the month of the specified year.
   * @param year an integer value specifying the year used in determining the number of days in the specified month.
   * Note, year only matters for February in leap years.
   * @return an integer value specifying the number of days in the specified month of the specified year.
   */
  public int getNumberOfDaysInMonth(int month, final int year) {
    if (logger.isDebugEnabled()) {
      logger.debug("month (" + getMonthDescription(month) + ")");
      logger.debug("year (" + year + ")");
    }

    // normalize the specified Calendar constanct month value
    month = DateUtil.getNormalizedMonth(month);

    if (logger.isDebugEnabled()) {
      logger.debug("numeric month (" + month + ")");
    }

    int numberOfDaysInMonth = ((Integer) NUMBER_OF_DAYS_IN_MONTH_LIST.get(month - 1)).intValue();
    numberOfDaysInMonth = (isFebruary(month - 1) && DateUtil.isLeapYear(year) ? numberOfDaysInMonth + 1 : numberOfDaysInMonth);

    if (logger.isDebugEnabled()) {
      logger.debug("number of days in month (" + month + ") for year (" + year + ") is (" + numberOfDaysInMonth + ")");
    }

    return numberOfDaysInMonth;
  }

  /**
   * Decrements the calendar's month.
   */
  public void decrementMonth() {
    roll(Calendar.MONTH, false);
  }

  /**
   * Decrements the calendar's year.
   */
  public void decrementYear() {
    roll(Calendar.YEAR, false);
  }

  /**
   * Increments the calendar's month.
   */
  public void incrementMonth() {
    roll(Calendar.MONTH, true);
  }

  /**
   * Increments the calendar's year.
   */
  public void incrementYear() {
    roll(Calendar.YEAR, true);
  }

  /**
   * Determines the value of the monthYearChanged property value.
   */
  private void updateMonthYearChange() {
    final Calendar currentCalendar = getCalendar();
    int currentMonth = 0;
    int currentYear = 0;

    if (ObjectUtil.isNotNull(currentCalendar)) {
      currentMonth = currentCalendar.get(Calendar.MONTH);
      currentYear = currentCalendar.get(Calendar.YEAR);
    }

    setMonthYearChanged((theMonth != currentMonth) || (theYear != currentYear));

    theMonth = currentMonth;
    theYear = currentYear;
  }

}
