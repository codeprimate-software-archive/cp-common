/*
 * BoundedDateVetoableChangeListener.java (c) 12 June 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.11
 * @see com.cp.common.beans.annotation.BoundedDate
 * @see com.cp.common.beans.annotation.IllegalUseOfAnnotationException
 * @see com.cp.common.beans.annotation.MalformedAnnotationDeclarationException
 * @see com.cp.common.beans.event.AbstractVetoableChangeListener
 * @see com.cp.common.beans.event.DateOutOfBoundsVetoableChangeListener
 */

package com.cp.common.beans.event;

import com.cp.common.beans.annotation.BoundedDate;
import com.cp.common.beans.annotation.IllegalUseOfAnnotationException;
import com.cp.common.beans.annotation.MalformedAnnotationDeclarationException;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.DateUtil;
import com.cp.common.util.Filter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BoundedDateVetoableChangeListener extends AbstractVetoableChangeListener {

  /**
   * Creates an instance of the BoundedDateVetoableChangeListener class initialized with the specified
   * BoundedDate annotated bean for constraining Calendar properties.
   * @param annotatedBean the bean annotated with the BoundedDate Annotation for specifying constraints on
   * Calendar properties.
   */
  public BoundedDateVetoableChangeListener(final Object annotatedBean) {
    super(annotatedBean);
  }

  /**
   * Handles property change events for Calendar properties constrained by the BoundedDate Annotation.
   * @param event an Event object containing information about the property change.
   * @throws PropertyVetoException if the property change violates the BoundedDate Annotation constraint.
   */
  protected void handle(final PropertyChangeEvent event) throws PropertyVetoException {
    // Note the identity equality comparison.  Normally we do no want to use the == or != operator
    // to compare Object references for equality, but in this case I do since I have the BeanInfo
    // for the registered bean of this listener.
    Assert.same(getBean(), event.getSource(), "The registered bean and the event bean are not the same object!");

    final Object newValue = event.getNewValue();

    if (ObjectUtil.isNotNull(newValue)) {
      final BoundedDateConstraint boundedDateConstraint = getBoundedDateConstraint(event.getPropertyName());

      if (ObjectUtil.isNotNull(boundedDateConstraint)) {
        try {
          final Calendar date = (Calendar) newValue;

          if (!boundedDateConstraint.accept(date)) {
            logger.warn("The value (" + DateUtil.toString(date) + ") for property (" + event.getPropertyName()
              + ") on bean (" + getBean().getClass().getName() + ") must be greater than equal ("
              + DateUtil.toString(boundedDateConstraint.getMinDate()) + ") and less than equal ("
              + DateUtil.toString(boundedDateConstraint.getMaxDate()) + ")!");
            throw new PropertyVetoException("The value (" + DateUtil.toString(date) + ") for property ("
              + event.getPropertyName() + ") on bean (" + getBean().getClass().getName() + ") must be greater than equal ("
              + DateUtil.toString(boundedDateConstraint.getMinDate()) + ") and less than equal ("
              + DateUtil.toString(boundedDateConstraint.getMaxDate()) + ")!", event);
          }
        }
        catch (ClassCastException e) {
          logger.error("Property (" + event.getPropertyName() + ") on bean (" + getBean().getClass().getName()
            + ") has a BoundedDate Annotation but is not of type Calendar!", e);
          throw new IllegalUseOfAnnotationException("Property (" + event.getPropertyName() + ") on bean ("
            + getBean().getClass().getName() + ") has a BoundedDate Annotation but is not of type Calendar!", e);
        }
      }
    }
  }

  /**
   * Gets a BoundedDateConstraint for the specified Calendar property having a BoundedDate Annotation.
   * @param propertyName the String name of the property.
   * @return a BoundedDateConstraint object containing the information from the BoundedDate Annotation
   * on the Calendar property specified by name, or return null if the property is not annotated with the
   * BoundedDate Annotation.
   * @see AbstractListener#getAnnotation(String, Class)
   */
  protected BoundedDateConstraint getBoundedDateConstraint(final String propertyName) {
    try {
      final BoundedDate boundedDate = getAnnotation(propertyName, BoundedDate.class);

      if (ObjectUtil.isNotNull(boundedDate)) {
        return new BoundedDateConstraint(boundedDate);
      }
      else {
        if (logger.isDebugEnabled()) {
          logger.debug("The write method for property (" + propertyName + ") is not annotated with the BoundedDate Annotation!");
        }
        return null;
      }
    }
    catch (NoSuchMethodException e) {
      // technically, the NoSuchMethodException should not be thrown if this listener got called for the specified property
      return null;
    }
  }

  protected class BoundedDateConstraint implements Filter<Calendar>, Serializable {

    private final Calendar maxDate;
    private final Calendar minDate;

    private final DateFormat dateFormat;

    private final String pattern;

    public BoundedDateConstraint(final BoundedDate boundedDate) {
      this(boundedDate.min(), boundedDate.max(), boundedDate.pattern());
    }

    public BoundedDateConstraint(final String minDate, final String maxDate, final String pattern) {
      this.pattern = (StringUtil.isNotEmpty(pattern) ? pattern : DateUtil.DEFAULT_DATE_FORMAT_PATTERN);
      this.dateFormat = new SimpleDateFormat(this.pattern);
      this.minDate = getCalendar(minDate);
      this.maxDate = getCalendar(maxDate);
    }

    protected Calendar getCalendar(final String date) {
      if (StringUtil.isNotEmpty(date)) {
        try {
          return DateUtil.getCalendar(dateFormat.parse(date).getTime());
        }
        catch (ParseException e) {
          logger.error("(" + date + ") is not a valid date as defined by pattern (" + getPattern() + ")!");
          throw new MalformedAnnotationDeclarationException("(" + date + ") is not a valid date as defined by pattern ("
            + getPattern() + ")!");
        }
      }

      return null;
    }

    public Calendar getMaxDate() {
      return maxDate;
    }

    public Calendar getMinDate() {
      return minDate;
    }

    public String getPattern() {
      return pattern;
    }

    public boolean accept(final Calendar date) {
      Assert.notNull(date, "The date value cannot be null!");

      // innocent until proven guilty
      boolean valid = true;

      if (ObjectUtil.isNotNull(getMinDate())) {
        valid &= !date.before(getMinDate());
      }

      if (ObjectUtil.isNotNull(getMaxDate())) {
        valid &= !date.after(getMaxDate());
      }

      return valid;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof BoundedDateConstraint)) {
        return false;
      }

      final BoundedDateConstraint that = (BoundedDateConstraint) obj;

      return ObjectUtil.equals(getMaxDate(), that.getMaxDate())
        && ObjectUtil.equals(getMinDate(), that.getMinDate())
        && ObjectUtil.equals(getPattern(), that.getPattern());
    }

    @Override
    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getMaxDate());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getMinDate());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getPattern());
      return hashValue;
    }

    @Override
    public String toString() {
      final StringBuffer buffer = new StringBuffer("{minDate = ");
      buffer.append(DateUtil.toString(getMinDate(), getPattern()));
      buffer.append(", maxDate = ").append(DateUtil.toString(getMaxDate(), getPattern()));
      buffer.append(", pattern = ").append(getPattern());
      buffer.append("}:").append(getClass().getName());
      return buffer.toString();
    }
  }

}
