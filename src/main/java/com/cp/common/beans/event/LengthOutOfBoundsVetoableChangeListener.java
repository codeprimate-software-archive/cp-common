/*
 * LengthOutOfBoundsVetoableChangeListener.java (c) 27 July 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.9.14
 * @see com.cp.common.beans.event.AbstractVetoableChangeListener
 * @see com.cp.common.beans.event.BoundedLengthVetoableChangeListener
 * @see com.cp.common.beans.event.IllegalUseOfListenerException
 * @see com.cp.common.beans.util.Filter
 */

package com.cp.common.beans.event;

import com.cp.common.lang.Assert;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.Filter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

public class LengthOutOfBoundsVetoableChangeListener extends AbstractVetoableChangeListener implements Filter<String> {

  private final int max;
  private final int min;

  /**
   * Instantiates an instance of the LengthOutOfBoundsVetoableChangeListener with a specified minimum and maximum length
   * to constrain the length of a String value accepted by this listener.
   * @param min an integer value specifying a String values minimum length accepted by this listener.
   * @param max an integer value specifying a String values maximum length accepted by this listener.
   */
  public LengthOutOfBoundsVetoableChangeListener(final int min, final int max) {
    this(null, min, max);
  }

  /**
   * Instantiates an instance of the LengthOutOfBoundsVetoableChangeListener with a specified minimum and maximum length
   * to constrain the length of a String value accepted by this listener for the specified property.
   * @param propertyName the String name of the property in which this listener applies.
   * @param min an integer value specifying a String values minimum length accepted by this listener.
   * @param max an integer value specifying a String values maximum length accepted by this listener.
   */
  public LengthOutOfBoundsVetoableChangeListener(final String propertyName, final int min, final int max) {
    super(propertyName);
    Assert.greaterThanEqual(min, 0, "The mininum length (" + min + ") must be greater than or equal to 0!");
    Assert.greaterThanEqual(max, min, "The maximum length (" + max
      + ") must be greater than equal to the minimum length (" + min + ")!");
    this.min = min;
    this.max = max;
  }

  /**
   * Gets the maximum String value length accepted by this listener.
   * @return an integer value indicating the maximum String value length in characters accepted by this listener.
   */
  public int getMax() {
    return max;
  }

  /**
   * Gets the minimum String value length accepted by this listener.
   * @return an integer value indicating the minimum String value length in characters accepted by this listener.
   */
  public int getMin() {
    return min;
  }

  /**
   * Determines whether this listener accepts the specified String value according to the value's length
   * @param value the String value who's length is being tested against the constraint's of this listener.
   * @return a boolean value indicating whether the specified String value satisfied the listener's constraints.
   */
  public boolean accept(final String value) {
    final int valueLength = StringUtil.length(value);
    return (valueLength >= getMin() && valueLength <= getMax());
  }

  /**
   * Processes a property change event on a String property value.
   * @param event the PropertyChangeEvent object encapsulating information about the property change.
   * @throws PropertyVetoException if the property change does not satisfy the constraints enforced by this listener.
   */
  protected void handle(final PropertyChangeEvent event) throws PropertyVetoException {
    try {
      if (logger.isDebugEnabled()) {
        logger.debug("new date value (" + event.getNewValue() + ") for property (" + event.getPropertyName()
          + ") of bean (" + event.getSource().getClass().getName() + ")");
      }

      final String stringValue = (String) event.getNewValue();

      if (!accept(stringValue)) {
        logger.warn("The length of value (" + stringValue + ") for property (" + event.getPropertyName() + ") on bean ("
          + event.getSource().getClass().getName() + ") must be between minium length (" + getMin()
          + ") and maximum length (" + getMax() + ")!");
        throw new PropertyVetoException("The length of value (" + stringValue + ") for property ("
          + event.getPropertyName() + ") on bean (" + event.getSource().getClass().getName()
          + ") must be between minimum length (" + getMin() + ") and maximum length (" + getMax() + ")!", event);
      }
    }
    catch (ClassCastException e) {
      logger.error("Property (" + event.getPropertyName() + ") on bean (" + event.getSource().getClass().getName()
        + ") is not of type String!", e);
      throw new IllegalUseOfListenerException("Property (" + event.getPropertyName() + ") on bean ("
        + event.getSource().getClass().getName() + ") is not of type String!", e);
    }
  }

}
