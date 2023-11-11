/*
 * ObjectUtil.java (c) 6 August 2003
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.7
 * @see java.lang.Class
 * @see java.lang.Object
 */

package com.cp.common.lang;

import com.cp.common.util.SystemException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class ObjectUtil {

  private static final Log log = LogFactory.getLog(ObjectUtil.class);

  /**
   * Default private constructor to prevent instantiation of the ObjectUtil utility class.  ObjectUtil is a
   * utility class used to perform common operations on objects in the Java programming language.
   */
  private ObjectUtil() {
  }

  /**
   * Compares two Objects for equality by handling null Object references.  Both Objects are considered equal
   * if and only if both are not null, both are the same Object or both Objects are equal in value.
   * @param obj1 the first Object in comparison for equality with Object 2.
   * @param obj2 the second Object in the comparison for equality with Object 1.
   * @return a boolean value indicating wether the specified Objects are equal.
   * @see ObjectUtil#equalsIgnoreNull(Object, Object)
   */
  public static boolean equals(final Object obj1, final Object obj2) {
    return (isNotNull(obj1) && obj1.equals(obj2));
  }

  /**
   * Compares two Objects for equality by ignoring null Object reference.  Both Objects are considered equal
   * if both are null, both are the same Object, or both are different Objects but equal in value.
   * @param obj1 the first Object in comparison for equality with Object 2.
   * @param obj2 the second Object in the comparison for equality with Object 1.
   * @return a boolean value indicating wether the specified Objects are equal.
   * @see ObjectUtil#equals(Object, Object)
   */
  public static boolean equalsIgnoreNull(final Object obj1, final Object obj2) {
    return (isNull(obj1) ? isNull(obj2) : obj1.equals(obj2));
  }

  /**
   * Returns the value parameter if not null, the first non-null default value or null if the value
   * parameter and all default values are null.
   * @param value the Object value in question for being null.
   * @param defaultValues the default Object values to be tested and returned if the value parameter is null.
   * @return the value parameter if not null, the first non-null default value or null if the value parameter
   * and all default values are null.
   * @see ObjectUtil#getSingleDefaultValue(Object, Object)
   */
  public static <T> T getDefaultValue(final T value, final T... defaultValues) {
    if (isNull(value) && isNotNull(defaultValues)) {
      for (final T defaultValue : defaultValues) {
        if (isNotNull(defaultValue)) {
          return defaultValue;
        }
      }
    }

    return value;
  }

  /**
   * Returns the defaultValue parameter if the specified value parameter is null,
   * otherwise returns the value parameter.
   * @param value the Object value returned from this method if value is not null.
   * @param defaultValue the Object value returned from this method if value is null.
   * @return value if not null, otherwise return defaultValue.
   */
  public static <T> T getSingleDefaultValue(final T value, final T defaultValue) {
    return (isNotNull(value) ? value : defaultValue);
  }

  /**
   * Computes the hashCode value of the specified Object.  If the Object is null, then this method returns 0.
   * @param obj the Object parameter in which to compute it's hash code value.
   * @return an integer value constituting the Object parameters hash code value,
   * or zero if the Object parameter is null.
   */
  public static int hashCode(final Object obj) {
    return (isNull(obj) ? 0 : obj.hashCode());
  }

  /**
   * Invokes the named method from the specified Class type.
   * @param type the Class object defining the method to be invoked.
   * @param methodName a String value specifying the name of the method to invoke on the target object.
   * @return the return value from the named method invoked on the Class object.
   * @throws SystemException if no such method, specified by name, can be found on the object's class type,
   * illegal access to the method on object occurs or the invocation of the method on object throws an Exception!
   * @see ObjectUtil#invokeMethod(com.cp.common.lang.ObjectUtil.MethodInvocationStrategy)
   */
  public static <T> T invokeClassMethod(final Class type, final String methodName) {
    return (T) invokeMethod(new StaticMethodInvocationStrategy(type, methodName));
  }

  /**
   * Invokes the named method from the specified Class type using the specified arguments.
   * @param type the Class object defining the method to be invoked.
   * @param methodName a String value specifying the name of the method to invoke on the target object.
   * @param args an array of Object values used as arguments to the method invocation.
   * @return the return value from the named method invoked on the Class object.
   * @throws SystemException if no such method, specified by name, can be found on the object's class type,
   * illegal access to the method on object occurs or the invocation of the method on object throws an Exception!
   * @see ObjectUtil#invokeMethod(com.cp.common.lang.ObjectUtil.MethodInvocationStrategy)
   */
  public static <T> T invokeClassMethod(final Class type, final String methodName, final Object[] args) {
    return (T) invokeMethod(new StaticMethodInvocationStrategy(type, methodName, args));
  }

  /**
   * Invokes the named method from the specified Class type using the specified arguments.
   * @param type the Class object defining the method to be invoked.
   * @param methodName a String value specifying the name of the method to invoke on the target object.
   * @param argTypes an array of Class objects specifying the types of the arguments passed to the method invocation.
   * @param args an array of Object values used as arguments to the method invocation.
   * @return the return value from the named method invoked on the Class object.
   * @throws SystemException if no such method, specified by name, can be found on the object's class type,
   * illegal access to the method on object occurs or the invocation of the method on object throws an Exception!
   * @see ObjectUtil#invokeMethod(com.cp.common.lang.ObjectUtil.MethodInvocationStrategy)
   */
  public static <T> T invokeClassMethod(final Class type,
                                         final String methodName,
                                         final Class[] argTypes,
                                         final Object[] args) {
    return (T) invokeMethod(new StaticMethodInvocationStrategy(type, methodName, argTypes, args));
  }

  /**
   * Invokes the named method on the target object.
   * @param obj the Object to invoke the named method on.
   * @param methodName a String value specifying the name of the method to invoke on the target object.
   * @return the return value from the named method invoked on the target object.
   * @throws SystemException if no such method, specified by name, can be found on the object's class type,
   * illegal access to the method on object occurs or the invocation of the method on object throws an Exception!
   * @see ObjectUtil#invokeMethod(com.cp.common.lang.ObjectUtil.MethodInvocationStrategy)
   */
  public static <T> T invokeInstanceMethod(final Object obj, final String methodName) {
    return (T) invokeMethod(new InstanceMethodInvocationStrategy(obj, methodName));
  }

  /**
   * Invokes the named method with the specified arguments on the target object.
   * @param obj the Object to invoke the named method on.
   * @param methodName a String value specifying the name of the method to invoke on the target object.
   * @param args an array of Object values used as arguments to the method invoked on the target object.
   * @return the return value from the named method invoked on the target object.
   * @throws SystemException if no such method, specified by name, can be found on the object's class type,
   * illegal access to the method on object occurs or the invocation of the method on object throws an Exception!
   * @see ObjectUtil#invokeMethod(com.cp.common.lang.ObjectUtil.MethodInvocationStrategy)
   */
  public static <T> T invokeInstanceMethod(final Object obj, final String methodName, final Object[] args) {
    return (T) invokeMethod(new InstanceMethodInvocationStrategy(obj, methodName, args));
  }

  /**
   * Invokes the specified named method with the specified arguments on the target object.
   * @param obj the Object to invoke the named method on.
   * @param methodName a String value specifying the name of the method to invoke on the target object.
   * @param argTypes an array of Class objects specifying the types of the arguments passed to the method invocation.
   * @param args an array of Object values used as arguments to the method invoked on the target object.
   * @return the return value from the named method invoked on the target object.
   * @throws SystemException if no such method, specified by name, can be found on the object's class type,
   * illegal access to the method on object occurs or the invocation of the method on object throws an Exception!
   * @see ObjectUtil#invokeMethod(com.cp.common.lang.ObjectUtil.MethodInvocationStrategy)
   */
  public static <T> T invokeInstanceMethod(final Object obj,
                                           final String methodName,
                                           final Class[] argTypes,
                                           final Object[] args) {
    return (T) invokeMethod(new InstanceMethodInvocationStrategy(obj, methodName, argTypes, args));
  }

  /**
   * Invokes a named method on an object instance or a Class object depending upon the MethodInvocationStrategy used.
   * @param methodInvocationStrategy a MethodInvocationStrategy used in determining how the method will be invoked.
   * @return the return value from the named method invoked.
   * @throws SystemException if no such method, specified by name, can be found on the object's class type,
   * illegal access to the method on object occurs or the invocation of the method on object throws an Exception!
   */
  private static <T> T invokeMethod(final MethodInvocationStrategy methodInvocationStrategy) {
    try {
      return (T) methodInvocationStrategy.invoke();
    }
    catch (NoSuchMethodException e) {
      log.error("The method (" + methodInvocationStrategy.getMethodName() + ") does not exist on class ("
        + methodInvocationStrategy.getClassName() + ")!", e);
      throw new SystemException("The method (" + methodInvocationStrategy.getMethodName() + ") does not exist on class ("
        + methodInvocationStrategy.getClassName() + ")!", e);
    }
    catch (IllegalAccessException e) {
      log.error("Failed to access method (" + methodInvocationStrategy.getMethodName() + ") on class ("
        + methodInvocationStrategy.getClassName() + "); please verify the method access modifier and system permissions!", e);
      throw new SystemException("Failed to access method (" + methodInvocationStrategy.getMethodName() + ") on class ("
        + methodInvocationStrategy.getClassName() + "); please verify the method access modifier and system permissions!", e);
    }
    catch (InvocationTargetException e) {
      log.error("Invoking method (" + methodInvocationStrategy.getMethodName() + ") on class ("
        + methodInvocationStrategy.getClassName() + ") threw an Exception!", e.getCause());
      throw new SystemException("Invoking method (" + methodInvocationStrategy.getMethodName() + ") on class ("
        + methodInvocationStrategy.getClassName() + ") threw an Exception!", e.getCause());
    }
  }

  /**
   * Determines whether all object references in an array of Objects are null.
   * @param args an array of Objects to test if all references are null.
   * @return a boolean value of true if all object references in the array of Objects are null.
   * @see ObjectUtil#isNotNull(Object)
   */
  public static boolean areAllNull(final Object... args) {
    for (final Object arg : args) {
      if (isNotNull(arg)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Determines whether at least one object reference in an array of Objects is null.
   * @param args an array of Objects to test for a null reference.
   * @return a boolean value of true if the array of Objects contains at least one null object reference.
   * @see ObjectUtil#isNull(Object)
   */
  public static boolean isAnyNull(final Object... args) {
    for (final Object arg : args) {
      if (isNull(arg)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Determines whether the specified Object parameter is NOT null.
   * @param obj the Object in question of being NOT null.
   * @return a boolean value indicating whether the object is NOT null.
   * @see ObjectUtil#isNull(Object)
   */
  public static boolean isNotNull(final Object obj) {
    return (obj != null);
  }

  /**
   * Determines if the specified Objects are different, or not the same (identical), using the identity comparison.
   * @param obj1 an Object in the identity comparison.
   * @param obj2 an Object in the identity comparison.
   * @return a boolean value indicating whether Object 1 is different than Object 2.
   */
  public static boolean isNotSame(final Object obj1, final Object obj2) {
    return (obj1 != obj2);
  }

  /**
   * Determines whether the specified Object parameter is null.
   * @param obj the Object in question of being null.
   * @return a boolean value indicating whether the object is null.
   * @see ObjectUtil#isNotNull(Object)
   */
  public static boolean isNull(final Object obj) {
    return (obj == null);
  }

  /**
   * Determines whether the specified Objects are the same (identical) by performing an identity comparison.
   * @param obj1 an Object in the identity comparison.
   * @param obj2 an Object in the identity comparison.
   * @return a boolean value indicating whether Object 1 is the same as Object 2.
   */
  public static boolean isSame(final Object obj1, final Object obj2) {
    return (obj1 == obj2);
  }

  /**
   * Returns a String representation of the Object parameter by calling toString on the Object,
   * or null if the Object parameter is null.
   * @param obj the Object to return a String representation for.
   * @return a String representation of the specified Object parameter by calling toString on the Object,
   * or null if the Object parameter is null.
   */
  public static String toString(final Object obj) {
      return (isNull(obj) ? null : obj.toString());
  }

  protected static interface MethodInvocationStrategy {

    public String getClassName();

    public String getMethodName();

    public Object invoke() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;

  }

  protected static abstract class AbstractMethodInvocationStrategy implements MethodInvocationStrategy {

    private final Class clz;

    private Class[] argumentTypes;

    private Object[] arguments;

    private final String methodName;

    public AbstractMethodInvocationStrategy(final Class clz, final String methodName) {
      this(clz, methodName, null, null);
    }

    public AbstractMethodInvocationStrategy(final Class clz, final String methodName, final Object[] arguments) {
      this(clz, methodName, null, arguments);
    }

    public AbstractMethodInvocationStrategy(final Class clz,
                                            final String methodName,
                                            final Class[] argumentTypes,
                                            final Object[] arguments) {
      Assert.notNull(clz, "The class for which the method will be invoked cannot be null!");
      Assert.notEmpty(methodName, "The name of the method cannot be null or empty!");
      this.clz = clz;
      this.methodName = methodName;
      this.argumentTypes = argumentTypes;
      this.arguments = arguments;
    }

    protected Object[] getArguments() {
      return arguments;
    }

    protected Class[] getArgumentTypes() {
      return (isNotNull(argumentTypes) ? argumentTypes
        : (isNotNull(getArguments()) ? ClassUtil.getClassTypes(getArguments()) : null));
    }

    public String getClassName() {
      return clz.getName();
    }

    protected abstract Object getInvocationObject();

    protected Method getMethod() throws NoSuchMethodException {
      return clz.getDeclaredMethod(getMethodName(), getArgumentTypes());
    }

    public String getMethodName() {
      return methodName;
    }

    public Object invoke() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return getMethod().invoke(getInvocationObject(), getArguments());
    }
  }

  protected static final class InstanceMethodInvocationStrategy extends AbstractMethodInvocationStrategy {

    private final Object obj;

    public InstanceMethodInvocationStrategy(final Object obj, final String methodName) {
      super(ClassUtil.getClass(obj), methodName);
      this.obj = obj;
    }

    public InstanceMethodInvocationStrategy(final Object obj, final String methodName, final Object[] arguments) {
      super(ClassUtil.getClass(obj), methodName, arguments);
      this.obj = obj;
    }

    public InstanceMethodInvocationStrategy(final Object obj,
                                            final String methodName,
                                            final Class[] argumentTypes,
                                            final Object[] arguments) {
      super(ClassUtil.getClass(obj), methodName, argumentTypes, arguments);
      this.obj = obj;
    }

    @Override
    protected Object getInvocationObject() {
      return obj;
    }
  }

  protected static final class StaticMethodInvocationStrategy extends AbstractMethodInvocationStrategy {

    public StaticMethodInvocationStrategy(final Class clz, final String methodName) {
      super(clz, methodName);
    }

    public StaticMethodInvocationStrategy(final Class clz, final String methodName, final Object[] arguments) {
      super(clz, methodName, arguments);
    }

    public StaticMethodInvocationStrategy(final Class clz,
                                          final String methodName,
                                          final Class[] argumentTypes,
                                          final Object[] arguments) {
      super(clz, methodName, argumentTypes, arguments);
    }

    @Override
    protected Object getInvocationObject() {
      return null;
    }
  }

}
