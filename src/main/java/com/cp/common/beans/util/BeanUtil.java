/*
 * BeanUtil.java (c) 14 January 2007
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.beans.User
 * @see com.cp.common.beans.annotation.BoundedDate
 * @see com.cp.common.beans.annotation.BoundedLength
 * @see com.cp.common.beans.annotation.BoundedNumber
 * @see com.cp.common.beans.annotation.Default
 * @see com.cp.common.beans.annotation.Required
 * @see com.cp.common.beans.util.ConvertUtil
 * @see com.cp.common.log4j.BeanRenderer
 * @see com.cp.common.log4j.CalendarRenderer
 * @see com.cp.common.log4j.CollectionRenderer
 * @see com.cp.common.log4j.EnumRenderer
 * @see com.cp.common.log4j.MapRenderer
 * @see com.cp.common.log4j.ProcessRenderer
 * @see com.cp.common.log4j.UserRenderer
 * @see java.beans.BeanInfo
 * @see java.beans.Introspector
 * @see java.beans.PropertyChangeEvent
 * @see java.beans.PropertyDescriptor
 * @see org.apache.log4j.or.ObjectRenderer
 */

package com.cp.common.beans.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

import com.cp.common.beans.Bean;
import com.cp.common.beans.User;
import com.cp.common.beans.annotation.BoundedDate;
import com.cp.common.beans.annotation.BoundedLength;
import com.cp.common.beans.annotation.BoundedNumber;
import com.cp.common.beans.annotation.Default;
import com.cp.common.beans.annotation.Required;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.ExceptionUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.log4j.BeanRenderer;
import com.cp.common.log4j.CalendarRenderer;
import com.cp.common.log4j.CollectionRenderer;
import com.cp.common.log4j.DefaultRenderer;
import com.cp.common.log4j.EnumRenderer;
import com.cp.common.log4j.MapRenderer;
import com.cp.common.log4j.ProcessRenderer;
import com.cp.common.log4j.UserRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.or.ObjectRenderer;

public final class BeanUtil {

  private static final Log logger = LogFactory.getLog(BeanUtil.class);

  static Map<Class, ObjectRenderer> DEFAULT_RENDERER_MAP = new HashMap<Class, ObjectRenderer>(7);
  private static final Map<Class, ObjectRenderer> USER_RENDERER_MAP = new HashMap<Class, ObjectRenderer>();

  static {
    DEFAULT_RENDERER_MAP.put(Bean.class, new BeanRenderer());
    DEFAULT_RENDERER_MAP.put(Calendar.class, new CalendarRenderer(Bean.DEFAULT_DATE_FORMAT_PATTERN));
    DEFAULT_RENDERER_MAP.put(Collection.class, new CollectionRenderer());
    DEFAULT_RENDERER_MAP.put(com.cp.common.enums.Enum.class, new EnumRenderer());
    DEFAULT_RENDERER_MAP.put(Map.class, new MapRenderer());
    DEFAULT_RENDERER_MAP.put(com.cp.common.beans.Process.class, new ProcessRenderer());
    DEFAULT_RENDERER_MAP.put(User.class, new UserRenderer());
    DEFAULT_RENDERER_MAP = Collections.unmodifiableMap(DEFAULT_RENDERER_MAP);
  }

  private static final Pattern INDEXED_PROPERTY_PATTERN = Pattern.compile(
    "[a-zA-Z_$]+[\\w$]*\\[\\d+\\]");

  private static final Pattern NESTED_PROPERTY_PATTERN = Pattern.compile(
    "[a-zA-Z_$]+[\\w$]*(\\[\\d+\\])?(\\.[a-zA-Z_$]+[\\w$]*(\\[\\d+\\])?){1,}");

  private static final String DOT_NOTATION = ".";
  private static final String LEFT_BRACKET = "[";

  /**
   * Default private constructor to prevent instantiation of the BeanUtil class.
   */
  private BeanUtil() { }

  /**
   * Determines whether the specified Method object is annotated with the specified Annotation type.
   * @param method the Method object used in determining if the method is annotated.
   * @param annotation the Annotation type this method is looking for on the Method object.
   * @return a boolean value indicating if the specified Method is annotated with the Annotation.
   */
  static boolean isAnnotationPresent(final Method method, final Class<? extends Annotation> annotation) {
    return (ObjectUtil.isNotNull(method) && method.isAnnotationPresent(annotation));
  }

