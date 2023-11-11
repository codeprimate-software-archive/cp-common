/*
 * DefaultPropertyDefinition.java (c) 10 January 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.1.10
 * @see com.cp.common.beans.definition.AbstractPropertyDefinition
 */

package com.cp.common.beans.definition;

public class DefaultPropertyDefinition extends AbstractPropertyDefinition {

  /**
   * Creates an instance of the DefaultPropertyDefinition initialized with the specified property name
   * and fully qualified class name.
   * @param propertyName a String value specifying the name of the property.
   * @param className a String value specifying the fully qualified class name of the property's type.
   */
  public DefaultPropertyDefinition(final String propertyName, final String className) {
    super(propertyName, className);
  }

}
