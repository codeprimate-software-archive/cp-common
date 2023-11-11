/*
 * AbstractListenerDefinition.java (c) 17 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.8.6
 * @see com.cp.common.beans.definition.BeanDefinition
 * @see com.cp.common.beans.definition.ListenerDefinition
 */

package com.cp.common.beans.definition;

import com.cp.common.beans.definition.support.BeanDefinitionsUtil;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.CollectionUtil;
import com.cp.common.util.Visitor;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractListenerDefinition implements ListenerDefinition {

  protected final Log logger = LogFactory.getLog(getClass());

  private BeanDefinition beanDefinition;

  private final Set<String> propertyNameSet = new TreeSet<String>();

  private final String className;

  /**
   * Creates an instance of the AbstractListenerDefinition class initialized with the fully qualified class name
   * of the listener.
   * @param className a String value specifying the fully qualified class name of the listener.
   */
  public AbstractListenerDefinition(final String className) {
    Assert.notEmpty(className, "The fully-qualified class name of the listener cannot be null or empty!");
    this.className = className;
  }

  /**
   * Gets the associated bean definition to which this listener defintion belongs.
   * @return a BeanDefintion object specifying the bean to declare the listener defined by this listener definition.
   */
  public BeanDefinition getBeanDefinition() {
    return beanDefinition;
  }

  /**
   * Sets the associated bean definition to which this listener defintion belongs.
   * @param beanDefinition a BeanDefintion object specifying the bean to declare the listener defined by this
   * listener definition.
   */
  public void setBeanDefinition(final BeanDefinition beanDefinition) {
    this.beanDefinition = beanDefinition;
  }

  /**
   * Gets the fully qualified class name of the listener.
   * @return a String value indicating the fully qualified class name of the listener.
   */
  public final String getClassName() {
    return className;
  }

  /**
   * Object implementing this interface defines the accept method to allow the Visitor to perform it's operations
   * on this object by calling the visit method.  This Visitor will then determine if the operation should be
   * applied to this type of object depending on it's function.
   * @param visitor the Visitor with the operation to perform on this object.
   */
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /**
   * Adds a property by name for which the listener will listen for events.
   * @param propertyName the String name of the property.
   * @return a boolean value indicating whether the specified property by name was added to this listener.
   */
  public boolean add(final String propertyName) {
    return (StringUtil.isNotEmpty(propertyName) && propertyNameSet.add(propertyName));
  }

  /**
   * Compares this ListenerDefinition with the specified listener to determine order.
   * @param listener the ListenerDefinition argument compared with this ListenerDefintion.
   * @return an integer value indicating whether this listener comes before, after
   * or is logically equivalent to the specified listener argument.
   */
  public int compareTo(final ListenerDefinition listener) {
    return getClassName().compareTo(listener.getClassName());
  }

  /**
   * Determines whether this listener will listen for events from the specified property by name.
   * @param propertyName the String name of the property.
   * @return a boolean value indicating whether this listener will listen for events from the specified property by name.
   */
  public boolean contains(final String propertyName) {
    return (StringUtil.isNotEmpty(propertyName) && propertyNameSet.contains(propertyName));
  }

  /**
   * Gets the Set of properties that this listener is interested in listening for events.
   * @return a Set of String values specifying the names of properties on the bean listened to by this listener.
   */
  public Set<String> getProperties() {
    return Collections.unmodifiableSet(propertyNameSet);
  }

  /**
   * Removes the specified property by name for which the listener would have listened for events.
   * @param propertyName the String name of the property.
   * @return a boolean value indicating whether the specified property by name was removed from this listener.
   */
  public boolean remove(final String propertyName) {
    return propertyNameSet.remove(propertyName);
  }

  /**
   * Determines whether the specified object is equal to this ListenerDefinition.
   * @param obj the Object used in the equality comparision with this ListenerDefinition.
   * @return a boolean value indicating whether the specified Object and this ListenerDefinition are equal.
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof ListenerDefinition)) {
      return false;
    }

    final ListenerDefinition that = (ListenerDefinition) obj;

    return ObjectUtil.equals(getClassName(), that.getClassName());
  }

  /**
   * Computes the hash value of this ListenerDefinition.
   * @return a integer value of the computed hash of this ListenerDefinition.
   */
  @Override
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getClassName());
    return hashValue;
  }

  /**
   * Gets a String containing the internal state of this ListenerDefinition.
   * @return a String value containing the internal state of this ListenerDefinition.
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{beanDefinition = ");
    buffer.append(BeanDefinitionsUtil.toString(getBeanDefinition()));
    buffer.append(", className = ").append(getClassName());
    buffer.append(", properties = ").append(CollectionUtil.toString(getProperties()));
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
