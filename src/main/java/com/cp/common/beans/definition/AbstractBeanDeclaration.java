/*
 * AbstractBeanDeclaration.java (c) 10 January 2008
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see com.cp.common.beans.definition.AbstractBeanDefinition
 * @see com.cp.common.beans.definition.BeanDeclaration
 * @see com.cp.common.beans.definition.InvocationArgument
 * @see com.cp.common.beans.definition.ListenerDeclaration
 * @see com.cp.common.beans.definition.Parameterizable
 * @see com.cp.common.beans.definition.PropertyDeclaration
 * @see com.cp.common.beans.definition.Scope
 */

package com.cp.common.beans.definition;

import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ArrayUtil;
import com.cp.common.util.CollectionUtil;
import java.util.List;

public abstract class AbstractBeanDeclaration extends AbstractBeanDefinition<ListenerDeclaration, PropertyDeclaration> implements BeanDeclaration {

  protected static final boolean DEFAULT_LAZY_INIT = false;

  protected static final Scope DEFAULT_BEAN_SCOPE = Scope.PROTOTYPE;

  private boolean lazyInit = DEFAULT_LAZY_INIT;

  private final Parameterizable parameterHandler;

  private Scope scope = DEFAULT_BEAN_SCOPE;

  private String destroyMethod;
  private String initMethod;

  /**
   * Creates an instance of the AbstractBeanDeclaration class initialized to the specified id and class name.
   * @param id a String value indication the unique identifier of the bean definition.
   * @param className the fully qualified class name of the bean.
   */
  public AbstractBeanDeclaration(final String id, final String className) {
    super(id, className);
    parameterHandler = getParameterHandlerImpl();
  }

  /**
   * Gets the String name of the bean method to call to destroy the bean.
   * @return a String value indicating the name of the bean method to call to destroy the bean.
   * @see BeanDeclaration#getInitMethod()
   */
  public String getDestroyMethod() {
    return destroyMethod;
  }

  /**
   * Sets the String name of the bean method to call to destroy the bean.
   * @param destroyMethod a String value indicating the name of the bean method to call to destroy the bean.
   */
  public void setDestroyMethod(final String destroyMethod) {
    this.destroyMethod = destroyMethod;
  }

  /**
   * Gets the String name of the bean method to call to initialize the bean.
   * @return a String value indicating the name of the bean method to call to initialize the bean.
   * @see BeanDeclaration#getDestroyMethod()
   */
  public String getInitMethod() {
    return initMethod;
  }

  /**
   * Sets the String name of the bean method to call to initialize the bean.
   * @param initMethod a String value indicating the name of the bean method to call to initialize the bean.
   */
  public void setInitMethod(final String initMethod) {
    this.initMethod = initMethod;
  }

  /**
   * Determines whether this bean is lazily initialized.
   * @return a boolean value indicating whether the bean declared by this definition will be lazily initialized.
   */
  public boolean isLazyInit() {
    return lazyInit;
  }

  /**
   * Determines whether this bean is lazily initialized.
   * @param lazyInit a boolean value indicating whether the bean declared by this definition will be lazily initialized.
   */
  public void setLazyInit(final boolean lazyInit) {
    this.lazyInit = ObjectUtil.getDefaultValue(lazyInit, DEFAULT_LAZY_INIT);
  }

  /**
   * Gets the Parameterizable object handler instance.
   * @return an instance of the Parameterizable interface.
   */
  protected Parameterizable getParameterHandler() {
    return parameterHandler;
  }

  /**
   * Gets the implementation class for the Parameterizable interface used by this class to manage the
   * parameterized types and values to the constructors/methods of this declared bean.
   * @return an implementation of the Parameterizable interface.
   */
  protected Parameterizable getParameterHandlerImpl() {
    return BeanDefinitionsFactory.getParameterizable();
  }

  /**
   * Convenience method determining whether this bean definition was declared as a prototype.
   * @return a boolean value indicating if this bean definition was declared as a prototype.
   * @see AbstractBeanDeclaration#getScope()
   * @see AbstractBeanDeclaration#isSingleton()
   */
  public boolean isPrototype() {
    return Scope.PROTOTYPE.equals(getScope());
  }

  /**
   * Gets the scope of the bean in the specified context.
   * @return an enumerated value indicating the scope of the bean.
   * @see AbstractBeanDeclaration#isPrototype()
   * @see AbstractBeanDeclaration#isSingleton()
   */
  public Scope getScope() {
    return scope;
  }

