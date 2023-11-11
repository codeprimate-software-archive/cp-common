/*
 * AccessorMethodMatcher.java (c) 7 June 2010
 *
 * Copyright (c) 2010, Codeprimate LLC
 * All Rights Reserved
 * @author jblum
 * @version 2010.6.10
 * @see com.cp.common.lang.reflect.AbstractMethodMatcher
 */

package com.cp.common.lang.reflect;

import com.cp.common.lang.Assert;
import java.lang.reflect.Method;

public final class AccessorMethodMatcher extends AbstractMethodMatcher {

  public static final AccessorMethodMatcher INSTANCE = new AccessorMethodMatcher();

  /**
   * Determines whether the specified method matches the criteria of the accessor matcher.
   * @param method a Method object representing the method being evaluated by this matcher.
   * @return a boolean value indicating if the method matches the criteria of the accessor matcher (filter).
   */
  public boolean matches(final Method method) {
    Assert.notNull(method, "The method object cannot be null!");
    final String methodName = method.getName();
    return (methodName.startsWith("get") || methodName.startsWith("is"));
  }

}