  /**
   * Gets all the annotated properties of the specified bean Object for the specified type of Annotation.
   * @param annotatedBean the bean Object containing annotated properties.
   * @param annotation the specific Annotation type to return a Set of properties for having the Annotation.
   * @return a Set of Strings specifying all the properties of the specified bean Object which are annotated
   * with the specified Annotation type.
   * @throws NullPointerException if the annotatedBean or Annotation type are null.
   * @see BeanUtil#getBeanInfo(Object)
   */
  public static Set<String> getAnnotatedProperties(final Object annotatedBean,
                                                   final Class<? extends Annotation> annotation)
  {
    Assert.notNull(annotatedBean, "The annotated bean cannot be null!");
    Assert.notNull(annotation, "The Annotation cannot be null!");

    final BeanInfo beanInfo = getBeanInfo(annotatedBean);
    final Set<String> annotatedPropertySet = new TreeSet<String>();

    for (final PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
      final Method readMethod = propertyDescriptor.getReadMethod();

      if (isAnnotationPresent(readMethod, annotation)) {
        annotatedPropertySet.add(propertyDescriptor.getName());
      }

      final Method writeMethod = propertyDescriptor.getWriteMethod();

      if (isAnnotationPresent(writeMethod, annotation)) {
        annotatedPropertySet.add(propertyDescriptor.getName());
      }
    }

    return annotatedPropertySet;
  }

  /**
   * Gets the BeanInfo class of the bean Object.  The BeanInfo class provides information about the bean Object
   * describing properties, behaviors and events of the bean Object.
   * @param bean the bean Object being described.
   * @return a BeanInfo class describing the specified bean Object using Introspection.
   * @throws BeanIntrospectionException if the bean Object could not be described.
   * @see java.beans.BeanInfo
   * @see java.beans.Introspector
   */
  public static BeanInfo getBeanInfo(final Object bean) {
    Assert.notNull(bean, "The bean cannot be null!");

    try {
      return Introspector.getBeanInfo(bean.getClass());
    }
    catch (IntrospectionException e) {
      logger.error("An Introspection Exception occurred while inspecting bean (" + bean.getClass().getName() + ")!", e);
      throw new BeanIntrospectionException("An Introspection Exception occurred while inspecting bean ("
        + bean.getClass().getName() + ")!", e);
    }
  }

  /**
   * Gets all properties of the specified bean Object annotated with the BoundedDate Annotation.
   * @param annotatedBean the bean Object whos properties are analyzed for Annotations.
   * @return a Set of Strings specifying property names of all the properties of the specified bean Object
   * that are annotated with the BoundedDate Annotation.
   * @see BeanUtil#getAnnotatedProperties(Object, Class<? extends java.lang.annotation.Annotation>)
   * @see com.cp.common.beans.annotation.BoundedDate
   */
  public static Set<String> getBoundedDateProperties(final Object annotatedBean) {
    return getAnnotatedProperties(annotatedBean, BoundedDate.class);
  }

  /**
   * Gets all properties of the specified bean Object annotated with the BoundedLength Annotation.
   * @param annotatedBean the bean Object whos properties are analyzed for Annotations.
   * @return a Set of Strings specifying property names of all the properties of the specified bean Object
   * that are annotated with the BoundedLength Annotation.
   * @see BeanUtil#getAnnotatedProperties(Object, Class<? extends java.lang.annotation.Annotation>)
   * @see com.cp.common.beans.annotation.BoundedLength
   */
  public static Set<String> getBoundedLengthProperties(final Object annotatedBean) {
    return getAnnotatedProperties(annotatedBean, BoundedLength.class);
  }

