/*
 * ReadOnlyVetoableChangeListener.java (c) 26 December 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.23
 * @see com.cp.common.beans.event.AbstractVetoableChangeListener
 */

package com.cp.common.beans.event;

import com.cp.common.lang.ObjectUtil;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

public final class ReadOnlyVetoableChangeListener extends AbstractVetoableChangeListener {

  public static final ReadOnlyVetoableChangeListener INSTANCE = new ReadOnlyVetoableChangeListener();

  /**
   * Creates an instance of the ReadOnlyVetoableChangeListener class to listen for property change events
   * and enforce read-only constraints on properties registered with this listener.
   * Default private constructor to enforce non-instantiability.
   */
  private ReadOnlyVetoableChangeListener() {
  }

  /**
   * Creates an instance of the ReadOnlyVetoableChangeListener class to listen for property changes events
   * for the specified property and enforce a read-only constraint on the property.
   * @param propertyName the name of the property to listen for property change events and enforce a
   * read-only constraint.
   */
  public ReadOnlyVetoableChangeListener(final String propertyName) {
    super(propertyName);
  }

  /**
   * Method called to handle the PropertyChangeEvent.
   * @param event the Event object used to describe the property change event that occurred.
   * @throws PropertyVetoException if the property change violates the contraints imposed upon the property.
   */
  protected void handle(final PropertyChangeEvent event) throws PropertyVetoException {
    if (!ObjectUtil.equals(event.getOldValue(), event.getNewValue())) {
      logger.warn("The property (" + event.getPropertyName() + ") of bean (" + event.getSource().getClass().getName()
        + ") is read-only and cannot be changed!");
      throw new PropertyVetoException("The property (" + event.getPropertyName() + ") of bean ("
        + event.getSource().getClass().getName() + ") is read-only and cannot be changed!", event);
    }
  }

}
