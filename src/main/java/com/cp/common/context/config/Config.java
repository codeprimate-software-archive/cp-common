/*
 * Config.java (c) 19 February 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.12.18
 * @see com.cp.common.util.ConfigurationException
 */

package com.cp.common.context.config;

import com.cp.common.util.ConfigurationException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

public interface Config {

  public static final boolean DEFAULT_FAIL_ON_MISSING_PROPERTY = true;

  /**
   * Determines whether the specified configuration information contains a property with the given name.
   * @param propertyName a String name of the property in the configuration information.
   * @return a boolean value indicating whether the specified configuration information contains a  property
   * with the given name.
   */
  public boolean contains(String propertyName);

  /**
   * Gets the value of the specified property by name as a BigDecimal.
   * @param propertyName the String name of the property.
   * @return a BigDecimal value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the BigDecimal type.
   */
  public BigDecimal getBigDecimalPropertyValue(String propertyName) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a BigDecimal.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a BigDecimal value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the BigDecimal type.
   */
  public BigDecimal getBigDecimalPropertyValue(String propertyName, boolean failOnMissingProperty) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a BigDecimal.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a BigDecimal value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the BigDecimal type.
   */
  public BigDecimal getBigDecimalPropertyValue(String propertyName, BigDecimal defaultValue) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a BigInteger.
   * @param propertyName the String name of the property.
   * @return a BigInteger value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the BigInteger type.
   */
  public BigInteger getBigIntegerPropertyValue(String propertyName) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a BigInteger.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a BigInteger value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the BigInteger type.
   */
  public BigInteger getBigIntegerPropertyValue(String propertyName, boolean failOnMissingProperty) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a BigInteger.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a BigInteger value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the BigInteger type.
   */
  public BigInteger getBigIntegerPropertyValue(String propertyName, BigInteger defaultValue) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Boolean.
   * @param propertyName the String name of the property.
   * @return a Boolean value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Boolean type.
   */
  public Boolean getBooleanPropertyValue(String propertyName) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Boolean.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Boolean value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Boolean type.
   */
  public Boolean getBooleanPropertyValue(String propertyName, boolean failOnMissingProperty) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Boolean.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Boolean value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Boolean type.
   */
  public Boolean getBooleanPropertyValue(String propertyName, Boolean defaultValue) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Byte.
   * @param propertyName the String name of the property.
   * @return a Byte value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Byte type.
   */
  public Byte getBytePropertyValue(String propertyName) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Byte.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Byte value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Byte type.
   */
  public Byte getBytePropertyValue(String propertyName, boolean failOnMissingProperty) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Byte.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Byte value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Byte type.
   */
  public Byte getBytePropertyValue(String propertyName, Byte defaultValue) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Calendar.
   * @param propertyName the String name of the property.
   * @return a Calendar value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Calendar type.
   */
  public Calendar getCalendarPropertyValue(String propertyName) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Calendar.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Calendar value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Calendar type.
   */
  public Calendar getCalendarPropertyValue(String propertyName, boolean failOnMissingProperty) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Calendar.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Calendar value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Calendar type.
   */
  public Calendar getCalendarPropertyValue(String propertyName, Calendar defaultValue) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Calendar.
   * @param propertyName the String name of the property.
   * @param dateFormatPattern the regular expression pattern used to parse the date/time String into a Calendar object.
   * @return a Calendar value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Calendar type.
   */
  public Calendar getCalendarPropertyValue(String propertyName, String dateFormatPattern) throws ConfigurationException;

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
  public Calendar getCalendarPropertyValue(String propertyName, String dateFormatPattern, boolean failOnMissingProperty) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Calendar.
   * @param propertyName the String name of the property.\
   * @param dateFormatPattern the regular expression pattern used to parse the date/time String into a Calendar object.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Calendar value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Calendar type.
   */
  public Calendar getCalendarPropertyValue(String propertyName, String dateFormatPattern, Calendar defaultValue) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Character.
   * @param propertyName the String name of the property.
   * @return a Character value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Character type.
   */
  public Character getCharacterPropertyValue(String propertyName) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Character.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Character value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Character type.
   */
  public Character getCharacterPropertyValue(String propertyName, boolean failOnMissingProperty) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Character.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Character value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Character type.
   */
  public Character getCharacterPropertyValue(String propertyName, Character defaultValue) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Date.
   * @param propertyName the String name of the property.
   * @return a Date value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Date type.
   */
  public Date getDatePropertyValue(String propertyName) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Date.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Date value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Date type.
   */
  public Date getDatePropertyValue(String propertyName, boolean failOnMissingProperty) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Date.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Date value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Date type.
   */
  public Date getDatePropertyValue(String propertyName, Date defaultValue) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Date.
   * @param propertyName the String name of the property.
   * @param dateFormatPattern the regular expression pattern used to parse the date/time String into a Calendar object.
   * @return a Date value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Date type.
   */
  public Date getDatePropertyValue(String propertyName, String dateFormatPattern) throws ConfigurationException;

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
  public Date getDatePropertyValue(String propertyName, String dateFormatPattern, boolean failOnMissingProperty) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Date.
   * @param propertyName the String name of the property.
   * @param dateFormatPattern the regular expression pattern used to parse the date/time String into a Calendar object.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Date value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Date type.
   */
  public Date getDatePropertyValue(String propertyName, String dateFormatPattern, Date defaultValue) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Double.
   * @param propertyName the String name of the property.
   * @return a Double value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Double type.
   */
  public Double getDoublePropertyValue(String propertyName) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Double.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Double value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Double type.
   */
  public Double getDoublePropertyValue(String propertyName, boolean failOnMissingProperty) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Double.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Double value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Double type.
   */
  public Double getDoublePropertyValue(String propertyName, Double defaultValue) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Float.
   * @param propertyName the String name of the property.
   * @return a Float value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Float type.
   */
  public Float getFloatPropertyValue(String propertyName) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Float.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Float value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Float type.
   */
  public Float getFloatPropertyValue(String propertyName, boolean failOnMissingProperty) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Float.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Float value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Float type.
   */
  public Float getFloatPropertyValue(String propertyName, Float defaultValue) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Integer.
   * @param propertyName the String name of the property.
   * @return a Integer value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Integer type.
   */
  public Integer getIntegerPropertyValue(String propertyName) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Integer.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Integer value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Integer type.
   */
  public Integer getIntegerPropertyValue(String propertyName, boolean failOnMissingProperty) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Integer.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Integer value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Integer type.
   */
  public Integer getIntegerPropertyValue(String propertyName, Integer defaultValue) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Long.
   * @param propertyName the String name of the property.
   * @return a Long value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Long type.
   */
  public Long getLongPropertyValue(String propertyName) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Long.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Long value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Long type.
   */
  public Long getLongPropertyValue(String propertyName, boolean failOnMissingProperty) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Long.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Long value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Long type.
   */
  public Long getLongPropertyValue(String propertyName, Long defaultValue) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Short.
   * @param propertyName the String name of the property.
   * @return a Short value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Short type.
   */
  public Short getShortPropertyValue(String propertyName) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Short.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a Short value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Short type.
   */
  public Short getShortPropertyValue(String propertyName, boolean failOnMissingProperty) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a Short.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a Short value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the Short type.
   */
  public Short getShortPropertyValue(String propertyName, Short defaultValue) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a String.
   * @param propertyName the String name of the property.
   * @return a String value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the String type.
   */
  public String getStringPropertyValue(String propertyName) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a String.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @return a String value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the String type.
   */
  public String getStringPropertyValue(String propertyName, boolean failOnMissingProperty) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name as a String.
   * @param propertyName the String name of the property.
   * @param defaultValue the default value to return if the property specified by name does not exist or is null.
   * @return a String value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the String type.
   */
  public String getStringPropertyValue(String propertyName, String defaultValue) throws ConfigurationException;

  /**
   * Gets the value of the specified property by name.
   * @param propertyName the String name of the property.
   * @param failOnMissingProperty a boolean value indicating whether the act of accessing the properties value
   * should fail if the property does not exist.
   * @param valueStrategy the GetValueStrategy implementation used to cast or convert the property value to the
   * appropriate type.
   * @return the value for the specified property.
   * @throws ConfigurationException if the property specified by name does not exist or the value of the property
   * is not of the user-defined type.
   */
  public <T> T getPropertyValue(final String propertyName, final GetValueStrategy<T> valueStrategy, final boolean failOnMissingProperty) throws ConfigurationException;

  /**
   * Interface defining the contract to convert a given value to the appropriate object type.  If the given value
   * is null, than a default value can be provided.
   */
  public static interface GetValueStrategy<T> {

    public T getDefaultValue();

    public boolean isDefaultValueNull();

    public T getValue(String value);

  }

}