  /**
   * Gets all properties of the specified bean Object annotated with the BoundedNumber Annotation.
   * @param annotatedBean the bean Object whos properties are analyzed for Annotations.
   * @return a Set of Strings specifying property names of all the properties of the specified bean Object
   * that are annotated with the BoundedNumber Annotation.
   * @see BeanUtil#getAnnotatedProperties(Object, Class<? extends java.lang.annotation.Annotation>)
   * @see com.cp.common.beans.annotation.BoundedNumber
   */
  public static Set<String> getBoundedNumberProperties(final Object annotatedBean) {
    return getAnnotatedProperties(annotatedBean, BoundedNumber.class);
  }

  /**
   * Gets all properties of the specified bean Object annotated with the Default Annotation.
   * @param annotatedBean the bean Object whos properties are analyzed for Annotations.
   * @return a Set of Strings specifying property names of all the properties of the specified bean Object
   * that are annotated with the Default Annotation.
   * @see BeanUtil#getAnnotatedProperties(Object, Class<? extends java.lang.annotation.Annotation>)
   * @see com.cp.common.beans.annotation.Default
   */
  public static Set<String> getDefaultedProperties(final Object annotatedBean) {
    return getAnnotatedProperties(annotatedBean, Default.class);
  }

  /**
   * Determines whether the specified property by name is an indexed property.  An indexed property is any property
   * using brackets to denote an element of some array or collection.
   * @param propertyName the name of the property used in determining whether the property is indexed.
   * @return a boolean value indicating if the property specified by name is indexed or not.
   * @see BeanUtil#isNestedProperty(String)
   */
  public static boolean isIndexedProperty(final String propertyName) {
    return (StringUtil.isNotEmpty(propertyName) && INDEXED_PROPERTY_PATTERN.matcher(propertyName).matches());
  }

  /**
   * Gets the index of the specified indexed property.
   * @param indexedPropertyName a String value specifying the name of the indexed property as well as the index itself.
   * For instance, the name of the property has the following format: propertyName[index].
   * @return an integer value for the specified index in the property's name.
   */
  static int getIndex(final String indexedPropertyName) {
    final int leftBracketIndex = StringUtil.indexOf(indexedPropertyName, LEFT_BRACKET);
    return (leftBracketIndex > -1 ? Integer.parseInt(StringUtil.getDigitsOnly(
      indexedPropertyName.substring(leftBracketIndex))) : -1);
  }

  /**
   * Determines whether the specified property by name is a nested property.  A nested property is any property using
   * dot notation to denote a property on an Object that is yet another property of some target Object.
   * @param propertyName the name of the property used in determining whether the property is nested.
   * @return a boolean value indicating if the property specified by name is nested or not.
   * @see BeanUtil#isIndexedProperty(String)
   */
  public static boolean isNestedProperty(final String propertyName) {
    return (StringUtil.isNotEmpty(propertyName) && NESTED_PROPERTY_PATTERN.matcher(propertyName).matches());
  }

  /**
   * Gets all property names of the bean Object.
   * @param bean the Object whose properties (following the JavaBeans naming convention) are returned.
   * @return a Set of Strings specifying the properties of the bean Object.
   * @see BeanUtil#getReadableProperties(Object)
   * @see BeanUtil#getWritableProperties(Object)
   */
  public static Set<String> getAllProperties(Object bean) {

    BeanInfo beanInfo = getBeanInfo(bean);
    Set<String> properties = new TreeSet<>();

    for (final PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
      properties.add(propertyDescriptor.getName());
    }

    return properties;
  }

  /**
   * Gets all accessible, readablable properties (properties with a public getter method) by name of the bean Object.
   * @param bean the Object whose accessible, readable properties (following the JavaBeans naming convention)
   * are returned.
   * @return a Set of Strings specifying the accessible and readable properties of the bean Object.
   * @see BeanUtil#getAllProperties(Object)
   * @see BeanUtil#getWritableProperties(Object)
   */
  public static Set<String> getReadableProperties(final Object bean) {
    final BeanInfo beanInfo = getBeanInfo(bean);
    final Set<String> properties = new TreeSet<String>();

    for (final PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
      final Method readMethod = propertyDescriptor.getReadMethod();

      if (ObjectUtil.isNotNull(readMethod) && Modifier.isPublic(readMethod.getModifiers())) {
        properties.add(propertyDescriptor.getName());
      }
    }

    return properties;
  }

