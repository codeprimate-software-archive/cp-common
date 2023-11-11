/*
 * BooleanConverter.java (c) 15 May 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.10.7
 * @see com.cp.common.beans.util.converters.AbstractConverter
 * @see com.cp.common.util.ConversionException
 */

package com.cp.common.beans.util.converters;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.ConversionException;
import java.util.HashSet;
import java.util.Set;

public class BooleanConverter extends AbstractConverter<Boolean> {

  private static final Set<String> FALSE_CONDITION_SET = new HashSet<String>(5);
  private static final Set<String> TRUE_CONDITION_SET = new HashSet<String>(5);

  static {
    FALSE_CONDITION_SET.add("false");
    FALSE_CONDITION_SET.add("0");
    FALSE_CONDITION_SET.add("n");
    FALSE_CONDITION_SET.add("no");
    TRUE_CONDITION_SET.add("true");
    TRUE_CONDITION_SET.add("1");
    TRUE_CONDITION_SET.add("y");
    TRUE_CONDITION_SET.add("yes");
  }

  /**
   * Default constructor to instantiate an instance of the BooleanConverter class.
   */
  public BooleanConverter() {
  }

  /**
   * Instantiate an instance of the BooleanConverter class initialized with a default Boolean value.
   * @param defaultValue a Boolean value returned as the default value of the Converter.
   */
  public BooleanConverter(final Boolean defaultValue) {
    super(defaultValue);
  }

  /**
   * Instantiate an instance of the BooleanConverter class initialized with a boolean condition indicating
   * whether a default Boolean value should be used on null values when converting.
   * @param useDefaultValue a boolean value indicating whether a default Boolean value should be used on nulls.
   */
  public BooleanConverter(final boolean useDefaultValue) {
    super(useDefaultValue);
  }

  /**
   * Converts the specified value into a Boolean value.
   * @param type the Boolean class type.
   * @param value the Object value to convert into a Boolean object.
   * @return a Boolean object value for the specified value.
   * @throws ConversionException if the value is not a valid boolean value.
   */
  protected Object convertImpl(final Class type, final Object value) {
    if (ObjectUtil.isNotNull(value)) {
      if (value instanceof Boolean) {
        return value;
      }
      else if (StringUtil.isNotEmpty(value.toString())) {
        return getBoolean(value.toString());
      }
      else {
        logger.warn("(" + value + ") is not a valid boolean condition!");
        throw new ConversionException("(" + value + ") is not a valid boolean condition!");
      }
    }

    return null;
  }

  /**
   * Gets a Boolean object value for the specified String condition.
   * @param condition a String value representing a boolean value.
   * @return a Boolean object value for the specified String condition value.
   * @throws ConversionException if the specified String conditional value cannot be converted into a
   * Boolean object value.
   */
  protected Boolean getBoolean(String condition) {
    Assert.notNull(condition, "The condition cannot be null!");

    condition = condition.toLowerCase().trim();

    if (TRUE_CONDITION_SET.contains(condition)) {
      return Boolean.TRUE;
    }
    else if (FALSE_CONDITION_SET.contains(condition)) {
      return Boolean.FALSE;
    }
    else {
      logger.warn("(" + condition + ") is not a valid boolean condition!");
      throw new ConversionException("(" + condition + ") is not a valid boolean condition!");
    }
  }

}
