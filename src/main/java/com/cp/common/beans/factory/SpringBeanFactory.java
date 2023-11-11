/*
 * SpringBeanFactory.java (c) 30 April 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.11.12
 * @see com.cp.common.beans.factory.BeanFactory
 * @see com.springframework.context.ApplicationContextAware
 */

package com.cp.common.beans.factory;

import com.cp.common.beans.Bean;
import com.cp.common.beans.definition.Scope;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringBeanFactory implements ApplicationContextAware, BeanFactory {

  private ApplicationContext applicationContext;

  /**
   * Gets the aliases for a particular bean identified by the unique identifier.
   * @param beanId a String value specifying the unique identifier of a bean.
   * @return a String array of aliases that are synonymous with the specified bean id for a bean.
   */
  public String[] getAliases(final String beanId) {
    return getApplicationContext().getAliases(beanId);
  }

  /**
   * Gets the Spring application context associated with this BeanFactory.
   * @return the Spring application context associated with this BeanFactory.
   */
  protected ApplicationContext getApplicationContext() {
    Assert.state(ObjectUtil.isNotNull(applicationContext), "The ApplicationContext for the SpringBeanFactory has not been properly configured in the Spring application context!");
    return applicationContext;
  }

  /**
   * Sets the Spring application context associated with this BeanFactory.
   * @param applicationContext the Spring application context associated with this BeanFactory.
   */
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    Assert.notNull(applicationContext, "The Spring Application Context cannot be null!");
    this.applicationContext = applicationContext;
  }

  /**
   * Gets a Bean object for the specified bean id, uniquely identifying the requested bean object.
   * @param beanId a String value specifying the unique identifier of the requested bean.
   * @return a Bean object identified by the specified unique identifier.
   */
  public <T extends Bean> T getBean(final String beanId) {
    return (T) getApplicationContext().getBean(beanId);
  }

  /**
   * Gets a Bean object for the specified bean id, uniquely identifying the requested bean object initialized
   * with the specified arguments.
   * @param beanId a String value specifying the unique identifier of the requested bean.
   * @param args an Object array containing values used to initialize the bean.
   * @return a Bean object identified by the specified unique identifier.
   */
  public <T extends Bean> T getBean(final String beanId, final Object[] args) {
    throw new UnsupportedOperationException("Not Implemented!");
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
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Gets the specified scope of the bean identified by the bean id.
   * @param beanId a String value specifying the unique identifier of a bean.
   * @return a Scope enumerated value specifying the scope of the bean identified by the specified bean id.
   */
  public Scope getScope(final String beanId) {
    return Scope.PROTOTYPE;
  }

  /**
   * Gets the Class type of the bean identified by the bean id.
   * @param beanId a String value specifying the unique identifier of a bean.
   * @return a Class object specifying the type of the bean identified by the specified bean id.
   */
  public Class getType(final String beanId) {
    return getApplicationContext().getType(beanId);
  }

  /**
   * Determines whether this factory manages the bean identified by the specified bean id.
   * @param beanId a String value specifying the unique identifier of a bean.
   * @return a boolean value indicating whether the bean identified by the specified bean id is managed by this factory.
   */
  public boolean containsBean(final String beanId) {
    return getApplicationContext().containsBean(beanId);
  }

}
