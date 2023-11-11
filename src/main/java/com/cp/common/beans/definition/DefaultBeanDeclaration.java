/*
 * DefaultBeanDeclaration.java (c) 10 January 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.1.10
 * @see com.cp.common.beans.definition.AbstractBeanDeclaration
 */

package com.cp.common.beans.definition;

public class DefaultBeanDeclaration extends AbstractBeanDeclaration {

  /**
   * Creates an instance of the DefaultBeanDeclaration class initialized to the specified id and class name.
   * @param id a String value indication the unique identifier of the bean definition.
   * @param className the fully qualified class name of the bean.
   */
  public DefaultBeanDeclaration(final String id, final String className) {
    super(id, className);
  }

}
