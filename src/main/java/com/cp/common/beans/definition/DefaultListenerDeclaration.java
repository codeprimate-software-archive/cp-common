/*
 * DefaultListenerDeclaration.java (c) 10 January 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.1.10
 * @see com.cp.common.beans.definition.AbstractListenerDeclaration
 */

package com.cp.common.beans.definition;

public class DefaultListenerDeclaration extends AbstractListenerDeclaration {

  /**
   * Creates an instance of the DefaultListenerDeclaration class initialized with the fully qualified class name
   * of the listener.
   * @param className a String value specifying the fully qualified class name of the listener.
   */
  public DefaultListenerDeclaration(final String className) {
    super(className);
  }

}
