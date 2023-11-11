/*
 * DefaultPropertyDeclaration.java (c) 17 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.12.17
 * @see com.cp.common.beans.definition.AbstractPropertyDeclaration
 */

package com.cp.common.beans.definition;

public class DefaultPropertyDeclaration extends AbstractPropertyDeclaration {

  /**
   * Creates an instance of the DefaultPropertyDeclaration initialized with the specified property name
   * and fully qualified class name.
   * @param propertyName a String value specifying the name of the property.
   * @param className a String value specifying the fully qualified class name of the property's type.
   */
  public DefaultPropertyDeclaration(final String propertyName, final String className) {
    super(propertyName, className);
  }

}
