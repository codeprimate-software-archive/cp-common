/*
 * BeanRenderer.java (c) 17 November 2006
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.log4j.AbstractObjectRenderer
 * @see java.beans.BeanInfo
 * @see java.bans.Introspector
 * @see java.beans.PropertyDescriptor
 * @see java.lang.reflect.Method
 */

package com.cp.common.log4j;

import com.cp.common.beans.Bean;
import com.cp.common.lang.ClassUtil;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BeanRenderer extends AbstractObjectRenderer {

  public String doRender(final Object obj) {
    if (obj instanceof Bean) {
      try {
        final Bean beanObj = (Bean) obj;
        final BeanInfo beanObjBeanInfo = Introspector.getBeanInfo(beanObj.getClass());

        final List<PropertyDescriptor> propertyDescriptorList = Arrays.asList(beanObjBeanInfo.getPropertyDescriptors());
        Collections.sort(propertyDescriptorList, PropertyDescriptorComparator.INSTANCE);

        final StringBuffer buffer = new StringBuffer("{");
        boolean condition = false;

        for (final PropertyDescriptor propertyDescriptor : propertyDescriptorList) {
          final String propertyName = propertyDescriptor.getName();
          final Method propertyReadMethod = propertyDescriptor.getReadMethod();
          final Object propertyValue = propertyReadMethod.invoke(beanObj, (Object[]) null);

          if (logger.isDebugEnabled()) {
            logger.debug("property name (" + propertyName + ")");
            logger.debug("property read method (" + propertyReadMethod.getName() + ")");
            logger.debug("property value (" + propertyValue + ")");
          }

          if (condition) {
            buffer.append(", ");
          }

          buffer.append(propertyName);
          buffer.append(" = ");
          buffer.append(getRenderer(propertyValue).doRender(propertyValue));
          condition = true;
        }

        buffer.append("}");

        return buffer.toString();
      }
      catch (Exception e) {
        logger.error("Failed to render object (" + ClassUtil.getClassName(obj) + ")!", e);
        return null;
      }
    }

    return getRenderer(obj).doRender(obj);
  }

  private static final class PropertyDescriptorComparator implements Comparator<PropertyDescriptor> {

    private static final PropertyDescriptorComparator INSTANCE = new PropertyDescriptorComparator();

    public int compare(final PropertyDescriptor propertyDescriptor1, final PropertyDescriptor propertyDescriptor2) {
      return propertyDescriptor1.getName().compareTo(propertyDescriptor2.getName());
    }
  }

}