  /**
   * Gets all accessible, writable properties (properties with a public setter method) by name of the bean Object.
   * @param bean the Object whose accessible, writable properties (following the JavaBeans naming convention)
   * are returned.
   * @return a Set of Strings specifying the accessible and writable properties of the bean Object.
   * @see BeanUtil#getAllProperties(Object)
   * @see BeanUtil#getReadableProperties(Object)
   */
  public static Set<String> getWritableProperties(final Object bean) {
    final BeanInfo beanInfo = getBeanInfo(bean);
    final Set<String> properties = new TreeSet<String>();

    for (final PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
      final Method writeMethod = propertyDescriptor.getWriteMethod();

      if (ObjectUtil.isNotNull(writeMethod) && Modifier.isPublic(writeMethod.getModifiers())) {
        properties.add(propertyDescriptor.getName());
      }
    }

    return properties;
  }

  /**
   * Constructs a PropertyChangeEvent object initialized to the source of the event, the property to which this event
   * is associated, the property's old and new values.
   * @param source the Object source of the event.
   * @param propertyName the String name of the property who's change triggered the event.
   * @param oldValue the old Object value of the property prior to the change.
   * @param newValue the new Object value of the property after the change.
   * @return a PropertyChangeEvent object capturing the event and all associated information.
   * @see java.beans.PropertyChangeEvent
   */
  public static PropertyChangeEvent getPropertyChangeEvent(final Object source,
                                                           final String propertyName,
                                                           final Object oldValue,
                                                           final Object newValue)
  {
    return new PropertyChangeEvent(source, propertyName, oldValue, newValue);
  }

  /**
   * Gets the PropertyDescriptor object for the specified property of the specified bean Object.
   * @param bean the bean Object who's property is being described.
   * @param propertyName the String name of the property in which to get the PropertyDescriptor for.
   * @return a PropertyDescriptor describing the property specified by name on the specified bean Object.
   * @throws NoSuchPropertyException if the property specified by name does not exist on the specified bean Object.
   * @see BeanUtil#getBeanInfo(Object)
   * @see BeanUtil#getPropertyValue(Object, String)
   * @see java.beans.PropertyDescriptor
   */
  public static PropertyDescriptor getPropertyDescriptor(Object bean, String propertyName) {
    Assert.notNull(bean, "The bean cannot be null!");
    Assert.notEmpty(propertyName, "The name of the property must be specified!");

    if (isNestedProperty(propertyName)) {
      final StringTokenizer nestedPropertyParser = new StringTokenizer(propertyName, DOT_NOTATION, false);

      while (nestedPropertyParser.hasMoreTokens()) {
        propertyName = nestedPropertyParser.nextToken().trim();

        if (nestedPropertyParser.hasMoreTokens()) {
          final Object nestedBean = getPropertyValueForNonNestedProperty(bean, propertyName);
          Assert.notNull(nestedBean, "The value of property (" + propertyName + ") on bean ("
            + bean.getClass().getName() + ") cannot be null!");
          bean = nestedBean;
        }
      }
    }

    if (isIndexedProperty(propertyName)) {
      propertyName = propertyName.substring(0, propertyName.indexOf(LEFT_BRACKET));
    }

    final BeanInfo beanInfo = getBeanInfo(bean);

    for (final PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
      if (ObjectUtil.equals(propertyName, propertyDescriptor.getName())) {
        return propertyDescriptor;
      }
    }

    logger.warn("(" + propertyName + ") is not a property of bean (" + bean.getClass().getName() + ")!");
    throw new NoSuchPropertyException("(" + propertyName + ") is not a property of bean ("
      + bean.getClass().getName() + ")!");
  }


