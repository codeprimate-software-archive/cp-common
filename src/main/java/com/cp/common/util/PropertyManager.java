/*
 * PropertyManager.java (c) 1 April 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.2.22
 * @see com.cp.common.util.CPCommonProperties
 */

package com.cp.common.util;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

public final class PropertyManager {

  private static final Logger logger = Logger.getLogger(PropertyManager.class);

  private static final boolean DEFAULT_FAIL_FOR_MISSING_PROPERTY = true;

  private static final String DEFAULT_DATE_PATTERN = "MM/dd/yyyy";
  private static final String DEFAULT_DATE_PATTERN_PROPERTY = "cp-common.default.date.pattern";

  static PropertyManager INSTANCE;

  private Properties localProperties = new Properties();

  /**
   * Private default constructor enforcing non-instantiability of the PropertyManager class adhering to the contract
   * of the Singleton design pattern.
   */
  private PropertyManager() {
  }

  /**
   * Creates an instance of the PropertyManager class initialized with properties defined in the file located at
   * absolute path.
   * @param absolutePath a String value specifying the absolute path of the file containing properties to be managed
   * by this PropertyManager.
   * @throws IOException if the properties in the file at the absolute path cannot be read.
   */
  private PropertyManager(final String absolutePath) throws IOException {
    final BufferedInputStream inStream = new BufferedInputStream(
      PropertyManager.class.getResourceAsStream(absolutePath));
    localProperties.load(inStream);
    inStream.close();
  }

  /**
   * Returns the Singleton instance of the PropertyManager class.
   *
   * Once an instance of the PropertyManager class is created, that same instance will be returned on subsequent calls
   * to the getInstance method.  The single instance is lazily instantiated, however, once created, the getInstance
   * method ensures that no other instance can be created.
   * @return the single instance of this PropertyManager class.
   * @see PropertyManager#getInstance(String)
   */
  public static PropertyManager getInstance() {
    if (ObjectUtil.isNull(INSTANCE)) {
      synchronized (PropertyManager.class) {
        if (ObjectUtil.isNull(INSTANCE)) {
          INSTANCE = new PropertyManager();
        }
      }
    }
    return INSTANCE;
  }

  /**
   * Returns a Singleton instance of the PropertyManager class initialized with properties defined in the file located
   * at absolute path.  The properties in the file located at absolute path are used to override the default
   * configuration properties of the cp-common Java Class Library.  Only properties that need to be overridden need be
   * defined in the file located at absolute path.  If a specific, existing property is not overridden, the default
   * property configuration for the cp-common Java Class Library will be used.
   *
   * Once an instance of the PropertyManager class is created, that same instance will be returned on subsequent calls
   * to the getInstance method.  The single instance is lazily instantiated, however, once created, the getInstance
   * method ensures that no other instance can be created.
   * @param absolutePath a String value indicating the absolute path to a file containing properties used to
   * initialize the PropertyManager.
   * @return the single instance of the PropertyManager class.
   * @throws IOException if an IO error occurs while reading properties from the file at absolute path.
   * @see PropertyManager#getInstance()
   */
  public static PropertyManager getInstance(final String absolutePath) throws IOException {
    if (ObjectUtil.isNull(INSTANCE)) {
      synchronized (PropertyManager.class) {
        if (ObjectUtil.isNull(INSTANCE)) {
          INSTANCE = new PropertyManager(absolutePath);
        }
      }
    }
    return INSTANCE;
  }

