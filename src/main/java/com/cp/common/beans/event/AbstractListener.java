/*
 * AbstractListener.java (c) 26 December 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.4
 * @see com.cp.common.beans.util.BeanUtil
 * @see com.cp.common.beans.util.NoSuchPropertyException
 * @see java.util.EventListener
 */

package com.cp.common.beans.event;

import com.cp.common.beans.util.BeanUtil;
import com.cp.common.beans.util.NoSuchPropertyException;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.EventListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractListener implements EventListener {

  protected final Log logger = LogFactory.getLog(getClass());

  private final BeanInfo beanInfo;

  private final Object bean;

  private final String propertyName;

  /**
   * Creates an instance of the AbstractListener class.
   */
  public AbstractListener() {
    this.bean = null;
    this.beanInfo = null;
    this.propertyName = null;
  }

  /**
   * Creates an instance of the AbstractListener class initialized with an object reference to the
   * annotated bean object.  The object reference is used to introspect the bean in order to read annotations
   * used by this listener to handle event processing.
   * @param annotatedBean the annotated bean object.
   */
  public AbstractListener(final Object annotatedBean) {
    this(annotatedBean, null);
  }

  /**
   * Creates an instance of the AbstractListener class initialized with the specified property name.
   * This listener will only handle property change events from the registered bean for the specified property.
   * @param propertyName the name of the property that this listener will handle and process property change events for.
   */
  public AbstractListener(final String propertyName) {
    Assert.isFalse("".equals(StringUtil.trim(propertyName)), "The name of the property cannot be empty!");
    this.bean = null;
    this.beanInfo = null;
    this.propertyName = propertyName;
  }

  /**
   * Creates an instance of the AbstractListener class initialized with an object reference to the
   * annotated bean object as well as the specifed property name for which property change events will be handled.
   * The object reference is used to introspect the bean in order to read annotations used by this listener to handle
   * event processing.  This listener will only handle property change events from the registered bean for the
   * specified property.
   * @param annotatedBean the annotated bean object.
   * @param propertyName the name of the property that this listener will handle and process property change events for.
   */
  public AbstractListener(final Object annotatedBean, final String propertyName) {
    Assert.notNull(annotatedBean, "The bean cannot be null!");
    Assert.isFalse("".equals(StringUtil.trim(propertyName)), "The name of the property cannot be empty!");
    this.bean = annotatedBean;
    this.beanInfo = BeanUtil.getBeanInfo(this.bean);
    this.propertyName = propertyName;
  }

  /**
   * Gets the Annotation of the specified type for the property given by name.
   * @param propertyName the String name of the property to get the Annotation for.
   * @param annotationType the type of the Annotation.
   * @return the Annotation object for the specified type on the given property.
   * @throws NoSuchMethodException if the property specified by name does not have a property setter.
   * @see AbstractListener#getWriteMethod(String)
   */
  protected <T extends Annotation> T getAnnotation(final String propertyName, final Class<T> annotationType)
    throws NoSuchMethodException
  {
    return getWriteMethod(propertyName).getAnnotation(annotationType);
  }

  /**
   * Determines whether the specified Annotation of type T is present on the property of the registered bean.
   * @param propertyName the name of the property.
   * @param annotationType the type of the Annotation.
   * @return a boolean value indicating whether the specified Annotation of type T is present on the property
   * of the registered bean.
   * @throws NoSuchMethodException if the property specified by name does not have a write method.
   */
  protected <T extends Annotation> boolean isAnnotationPresent(final String propertyName, final Class<T> annotationType)
    throws NoSuchMethodException
  {
    return getWriteMethod(propertyName).isAnnotationPresent(annotationType);
  }

  /**
   * Returns the registered bean object for which this listener listens and handles property change events for.
   * @return the registered bean object that this listener handles property change events for.
   */
  public Object getBean() {
    return bean;
  }

  /**
   * Returns the BeanInfo object used to describe the characteristics of the registered bean object.
   * @return a BeanInfo describing the registered bean object.
   */
  protected BeanInfo getBeanInfo() {
    return beanInfo;
  }

  /**
   * Determines whether this listener is interested in property change events of the specified property.
   * @param propertyName the name of the property that generated a change event.
   * @return a boolean value indicating whether this listener is interested in change events of the specified property.
   */
  protected boolean isInterested(final String propertyName) {
    return (ObjectUtil.isNull(getPropertyName()) || ObjectUtil.equals(getPropertyName(), propertyName));
  }

  /**
   * Returns the PropertyDescriptor for the property with the given name.
   * @param propertyName the name of the property on the registered bean.
   * @return the PropertyDescriptor for the specified property.
   * @throws com.cp.common.beans.util.NoSuchPropertyException if the property specified by name does not exist on the registered bean.
   */
  protected PropertyDescriptor getPropertyDescriptor(final String propertyName) throws NoSuchPropertyException {
    for (final PropertyDescriptor propertyDescriptor : getBeanInfo().getPropertyDescriptors()) {
      if (ObjectUtil.equals(propertyDescriptor.getName(), propertyName)) {
        return propertyDescriptor;
      }
    }

    // Note, the NoSuchPropertyException should never be thrown since this listener was notified as a result
    // of the specified property changing.
    logger.warn("(" + propertyName + ") is not a property of bean (" + getBean().getClass().getName() + ")!");
    throw new NoSuchPropertyException("(" + propertyName + ") is not a property of bean ("
      + getBean().getClass().getName() + ")!");
  }

  /**
   * Returns the name of the property on the registered bean object for which this listener handles and processes
   * property change events for.
   * @return a String value specifying the name of the property on the registered bean object.
   */
  public String getPropertyName() {
    return propertyName;
  }

  /**
   * Returns the setter methods for the property with the given name.
   * @param propertyName the name of the property on the registered bean.
   * @return a Method object representing the setter method for the specified property.
   * @throws NoSuchMethodException if a setter method for the specified property does not exist.  There would be
   * no setter method for a read-only property on a bean.
   * @throws NoSuchPropertyException if the property specified by name does not exist on the registered bean.
   * @see AbstractListener#getPropertyDescriptor(String)
   */
  protected Method getWriteMethod(final String propertyName) throws NoSuchMethodException, NoSuchPropertyException {
    final PropertyDescriptor propertyDescriptor = getPropertyDescriptor(propertyName);
    final Method writeMethod = propertyDescriptor.getWriteMethod();

    if (ObjectUtil.isNull(writeMethod)) {
      logger.warn("No write method exists for property (" + propertyName + ") on bean ("
        + getBean().getClass().getName() + ")!");
      throw new NoSuchMethodException("No write method exists for property (" + propertyName + ") on bean ("
        + getBean().getClass().getName() + ")!");
    }

    return writeMethod;
  }

}
