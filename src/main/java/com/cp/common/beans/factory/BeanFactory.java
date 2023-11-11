/*
 * BeanFactory.java (c) 18 December 2007
 *
 * The BeanFactory interface defines a contract for callers to obtain Bean objects using a factory
 * (based on the Abstract Factory pattern) from various bean definition and declaration configurations.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.1.2
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.beans.definition.Scope
 */

package com.cp.common.beans.factory;

import com.cp.common.beans.Bean;
import com.cp.common.beans.definition.Scope;

public interface BeanFactory {

  /**
   * Gets the aliases for a particular bean identified by the unique identifier.
   * @param beanId a String value specifying the unique identifier of a bean.
   * @return a String array of aliases that are synonymous with the specified bean id for a bean.
   */
  public String[] getAliases(String beanId);

  /**
   * Gets a Bean object for the specified bean id, uniquely identifying the requested bean object.
   * @param beanId a String value specifying the unique identifier of the requested bean.
   * @return a Bean object identified by the specified unique identifier.
   */
  public <T extends Bean> T getBean(String beanId);

  /**
   * Gets a Bean object for the specified bean id, uniquely identifying the requested bean object initialized
   * with the specified arguments.
   * @param beanId a String value specifying the unique identifier of the requested bean.
   * @param args an Object array containing values used to initialize the bean.
   * @return a Bean object identified by the specified unique identifier.
   */
  public <T extends Bean> T getBean(String beanId, Object[] args);

  /**
   * Gets a Bean object for the specified bean id, uniquely identifying the requested bean object initialized
   * with the specified arguments.
   * @param beanId a String value specifying the unique identifier of the requested bean.
   * @param argTypes a Class array containing the argument types.
   * @param args an Object array containing values used to initialize the bean.
   * @return a Bean object identified by the specified unique identifier.
   */
  public <T extends Bean> T getBean(String beanId, Class[] argTypes, Object[] args);

  /**
   * Gets the specified scope of the bean identified by the bean id.
   * @param beanId a String value specifying the unique identifier of a bean.
   * @return a Scope enumerated value specifying the scope of the bean identified by the specified bean id.
   */
  public Scope getScope(String beanId);

  /**
   * Gets the Class type of the bean identified by the bean id.
   * @param beanId a String value specifying the unique identifier of a bean.
   * @return a Class object specifying the type of the bean identified by the specified bean id.
   */
  public Class getType(String beanId);

  /**
   * Determines whether this factory manages the bean identified by the specified bean id.
   * @param beanId a String value specifying the unique identifier of a bean.
   * @return a boolean value indicating whether the bean identified by the specified bean id is managed by this factory.
   */
  public boolean containsBean(String beanId);

}
