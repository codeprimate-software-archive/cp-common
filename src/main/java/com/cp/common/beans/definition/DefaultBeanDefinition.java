/*
 * DefaultBeanDefinition.java (c) 17 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.1.10
 * @see com.cp.common.beans.definition.AbstractBeanDefinition
 * @see com.cp.common.beans.definition.ListenerDefinition
 * @see com.cp.common.beans.definition.PropertyDefinition
 */

package com.cp.common.beans.definition;

public class DefaultBeanDefinition extends AbstractBeanDefinition<ListenerDefinition, PropertyDefinition> {

  /**
   * Creates an instance of the DefaultBeanDefinition class initialized to the specified id and class name.
   * @param id a String value indication the unique identifier of the bean definition.
   * @param className the fully qualified class name of the bean.
   */
  public DefaultBeanDefinition(final String id, final String className) {
    super(id, className);
  }

}
