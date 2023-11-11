/*
 * DateUtil.java (c) 21 October 2001
 *
 * This class contains date functions and operations for Calendar and Date objects.
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.12
 * @see java.text.DateFormat
 * @see java.text.SimpleDateFormat
 * @see java.util.Calendar
 * @see java.util.Date
 * @see java.util.Locale
 * @see java.util.TimeZone
 */

package com.cp.common.util;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class DateUtil {

  private static final Log logger = LogFactory.getLog(DateUtil.class);

  public static final int NUMBER_OF_DAYS_IN_FEBRUARY_OF_LEAP_YEAR = 29;
  public static final int NUMBER_OF_DAYS_IN_LEAP_YEAR = 366;
  public static final int NUMBER_OF_DAYS_IN_WEEK = 7;
  public static final int NUMBER_OF_DAYS_IN_YEAR = 365;
  public static final int NUMBER_OF_HOURS_IN_DAY = 24;
  public static final int NUMBER_OF_MINUTES_IN_HOUR = 60;
  public static final int NUMBER_OF_MONTHS_IN_YEAR = 12;
  public static final int NUMBER_OF_SECONDS_IN_MINUTE = 60;
  public static final int NUMBER_OF_WEEKS_IN_YEAR = 52;
  public static final int NUMBER_OF_YEARS_IN_CENTURY = 100;

  public static final int[] NUMBER_OF_DAYS_IN_MONTH = {
    31, // January
    28, // February (non-leap year)
    31, // March
    30, // April
    31, // May
    30, // June
    31, // July
    31, // August
    30, // September
    31, // October
    30, // November
    31 // December
  };

  public static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("MM/dd/yyyy h:mm a");
  public static final DateFormat DAY_MONTH_YEAR_FORMAT = new SimpleDateFormat("dd MMM yyyy");
  public static final DateFormat DAY_OF_WEEK_DATE_FORMAT = new SimpleDateFormat("EEE, MMM dd, yyyy");
  public static final DateFormat DAY_OF_WEEK_DATE_TIME_FORMAT = new SimpleDateFormat("EEE, MM dd, yyyy h:mm a");
  public static final DateFormat MONTH_YEAR = new SimpleDateFormat("MMMMM, yyyy");
  public static final DateFormat YEAR_MONTH_DAY_FORMAT = new SimpleDateFormat("yyyy.mm.dd");

  public static final String DEFAULT_DATE_FORMAT_PATTERN = "MM/dd/yyyy hh:mm:ss a";

  /**
   * Private constructor to enforce non-instantiability.
   */
  private DateUtil() {
  }

  /**
   * Gets an instance of the Calendar class with the milliseconds set to 0.
   * @return a Calendar object instance with the milliseconds set to 0.
   * @see DateUtil#getCalendarNoTimestamp()
   */
  public static Calendar getCalendar() {
    final Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar;
  }

  /**
   * Constructs a Calendar object from the milliseconds value.  Thus, the Calendar object will represent the time
   * of milliseconds.  The milliseconds is measured from the beginning of the epoch (Midnight on the 1st of January, 1970).
   * @param milliseconds the number of milliseconds since the epoch.
   * @return a Calendar object constructed from the milliseconds value.
   */
  public static Calendar getCalendar(final long milliseconds) {
    if (logger.isDebugEnabled()) {
      logger.debug("milliseconds (" + milliseconds + ")");
    }

    final Calendar calendar = Calendar.getInstance();
    calendar.clear();
    calendar.setTimeInMillis(milliseconds);
    return calendar;
  }

  /**
   * Converts a Date object to a Calendar object initialized to the proper time.
   * @param date the Date object to convert to a Calendar.
   * @return a Calendar object initialized to the time of the specified Date object.
   */
  public static Calendar getCalendar(final Date date) {
    if (logger.isDebugEnabled()) {
      logger.debug("date (" + date + ")");
    }

    if (ObjectUtil.isNull(date)) {
      return null;
    }
    else {
      final Calendar calendar = Calendar.getInstance();
      calendar.clear();
      calendar.setTimeInMillis(date.getTime());
      return calendar;
    }
  }

  /**
   * Gets a Calendar object for the specified date String value using the default date format pattern as defined by
   * this DateUtil class.
   * @param date the String value representing a date/time value.
   * @return a Calendar object from the parsed date/time String value using the default date format pattern.
   * @see DateUtil#getCalendar(String, String)
   */
  public static Calendar getCalendar(final String date) {
    return getCalendar(date, DEFAULT_DATE_FORMAT_PATTERN);
  }

  /**
   * Gets a Calendar object for the specified date String value using the specified date format pattern.
   * @param date the String value representing a date/time value.
   * @param dateFormatPattern the pattern used to parse the date/time String value.
   * @return a Calendar object from the parsed date/time String value using the specified date format pattern.
   * @see DateUtil#getCalendar(String)
   */
  public static Calendar getCalendar(final String date, String dateFormatPattern) {
    if (logger.isDebugEnabled()) {
      logger.debug("date (" + date + ")");
      logger.debug("date format pattern (" + dateFormatPattern + ")");
    }

    dateFormatPattern = ObjectUtil.getDefaultValue(dateFormatPattern, DEFAULT_DATE_FORMAT_PATTERN);

    try {
      return getCalendar(new SimpleDateFormat(dateFormatPattern).parse(date));
    }
    catch (ParseException e) {
      logger.error("The date (" + date + ") is not parseable using date format pattern (" + dateFormatPattern + ")!", e);
      throw new IllegalArgumentException("The date (" + date + ") is not parseable using date format pattern ("
        + dateFormatPattern + ")!", e);
    }
  }

  /**
   * Returns a Calendar object initialized to the specified date.  The values for time are zeroed out.
   * The Calendar is implicitly initialized to the default time zone and locale.
   * @param year the Calendar year (1801, 1950, 2000, 2001, 2002, etc).
   * @param month the Calenar month (January, February, March, etc).
   * @param dayOfMonth the day of the month (such as March 31st, or February 28th, etc).
   * @return the Calendar object initialized to the specified date.
   */
  public static Calendar getCalendar(final int year, final int month, final int dayOfMonth) {
    return getCalendar(year, month, dayOfMonth, 0, 0, 0, 0, Locale.getDefault(), TimeZone.getDefault());
  }

  /**
   * Returns a Calendar object initialized to the specified time.  The values for date are set to the
   * current date (or today). The Calendar is implicitly initialized to the default time zone and locale.
   * @param hourOfDay the hourOfDay of day (11 AM, or 1600 O'Clock, as in 4 PM, etc).
   * @param minute the minute of the hourOfDay (1..60).
   * @param second the second of the minute (1..60).
   * @param millisecond the millisecond of the second (1..1000).
   * @return the Calendar object initialized to the specified time.
   */
  public static Calendar getCalendar(final int hourOfDay, final int minute, final int second, final int millisecond) {
    final Calendar today = Calendar.getInstance();
    return getCalendar(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH),
      hourOfDay, minute, second, millisecond);
  }

  /**
   * Returns a Calendar object initialized to the specified date and time.  The Calendar is implicitly
   * initialized to the default time zone and locale.
   * @param year the Calendar year (1801, 1950, 2000, 2001, 2002, etc).
   * @param month the Calenar month (January, February, March, etc).
   * @param dayOfMonth the day of the month (such as March 31st, or February 28th, etc).
   * @param hourOfDay the hourOfDay of day (11 AM, or 1600 O'Clock, as in 4 PM, etc).
   * @param minute the minute of the hourOfDay (1..60).
   * @param second the second of the minute (1..60).
   * @param millisecond the millisecond of the second (1..1000).
   * @return the Calendar object initialized to the specified values for date and time.
   */
  public static Calendar getCalendar(final int year,
                                     final int month,
                                     final int dayOfMonth,
                                     final int hourOfDay,
                                     final int minute,
                                     final int second,
                                     final int millisecond) {
    return getCalendar(year, month, dayOfMonth, hourOfDay, minute, second, millisecond, Locale.getDefault(), TimeZone.getDefault());
  }

  /**
   * Returns a Calendar object initialized to the specified date, time, time zone and locale.
   * @param year the Calendar year (1801, 1950, 2000, 2001, 2002, etc).
   * @param month the Calenar month (January, February, March, etc).
   * @param dayOfMonth the day of the month (such as March 31st, or February 28th, etc).
   * @param hourOfDay the hourOfDay of day (11 AM, or 1600 O'Clock, as in 4 PM, etc).
   * @param minute the minute of the hourOfDay (1..60).
   * @param second the second of the minute (1..60).
   * @param millisecond the millisecond of the second (1..1000).
   * @param locale the specified locality, representing the specified geographical, political,
   * or cultural region.
   * @param timeZone the time zone of the Calendar (Central Time, Mountain Time, Pacific Time, GMT, etc).
   * @return the Calendar object initialized to the specified values for date, time, time zone and locale.
   */
  public static Calendar getCalendar(final int year,
                                     final int month,
                                     final int dayOfMonth,
                                     final int hourOfDay,
                                     final int minute,
                                     final int second,
                                     final int millisecond,
                                     final Locale locale,
                                     final TimeZone timeZone) {
    if (logger.isDebugEnabled()) {
      logger.debug("year (" + year + ")");
      logger.debug("month (" + month + ")");
      logger.debug("dayOfMonth (" + dayOfMonth + ")");
      logger.debug("hourOfDay (" + hourOfDay + ")");
      logger.debug("minute (" + minute + ")");
      logger.debug("second (" + second + ")");
      logger.debug("millisecond (" + millisecond + ")");
      logger.debug("locale (" + locale + ")");
      logger.debug("timeZone (" + timeZone + ")");
    }

    final Calendar calendar = Calendar.getInstance(locale);
    calendar.clear();
    calendar.setTimeZone(timeZone);
    calendar.set(year, month, dayOfMonth, hourOfDay, minute, second);
    calendar.set(Calendar.MILLISECOND, millisecond);
    calendar.set(Calendar.AM_PM, (hourOfDay > 12 ? Calendar.PM : Calendar.AM));
    return calendar;
  }

  /**
   * Creates a Comparator instance to compare two Calendar objects for ordering
   * by Year, Month, Day of Month, Hour, Minute, Second and finally, Millisecond.
   * @return a Comparator instance to compare two Calendar objects.
   */
  public static Comparator<Calendar> getCalendarComparator() {
    return new Comparator<Calendar>() {
      public int compare(final Calendar c1, final Calendar c2) {
        int compareValue = 0;

        if ((compareValue = diff(c1, c2, Calendar.YEAR)) != 0) {
          logger.debug("differ by year!");
          return compareValue;
        }
        else if ((compareValue = diff(c1, c2, Calendar.MONTH)) != 0) {
          logger.debug("differ by month!");
          return compareValue;
        }
        else if ((compareValue = diff(c1, c2, Calendar.DAY_OF_MONTH)) != 0) {
          logger.debug("differ by day of month!");
          return compareValue;
        }
        else if ((compareValue = diff(c1, c2, Calendar.HOUR)) != 0) {
          logger.debug("differ by hour!");
          return compareValue;
        }
        else if ((compareValue = diff(c1, c2, Calendar.MINUTE)) != 0) {
          logger.debug("differ by minute!");
          return compareValue;
        }
        else if ((compareValue = diff(c1, c2, Calendar.SECOND)) != 0) {
          logger.debug("differ by second!");
          return compareValue;
        }

        return diff(c1, c2, Calendar.MILLISECOND);
      }

      private int diff(final Calendar c1, final Calendar c2, final int calendarField) {
        return (c1.get(calendarField) - c2.get(calendarField));
      }

      public String toString() {
        return "DateUtil.CalendarComparator!";
      }
    };
  }

  /**
   * Gets the Calendar month constant for the specified month within the year numeric value.
   * @param month an integer value specifying the numeric month within the year.
   * @return a Calendar month constant for the numeric month within the year value.
   * @see DateUtil#getNormalizedMonth(int)
   * @see DateUtil#getCalendarMonthDescription(int)
   */
  public static int getCalendarMonth(final int month) {
    switch (month) {
      case 1:
        return Calendar.JANUARY;
      case 2:
        return Calendar.FEBRUARY;
      case 3:
        return Calendar.MARCH;
      case 4:
        return Calendar.APRIL;
      case 5:
        return Calendar.MAY;
      case 6:
        return Calendar.JUNE;
      case 7:
        return Calendar.JULY;
      case 8:
        return Calendar.AUGUST;
      case 9:
        return Calendar.SEPTEMBER;
      case 10:
        return Calendar.OCTOBER;
      case 11:
        return Calendar.NOVEMBER;
      case 12:
        return Calendar.DECEMBER;
      default:
        logger.warn("(" + month + ") is not a valid Calendar month!");
        throw new IllegalArgumentException("(" + month + ") is not a valid Calendar month!");
    }
  }

  /**
   * Gets a String representation of the specified Calendar month constant.
   * @param month an integer value specifying the Calendar month constant.
   * @return a String value name of the given Calendar month constant.
   * @see DateUtil#getNormalizedMonthDescription(int)
   */
  public static String getCalendarMonthDescription(final int month) {
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
        logger.warn("(" + month + ") is not a valid Calendar month constant!");
        throw new IllegalArgumentException("(" + month + ") is not a valid Calendar month constant!");
    }
  }

  /**
   * Gets a normalized Calendar instance set to today's date but with no timestamp.
   * @return a Calendar object with only the date fields set.
   * @see DateUtil#getCalendar()
   * @see DateUtil#truncate(java.util.Calendar)
   */
  public static Calendar getCalendarNoTimestamp() {
    return truncate(getCalendar());
  }

  /**
   * Returns a Calendar object denoting tomorrow (the day after today) at the same time.
   * @return a Calendar object denoting tomorrow.
   * @see DateUtil#getCalendar()
   * @see DateUtil#getYesterday()
   */
  public static Calendar getTomorrow() {
    final Calendar today = getCalendar();
    today.add(Calendar.DAY_OF_MONTH, 1);
    return today;
  }

  /**
   * Returns a Calendar object denoting yesterday (the day before today) at the same time.
   * @return a Calendar object denoting yesterday.
   * @see DateUtil#getCalendar()
   * @see DateUtil#getTomorrow()   
   */
  public static Calendar getYesterday() {
    final Calendar today = getCalendar();
    today.add(Calendar.DAY_OF_MONTH, -1);
    return today;
  }

  /**
   * Returns the difference between the specified Calendar date an now in number of days.
   * @param actualDate the target date.
   * @return an integer value specifying the difference between the specified Calendar date and now in number of days.
   * @see DateUtil#getDiffInDays(java.util.Calendar, java.util.Calendar)
   */
  public static int getDiffInDays(final Calendar actualDate) {
    return getDiffInDays(actualDate, Calendar.getInstance());
  }

  /**
   * Returns the difference between two Calendar dates in number of days.
   * @param actualDate the target date.
   * @param relativeDate the date relative to the target date.
   * @return an integer value specifying the difference between two Calendar dates in number of days.
   */
  public static int getDiffInDays(Calendar actualDate, Calendar relativeDate) {
    Assert.notNull(actualDate, "The actual date cannot be null!");
    Assert.notNull(relativeDate, "The relative date cannot be null!");

    if (logger.isDebugEnabled()) {
      logger.debug("actual date (" + toString(actualDate) + ")");
      logger.debug("relative date (" + toString(relativeDate) + ")");
    }

    actualDate = truncate(copy(actualDate));
    relativeDate = truncate(copy(relativeDate));

    if (actualDate.equals(relativeDate)) {
      return 0;
    }
    else {
      final Calendar startDate = (actualDate.before(relativeDate) ? actualDate : relativeDate);
      final Calendar endDate = (startDate == actualDate ? relativeDate : actualDate);

      int diff = 0;
      int multiplier = (endDate == actualDate ? 1 : -1);

      for ( ; startDate.before(endDate); startDate.add(Calendar.DAY_OF_MONTH, 1)) {
        diff++;
      }

      return (diff * multiplier);
    }
  }

  /**
   * Returns the difference between the specified Calendar date an now in number of months.
   * @param actualDate the target date.
   * @return an integer value specifying the difference between the specified Calendar date and now in number of months.
   * @see DateUtil#getDiffInMonths(java.util.Calendar, java.util.Calendar)
   */
  public static int getDiffInMonths(final Calendar actualDate) {
    return getDiffInMonths(actualDate, Calendar.getInstance());
  }

  /**
   * Returns the difference between two Calendar dates in number of months.
   * @param actualDate the target date.
   * @param relativeDate the date relative to the target date.
   * @return an integer value specifying the difference between two Calendar dates in number of months.
   */
  public static int getDiffInMonths(final Calendar actualDate, final Calendar relativeDate) {
    Assert.notNull(actualDate, "The actual date cannot be null!");
    Assert.notNull(relativeDate, "The relative date cannot be null!");

    if (logger.isDebugEnabled()) {
      logger.debug("actual date (" + toString(actualDate) + ")");
      logger.debug("relative date (" + toString(relativeDate) + ")");
    }

    final int multiplier = (actualDate.before(relativeDate) ? -1 : 1);
    final int actualDateMonth = getNormalizedMonth(actualDate.get(Calendar.MONTH));
    final int actualDateYear = actualDate.get(Calendar.YEAR);
    final int relativeDateMonth = getNormalizedMonth(relativeDate.get(Calendar.MONTH));
    final int relativeDateYear = relativeDate.get(Calendar.YEAR);

    return (multiplier * (Math.abs(actualDateYear - relativeDateYear) * NUMBER_OF_MONTHS_IN_YEAR
      + multiplier * (actualDateMonth - relativeDateMonth)));
  }

  /**
   * Returns the difference between the specified Calendar date an now in number of years.
   * @param actualDate the target date.
   * @return an integer value specifying the difference between the specified Calendar date and now in number of years.
   * @see DateUtil#getDiffInYears(java.util.Calendar, java.util.Calendar)
   */
  public static int getDiffInYears(final Calendar actualDate) {
    return getDiffInYears(actualDate, Calendar.getInstance());
  }

  /**
   * Returns the difference between two Calendar dates in number of years.
   * @param actualDate the target date.
   * @param relativeDate the date relative to the target date.
   * @return an integer value specifying the difference between two Calendar dates in number of years.
   */
  public static int getDiffInYears(final Calendar actualDate, final Calendar relativeDate) {
    Assert.notNull(actualDate, "The actual date cannot be null!");
    Assert.notNull(relativeDate, "The relative date cannot be null!");

    if (logger.isDebugEnabled()) {
      logger.debug("actual date (" + toString(actualDate) + ")");
      logger.debug("relative date (" + toString(relativeDate) + ")");
    }

    final Calendar startDate = (actualDate.before(relativeDate) ? actualDate : relativeDate);
    final Calendar endDate = (startDate == actualDate ? relativeDate : actualDate);

    final int multiplier = (endDate == actualDate ? 1 : -1);
    final int startDateYear = startDate.get(Calendar.YEAR);
    final int endDateYear = endDate.get(Calendar.YEAR);

    int diff = (endDateYear - startDateYear);

    if (diff > 0) {
      final int startDateMonth = getNormalizedMonth(startDate.get(Calendar.MONTH));
      final int endDateMonth = getNormalizedMonth(endDate.get(Calendar.MONTH));

      if (endDateMonth < startDateMonth) {
        diff--;
      }
      else if (endDateMonth == startDateMonth) {
        final int startDateDay = startDate.get(Calendar.DAY_OF_MONTH);
        final int endDateDay = endDate.get(Calendar.DAY_OF_MONTH);

        if (endDateDay < startDateDay) {
          diff--;
        }
      }
    }

    return (diff * multiplier);
  }

  /**
   * Gets the earlier of two Calendar objects.
   * @param comparableDate the Calendar object compared with the relative date.
   * @param relativeDate the Calendar object relative to the comparable date.
   * @return the earlier of the two Calendar objects, or return the other Calendar objects if one of
   * the Calendar objects is null.
   * @see DateUtil#getLaterDate(java.util.Calendar, java.util.Calendar)
   */
  public static Calendar getEarlierDate(final Calendar comparableDate, final Calendar relativeDate) {
    return (ObjectUtil.isNull(comparableDate) ? relativeDate : (ObjectUtil.isNull(relativeDate) ? comparableDate
      : (comparableDate.before(relativeDate) ? comparableDate : relativeDate)));
  }

  /**
   * Gets the earliest date in an array of Calendar objects.
   * @param dates an array of Calendar object used in determining the earliest date.
   * @return the earliest date in the array of Calendar objects, or return null if all Calendar objects
   * in the array are null.
   * @see DateUtil#getEarlierDate(java.util.Calendar, java.util.Calendar)
   */
  public static Calendar getEarliestDate(final Calendar... dates) {
    Calendar earliestDate = null;

    for (final Calendar date : dates) {
      earliestDate = getEarlierDate(earliestDate, date);
    }

    return earliestDate;
  }

  /**
   * Gets the later of two Calendar objects.
   * @param comparableDate the Calendar object compared with the relative date.
   * @param relativeDate the Calendar object relative to the comparable date.
   * @return the later of the two Calendar objects, or return the other Calendar objects if one of
   * the Calendar objects is null.
   * @see DateUtil#getEarlierDate(java.util.Calendar, java.util.Calendar)
   */
  public static Calendar getLaterDate(final Calendar comparableDate, final Calendar relativeDate) {
    return (ObjectUtil.isNull(comparableDate) ? relativeDate : (ObjectUtil.isNull(relativeDate) ? comparableDate
      : (comparableDate.after(relativeDate) ? comparableDate : relativeDate)));
  }

  /**
   * Gets the latest date in an array of Calendar objects.
   * @param dates an array of Calendar object used in determining the latest date.
   * @return the latest date in the array of Calendar objects, or return null if all Calendar objects
   * in the array are null.
   * @see DateUtil#getLaterDate(java.util.Calendar, java.util.Calendar)
   */
  public static Calendar getLatestDate(final Calendar... dates) {
    Calendar latestDate = null;

    for (final Calendar date : dates) {
      latestDate = getLaterDate(latestDate, date);
    }

    return latestDate;
  }

  /**
   * Returns the next month after the current month specified as a Calendar constant.
   * @param month an integer value specifying the current month.
   * @return a Calendar constant that represents the next month after the specified current month.
   */
  public static int getNextMonth(final int month) {
    switch (month) {
      case Calendar.JANUARY:
        return Calendar.FEBRUARY;
      case Calendar.FEBRUARY:
        return Calendar.MARCH;
      case Calendar.MARCH:
        return Calendar.APRIL;
      case Calendar.APRIL:
        return Calendar.MAY;
      case Calendar.MAY:
        return Calendar.JUNE;
      case Calendar.JUNE:
        return Calendar.JULY;
      case Calendar.JULY:
        return Calendar.AUGUST;
      case Calendar.AUGUST:
        return Calendar.SEPTEMBER;
      case Calendar.SEPTEMBER:
        return Calendar.OCTOBER;
      case Calendar.OCTOBER:
        return Calendar.NOVEMBER;
      case Calendar.NOVEMBER:
        return Calendar.DECEMBER;
      case Calendar.DECEMBER:
        return Calendar.JANUARY;
      default:
        logger.warn("(" + month + ") is not a valid Calendar month constant!");
        throw new IllegalArgumentException("(" + month + ") is not a valid Calendar month constant!");
    }
  }

  /**
   * Returns an integer value specifying the Calendar month (i.e. Calendar.JANUARY is 1, Calendar.MAY is 5 and so on).
   * @param month the Calendar month.
   * @return an numeric value specifying the month of year (1-12).
   * @throws IllegalArgumentException if the month parameter is not a valid Calendar month constant.
   * @see DateUtil#getCalendarMonth(int)
   * @see DateUtil#getNormalizedMonthDescription(int)
   */
  public static int getNormalizedMonth(final int month) {
    switch (month) {
      case Calendar.JANUARY:
        return 1;
      case Calendar.FEBRUARY:
        return 2;
      case Calendar.MARCH:
        return 3;
      case Calendar.APRIL:
        return 4;
      case Calendar.MAY:
        return 5;
      case Calendar.JUNE:
        return 6;
      case Calendar.JULY:
        return 7;
      case Calendar.AUGUST:
        return 8;
      case Calendar.SEPTEMBER:
        return 9;
      case Calendar.OCTOBER:
        return 10;
      case Calendar.NOVEMBER:
        return 11;
      case Calendar.DECEMBER:
        return 12;
      default:
        logger.warn("(" + month + ") is not a valid Calendar month constant!");
        throw new IllegalArgumentException("(" + month + ") is not a valid Calendar month constant!");
    }
  }

  /**
   * Gets a String representation of the specified Calendar month.
   * @param month an integer value specifying the Calendar month.
   * @return a String value name of the given Calendar month.
   * @see DateUtil#getCalendarMonthDescription(int)
   */
  public static String getNormalizedMonthDescription(final int month) {
    switch (month) {
      case 1:
        return "January";
      case 2:
        return "February";
      case 3:
        return "March";
      case 4:
        return "April";
      case 5:
        return "May";
      case 6:
        return "June";
      case 7:
        return "July";
      case 8:
        return "August";
      case 9:
        return "September";
      case 10:
        return "October";
      case 11:
        return "November";
      case 12:
        return "December";
      default:
        logger.warn("(" + month + ") is not a valid Calendar month!");
        throw new IllegalArgumentException("(" + month + ") is not a valid Calendar month!");
    }
  }

  /**
   * Gets the number of days in the specified Calendar month of the current Calendar year taking into account
   * February in leap years.
   * @param month an integer value specifying the Calendar month constant.
   * @return an integer value indicating the number of days in the specified month of the specified year
   * taking into account February in leap years.
   * @see DateUtil#getNumberOfDaysInMonth(int, int)
   */
  public static int getNumberOfDaysInMonth(final int month) {
    return getNumberOfDaysInMonth(month, Calendar.getInstance().get(Calendar.YEAR));
  }

  /**
   * Gets the number of days in the specified Calendar month of the specified Calendar year taking into account
   * February in leap years.
   * @param month an integer value specifying the Calendar month constant.
   * @param year an integer value specifying the Calendar year.
   * @return an integer value indicating the number of days in the specified month of the specified year
   * taking into account February in leap years.
   * @see DateUtil#getNormalizedMonth(int)
   */
  public static int getNumberOfDaysInMonth(final int month, final int year) {
    Assert.positive(getNormalizedMonth(month), "(" + month + ") is not a valid Calendar month constant!");
    Assert.greaterThanEqual(year, 0, "(" + year + ") is not a valid Calendar year!");

    return (month == Calendar.FEBRUARY && isLeapYear(year) ? NUMBER_OF_DAYS_IN_FEBRUARY_OF_LEAP_YEAR
      : NUMBER_OF_DAYS_IN_MONTH[month]);
  }

  /**
   * Returns the previous month before the current month specified as a Calendar constant.
   * @param month an integer value specifying the current month.
   * @return a Calendar constant that represents the previous month before the specified current month.
   */
  public static int getPreviousMonth(final int month) {
    switch (month) {
      case Calendar.JANUARY:
        return Calendar.DECEMBER;
      case Calendar.FEBRUARY:
        return Calendar.JANUARY;
      case Calendar.MARCH:
        return Calendar.FEBRUARY;
      case Calendar.APRIL:
        return Calendar.MARCH;
      case Calendar.MAY:
        return Calendar.APRIL;
      case Calendar.JUNE:
        return Calendar.MAY;
      case Calendar.JULY:
        return Calendar.JUNE;
      case Calendar.AUGUST:
        return Calendar.JULY;
      case Calendar.SEPTEMBER:
        return Calendar.AUGUST;
      case Calendar.OCTOBER:
        return Calendar.SEPTEMBER;
      case Calendar.NOVEMBER:
        return Calendar.OCTOBER;
      case Calendar.DECEMBER:
        return Calendar.NOVEMBER;
      default:
        logger.warn("(" + month + ") is not a valid Calendar month constant!");
        throw new IllegalArgumentException("(" + month + ") is not a valid Calendar month constant!");
    }
  }

  /**
   * Determines if the specified Calendar date is after the given relative Calendar date.  This method returns false
   * (is indeterministic) if either the Calendar date or relative Calendar date are null.
   * @param date the Calendar date that is the subject of the conditional check.
   * @param relativeDate the Calendar date relative to the Calendar date in determining whether the Calendar date comes
   * after the relative Calendar date.
   * @return a boolean value if the Calendar date is after the relative Calendar date.  If either the Calendar date or
   * the relative Calendar date is null, then this conditional check is indeterminant and the method returns false.
   * @see DateUtil#isOnOrAfter(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isBefore(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOnOrBefore(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isBetween(java.util.Calendar, java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOn(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOutside(java.util.Calendar, java.util.Calendar, java.util.Calendar)
   */
  public static boolean isAfter(final Calendar date, final Calendar relativeDate) {
    return (!ObjectUtil.isAnyNull(date, relativeDate) && date.after(relativeDate));
  }

  /**
   * Determines if the specified Calendar date is on or after the given relative Calendar date.  This method
   * returns false (is indeterministic) if either the Calendar date or relative Calendar date are null.
   * @param date the Calendar date that is the subject of the conditional check.
   * @param relativeDate the Calendar date relative to the Calendar date in determining whether the Calendar date comes
   * on or after the relative Calendar date.
   * @return a boolean value if the Calendar date is on or after the relative Calendar date.  If either the Calendar
   * date or the relative Calendar date is null, then this conditional check is indeterminant and the method returns
   * false.
   * @see DateUtil#isAfter(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isBefore(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOnOrBefore(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isBetween(java.util.Calendar, java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOn(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOutside(java.util.Calendar, java.util.Calendar, java.util.Calendar)
   */
  public static boolean isOnOrAfter(final Calendar date, final Calendar relativeDate) {
    return (!ObjectUtil.isAnyNull(date, relativeDate) && getCalendarComparator().compare(date, relativeDate) >= 0);
  }

  /**
   * Determines if the specified Calendar date is before the given relative Calendar date.  This method returns false
   * (is indeterministic) if either the Calendar date or relative Calendar date are null.
   * @param date the Calendar date that is the subject of the conditional check.
   * @param relativeDate the Calendar date relative to the Calendar date in determining whether the Calendar date comes
   * before the relative Calendar date.
   * @return a boolean value if the Calendar date is before the relative Calendar date.  If either the Calendar date or
   * the relative Calendar date is null, then this conditional check is indeterminant and the method returns false.
   * @see DateUtil#isAfter(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOnOrAfter(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOnOrBefore(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isBetween(java.util.Calendar, java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOn(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOutside(java.util.Calendar, java.util.Calendar, java.util.Calendar)
   */
  public static boolean isBefore(final Calendar date, final Calendar relativeDate) {
    return (!ObjectUtil.isAnyNull(date, relativeDate) && date.before(relativeDate));
  }

  /**
   * Determines if the specified Calendar date is on or before the given relative Calendar date.  This method
   * returns false (is indeterministic) if either the Calendar date or relative Calendar date are null.
   * @param date the Calendar date that is the subject of the conditional check.
   * @param relativeDate the Calendar date relative to the Calendar date in determining whether the Calendar date comes
   * on or before the relative Calendar date.
   * @return a boolean value if the Calendar date is on or before the relative Calendar date.  If either the Calendar
   * date or the relative Calendar date is null, then this conditional check is indeterminant and the method returns
   * false.
   * @see DateUtil#isAfter(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOnOrAfter(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isBefore(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isBetween(java.util.Calendar, java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOn(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOutside(java.util.Calendar, java.util.Calendar, java.util.Calendar)
   */
  public static boolean isOnOrBefore(final Calendar date, final Calendar relativeDate) {
    return (!ObjectUtil.isAnyNull(date, relativeDate) && getCalendarComparator().compare(date, relativeDate) <= 0);
  }

  /**
   * Determines whether the specified Calendar date is between the given begin and end Calendar dates.  This method
   * returns false (is indeterministic) if either the Calendar date or the begin and end Calendar dates are null.
   * @param date the Calendar date that is the subject of the conditional check.
   * @param beginDate a Calendar object specifying the start date of the time period.
   * @param endDate a Calendar object specifying the end date of the time period.
   * @return a boolean value indicating whether the specified Calendar object is between the given begin
   * and end Calendar dates.
   * @see DateUtil#isAfter(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOnOrAfter(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isBefore(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOnOrBefore(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOn(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOutside(java.util.Calendar, java.util.Calendar, java.util.Calendar)
   */
  public static boolean isBetween(final Calendar date, final Calendar beginDate, final Calendar endDate) {
    return (!ObjectUtil.isAnyNull(date, beginDate, endDate) && isOnOrAfter(date, beginDate) && isOnOrBefore(date, endDate));
  }

  /**
   * Determines whether the specified Calendar date is equal to another given Calendar date.  This method returns false
   * (is indeterministic) if either the Calendar date or the given Calendar date are null.
   * @param date the Calendar date that is the subject of the equality comparison.
   * @param givenDate the Calendar date used in determining whether the specified date is the given Calendar date.
   * @return a boolean value indicating whether the specified Calendar date is equal to the given Calendar date.
   * @see DateUtil#isAfter(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOnOrAfter(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isBefore(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOnOrBefore(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isBetween(java.util.Calendar, java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOutside(java.util.Calendar, java.util.Calendar, java.util.Calendar)
   */
  public static boolean isOn(final Calendar date, final Calendar givenDate) {
    return (!ObjectUtil.isAnyNull(date, givenDate) && date.equals(givenDate));
  }

  /**
   * Determines whether the specified Calendar date falls outside the given minimum and maximum Calendar dates.
   * This method returns false (is indeterministic) if either the Calendar date or the min and max Calendar dates
   * are null.
   * @param date the Calendar date that is the subject of the conditional check.
   * @param minDate a Calendar object specifying the minimum date of the time period.
   * @param maxDate a Calendar object specifying the maximum date of the time period.
   * @return a boolean value indicating whether the specified Calendar object falls outside the given minimum
   * and maximum Calendar dates.
   * @see DateUtil#isAfter(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOnOrAfter(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isBefore(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOnOrBefore(java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isBetween(java.util.Calendar, java.util.Calendar, java.util.Calendar)
   * @see DateUtil#isOn(java.util.Calendar, java.util.Calendar)
   */
  public static boolean isOutside(final Calendar date, final Calendar minDate, final Calendar maxDate) {
    return (!ObjectUtil.isAnyNull(date, minDate, maxDate) && (isBefore(date, minDate) || isAfter(date, maxDate)));
  }

  /**
   * Determines whether the specified date (year, month and day of week) is at the first day of daylight saving time (DST).
   * @param date the Calendar object being determined whether the date value is the beginning of day light savings time.
   * @return a boolean value indicating if the date value represented by the Calendar object is the beginning of
   * day light savings time.
   */
  public static boolean isDaylightSavingTimeBegin(final Calendar date) {
    Assert.notNull(date, "The date cannot be null!");

    if (date.get(Calendar.YEAR) < 2007) {
      if (date.get(Calendar.MONTH) == Calendar.APRIL) {
        if (date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
          return (date.get(Calendar.DAY_OF_MONTH) < 8);
        }
      }
    }
    else {
      if (date.get(Calendar.MONTH) == Calendar.MARCH) {
        if (date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
          return (date.get(Calendar.DAY_OF_MONTH) < 15);
        }
      }
    }

    return false;
  }

  /**
   * Determines whether the specified date (year, month and day of week) is at the last day of daylight saving time (DST).
   * @param date the Calendar object being determined whether the date value is the end of day light savings time.
   * @return a boolean value indicating if the date value represented by the Calendar object is the end of
   * day light savings time.
   */
  public static boolean isDaylightSavingTimeEnd(final Calendar date) {
    Assert.notNull(date, "The date cannot be null!");

    if (date.get(Calendar.YEAR) < 2007) {
      if (date.get(Calendar.MONTH) == Calendar.OCTOBER) {
        if (date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
          return (date.get(Calendar.DAY_OF_MONTH) > 24);
        }
      }
    }
    else {
      if (date.get(Calendar.MONTH) == Calendar.NOVEMBER) {
        if (date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
          return (date.get(Calendar.DAY_OF_MONTH) < 8);
        }
      }
    }

    return false;
  }

  /**
   * Determines whether the specified date/time is a future date, meaning after the current date/time.
   * @param date the Calendar object specifying the date/time in question of being a future date.
   * @return a boolean value indicating if the Calendar object represents a future date and time.
   * @see DateUtil#isPastDate(java.util.Calendar)
   * @see DateUtil#isPresentDate(java.util.Calendar)
   */
  public static boolean isFutureDate(final Calendar date) {
    final Calendar now = Calendar.getInstance();
    return (ObjectUtil.isNotNull(date) && date.after(now));
  }

  /**
   * Determines if the specified year is a leap year.
   * @param year the year specified as an integer value.
   * @return a boolean value indicating if the specified year is a leap year.
   */
  public static boolean isLeapYear(final int year) {
    return (year % 4 == 0 && year % 100 != 0 || year % 400 == 0);
  }

  /**
   * Determines whether the specified date/time is a past date, meaning after the current date/time.
   * @param date the Calendar object specifying the date/time in question of being a past date.
   * @return a boolean value indicating if the Calendar object represents a past date and time.
   * @see DateUtil#isFutureDate(java.util.Calendar)
   * @see DateUtil#isPresentDate(java.util.Calendar)
   */
  public static boolean isPastDate(final Calendar date) {
    final Calendar now = Calendar.getInstance();
    return (ObjectUtil.isNotNull(date) && date.before(now));
  }

  /**
   * Determines whether the specified date/time is the present date, meaning equal to the current date/time.
   * @param date the Calendar object specifying the date/time in question of being a present date.
   * @return a boolean value indicating if the Calendar object represents the present date and time.
   * @see DateUtil#isFutureDate(java.util.Calendar)
   * @see DateUtil#isPastDate(java.util.Calendar)
   */
  public static boolean isPresentDate(final Calendar date) {
    final Calendar now = truncate(Calendar.getInstance());
    return (ObjectUtil.isNotNull(date) && now.equals(truncate(date)));
  }

  /**
   * Determines if the specified month is January.
   * @param month the month specified as an integer constant of the Calendar class.
   * @return a boolean value indicating if the specified month is January.
   * @see java.util.Calendar
   */
  public static boolean isJanuary(final int month) {
    return (month == Calendar.JANUARY);
  }

  /**
   * Determines if the specified month is February.
   * @param month the month specified as an integer constant of the Calendar class.
   * @return a boolean value indicating if the specified month is February.
   * @see java.util.Calendar
   */
  public static boolean isFebruary(final int month) {
    return (month == Calendar.FEBRUARY);
  }

  /**
   * Determines if the specified month is March.
   * @param month the month specified as an integer constant of the Calendar class.
   * @return a boolean value indicating if the specified month is March.
   * @see java.util.Calendar
   */
  public static boolean isMarch(final int month) {
    return (month == Calendar.MARCH);
  }

  /**
   * Determines if the specified month is April.
   * @param month the month specified as an integer constant of the Calendar class.
   * @return a boolean value indicating if the specified month is April.
   * @see java.util.Calendar
   */
  public static boolean isApril(final int month) {
    return (month == Calendar.APRIL);
  }

  /**
   * Determines if the specified month is May.
   * @param month the month specified as an integer constant of the Calendar class.
   * @return a boolean value indicating if the specified month is May.
   * @see java.util.Calendar
   */
  public static boolean isMay(final int month) {
    return (month == Calendar.MAY);
  }

  /**
   * Determines if the specified month is June.
   * @param month the month specified as an integer constant of the Calendar class.
   * @return a boolean value indicating if the specified month is June.
   * @see java.util.Calendar
   */
  public static boolean isJune(final int month) {
    return (month == Calendar.JUNE);
  }

  /**
   * Determines if the specified month is July.
   * @param month the month specified as an integer constant of the Calendar class.
   * @return a boolean value indicating if the specified month is July.
   * @see java.util.Calendar
   */
  public static boolean isJuly(final int month) {
    return (month == Calendar.JULY);
  }

  /**
   * Determines if the specified month is August.
   * @param month the month specified as an integer constant of the Calendar class.
   * @return a boolean value indicating if the specified month is August.
   * @see java.util.Calendar
   */
  public static boolean isAugust(final int month) {
    return (month == Calendar.AUGUST);
  }

  /**
   * Determines if the specified month is September.
   * @param month the month specified as an integer constant of the Calendar class.
   * @return a boolean value indicating if the specified month is September.
   * @see java.util.Calendar
   */
  public static boolean isSeptember(final int month) {
    return (month == Calendar.SEPTEMBER);
  }

  /**
   * Determines if the specified month is October.
   * @param month the month specified as an integer constant of the Calendar class.
   * @return a boolean value indicating if the specified month is October.
   * @see java.util.Calendar
   */
  public static boolean isOctober(final int month) {
    return (month == Calendar.OCTOBER);
  }

  /**
   * Determines if the specified month is November.
   * @param month the month specified as an integer constant of the Calendar class.
   * @return a boolean value indicating if the specified month is November.
   * @see java.util.Calendar
   */
  public static boolean isNovember(final int month) {
    return (month == Calendar.NOVEMBER);
  }

  /**
   * Determines if the specified month is December.
   * @param month the month specified as an integer constant of the Calendar class.
   * @return a boolean value indicating if the specified month is December.
   * @see java.util.Calendar
   */
  public static boolean isDecember(final int month) {
    return (month == Calendar.DECEMBER);
  }

  /**
   * Determines if the specified day of week is a Sunday.
   * @param dayOfWeek the week day specified as a integer constant of the Calendar class.
   * @return a boolean value indicating if the specified week day is Sunday.
   * @see java.util.Calendar
   */
  public static boolean isSunday(final int dayOfWeek) {
    return (dayOfWeek == Calendar.SUNDAY);
  }

  /**
   * Determines if the specified day of week is a Monday.
   * @param dayOfWeek the week day specified as a integer constant of the Calendar class.
   * @return a boolean value indicating if the specified week day is Monday.
   * @see java.util.Calendar
   */
  public static boolean isMonday(final int dayOfWeek) {
    return (dayOfWeek == Calendar.MONDAY);
  }

  /**
   * Determines if the specified day of week is a Tuesday.
   * @param dayOfWeek the week day specified as a integer constant of the Calendar class.
   * @return a boolean value indicating if the specified week day is Tuesday.
   * @see java.util.Calendar
   */
  public static boolean isTuesday(final int dayOfWeek) {
    return (dayOfWeek == Calendar.TUESDAY);
  }

  /**
   * Determines if the specified day of week is a Wednesday.
   * @param dayOfWeek the week day specified as a integer constant of the Calendar class.
   * @return a boolean value indicating if the specified week day is Wednesday.
   * @see java.util.Calendar
   */
  public static boolean isWednesday(final int dayOfWeek) {
    return (dayOfWeek == Calendar.WEDNESDAY);
  }

  /**
   * Determines if the specified day of week is a Thursday.
   * @param dayOfWeek the week day specified as a integer constant of the Calendar class.
   * @return a boolean value indicating if the specified week day is Thursday.
   * @see java.util.Calendar
   */
  public static boolean isThursday(final int dayOfWeek) {
    return (dayOfWeek == Calendar.THURSDAY);
  }

  /**
   * Determines if the specified day of week is a Friday.
   * @param dayOfWeek the week day specified as a integer constant of the Calendar class.
   * @return a boolean value indicating if the specified week day is Friday.
   * @see java.util.Calendar
   */
  public static boolean isFriday(final int dayOfWeek) {
    return (dayOfWeek == Calendar.FRIDAY);
  }

  /**
   * Determines if the specified day of week is a Saturday.
   * @param dayOfWeek the week day specified as a integer constant of the Calendar class.
   * @return a boolean value indicating if the specified week day is Saturday.
   * @see java.util.Calendar
   */
  public static boolean isSaturday(final int dayOfWeek) {
    return (dayOfWeek == Calendar.SATURDAY);
  }

  /**
   * Determines whether the specified day of week is a week day.
   * @param dayOfWeek the day of week specified as a integer constant of the Calendar class.
   * @return a boolean value indicating if the specified day of week a week day.
   * @see java.util.Calendar
   */
  public static boolean isWeekday(final int dayOfWeek) {
    switch (dayOfWeek) {
      case Calendar.MONDAY:
      case Calendar.TUESDAY:
      case Calendar.WEDNESDAY:
      case Calendar.THURSDAY:
      case Calendar.FRIDAY:
        return true;
      default:
        return false;
    }
  }

  /**
   * Determines whether the specified day of week is a weekend.
   * @param dayOfWeek the day of week specified as a integer constant of the Calendar class.
   * @return a boolean value indicating if the specified day of week a weekend.
   * @see java.util.Calendar
   */
  public static boolean isWeekend(final int dayOfWeek) {
    switch (dayOfWeek) {
      case Calendar.SUNDAY:
      case Calendar.SATURDAY:
        return true;
      default:
        return false;
    }
  }

  /**
   * Makes a defensive copy of the specified Calendar object.  The returned Calendar contains the exact same time
   * (contents), but refers to a different Calendar object in memory.
   * @param calendar the Calenar object to clone.
   * @return a Calendar object copied from the specified Calendar.
   * @see DateUtil#getCalendar
   * @see DateUtil#copy(java.util.Calendar, int...)
   */
  public static Calendar copy(final Calendar calendar) {
    if (logger.isDebugEnabled()) {
      logger.debug("calendar (" + calendar + ")");
    }

    if (ObjectUtil.isNull(calendar)) {
      return null;
    }
    else {
      return getCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
        calendar.get(Calendar.MILLISECOND), Locale.getDefault(), calendar.getTimeZone());
    }
  }

  /**
   * Copies specific Calendar fields of the specified Calendar object to a new Calendar object.
   * @param calendar the Calenar object to copy information from.
   * @param calendarFields is an integer array specify specific Calendar fields to copy from the Calendar object
   * parameter to the Calendar object copy.
   * @return a Calendar object copied from the specified Calendar.
   * @see DateUtil#copy(java.util.Calendar)
   */
  public static Calendar copy(final Calendar calendar, final int... calendarFields) {
    if (logger.isDebugEnabled()) {
      logger.debug("calendar (" + calendar + ")");
    }

    if (ObjectUtil.isNull(calendar)) {
      return null;
    }
    else {
      final Calendar copy = Calendar.getInstance(calendar.getTimeZone(), Locale.getDefault());

      copy.clear();

      for (final int calendarField : calendarFields) {
        copy.set(calendarField, calendar.get(calendarField));
      }

      return copy;
    }
  }

  /**
   * Sets the given Calendar to the first day of the current month based on the time values of the individual
   * Calendar fields of the specified Calendar object.
   * @param date the Calendar object who's day of month will be set to the first day of the current month.
   * @return the specified Calendar object with the day of month set to the first day of the current month.
   * @see DateUtil#setToLastDayOfMonth(java.util.Calendar)
   */
  public static Calendar setToFirstDayOfMonth(final Calendar date) {
    Assert.notNull(date, "The date cannot be null!");
    date.set(Calendar.DAY_OF_MONTH, 1);
    return date;
  }

  /**
   * Sets the given Calendar to the last day of the current month based on the time values of the individual
   * Calendar fields of the specified Calendar object.
   * @param date the Calendar object who's day of month will be set to the last day of the current month.
   * @return the specified Calendar object with the day of month set to the last day of the current month.
   * @see DateUtil#setToFirstDayOfMonth(java.util.Calendar)
   */
  public static Calendar setToLastDayOfMonth(final Calendar date) {
    Assert.notNull(date, "The date cannot be null!");
    date.set(Calendar.DAY_OF_MONTH, date.getActualMaximum(Calendar.DAY_OF_MONTH));
    return date;
  }

  /**
   * Returns a String representation of the Calendar object using the default pattern "MM/dd/yyyy hh:mm:ss a".
   * @param date the Calendar object to convert to a String.
   * @return a String representation of the Calendar object, or null if the Calendar object is null.
   */
  public static String toString(final Calendar date) {
    return toString(date, DEFAULT_DATE_FORMAT_PATTERN);
  }

  /**
   * Returns a String representation of the Calendar object using the specified pattern.
   * @param date the Calendar object to convert to a String.
   * @param pattern the specified date format pattern used to convert the Calendar object to a String, or the
   * default pattern if the pattern parameter is null.
   * @return a String representation of the Calendar object, or null if the Calendar object is null.
   * @see java.text.SimpleDateFormat
   */
  public static String toString(final Calendar date, String pattern) {
    pattern = (StringUtil.isEmpty(pattern) ? DEFAULT_DATE_FORMAT_PATTERN : pattern);
    return (ObjectUtil.isNull(date) ? null : new SimpleDateFormat(pattern).format(date.getTime()));
  }

  /**
   * Truncates the time portion of the specified Calendar object leaving only the date portion intact.
   * @param date the Calendar object used to truncate the time.
   * @return the Calendar object with the time portion (hour of day, minute, second and milliseond)
   * of the date/timestamp truncated.
   */
  public static Calendar truncate(final Calendar date) {
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);
    return date;
  }

}
