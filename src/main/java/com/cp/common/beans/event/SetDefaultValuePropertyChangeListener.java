/*
 * SetDefaultValuePropertyChangeListener.java (c) 12 January 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.26
 * @see com.cp.common.beans.annotation.Default
 * @see com.cp.common.beans.annotation.MalformedAnnotationDeclarationException
 * @see com.cp.common.beans.event.AbstractPropertyChangeListener
 * @see com.cp.common.beans.util.ConvertUtil
 */

package com.cp.common.beans.event;

import com.cp.common.beans.ConstraintViolationException;
import com.cp.common.beans.annotation.Default;
import com.cp.common.beans.annotation.MalformedAnnotationDeclarationException;
import com.cp.common.beans.util.ConvertUtil;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SetDefaultValuePropertyChangeListener extends AbstractPropertyChangeListener {

  /**
   * Creates an instance of the SetDefaultValuePropertyChangeListener class initialized with the specified
   * annotated bean.
   * @param annotatedBean the annotated bean object.
   */
  public SetDefaultValuePropertyChangeListener(final Object annotatedBean) {
    super(annotatedBean);
  }

  /**
   * Converts the specified value into an object of the specified class type.
   * @param type the Class type to convert the String value to.
   * @param value the String value being converted into an object of the specified class type.
   * @return an Object value for the specified class type.
   * @see com.cp.common.beans.util.ConvertUtil#convert(Class, Object)
   */
  protected Object convert(final Class type, final String value) {
    return ("null".equals(StringUtil.trim(value)) ? null : ConvertUtil.convert(type, value));
  }

  /**
   * Method called to handle/process the PropertyChangeEvent.
   * @param event the PropertyChangeEvent object containing information about property change of the registered bean.
   */
  protected void handle(final PropertyChangeEvent event) {
    Assert.same(getBean(), event.getSource(), "The registered bean and event bean should be the same object!");

    final String propertyName = event.getPropertyName();
    final Object propertyValue = event.getNewValue();

    if (logger.isDebugEnabled()) {
      logger.debug("setting property (" + propertyName + ") with value (" + propertyValue + ")");
    }

    try {
      if (ObjectUtil.isNull(propertyValue) && isAnnotationPresent(propertyName, Default.class)) {
        final Default defaultAnnotation = getAnnotation(propertyName, Default.class);
        final String defaultValueAsString = defaultAnnotation.value();
        final Method writeMethod = getWriteMethod(propertyName);
        final Class propertyType = writeMethod.getParameterTypes()[0];
        final Object defaultValue = convert(propertyType, defaultValueAsString);

        if (logger.isDebugEnabled()) {
          logger.debug("default value as string (" + defaultValueAsString + ")");
          logger.debug("property setter (" + writeMethod.getName() + ")");
          logger.debug("property type (" + propertyType.getName() + ")");
          logger.debug("default value (" + defaultValue + ")");
        }

        Assert.isTrue(ObjectUtil.isNotNull(defaultValue), "The default value should not be null!");

        writeMethod.invoke(event.getSource(), defaultValue);
      }
    }
    catch (IllegalArgumentException e) {
      throw new MalformedAnnotationDeclarationException(e.getMessage());
    }
    catch (InvocationTargetException e) {
      if (e.getCause() instanceof ConstraintViolationException) {
        throw new MalformedAnnotationDeclarationException(e.getCause().getMessage());
      }

      logger.error("Failed to set the default value for property (" + propertyName + ") of bean ("
        + event.getSource().getClass().getName() +  ")!", e);
    }
    catch (Exception e) {
      logger.error("Failed to set the default value for property (" + propertyName + ") of bean ("
        + event.getSource().getClass().getName() +  ")!", e);
    }
  }

}
