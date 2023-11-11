/*
 * ConvertUtil.java (c) 2 March 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.10.3
 * @see com.cp.common.beans.util.BeanUtil
 * @see com.cp.common.beans.util.converters.BooleanConverter
 * @see com.cp.common.beans.util.converters.CalendarConverter
 * @see com.cp.common.beans.util.converters.EnumConverter
 * @see com.cp.common.beans.util.converters.IdentifiableConverter
 * @see com.cp.common.beans.util.converters.NumberConverter
 * @see com.cp.common.beans.util.converters.ProcessConverter
 * @see com.cp.common.beans.util.converters.StringConverter
 * @see com.cp.common.beans.util.converters.UserConverter
 * @see org.apache.commons.beanutils.Converter
 * @see org.apache.commons.beanutils.converters.BigDecimalConverter
 * @see org.apache.commons.beanutils.converters.BigIntegerConverter
 * @see org.apache.commons.beanutils.converters.ByteConverter
 * @see org.apache.commons.beanutils.converters.CharacterConverter
 * @see org.apache.commons.beanutils.converters.ClassConverter
 * @see org.apache.commons.beanutils.converters.DoubleConverter
 * @see org.apache.commons.beanutils.converters.FileConverter
 * @see org.apache.commons.beanutils.converters.FloatConverter
 * @see org.apache.commons.beanutils.converters.IntegerConverter
 * @see org.apache.commons.beanutils.converters.LongConverter
 * @see org.apache.commons.beanutils.converters.ShortConverter
 * @see org.apache.commons.beanutils.converters.URLConverter
 */

package com.cp.common.beans.util;

import com.cp.common.beans.User;
import com.cp.common.beans.util.converters.BooleanConverter;
import com.cp.common.beans.util.converters.CalendarConverter;
import com.cp.common.beans.util.converters.EnumConverter;
import com.cp.common.beans.util.converters.IdentifiableConverter;
import com.cp.common.beans.util.converters.NumberConverter;
import com.cp.common.beans.util.converters.ProcessConverter;
import com.cp.common.beans.util.converters.StringConverter;
import com.cp.common.beans.util.converters.UserConverter;
import com.cp.common.lang.Assert;
import com.cp.common.lang.Identifiable;
import com.cp.common.lang.ObjectUtil;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.BigIntegerConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.ClassConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FileConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.converters.URLConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class ConvertUtil {

  protected static final Converter NO_OP_CONVERTER = new NoOpConverter();

  @SuppressWarnings("unused")
  private static final Log logger = LogFactory.getLog(ConvertUtil.class);

  private static final Map<Class, Converter> DEFAULT_CONVERTER_MAP = new HashMap<Class, Converter>();
  private static final Map<Class, Converter> USER_CONVERTER_MAP = new HashMap<Class, Converter>();

  static {
    DEFAULT_CONVERTER_MAP.put(BigDecimal.class, new BigDecimalConverter(null));
    DEFAULT_CONVERTER_MAP.put(BigInteger.class, new BigIntegerConverter(null));
    DEFAULT_CONVERTER_MAP.put(Boolean.class, new BooleanConverter());
    DEFAULT_CONVERTER_MAP.put(Byte.class, new ByteConverter(null));
    DEFAULT_CONVERTER_MAP.put(Calendar.class, new CalendarConverter());
    DEFAULT_CONVERTER_MAP.put(Character.class, new CharacterConverter(null));
    DEFAULT_CONVERTER_MAP.put(Class.class, new ClassConverter(null));
    DEFAULT_CONVERTER_MAP.put(Double.class, new DoubleConverter(null));
    DEFAULT_CONVERTER_MAP.put(File.class, new FileConverter(null));
    DEFAULT_CONVERTER_MAP.put(Float.class, new FloatConverter(null));
    DEFAULT_CONVERTER_MAP.put(com.cp.common.enums.Enum.class, new EnumConverter());
    DEFAULT_CONVERTER_MAP.put(Identifiable.class, new IdentifiableConverter());
    DEFAULT_CONVERTER_MAP.put(Integer.class, new IntegerConverter(null));
    DEFAULT_CONVERTER_MAP.put(Long.class, new LongConverter(null));
    DEFAULT_CONVERTER_MAP.put(Number.class, new NumberConverter());
    DEFAULT_CONVERTER_MAP.put(com.cp.common.beans.Process.class, new ProcessConverter());
    DEFAULT_CONVERTER_MAP.put(Short.class, new ShortConverter(null));
    DEFAULT_CONVERTER_MAP.put(String.class, new StringConverter());
    DEFAULT_CONVERTER_MAP.put(URL.class, new URLConverter(null));
    DEFAULT_CONVERTER_MAP.put(User.class, new UserConverter());
  }

  /**
   * Default private constructor to enforce non-instantiability.
   */
  private ConvertUtil() {
  }

  /**
   * Converts the specified Object value into an Object of the specified type.
   * @param type the type of Object to convert value to.
   * @param value the value Object being converted.
   * @return a converted Object to the specified type of the specified value.
   */
  public static Object convert(final Class type, final Object value) {
    Assert.notNull(type, "The class type to convert to cannot be null!");

    final Converter converter = ObjectUtil.getDefaultValue(getConverter(USER_CONVERTER_MAP, type),
      getConverter(DEFAULT_CONVERTER_MAP, type), NO_OP_CONVERTER);

    if (logger.isDebugEnabled()) {
      logger.debug("converter (" + converter.getClass().getName() + ")");
    }

    return converter.convert(type, value);
  }

  /**
   * Gets a Converter for the specified Class type in the Converter mapping.
   * @param converterMap the Map of Class types to Converter object for that Class type.
   * @param type the Class type to retrieve a Converter for.
   * @return a Converter object for the specified Class type or null if no such Converter object is registered
   * to the specified Class type.
   */
  protected static Converter getConverter(final Map<Class, Converter> converterMap, final Class type) {
    Assert.notNull(converterMap, "The Converter Map cannot be null!");
    Assert.notNull(type, "The class type for the registered Converter cannot be null!");

    // first, look for a Converter for the specific type.
    if (converterMap.containsKey(type)) {
      return converterMap.get(type);
    }

    // then, look for a Converter by the parent class of type.
    for (final Class registeredType : converterMap.keySet()) {
      if (registeredType.isAssignableFrom(type)) {
        return converterMap.get(registeredType);
      }
    }

    return null;
  }

  /**
   * Registers and associates the specified Converter object to the Class type.
   * @param type the specified Class type.
   * @param converter the Converter object register with the specified Class type.
   * @throws NullPointerException if either the Class type or Converter object are null!
   */
  public static void register(final Class type, final Converter converter) {
    Assert.notNull(type, "The class type cannot be null!");
    Assert.notNull(converter, "The converter cannot be null!");
    USER_CONVERTER_MAP.put(type, converter);
  }

  /**
   * Removes the registered association of the Class type to Converter object.
   * @param type the Class type to unregister the Converter object for.
   * @return the Converter object registered and associated with the specified Class type.
   */
  public static Converter unregister(final Class type) {
    return USER_CONVERTER_MAP.remove(type);
  }

  /**
   * The NoOpConverter class is an implementation of the Converter interface the returns the value exactly as is.
   */
  private static final class NoOpConverter implements Converter {

    public Object convert(final Class type, final Object value) {
      return value;
    }
  }

}
