/*
 * BoundedLengthVetoableChangeListener.java (c) 27 July 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.9.13
 * @see com.cp.common.beans.annotation.BoundedLength
 * @see com.cp.common.beans.annotation.IllegalUseOfAnnotationException
 * @see com.cp.common.beans.event.AbstractVetoableChangeListener
 */

package com.cp.common.beans.event;

import com.cp.common.beans.annotation.BoundedLength;
import com.cp.common.beans.annotation.IllegalUseOfAnnotationException;
import com.cp.common.beans.annotation.MalformedAnnotationDeclarationException;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.Filter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.io.Serializable;

public class BoundedLengthVetoableChangeListener extends AbstractVetoableChangeListener {

  /**
   * Constructs an instance of the BoundedLengthVetoableChangeListener class with the specified annotated bean.
   * @param annotatedBean the bean annotated with the BoundedLength Annotation for specifying constraints
   * on String properties.
   */
  public BoundedLengthVetoableChangeListener(final Object annotatedBean) {
    super(annotatedBean);
  }

  /**
   * Handles property change events for String properties constrained by the BoundedLength Annotation.
   * @param event an Event object containing information about the property change.
   * @throws PropertyVetoException if the property change violates the BoundedLength Annotation constraint.
   */
  protected void handle(final PropertyChangeEvent event) throws PropertyVetoException {
    // Note the identity equality comparison.  Normally we do no want to use the == or != operator
    // to compare Object references for equality, but in this case I do since I have the BeanInfo
    // for the registered bean of this listener.
    Assert.same(getBean(), event.getSource(), "The registered bean and the event bean are not the same object!");

    final Object newValue = event.getNewValue();

    if (ObjectUtil.isNotNull(newValue)) {
      try {
        final BoundedLengthConstraint boundedLengthConstraint = getBoundedLengthConstraint(event.getPropertyName());

        if (ObjectUtil.isNotNull(boundedLengthConstraint)) {
          final String stringValue = (String) newValue;

          if (!boundedLengthConstraint.accept(stringValue)) {
            logger.warn("The length of value (" + stringValue + ") for property (" + event.getPropertyName()
              + ") on bean (" + getBean().getClass().getName() + ") must be between minimum length ("
              + boundedLengthConstraint.getMin() + ") and maximum length (" + boundedLengthConstraint.getMax() + ")!");
            throw new PropertyVetoException("The length of value (" + stringValue + ") for property ("
              + event.getPropertyName() + ") on bean (" + getBean().getClass().getName()
              + ") must be between minimum length (" + boundedLengthConstraint.getMin() + ") and maximum length ("
              + boundedLengthConstraint.getMax() + ")!", event);
          }
        }
      }
      catch (ClassCastException e) {
        logger.error("Property (" + event.getPropertyName() + ") on bean (" + getBean().getClass().getName()
          + ") has a BoundedLength Annotation but is not of type String!", e);
        throw new IllegalUseOfAnnotationException("Property (" + event.getPropertyName() + ") on bean ("
          + getBean().getClass().getName() + ") has a BoundedLength Annotation but is not of type String!", e);
      }
    }
  }

  /**
   * Gets a BoundedLengthConstraint for the specified String property having a BoundedLength Annotation.
   * @param propertyName the String name of the property.
   * @return a BoundedLengthConstraint object containing the information from the BoundedLength Annotation
   * on the String property specified by name, or return null if the property is not annotated with the
   * BoundedLength Annotation.
   * @see AbstractListener#getAnnotation(String, Class)
   */
  protected BoundedLengthConstraint getBoundedLengthConstraint(final String propertyName) {
    try {
      final BoundedLength boundedLength = getAnnotation(propertyName, BoundedLength.class);

      if (ObjectUtil.isNotNull(boundedLength)) {
        return new BoundedLengthConstraint(boundedLength);
      }
      else {
        if (logger.isDebugEnabled()) {
          logger.debug("The write method for property (" + propertyName + ") is not annotated with the BoundedLength Annotation!");
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

  protected class BoundedLengthConstraint implements Filter<String>, Serializable {

    private final int max;
    private final int min;

    public BoundedLengthConstraint(final BoundedLength boundedLength) {
      this(boundedLength.min(), boundedLength.max());
    }

    public BoundedLengthConstraint(final int min, final int max) {
      Assert.greaterThanEqual(min, 0, "The minimum length (" + min + ") must be greater than or equal to 0!");
      Assert.greaterThanEqual(max, min, "The maximum length (" + max + ") must be greater than equal to the minimum length (" + min + ")!");
      this.min = min;
      this.max = max;
    }

    public int getMax() {
      return max;
    }

    public int getMin() {
      return min;
    }

    public boolean accept(final String value) {
      final int valueLength = StringUtil.length(value);
      return (valueLength >= getMin() && valueLength <= getMax());
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof BoundedLengthConstraint)) {
        return false;
      }

      final BoundedLengthConstraint that = (BoundedLengthConstraint) obj;

      return ObjectUtil.equals(getMin(), that.getMin())
        && ObjectUtil.equals(getMax(), that.getMax());
    }

    @Override
    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getMin());
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getMax());
      return hashValue;
    }

    @Override
    public String toString() {
      final StringBuffer buffer = new StringBuffer("{min = ");
      buffer.append(getMin());
      buffer.append(", max = ").append(getMax());
      buffer.append("}:").append(getClass().getName());
      return buffer.toString();
    }
  }

}
