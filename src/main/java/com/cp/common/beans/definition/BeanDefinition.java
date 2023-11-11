/*
 * BeanDefinition.java (c) 16 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.7.29
 * @see com.cp.common.beans.definition.ListenerDefinition
 * @see com.cp.common.beans.definition.PropertyDefinition
 * @see com.cp.common.lang.Visitable
 * @see java.lang.Comparable
 */

package com.cp.common.beans.definition;

import com.cp.common.lang.Visitable;
import java.util.Set;

public interface BeanDefinition<L extends ListenerDefinition, P extends PropertyDefinition> extends Comparable<BeanDefinition>, Visitable {

  /**
   * Gets the fully qualifed class name of the bean.
   * @return a String value indicating the fully qualified class name of the bean.
   */
  public String getClassName();

  /**
   * Gets the id uniquely identifying this bean.
   * @return a String value indicating the unique identifier of this bean.
   * @see BeanDefinition#getName() 
   * @see BeanDefinition#getNames()
   */
  public String getId();

  /**
   * Gets the name of this bean.
   * @return a String value indicating the bean's name.
   * @see BeanDefinition#getId()
   * @see BeanDefinition#getNames()
   */
  public String getName();

  /**
   * Sets the name of this bean.
   * @param name a String value indicating the bean's name.
   */
  public void setName(String name);

  /**
   * Gets all the names for this bean as specified in the bean definition.
   * @return a String array containing all the names for this bean in the bean definition.
   * @see BeanDefinition#getId()
   * @see BeanDefinition#getName()
   */
  public String[] getNames();

  /**
   * Adds the specified listener to this bean.
   * @param listener a ListenerDefinition describing the listener.
   * @return a boolean value indicating whether the listener was successfully added to this bean.
   * @see BeanDefinition#remove(ListenerDefinition)
   */
  public boolean add(L listener);

  /**
   * Adds the specified property to this bean.
   * @param property a PropertyDefinition describing the property.
   * @return a boolean value indicating whether the property was successfully added to this bean.
   * @see BeanDefinition#remove(PropertyDefinition)
   */
  public boolean add(P property);

  /**
   * Gets the specified ListenerDefinition from this bean by fully qualified class name of the listener.
   * @param className a String specifying the fully qualified class name of the listener.
   * @return a ListenerDefinition for the fully qualified class name of the listener.
   * @see BeanDefinition#getListeners()
   */
  public L getListenerDefinition(String className);

  /**
   * Gets a Set containing all the listeners on this bean.
   * @return a Set of ListenerDefinitions for all the listeners on this bean.
   * @see BeanDefinition#getListenerDefinition(String)
   */
  public Set<L> getListeners();

  /**
   * Gets the specified PropertyDeclarations for this bean by property name.
   * @param propertyName a String specifying the name of the property.
   * @return a PropertyDeclaration for the specified name of the property.
   * @see BeanDefinition#getProperties()
   */
  public P getPropertyDefinition(String propertyName);

  /**
   * Gets a Set containing all the properties of this bean.
   * @return a Set of PropertyDeclarations for all the properties of this bean.
   * @see BeanDefinition#getPropertyDefinition(String)
   */
  public Set<P> getProperties();

  /**
   * Removes the specified listener from this bean.
   * @param listener a ListenerDefinition describing the listener.
   * @return a boolean value indicating whether the listener was successfully removed from this bean.
   * @see BeanDefinition#add(ListenerDefinition)
   */
  public boolean remove(L listener);

  /**
   * Removes the specified property from this bean.
   * @param property a PropertyDefinition describing the property.
   * @return a boolean value indicating whether the property was successfully removed from this bean.
   * @see BeanDefinition#add(PropertyDefinition)
   */
  public boolean remove(P property);

}