  /**
   * Get the value of the specified property by name for the given bean Object.
   * @param bean the bean Object from which to retrive the value of the specified property.
   * @param propertyName the String name of the property on the bean to retrieve the value for.
   * @return the Object value of the specified property on the given bean Object.
   * @see BeanUtil#getPropertyValueForNonNestedProperty(Object, String)
   */
  public static <T> T getPropertyValue(Object bean, String propertyName) {
    Assert.notNull(bean, "The bean cannot be null!");
    Assert.notEmpty(propertyName, "The name of the property must be specified!");

    if (isNestedProperty(propertyName)) {
      final StringTokenizer nestedPropertyParser = new StringTokenizer(propertyName, DOT_NOTATION, false);

      while (nestedPropertyParser.hasMoreTokens()) {
        propertyName = nestedPropertyParser.nextToken().trim();

        if (nestedPropertyParser.hasMoreTokens()) {
          final Object nestedBean = getPropertyValueForNonNestedProperty(bean, propertyName);
          Assert.notNull(nestedBean, "The value of property (" + propertyName + ") on bean ("
            + bean.getClass().getName() + ") cannot be null!");
          bean = nestedBean;
        }
      }
    }

    return (T) getPropertyValueForNonNestedProperty(bean, propertyName);
  }

  /**
   * Gets the value for the specified property of the specified bean Object.
   * @param bean the bean Object who's property value is being retrieved.
   * @param propertyName the String name specifying the property of the bean Object to get the value of.
   * @return an Object value for the specified property of the specified bean Object.
   * @throws FailedToReadPropertyException if the property specified by name could not be read, such as in an
   * IllegalAccessException.
   * @throws UnreadablePropertyException if the property specified by name is write only.
   * @see BeanUtil#getPropertyDescriptor(Object, String)
   */
  static <T> T getPropertyValueForNonNestedProperty(final Object bean, final String propertyName) {
    Assert.isFalse(isNestedProperty(propertyName), "The property (" + propertyName + ") should not be nested!");

    final PropertyDescriptor propertyDescriptor = getPropertyDescriptor(bean, propertyName);
    final Method readMethod = propertyDescriptor.getReadMethod();

    if (ObjectUtil.isNotNull(readMethod)) {
      try {
        final Object value = readMethod.invoke(bean, (Object[]) null);

        if (isIndexedProperty(propertyName)) {
          final int index = getIndex(propertyName);

          if (logger.isDebugEnabled()) {
            logger.debug("index property name (" + propertyName + ")");
            logger.debug("index (" + index + ")");
          }

          if (value instanceof Object[]) {
            return (T) ((Object[]) value)[index];
          }
          else if (value instanceof Collection) {
            return (T) new ArrayList<Object>((Collection<?>) value).get(index);
          }
          else {
            logger.warn("The value of property (" + propertyName + ") on bean (" + bean.getClass().getName()
              + ") is not an array or Collection type!");
            throw new IllegalArgumentException("The value of property (" + propertyName + ") on bean ("
              + bean.getClass().getName() + ") is not an array or Collection type!");
          }
        }

        return (T) value;
      }
      catch (Exception e) {
        final Throwable t = ExceptionUtil.getCauseIfInvocationTargetException(e);
        logger.error("Failed to read property (" + propertyName + ") of bean (" + bean.getClass().getName() + ")!", t);
        throw new FailedToReadPropertyException("Failed to read property (" + propertyName + ") of bean ("
          + bean.getClass().getName() + ")!", t);
      }
    }
    else {
      logger.warn("Property (" + propertyName + ") of bean (" + bean.getClass().getName() + ") cannot be read!");
      throw new UnreadablePropertyException("Property (" + propertyName + ") of bean (" + bean.getClass().getName()
        + ") cannot be read!");
    }
  }

  /**
   * Gets the values of all readable properties on the specified bean Object.
   * @param bean the bean Object for which a mapping of property names to property values is returned.
   * @return a Map of property names to the property values of the specified bean Object.
   * @throws FailedToReadPropertyException any property of the specified bean Object could not be read,
   * such as in an IllegalAccessException.
   * @see BeanUtil#getBeanInfo(Object)
   */
  public static Map<String, Object> getPropertyValues(final Object bean) {
    final BeanInfo beanInfo = getBeanInfo(bean);
    final Map<String, Object> propertyValueMap = new TreeMap<String, Object>();

    for (final PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
      final Method readMethod = propertyDescriptor.getReadMethod();

      if (ObjectUtil.isNotNull(readMethod)) {
        final String propertyName = propertyDescriptor.getName();
        Object propertyValue;

        try {
          propertyValue = readMethod.invoke(bean, (Object[]) null);
        }
        catch (Exception e) {
          logger.error("Failed to read property (" + propertyName + ") of bean (" + bean.getClass().getName() + ")!", e);
          throw new FailedToReadPropertyException("Failed to read property (" + propertyName + ") of bean ("
            + bean.getClass().getName() + ")!", e);
        }

        propertyValueMap.put(propertyName, propertyValue);
      }
    }

    return propertyValueMap;
  }