  /**
   * Sets the scope of the bean in the specified context.
   * @param scope an enumerated value indicating the scope of the bean.
   */
  public void setScope(final Scope scope) {
    this.scope = ObjectUtil.getDefaultValue(scope, DEFAULT_BEAN_SCOPE);
  }

  /**
   * Convenience method determining whether this bean definition was declared as a singleton.
   * @return a boolean value indicating if this bean definition was declared as a singleton.
   * @see AbstractBeanDeclaration#isPrototype()
   * @see AbstractBeanDeclaration#getScope()
   */
  public boolean isSingleton() {
    return Scope.SINGLETON.equals(getScope());
  }

  /**
   * Gets the Class object type of this bean.
   * @return a Class object specifying the bean's Class type.
   * @see BeanDefinition#getClassName()
   * @throws ClassNotFoundException if the Class specified by the fully-qualified class name cannot be found
   * in the CLASSPATH!
   */
  public Class getType() throws ClassNotFoundException {
    return ClassUtil.loadClass(getClassName());
  }

  /**
   * Adds the specified InvocationArgument modeling a constructor argument of this declared bean.
   * @param constructorArgument an InvocationArgument modeling the contructor argument of this declared bean.
   * @return a boolean value indicating whether the specified InvocationArgument was successfully added to the
   * ordered list of constructor arguments for this declared bean.
   */
  public boolean add(final InvocationArgument constructorArgument) {
    return getParameterHandler().add(constructorArgument);
  }

  /**
   * Gets the specified constructor argument for this declared bean at the specified index.
   * @param index an integer value specifying the index of the constructor argument of this declared bean.
   * @return an InvocationArgument modeling the constructor argument of this declared bean at the specified index.
   */
  public InvocationArgument getInvocationArgument(final int index) {
    return getParameterHandler().getInvocationArgument(index);
  }

  /**
   * Gets a list of constructor arguments used to initialize the bean upon instantiation.
   * @return a List of InvocationArguments representing the arguments to one of the constructors of the declared bean.
   */
  public List<InvocationArgument> getInvocationArguments() {
    return getParameterHandler().getInvocationArguments();
  }

  /**
   * Gets the Class types of the arguments used in the constructor invocation of the declared bean.
   * @return an array of Class objects specifying the argument types of the parameters in the constructor of the
   * declared bean.
   */
  public Class[] getConstructorArgumentTypes() {
    return getInvocationArgumentTypes();
  }

  /**
   * Gets the Object values used as arguments to the constructor invocation of the declared bean.
   * @return an array of Objects specifying the arguments to the constructor of the declared bean.
   */
  public Object[] getConstructorArgumentValues() {
    return getInvocationArgumentValues();
  }

  /**
   * Gets the Class types of the arguments used in the constructor invocation of the declared bean.
   * @return an array of Class objects specifying the argument types of the parameters in the constructor of the
   * declared bean.
   */
  public Class[] getInvocationArgumentTypes() {
    return getParameterHandler().getInvocationArgumentTypes();
  }

  /**
   * Gets the Object values used as arguments to the constructor invocation of the declared bean.
   * @return an array of Objects specifying the arguments to the constructor of the declared bean.
   */
  public Object[] getInvocationArgumentValues() {
    return getParameterHandler().getInvocationArgumentValues();
  }

  /**
   * Removes the specified InvocationArgument instance modeling a constructor argument to the declared bean.
   * @param constructorArgument an InvocationArgument instance representing the contructor argument to the bean.
   * @return a boolean value indicating whether the specified InvocationArgument was removed from the
   * list of constructor arguments for this bean.
   */
  public boolean remove(final InvocationArgument constructorArgument) {
    return getParameterHandler().remove(constructorArgument);
  }

  /**
   * Gets a String description of the internal state of this bean definition.
   * @return a String description of the internal state of this bean definition.
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{className = ");
    buffer.append(getClassName());
    buffer.append(", constructorArguments = ").append(CollectionUtil.toString(getInvocationArguments()));
    buffer.append(", destroyMethod = ").append(getDestroyMethod());
    buffer.append(", id = ").append(getId());
    buffer.append(", initMethod = ").append(getInitMethod());
    buffer.append(", lazyInit = ").append(isLazyInit());
    buffer.append(", name = ").append(getName());
    buffer.append(", names = ").append(ArrayUtil.toString((Object[]) getNames()));
    buffer.append(", scope = ").append(getScope());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
