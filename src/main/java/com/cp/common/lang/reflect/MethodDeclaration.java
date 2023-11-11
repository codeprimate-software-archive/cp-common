/*
 * MethodDeclaration.java (c) 10 June 2010
 *
 * Copyright (c) 2010, Codeprimate LLC
 * All Rights Reserved
 * @author jblum
 * @version 2010.06.10
 * @see java.lang.reflect.Method
 */

package com.cp.common.lang.reflect;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ArrayUtil;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MethodDeclaration implements Serializable {

  private int modifiers;

  private Class declaringClass;

  private final Class[] parameterTypes;

  private final String methodName;

  public MethodDeclaration(final String methodName) {
    this(null, methodName, (Class[]) null);
  }

  public MethodDeclaration(final String methodName, final Class... parameterTypes) {
    this(null, methodName, parameterTypes);
  }

  public MethodDeclaration(final Class declaringClass, final String methodName, final Class... parameterTypes) {
    Assert.notBlank(methodName, "The name of the method cannot be null, empty or whitespace!");
    this.declaringClass = declaringClass;
    this.methodName = methodName;
    this.parameterTypes = parameterTypes;
  }

  public Class getDeclaringClass() {
    return declaringClass;
  }

  public void setDeclaringClass(final Class declaringClass) {
    this.declaringClass = declaringClass;
  }

  public String getMethodName() {
    return methodName;
  }

  public int getModifiers() {
    return modifiers;
  }

  public Class[] getParameterTypes() {
    return parameterTypes;
  }

  protected <T> List<T> asList(final T[] array) {
    return (ObjectUtil.isNull(array) ? Collections.<T>emptyList() : Arrays.asList(array));
  }

  public <T> T invoke(final Object obj, final Object... arguments) {
    Assert.notNull(obj, "The target object on which this method (" + getMethodName()
      + ") will be invoked cannot be null!");

    if (Modifier.isStatic(getModifiers())) {
      return ObjectUtil.<T>invokeClassMethod(ObjectUtil.getDefaultValue(getDeclaringClass(), obj.getClass()),
        getMethodName(), getParameterTypes(), arguments);
    }
    else {
      Assert.equals(getDeclaringClass(), obj.getClass(), "The class types of method declaration (" + getDeclaringClass()
        + ") and the object (" + obj.getClass() + ") do not match!");
      return ObjectUtil.<T>invokeInstanceMethod(obj, getMethodName(), getParameterTypes(), arguments);
    }
  }

  public boolean is(final Method method) {
    boolean same = false;

    if (ObjectUtil.isNotNull(method)) {
      same = (ObjectUtil.isNull(getDeclaringClass()) || method.getDeclaringClass().equals(getDeclaringClass()));
      same &= getMethodName().equals(method.getName());
      same &= asList(getParameterTypes()).equals(asList(method.getParameterTypes()));
    }

    return same;
  }

  public void set(final int modifier) {
    this.modifiers = (this.modifiers | modifier);
  }

  public String toString() {
    final StringBuffer buffer = new StringBuffer("{declaring class = ");
    buffer.append(ClassUtil.getClassName(getDeclaringClass()));
    buffer.append(", method name = ").append(getMethodName());
    buffer.append(", parameter types = ").append(ArrayUtil.toString(getParameterTypes()));
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }
}
