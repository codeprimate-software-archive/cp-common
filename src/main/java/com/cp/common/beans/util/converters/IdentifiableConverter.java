/*
 * IdentifiableConverter.java (c) 18 May 2007
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see com.cp.common.beans.util.NoSuchConstructorException
 * @see com.cp.common.beans.util.converters.AbstractConverter
 * @see com.cp.common.lang.Identifiable
 * @see com.cp.common.util.ConversionException
 */

package com.cp.common.beans.util.converters;

import com.cp.common.beans.util.NoSuchConstructorException;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.Identifiable;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.ConversionException;
import java.lang.reflect.Constructor;

public class IdentifiableConverter extends AbstractConverter<Identifiable> {

  /**
   * Converts the value Object into an Identifiable object of the specified type.
   * @param type the Class type of the Identifiable parameter.
   * @param value the Object value to convert to an Identifiable object of the specified type.
   * @return the value Objects converted into an Identifiable object of the specified type.
   * @throws ConversionException if the value cannot be converted into an Identifiable object of the specified type.
   */
  protected Object convertImpl(final Class type, final Object value) {
    Assert.notNull(type, "The Class type cannot be null!");
    Assert.isAssignableFrom(type, Identifiable.class, "(" + type.getName() + ") must extend or implement the Identifiable interface!");

    if (ObjectUtil.isNotNull(value)) {
      if (type.isInstance(value)) {
        return value;
      }
      else if (value instanceof Number) {
        return getTypeInstance(type, (Number) value);
      }
      else if (StringUtil.isDigitsOnly(value.toString().trim())) {
        return getTypeInstance(type, Long.valueOf(value.toString().trim()));
      }
      else {
        logger.warn("Value (" + value + ") cannot be converted into an Identifiable object!");
        throw new ConversionException("Value (" + value + ") cannot be converted into an Identifiable object!");
      }
    }

    return null;
  }

  /**
   * Converts the Number object into an appropriate subtype specified by the paramater type.
   * @param parameterType the specific subtype of Number (such as Long, Integer, Short, Byte, etc) to convert the
   * Number object into.
   * @param number the Number object being converted into one of the Number subclasses.
   * @return a subclass of the Number object according to the parameter type.
   */
  protected Object getArgument(final Class parameterType, final Number number) {
    if (Long.class.isAssignableFrom(parameterType)) {
      return number.longValue();
    }
    else if (Integer.class.isAssignableFrom(parameterType)) {
      return number.intValue();
    }
    else if (Short.class.isAssignableFrom(parameterType)) {
      return number.shortValue();
    }
    else if (Byte.class.isAssignableFrom(parameterType)) {
      return number.byteValue();
    }
    else {
      logger.warn("Expected a whole number as the unique identifier parameter type; but was ("
        + parameterType.getName() + ")!");
      throw new ConversionException("Expected a whole number as the unique identifier parameter type; but was ("
        + parameterType.getName() + ")!");
    }
  }

  /**
   * Gets the Class constructor accepting a numerical parameter representing the unique identifier of the Identifiable
   * object of the specified type.
   * @param type the specified Class type of the Identifiable object.
   * @return a Constructor object for the Identifiable object of type accepting an numeric ID parameter.
   * @throws NoSuchConstructorException if the Identifiable object of type has no constructor accepting a
   * numeric ID parameter.
   */
  protected Constructor getConstructorWithIdParameter(final Class type) {
    for (final Constructor constructor : type.getConstructors()) {
      final Class[] parameterTypes = constructor.getParameterTypes();
      if (parameterTypes.length == 1 && Number.class.isAssignableFrom(parameterTypes[0])) {
        return constructor;
      }
    }

    logger.warn("Constructor with an ID parameter does not exist for class (" + type.getName() + ")!");
    throw new NoSuchConstructorException("Constructor with an ID parameter does not exist for class ("
      + type.getName() + ")!");
  }

  /**
   * Tries to determine the implementing class for the specified type.  If the specified type is an interface,
   * then this method will append "Impl" to the fully-qualified interface name to determine the implementing class.
   * In addition, this method assumes the implementing class is in the same package as the interface.
   * @param type the Class type for which an implementing class will be determined.
   * @return a Class object representing the implementing class of the specified type.
   */
  protected Class getImplementingClass(final Class type) {
    if (type.isInterface() && !type.isAnnotation()) {
      final String interfaceName = type.getName();
      final String className = (interfaceName + "Impl");

      if (logger.isDebugEnabled()) {
        logger.debug("interface name (" + interfaceName + ")");
        logger.debug("class name (" + className + ")");
      }

      try {
        return ClassUtil.loadClass(className);
      }
      catch (ClassNotFoundException e) {
        logger.error("Unable to determine implementing class for interface (" + interfaceName + ")!", e);
        throw new ConversionException("Unable to determine implementing class for interface (" + interfaceName + ")!", e);
      }
    }
    else if (type.isAnnotation() || type.isAnonymousClass() || type.isArray() || type.isEnum() || type.isLocalClass()
      || type.isPrimitive() || type.isSynthetic()) {
      logger.warn("Type (" + type.getName() + ") is non-instantiable!");
      throw new ConversionException("Type (" + type.getName() + ") is non-instantiable!");
    }
    else {
      // NOTE assuming the type is a class type.
      return type;
    }
  }

  /**
   * Tries to construct an Identifiable object instance of the specified type initialized with the specified
   * Number value as the Identifiable object's unique identifier.
   * @param type the specified Identifiable type.
   * @param number the Number value used as an identifier for the specified Identifiable type.
   * @return a new instance of the Identifiable object of the specific type initialized with the specified Number value
   * as the unique identifier.
   * @throws ConversionException if the Identifiable type could not be constructed with the Number value as it's
   * unique identifier!
   */
  protected Object getTypeInstance(Class type, final Number number) {
    try {
      type = getImplementingClass(type);

      final Constructor constructor = getConstructorWithIdParameter(type);

      return constructor.newInstance(getArgument(constructor.getParameterTypes()[0], number));
    }
    catch (Exception e) {
      logger.error("Failed to instantiate an instance of class (" + type.getName() + ") with ID (" + number + ")!", e);
      throw new ConversionException("Failed to instantiate an instance of class (" + type.getName() + ") with ID ("
        + number + ")!", e);
    }
  }

}
