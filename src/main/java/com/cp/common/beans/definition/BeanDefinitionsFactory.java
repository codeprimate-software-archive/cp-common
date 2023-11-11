/*
 * BeanDefinitionsFactory.java (c) 17 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.5.2
 * @see com.cp.common.beans.definition.BeanDeclaration
 * @see com.cp.common.beans.definition.BeanDefinition
 * @see com.cp.common.beans.definition.InvocationArgument
 * @see com.cp.common.beans.definition.ListenerDeclaration
 * @see com.cp.common.beans.definition.ListenerDefinition
 * @see com.cp.common.beans.definition.PropertyDeclaration
 * @see com.cp.common.beans.definition.PropertyDefinition
 */

package com.cp.common.beans.definition;

public class BeanDefinitionsFactory {

  /**
   * Default private constructor to enforce non-instantiation.
   */
  private BeanDefinitionsFactory() {
  }

  /**
   * Creates an instance of the BeanDeclaration interface initialized with the specified identifier
   * and fully qualified class name of the bean.
   * @param id a String value specifying the unique identifier of the bean definition.
   * @param className the fully qualified class name of the bean.
   * @return an instance of the BeanDeclaration interface initialized with the id and class name.
   */
  public static BeanDeclaration getBeanDeclaration(final String id, final String className) {
    return new DefaultBeanDeclaration(id, className);
  }

  /**
   * Creates an instance of the BeanDefinition interface initialized with the specified identifier
   * and fully qualified class name of the bean.
   * @param id a String value specifying the unique identifier of the bean definition.
   * @param className the fully qualified class name of the bean.
   * @return an instance of the BeanDefinition interface initialized with the id and class name.
   */
  public static BeanDefinition getBeanDefinition(final String id, final String className) {
    return new DefaultBeanDefinition(id, className);
  }

  /**
   * Creates an instance of the InvocationArgument interface initialized with the specified argument type
   * and argument value.
   * @param type a String value specifying the fully-qualified class name of the invocation argument type.
   * @param value a String representation of the invocation argument's value.
   * @return an instance of the InvocationArgument interface initialized with the specified type and value.
   */
  public static InvocationArgument getInvocationArgument(final String type, final String value) {
    return new DefaultInvocationArgument(type, value);
  }

  /**
   * Creates an instance of the ListenerDeclaration interface initialized with the fully qualified class name
   * of the listener.
   * @param className the fully qualified class name of the listener.
   * @return an instance of the ListenerDeclaration interface initialized with the class name.
   */
  public static ListenerDeclaration getListenerDeclaration(final String className) {
    return new DefaultListenerDeclaration(className);
  }

  /**
   * Creates an instance of the ListenerDefinition interface initialized with the fully qualified class name
   * of the listener.
   * @param className the fully qualified class name of the listener.
   * @return an instance of the ListenerDefinition interface initialized with the class name.
   */
  public static ListenerDefinition getListenerDefinition(final String className) {
    return new DefaultListenerDefinition(className);
  }

  /**
   * Gets an instance of the Parameterizable interface.
   * @return an instance of the Parameterizable interface.
   */
  public static Parameterizable getParameterizable() {
    return new DefaultParameterHandler();
  }

  /**
   * Gets an instance of the PropertyDeclaration interface initialized with the name of the property as well as
   * the fully qualified class name of the property's type.
   * @param propertyName a String value specifying the name of the property.
   * @return an instance of the PropertyDeclaration interface initialized with the property's name
   * and property's type class name.
   */
  public static PropertyDeclaration getPropertyDeclaration(final String propertyName) {
    return new DefaultPropertyDeclaration(propertyName, String.class.getName());
  }

  /**
   * Gets an instance of the PropertyDefinition interface initialized with the name of the property as well as
   * the fully qualified class name of the property's type.
   * @param propertyName a String value specifying the name of the property.
   * @param className the fully qualified class name of the property's type.
   * @return an instance of the PropertyDefinition interface initialized with the property's name
   * and property's type class name.
   */
  public static PropertyDefinition getPropertyDefinition(final String propertyName, final String className) {
    return new DefaultPropertyDefinition(propertyName, className);
  }

  /**
   * Gets an instance of the ReferenceObject interface initialized to the specified reference id and Class type
   * of the reference.
   * @param referenceId a String value specifying the unique identifier of the referenced object.
   * @param type the Class type of the referenced object.
   * @return an instance of the ReferenceObject interface initialized with the reference id and type
   * of the referenced object.
   */
  public static ReferenceObject getReferenceObject(final String referenceId, final Class type) {
    return new DefaultReferenceObject(referenceId, type);
  }

}
