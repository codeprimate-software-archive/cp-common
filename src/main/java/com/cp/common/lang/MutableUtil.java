/*
 * MutableUtil.java (c) 19 October 2003
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.10
 * @see com.cp.common.lang.Mutable
 * @see com.cp.common.lang.ObjectImmutableException
 * @see com.cp.common.lang.reflect.MutableInvocationHandler
 */

package com.cp.common.lang;

import com.cp.common.lang.reflect.MethodMatcher;
import com.cp.common.lang.reflect.MutableInvocationHandler;
import com.cp.common.lang.reflect.MutatorMethodMatcher;
import java.awt.Point;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public final class MutableUtil {

  private static final Set<Class> JAVA_IMMUTABLE_CLASSES = new HashSet<Class>(13);

  static {
    JAVA_IMMUTABLE_CLASSES.add(null);
    JAVA_IMMUTABLE_CLASSES.add(Boolean.class);
    JAVA_IMMUTABLE_CLASSES.add(Byte.class);
    JAVA_IMMUTABLE_CLASSES.add(Short.class);
    JAVA_IMMUTABLE_CLASSES.add(Integer.class);
    JAVA_IMMUTABLE_CLASSES.add(Long.class);
    JAVA_IMMUTABLE_CLASSES.add(Float.class);
    JAVA_IMMUTABLE_CLASSES.add(Double.class);
    JAVA_IMMUTABLE_CLASSES.add(BigDecimal.class);
    JAVA_IMMUTABLE_CLASSES.add(BigInteger.class);
    JAVA_IMMUTABLE_CLASSES.add(Character.class);
    JAVA_IMMUTABLE_CLASSES.add(String.class);
    JAVA_IMMUTABLE_CLASSES.add(Point.class);
  }

  /**
   * Default private constructor to prevent instantiation of the MutableUtil utility class.
   */
  private MutableUtil() {
  }

  /**
   * Determines whether the specified object is an instance of an immutable class in the Java language.
   * @param obj the object instance being evaluated for immutability.
   * @return a boolean value indicating whether the object is an instance of one of Java's immutable class types.
   */
  public static boolean isInstanceOfJavaImmutableClass(final Object obj) {
    return JAVA_IMMUTABLE_CLASSES.contains(ClassUtil.getClass(obj));
  }

  /**
   * Determines whether the specified object is mutable or immutable.  The object is immutable if it is null or
   * implements the Mutable interface and the object's mutable property is set to false (immutable).  In addition,
   * the object is immutable if it is an instance of one of Java' primitive type wrapper classes in the
   * java.lang package.
   * @param obj the object evaluated for mutability.
   * @return a boolean value of true if the object is mutable, false if the objects is immutable.
   * @see MutableUtil#isInstanceOfJavaImmutableClass(Object)
   */
  public static boolean isMutable(final Object obj) {
    if (obj instanceof Mutable) {
      return ((Mutable) obj).isMutable();
    }
    else {
      return !isInstanceOfJavaImmutableClass(obj);
    }
  }

  /**
   * Determines whether the specified object is possibly an instance of an immutable class.
   * @param obj the Object being evaluated for immutability.
   * @return a boolean value indicating whether the object is an instance of the immutable class.
   * @see MutableUtil#isInstanceOfJavaImmutableClass(Object)
   */
  public static boolean isPotentiallyImmutable(final Object obj) {
    if (isInstanceOfJavaImmutableClass(obj) || (obj instanceof Mutable && !((Mutable) obj).isMutable())) {
      return true;
    }
    else {
      // Note, an immutable class, such as the java.lang.String class, is final.
      if (Modifier.isFinal(obj.getClass().getModifiers())) {
        // Note, we are only interested in the public members of a class.
        for (final Method method : obj.getClass().getMethods()) {
          if (MutatorMethodMatcher.INSTANCE.matches(method)) {
            return false;
          }
        }

        return true;
      }

      return false;
    }
  }

  /**
   * Allows any Object to implement the Mutable interface and to enforce immutable state on the given object instance
   * depending on the state of the mutable property.  If the object instance's mutable property is set to immutable,
   * then any mutator method call on the object will result in an ObjectImmutableException being thrown.
   * @param obj the object instance to have implement the Mutable interface and to enforce the immutable state.
   * mutable property of the implementing object.
   * @return a proxy object instance encapsulating the original object instance and implementing the Mutable interface.
   * @see MutableUtil#implementMutable(Object, com.cp.common.lang.reflect.MethodMatcher)
   */
  public static <T extends Mutable> T implementMutable(final Object obj) {
    return MutableUtil.<T>implementMutable(obj, null);
  }

  /**
   * Allows any Object to implement the Mutable interface and to enforce immutable state on the given object instance
   * depending on the state of the mutable property.  If the object instance's mutable property is set to immutable,
   * then any mutator method call on the object will result in an ObjectImmutableException being thrown.
   * @param obj the object instance to have implement the Mutable interface and to enforce the immutable state.
   * @param methodMatcher an instance of MethodMatcher describing which methods will adhere to and enforce the
   * mutable property of the implementing object.
   * @return a proxy object instance encapsulating the original object instance and implementing the Mutable interface.
   */
  public static <T extends Mutable> T implementMutable(final Object obj, final MethodMatcher methodMatcher) {
    Assert.isFalse(isInstanceOfJavaImmutableClass(obj), "The object is either an instance of class ("
      + ClassUtil.getClassName(obj) + ") which is a Java language immutable class or null!");

    Class[] allInterfaces = ClassUtil.getAllInterfaces(obj.getClass());
    List<Class> allInterfacesList = new LinkedList<Class>(Arrays.asList(allInterfaces));

    if (!allInterfacesList.contains(Mutable.class)) {
      allInterfacesList.add(Mutable.class);
      allInterfaces = allInterfacesList.toArray(new Class[allInterfacesList.size()]);
    }

    final MutableInvocationHandler invocationHandler = new MutableInvocationHandler(obj);
    invocationHandler.setMethodMatcher(methodMatcher);

    return (T) Proxy.newProxyInstance(obj.getClass().getClassLoader(), allInterfaces, invocationHandler);
  }

  /**
   * Verifies that the specified object is indeed mutable.  If the object is immutable (as defined by the
   * isMutable method), the method throws an ObjectImmutableException.
   * @param obj an object being verified for mutability.
   * @throws ObjectImmutableException if the object is immutable.
   * @see MutableUtil#isMutable(Object)
   */
  public static void verifyMutable(final Object obj) {
    Assert.state(isMutable(obj), new ObjectImmutableException("The object of type (" + ClassUtil.getClassName(obj)
      + ") is immutable!"));
  }

}
