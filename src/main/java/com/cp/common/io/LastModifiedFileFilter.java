/*
 * LastModifiedFileFilter.java (c) 17 April 2002
 *
 * The FileModificationDateFilter is a FileFilter subclass used for filtering files based on time,
 * whether a File was last modified on a specified date, after a specified date, before a specified
 * date, or between a begin and end date.
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.12
 * @see com.cp.common.io.AbstractFileFilter
 * @see com.cp.common.uilt.DateUtil
 * @see com.cp.common.util.Filter
 */

package com.cp.common.io;

import com.cp.common.lang.Assert;
import com.cp.common.util.DateUtil;
import com.cp.common.util.Filter;
import java.io.File;
import java.util.Calendar;

public class LastModifiedFileFilter extends AbstractFileFilter {

  private final Filter<Calendar> filter;

  /**
   * Constructs an intance of the LastModifiedFileFilter class initialized with the specified Filter object
   * used to filter File objects based on their lastModified (timestamp) property.
   * The LastModifiedFileFilter class also contains several common factory methods for filtering File objects based on
   * on certain periods of time and other date value constraints.
   * @param filter the Filter object used to filter File objects based on the file's lastModified (timestamp) property.
   */
  public LastModifiedFileFilter(final Filter<Calendar> filter) {
    Assert.notNull(filter, "The filter cannot be null!");
    this.filter = filter;
  }

  /**
   * Factory method used to create a LastModifiedFileFilter instance that filters (includes) files whos' lastModified
   * property value is after the specified Calendar date.
   * @param afterDate a Calendar object specifying the date/time that a File object's lastModified property value must
   * fall after.
   * @return a LastModifiedFileFilter instance to filter File objects based on their lastModified date/time
   * property value.
   */
  public static LastModifiedFileFilter getAfterInstance(final Calendar afterDate) {
    return new LastModifiedFileFilter(new AfterFilter(afterDate));
  }

  /**
   * Factory method used to create a LastModifiedFileFilter instance that filters (includes) files whos' lastModified
   * property value is before the specified Calendar date.
   * @param beforeDate a Calendar object specifying the date/time that a File object's lastModified property value must
   * fall before.
   * @return a LastModifiedFileFilter instance to filter File objects based on their lastModified date/time
   * property value.
   */
  public static LastModifiedFileFilter getBeforeInstance(final Calendar beforeDate) {
    return new LastModifiedFileFilter(new BeforeFilter(beforeDate));
  }

  /**
   * Factory method used to create a LastModifiedFileFilter instance that filters (includes) files whos' lastModified
   * property value is between the specified Calendar begin and end dates.
   * @param beginDate a Calendar object specifying the date/time that a File object's lastModified property value must
   * fall on or after.
   * @param endDate a Calendar object specifying the date/time that a File object's lastModified property value must
   * fall on or before.
   * @return a LastModifiedFileFilter instance to filter File objects based on their lastModified date/time
   * property value.
   */
  public static LastModifiedFileFilter getBetweenInstance(final Calendar beginDate, final Calendar endDate) {
    return new LastModifiedFileFilter(new BetweenFilter(beginDate, endDate));
  }

  /**
   * Factory method used to create a LastModifiedFileFilter instance that filters (includes) files whos' lastModified
   * property value is on the specified Calendar date.
   * @param onDate a Calendar object specifying the date/time that a File object's lastModified property value must
   * fall on.
   * @return a LastModifiedFileFilter instance to filter File objects based on their lastModified date/time
   * property value.
   */
  public static LastModifiedFileFilter getOnInstance(final Calendar onDate) {
    return new LastModifiedFileFilter(new OnFilter(onDate));
  }

  /**
   * Factory method used to create a LastModifiedFileFilter instance that filters (includes) files whos' lastModified
   * property value is outside the specified Calendar min and max dates.
   * @param minDate a Calendar object specifying the date/time that a File object's lastModified property value must
   * fall below.
   * @param maxDate a Calendar object specifying the date/time that a File object's lastModified property value must
   * fall above.
   * @return a LastModifiedFileFilter instance to filter File objects based on their lastModified date/time
   * property value.
   */
  public static LastModifiedFileFilter getOutsideInstance(final Calendar minDate, final Calendar maxDate) {
    return new LastModifiedFileFilter(new OutsideFilter(minDate, maxDate));
  }

  /**
   * Gets the Filter object used by the LastModifiedFileFilter instance to filter File objects based on time periods
   * and other date value constraints of the file's lastModified property.
   * @return a Filter object used to filter File objects based on the lastModified (timestamp) property.
   */
  public Filter<Calendar> getFilter() {
    return filter;
  }