  /**
   * Gets all properties of the specified bean Object annotated with the Required Annotation.
   * @param annotatedBean the bean Object whos properites are analyzed.
   * @return a Set of Strings specifying property names of all the properties of the specified bean Object
   * that are annotated with the Required Annotation.
   * @see BeanUtil#getAnnotatedProperties(Object, Class<? extends java.lang.annotation.Annotation>)
   * @see com.cp.common.beans.annotation.Required
   */
  public static Set<String> getRequiredProperties(final Object annotatedBean) {
    return getAnnotatedProperties(annotatedBean, Required.class);
  }

  /**
   * Sets the value for the specified property on the given bean Object.
   * @param bean the bean Object who's property value is being set with the given Object value.
   * @param propertyName the String name of the property on the bean Object to set the value for.
   * @param value the Object value to set the specified property of the given bean Object to.
   * @throws FailedToWritePropertyException if the property specified by name could not be written, such as in an
   * IllegalAccessException.
   * @throws UnwritablePropertyException if the property specified by name is read only.
   * @see BeanUtil#getPropertyValueForNonNestedProperty(Object, String)
   */
  public static void setPropertyValue(Object bean, String propertyName, final Object value) {
    Assert.notNull(bean, "The bean cannot be null!");
    Assert.notEmpty(propertyName, "The name of the property must be specified!");

    if (isNestedProperty(propertyName)) {
      final StringTokenizer nestedPropertyParser = new StringTokenizer(propertyName, DOT_NOTATION, false);

      while (nestedPropertyParser.hasMoreTokens()) {
        propertyName = nestedPropertyParser.nextToken().trim();

        if (nestedPropertyParser.hasMoreTokens()) {
          final Object nestedBean = getPropertyValueForNonNestedProperty(bean, propertyName);
          Assert.notNull(nestedBean, "The value of property (" + propertyName + ") on bean ("
            + bean.getClass().getName() + ") cannot be null!");
          bean = nestedBean;
        }
      }
    }

    setPropertyValueForNonNestedNonIndexedProperty(bean, propertyName, value);
  }

  /**
   * Sets the value for the specified property on the given bean Object.
   * @param bean the bean Object who's property value is being retrieved.
   * @param propertyName the String name specifying the property of the bean Object to set the value for.
   * @param value the Object value to set the specified property of the specified bean Object to.
   * @throws FailedToWritePropertyException if the property specified by name could not be written, such as in an
   * IllegalAccessException.
   * @throws UnwritablePropertyException if the property specified by name is read only.
   * @see BeanUtil#getPropertyDescriptor(Object, String)
   * @see com.cp.common.beans.util.ConvertUtil
   */
  static void setPropertyValueForNonNestedNonIndexedProperty(final Object bean, final String propertyName, Object value) {
    Assert.isFalse(isIndexedProperty(propertyName) || isNestedProperty(propertyName), "Cannot set the value of an indexed or nested property ("
      + propertyName + ")!");

    final PropertyDescriptor propertyDescriptor = getPropertyDescriptor(bean, propertyName);
    final Method writeMethod = propertyDescriptor.getWriteMethod();

    if (ObjectUtil.isNotNull(writeMethod)) {
      try {
        value = ConvertUtil.convert(propertyDescriptor.getPropertyType(), value);
        writeMethod.invoke(bean, value);
      }
      catch (Exception e) {
        final Throwable t = ExceptionUtil.getCauseIfInvocationTargetException(e);
        logger.error("Failed to write property (" + propertyName + ") of type ("
          + propertyDescriptor.getPropertyType().getName() + ") on bean (" + bean.getClass().getName()
          + ") with value (" + value + ")!", t);
        throw new FailedToWritePropertyException("Failed to write property (" + propertyName + ") of type ("
          + propertyDescriptor.getPropertyType().getName() + ") on bean (" + bean.getClass().getName()
          + ") with value (" + value + ")!", t);
      }
    }
    else {
      logger.warn("Cannot set value (" + value + ") of property (" + propertyName + ") on bean ("
        + bean.getClass().getName() + ")!");
      throw new UnwritablePropertyException("Cannot set value (" + value + ") of property (" + propertyName
        + ") on bean (" + bean.getClass().getName() + ")!");
    }
  }

