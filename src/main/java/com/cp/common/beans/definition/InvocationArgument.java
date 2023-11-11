/*
 * InvocationArgument.java (c) 6 March 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.8.17
 * @see com.cp.common.beans.definition.BeanDeclaration
 * @see com.cp.common.beans.definition.ListenerDeclaration
 * @see com.cp.common.beans.definition.Parameterizable
 */

package com.cp.common.beans.definition;

public interface InvocationArgument {

  /**
   * Gets a String indicating the pattern used to format the invocation argument's value.
   * @return a String value indicating the pattern used to format the invocation arguments value.
   */
  public String getFormatPattern();

  /**
   * Sets a String indicating the pattern used to format the invocation argument's value.
   * @param formatPattern a String value indicating the pattern used to format the invocation arguments value.
   */
  public void setFormatPattern(String formatPattern);

  /**
   * Gets the value of the reference id (refid) attribute as defined/declared in the configuration file.
   * @return a String value specifying the id reference value.
   */
  public String getRefId();

  /**
   * Sets the value of the reference id (refid) attribute as defined/declared in the configuration file.
   * @param refId a String value specifying the id reference value.
   */
  public void setRefId(String refId);

  /**
   * Gets the specified type of the invocation argument as the String value specified in the bean definition file.
   * @return a String value indicating the fully qualified class name, or type of the invocation argument.
   */
  public String getStringType();

  /**
   * Sets the specified type of the invocation argument as the String value specified in the bean definition file.
   * @param type a String value indicating the fully qualified class name, or type of the invocation argument.
   */
  public void setStringType(String type);

  /**
   * Gets the value of the invocation argument as a String value.
   * @return a String value specifying the invocation argument.
   */
  public String getStringValue();

  /**
   * Sets the value of the invocation argument as a String value.
   * @param value a String value specifying the invocation argument.
   */
  public void setStringValue(String value);

  /**
   * Gets the actual fully-qualifed Class type of the invocation argument.
   * @return a Class object specifying the type of invocation argument.
   */
  public Class getType();

  /**
   * Gets the actual value of the invocation argument as an instance of the invocation argument type.
   * @return a Object value for the invocation argument.
   */
  public Object getValue();

}
