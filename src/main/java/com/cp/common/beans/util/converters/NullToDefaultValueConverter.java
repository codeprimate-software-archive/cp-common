/*
 * NullToDefaultValueConverter.java (c) 6 October 2008
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see org.apache.commons.beanutils.converters.Converter
 */

package com.cp.common.beans.util.converters;

import com.cp.common.beans.DefaultProcess;
import com.cp.common.beans.DefaultUser;
import com.cp.common.beans.User;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Resettable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.beanutils.Converter;

public final class NullToDefaultValueConverter implements Converter, Resettable {

  public static final NullToDefaultValueConverter INSTANCE = new NullToDefaultValueConverter();

  private static final Map<Class, Object> DEFAULT_VALUE_MAP = new HashMap<Class, Object>(13);
  private static final Map<Class, Object> USER_DEFAULT_VALUE_MAP = new HashMap<Class, Object>();

  static {
    DEFAULT_VALUE_MAP.put(BigDecimal.class, new BigDecimal("0.0"));
    DEFAULT_VALUE_MAP.put(BigInteger.class, new BigInteger("0"));
    DEFAULT_VALUE_MAP.put(Boolean.class, Boolean.FALSE);
    DEFAULT_VALUE_MAP.put(Byte.class, (byte) 0);
    DEFAULT_VALUE_MAP.put(Calendar.class, CalendarValueFactory.INSTANCE);
    DEFAULT_VALUE_MAP.put(Character.class, '?');
    DEFAULT_VALUE_MAP.put(Date.class, DateValueFactory.INSTANCE);
    DEFAULT_VALUE_MAP.put(Double.class, 0.0d);
    DEFAULT_VALUE_MAP.put(Float.class, 0.0f);
    DEFAULT_VALUE_MAP.put(Integer.class, 0);
    DEFAULT_VALUE_MAP.put(Long.class, 0l);
    DEFAULT_VALUE_MAP.put(com.cp.common.beans.Process.class, new DefaultProcess("system"));
    DEFAULT_VALUE_MAP.put(Short.class, (short) 0);
    DEFAULT_VALUE_MAP.put(String.class, "");
    DEFAULT_VALUE_MAP.put(User.class, new DefaultUser("root"));
  }

  /**
   * Default private constructor to enforce non-instantiability.
   */
  private NullToDefaultValueConverter() {
  }

  /**
   * Converts the specified Object value if null into a default value depending upon the default value
   * corresponding to the specified Class type.
   * @param type the Class type to convert the Object value to.
   * @param value the Object value being converted.
   * @return a default value of the specified Class type if the Object value is null, otherwise return the Object value
   * as is.
   */
  public Object convert(final Class type, final Object value) {
    if (ObjectUtil.isNull(value)) {
      return ObjectUtil.getDefaultValue(getDefaultValue(USER_DEFAULT_VALUE_MAP, type),
        getDefaultValue(DEFAULT_VALUE_MAP, type));
    }
    else {
      return value;
    }
  }

  /**
   * Gets the registered default value for the specified Class type from the default value mapping.
   * @param defaultValueMap the default value mapping of Class types to their corresponding default values.
   * @param type the specified Class type to return a default value for.
   * @return a default Object value corresponding to the specified Class type.
   */
  protected Object getDefaultValue(final Map<Class, Object> defaultValueMap, final Class type) {
    Assert.notNull(defaultValueMap, "The default value mapping cannot be null!");
    Assert.notNull(type, "The Class type of the default value must be specified!");

    Object defaultValue = defaultValueMap.get(type);

    if (ObjectUtil.isNull(defaultValue)) {
      for (final Class registeredType : defaultValueMap.keySet()) {
        if (registeredType.isAssignableFrom(type)) {
          defaultValue = defaultValueMap.get(registeredType);
          break;
        }
      }
    }

    if (defaultValue instanceof ValueFactory) {
      defaultValue = ((ValueFactory) defaultValue).getValue();
    }

    return defaultValue;
  }

  /**
   * Registers a user-defined Object value to be used as the default value for null values of the specified Class type.
   * @param type the specified Class type of the default value.
   * @param defaultValue the Object value used as the default value when null values of the specified Class type
   * are encountered.
   * @return a boolean value indicating whether the registration of the Class type to it's default value
   * was successful or not.
   */
  public static <T> boolean registerDefaultValue(final Class<T> type, final T defaultValue) {
    Assert.notNull(type, "The Class type of the default value cannot be null!");
    Assert.isTrue(type.isInstance(defaultValue), "The Class type (" + ClassUtil.getClassName(defaultValue)
      + ") of the default value must be assignable to (" + type.getName() + ")!");
    USER_DEFAULT_VALUE_MAP.put(type, defaultValue);
    return USER_DEFAULT_VALUE_MAP.get(type).equals(defaultValue);
  }

  /**
   * Registers a ValueFactory object used to create default values for null values of the specified Class type.
   * @param type the specified Class type of the default value.
   * @param factory the ValueFactory used to create default values when null values of the specified Class type
   * are encountered.
   * @return a boolean value indicating whether the registration of the Class type to the ValueFactory object
   * was successful or not.
   */
  public static <T> boolean registerDefaultValue(final Class<T> type, final ValueFactory<T> factory) {
    Assert.notNull(type, "The Class type of the default value cannot be null!");
    Assert.notNull(factory, "The ValueFactory cannot be null!");
    USER_DEFAULT_VALUE_MAP.put(type, factory);
    return USER_DEFAULT_VALUE_MAP.get(type).equals(factory);
  }

  /**
   * Clears all the registered default Object values to their corresponding Class types as defined by the user.
   */
  public void reset() {
    USER_DEFAULT_VALUE_MAP.clear();
  }

  /**
   * Unregisters the default value associated with the specified Class type for the user-defined mapping.
   * @param type the specified Class type of the default value.
   * @return a boolean value indicating with the unregistration of the specified Class type to it's
   * default user-defined Object value.
   */
  public static boolean unregisterDefaultValue(final Class type) {
    return ObjectUtil.isNotNull(USER_DEFAULT_VALUE_MAP.remove(type));
  }

  /**
   * Abstract Data Type (ADT) defining a factory used to create Object values.
   * This interface models the Abstract Factory Design Pattern.
   */
  public static interface ValueFactory<T> {

    public T getValue();

  }

  /**
   * Abstract Factory used to create instances of Calendar objects.
   */
  protected static final class CalendarValueFactory implements ValueFactory<Calendar> {

    public static final CalendarValueFactory INSTANCE = new CalendarValueFactory();

    public Calendar getValue() {
      return Calendar.getInstance();
    }
  }

  /**
   * Abstract Factory used to create instances of Date objects.
   */
  protected static final class DateValueFactory implements ValueFactory<Date> {

    public static final DateValueFactory INSTANCE = new DateValueFactory();

    public Date getValue() {
      return Calendar.getInstance().getTime();
    }
  }

}
