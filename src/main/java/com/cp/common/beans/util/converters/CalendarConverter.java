/*
 * CalendarConverter.java (c) 27 May 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.10.7
 * @see com.cp.common.beans.util.converters.AbstractConverter
 * @see com.cp.common.util.ConversionException
 * @see com.cp.common.util.DateUtil
 * @see java.text.SimpleDateFormat
 */

package com.cp.common.beans.util.converters;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.ConversionException;
import com.cp.common.util.DateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

public class CalendarConverter extends AbstractConverter<Calendar> {

  private static final Set<String> USER_DATE_FORMAT_PATTERNS = new TreeSet<String>();

  protected static final String[] DEFAULT_DATE_FORMAT_PATTERNS =
  {
    "MM/dd/yyyy hh:mm:ss.S a",
    "MM/dd/yyyy hh:mm:ss a",
    "MM/dd/yyyy hh:mm a",
    "MM/dd/yyyy",
    "MMMMM, yyyyy"
  };

  /**
   * Instantiates an instance of the CalendarConverter class.
   */
  public CalendarConverter() {
  }

  /**
   * Instantiates an instance of the CalendarConverter class initialized with the default Calendar value on null values
   * during conversion.
   * @param defaultValue the default Calendar value used during conversion of null values.
   */
  public CalendarConverter(final Calendar defaultValue) {
    super(defaultValue);
  }

  /**
   * Instantiates an instance of the CalendarConverter class initialized with a boolean condition indicating whether
   * a default Calendar value should be used when converting null values.
   * @param useDefaultValue a boolean condition indicating whether a default Calendar value should be used when
   * converting null values.
   */
  public CalendarConverter(final boolean useDefaultValue) {
    super(useDefaultValue);
  }

  /**
   * Adds a specified, custom user-defined date format pattern to a set of date format patterns used
   * to convert object values to Calendar objects.
   * @param dateFormatPattern a String value specifying the date format pattern used to parse the Object value
   * and create an instance of Calendar.
   * @return a boolean value indicating if the custom user-defined date format pattern was added to the set of
   * date format patterns successfully.
   */
  public static boolean addDateFormatPattern(final String dateFormatPattern) {
    return (StringUtil.isNotEmpty(dateFormatPattern) && USER_DATE_FORMAT_PATTERNS.add(dateFormatPattern));
  }

  /**
   * Gets a collection of date format patterns used by this Converter to convert object values into an
   * instance of Calendar.
   * @return a collection of strings specifying date format patterns used by this Converter to parse and
   * convert object values into Calendars.
   */
  public static Collection<String> getDateFormatPatterns() {
    final Collection<String> dateFormatPatternCollection = new LinkedList<String>(USER_DATE_FORMAT_PATTERNS);
    dateFormatPatternCollection.addAll(Arrays.asList(DEFAULT_DATE_FORMAT_PATTERNS));
    return dateFormatPatternCollection;
  }

  /**
   * Removes the specified, custom user-defined date format pattern from the set of date format patterns used
   * to convert object values to Calendar objects.
   * @param dateFormatPattern a String value specifying the date format pattern used to parse the Object value
   * and create an instance of Calendar.
   * @return a boolean value indicating if the custom user-defined date format pattern was remove from the set of
   * date format patterns successfully.
   */
  public static boolean removeDateFormatPattern(final String dateFormatPattern) {
    return USER_DATE_FORMAT_PATTERNS.remove(dateFormatPattern);
  }

  /**
   * Converts the specified Object value into a Calendar object.
   * @param type the Calendar class.
   * @param value the Object value to convert into a Calendar.
   * @return a Calendar object for the specified Object value.
   * @throws ConversionException if the specified Object value cannot be converted into a Calendar.
   */
  protected Object convertImpl(final Class type, final Object value) {
    if (ObjectUtil.isNotNull(value)) {
      if (value instanceof Calendar) {
        return value;
      }
      else if (value instanceof Date) {
        return DateUtil.getCalendar(((Date) value).getTime());
      }
      else if (value instanceof Number) {
        return DateUtil.getCalendar(((Number) value).longValue());
      }
      else if (value instanceof String) {
        final String stringValue = value.toString().trim();

        if (StringUtil.isDigitsOnly(stringValue)) {
          return DateUtil.getCalendar(Long.parseLong(stringValue));
        }
        else {
          return getCalendar(stringValue);
        }
      }
      else {
        logger.warn("Failed to convert (" + value + ") of type (" + value.getClass().getName()
          + ") to a Calendar object!");
        throw new ConversionException("Failed to convert (" + value + ") of type (" + value.getClass().getName()
          + ") to a Calendar object!");
      }
    }

    return null;
  }

  /**
   * Gets a Calendar object for the specified String value representing a date/time value.
   * @param stringValue a String containing a date and time value.
   * @return a Calendar object for the specified String value representing a date/time value.
   * @throws ConversionException if the specified String value cannot be parsed using one of the date format patterns
   * into a Calendar object.
   */
  protected Calendar getCalendar(final String stringValue) {
    final SimpleDateFormat dateFormat = new SimpleDateFormat();

    for (final String dateFormatPattern : getDateFormatPatterns()) {
      try {
        dateFormat.applyPattern(dateFormatPattern);
        return DateUtil.getCalendar(dateFormat.parse(stringValue).getTime());
      }
      catch (ParseException e) {
        logger.warn("Failed to parse String value (" + stringValue + ") with date format pattern ("
          + dateFormatPattern + ")!");
      }
    }

    logger.warn("The String value (" + stringValue + ") cannot be converted to a Calendar object!");
    throw new ConversionException("The String value (" + stringValue + ") cannot be converted to a Calendar object!");
  }

}
