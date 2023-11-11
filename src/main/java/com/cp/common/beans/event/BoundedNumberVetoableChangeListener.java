/*
 * BoundedNumberVetoableChangeListener.java (c) 16 December 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.12
 * @see com.cp.common.beans.annotation.BoundedNumber
 * @see com.cp.common.beans.annotation.IllegalUseOfAnnotationException
 * @see com.cp.common.beans.annotation.MalformedAnnotationDeclarationException
 * @see com.cp.common.beans.event.AbstractVetoableChangeListener
 * @see com.cp.common.beans.event.NumberOutOfBoundsVetoableChangeListener
 */

package com.cp.common.beans.event;

import com.cp.common.beans.annotation.BoundedNumber;
import com.cp.common.beans.annotation.IllegalUseOfAnnotationException;
import com.cp.common.beans.annotation.MalformedAnnotationDeclarationException;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.Filter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.io.Serializable;

public class BoundedNumberVetoableChangeListener extends AbstractVetoableChangeListener {

  /**
   * Creates an instance of the BoundedNumberVetoableChangeListener class initialized with the specified bean object
   * annotated with the BoundedNumber Annotation.
   * @param annotatedBean the bean object annotated with the BoundedNumber Annotation.
   */
  public BoundedNumberVetoableChangeListener(final Object annotatedBean) {
    super(annotatedBean);
  }

  /**
   * Handles property change events for Number properties constrained by the BoundedNumber Annotation.
   * @param event an Event object containing information about the property change.
   * @throws PropertyVetoException if the property change violates the BoundedNumber Annotation constraint.
   */
  protected void handle(final PropertyChangeEvent event) throws PropertyVetoException {
    // Note the identity equality comparison.  Normally we do no want to use the == or != operator
    // to compare Object references for equality, but in this case I do since I have the BeanInfo
    // for the registered bean of this listener.
    Assert.same(getBean(), event.getSource(), "The registered bean and event bean are not the same object!");

    final Object newValue = event.getNewValue();

    if (ObjectUtil.isNotNull(newValue)) {
      final BoundedNumberConstraint boundedNumberConstraint = getBoundedNumberConstraint(event.getPropertyName());

      if (ObjectUtil.isNotNull(boundedNumberConstraint)) {
        try {
          final Number number = (Number) newValue;

          if (!boundedNumberConstraint.accept(number.doubleValue())) {
            logger.warn("The value (" + number + ") for property (" + event.getPropertyName() + ") on bean ("
              + getBean().getClass().getName() + ") must be greater than equal (" + boundedNumberConstraint.getMin()
              + ") and less than equal (" + boundedNumberConstraint.getMax() + ")!");
            throw new PropertyVetoException("The value (" + number + ") for property (" + event.getPropertyName()
              + ") on bean (" + getBean().getClass().getName() + ") must be greater than equal ("
              + boundedNumberConstraint.getMin() + ") and less than equal (" + boundedNumberConstraint.getMax() + ")!",
              event);
          }
        }
        catch (ClassCastException e) {
          logger.error("Property (" + event.getPropertyName() + ") on bean (" + getBean().getClass().getName()
            + ") has a BoundedNumber Annotation but is not of type Number!", e);
          throw new IllegalUseOfAnnotationException("Property (" + event.getPropertyName() + ") on bean ("
            + getBean().getClass().getName() + ") has a BoundedNumber Annotation but is not of type Number!", e);
        }
      }
    }
  }

  /**
   * Gets a BoundedNumberConstraint for the specified Number property having a BoundedNumber Annotation.
   * @param propertyName the String name of the property.
   * @return a BoundedNumberConstraint object containing the information from the BoundedNumber Annotation
   * on the Number property specified by name, or return null if the property is not annotated with the
   * BoundedNumber Annotation.
   * @see AbstractListener#getAnnotation(String, Class)
   */
  protected BoundedNumberConstraint getBoundedNumberConstraint(final String propertyName) {
    try {
      final BoundedNumber boundedNumber = getAnnotation(propertyName, BoundedNumber.class);

      if (ObjectUtil.isNotNull(boundedNumber)) {
        return new BoundedNumberConstraint(boundedNumber);
      }
      else {
        if (logger.isDebugEnabled()) {
          logger.debug("The write method for property (" + propertyName + ") is not annotated with the BoundedNumber Annotation!");
        }
        return null;
      }
    }
    catch (IllegalArgumentException e) {
      throw new MalformedAnnotationDeclarationException(e.getMessage());
    }
    catch (NoSuchMethodException e) {
      // technically, the NoSuchMethodException should not be thrown if this listener got called for the specified property
      return null;
    }
  }

  protected static final class BoundedNumberConstraint implements Filter<Double>, Serializable {

    private final double max;
    private final double min;

    public BoundedNumberConstraint(final BoundedNumber boundedNumber) {
      this(boundedNumber.min(), boundedNumber.max());
    }

    public BoundedNumberConstraint(final double min, final double max) {
      Assert.greaterThanEqual(max, min, "The maximum value (" + max
        + ") must be greater than equal to the minimum value (" + min + ")!");
      this.min = min;
      this.max = max;
    }

    public double getMax() {
      return max;
    }

    public double getMin() {
      return min;
    }

    public boolean accept(final Double value) {
      return (value >= getMin() && value <= getMax());
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof BoundedNumberConstraint)) {
        return false;
      }

      final BoundedNumberConstraint that = (BoundedNumberConstraint) obj;

      return (getMax() == that.getMax())
        && (getMin() == that.getMin());
    }

    @Override
    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getMax());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getMin());
      return hashValue;
    }

    @Override
    public String toString() {
      final StringBuffer buffer = new StringBuffer("{");
      buffer.append(getMin());
      buffer.append(" <= x <= ");
      buffer.append(getMax());
      buffer.append("}");
      return buffer.toString();
    }
  }

}
