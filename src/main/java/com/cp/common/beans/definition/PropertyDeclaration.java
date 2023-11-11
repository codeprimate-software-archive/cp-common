/*
 * PropertyDeclaration.java (c) 16 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.8.7
 * @see com.cp.common.beans.definition.PropertyDefinition
 */

package com.cp.common.beans.definition;

public interface PropertyDeclaration extends PropertyDefinition {

  /**
   * Gets a String indicating the pattern used to format the property's value.
   * @return a String value indicating the pattern used to format the property's value.
   */
  public String getFormatPattern();

  /**
   * Sets a String indicating the pattern used to format the property's value.
   * @param formatPattern a String value indicating the pattern used to format the property's value.
   */
  public void setFormatPattern(String formatPattern);

  /**
   * Gets a String value used as an id reference to another bean definition in a bean configuration file.
   * @return a String value used as an id reference to another bean definitio in a bean configuration file.
   */
  public String getRefId();

  /**
   * Sets a String value used as an id reference to another bean definition in a bean configuration file.
   * @param refId a String value used as an id reference to another bean definitio in a bean configuration file.
   */
  public void setRefId(String refId);

  /**
   * Gets the value of the property.
   * @return a String value indicating the property's value.
   */
  public String getValue();

  /**
   * Sets the value of the property.
   * @param value a String value indicating the property's value.
   */
  public void setValue(String value);

}
