/*
 * CalendarRenderer.java (c) 21 November 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.11
 * @see com.cp.common.lang.ObjectUtil
 * @see com.cp.common.log4j.AbstractObjectRenderer
 * @see java.text.DateFormat
 * @see java.util.Calendar
 */

package com.cp.common.log4j;

import com.cp.common.lang.ObjectUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarRenderer extends AbstractObjectRenderer {

  public static final String DEFAULT_DATE_FORMAT_PATTERN = "MM/dd/yyyy hh:mm:ss a";

  private String dateFormatPattern = DEFAULT_DATE_FORMAT_PATTERN;

  /**
   * Creates an instance of the CalendarRenderer class to render Calendar objects in a Log4J context.
   */
  public CalendarRenderer() {
  }

  /**
   * Creates an instance of the CalendarRenderer class initialized with the specified date format to
   * renderer Calendar objects in a Log4J context.
   * @param dateFormatPattern the String pattern used to format the Calendar.
   */
  public CalendarRenderer(final String dateFormatPattern) {
    setDateFormatPattern(dateFormatPattern);
  }

  /**
   * Returns the date format pattern used to render a Calendar object.
   * @return a String pattern specifying the date format to render a Calendar object.
   */
  public String getDateFormatPattern() {
    return dateFormatPattern;
  }

  /**
   * Sets the date format pattern used to render a Calendar object.
   * @param dateFormatPattern a String pattern specifying the date format to render a Calendar object.
   */
  public void setDateFormatPattern(final String dateFormatPattern) {
    this.dateFormatPattern = (ObjectUtil.isNotNull(dateFormatPattern) ? dateFormatPattern
      : DEFAULT_DATE_FORMAT_PATTERN);
  }

  /**
   * Renders Objects of type Calendar using the specified dateFormatPattern.
   * @param obj the Calendar object to render.
   * @return a String representation of the Calendar rendered using the specified dateFormatPattern.
   */
  public String doRender(final Object obj) {
    if (obj instanceof Calendar) {
      final Calendar date = (Calendar) obj;
      final DateFormat dateFormat = new SimpleDateFormat(getDateFormatPattern());
      return dateFormat.format(date.getTime());
    }

    return getRenderer(obj).doRender(obj);
  }

}