  /**
   * Returns the value of the property with the specified name as a Boolean.
   * @param propertyName the name of the property to return the value of.
   * @return the value of the specified property as Boolean object.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined.
   */
  public Boolean getBooleanPropertyValue(final String propertyName) {
    return getBooleanPropertyValue(propertyName, null, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as a Boolean.
   * @param propertyName the name of the property to return the value of.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should thrown a MissingResourceException.
   * @return the value of the specified property as a Boolean object, or null if the property is not defined
   * and the failForMissingProperty condition is set to false.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the failForMissingProperty condition is set to true.
   */
  public Boolean getBooleanPropertyValue(final String propertyName, final boolean failForMissingProperty) {
    return getBooleanPropertyValue(propertyName, null, failForMissingProperty);
  }

  /**
   * Returns the value of the property with the specified name as a Boolean.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default Boolean value returned for the specified property
   * if the property's value is null.
   * @return the value of the specified property as a Boolean object, or the default Boolean value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the default value is null.
   */
  public Boolean getBooleanPropertyValue(final String propertyName, final Boolean defaultPropertyValue) {
    return getBooleanPropertyValue(propertyName, defaultPropertyValue, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as a Boolean.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default Boolean value returned for the specified property
   * if the property's value is null.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should thrown a MissingResourceException.
   * @return the value of the specified property as a Boolean object, or the default Boolean value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined, no
   * default value has been specified and the failForMissingProperty condition is set to true.
   */
  public Boolean getBooleanPropertyValue(final String propertyName,
                                         final Boolean defaultPropertyValue,
                                         final boolean failForMissingProperty) {
    final String propertyValue = getPropertyValue(propertyName, getDefaultPropertyValue(defaultPropertyValue),
      failForMissingProperty);
    return (StringUtil.isEmpty(propertyValue) ? defaultPropertyValue : Boolean.valueOf(propertyValue));
  }

  /**
   * Returns the value of the property with the specified name as a Byte.
   * @param propertyName the name of the property to return the value of.
   * @return the value of the specified property as Byte object.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Byte getBytePropertyValue(final String propertyName) {
    return getBytePropertyValue(propertyName, null, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as a Byte.
   * @param propertyName the name of the property to return the value of.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should thrown a MissingResourceException.
   * @return the value of the specified property as a Byte object, or null if the property is not defined
   * and the failForMissingProperty condition is set to false.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the failForMissingProperty condition is set to true.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Byte getBytePropertyValue(final String propertyName, final boolean failForMissingProperty) {
    return getBytePropertyValue(propertyName, null, failForMissingProperty);
  }

  /**
   * Returns the value of the property with the specified name as a Byte.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default Byte value returned for the specified property
   * if the property's value is null.
   * @return the value of the specified property as a Byte object, or the default Byte value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the default value is null.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Byte getBytePropertyValue(final String propertyName, final Byte defaultPropertyValue) {
    return getBytePropertyValue(propertyName, defaultPropertyValue, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as a Byte.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default Byte value returned for the specified property
   * if the property's value is null.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should thrown a MissingResourceException.
   * @return the value of the specified property as a Byte object, or the default Byte value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined, no
   * default value has been specified and the failForMissingProperty condition is set to true.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Byte getBytePropertyValue(final String propertyName,
                                   final Byte defaultPropertyValue,
                                   final boolean failForMissingProperty) {
    final String propertyValue = getPropertyValue(propertyName, getDefaultPropertyValue(defaultPropertyValue),
      failForMissingProperty);
    return (StringUtil.isEmpty(propertyValue) ? defaultPropertyValue : Byte.valueOf(propertyValue));
  }

  /**
   * Returns the value of the property with the specified name as a Calendar.
   * @param propertyName the name of the property to return the value of.
   * @return the value of the specified property as Calendar object.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined.
   * @throws ParseException if the format of the specified property's value does not match the specified date format.
   */
  public Calendar getCalendarPropertyValue(final String propertyName) throws ParseException {
    return getCalendarPropertyValue(propertyName, getDefaultDatePattern(), null, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as a Calendar.
   * @param propertyName the name of the property to return the value of.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should throw a MissingResourceException.
   * @return the value of the specified property as a Calendar object, or null if the property is not defined
   * and the failForMissingProperty condition is set to false.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the failForMissingProperty condition is set to true.
   * @throws ParseException if the format of the specified property's value does not match the specified date format.
   */
  public Calendar getCalendarPropertyValue(final String propertyName, final boolean failForMissingProperty)
      throws ParseException {
    return getCalendarPropertyValue(propertyName, getDefaultDatePattern(), null, failForMissingProperty);
  }

  /**
   * Returns the value of the property with the specified name as a Calendar.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default Calendar value returned for the specified property
   * if the property's value is null.
   * @return the value of the specified property as a Calendar object, or the default Calendar value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the default value is null.
   * @throws ParseException if the format of the specified property's value does not match the specified date format.
   */
  public Calendar getCalendarPropertyValue(final String propertyName, final Calendar defaultPropertyValue)
      throws ParseException {
    return getCalendarPropertyValue(propertyName, getDefaultDatePattern(), defaultPropertyValue,
      DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as a Calendar.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default Calendar value returned for the specified property
   * if the property's value is null.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should throw a MissingResourceException.
   * @return the value of the specified property as a Calendar object, or the default Calendar value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined, no
   * default value has been specified and the failForMissingProperty condition is set to true.
   * @throws ParseException if the format of the specified property's value does not match the specified date format.
   */
  public Calendar getCalendarPropertyValue(final String propertyName,
                                           final Calendar defaultPropertyValue,
                                           final boolean failForMissingProperty)
      throws ParseException {
    return getCalendarPropertyValue(propertyName, getDefaultDatePattern(), defaultPropertyValue, failForMissingProperty);
  }

  /**
   * Returns the value of the property with the specified name as a Calendar.
   * @param propertyName the name of the property to return the value of.
   * @param datePattern a String value specifying the date format as required by the SimpleDateFormat class.
   * @return the value of the specified property as Calendar object.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined.
   * @throws ParseException if the format of the specified property's value does not match the specified date format.
   */
  public Calendar getCalendarPropertyValue(final String propertyName, final String datePattern)
      throws ParseException {
    return getCalendarPropertyValue(propertyName, datePattern, null, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as a Calendar.
   * @param propertyName the name of the property to return the value of.
   * @param datePattern a String value specifying the date format as required by the SimpleDateFormat class.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should throw a MissingResourceException.
   * @return the value of the specified property as a Calendar object, or null if the property is not defined
   * and the failForMissingProperty condition is set to false.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the failForMissingProperty condition is set to true.
   * @throws ParseException if the format of the specified property's value does not match the specified date format.
   */
  public Calendar getCalendarPropertyValue(final String propertyName,
                                           final String datePattern,
                                           final boolean failForMissingProperty)
      throws ParseException {
    return getCalendarPropertyValue(propertyName, datePattern, null, failForMissingProperty);
  }

  /**
   * Returns the value of the property with the specified name as a Calendar.
   * @param propertyName the name of the property to return the value of.
   * @param datePattern a String value specifying the date format as required by the SimpleDateFormat class.
   * @param defaultPropertyValue the default Calendar value returned for the specified property
   * if the property's value is null.
   * @return the value of the specified property as a Calendar object, or the default Calendar value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the default value is null.
   * @throws ParseException if the format of the specified property's value does not match the specified date format.
   */
  public Calendar getCalendarPropertyValue(final String propertyName,
                                           final String datePattern,
                                           final Calendar defaultPropertyValue)
      throws ParseException {
    return getCalendarPropertyValue(propertyName, datePattern, defaultPropertyValue, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as a Calendar.
   * @param propertyName the name of the property to return the value of.
   * @param datePattern a String value specifying the date format as required by the SimpleDateFormat class.
   * @param defaultPropertyValue the default Calendar value returned for the specified property
   * if the property's value is null.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should throw a MissingResourceException.
   * @return the value of the specified property as a Calendar object, or the default Calendar value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined, no
   * default value has been specified and the failForMissingProperty condition is set to true.
   * @throws ParseException if the format of the specified property's value does not match the specified date format.
   */
  public Calendar getCalendarPropertyValue(final String propertyName,
                                           final String datePattern,
                                           final Calendar defaultPropertyValue,
                                           final boolean failForMissingProperty)
      throws ParseException {
    final String propertyValue = getPropertyValue(propertyName, getDefaultPropertyValue(defaultPropertyValue),
      failForMissingProperty);

    if (StringUtil.isNotEmpty(propertyValue)) {
      final DateFormat dateParser = new SimpleDateFormat(datePattern);
      final Date date = dateParser.parse(propertyValue);
      return DateUtil.getCalendar(date.getTime());
    }

    return defaultPropertyValue;
  }

  /**
   * Returns the value of the property with the specified name as a Character.
   * @param propertyName the name of the property to return the value of.
   * @return the value of the specified property as Character object.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined.
   */
  public Character getCharacterPropertyValue(final String propertyName) {
    return getCharacterPropertyValue(propertyName, null, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as a Character.
   * @param propertyName the name of the property to return the value of.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should thrown a MissingResourceException.
   * @return the value of the specified property as a Character object, or null if the property is not defined
   * and the failForMissingProperty condition is set to false.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the failForMissingProperty condition is set to true.
   */
  public Character getCharacterPropertyValue(final String propertyName, final boolean failForMissingProperty) {
    return getCharacterPropertyValue(propertyName, null, failForMissingProperty);
  }

  /**
   * Returns the value of the property with the specified name as a Character.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default Character value returned for the specified property
   * if the property's value is null.
   * @return the value of the specified property as a Character object, or the default Character value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the default value is null.
   */
  public Character getCharacterPropertyValue(final String propertyName, final Character defaultPropertyValue) {
    return getCharacterPropertyValue(propertyName, defaultPropertyValue, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as a Character.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default Character value returned for the specified property
   * if the property's value is null.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should thrown a MissingResourceException.
   * @return the value of the specified property as a Character object, or the default Character value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined, no
   * default value has been specified and the failForMissingProperty condition is set to true.
   */
  public Character getCharacterPropertyValue(final String propertyName,
                                             final Character defaultPropertyValue,
                                             final boolean failForMissingProperty) {
    final String propertyValue = getPropertyValue(propertyName, getDefaultPropertyValue(defaultPropertyValue),
      failForMissingProperty);
    return (StringUtil.isEmpty(propertyValue) ? defaultPropertyValue : Character.valueOf(propertyValue.charAt(0)));
  }

  /**
   * Returns the value of the property with the specified name as a Date.
   * @param propertyName the name of the property to return the value of.
   * @return the value of the specified property as Date object.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined.
   * @throws ParseException if the format of the specified property's value does not match the specified date format.
   */
  public Date getDatePropertyValue(final String propertyName) throws ParseException {
    return getDatePropertyValue(propertyName, getDefaultDatePattern(), null, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as a Date.
   * @param propertyName the name of the property to return the value of.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should throw a MissingResourceException.
   * @return the value of the specified property as a Date object, or null if the property is not defined
   * and the failForMissingProperty condition is set to false.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the failForMissingProperty condition is set to true.
   * @throws ParseException if the format of the specified property's value does not match the specified date format.
   */
  public Date getDatePropertyValue(final String propertyName, final boolean failForMissingProperty)
      throws ParseException {
    return getDatePropertyValue(propertyName, getDefaultDatePattern(), null, failForMissingProperty);
  }

  /**
   * Returns the value of the property with the specified name as a Date.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default Date value returned for the specified property
   * if the property's value is null.
   * @return the value of the specified property as a Date object, or the default Date value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the default value is null.
   * @throws ParseException if the format of the specified property's value does not match the specified date format.
   */
  public Date getDatePropertyValue(final String propertyName, final Date defaultPropertyValue) throws ParseException {
    return getDatePropertyValue(propertyName, getDefaultDatePattern(), defaultPropertyValue,
      DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as a Date.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default Date value returned for the specified property
   * if the property's value is null.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should throw a MissingResourceException.
   * @return the value of the specified property as a Date object, or the default Date value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined, no
   * default value has been specified and the failForMissingProperty condition is set to true.
   * @throws ParseException if the format of the specified property's value does not match the specified date format.
   */
  public Date getDatePropertyValue(final String propertyName,
                                   final Date defaultPropertyValue,
                                   final boolean failForMissingProperty)
      throws ParseException {
    return getDatePropertyValue(propertyName, getDefaultDatePattern(), defaultPropertyValue, failForMissingProperty);
  }
  
  /**
   * Returns the value of the property with the specified name as a Date.
   * @param propertyName the name of the property to return the value of.
   * @param datePattern a String value specifying the date format as required by the SimpleDateFormat class.
   * @return the value of the specified property as Date object.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined.
   * @throws ParseException if the format of the specified property's value does not match the specified date format.
   */
  public Date getDatePropertyValue(final String propertyName, final String datePattern) throws ParseException {
    return getDatePropertyValue(propertyName, datePattern, null, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }
  
  /**
   * Returns the value of the property with the specified name as a Date.
   * @param propertyName the name of the property to return the value of.
   * @param datePattern a String value specifying the date format as required by the SimpleDateFormat class.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should throw a MissingResourceException.
   * @return the value of the specified property as a Date object, or null if the property is not defined
   * and the failForMissingProperty condition is set to false.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the failForMissingProperty condition is set to true.
   * @throws ParseException if the format of the specified property's value does not match the specified date format.
   */
  public Date getDatePropertyValue(final String propertyName,
                                   final String datePattern,
                                   final boolean failForMissingProperty)
      throws ParseException {
    return getDatePropertyValue(propertyName, datePattern, null, failForMissingProperty);
  }
  
  /**
   * Returns the value of the property with the specified name as a Date.
   * @param propertyName the name of the property to return the value of.
   * @param datePattern a String value specifying the date format as required by the SimpleDateFormat class.
   * @param defaultPropertyValue the default Date value returned for the specified property
   * if the property's value is null.
   * @return the value of the specified property as a Date object, or the default Date value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the default value is null.
   * @throws ParseException if the format of the specified property's value does not match the specified date format.
   */
  public Date getDatePropertyValue(final String propertyName, final String datePattern, final Date defaultPropertyValue)
      throws ParseException {
    return getDatePropertyValue(propertyName, datePattern, defaultPropertyValue, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as a Date.
   * @param propertyName the name of the property to return the value of.
   * @param datePattern a String value specifying the date format as required by the SimpleDateFormat class.
   * @param defaultPropertyValue the default Date value returned for the specified property
   * if the property's value is null.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should throw a MissingResourceException.
   * @return the value of the specified property as a Date object, or the default Date value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined, no
   * default value has been specified and the failForMissingProperty condition is set to true.
   * @throws ParseException if the format of the specified property's value does not match the specified date format.
   */
  public Date getDatePropertyValue(final String propertyName,
                                   final String datePattern,
                                   final Date defaultPropertyValue,
                                   final boolean failForMissingProperty)
      throws ParseException {
    final String propertyValue = getPropertyValue(propertyName, getDefaultPropertyValue(defaultPropertyValue),
      failForMissingProperty);

    if (StringUtil.isNotEmpty(propertyValue)) {
      final DateFormat dateParser = new SimpleDateFormat(datePattern);
      return dateParser.parse(propertyValue);
    }

    return defaultPropertyValue;
  }

  /**
   * Returns the default date pattern defined by the cp-common.default.date.pattern property, or the default date
   * pattern (MM/dd/yyyy) if the property is not defined in any property configuration.  The date pattern must be
   * specified in a format expected by the SimpleDateFormat class.
   * @return a SimpleDateFormat pattern used to parse String dates and times into Calendar and Date objects.
   */
  private String getDefaultDatePattern() {
    return getPropertyValue(DEFAULT_DATE_PATTERN_PROPERTY, DEFAULT_DATE_PATTERN, false);
  }

  /**
   * Determines the default property value argument to the getPropertyValue methods.
   * @param value the Object used in determining whether the default property value argument to the
   * getPropertyValue methods is null or contains a value.
   * @return null or an empty String as the default value argument for the getPropertyValue methods.
   * @see PropertyManager#getPropertyValue(String, String)
   * @see PropertyManager#getPropertyValue(String, String, boolean)
   */
  private String getDefaultPropertyValue(final Object value) {
    return (ObjectUtil.isNull(value) ? null : "");
  }

  /**
   * Returns the value of the property with the specified name as a Double.
   * @param propertyName the name of the property to return the value of.
   * @return the value of the specified property as Double object.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Double getDoublePropertyValue(final String propertyName) {
    return getDoublePropertyValue(propertyName, null, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as a Double.
   * @param propertyName the name of the property to return the value of.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should thrown a MissingResourceException.
   * @return the value of the specified property as a Double object, or null if the property is not defined
   * and the failForMissingProperty condition is set to false.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the failForMissingProperty condition is set to true.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Double getDoublePropertyValue(final String propertyName, final boolean failForMissingProperty) {
    return getDoublePropertyValue(propertyName, null, failForMissingProperty);
  }

  /**
   * Returns the value of the property with the specified name as a Double.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default Double value returned for the specified property
   * if the property's value is null.
   * @return the value of the specified property as a Double object, or the default Double value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the default value is null.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Double getDoublePropertyValue(final String propertyName, final Double defaultPropertyValue) {
    return getDoublePropertyValue(propertyName, defaultPropertyValue, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as a Double.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default Double value returned for the specified property
   * if the property's value is null.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should thrown a MissingResourceException.
   * @return the value of the specified property as a Double object, or the default Double value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined, no
   * default value has been specified and the failForMissingProperty condition is set to true.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Double getDoublePropertyValue(final String propertyName,
                                       final Double defaultPropertyValue,
                                       final boolean failForMissingProperty) {
    final String propertyValue = getPropertyValue(propertyName, getDefaultPropertyValue(defaultPropertyValue),
      failForMissingProperty);
    return (StringUtil.isEmpty(propertyValue) ? defaultPropertyValue : Double.valueOf(propertyValue));
  }

  /**
   * Returns the value of the property with the specified name as a Float.
   * @param propertyName the name of the property to return the value of.
   * @return the value of the specified property as Float object.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Float getFloatPropertyValue(final String propertyName) {
    return getFloatPropertyValue(propertyName, null, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as a Float.
   * @param propertyName the name of the property to return the value of.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should thrown a MissingResourceException.
   * @return the value of the specified property as a Float object, or null if the property is not defined
   * and the failForMissingProperty condition is set to false.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the failForMissingProperty condition is set to true.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Float getFloatPropertyValue(final String propertyName, final boolean failForMissingProperty) {
    return getFloatPropertyValue(propertyName, null, failForMissingProperty);
  }

  /**
   * Returns the value of the property with the specified name as a Float.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default Float value returned for the specified property
   * if the property's value is null.
   * @return the value of the specified property as a Float object, or the default Float value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the default value is null.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Float getFloatPropertyValue(final String propertyName, final Float defaultPropertyValue) {
    return getFloatPropertyValue(propertyName, defaultPropertyValue, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as a Float.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default Float value returned for the specified property
   * if the property's value is null.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should thrown a MissingResourceException.
   * @return the value of the specified property as a Float object, or the default Float value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined, no
   * default value has been specified and the failForMissingProperty condition is set to true.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Float getFloatPropertyValue(final String propertyName,
                                     final Float defaultPropertyValue,
                                     final boolean failForMissingProperty) {
    final String propertyValue = getPropertyValue(propertyName, getDefaultPropertyValue(defaultPropertyValue),
      failForMissingProperty);
    return (StringUtil.isEmpty(propertyValue) ? defaultPropertyValue : Float.valueOf(propertyValue));
  }

  /**
   * Returns the value of the property with the specified name as an Integer.
   * @param propertyName the name of the property to return the value of.
   * @return the value of the specified property as an Integer object.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Integer getIntegerPropertyValue(final String propertyName) {
    return getIntegerPropertyValue(propertyName, null, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as an Integer.
   * @param propertyName the name of the property to return the value of.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should thrown a MissingResourceException.
   * @return the value of the specified property as an Integer object, or null if the property is not defined
   * and the failForMissingProperty condition is set to false.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the failForMissingProperty condition is set to true.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Integer getIntegerPropertyValue(final String propertyName, final boolean failForMissingProperty) {
    return getIntegerPropertyValue(propertyName, null, failForMissingProperty);
  }

  /**
   * Returns the value of the property with the specified name as an Integer.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default Integer value returned for the specified property
   * if the property's value is null.
   * @return the value of the specified property as an Integer object, or the default Integer value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the default value is null.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Integer getIntegerPropertyValue(final String propertyName, final Integer defaultPropertyValue) {
    return getIntegerPropertyValue(propertyName, defaultPropertyValue, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as an Integer.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default Integer value returned for the specified property
   * if the property's value is null.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should thrown a MissingResourceException.
   * @return the value of the specified property as an Integer object, or the default Integer value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined, no
   * default value has been specified and the failForMissingProperty condition is set to true.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Integer getIntegerPropertyValue(final String propertyName,
                                         final Integer defaultPropertyValue,
                                         final boolean failForMissingProperty) {
    final String propertyValue = getPropertyValue(propertyName, getDefaultPropertyValue(defaultPropertyValue),
      failForMissingProperty);
    return (StringUtil.isEmpty(propertyValue) ? defaultPropertyValue : Integer.valueOf(propertyValue));
  }

  /**
   * Returns the value of the property with the specified name as a Long.
   * @param propertyName the name of the property to return the value of.
   * @return the value of the specified property as Long object.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Long getLongPropertyValue(final String propertyName) {
    return getLongPropertyValue(propertyName, null, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as a Long.
   * @param propertyName the name of the property to return the value of.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should thrown a MissingResourceException.
   * @return the value of the specified property as a Long object, or null if the property is not defined
   * and the failForMissingProperty condition is set to false.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the failForMissingProperty condition is set to true.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Long getLongPropertyValue(final String propertyName, final boolean failForMissingProperty) {
    return getLongPropertyValue(propertyName, null, failForMissingProperty);
  }

  /**
   * Returns the value of the property with the specified name as a Long.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default Long value returned for the specified property
   * if the property's value is null.
   * @return the value of the specified property as a Long object, or the default Long value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the default value is null.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Long getLongPropertyValue(final String propertyName, final Long defaultPropertyValue) {
    return getLongPropertyValue(propertyName, defaultPropertyValue, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as a Long.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default Long value returned for the specified property
   * if the property's value is null.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should thrown a MissingResourceException.
   * @return the value of the specified property as a Long object, or the default Long value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined, no
   * default value has been specified and the failForMissingProperty condition is set to true.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Long getLongPropertyValue(final String propertyName,
                                   final Long defaultPropertyValue,
                                   final boolean failForMissingProperty) {
    final String propertyValue = getPropertyValue(propertyName, getDefaultPropertyValue(defaultPropertyValue),
      failForMissingProperty);
    return (StringUtil.isEmpty(propertyValue) ? defaultPropertyValue : Long.valueOf(propertyValue));
  }

  /**
   * Returns the value of the property with the specified name as a Short.
   * @param propertyName the name of the property to return the value of.
   * @return the value of the specified property as Short object.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Short getShortPropertyValue(final String propertyName) {
    return getShortPropertyValue(propertyName, null, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as a Short.
   * @param propertyName the name of the property to return the value of.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should thrown a MissingResourceException.
   * @return the value of the specified property as a Short object, or null if the property is not defined
   * and the failForMissingProperty condition is set to false.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the failForMissingProperty condition is set to true.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Short getShortPropertyValue(final String propertyName, final boolean failForMissingProperty) {
    return getShortPropertyValue(propertyName, null, failForMissingProperty);
  }

  /**
   * Returns the value of the property with the specified name as a Short.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default Short value returned for the specified property
   * if the property's value is null.
   * @return the value of the specified property as a Short object, or the default Short value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the default value is null.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Short getShortPropertyValue(final String propertyName, final Short defaultPropertyValue) {
    return getShortPropertyValue(propertyName, defaultPropertyValue, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns the value of the property with the specified name as a Short.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default Short value returned for the specified property
   * if the property's value is null.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should thrown a MissingResourceException.
   * @return the value of the specified property as a Short object, or the default Short value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined, no
   * default value has been specified and the failForMissingProperty condition is set to true.
   * @throws NumberFormatException if the specified property's value is not numeric.
   */
  public Short getShortPropertyValue(final String propertyName,
                                     final Short defaultPropertyValue,
                                     final boolean failForMissingProperty) {
    final String propertyValue = getPropertyValue(propertyName, getDefaultPropertyValue(defaultPropertyValue),
      failForMissingProperty);
    return (StringUtil.isEmpty(propertyValue) ? defaultPropertyValue : Short.valueOf(propertyValue));
  }

  /**
   * Returns the value of the property with the specified name as a String.
   * @param propertyName the name of the property to return the value of.
   * @return the value of the specified property as String object.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined.
   */
  public String getStringPropertyValue(final String propertyName) {
    return getPropertyValue(propertyName);
  }

  /**
   * Returns the value of the property with the specified name as a String.
   * @param propertyName the name of the property to return the value of.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should thrown a MissingResourceException.
   * @return the value of the specified property as a String object, or null if the property is not defined
   * and the failForMissingProperty condition is set to false.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the failForMissingProperty condition is set to true.
   */
  public String getStringPropertyValue(final String propertyName, final boolean failForMissingProperty) {
    return getPropertyValue(propertyName, failForMissingProperty);
  }

  /**
   * Returns the value of the property with the specified name as a String.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default String value returned for the specified property
   * if the property's value is null.
   * @return the value of the specified property as a String object, or the default String value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the default value is null.
   */
  public String getStringPropertyValue(final String propertyName, final String defaultPropertyValue) {
    return getPropertyValue(propertyName, defaultPropertyValue);
  }

  /**
   * Returns the value of the property with the specified name as a String.
   * @param propertyName the name of the property to return the value of.
   * @param defaultPropertyValue the default String value returned for the specified property
   * if the property's value is null.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent
   * property should thrown a MissingResourceException.
   * @return the value of the specified property as a String object, or the default String value
   * if the property is not defined.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined, no
   * default value has been specified and the failForMissingProperty condition is set to true.
   */
  public String getStringPropertyValue(final String propertyName,
                                       final String defaultPropertyValue,
                                       final boolean failForMissingProperty) {
    return getPropertyValue(propertyName, defaultPropertyValue, failForMissingProperty);
  }

  /**
   * Returns a String value for the property specified by property name.  The method follows an order of precedence,
   * or hierarchy, when determining the value of the specified property.  The method will return the value of the
   * System property having the given property name if it exists.  If the System property is not defined, the local
   * properites managed by this PropertyManager are used to determine the value.  Next, the method proceeds by looking
   * up the value of the property with property name in the cp-common.properties configuration file for the cp-common
   * Java Class Library.  Finally, if no such value exists, the method will return null.
   * @param propertyName the name of the property to return the value for.
   * @return the value of the given property.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined.
   */
  final String getPropertyValue(final String propertyName) {
    return getPropertyValue(propertyName, null, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns a String value for the property specified by property name.  The method follows an order of precedence,
   * or hierarchy, when determining the value of the specified property.  The method will first return the value of
   * the System property having the specified property name if it exists.  If the System property is not defined, the
   * local properites managed by this PropertyManager are used to determine the value.  Next, the method will look up
   * the value of the property having the specified property name in the cp-common.properties configuration file for
   * the cp-common Java Class Library.  Finally, if no such value exists, the method will return the value of the
   * defaultPropertyValue.
   * @param propertyName the name of the property to return the value for.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent property
   * should thrown a MissingResourceException.
   * @return the value of the specified property, or null if the property is not defined and the failForMissingProperty
   * condition is set to false.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined
   * and the failForMissingProperty condition is set to true.
   */
  final String getPropertyValue(final String propertyName, final boolean failForMissingProperty) {
    return getPropertyValue(propertyName, null, failForMissingProperty);
  }

  /**
   * Returns a String value for the property specified by property name.  The method follows an order of precedence,
   * or hierarchy, when determining the value of the specified property.  The method will first return the value of
   * the System property having the specified property name if it exists.  If the System property is not defined, the
   * local properites managed by this PropertyManager are used to determine the value.  Next, the method will look up
   * the value of the property having the specified property name in the cp-common.properties configuration file for
   * the cp-common Java Class Library.  Finally, if no such value exists, the method will return the value of the
   * defaultPropertyValue.
   * @param propertyName the name of the property to return the value for.
   * @param defaultPropertyValue a default String value returned for the specified property if the property
   * cannot be found using the hierarchical/order-of-precedence determination rules.
   * @return the value of the specified property, or the default String value if the property's value is null.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined, no
   * default value has been specified and the failForMissingProperty condition is set to true.
   */
  final String getPropertyValue(final String propertyName, final String defaultPropertyValue) {
    return getPropertyValue(propertyName, defaultPropertyValue, DEFAULT_FAIL_FOR_MISSING_PROPERTY);
  }

  /**
   * Returns a String value for the property specified by property name.  The method follows an order of precedence,
   * or hierarchy, when determining the value of the specified property.  The method will first return the value of
   * the System property having the specified property name if it exists.  If the System property is not defined, the
   * local properites managed by this PropertyManager are used to determine the value.  Next, the method will look up
   * the value of the property having the specified property name in the cp-common.properties configuration file for
   * the cp-common Java Class Library.  Finally, if no such value exists, the method will return the value of the
   * defaultPropertyValue.
   * @param propertyName the name of the property to return the value for.
   * @param defaultPropertyValue a default String value returned for the specified property if the property
   * cannot be found using the hierarchical/order-of-precedence determination rules.
   * @param failForMissingProperty conditional value indicating whether resource value lookup on a non-existent property
   * should thrown a MissingResourceException.
   * @return the value of the specified property, or the default String value if the property's value is null.
   * @throws ConfigurationException if an IO error, or some other type of error occurs while
   * obtaining the value of the specified property from the property configuration.
   * @throws MissingResourceException if the property specified by name has not been defined, no
   * default value has been specified and the failForMissingProperty condition is set to true.
   */
  final String getPropertyValue(final String propertyName,
                                final String defaultPropertyValue,
                                final boolean failForMissingProperty) {
    if (logger.isDebugEnabled()) {
      logger.debug("propertyName (" + propertyName + ")");
      logger.debug("defaultPropertyValue (" + defaultPropertyValue + ")");
    }

    String propertyValue = System.getProperty(propertyName);

    if (ObjectUtil.isNull(propertyValue)) {
      propertyValue = localProperties.getProperty(propertyName);
    }

    if (ObjectUtil.isNull(propertyValue)) {
      try {
        propertyValue = CPCommonProperties.getInstance().getString(propertyName);
      }
      catch (IOException e) {
        logger.error("An IO error occurred trying to obtain an instance of the CPCommonProperties class!", e);
        throw new ConfigurationException("An IO error occurred trying to obtain an instance of the CPCommonProperties class!", e);
      }
      catch (MissingResourceException e) {
        logger.warn("Failed to find value for property (" + propertyName + ") in the cp-common.properties file!", e);
        if (failForMissingProperty && ObjectUtil.isNull(defaultPropertyValue)) {
          throw e;
        }
      }
    }

    propertyValue = ObjectUtil.getDefaultValue(propertyValue, defaultPropertyValue);

    if (logger.isDebugEnabled()) {
      logger.debug("propertyValue (" + propertyValue + ")");
    }

    return propertyValue;
  }

  /**
   * Adds the specified properties to the local properties managed by this PropertyManager.  Any matching property
   * defined by the Properties object will overwrite the value in the local properties of this PropertyManager.
   * @param properties a set of properties to be managed by this PropertyManager.
   */
  public void setProperties(final Properties properties) {
    for (Iterator it = CollectionUtil.iterator(properties.keys()); it.hasNext(); ) {
      final String key = it.next().toString();
      final String value = properties.getProperty(key);
      localProperties.setProperty(key, value);
    }
  }

  /**
   * Adds the specified properties from the resource bundle to the local properties managed by this PropertyManager.
   * Any matching property defined by the ResourceBundle will overwrite the value in the local properties of this
   * PropertyManager.
   * @param bundle a bundle of properties to be managed by this PropertyManager.
   */
  public void setProperties(final ResourceBundle bundle) {
    for (Iterator it = CollectionUtil.iterator(bundle.getKeys()); it.hasNext(); ) {
      final String key = it.next().toString();
      final String value = bundle.getString(key);
      localProperties.setProperty(key, value);
    }
  }

  /**
   * A method to be used at runtime to change the value of a property with the specified name.  If a property
   * with the specified name does not exist, then the property is mapped to the specified value.
   * @param propertyName the name of the property to change.
   * @param propertyValue the new value of the property.
   * @return the old value of the property specified by the name if it exists, otherwise null.
   */
  public String setPropertyValue(final String propertyName, final String propertyValue) {
    Object previousPropertyValue = null;

    if (StringUtil.isEmpty(propertyValue)) {
      previousPropertyValue = localProperties.remove(propertyName);
    }
    else {
      previousPropertyValue = localProperties.setProperty(propertyName, propertyValue);
    }

    return ObjectUtil.toString(previousPropertyValue);
  }

}
