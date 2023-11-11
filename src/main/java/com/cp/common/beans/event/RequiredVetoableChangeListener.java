/*
 * RequiredVetoableChangeListener.java (c) 15 December 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.24
 * @see com.cp.common.beans.annotation.Required
 * @see com.cp.common.beans.event.AbstractVetoableChangeListener
 * @see com.cp.common.beans.event.RequiredFieldVetoableChangeListener
 */

package com.cp.common.beans.event;

import com.cp.common.beans.annotation.Required;
import com.cp.common.beans.util.NoSuchPropertyException;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

public class RequiredVetoableChangeListener extends AbstractVetoableChangeListener {

  /**
   * Creates an instance of the RequiredVetoableChangeListener class initialized with the specified bean
   * annotated with the Required Annotation.
   * @param annotatedBean the bean object who's properties are annotated with the Required Annotation.
   */
  public RequiredVetoableChangeListener(final Object annotatedBean) {
    super(annotatedBean);
  }

  /**
   * Method called to handle/process the PropertyChangeEvent.
   * @param event the PropertyChangeEvent object containing information about property change of the registered bean.
   * @throws PropertyVetoException if the property change event is vetoed by this listener.
   */
  protected void handle(final PropertyChangeEvent event) throws PropertyVetoException {
    // Note the identity equality comparison.  Normally we do no want to use the == or != operator
    // to compare Object references for equality, but in this case I do since I have the BeanInfo
    // for the registered bean of this listener.
    Assert.same(getBean(), event.getSource(), "The registered bean and event bean are not the same object!");

    final String propertyName = event.getPropertyName();

    if (isRequired(propertyName) && ObjectUtil.isNull(event.getNewValue())) {
      logger.warn("The property (" + propertyName + ") of bean (" + getBean().getClass().getName() + ") is required!");
      throw new PropertyVetoException("The property (" + propertyName + ") of bean (" + getBean().getClass().getName()
        + ") is required!", event);
    }
  }

  /**
   * Determines whether he specified property by name is required on the registered bean of this listener.
   * @param propertyName the String name of the property.
   * @return a boolean value indicating whether the property identified by name is required or not.
   * @throws NoSuchPropertyException if the property identified by name is not a property of the registered bean
   * of this listener.
   */
  protected boolean isRequired(final String propertyName) throws NoSuchPropertyException {
    try {
      return isAnnotationPresent(propertyName, Required.class);
    }
    catch (NoSuchMethodException e) {
      // the property may be read-only
      return false;
    }
  }

}
