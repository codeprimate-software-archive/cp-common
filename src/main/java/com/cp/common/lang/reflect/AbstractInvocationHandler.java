/*
 * AbstractInvocationHandler.java (c) 7 June 2010
 *
 * Copyright (c) 2010, Codeprimate LLC
 * All Rights Reserved
 * @author jblum
 * @version 2010.6.9
 * @see java.lang.reflect.InvocationHandler
 * @see java.lang.reflect.Method
 * @see java.lang.reflect.Modifier
 */

package com.cp.common.lang.reflect;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public abstract class AbstractInvocationHandler implements InvocationHandler {

  private final Object target;

  /**
   * Constructs an instance of the AbstractInvocationHandler class requiring an object instance that will be
   * the target of the method invocation.
   * @param target an object instance that is the target of the method invocation.
   */
  public AbstractInvocationHandler(final Object target) {
    Assert.notNull(target, "The target object of the method invocation cannot be null!");
    this.target = target;
  }

  /**
   * Gets the object instance that is the target of the method invocation.
   * @return the object instance that is the target of the method invocation.
   */
  protected Object getTarget() {
    return target;
  }

  /**
   * Gets the Class type of the object instance that is the target of the method invocation.
   * @return a Class object representing the type of object that is the target of the method invocation.
   * @see AbstractInvocationHandler#getTarget()
   */
  protected Class getTargetClass() {
    return getTarget().getClass();
  }

  /**
   * Processes a method invocation on a proxy instance and returns the result. This method will be invoked
   * on an invocation handler when a method is invoked on a proxy instance that it is associated with.
   * @param proxy the proxy instance that the method was invoked on.
   * @param method the Method instance corresponding to the interface method invoked on the proxy instance.
   * The declaring class of the Method object will be the interface that the method was declared in, which may be
   * a superinterface of the proxy interface that the proxy class inherits the method through.
   * @param args an array of objects containing the values of the arguments passed in the method invocation on
   * the proxy instance, or null if interface method takes no arguments. Arguments of primitive types are wrapped
   * in instances of the appropriate primitive wrapper class, such as java.lang.Integer or java.lang.Boolean.
   * @return the value to return from the method invocation on the proxy instance.
   * @throws Throwable the exception to throw from the method invocation on the proxy instance.
   * @see com.cp.common.lang.ObjectUtil#invokeClassMethod(Class, String, Object[])
   * @see com.cp.common.lang.ObjectUtil#invokeInstanceMethod(Object, String, Object[])
   */
  public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
    if (Modifier.isStatic(method.getModifiers())) {
      return ObjectUtil.invokeClassMethod(getTargetClass(), method.getName(), method.getParameterTypes(), args);
    }
    else {
      return ObjectUtil.invokeInstanceMethod(getTarget(), method.getName(), method.getParameterTypes(), args);
    }
  }

}
