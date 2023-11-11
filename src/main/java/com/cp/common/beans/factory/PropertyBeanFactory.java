/*
 * PropertyBeanFactory.java (c) 18 December 2007
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.beans.definition.Scope
 * @see com.cp.common.beans.factory.AbstractBeanFactory
 * @see com.cp.common.context.config.Config
 */

package com.cp.common.beans.factory;

import com.cp.common.beans.Bean;
import com.cp.common.beans.definition.Scope;
import com.cp.common.context.config.Config;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.ConfigurationException;
import com.cp.common.util.SystemException;
import java.util.MissingResourceException;

public class PropertyBeanFactory extends AbstractBeanFactory {

  /**
   * Creates an instance of the PropertyBeanFactory class initialized with the specified Config object.
   * @param config a Config object containing configuration information for this factory.
   */
  public PropertyBeanFactory(final Config config) {
    super(config);
  }

  /**
   * Gets the aliases for a particular bean identified by the unique identifier.
   * @param beanId a String value specifying the unique identifier of a bean.
   * @return a String array of aliases that are synonymous with the specified bean id for a bean.
   */
  public String[] getAliases(final String beanId) {
    Assert.notEmpty(beanId, "The bean id cannot be null or empty!");

    if (containsBean(beanId)) {
      return new String[0];
    }
    else {
      logger.warn("The bean identified by id (" + beanId + ") cannot be found!");
      throw new BeanNotFoundException("The bean identified by id (" + beanId + ") cannot be found!");
    }
  }

  /**
   * Gets a Bean object for the specified bean id, uniquely identifying the requested bean object.
   * @param beanId a String value specifying the unique identifier of the requested bean.
   * @return a Bean object identified by the specified unique identifier.
   */
  public <T extends Bean> T getBean(final String beanId) {
    try {
      Assert.notEmpty(beanId, "The bean id cannot be null or empty!");
      return (T) getInstance(beanId);
    }
    catch (MissingResourceException e) {
      logger.error("The bean identified by id (" + beanId + ") cannot be found!", e);
      throw new BeanNotFoundException("The bean identified by id (" + beanId + ") cannot be found!", e);
    }
  }

  /**
   * Gets a Bean object for the specified bean id, uniquely identifying the requested bean object initialized
   * with the specified arguments.
   * @param beanId a String value specifying the unique identifier of the requested bean.
   * @param args an Object array containing values used to initialize the bean.
   * @return a Bean object identified by the specified unique identifier.
   */
  public <T extends Bean> T getBean(final String beanId, final Object[] args) {
    try {
      Assert.notEmpty(beanId, "The bean id cannot be null or empty!");
      return (T) getInstance(beanId, args);
    }
    catch (MissingResourceException e) {
      logger.error("The bean identified by id (" + beanId + ") cannot be found!", e);
      throw new BeanNotFoundException("The bean identified by id (" + beanId + ") cannot be found!", e);
    }
  }

  /**
   * Gets a Bean object for the specified bean id, uniquely identifying the requested bean object initialized
   * with the specified arguments.
   * @param beanId a String value specifying the unique identifier of the requested bean.
   * @param argTypes a Class array containing the argument types.
   * @param args an Object array containing values used to initialize the bean.
   * @return a Bean object identified by the specified unique identifier.
   */
  public <T extends Bean> T getBean(final String beanId, final Class[] argTypes, final Object[] args) {
    try {
      Assert.notEmpty(beanId, "The bean id cannot be null or empty!");
      return (T) getInstance(beanId, argTypes, args);
    }
    catch (MissingResourceException e) {
      logger.error("The bean identified by id (" + beanId + ") cannot be found!", e);
      throw new BeanNotFoundException("The bean identified by id (" + beanId + ") cannot be found!", e);
    }
  }

  /**
   * Gets the specified scope of the bean identified by the bean id.
   * @param beanId a String value specifying the unique identifier of a bean.
   * @return a Scope enumerated value specifying the scope of the bean identified by the specified bean id.
   */
  public Scope getScope(final String beanId) {
    Assert.notEmpty(beanId, "The bean id cannot be null or empty!");

    if (containsBean(beanId)) {
      return Scope.PROTOTYPE;
    }
    else {
      logger.warn("The bean identified by id (" + beanId + ") cannot be found!");
      throw new BeanNotFoundException("The bean identified by id (" + beanId + ") cannot be found!");
    }
  }

  /**
   * Gets the Class type of the bean identified by the bean id.
   * @param beanId a String value specifying the unique identifier of a bean.
   * @return a Class object specifying the type of the bean identified by the specified bean id.
   */
  public Class getType(final String beanId) {
    Assert.notEmpty(beanId, "The bean id cannot be null or empty!");

    if (containsBean(beanId)) {
      final String beanClassName = getConfig().getStringPropertyValue(beanId);

      if (StringUtil.isEmpty(beanClassName)) {
        logger.warn("The fully qualified bean class name was not defined for bean identified by id (" + beanId + ")!");
        throw new ConfigurationException("The fully qualified bean class name was not defined for bean identified by id ("
          + beanId + ")!");
      }

      try {
        return ClassUtil.loadClass(beanClassName);
      }
      catch (ClassNotFoundException e) {
        logger.error("The fully qualified bean class name (" + beanClassName + ") for bean identified by id ("
          + beanId + ") could not be found in the CLASSPATH!", e);
        throw new SystemException("The fully qualified bean class name (" + beanClassName + ") for bean identified by id ("
          + beanId + ") could not be found in the CLASSPATH!", e);
      }
    }
    else {
      logger.warn("The bean identified by id (" + beanId + ") cannot be found!");
      throw new BeanNotFoundException("The bean identified by id (" + beanId + ") cannot be found!");
    }
  }

  /**
   * Determines whether this factory manages the bean identified by the specified bean id.
   * @param beanId a String value specifying the unique identifier of a bean.
   * @return a boolean value indicating whether the bean identified by the specified bean id is managed by this factory.
   */
  public boolean containsBean(final String beanId) {
    Assert.notEmpty(beanId, "The bean id cannot be null or empty!");
    return getConfig().contains(beanId);
  }

}
