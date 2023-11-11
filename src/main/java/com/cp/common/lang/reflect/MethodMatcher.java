/*
 * MethodMatcher.java (c) 6 June 2010
 *
 * The MethodMatcher interface defines 
 * Copyright (c) 2010, Codeprimate LLC
 * All Rights Reserved
 * @author jblum
 * @version 2010.6.6
 * @see com.cp.common.util.Filter
 * @see java.lang.reflect.Method
 */

package com.cp.common.lang.reflect;

import com.cp.common.util.Filter;
import java.lang.reflect.Method;

public interface MethodMatcher extends Filter<Method> {

  /**
   * Determines whether the specified method matches the criteria of this matcher.
   * @param method a Method object representing the method being evaluated by this matcher.
   * @return a boolean value indicating if the method matches the criteria of this matcher (filter).
   */
  public boolean matches(final Method method);

}
