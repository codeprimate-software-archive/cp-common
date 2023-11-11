/*
 * NamedMethodMatcher.java (c) 10 June 2010
 *
 * Copyright (c) 2010, Codeprimate LLC
 * All Rights Reserved
 * @author jblum
 * @version 2010.06.10
 * @see com.cp.common.lang.reflect.AbstractMethodMatcher
 */

package com.cp.common.lang.reflect;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.TreeSet;

public class NamedMethodMatcher extends AbstractMethodMatcher {

  private final Set<String> METHOD_NAMES = new TreeSet<String>();

  public boolean add(final String methodName) {
    return (ObjectUtil.isNotNull(methodName) && METHOD_NAMES.add(methodName));
  }

  public boolean matches(final Method method) {
    Assert.notNull(method, "The method object cannot be null!");
    return METHOD_NAMES.contains(method.getName());
  }

  public boolean remove(final String methodName) {
    return METHOD_NAMES.remove(methodName);
  }

}