  /**
   * Sets the values of all the properties in the specified Map object on the specified bean Object.
   * @param bean the bean Object for which properties will be set.
   * @param propertyValueMap a Map of property names to property values used to set the values of properties
   * of the specified bean Object.
   * @throws FailedToWritePropertyException any property of the specified bean Object could not be written,
   * such as in an IllegalAccessException.
   * @throws UnwritablePropertyException if any property specified in the Map is read only on the specified
   * bean Object.
   * @see BeanUtil#setPropertyValue(Object, String, Object)
   */
  public static void setPropertyValues(final Object bean, final Map<String, Object> propertyValueMap) {
    Assert.notNull(propertyValueMap, "The property to value Map cannot be null!");

    for (final String propertyName : propertyValueMap.keySet()) {
      setPropertyValue(bean, propertyName, propertyValueMap.get(propertyName));
    }
  }

  /**
   * Gets an ObjectRenderer from the specified mapping for a given Object.
   * @param obj the Object who's Class type will be used to get an ObjectRenderer from the mapping.
   * @param rendererMap a Map object mapping Class types to ObjectRenderers.
   * @return an ObjectRenderer for the given Object from the specified mapping.
   */
  static ObjectRenderer getRenderer(final Object obj, final Map<Class, ObjectRenderer> rendererMap) {
    Assert.notNull(rendererMap, "The ObjectRenderer Map cannot be null!");

    final Class objClass = ClassUtil.getClass(obj);
    final ObjectRenderer renderer = rendererMap.get(objClass);

    if (ObjectUtil.isNull(renderer) && ObjectUtil.isNotNull(objClass)) {
      for (final Class type : rendererMap.keySet()) {
        if (type.isAssignableFrom(objClass)) {
          return rendererMap.get(type);
        }
      }
    }

    return renderer;
  }

  /**
   * Registers the specified ObjectRenderer for the given Class type.  Thus, all objects of the given Class type
   * with be rendered using the specified Log4J ObjectRenderer.
   * @param type the Class type of the objects to be rendered with the specified ObjectRenderer.
   * @param renderer the ObjectRenderer used to format object of the given Class type into Strings.
   * @return a boolean value indicating if the ObjectRenderer was successfully registered for the given Class type.
   * @see BeanUtil#unregisterRenderer(Class)
   */
  public static boolean registerRenderer(final Class type, final ObjectRenderer renderer) {
    Assert.notNull(type, "The Class to register the ObjectRenderer for cannot be null!");
    Assert.notNull(renderer, "The ObjectRenderer cannot be null");
    USER_RENDERER_MAP.put(type, renderer);
    return (renderer == USER_RENDERER_MAP.get(type));
  }

  /**
   * Removes the registered ObjectRenderer for the specified Class type from the user-defined ObjectRenderer mapping.
   * @param type the Class type associated with the ObjectRenderer.
   * @return a boolean value indicating whether the ObjectRenderer for the specified Class type was removed or not.
   * @see BeanUtil#registerRenderer(Class, org.apache.log4j.or.ObjectRenderer)
   */
  public static boolean unregisterRenderer(final Class type) {
    return ObjectUtil.isNotNull(USER_RENDERER_MAP.remove(type));
  }

  /**
   * Returns a String representation for the specified Object value
   * @param value the Object who's String representation is returned.
   * @return a String representation of the specified Object value.
   * @see BeanUtil#getRenderer(Object, java.util.Map)
   */
  public static String toString(final Object value) {
    final ObjectRenderer renderer = ObjectUtil.getDefaultValue(getRenderer(value, USER_RENDERER_MAP),
      getRenderer(value, DEFAULT_RENDERER_MAP), DefaultRenderer.INSTANCE);
    return renderer.doRender(value);
  }

}
