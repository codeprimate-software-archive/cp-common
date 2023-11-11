/*
 * StringConverter.java (c) 17 May 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.10.10
 * @see com.cp.common.beans.util.converters.AbstractConverter
 * @see java.text.DateFormat
 */

package com.cp.common.beans.util.converters;

import com.cp.common.lang.Assert;
import com.cp.common.lang.Identifiable;
import com.cp.common.lang.ObjectUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StringConverter extends AbstractConverter<String> {

  public static final EnumConversionStrategy ENUM_TO_CODE = new EnumToCodeConversionStrategy();
  public static final EnumConversionStrategy ENUM_TO_DESCRIPTION = new EnumToDescriptionConversionStrategy();
  public static final EnumConversionStrategy ENUM_TO_EXTERNAL_CODE = new EnumToExternalCodeConversionStrategy();

  public static final String DEFAULT_DATE_FORMAT_PATTERN = "MM/dd/yyyy hh:mm a";

  private EnumConversionStrategy enumConversionStrategy = ENUM_TO_CODE;

  private String dateFormatPattern = DEFAULT_DATE_FORMAT_PATTERN;

  /**
   * Constructs a default instance of the StringConverter class.
   */
  public StringConverter() {
  }

  /**
   * Instantiates an instance of the StringConverter class initialized with a default String value returned when
   * converting null Object values.
   * @param defaultValue the String value returned when the Object value converted is null.
   */
  public StringConverter(final String defaultValue) {
    super(defaultValue);
  }

  /**
   * Instantiates an instance of the StringConverter class initialized with a boolean value indicating whether or not
   * a default String value should be returned when converting null Object values.
   * @param usingDefaultValue boolean value indicating whether or not a default String value will be return when
   * converting null Object values.
   */
  public StringConverter(final boolean usingDefaultValue) {
    super(usingDefaultValue);
  }

  /**
   * Gets the date format pattern used to format Calendar and Date objects into Strings.
   * @return a String specifying the date format pattern to format Calendar and Date objects.
   */
  public String getDateFormatPattern() {
    return dateFormatPattern;
  }

  /**
   * Sets the date format pattern used to format Calendar and Date objects into Strings.
   * @param dateFormatPattern a String specifying the date format pattern to format Calendar and Date objects.
   */
  public void setDateFormatPattern(final String dateFormatPattern) {
    this.dateFormatPattern = ObjectUtil.getDefaultValue(dateFormatPattern, DEFAULT_DATE_FORMAT_PATTERN);
  }

  /**
   * Gets the EnumConversionStrategy used to convert Enums into Strings.
   * @return an EnumConversionStrategy used to convert Enums into Strings.
   */
  public EnumConversionStrategy getEnumConversionStrategy() {
    return enumConversionStrategy;
  }

  /**
   * Sets the EnumConversionStrategy used to convert Enums into Strings.
   * @param enumConversionStrategy an EnumConversionStrategy used to convert Enums into Strings.
   */
  public void setEnumConversionStrategy(final EnumConversionStrategy enumConversionStrategy) {
    this.enumConversionStrategy = ObjectUtil.getDefaultValue(enumConversionStrategy, ENUM_TO_CODE);
  }

  /**
   * Converts the specified Object value into a String.
   * @param type the String class type.
   * @param value the Object value to convert into a String.
   * @return a String representation of the specified Object value.
   */
  protected Object convertImpl(final Class type, final Object value) {
    if (ObjectUtil.isNotNull(value)) {
      if (value instanceof String) {
        return value;
      }
      else if (value instanceof Calendar) {
        final DateFormat dateFormat = new SimpleDateFormat(getDateFormatPattern());
        return dateFormat.format(((Calendar) value).getTime());
      }
      else if (value instanceof Date) {
        final DateFormat dateFormat = new SimpleDateFormat(getDateFormatPattern());
        return dateFormat.format((Date) value);
      }
      else if (value instanceof com.cp.common.enums.Enum) {
        return getEnumConversionStrategy().convert((com.cp.common.enums.Enum) value);
      }
      else if (value instanceof Identifiable) {
        return convert((Identifiable) value);
      }
      else {
        return value.toString();
      }
    }

    return null;
  }

  /**
   * Method to convert Identifiable objects into a String.
   * @param value an Identifiable object to convert into a String.
   * @return a String representation of the specified Identifiable object value.
   */
  protected String convert(final Identifiable value) {
    Assert.notNull(value, "The Identifiable object cannot be null!");

    final StringBuffer buffer = new StringBuffer(value.getClass().getName());
    buffer.append("(");
    buffer.append(value.getId());
    buffer.append(")");
    return buffer.toString();
  }

  protected static interface EnumConversionStrategy {

    public String convert(com.cp.common.enums.Enum enumx);

  }

  protected static final class EnumToCodeConversionStrategy implements EnumConversionStrategy {

    public String convert(final com.cp.common.enums.Enum enumx) {
      return enumx.getCode();
    }
  }

  protected static final class EnumToDescriptionConversionStrategy implements EnumConversionStrategy {

    public String convert(final com.cp.common.enums.Enum enumx) {
      return enumx.getDescription();
    }
  }

  protected static final class EnumToExternalCodeConversionStrategy implements EnumConversionStrategy {

    public String convert(final com.cp.common.enums.Enum enumx) {
      return enumx.getExternalCode();
    }
  }

}
