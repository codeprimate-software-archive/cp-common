/*
 * BeanDeclaration.java (c) 9 January 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.8.1
 * @see com.cp.common.beans.definition.BeanDefinition
 * @see com.cp.common.beans.definition.InvocationArgument
 * @see com.cp.common.beans.definition.ListenerDeclaration
 * @see com.cp.common.beans.definition.Parameterizable
 * @see com.cp.common.beans.definition.PropertyDeclaration
 * @see com.cp.common.beans.definition.Scope
 */

package com.cp.common.beans.definition;

public interface BeanDeclaration extends BeanDefinition<ListenerDeclaration, PropertyDeclaration>, Parameterizable {

  /**
   * Gets the String name of the bean method to call to destroy the bean.
   * @return a String value indicating the name of the bean method to call to destroy the bean.
   * @see BeanDeclaration#getInitMethod()
   */
  public String getDestroyMethod();

  /**
   * Sets the String name of the bean method to call to destroy the bean.
   * @param destroyMethod a String value indicating the name of the bean method to call to destroy the bean.
   */
  public void setDestroyMethod(String destroyMethod);

  /**
   * Gets the String name of the bean method to call to initialize the bean.
   * @return a String value indicating the name of the bean method to call to initialize the bean.
   * @see BeanDeclaration#getDestroyMethod()
   */
  public String getInitMethod();

  /**
   * Sets the String name of the bean method to call to initialize the bean.
   * @param initMethod a String value indicating the name of the bean method to call to initialize the bean.
   */
  public void setInitMethod(String initMethod);

  /**
   * Determines whether this bean is lazily initialized.
   * @return a boolean value indicating whether the bean declared by this definition will be lazily initialized.
   */
  public boolean isLazyInit();

  /**
   * Determines whether this bean is lazily initialized.
   * @param lazyInit a boolean value indicating whether the bean declared by this definition will be lazily initialized.
   */
  public void setLazyInit(boolean lazyInit);

  /**
   * Convenience method determining whether this bean definition was declared as a prototype.
   * @return a boolean value indicating if this bean definition was declared as a prototype.
   * @see BeanDeclaration#getScope()
   * @see BeanDeclaration#isSingleton()
   */
  public boolean isPrototype();

  /**
   * Gets the scope of the bean in the specified context.
   * @return an enumerated value indicating the scope of the bean.
   * @see BeanDeclaration#isPrototype()
   * @see BeanDeclaration#isSingleton()
   */
  public Scope getScope();

  /**
   * Sets the scope of the bean in the specified context.
   * @param scope an enumerated value indicating the scope of the bean.
   */
  public void setScope(Scope scope);

  /**
   * Convenience method determining whether this bean definition was declared as a singleton.
   * @return a boolean value indicating if this bean definition was declared as a singleton.
   * @see BeanDeclaration#isPrototype()
   * @see BeanDeclaration#getScope()
   */
  public boolean isSingleton();

  /**
   * Gets the Class object type of this bean.
   * @return a Class object specifying the bean's Class type.
   * @see BeanDefinition#getClassName()
   * @throws ClassNotFoundException if the Class specified by the fully-qualified class name cannot be found
   * in the CLASSPATH!
   */
  public Class getType() throws ClassNotFoundException;

  /**
   * Gets the Class types of the arguments used in the constructor invocation of the declared bean.
   * @return an array of Class objects specifying the argument types of the parameters in the constructor of the
   * declared bean.
   */
  public Class[] getConstructorArgumentTypes();

  /**
   * Gets the Object values used as arguments to the constructor invocation of the declared bean.
   * @return an array of Objects specifying the arguments to the constructor of the declared bean.
   */
  public Object[] getConstructorArgumentValues();

}
