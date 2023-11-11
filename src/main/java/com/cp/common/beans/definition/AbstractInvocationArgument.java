/*
 * AbstractInvocationArgument.java (c) 6 March 2008
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see com.cp.common.beans.definition.InvocationArgument
 */

package com.cp.common.beans.definition;

import com.cp.common.beans.util.ConvertUtil;
import com.cp.common.beans.util.converters.CalendarConverter;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.SystemException;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractInvocationArgument implements InvocationArgument {

  protected final Log logger = LogFactory.getLog(getClass());

  private boolean valueEvaluated = false;

  private Object actualValue;

  private String formatPattern;
  private String refId;
  private String type;
  private String value;

  /**
   * Creates an instance of the AbstractInvocationArgument class initialized with the specified type and value.
   * @param type a String value specifying the fully-qualified class name of the invocation argument type.
   * @param value a String representation of the invocation argument's value.
   */
  public AbstractInvocationArgument(final String type, final String value) {
    setStringType(type);
    setStringValue(value);
  }

  /**
   * Gets a String indicating the pattern used to format the invocation argument's value.
   * @return a String value indicating the pattern used to format the invocation arguments value.
   */
  public String getFormatPattern() {
    return formatPattern;
  }

  /**
   * Sets a String indicating the pattern used to format the invocation argument's value.
   * @param formatPattern a String value indicating the pattern used to format the invocation arguments value.
   */
  public void setFormatPattern(final String formatPattern) {
    this.formatPattern = formatPattern;
  }

  /**
   * Gets the value of the reference id (refid) attribute as defined/declared in the configuration file.
   * @return a String value specifying the id reference value.
   */
  public String getRefId() {
    return refId;
  }

  /**
   * Sets the value of the reference id (refid) attribute as defined/declared in the configuration file.
   * @param refId a String value specifying the id reference value.
   */
  public void setRefId(final String refId) {
    this.refId = refId;
  }

  /**
   * Gets the specified type of the invocation argument as the String value specified in the bean definition file.
   * @return a String value indicating the fully qualified class name, or type of the invocation argument.
   */
  public String getStringType() {
    return type;
  }

  /**
   * Sets the specified type of the invocation argument as the String value specified in the bean definition file.
   * @param type a String value indicating the fully qualified class name, or type of the invocation argument.
   */
  public void setStringType(final String type) {
    Assert.notEmpty(type, "The type of the invocation argument must be specified!");
    this.type = type;
  }

  /**
   * Gets the value of the invocation argument as a String value.
   * @return a String value specifying the invocation argument.
   */
  public String getStringValue() {
    return value;
  }

  /**
   * Sets the value of the invocation argument as a String value.
   * @param value a String value specifying the invocation argument.
   */
  public void setStringValue(final String value) {
    Assert.notEmpty(value, "The value of the invocation argument must be specified!");
    this.valueEvaluated = ObjectUtil.equals(this.value, value);
    this.value = value;
  }

  /**
   * Gets the actual fully-qualifed Class type of the invocation argument.
   * @return a Class object specifying the type of invocation argument.
   */
  public Class getType() {
    try {
      return ClassUtil.loadClass(getStringType());
    }
    catch (ClassNotFoundException e) {
      logger.error("The class (" + getStringType() + ") could not be found in the CLASSPATH!", e);
      throw new SystemException("The class (" + getStringType() + ") could not be found in the CLASSPATH!", e);
    }
  }

  /**
   * Gets the actual value of the invocation argument as an instance of the invocation argument type.
   * @return a Object value for the invocation argument.
   */
  public synchronized Object getValue() {
    if (!valueEvaluated) {
      final Class type = getType();

      if (Calendar.class.isAssignableFrom(type)) {
        CalendarConverter.addDateFormatPattern(getFormatPattern());
      }

      actualValue = ConvertUtil.convert(type, getStringValue());
      valueEvaluated = true;
    }

    return actualValue;
  }

  /**
   * Determines whether the specified Object is equal to this InvocationArgument.
   * @param obj the Object compared for equality with this InvocationArgument.
   * @return a boolean indicating whether the specified Object is equal to this InvocationArgument.
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof InvocationArgument)) {
      return false;
    }

    final InvocationArgument that = (InvocationArgument) obj;

    return ObjectUtil.equals(getFormatPattern(), that.getFormatPattern())
      && ObjectUtil.equals(getRefId(), that.getRefId())
      && ObjectUtil.equals(getStringType(), that.getStringType())
      && ObjectUtil.equals(getStringValue(), that.getStringValue());
  }

  /**
   * Computes a hash value for this InvocationArgument based on the state of this instance.
   * @return an integer value specifying the computed hash value of this InvocationArgument instance.
   */
  @Override
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getFormatPattern());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getRefId());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getStringType());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getStringValue());
    return hashValue;
  }

  /**
   * Gets a String representation of the internal state of this invocation argument.
   * @return a String value specifying the state of this invocation argument.
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{formatPattern = ");
    buffer.append(getFormatPattern());
    buffer.append(", refId = ").append(getRefId());
    buffer.append(", stringType = ").append(getStringType());
    buffer.append(", stringValue = ").append(getStringValue());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
