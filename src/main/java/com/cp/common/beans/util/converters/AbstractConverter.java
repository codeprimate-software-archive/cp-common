/*
 * AbstractConverter.java (c) 7 March 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.10.7
 * @see com.apache.commons.beanutils.Converter
 */

package com.cp.common.beans.util.converters;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractConverter<T> implements Converter {

  protected final Log logger = LogFactory.getLog(getClass());

  private final boolean usingDefaultValue;

  private final Converter converter;

  private final T defaultValue;

  /**
   * Default constructor setting the default value of the Converter to null.
   */
  public AbstractConverter() {
    this(null);
  }

  /**
   * Constructor used to instantiate an instance of the AbstractConverter class initialized with a Object value
   * for the default value.
   * @param defaultValue the value returned by this Converter in the event that the value to be converted is null.
   */
  public AbstractConverter(final T defaultValue) {
    this.converter = NoOpConverter.INSTANCE;
    this.defaultValue = defaultValue;
    this.usingDefaultValue = ObjectUtil.isNotNull(defaultValue);
  }

  /**
   * Constructor used to instantiate an instance of the AbstractConverter class initialized with whether to use the
   * NullToDefaultValueConverter class to convert null values into default values of the specified Class type.
   * @param useDefaultValue a boolean value indicating whether or not to use the NullToDefaultValueConverter class
   * to convert null values of the specified Class type into their corresponding default values based on Class type.
   */
  public AbstractConverter(final boolean useDefaultValue) {
    this.converter = (useDefaultValue ? NullToDefaultValueConverter.INSTANCE : NoOpConverter.INSTANCE);
    this.defaultValue = null;
    this.usingDefaultValue = useDefaultValue;
  }

  /**
   * Gets the Converter used for default values.
   * @return the default value Converter.
   */
  private Converter getConverter() {
    return converter;
  }

  /**
   * Returns the default value that was set during initialization of the Converter.
   * @return the value used as the default value and returned by this Converter in the event that the value
   * to be converted is null.
   */
  protected T getDefaultValue() {
    return defaultValue;
  }

  /**
   * Determines whether or not this Converter is using a default value on nulls.
   * @return a boolean value indicating whether or not this Converter is using a default value on nulls.
   */
  public boolean isUsingDefaultValue() {
    return usingDefaultValue;
  }

  /**
   * Converts the specify Object value into an Object having the specified Class type.
   * @param type the Class type to convert the Object value to.
   * @param value the Object value being converted into an object of the specified Class type.
   * @return an Object value of the specified Class type converted from the specified Object value.
   */
  public Object convert(final Class type, Object value) {
    if ("null".equalsIgnoreCase(StringUtil.toString(value).trim())) {
      value = null;
    }

    return getConverter().convert(type, ObjectUtil.getDefaultValue(convertImpl(type, value), getDefaultValue()));
  }

  /**
   * Converts the specify Object value into an Object having the specified Class type.
   * @param type the Class type to convert the Object value to.
   * @param value the Object value being converted into an object of the specified Class type.
   * @return an Object value of the specified Class type converted from the specified Object value.
   */
  protected abstract Object convertImpl(Class type, Object value);

  /**
   * No operation Converter.
   */
  private static final class NoOpConverter implements Converter {

    public static final NoOpConverter INSTANCE = new NoOpConverter();

    public Object convert(final Class type, final Object value) {
      return value;
    }
  }

}
