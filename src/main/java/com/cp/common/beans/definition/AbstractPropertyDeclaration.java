/*
 * AbstractPropertyDeclaration.java (c) 17 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.8.7
 * @see com.cp.common.beans.definition.AbstractPropertyDefinition
 * @see com.cp.common.beans.definition.PropertyDeclaration
 */

package com.cp.common.beans.definition;

public abstract class AbstractPropertyDeclaration extends AbstractPropertyDefinition implements PropertyDeclaration {

  private String formatPattern;
  private String refId;
  private String value;

  /**
   * Creates an instance of the AbstractPropertyDeclaration initialized with the specified property name
   * and fully qualified class name.
   * @param propertyName a String value specifying the name of the property.
   * @param className a String value specifying the fully qualified class name of the property's type.
   */
  public AbstractPropertyDeclaration(final String propertyName, final String className) {
    super(propertyName, className);
  }

  /**
   * Gets a String indicating the pattern used to format the property's value.
   * @return a String value indicating the pattern used to format the property's value.
   */
  public String getFormatPattern() {
    return formatPattern;
  }

  /**
   * Sets a String indicating the pattern used to format the property's value.
   * @param formatPattern a String value indicating the pattern used to format the property's value.
   */
  public void setFormatPattern(final String formatPattern) {
    this.formatPattern = formatPattern;
  }

  /**
   * Gets a String value used as an id reference to another bean definition in a bean configuration file.
   * @return a String value used as an id reference to another bean definitio in a bean configuration file.
   */
  public String getRefId() {
    return refId;
  }

  /**
   * Sets a String value used as an id reference to another bean definition in a bean configuration file.
   * @param refId a String value used as an id reference to another bean definitio in a bean configuration file.
   */
  public void setRefId(final String refId) {
    this.refId = refId;
  }

  /**
   * Gets the value of the property.
   * @return a String value indicating the property's value.
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the value of the property.
   * @param value a String value indicating the property's value.
   */
  public void setValue(final String value) {
    this.value = value;
  }

  /**
   * Gets a String containing the internal state of this PropertyDeclaration.
   * @return a String value containing the internal state of this PropertyDeclaration.
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{className = ");
    buffer.append(getClassName());
    buffer.append(", formatPattern = ").append(getFormatPattern());
    buffer.append(", name = ").append(getName());
    buffer.append(", refId = ").append(getRefId());
    buffer.append(", value = ").append(getValue());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
