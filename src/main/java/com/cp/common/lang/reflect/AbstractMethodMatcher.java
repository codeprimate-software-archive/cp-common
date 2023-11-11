/*
 * AbstractMethodMatcher.java (c) 7 June 2010
 *
 * Copyright (c) 2010, Codeprimate LLC
 * All Rights Reserved
 * @author jblum
 * @version 2010.6.7
 * @see com.cp.common.lang.reflect.MethodMatcher
 * @see java.lang.reflect.Method
 */

package com.cp.common.lang.reflect;

import java.lang.reflect.Method;

public abstract class AbstractMethodMatcher implements MethodMatcher {

  /**
   * Accept determines whether the Method object representing the method being evaluated satisfies
   * the requirements/criteria of this filter.
   * @param method a Method object representing the method being evaluated for acceptance.
   * @return a boolean value indicating if the specified method satisfies the requirements/criteria of this filter.
   */
  public boolean accept(final Method method) {
    return matches(method);
  }

}
