/*
 * RequiredFieldVetoableChangeListener.java (c) 26 December 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.9.16
 * @see com.cp.common.beans.event.AbstractVetoableChangeListener
 * @see com.cp.common.beans.event.RequiredVetoableChangeListener
 */

package com.cp.common.beans.event;

import com.cp.common.lang.ObjectUtil;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

public final class RequiredFieldVetoableChangeListener extends AbstractVetoableChangeListener {

  public static final RequiredFieldVetoableChangeListener INSTANCE = new RequiredFieldVetoableChangeListener();

  /**
   * Creates an instance of the RequiredFieldVetoableChangeListener to listen for property change events
   * and enforce non-null constraints on properties registered with this listener.
   */
  private RequiredFieldVetoableChangeListener() {
  }

  /**
   * Creates an instance of the RequiredFieldVetoableChangeListener class to listen for property change events
   * on the specified property and enforce a non-null contraint on the property.
   * @param propertyName the name of the property to contrain to non-null values.
   */
  public RequiredFieldVetoableChangeListener(final String propertyName) {
    super(propertyName);
  }

  /**
   * Method called to handle the PropertyChangeEvent.
   * @param event the Event object used to describe the property change event that occurred.
   * @throws PropertyVetoException if the property change violates the contraints imposed upon the property.
   */
  protected void handle(final PropertyChangeEvent event) throws PropertyVetoException {
    if (ObjectUtil.isNull(event.getNewValue())) {
      logger.warn("The property (" + event.getPropertyName() + ") of bean (" + event.getSource().getClass().getName()
        + ") is a required field and cannot be null!");
      throw new PropertyVetoException("The property (" + event.getPropertyName() + ") of bean ("
        + event.getSource().getClass().getName() + ") is a required field and cannot be null!", event);
    }
  }

}
