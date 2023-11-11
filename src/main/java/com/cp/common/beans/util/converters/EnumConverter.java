/*
 * EnumConverter.java (c) 21 May 2007
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
import com.cp.common.util.Filter;
import java.lang.reflect.Method;
import java.util.Set;

public class EnumConverter extends AbstractConverter<com.cp.common.enums.Enum> {

  /**
   * Converts the specified to an Enum object of the specified type.
   * @param type the Class type of the Enum to convert the specified value to.
   * @param value the Object value being converted into an Enum value of the specified type.
   * @return the value converted into one of the appropriate Enum values of the specified type.
   * @throws ConversionException if the specified value Object cannot be converted into an Enum of the specified type.
   */
  protected Object convertImpl(final Class type, final Object value) {
    Assert.notNull(type, "The Class type of the Enum to convert to cannot be null!");
    Assert.isAssignableFrom(type, com.cp.common.enums.Enum.class, "(" + type.getName() + ") must implement the com.cp.common.enums.Enum interface!");

    if (ObjectUtil.isNotNull(value)) {
      if (value instanceof com.cp.common.enums.Enum) {
        return value;
      }
      else if (value instanceof Number) {
        return getEnumById(type, ((Number) value).intValue());
      }
      else if (value instanceof String) {
        final String stringValue = value.toString().trim();

        if (logger.isDebugEnabled()) {
          logger.debug("value as a String (" + stringValue + ")");
        }

        if (StringUtil.isDigitsOnly(stringValue)) {
          return getEnumById(type, Integer.valueOf(stringValue));
        }
        else {
          try {
            return getEnumByCode(type, stringValue);
          }
          catch (ConversionException e) {
            logger.warn(e.getMessage());
            return getEnumByExternalCode(type, stringValue);
          }
        }
      }
      else {
        logger.warn("Unable to determine Enum of type (" + type.getName() + ") for value (" + value + ")!");
        throw new ConversionException("Unable to determine Enum of type (" + type.getName()
          + ") for value (" + value + ")!");
      }
    }

    return null;
  }

  /**
   * Gets an enumerated value by code from the Enum class of the specified type.
   * @param enumType the Class type of the Enum.
   * @param code the code property value of the Enum.
   * @return an Enum object of the specified type with the specified code.
   * @throws IllegalArgumentException if an appropriate Enum value of the specified type cannot be found
   * with the specified code.
   */
  protected com.cp.common.enums.Enum getEnumByCode(final Class enumType, final String code) {
    return getEnumByValue(enumType, new EnumCodeLookupStrategy(code));
  }

  /**
   * Gets an enumerated value by external code from the Enum class of the specified type.
   * @param enumType the Class type of the Enum.
   * @param externalCode the external code property value of the Enum.
   * @return an Enum object of the specified type with the specified external code.
   * @throws IllegalArgumentException if an appropriate Enum value of the specified type cannot be found
   * with the specified external code.
   */
  protected com.cp.common.enums.Enum getEnumByExternalCode(final Class enumType, final String externalCode) {
    return getEnumByValue(enumType, new EnumExternalCodeLookupStrategy(externalCode));
  }

  /**
   * Gets an enumerated value by id from the Enum class of the specified type.
   * @param enumType the Class type of the Enum.
   * @param id the id property value of the Enum.
   * @return an Enum object of the specified type with the specified id.
   * @throws IllegalArgumentException if an appropriate Enum value of the specified type cannot be found
   * with the specified ID.
   */
  protected com.cp.common.enums.Enum getEnumById(final Class enumType, final Integer id) {
    return getEnumByValue(enumType, new EnumIdLookupStrategy(id));
  }

  /**
   * Gets one of the enumerated values from the Enum class of the specified type using an EnumValueLookupStrategy.
   * @param enumType the Class type of the Enum.
   * @param enumValueLookupStrategy the Enum value lookup strategy object used to get an enumerated value
   * of the specified Enum type.
   * @return an Enum object of the specified type satisfying the Enum value lookup strategy object.
   * @throws IllegalArgumentException if an appropriate Enum value of the specified type cannot be found using the
   * Enum value lookup strategy.
   */
  protected com.cp.common.enums.Enum getEnumByValue(final Class enumType,
                                                    final EnumValueLookupStrategy enumValueLookupStrategy)
  {
    for (final com.cp.common.enums.Enum enumx : getEnumValues(enumType)) {
      if (enumValueLookupStrategy.accept(enumx)) {
        return enumx;
      }
    }

    logger.warn("(" + enumValueLookupStrategy.getValue() + ") is not a valid (" + enumValueLookupStrategy.getDescription()
      + ") for Enum type (" + enumType.getName() + ")!");
    throw new ConversionException("(" + enumValueLookupStrategy.getValue() + ") is not a valid ("
      + enumValueLookupStrategy.getDescription() + ") for Enum type (" + enumType.getName() + ")!");
  }

  /**
   * Gets the set of enumerated values for the Enum of the specified type.
   * @param enumType the Class type of the Enum.
   * @return a Set of enumerated values for the Enum of the specified type.
   * @throws ConversionException if the Enum class of type does not have a values method.
   */
  protected Set<com.cp.common.enums.Enum> getEnumValues(final Class enumType) {
    try {
      final Method valuesMethod = enumType.getMethod("values", (Class[]) null);
      return (Set<com.cp.common.enums.Enum>) valuesMethod.invoke(null, (Object[]) null);
    }
    catch (Exception e) {
      logger.error("Failed to get all the values of Enum type (" + enumType.getName() + ")!", e);
      throw new ConversionException("Failed to get all the values of Enum type (" + enumType.getName() + ")!", e);
    }
  }

  /**
   * Abstract Data Type (ADT) defining an interface for looking up Enum values based on a particular property
   * of the Enum.  This interface is an implementation of the Strategy Design Pattern.
   */
  protected static interface EnumValueLookupStrategy<T> extends Filter<com.cp.common.enums.Enum> {

    public String getDescription();

    public T getValue();

    public boolean accept(final com.cp.common.enums.Enum enumx);

  }

  /**
   * Abstract base class for all EnumValueLookupStrategy implementations supporting common behavior.
   */
  protected static abstract class AbstractEnumValueLookupStrategy<T> implements EnumValueLookupStrategy<T> {

    private final T value;

    public AbstractEnumValueLookupStrategy(final T value) {
      Assert.notNull(value, "The value to compare with all values of the Enum for a particular property cannot be null!");
      this.value = value;
    }

    public T getValue() {
      return value;
    }
  }

  /**
   * Lookup strategy to find an Enum value by the Enum's code property.
   */
  protected static final class EnumCodeLookupStrategy extends AbstractEnumValueLookupStrategy<String> {

    public EnumCodeLookupStrategy(final String code) {
      super(code);
    }

    public String getDescription() {
      return "Code";
    }

    public boolean accept(final com.cp.common.enums.Enum enumx) {
      return getValue().equals(enumx.getCode());
    }
  }

  /**
   * Lookup strategy to find an Enum value by the Enum's external code property.
   */
  protected static final class EnumExternalCodeLookupStrategy extends AbstractEnumValueLookupStrategy<String> {

    public EnumExternalCodeLookupStrategy(final String externalCode) {
      super(externalCode);
    }

    public String getDescription() {
      return "External Code";
    }

    public boolean accept(final com.cp.common.enums.Enum enumx) {
      return getValue().equals(enumx.getExternalCode());
    }
  }

  /**
   * Lookup strategy to find an Enum value by the Enum's id property.
   */
  protected static final class EnumIdLookupStrategy extends AbstractEnumValueLookupStrategy<Integer> {

    public EnumIdLookupStrategy(final Integer id) {
      super(id);
    }

    public String getDescription() {
      return "ID";
    }

    public boolean accept(final com.cp.common.enums.Enum enumx) {
      return getValue().equals(enumx.getId());
    }
  }

}
