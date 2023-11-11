/*
 * MutatorMethod.java (c) 7 June 2010
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
import java.util.Set;
import java.util.TreeSet;

public final class MutatorMethodMatcher extends AbstractMethodMatcher {

  public static final MutatorMethodMatcher INSTANCE = new MutatorMethodMatcher();

  private static final Set<String> MUTATOR_METHOD_NAMES = new TreeSet<String>();

  static {
    MUTATOR_METHOD_NAMES.add("add");
    MUTATOR_METHOD_NAMES.add("addAll");
    MUTATOR_METHOD_NAMES.add("append");
    MUTATOR_METHOD_NAMES.add("concat");
    MUTATOR_METHOD_NAMES.add("clear");
    MUTATOR_METHOD_NAMES.add("concatenate");
    MUTATOR_METHOD_NAMES.add("put");
    MUTATOR_METHOD_NAMES.add("remove");
    MUTATOR_METHOD_NAMES.add("removeAll");
    MUTATOR_METHOD_NAMES.add("retain");
    MUTATOR_METHOD_NAMES.add("retainAll");
  }

  /**
   * Determines whether the specified method matches the criteria of the mutator matcher.
   * @param method a Method object representing the method being evaluated by this matcher.
   * @return a boolean value indicating if the method matches the criteria of the mutator matcher (filter).
   */
  public boolean matches(final Method method) {
    Assert.notNull(method, "The method object cannot be null!");
    final String methodName = method.getName();
    return (methodName.startsWith("set") || MUTATOR_METHOD_NAMES.contains(methodName));
  }

}
