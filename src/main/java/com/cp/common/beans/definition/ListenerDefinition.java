/*
 * ListenerDefinition.java (c) 16 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.8.6
 * @see com.cp.common.beans.definition.BeanDefinition
 * @see com.cp.common.lang.Visitable
 * @see java.lang.Comparable
 */

package com.cp.common.beans.definition;

import com.cp.common.lang.Visitable;
import java.util.Set;

public interface ListenerDefinition extends Comparable<ListenerDefinition>, Visitable {

  /**
   * Gets the associated bean definition to which this listener defintion belongs.
   * @return a BeanDefintion object specifying the bean to declare the listener defined by this listener definition.
   */
  public BeanDefinition getBeanDefinition();

  /**
   * Sets the associated bean definition to which this listener defintion belongs.
   * @param beanDefinition a BeanDefintion object specifying the bean to declare the listener defined by this
   * listener definition.
   */
  public void setBeanDefinition(BeanDefinition beanDefinition);

  /**
   * Gets the fully qualified class name of the listener.
   * @return a String value indicating the fully qualified class name of the listener.
   */
  public String getClassName();

  /**
   * Adds a property by name for which the listener will listen for events.
   * @param propertyName the String name of the property.
   * @return a boolean value indicating whether the specified property by name was added to this listener.
   */
  public boolean add(String propertyName);

  /**
   * Determines whether this listener will listen for events from the specified property by name.
   * @param propertyName the String name of the property.
   * @return a boolean value indicating whether this listener will listen for events from the specified property by name.
   */
  public boolean contains(String propertyName);

  /**
   * Gets the Set of properties that this listener is interested in listening for events.
   * @return a Set of String values specifying the names of properties on the bean listened to by this listener.
   */
  public Set<String> getProperties();

  /**
   * Removes the specified property by name for which the listener would have listened for events.
   * @param propertyName the String name of the property.
   * @return a boolean value indicating whether the specified property by name was removed from this listener.
   */
  public boolean remove(String propertyName);

}