  /**
   * Determines whether the specified File object meets the filter criteria of this FileFilter.  This FileFilter filters
   * files based on their lastModified (timestamp) property value.
   * @param pathname the File object being evaluated by this FileFilter object.
   * @return a boolean value indicating whether the specified File meets the filter criteria of this FileFilter.
   */
  public boolean accept(final File pathname) {
    return getFilter().accept(DateUtil.getCalendar(pathname.lastModified()));
  }

  /**
   * Returns a String containing the state of this FileFilter instance.
   * @return a String representation of this FilterFilter instance.
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{filter = ");
    buffer.append(getFilter());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

  /**
   * A Filter specifying the file should have been last modified after the specified Calendar date.
   */
  private static final class AfterFilter implements Filter<Calendar> {

    private final Calendar afterDate;

    public AfterFilter(final Calendar afterDate) {
      Assert.notNull(afterDate, "The after date cannot be null!");
      this.afterDate = DateUtil.copy(afterDate);
    }

    public boolean accept(final Calendar fileDate) {
      return DateUtil.isAfter(fileDate, afterDate);
    }

    @Override
    public String toString() {
      return "Filter files last modified after (" + DateUtil.toString(afterDate) + ")";
    }
  }

  /**
   * A Filter specifying the file should have been last modified before the specified Calendar date.
   */
  private static final class BeforeFilter implements Filter<Calendar> {

    private final Calendar beforeDate;

    public BeforeFilter(final Calendar beforeDate) {
      Assert.notNull(beforeDate, "The before date cannot be null!");
      this.beforeDate = DateUtil.copy(beforeDate);
    }

    public boolean accept(final Calendar fileDate) {
      return DateUtil.isBefore(fileDate, beforeDate);
    }

    @Override
    public String toString() {
      return "Filter files last modified before (" + DateUtil.toString(beforeDate) + ")";
    }
  }

  /**
   * A Filter specifying the file should have been last modified between the specified begin and end Calendar dates.
   */
  private static final class BetweenFilter implements Filter<Calendar> {

    private final Calendar beginDate;
    private final Calendar endDate;

    public BetweenFilter(final Calendar beginDate, final Calendar endDate) {
      Assert.notNull(beginDate, "The begin date cannot be null!");
      Assert.notNull(endDate, "The end date cannot be null!");
      this.beginDate = DateUtil.copy(beginDate);
      this.endDate = DateUtil.copy(endDate);
    }

    public boolean accept(final Calendar fileDate) {
      return DateUtil.isBetween(fileDate, beginDate, endDate);
    }

    @Override
    public String toString() {
      return "Filter files last modified between (" + DateUtil.toString(beginDate)
        + ") and (" + DateUtil.toString(endDate) + ")";
    }
  }

  /**
   * A Filter specifying the file should have been last modified on the specified Calendar date.
   */
  private static final class OnFilter implements Filter<Calendar> {

    private static final int[] CALENDAR_FIELDS = {
      Calendar.YEAR,
      Calendar.MONTH,
      Calendar.DAY_OF_MONTH,
      Calendar.HOUR_OF_DAY,
      Calendar.MINUTE,
      Calendar.SECOND,
      Calendar.MILLISECOND
    };

    private final Calendar onDate;

    public OnFilter(final Calendar onDate) {
      Assert.notNull(onDate, "The on date cannot be null!");
      this.onDate = DateUtil.copy(onDate);
    }

    public boolean accept(final Calendar fileDate) {
      return DateUtil.isOn(truncateFileDate(fileDate), onDate);
    }

    @Override
    public String toString() {
      return "Filter files last modified on (" + DateUtil.toString(onDate) + ")";
    }

    public Calendar truncateFileDate(final Calendar fileDate) {
      for (final int calendarField : CALENDAR_FIELDS) {
        if (onDate.get(calendarField) == 0) {
          fileDate.set(calendarField, 0);
        }
      }

      return fileDate;
    }
  }

  /**
   * A Filter specifying the file should have been last modified outside of the specified minimum and maximum
   * Calendar dates.
   */
  private static final class OutsideFilter implements Filter<Calendar> {

    private final Calendar minDate;
    private final Calendar maxDate;

    public OutsideFilter(final Calendar minDate, final Calendar maxDate) {
      Assert.notNull(minDate, "The minimum date cannot be null!");
      Assert.notNull(maxDate, "The maximum date cannot be null!");
      this.minDate = minDate;
      this.maxDate = maxDate;
    }

    public boolean accept(final Calendar fileDate) {
      return DateUtil.isOutside(fileDate, minDate, maxDate);
    }

    @Override
    public String toString() {
      return "Filter files last modified outside of (" + DateUtil.toString(minDate)
        + ") and (" + DateUtil.toString(maxDate) + ")";
    }
  }

}
