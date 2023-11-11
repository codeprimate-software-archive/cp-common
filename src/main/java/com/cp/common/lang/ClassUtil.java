/*
 * ClassUtil.java (c) 31 May 2010
 *
 * Copyright (c) 2010, Codeprimate LLC
 * All Rights Reserved
 * @author jblum
 * @version 2010.6.3
 * @see com.cp.common.lang.ObjectUtil
 * @see java.lang.Class
 * @see java.lang.Object
 */

package com.cp.common.lang;

import com.cp.common.util.ArrayUtil;
import com.cp.common.util.SystemException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static com.cp.common.lang.ObjectUtil.isNull;

public final class ClassUtil {

  private static final Log log = LogFactory.getLog(ClassUtil.class);

  /**
   * Default private constructor to prevent instantiation of the ClassUtil utility class.
   */
  private ClassUtil() {
  }

  /**
   * Gets an array of Class objects indicating the interfaces implemented by the given class.  If the Class object
   * represents an interface, then the interface is returned as a single element in a Class array.  The interfaces
   * represented in the Class array are returned in the order in which they were declared, as specified in the
   * implements clause of a class declaration for the given class and all super classes.
   * @param clazz the Class being evaluated for implemented interfaces.
   * @return a Class array indicating all interfaces implemented by the given class.
   */
  public static Class[] getAllInterfaces(final Class clazz) {
    Assert.notNull(clazz, "The Class object cannot be null!");

    if (clazz.isInterface()) {
      return new Class[] { clazz };
    }
    else {
      final List<Class> classList = new LinkedList<Class>();
      Class currentClass = clazz;

      while (currentClass != null) {
        for (final Class implementedInterface : currentClass.getInterfaces()) {
          if (!classList.contains(implementedInterface)) {
            classList.add(implementedInterface);
          }
        }

        currentClass = currentClass.getSuperclass();
      }

      return classList.toArray(new Class[classList.size()]);
    }
  }

  /**
   * Return the Class type of the given Object.  Returns null if the Object parameter/argument is null.
   * @param obj the Object who's Class type is being determined.
   * @return the Class type of the given Object or null if the Object parameter/argument is null.
   */
  public static Class getClass(final Object obj) {
    return (isNull(obj) ? null : obj.getClass());
  }

  /**
   * Returns the class name of the given Object.
   * @param obj the Object who's class name will be determined.
   * @return a String value specifying the class name of the given Object, or null if the object is null.
   * @see ClassUtil#getClass(Object)
   * @see ClassUtil#getClassName(Class)
   */
  public static String getClassName(final Object obj) {
    return getClassName(getClass(obj));
  }

  /**
   * Returns the name of the given Class object.
   * @param clazz the Class object used to determine the name of the class.
   * @return a String value specifying the name of the given Class object, or null if the Class object is null.
   */
  public static String getClassName(final Class clazz) {
    return (isNull(clazz) ? null : clazz.getName());
  }

  /**
   * Gets an array of Class objects (class types) for all objects in the specified Object array.
   * @param objs an array of Objects who's types are being determined.
   * @return a Class array containing the class types of each object in the Object array.
   * @see ClassUtil#getClass(Object)
   */
  public static Class[] getClassTypes(final Object... objs) {
    Assert.notNull(objs, "The array of objects cannot be null!");

    final List<Class> classList = new ArrayList<Class>(objs.length);

    for (final Object obj : objs) {
      classList.add(getClass(obj));
    }

    return classList.toArray(new Class[classList.size()]);
  }

  /**
   * Constructs an instance of a class having the given class name.
   * @param className a String value specifying the fully-qualified class name of the class to instantiate.
   * @return a newly created instance of the given class.
   * @throws ClassNotFoundException if the class with the given class name could not be found in the classpath.
   * @throws InstantiationException if an instance of the class could not be created.
   * @see ClassUtil#loadClass(String)
   */
  public static <T> T getInstance(final String className) throws ClassNotFoundException, InstantiationException {
    return ClassUtil.<T>getInstance(loadClass(className), null, null);
  }

  /**
   * Constructs an instance of a class having the given class name, using a constructor having a signature
   * matching the order and types of arguments given to initialize the instance.
   * @param className a String value specifying the fully-qualified class name of the class to instantiate.
   * @param args an Object array specifying the arguments to the constructor of the class when the new instance
   *  is created.
   * @return a newly created instance of the given class initialized with the given arguments.
   * @throws ClassNotFoundException if the class with the given class name could not be found in the classpath.
   * @throws InstantiationException if an instance of the class could not be created.
   * @see ClassUtil#loadClass(String)
   * @see ClassUtil#getClassTypes(Object...)
   */
  public static <T> T getInstance(final String className, final Object[] args)
      throws ClassNotFoundException, InstantiationException
  {
    return ClassUtil.<T>getInstance(loadClass(className), getClassTypes(args), args);
  }

  /**
   * Constructs an instance of a class having the given class name, using a constructor having a signature
   * matching argument types Class array.
   * @param className a String value specifying the fully-qualified class name of the class to instantiate.
   * @param argTypes the class types of all arguments in the Object array.
   * @param args an Object array specifying the arguments to the constructor of the class when the new instance
   *  is created.
   * @return a newly created instance of the given class initialized with the given arguments.
   * @throws ClassNotFoundException if the class with the given class name could not be found in the classpath.
   * @throws InstantiationException if an instance of the class could not be created.
   * @see ClassUtil#loadClass(String)
   */
  public static <T> T getInstance(final String className, final Class[] argTypes, final Object[] args)
      throws ClassNotFoundException, InstantiationException
  {
    return ClassUtil.<T>getInstance(loadClass(className), argTypes, args);
  }

