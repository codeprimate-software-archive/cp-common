/*
 * DefaultListenerDefinition.java (c) 17 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.12.17
 * @see com.cp.common.beans.definition.AbstractListenerDefinition
 */

package com.cp.common.beans.definition;

public class DefaultListenerDefinition extends AbstractListenerDefinition {

  /**
   * Creates an instance of the DefaultListenerDefinition class initialized with the fully qualified class name
   * of the listener.
   * @param className a String value specifying the fully qualified class name of the listener.
   */
  public DefaultListenerDefinition(final String className) {
    super(className);
  }

}
