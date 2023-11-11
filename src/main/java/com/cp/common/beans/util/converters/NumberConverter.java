/*
 * NumberConverter.java (c) 17 May 2007
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see com.cp.common.beans.util.converters.AbstractConverter
 * @see org.apache.commons.beanutils.Converter
 */

package com.cp.common.beans.util.converters;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.ConversionException;

public class NumberConverter extends AbstractConverter<Number> {

  /**
   * Default constructor to instantiate an instance of the NumberConverter class.
   */
  public NumberConverter() {
  }

  /**
   * Instantiate an instance of the NumberConverter class initialized with a default Number value.
   * @param defaultValue a Number object specifying the default value of this converter.
   */
  public NumberConverter(final Number defaultValue) {
    super(defaultValue);
  }

  /**
   * Instantiates an instance of the NumberConverter class initialized with a boolean value indicating whether or not
   * a default numeric value should be applied when converting null values.
   * @param usingDefaultValue a boolean indicating whether a default numeric value should be used when
   * converting null values.
   */
  public NumberConverter(final boolean usingDefaultValue) {
    super(usingDefaultValue);
  }

  /**
   * Converts the specified Object value into a Number.
   * @param type a subclass of the Number type.
   * @param value the Object value to convert into a Number.
   * @return a Number value for the specified Object value.
   * @throws ConversionException if the Object value cannot be converted into a Number.
   */
  protected Object convertImpl(final Class type, final Object value) {
    Assert.notNull(type, "The Class type to convert to cannot be null!");
    Assert.isAssignableFrom(type, Number.class, "The Class type to convert to must be a type of Number!");
    Assert.notEqual(Number.class, type, "The Class type cannot be the Number class explicitly but rather a sublclass of Number!");

    if (ObjectUtil.isNotNull(value)) {
      if (type.isAssignableFrom(value.getClass())) {
        return value;
      }
      else if (StringUtil.isNotEmpty(value.toString())) {
        return getNumber(type, value);
      }
      else {
        logger.error("(" + value + ") is not valid numeric value!");
        throw new ConversionException("(" + value + ") is not valid numeric value!");
      }
    }

    return null;
  }

  /**
   * Gets a Number object for the specified Object value.
   * @param type the Number subclass type to convert the value to.
   * @param value the Object value being converted to a Number.
   * @return a subclass of Number based on type with the Object value.
   * @throws ConversionException if the Object value cannot be converted into a Number of type.
   */
  protected <T extends Number> T getNumber(final Class<T> type, final Object value) {
    Assert.notNull(type, "The Class type cannot be null!");
    Assert.notEqual(type, Number.class, "The Class type cannot be the Number class explicitly but rather a subclass of Number!");

    try {
      return ClassUtil.<T>getInstance(type, new Object[] { value.toString().trim() });
    }
    catch (Exception e) {
      logger.error("(" + value + ") is not valid (" + type.getName() + ") value!", e);
      throw new ConversionException("(" + value + ") is not valid (" + type.getName() + ") value!", e);
    }
  }

}