  /**
   * Constructs an instance of the specified Class object.
   * @param clazz the Class object from which the instance will be created.
   * @return an instance of the specified Class object.
   * @throws InstantiationException if an instance of the Class object could not be created.
   * @throws NullPointerException if the Class object is null.
   */
  public static <T> T getInstance(final Class clazz) throws InstantiationException {
    return ClassUtil.<T>getInstance(clazz, null, null);
  }

  /**
   * Constructs an instance of the specified Class object initialized with the given constructor arguments.
   * @param clazz the Class object from which the instance will be created.
   * @param args the Object array of arguments to the constructor of the class to initialize the instance.
   * @return an instance of the specified Class object initialized with the provided constructor arguments.
   * @throws InstantiationException if an instance of the Class object could not be created.
   * @throws NullPointerException if the Class object is null.
   * @see ClassUtil#getClassTypes(Object...)
   */
  public static <T> T getInstance(final Class clazz, final Object[] args) throws InstantiationException {
    return ClassUtil.<T>getInstance(clazz, getClassTypes(args), args);
  }

  /**
   * Creates an instance of the specified Class object initialized with the constructor arguments.
   * @param clazz the Class object from which the instance will be created.
   * @param argTypes the class types of all arguments in the args Object array.
   * @param args Object array of arguments to the constructor of the class.
   * @return an instance of the specified Class object.
   * @throws InstantiationException if an instance of the Class object could not be created.
   * @throws NullPointerException if the clazz argument is null!
   */
  public static <T> T getInstance(final Class clazz, final Class[] argTypes, final Object[] args) 
    throws InstantiationException
  {
    Assert.notNull(clazz, "The Class type from which the instance will be created cannot be null!");

    final int argTypesLength = ArrayUtil.length(argTypes);
    final int argsLength = ArrayUtil.length(args);

    Assert.equals(argTypesLength, argsLength,
      MessageFormat.format("The number of arguments ({0}) does not match the number of parameters ({1}) in the constructor of class ({2})!",
      argsLength, argTypesLength, clazz.getName()));

    try {
      if (argsLength > 0) {
        final Constructor constructor = clazz.getDeclaredConstructor(argTypes);
        return (T) constructor.newInstance(args);
      }
      else {
        return (T) clazz.newInstance();
      }
    }
    catch (NoSuchMethodException e) {
      log.error("No constructor in class (" + clazz.getName() + ") exists having the following parameters ("
        + ArrayUtil.toString(argTypes) + ")!", e);
      throw new IllegalArgumentException("No constructor in class (" + clazz.getName()
        + ") exists having the following parameters (" + ArrayUtil.toString(argTypes) + ")!", e);
    }
    catch (IllegalAccessException e) {
      log.error("Failed to access constructor of class (" + clazz.getName() + ") having parameters ("
        + ArrayUtil.toString(argTypes) + "); please verify the constructor access modifiers and system permissions!", e);
      throw new SystemException("Failed to access constructor of class (" + clazz.getName() + ") having parameters ("
        + ArrayUtil.toString(argTypes) + "); please verify the constructor access modifiers and system permissions!", e);
    }
    catch (InvocationTargetException e) {
      log.error("Invoking constructor with parameters (" + ArrayUtil.toString(argTypes) + ") of class ("
        + clazz.getName() + ") threw an Exception!", e.getCause());
      throw new SystemException("Invoking constructor with parameters (" + ArrayUtil.toString(argTypes)
        + ") of class (" + clazz.getName() + ") threw an Exception!", e.getCause());
    }
    catch (Exception e) {
      log.error("Failed to create instance of class (" + clazz.getName() + ")!", e);
      throw new InstantiationException("Failed to create instance of class (" + clazz.getName() + ")!");
    }
  }

  /**
   * Loads the Class object for the specified class name using the current thread's context class loader.
   * @param className a String value specifying the name of the class to load.
   * @return a Class object representing the class having the specified name.
   * @throws ClassNotFoundException if the class with class name could not be found in the classpath.
   * @see ClassUtil#loadClass(String, boolean, ClassLoader)
   */
  public static Class loadClass(final String className) throws ClassNotFoundException {
    return loadClass(className, true, Thread.currentThread().getContextClassLoader());
  }

  /**
   * Loads the Class object for the specified class name using the specified class loader.
   * @param className a String value specifying the name of the class to load.
   * @param initialize is a boolean value indicating whether the class should be initialized after it is loaded.
   * @param classLoader the designated class loader used to locate and load the class specified by name.
   * @return a Class object representing the class having the specified name.
   * @throws ClassNotFoundException if the class with class name could not be found in the classpath.
   * @see Class#forName(String, boolean, ClassLoader)
   */
  public static Class loadClass(final String className,
                                final boolean initialize,
                                final ClassLoader classLoader)
    throws ClassNotFoundException
  {
    return Class.forName(className, initialize, classLoader);
  }

}
