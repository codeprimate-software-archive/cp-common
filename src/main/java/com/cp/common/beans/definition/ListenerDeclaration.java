/*
 * ListenerDeclaration.java (c) 10 January 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.8.6
 * @see com.cp.common.beans.definition.ListenerDefinition
 * @see com.cp.common.beans.definition.Parameterizable
 * @see com.cp.common.beans.definition.Scope
 */

package com.cp.common.beans.definition;

public interface ListenerDeclaration extends ListenerDefinition, Parameterizable {

  /**
   * Gets the Class types of the arguments used in the constructor invocation of the declared listener.
   * @return an array of Class objects specifying the argument types of the parameters in the constructor of the
   * declared listener.
   */
  public Class[] getConstructorArgumentTypes();

  /**
   * Gets the Object values used as arguments to the constructor invocation of the declared listener.
   * @return an array of Objects specifying the arguments to the constructor of the declared listener.
   */
  public Object[] getConstructorArgumentValues();

  /**
   * Convenience method determining whether this listener definition was declared as a prototype.
   * @return a boolean value indicating if this listener definition was declared as a prototype.
   * @see ListenerDeclaration#getScope()
   * @see ListenerDeclaration#isSingleton()
   */
  public boolean isPrototype();

  /**
   * Gets the scope of the listener in the specified context.
   * @return an enumerated value indicating the scope of the listener.
   */
  public Scope getScope();

  /**
   * Sets the scope of the listener in the specified context.
   * @param scope an enumerated value indicating the scope of the listener.
   */
  public void setScope(Scope scope);

  /**
   * Convenience method determining whether this listener definition was declared as a singleton.
   * @return a boolean value indicating if this listener definition was declared as a singleton.
   * @see ListenerDeclaration#getScope()
   * @see ListenerDeclaration#isPrototype()
   */
  public boolean isSingleton();

  /**
   * Convenience method for getting the Class type of the Listener using the className property.
   * @return a Class object representing the type of the Listener.
   * @throws ClassNotFoundException if the fully-qualified class specified by the class's name using
   * the className property cannot be found in the CLASSPATH!
   * @see ListenerDefinition#getClassName()
   */
  public Class getType() throws ClassNotFoundException;

}
