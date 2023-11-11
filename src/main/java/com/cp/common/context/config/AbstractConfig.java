/*
 * AbstractConfig.java (c) 19 February 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.12.18
 * @see com.cp.common.context.config.Config
 * @see com.cp.common.context.config.Config.GetValueStrategy
 * @see com.cp.common.lang.Assert
 * @see com.cp.common.lang.ObjectUtil
 * @see com.cp.common.util.ConfigurationException
 * @see com.cp.common.util.DateUtil
 * @see java.util.MissingResourceException
 */

package com.cp.common.context.config;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ConfigurationException;
import com.cp.common.util.DateUtil;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.MissingResourceException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractConfig implements Config {

  private final Config parentConfig;

  protected final Log logger = LogFactory.getLog(getClass());

  /**
   * Creates an instance of the AbstractConfig class without a parent Config object.
   */
  public AbstractConfig() {
    this(null);
  }

  /**
   * Creates an instance of the AbstractConfig class initialized with the specified parent Config object.
   * @param parentConfig the parent Config object used in property value lookup operations if the property
   * cannot be found in the current property configuration.
   */
  public AbstractConfig(final Config parentConfig) {
    Assert.notSame(this, parentConfig, "The parent config cannot be the same as this object!");
    this.parentConfig = parentConfig;
  }

  /**
   * Determines whether the specified configuration information contains a property with the given name.
   * @param propertyName a String name of the property in the configuration information.
   * @return a boolean value indicating whether the specified configuration information contains a  property
   * with the given name.
   */
  public boolean contains(final String propertyName) {
    return (ObjectUtil.isNotNull(parentConfig) && parentConfig.contains(propertyName));
  }

  /**
   * Gets the value of the specified property by name as a BigDecimal.
   * @param propertyName the String name of the property.
   * @return a BigDecimal value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the BigDecimal type.
   */
  public BigDecimal getBigDecimalPropertyValue(final String propertyName) throws ConfigurationException {
    return getPropertyValue(propertyName, GetBigDecimalValueStrategy.INSTANCE, DEFAULT_FAIL_ON_MISSING_PROPERTY);
  }

  /**
   * Gets the value of the specified property by name as a BigDecimal.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a BigDecimal value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the BigDecimal type.
   */
  public BigDecimal getBigDecimalPropertyValue(final String propertyName, final boolean failOnMissingProperty) throws ConfigurationException {
    return getPropertyValue(propertyName, GetBigDecimalValueStrategy.INSTANCE, failOnMissingProperty);
  }

  /**
   * Gets the value of the specified property by name as a BigDecimal.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a BigDecimal value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the BigDecimal type.
   */
  public BigDecimal getBigDecimalPropertyValue(final String propertyName, final BigDecimal defaultValue) throws ConfigurationException {
    return getPropertyValue(propertyName, new GetBigDecimalValueStrategy(defaultValue), false);
  }

  /**
   * Gets the value of the specified property by name as a BigInteger.
   * @param propertyName the String name of the property.
   * @return a BigInteger value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the BigInteger type.
   */
  public BigInteger getBigIntegerPropertyValue(final String propertyName) throws ConfigurationException {
    return getPropertyValue(propertyName, GetBigIntegerValueStrategy.INSTANCE, DEFAULT_FAIL_ON_MISSING_PROPERTY);
  }

  /**
   * Gets the value of the specified property by name as a BigInteger.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a BigInteger value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the BigInteger type.
   */
  public BigInteger getBigIntegerPropertyValue(final String propertyName, final boolean failOnMissingProperty) throws ConfigurationException {
    return getPropertyValue(propertyName, GetBigIntegerValueStrategy.INSTANCE, failOnMissingProperty);
  }

  /**
   * Gets the value of the specified property by name as a BigInteger.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a BigInteger value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the BigInteger type.
   */
  public BigInteger getBigIntegerPropertyValue(final String propertyName, final BigInteger defaultValue) throws ConfigurationException {
    return getPropertyValue(propertyName, new GetBigIntegerValueStrategy(defaultValue), false);
  }

  /**
   * Gets the value of the specified property by name as a Boolean.
   * @param propertyName the String name of the property.
   * @return a Boolean value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Boolean type.
   */
  public Boolean getBooleanPropertyValue(final String propertyName) throws ConfigurationException {
    return getPropertyValue(propertyName, GetBooleanValueStrategy.INSTANCE, DEFAULT_FAIL_ON_MISSING_PROPERTY);
  }

  /**
   * Gets the value of the specified property by name as a Boolean.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Boolean value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Boolean type.
   */
  public Boolean getBooleanPropertyValue(final String propertyName, final boolean failOnMissingProperty) throws ConfigurationException {
    return getPropertyValue(propertyName, GetBooleanValueStrategy.INSTANCE, failOnMissingProperty);
  }

  /**
   * Gets the value of the specified property by name as a Boolean.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Boolean value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Boolean type.
   */
  public Boolean getBooleanPropertyValue(final String propertyName, final Boolean defaultValue) throws ConfigurationException {
    return getPropertyValue(propertyName, new GetBooleanValueStrategy(defaultValue), false);
  }

  /**
   * Gets the value of the specified property by name as a Byte.
   * @param propertyName the String name of the property.
   * @return a Byte value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Byte type.
   */
  public Byte getBytePropertyValue(final String propertyName) throws ConfigurationException {
    return getPropertyValue(propertyName, GetByteValueStrategy.INSTANCE, DEFAULT_FAIL_ON_MISSING_PROPERTY);
  }

  /**
   * Gets the value of the specified property by name as a Byte.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Byte value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Byte type.
   */
  public Byte getBytePropertyValue(final String propertyName, final boolean failOnMissingProperty) throws ConfigurationException {
    return getPropertyValue(propertyName, GetByteValueStrategy.INSTANCE, failOnMissingProperty);
  }

  /**
   * Gets the value of the specified property by name as a Byte.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Byte value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Byte type.
   */
  public Byte getBytePropertyValue(final String propertyName, final Byte defaultValue) throws ConfigurationException {
    return getPropertyValue(propertyName, new GetByteValueStrategy(defaultValue), false);
  }

  /**
   * Gets the value of the specified property by name as a Calendar.
   * @param propertyName the String name of the property.
   * @return a Calendar value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Calendar type.
   */
  public Calendar getCalendarPropertyValue(final String propertyName) throws ConfigurationException {
    return getPropertyValue(propertyName, GetCalendarValueStrategy.INSTANCE, DEFAULT_FAIL_ON_MISSING_PROPERTY);
  }

  /**
   * Gets the value of the specified property by name as a Calendar.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Calendar value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Calendar type.
   */
  public Calendar getCalendarPropertyValue(final String propertyName, final boolean failOnMissingProperty) throws ConfigurationException {
    return getPropertyValue(propertyName, GetCalendarValueStrategy.INSTANCE, failOnMissingProperty);
  }

  /**
   * Gets the value of the specified property by name as a Calendar.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Calendar value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Calendar type.
   */
  public Calendar getCalendarPropertyValue(final String propertyName, final Calendar defaultValue) throws ConfigurationException {
    return getPropertyValue(propertyName, new GetCalendarValueStrategy(defaultValue), false);
  }

  /**
   * Gets the value of the specified property by name as a Calendar.
   * @param propertyName the String name of the property.
   * @param dateFormatPattern the regular expression pattern used to parse the date/time String into a Calendar object.
   * @return a Calendar value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Calendar type.
   */
  public Calendar getCalendarPropertyValue(final String propertyName, final String dateFormatPattern) throws ConfigurationException {
    return getPropertyValue(propertyName, new GetCalendarValueStrategy(null, dateFormatPattern), DEFAULT_FAIL_ON_MISSING_PROPERTY);
  }

  /**
   * Gets the value of the specified property by name as a Calendar.
   * @param propertyName the String name of the property.
   * @param dateFormatPattern the regular expression pattern used to parse the date/time String into a Calendar object.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Calendar value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Calendar type.
   */
  public Calendar getCalendarPropertyValue(final String propertyName, final String dateFormatPattern, final boolean failOnMissingProperty) throws ConfigurationException {
    return getPropertyValue(propertyName, new GetCalendarValueStrategy(null, dateFormatPattern), failOnMissingProperty);
  }

  /**
   * Gets the value of the specified property by name as a Calendar.
   * @param propertyName the String name of the property.\
   * @param dateFormatPattern the regular expression pattern used to parse the date/time String into a Calendar object.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Calendar value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Calendar type.
   */
  public Calendar getCalendarPropertyValue(final String propertyName, final String dateFormatPattern, final Calendar defaultValue) throws ConfigurationException {
    return getPropertyValue(propertyName, new GetCalendarValueStrategy(defaultValue, dateFormatPattern), false);
  }

  /**
   * Gets the value of the specified property by name as a Character.
   * @param propertyName the String name of the property.
   * @return a Character value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Character type.
   */
  public Character getCharacterPropertyValue(final String propertyName) throws ConfigurationException {
    return getPropertyValue(propertyName, GetCharacterValueStrategy.INSTANCE, DEFAULT_FAIL_ON_MISSING_PROPERTY);
  }

  /**
   * Gets the value of the specified property by name as a Character.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Character value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Character type.
   */
  public Character getCharacterPropertyValue(final String propertyName, final boolean failOnMissingProperty) throws ConfigurationException {
    return getPropertyValue(propertyName, GetCharacterValueStrategy.INSTANCE, failOnMissingProperty);
  }

  /**
   * Gets the value of the specified property by name as a Character.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Character value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Character type.
   */
  public Character getCharacterPropertyValue(final String propertyName, final Character defaultValue) throws ConfigurationException {
    return getPropertyValue(propertyName, new GetCharacterValueStrategy(defaultValue), false);
  }

  /**
   * Gets the value of the specified property by name as a Date.
   * @param propertyName the String name of the property.
   * @return a Date value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Date type.
   */
  public Date getDatePropertyValue(final String propertyName) throws ConfigurationException {
    return getPropertyValue(propertyName, GetDateValueStrategy.INSTANCE, DEFAULT_FAIL_ON_MISSING_PROPERTY);
  }

  /**
   * Gets the value of the specified property by name as a Date.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Date value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Date type.
   */
  public Date getDatePropertyValue(final String propertyName, final boolean failOnMissingProperty) throws ConfigurationException {
    return getPropertyValue(propertyName, GetDateValueStrategy.INSTANCE, failOnMissingProperty);
  }

  /**
   * Gets the value of the specified property by name as a Date.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Date value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Date type.
   */
  public Date getDatePropertyValue(final String propertyName, final Date defaultValue) throws ConfigurationException {
    return getPropertyValue(propertyName, new GetDateValueStrategy(defaultValue), false);
  }

  /**
   * Gets the value of the specified property by name as a Date.
   * @param propertyName the String name of the property.
   * @param dateFormatPattern the regular expression pattern used to parse the date/time String into a Calendar object.
   * @return a Date value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Date type.
   */
  public Date getDatePropertyValue(final String propertyName, final String dateFormatPattern) throws ConfigurationException {
    return getPropertyValue(propertyName, new GetDateValueStrategy(null, dateFormatPattern), DEFAULT_FAIL_ON_MISSING_PROPERTY);
  }

  /**
   * Gets the value of the specified property by name as a Date.
   * @param propertyName the String name of the property.
   * @param dateFormatPattern the regular expression pattern used to parse the date/time String into a Calendar object.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Date value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Date type.
   */
  public Date getDatePropertyValue(final String propertyName, final String dateFormatPattern, final boolean failOnMissingProperty) throws ConfigurationException {
    return getPropertyValue(propertyName, new GetDateValueStrategy(null, dateFormatPattern), failOnMissingProperty);
  }

  /**
   * Gets the value of the specified property by name as a Date.
   * @param propertyName the String name of the property.
   * @param dateFormatPattern the regular expression pattern used to parse the date/time String into a Calendar object.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Date value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Date type.
   */
  public Date getDatePropertyValue(final String propertyName, final String dateFormatPattern, final Date defaultValue) throws ConfigurationException {
    return getPropertyValue(propertyName, new GetDateValueStrategy(defaultValue, dateFormatPattern), false);
  }

  /**
   * Gets the value of the specified property by name as a Double.
   * @param propertyName the String name of the property.
   * @return a Double value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Double type.
   */
  public Double getDoublePropertyValue(final String propertyName) throws ConfigurationException {
    return getPropertyValue(propertyName, GetDoubleValueStrategy.INSTANCE, DEFAULT_FAIL_ON_MISSING_PROPERTY);
  }

  /**
   * Gets the value of the specified property by name as a Double.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Double value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Double type.
   */
  public Double getDoublePropertyValue(final String propertyName, final boolean failOnMissingProperty) throws ConfigurationException {
    return getPropertyValue(propertyName, GetDoubleValueStrategy.INSTANCE, failOnMissingProperty);
  }

  /**
   * Gets the value of the specified property by name as a Double.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Double value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Double type.
   */
  public Double getDoublePropertyValue(final String propertyName, final Double defaultValue) throws ConfigurationException {
    return getPropertyValue(propertyName, new GetDoubleValueStrategy(defaultValue), false);
  }

  /**
   * Gets the value of the specified property by name as a Float.
   * @param propertyName the String name of the property.
   * @return a Float value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Float type.
   */
  public Float getFloatPropertyValue(final String propertyName) throws ConfigurationException {
    return getPropertyValue(propertyName, GetFloatValueStrategy.INSTANCE, DEFAULT_FAIL_ON_MISSING_PROPERTY);
  }

  /**
   * Gets the value of the specified property by name as a Float.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Float value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Float type.
   */
  public Float getFloatPropertyValue(final String propertyName, final boolean failOnMissingProperty) throws ConfigurationException {
    return getPropertyValue(propertyName, GetFloatValueStrategy.INSTANCE, failOnMissingProperty);
  }

  /**
   * Gets the value of the specified property by name as a Float.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Float value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Float type.
   */
  public Float getFloatPropertyValue(final String propertyName, final Float defaultValue) throws ConfigurationException {
    return getPropertyValue(propertyName, new GetFloatValueStrategy(defaultValue), false);
  }

  /**
   * Gets the value of the specified property by name as a Integer.
   * @param propertyName the String name of the property.
   * @return a Integer value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Integer type.
   */
  public Integer getIntegerPropertyValue(final String propertyName) throws ConfigurationException {
    return getPropertyValue(propertyName, GetIntegerValueStrategy.INSTANCE, DEFAULT_FAIL_ON_MISSING_PROPERTY);
  }

  /**
   * Gets the value of the specified property by name as a Integer.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Integer value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Integer type.
   */
  public Integer getIntegerPropertyValue(final String propertyName, final boolean failOnMissingProperty) throws ConfigurationException {
    return getPropertyValue(propertyName, GetIntegerValueStrategy.INSTANCE, failOnMissingProperty);
  }

  /**
   * Gets the value of the specified property by name as a Integer.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Integer value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Integer type.
   */
  public Integer getIntegerPropertyValue(final String propertyName, final Integer defaultValue) throws ConfigurationException {
    return getPropertyValue(propertyName, new GetIntegerValueStrategy(defaultValue), false);
  }

  /**
   * Gets the value of the specified property by name as a Long.
   * @param propertyName the String name of the property.
   * @return a Long value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Long type.
   */
  public Long getLongPropertyValue(final String propertyName) throws ConfigurationException {
    return getPropertyValue(propertyName, GetLongValueStrategy.INSTANCE, DEFAULT_FAIL_ON_MISSING_PROPERTY);
  }

  /**
   * Gets the value of the specified property by name as a Long.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Long value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Long type.
   */
  public Long getLongPropertyValue(final String propertyName, final boolean failOnMissingProperty) throws ConfigurationException {
    return getPropertyValue(propertyName, GetLongValueStrategy.INSTANCE, failOnMissingProperty);
  }

  /**
   * Gets the value of the specified property by name as a Long.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Long value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Long type.
   */
  public Long getLongPropertyValue(final String propertyName, final Long defaultValue) throws ConfigurationException {
    return getPropertyValue(propertyName, new GetLongValueStrategy(defaultValue), false);
  }

  /**
   * Gets the value of the specified property by name as a Short.
   * @param propertyName the String name of the property.
   * @return a Short value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Short type.
   */
  public Short getShortPropertyValue(final String propertyName) throws ConfigurationException {
    return getPropertyValue(propertyName, GetShortValueStrategy.INSTANCE, DEFAULT_FAIL_ON_MISSING_PROPERTY);
  }

  /**
   * Gets the value of the specified property by name as a Short.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Short value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Short type.
   */
  public Short getShortPropertyValue(final String propertyName, final boolean failOnMissingProperty) throws ConfigurationException {
    return getPropertyValue(propertyName, GetShortValueStrategy.INSTANCE, failOnMissingProperty);
  }

  /**
   * Gets the value of the specified property by name as a Short.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Short value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Short type.
   */
  public Short getShortPropertyValue(final String propertyName, final Short defaultValue) throws ConfigurationException {
    return getPropertyValue(propertyName, new GetShortValueStrategy(defaultValue), false);
  }

  /**
   * Gets the value of the specified property by name as a String.
   * @param propertyName the String name of the property.
   * @return a String value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the String type.
   */
  public String getStringPropertyValue(final String propertyName) throws ConfigurationException {
    return getPropertyValue(propertyName, GetStringValueStrategy.INSTANCE, DEFAULT_FAIL_ON_MISSING_PROPERTY);
  }

  /**
   * Gets the value of the specified property by name as a String.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a String value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the String type.
   */
  public String getStringPropertyValue(final String propertyName, final boolean failOnMissingProperty) throws ConfigurationException {
    return getPropertyValue(propertyName, GetStringValueStrategy.INSTANCE, failOnMissingProperty);
  }

  /**
   * Gets the value of the specified property by name as a String.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a String value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the String type.
   */
  public String getStringPropertyValue(final String propertyName, final String defaultValue) throws ConfigurationException {
    return getPropertyValue(propertyName, new GetStringValueStrategy(defaultValue), false);
  }

  /**
   * Returns the parent Config object used to obtain configuration information.
   * @return a Config object designated as the parent of this Config object.
   */
  protected Config getParentConfig() {
    return parentConfig;
  }

  /**
   * Returns the String value for the property with the specified name.  The method accepts a GetValueStrategy
   * to handle property value conversions to the appropriate wrapper object type and default values.  This method
   * will throw a MissingResourceException if the property is not defined in the property configuration resource,
   * and the failOnMissingProperty boolean value is set to true.
   * @param propertyName the name of the property for which the value of will be retrieved.
   * @param valueStrategy the GetValueStrategy used to get the value as an appropriate Object data type.
   * @param failOnMissingProperty a boolean value indicating whether to throw a MissingResourceException if the
   * property specified by name is not defined in the configuration file and the default String value is null.
   * @return a String value of the property specified by name as defined in the configuration file, or the
   * default String value if the property specified by name is not defined in the configuration file.
   * @throws ConfigurationException if there is a problem reading from the property configuration resource.
   * @throws MissingResourceException if the  property specified by name is not defined in the property configuration
   * resource and the failOnMissingProperty is set to true.
   * @see AbstractConfig#getPropertyValueImpl(String)
   */
  public <T> T getPropertyValue(final String propertyName, final GetValueStrategy<T> valueStrategy, final boolean failOnMissingProperty) throws ConfigurationException {
    if (logger.isDebugEnabled()) {
      logger.debug("property name (" + propertyName + ")");
      logger.debug("value strategy (" + valueStrategy + ")");
      logger.debug("fail on missing property (" + failOnMissingProperty + ")");
    }

    Assert.notEmpty(propertyName, "The name of the property cannot be null or empty!");

    try {
      return valueStrategy.getValue(getPropertyValueImpl(propertyName));
    }
    catch (MissingResourceException e) {
      logger.warn(e.getMessage());

      if (ObjectUtil.isNotNull(getParentConfig())) {
        return getParentConfig().getPropertyValue(propertyName, valueStrategy, failOnMissingProperty);
      }
      else {
        if (failOnMissingProperty) {
          throw e;
        }
        else {
          return valueStrategy.getDefaultValue();
        }
      }
    }
  }

  /**
   * Gets the value of the property specified name from the actual Config implementation based on some
   * property resource.  This method will throw a MissingResourceException if the property specified by name
   * is not defined in the property configuration resource.
   * @param propertyName the name of the property of interest.
   * @return the String value of the property specified by name from the property configuration resource.
   * @throws ConfigurationException if the configuration resource could not be accessed.
   * @throws MissingResourceException if the property specified by name is not defined in the property configuration
   * resource.
   * @see AbstractConfig#getPropertyValue(String, GetValueStrategy, boolean)
   */
  protected abstract String getPropertyValueImpl(String propertyName) throws ConfigurationException;

  /**
   * Abstract partial implementation of the GetValueStrategy interface holding the default value.
   * @see GetValueStrategy
   */
  protected static abstract class AbstractGetValueStrategy<T> implements GetValueStrategy<T> {

    private final T defaultValue;

    public AbstractGetValueStrategy(final T defaultValue) {
      this.defaultValue = defaultValue;
    }

    public T getDefaultValue() {
      return defaultValue;
    }

    public boolean isDefaultValueNull() {
      return ObjectUtil.isNull(getDefaultValue());
    }

    public String toString() {
      final StringBuffer buffer = new StringBuffer("{defaultValue = ");
      buffer.append(getDefaultValue());
      buffer.append(", defaultValueNull = ").append(isDefaultValueNull());
      buffer.append("}:").append(getClass().getName());
      return buffer.toString();
    }
  }

  /**
   * Abstract extended implementation of the AbstractGetValueStrategy class holding format information for parsing
   * time-based property values.
   * @see AbstractGetValueStrategy
   */
  protected static abstract class AbstractGetTimeBasedValueStrategy<T> extends AbstractGetValueStrategy<T> {

    private final String dateFormatPattern;

    public AbstractGetTimeBasedValueStrategy(final T defaultValue) {
      this(defaultValue, DateUtil.DEFAULT_DATE_FORMAT_PATTERN);
    }

    public AbstractGetTimeBasedValueStrategy(final T defaultValue, final String dateFormatPattern) {
      super(defaultValue);
      this.dateFormatPattern = ObjectUtil.getDefaultValue(dateFormatPattern, DateUtil.DEFAULT_DATE_FORMAT_PATTERN);
    }

    protected String getDateFormatPattern() {
      return dateFormatPattern;
    }
  }

  /**
   * Implementation of the GetValueStrategy interface to convert String values into BigDecimals.
   * @see AbstractGetValueStrategy
   */
  protected static final class GetBigDecimalValueStrategy extends AbstractGetValueStrategy<BigDecimal> {

    public static final GetBigDecimalValueStrategy INSTANCE = new GetBigDecimalValueStrategy(null);

    public GetBigDecimalValueStrategy(final BigDecimal defaultValue) {
      super(defaultValue);
    }

    public BigDecimal getValue(final String value) {
      return new BigDecimal(value);
    }
  }

  /**
   * Implementation of the GetValueStrategy interface to convert String values into BigIntegers.
   * @see AbstractGetValueStrategy
   */
  protected static final class GetBigIntegerValueStrategy extends AbstractGetValueStrategy<BigInteger> {

    public static final GetBigIntegerValueStrategy INSTANCE = new GetBigIntegerValueStrategy(null);

    public GetBigIntegerValueStrategy(final BigInteger defaultValue) {
      super(defaultValue);
    }

    public BigInteger getValue(final String value) {
      return new BigInteger(value);
    }
  }

  /**
   * Implementation of the GetValueStrategy interface to convert String values into Booleans.
   * @see AbstractGetValueStrategy
   */
  protected static final class GetBooleanValueStrategy extends AbstractGetValueStrategy<Boolean> {

    public static final GetBooleanValueStrategy INSTANCE = new GetBooleanValueStrategy(null);

    public GetBooleanValueStrategy(final Boolean defaultValue) {
      super(defaultValue);
    }

    public Boolean getValue(final String value) {
      return Boolean.valueOf(value);
    }
  }

  /**
   * Implementation of the GetValueStrategy interface to convert String values into Bytes.
   * @see AbstractGetValueStrategy
   */
  protected static final class GetByteValueStrategy extends AbstractGetValueStrategy<Byte> {

    public static final GetByteValueStrategy INSTANCE = new GetByteValueStrategy(null);

    public GetByteValueStrategy(final Byte defaultValue) {
      super(defaultValue);
    }

    public Byte getValue(final String value) {
      return Byte.valueOf(value);
    }
  }

  /**
   * Implementation of the GetValueStrategy interface to convert String values into Calendars.
   * @see AbstractGetValueStrategy
   */
  protected static final class GetCalendarValueStrategy extends AbstractGetTimeBasedValueStrategy<Calendar> {

    public static final GetCalendarValueStrategy INSTANCE = new GetCalendarValueStrategy(null);

    public GetCalendarValueStrategy(final Calendar defaultValue) {
      super(defaultValue);
    }

    public GetCalendarValueStrategy(final Calendar defaultValue, final String dateFormatPattern) {
      super(defaultValue, dateFormatPattern);
    }

    public Calendar getValue(final String value) {
      return DateUtil.getCalendar(value, getDateFormatPattern());
    }
  }

  /**
   * Implementation of the GetValueStrategy interface to convert String values into Characters.
   * @see AbstractGetValueStrategy
   */
  protected static final class GetCharacterValueStrategy extends AbstractGetValueStrategy<Character> {

    public static final GetCharacterValueStrategy INSTANCE = new GetCharacterValueStrategy(null);

    public GetCharacterValueStrategy(final Character defaultValue) {
      super(defaultValue);
    }

    public Character getValue(final String value) {
      // TODO verify implementation
      return Character.valueOf(ObjectUtil.getDefaultValue(value, "").charAt(0));
    }
  }

  /**
   * Implementation of the GetValueStrategy interface to convert String values into Dates.
   * @see AbstractGetValueStrategy
   */
  protected static final class GetDateValueStrategy extends AbstractGetTimeBasedValueStrategy<Date> {

    public static final GetDateValueStrategy INSTANCE = new GetDateValueStrategy(null);

    public GetDateValueStrategy(final Date defaultValue) {
      super(defaultValue);
    }

    public GetDateValueStrategy(final Date defaultValue, final String dateFormatPattern) {
      super(defaultValue, dateFormatPattern);
    }

    public Date getValue(final String value) {
      return DateUtil.getCalendar(value, getDateFormatPattern()).getTime();
    }
  }

  /**
   * Implementation of the GetValueStrategy interface to convert String values into Doubles.
   * @see AbstractGetValueStrategy
   */
  protected static final class GetDoubleValueStrategy extends AbstractGetValueStrategy<Double> {

    public static final GetDoubleValueStrategy INSTANCE = new GetDoubleValueStrategy(null);

    public GetDoubleValueStrategy(final Double defaultValue) {
      super(defaultValue);
    }

    public Double getValue(final String value) {
      return Double.valueOf(value);
    }
  }

  /**
   * Implementation of the GetValueStrategy interface to convert String values into Floats.
   * @see AbstractGetValueStrategy
   */
  protected static final class GetFloatValueStrategy extends AbstractGetValueStrategy<Float> {

    public static final GetFloatValueStrategy INSTANCE = new GetFloatValueStrategy(null);

    public GetFloatValueStrategy(final Float defaultValue) {
      super(defaultValue);
    }

    public Float getValue(final String value) {
      return Float.valueOf(value);
    }
  }

  /**
   * Implementation of the GetValueStrategy interface to convert String values into Integers.
   * @see AbstractGetValueStrategy
   */
  protected static final class GetIntegerValueStrategy extends AbstractGetValueStrategy<Integer> {

    public static final GetIntegerValueStrategy INSTANCE = new GetIntegerValueStrategy(null);

    public GetIntegerValueStrategy(final Integer defaultValue) {
      super(defaultValue);
    }

    public Integer getValue(final String value) {
      return Integer.valueOf(value);
    }
  }

  /**
   * Implementation of the GetValueStrategy interface to convert String values into Longs.
   * @see AbstractGetValueStrategy
   */
  protected static final class GetLongValueStrategy extends AbstractGetValueStrategy<Long> {

    public static final GetLongValueStrategy INSTANCE = new GetLongValueStrategy(null);

    public GetLongValueStrategy(final Long defaultValue) {
      super(defaultValue);
    }

    public Long getValue(final String value) {
      return Long.valueOf(value);
    }
  }

  /**
   * Implementation of the GetValueStrategy interface to convert String values into Shorts.
   * @see AbstractGetValueStrategy
   */
  protected static final class GetShortValueStrategy extends AbstractGetValueStrategy<Short> {

    public static final GetShortValueStrategy INSTANCE = new GetShortValueStrategy(null);

    public GetShortValueStrategy(final Short defaultValue) {
      super(defaultValue);
    }

    public Short getValue(final String value) {
      return Short.valueOf(value);
    }
  }

  /**
   * Implementation of the GetValueStrategy interface to convert String values into Strings.
   * @see AbstractGetValueStrategy
   */
  protected static final class GetStringValueStrategy extends AbstractGetValueStrategy<String> {

    public static final GetStringValueStrategy INSTANCE = new GetStringValueStrategy(null);

    public GetStringValueStrategy(final String defaultValue) {
      super(defaultValue);
    }

    public String getValue(final String value) {
      return value;
    }
  }

}
