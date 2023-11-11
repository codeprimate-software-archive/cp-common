/*
 * MutableInvocationHandler.java (c) 6 June 2010
 *
 * Copyright (c) 2010, Codeprimate LLC
 * All Rights Reserved
 * @author jblum
 * @version 2010.6.10
 * @see com.cp.common.lang.Mutable
 * @see com.cp.common.lang.reflect.AbstractInvocationHandler
 * @see com.cp.common.lang.reflect.MethodMatcher
 * @see java.lang.reflect.Method
 */

package com.cp.common.lang.reflect;

import com.cp.common.lang.Mutable;
import com.cp.common.lang.ObjectImmutableException;
import com.cp.common.lang.ObjectUtil;
import java.lang.reflect.Method;

public class MutableInvocationHandler extends AbstractInvocationHandler implements Mutable {

  private boolean mutable = Mutable.MUTABLE;

  private MethodMatcher methodMatcher;

  /**
   * Constructs an instance of the MutableInvocationHandler class requiring an object instance that will be the
   * target of method invocations.
   * @param target an Object instance that is the target of method invocations.
   */
  public MutableInvocationHandler(final Object target) {
    super(target);
  }

  /**
   * Gets the method matcher associated with this InvocationHandler for filtering methods by criteria defined
   * by the matcher.  The method matcher determines which methods should be handled by special logic in the
   * InvocationHandler.  If method matcher for this InvocationHandler is null, then an instance of the
   * MutatorMethodMatcher is returned.
   * @return the MethodMatcher object associated with this InvocationHandler.
   * @see MutatorMethodMatcher
   */
  public MethodMatcher getMethodMatcher() {
    return ObjectUtil.getDefaultValue(methodMatcher, MutatorMethodMatcher.INSTANCE);
  }

  /**
   * Sets the method matcher associated with this InvocationHandler for filtering methods by criteria defined
   * by the matcher.  The method matcher determines which methods should be handled by special logic in the
   * InvocationHandler.
   * @param methodMatcher the MethodMatcher object to be associated with and used by this InvocationHandler
   * during method invocations.
   */
  public void setMethodMatcher(final MethodMatcher methodMatcher) {
    this.methodMatcher = methodMatcher;
  }

  /**
   * Determines whether the mutable state of objects who's methods are invoked by this InvocationHandler is set
   * to mutable or immutable.
   * @return a boolean value indicating whether this object is mutable instance or read-only.
   */
  public boolean isMutable() {
    return mutable;
  }

  /**
   * Sets whether the mutable state of objects who's methods are invoked by this InvocationHandler should be mutable
   * or immutable.
   * @param mutable a boolean value indicating whether this object should be mutable or read-only.
   */
  public void setMutable(final boolean mutable) {
    this.mutable = mutable;
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
   */
  @Override
  public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
    if ("isMutable".equals(method.getName())) {
      return isMutable();
    }
    else if ("setMutable".equals(method.getName())) {
      setMutable(Boolean.valueOf(String.valueOf(args[0])));
      return null;
    }
    else {
      if (getMethodMatcher().matches(method) && !isMutable()) {
        throw new ObjectImmutableException("Object of class (" + getTargetClass().getName() + ") is immutable!");
      }
      else {
        return super.invoke(proxy, method, args);
      }

    }
  }

}
