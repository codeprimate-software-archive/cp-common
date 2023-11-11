/*
 * DefaultInvocationArgument.java (c) 6 March 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.3.6
 * @see com.cp.common.beans.definition.AbstractInvocationArgument
 */

package com.cp.common.beans.definition;

public class DefaultInvocationArgument extends AbstractInvocationArgument {

  /**
   * Creates an instance of the DefaultInvocationArgument class initialized with the specified type and value.
   * @param type a String value specifying the fully-qualified class name of the invocation argument type.
   * @param value a String representation of the invocation argument's value.
   */
  public DefaultInvocationArgument(final String type, final String value) {
    super(type, value);
  }

}
