/*
 * Timespan.java (c) 5 April 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2009.5.6
 * @see com.cp.common.util.DateUtil
 * @see java.util.Calendar
 */

package com.cp.common.util;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import java.util.Calendar;

public final class Timespan {

  private final Calendar beginDate;
  private final Calendar endDate;

  /**
   * Constructs an instance of the Timespan class initialized with the specified begin and end dates.
   * @param beginDate a Calendar object specifying the begin date/time.
   * @param endDate a Calendar object speicfying the end date/time.
   */
  public Timespan(final Calendar beginDate, final Calendar endDate) {
    if (!ObjectUtil.isAnyNull(beginDate, endDate)) {
      Assert.isTrue(DateUtil.isOnOrAfter(endDate, beginDate), "The end date (" + DateUtil.toString(endDate)
        + " must come on or after the begin date (" + DateUtil.toString(beginDate) + "!");
    }

    this.beginDate = DateUtil.copy(beginDate);
    this.endDate = DateUtil.copy(endDate);
  }

  /**
   * Factory method for creating an instance of the Timespan class initialized with the specified begin and end dates.
   * @param beginDate a Calendar object specifying the begin date/time.
   * @param endDate a Calendar object speicfying the end date/time.
   * @return an instance of the Timespan class initialized with the specified begin and end dates.
   */
  public static Timespan getTimespan(final Calendar beginDate, final Calendar endDate) {
    return new Timespan(beginDate, endDate);
  }

  /**
   * Gets the begin date of the Timespan.
   * @return a Calendar object denoting the begin date/time of the Timespan.
   * @see Timespan#getEndDate()
   */
  public Calendar getBeginDate() {
    return DateUtil.copy(beginDate);
  }

  /**
   * Determines whether this Timespan has a beginning, meaning a non-null, begin date/time value.
   * @return a boolean value indicating whether this Timespan has a non-null, begin date/time value.
   * @see Timespan#hasEnding()
   */
  public boolean hasBeginning() {
    return ObjectUtil.isNotNull(getBeginDate());
  }

  /**
   * Determines whether this Timespan has already begun, meaning the current date/time is after the Timespan's
   * begin date.
   * @return a boolean value indicating whether this Timespan has started.
   * @see Timespan#hasEnded()
   */
  public boolean hasBegun() {
    return (!hasBeginning() || Calendar.getInstance().after(getBeginDate()));
  }

  /**
   * Gets the begin date of the Timespan.
   * @return a Calendar object denoting the begin date/time of the Timespan.
   * @see Timespan#getBeginDate()
   */
  public Calendar getEndDate() {
    return DateUtil.copy(endDate);
  }

  /**
   * Determines whether this Timespan has already ended, meaning the current date/time is before the Timespan's
   * end date.
   * @return a boolean value indicating whether this Timespan has ended.
   * @see Timespan#hasBegun()
   */
  public boolean hasEnded() {
    return (hasEnding() && Calendar.getInstance().after(getEndDate()));
  }

  /**
   * Determines whether this Timespan has an ending, meaning a non-null, end date/time value.
   * @return a boolean value indicating whether this Timespan has a non-null, end date/time value.
   * @see Timespan#hasBeginning()
   */
  public boolean hasEnding() {
    return ObjectUtil.isNotNull(getEndDate());
  }

  /**
   * Determines whether the specified date and time occur within (or during) this span of time.
   * @param date a Calendar object specifying the date and time value.
   * @return a boolean value indicating whether the specified date and time occurs within (or during)
   * this span of time.
   */
  public boolean isDuring(final Calendar date) {
    Assert.notNull(date, "The date cannot be null!");
    return ((ObjectUtil.isNull(getBeginDate()) || date.after(getBeginDate()))
      && (ObjectUtil.isNull(getEndDate()) || date.before(getEndDate())));
  }

  /**
   * Determines whether this Timespan is ongoing into the future, meaning have a null end date/time value.
   * @return a boolean value indicating if the Timespan is ongoing by having a null end date/time value.
   */
  public boolean isOngoing() {
    return !hasEnding();
  }

  /**
   * Determines whether this Timespan is open ended having both a null begin and end date/time values.
   * @return a boolean value indicating if the Timespan is open ended by having both a null begin and end
   * date/time values.
   */
  public boolean isOpenEnded() {
    return (!hasBeginning() && isOngoing());
  }

  /**
   * Determines whether the specified timespan overlaps with this span of time.
   * @param timespan a Timespan representing a begin and end date and time.
   * @return a boolena value indicating whether the given timespan overlaps within this span of time.
   */
  public boolean isOverlapping(final Timespan timespan) {
    Assert.notNull(timespan, "The timespan cannot be null!");
    return (ObjectUtil.isNull(getEndDate()) || ObjectUtil.isNull(timespan.getBeginDate())
          || timespan.getBeginDate().before(getEndDate()))
      && (ObjectUtil.isNull(getBeginDate()) || ObjectUtil.isNull(timespan.getEndDate())
          || timespan.getEndDate().after(getBeginDate()));
  }

  /**
   * Determines whether the specified Object value is equal to this Timespan.
   * @param obj an arbitrary Object value used in the equality operation.
   * @return a boolean value indicating whether another Object is logical equal to this Timespan.
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof Timespan)) {
      return false;
    }

    final Timespan that = (Timespan) obj;

    return ObjectUtil.equals(getBeginDate(), that.getBeginDate())
      && ObjectUtil.equals(getEndDate(), that.getEndDate());
  }

  /**
   * Calculates the hash value for this Timespan
   * @return an integer value representing the calculated hash value of this Timespan.
   */
  @Override
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getBeginDate());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getEndDate());
    return hashValue;
  }

  /**
   * Returns a String describing this span of time.
   * @return a String value describing this span of time.
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{beginDate = ");
    buffer.append(DateUtil.toString(getBeginDate()));
    buffer.append(", endDate = ").append(DateUtil.toString(getEndDate()));
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
