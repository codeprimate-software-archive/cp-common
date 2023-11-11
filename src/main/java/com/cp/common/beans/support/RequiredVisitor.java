/*
 * RequiredVisitor.java (c) 15 December 2006
 *
 * The RequiredVisitor walks the object graph, starting with the object who's accept method is called
 * with an instance of this Visitor, in search of objects having required properties that are not set.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.6.20
 * @see com.cp.common.lang.Visitable
 * @see com.cp.common.util.Visitor
 */

package com.cp.common.beans.support;

import com.cp.common.beans.annotation.Required;
import com.cp.common.beans.util.BeanUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Visitable;
import com.cp.common.util.Visitor;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RequiredVisitor implements Visitor {

  protected final Log logger = LogFactory.getLog(getClass());

  /**
   * Visits all objects with @Required annotated properties verifying that the property has a value.
   * @param obj the object with annotated with required properties.
   */
  public void visit(final Visitable obj) {
    for (final PropertyDescriptor requiredProperty : getRequiredProperties(obj)) {
      final Method readMethod = requiredProperty.getReadMethod();

      if (ObjectUtil.isNotNull(readMethod)) {
        try {
          final Object value = readMethod.invoke(obj, (Object[]) null);

          if (ObjectUtil.isNull(value)) {
            logger.warn("Property (" + requiredProperty.getName() + ") of bean (" + obj.getClass().getName()
              + ") is required!");
            throw new IllegalStateException("Property (" + requiredProperty.getName() + ") of bean ("
              + obj.getClass().getName() + ") is required!");
          }
        }
        catch (IllegalAccessException e) {
          logger.error("Failed to access method (" + readMethod.getName() + ") of bean (" + obj.getClass().getName()
            + ")!", e);
        }
        catch (InvocationTargetException e) {
          logger.error("Failed to invoke method (" + readMethod.getName() + ") of bean (" + obj.getClass().getName()
            + ")!", e);
        }
      }
    }
  }

  /**
   * Gets PropertyDescriptors for all required properties on the specified object.
   * @param obj the Object who's required properties are determined and described in the array of PropertyDescriptors.
   * @return an array of PropertyDescriptor objects for all the required properties of the specified object.
   */
  protected PropertyDescriptor[] getRequiredProperties(final Object obj) {
    final BeanInfo beanInfo = BeanUtil.getBeanInfo(obj);
    final List<PropertyDescriptor> requiredProperties = new LinkedList<PropertyDescriptor>();

    for (final PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
      final Method writeMethod = property.getWriteMethod();

      if (ObjectUtil.isNotNull(writeMethod) && writeMethod.isAnnotationPresent(Required.class)) {
        requiredProperties.add(property);
      }
    }

    return requiredProperties.toArray(new PropertyDescriptor[requiredProperties.size()]);
  }

}
