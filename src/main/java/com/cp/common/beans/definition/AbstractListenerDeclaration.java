/*
 * AbstractListenerDeclaration.java (c) 10 January 2008
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see com.cp.common.beans.definition.AbstractListenerDefinition
 * @see com.cp.common.beans.definition.ListenerDeclaration
 * @see com.cp.common.beans.definition.Parameterizable
 * @see com.cp.common.beans.definition.Scope
 */

package com.cp.common.beans.definition;

import com.cp.common.beans.definition.support.BeanDefinitionsUtil;
import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.CollectionUtil;
import java.util.List;

public abstract class AbstractListenerDeclaration extends AbstractListenerDefinition implements ListenerDeclaration {

  protected static final Scope DEFAULT_LISTENER_SCOPE = Scope.PROTOTYPE;

  private final Parameterizable parameterHandler;

  private Scope scope = DEFAULT_LISTENER_SCOPE;

  /**
   * Creates an instance of the AbstractListenerDeclaration class initialized with the fully qualified class name
   * of the listener.
   * @param className a String value specifying the fully qualified class name of the listener.
   */
  public AbstractListenerDeclaration(final String className) {
    super(className);
    parameterHandler = getParameterHandlerImpl();
  }

  /**
   * Gets the Class types of the arguments used in the constructor invocation of the declared listener.
   * @return an array of Class objects specifying the argument types of the parameters in the constructor of the
   * declared listener.
   */
  public Class[] getConstructorArgumentTypes() {
    return getInvocationArgumentTypes();
  }

  /**
   * Gets the Object values used as arguments to the constructor invocation of the declared listener.
   * @return an array of Objects specifying the arguments to the constructor of the declared listener.
   */
  public Object[] getConstructorArgumentValues() {
    return getInvocationArgumentValues();
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
   * Convenience method determining whether this listener definition was declared as a prototype.
   * @return a boolean value indicating if this listener definition was declared as a prototype.
   * @see ListenerDeclaration#getScope()
   * @see ListenerDeclaration#isSingleton()
   */
  public boolean isPrototype() {
    return Scope.PROTOTYPE.equals(getScope());
  }

  /**
   * Gets the scope of the listener in the specified context.
   * @return an enumerated value indicating the scope of the listener.
   */
  public Scope getScope() {
    return scope;
  }

  /**
   * Sets the scope of the listener in the specified context.
   * @param scope an enumerated value indicating the scope of the listener.
   */
  public void setScope(final Scope scope) {
    this.scope = ObjectUtil.getDefaultValue(scope, DEFAULT_LISTENER_SCOPE);
  }

  /**
   * Convenience method determining whether this listener definition was declared as a singleton.
   * @return a boolean value indicating if this listener definition was declared as a singleton.
   * @see ListenerDeclaration#getScope()
   * @see ListenerDeclaration#isPrototype()
   */
  public boolean isSingleton() {
    return Scope.SINGLETON.equals(getScope());
  }

  /**
   * Convenience method for getting the Class type of the Listener using the className property.
   * @return a Class object representing the type of the Listener.
   * @throws ClassNotFoundException if the fully-qualified class specified by the class's name using
   * the className property cannot be found in the CLASSPATH!
   * @see ListenerDefinition#getClassName()
   */
  public Class getType() throws ClassNotFoundException {
    return ClassUtil.loadClass(getClassName());
  }

  /**
   * Adds the specified InvocationArgument modeling a constructor argument of this declared listener.
   * @param constructorArgument an InvocationArgument modeling the contructor argument of this declared listener.
   * @return a boolean value indicating whether the specified InvocationArgument was successfully added to the
   * ordered list of constructor arguments for this declared listener.
   */
  public boolean add(final InvocationArgument constructorArgument) {
    return getParameterHandler().add(constructorArgument);
  }

  /**
   * Gets the specified constructor argument for this declared listener at the specified index.
   * @param index an integer value specifying the index of the constructor argument of this declared listener.
   * @return an InvocationArgument modeling the constructor argument of this declared listener at the specified index.
   */
  public InvocationArgument getInvocationArgument(final int index) {
    return getParameterHandler().getInvocationArgument(index);
  }

  /**
   * Gets a list of constructor arguments used to initialize the bean upon instantiation.
   * @return a List of InvocationArguments representing the arguments to one of the constructors of the declared listener.
   */
  public List<InvocationArgument> getInvocationArguments() {
    return getParameterHandler().getInvocationArguments();
  }

  /**
   * Gets the Class types of the arguments used in the constructor invocation of the declared listener.
   * @return an array of Class objects specifying the argument types of the parameters in the constructor of the
   * declared listener.
   */
  public Class[] getInvocationArgumentTypes() {
    return getParameterHandler().getInvocationArgumentTypes();
  }

  /**
   * Gets the Object values used as arguments to the constructor invocation of the declared listener.
   * @return an array of Objects specifying the arguments to the constructor of the declared listener.
   */
  public Object[] getInvocationArgumentValues() {
    return getParameterHandler().getInvocationArgumentValues();
  }

  /**
   * Removes the specified InvocationArgument instance modeling a constructor argument to the declared listener.
   * @param constructorArgument an InvocationArgument instance representing the contructor argument to the bean.
   * @return a boolean value indicating whether the specified InvocationArgument was removed from the
   * list of constructor arguments for this bean.
   */
  public boolean remove(final InvocationArgument constructorArgument) {
    return getParameterHandler().remove(constructorArgument);
  }

  /**
   * Gets a String containing the internal state of this ListenerDeclaration.
   * @return a String value containing the internal state of this ListenerDeclaration.
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{beanDefinition = ");
    buffer.append(BeanDefinitionsUtil.toString(getBeanDefinition()));
    buffer.append(", className = ").append(getClassName());
    buffer.append(", properties = ").append(CollectionUtil.toString(getProperties()));
    buffer.append(", scope = ").append(getScope());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
