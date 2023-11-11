/*
 * DateOutOfBoundsVetoableChangeListener.java (c) 12 June 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.14
 * @see com.cp.common.beans.event.AbstractVetoableChangeListener
 * @see com.cp.common.beans.event.BoundedDateVetoableChangeListener
 * @see com.cp.common.beans.event.IllegalUseOfListenerException
 * @see com.cp.common.util.DateUtil
 * @see com.cp.common.util.Filter
 * @see java.util.Calendar
 */

package com.cp.common.beans.event;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.DateUtil;
import com.cp.common.util.Filter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.Calendar;

public class DateOutOfBoundsVetoableChangeListener extends AbstractVetoableChangeListener implements Filter<Calendar> {

  private final Calendar maxDate;
  private final Calendar minDate;

  /**
   * Creates an instance of the DateOutOfBoundsVetoableChangeListener class initiailized with the specified
   * minimum and maximum date values.  These min and max date values are used to constrain property values
   * for which this listener is interested.
   * @param minDate the minimum date value.
   * @param maxDate the maximum date value.
   */
  public DateOutOfBoundsVetoableChangeListener(final Calendar minDate, final Calendar maxDate) {
    this(null, minDate, maxDate);
  }

  /**
   * Creates an instance of the DateOutOfBoundsVetoableChangeListener class initiailized with the specified
   * minimum and maximum date values.  These min and max date values are used to constrain property values
   * for which this listener is interested.  This listener in particular is only interested in property changes
   * of the specified property by name.
   * @param propertyName the name of the property for which this listener is interested.
   * @param minDate the minimum date value.
   * @param maxDate the maximum date value.
   */
  public DateOutOfBoundsVetoableChangeListener(final String propertyName, final Calendar minDate, final Calendar maxDate) {
    super(propertyName);

    if (ObjectUtil.isNotNull(minDate) && ObjectUtil.isNotNull(maxDate)) {
      if (minDate.after(maxDate)) {
        logger.warn("The min date (" + DateUtil.toString(minDate) + ") cannot be after max date ("
          + DateUtil.toString(maxDate) + ")!");
        throw new IllegalArgumentException("The min date (" + DateUtil.toString(minDate) + ") cannot be after max date ("
          + DateUtil.toString(maxDate) + ")!");
      }
    }

    this.minDate = DateUtil.copy(minDate);
    this.maxDate = DateUtil.copy(maxDate);
  }

  /**
   * Gets the maximum date value allowed by this listener.
   * @return a Calendar object specifying the maximum date value allowed by this listener.
   */
  public Calendar getMaxDate() {
    return DateUtil.copy(maxDate);
  }

  /**
   * Gets the minimum date value allowed by this listener.
   * @return a Calendar object specifying the minimum date value allowed by this listener.
   */
  public Calendar getMinDate() {
    return DateUtil.copy(minDate);
  }

  /**
   * Determines whether the specified date value satisfies the constraints of this listener.
   * @param date the Calendar object value in question.
   * @return a boolean value indicating whether the specified date value satisfies the constraints
   * enforced by this listener.
   */
  public boolean accept(final Calendar date) {
    // innocent until proven guilty
    boolean valid = true;

    if (ObjectUtil.isNotNull(date)) {
      if (ObjectUtil.isNotNull(getMinDate())) {
        valid &= !date.before(getMinDate());
      }

      if (ObjectUtil.isNotNull(getMaxDate())) {
        valid &= !date.after(getMaxDate());
      }
    }

    return valid;
  }

  /**
   * Processes a property change event on a date property value.
   * @param event the PropertyChangeEvent object encapsulating information about the property change.
   * @throws PropertyVetoException if the property change does not satisfy the constraints enforced by this listener.
   */
  protected void handle(final PropertyChangeEvent event) throws PropertyVetoException {
    try {
      if (logger.isDebugEnabled()) {
        logger.debug("new date value (" + event.getNewValue() + ") for property (" + event.getPropertyName()
          + ") of bean (" + event.getSource().getClass().getName() + ")");
      }

      final Calendar date = (Calendar) event.getNewValue();

      if (!accept(date)) {
        logger.warn("The new date value (" + DateUtil.toString(date)
          + ") is out of bounds; the date is not between the min (" + DateUtil.toString(getMinDate()) + ") and max ("
          + DateUtil.toString(getMaxDate()) + ") dates inclusive!");
        throw new PropertyVetoException("The new date value (" + DateUtil.toString(date)
          + ") is out of bounds; the date is not between the min (" + DateUtil.toString(getMinDate()) + ") and max ("
          + DateUtil.toString(getMaxDate()) + ") dates inclusive!", event);
      }
    }
    catch (ClassCastException e) {
      logger.error("property (" + event.getPropertyName() + ") of bean (" + event.getSource().getClass().getName()
        + ") is not of type Calendar!", e);
      throw new IllegalUseOfListenerException("property (" + event.getPropertyName() + ") of bean ("
        + event.getSource().getClass().getName() + ") is not of type Calendar!", e);
    }
  }

}
