/*
 * BeanDefinitionsUtil.java (c) 6 August 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.8.23
 * @see com.cp.common.beans.definition.BeanDefinition
 * @see com.cp.common.beans.definition.InvocationArgument
 * @see com.cp.common.beans.definition.ListenerDefinition
 * @see com.cp.common.beans.definition.Parameterizable
 * @see com.cp.common.beans.definition.PropertyDefinition
 * @see com.cp.common.beans.definition.ReferenceObject
 */

package com.cp.common.beans.definition.support;

import com.cp.common.beans.definition.BeanDefinition;
import com.cp.common.beans.definition.InvocationArgument;
import com.cp.common.beans.definition.ListenerDefinition;
import com.cp.common.beans.definition.Parameterizable;
import com.cp.common.beans.definition.PropertyDefinition;
import com.cp.common.beans.definition.ReferenceObject;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;

public final class BeanDefinitionsUtil {

  private BeanDefinitionsUtil() {
  }

  /**
   * Converts the BeanDefinition to a String containing meta-data.
   * @param beanDefinition the BeanDefinition object to convert to a String.
   * @return a String containing meta-data information for the specified BeanDefinition.
   */
  public static String toString(final BeanDefinition beanDefinition) {
    if (ObjectUtil.isNotNull(beanDefinition)) {
      final StringBuffer buffer = new StringBuffer(beanDefinition.getClass().getName());
      buffer.append("(").append(beanDefinition.getId());
      buffer.append(", ").append(beanDefinition.getClassName());
      buffer.append(")");
      return buffer.toString();
    }

    return null;
  }

  /**
   * Converts the InvocationArgument to a String containing meta-data.
   * @param invocationArgument the InvocationArgument object to convert to a String.
   * @return a String containing meta-data information for the specified InvocationArgument.
   */
  public static String toString(final InvocationArgument invocationArgument) {
    if (ObjectUtil.isNotNull(invocationArgument)) {
      final StringBuffer buffer = new StringBuffer(invocationArgument.getClass().getName());

      if (StringUtil.isNotEmpty(invocationArgument.getRefId())) {
        buffer.append("(refId=").append(invocationArgument.getRefId());
      }
      else {
        buffer.append("(").append(invocationArgument.getStringType());
        buffer.append(", ").append(invocationArgument.getStringValue());
      }

      buffer.append(")");

      return buffer.toString();
    }

    return null;
  }

  /**
   * Converts the ListenerDefinition to a String containing meta-data.
   * @param listenerDefinition the ListenerDefinition object to convert to a String.
   * @return a String containing meta-data information for the specified ListenerDefinition.
   */
  public static String toString(final ListenerDefinition listenerDefinition) {
    if (ObjectUtil.isNotNull(listenerDefinition)) {
      final StringBuffer buffer = new StringBuffer(listenerDefinition.getClass().getName());
      buffer.append("(").append(listenerDefinition.getClassName()).append(")");
      return buffer.toString();
    }

    return null;
  }

  /**
   * Converts the Parameterizable to a String containing meta-data.
   * @param parameterHandler the Parameterizable object to convert to a String.
   * @return a String containing meta-data information for the specified Parameterizable.
   */
  public static String toString(final Parameterizable parameterHandler) {
    if (ObjectUtil.isNotNull(parameterHandler)) {
      final StringBuffer buffer = new StringBuffer(parameterHandler.getClass().getName());
      boolean condition = false;

      buffer.append("[");

      for (final InvocationArgument invocationArgument : parameterHandler.getInvocationArguments()) {
        buffer.append(condition ? ", " : "");
        buffer.append(toString(invocationArgument));
        condition = true;
      }

      buffer.append("]");

      return buffer.toString();
    }

    return null;
  }

  /**
   * Converts the PropertyDefinition to a String containing meta-data.
   * @param propertyDefinition the PropertyDefinition object to convert to a String.
   * @return a String containing meta-data information for the specified PropertyDefinition.
   */
  public static String toString(final PropertyDefinition propertyDefinition) {
    if (ObjectUtil.isNotNull(propertyDefinition)) {
      final StringBuffer buffer = new StringBuffer(propertyDefinition.getClass().getName());
      buffer.append("(").append(propertyDefinition.getName());
      buffer.append(", ").append(propertyDefinition.getClassName());
      buffer.append(")");
      return buffer.toString();
    }

    return null;
  }

  /**
   * Converts the ReferenceObject to a String containing meta-data.
   * @param referenceObject the ReferenceObject object to convert to a String.
   * @return a String containing meta-data information for the specified ReferenceObject.
   */
  public static String toString(final ReferenceObject referenceObject) {
    if (ObjectUtil.isNotNull(referenceObject)) {
      final StringBuffer buffer = new StringBuffer(referenceObject.getClass().getName());
      buffer.append("(").append(referenceObject.getReferenceId()).append(")");
      return buffer.toString();
    }

    return null;
  }

}
