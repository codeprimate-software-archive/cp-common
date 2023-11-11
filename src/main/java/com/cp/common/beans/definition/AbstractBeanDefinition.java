/*
 * AbstractBeanDefinition.java (c) 17 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.7.31
 * @see com.cp.common.beans.definition.BeanDefinition
 * @see com.cp.common.beans.definition.ListenerDefinition
 * @see com.cp.common.beans.definition.PropertyDefinition
 * @see com.cp.common.beans.definition.Scope
 */

package com.cp.common.beans.definition;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.ArrayUtil;
import com.cp.common.util.ComparableComparator;
import com.cp.common.util.Visitor;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractBeanDefinition<L extends ListenerDefinition, P extends PropertyDefinition> implements BeanDefinition<L, P> {

  protected static final String DEFAULT_NAME_DELIMITER = ",";

  protected final Log logger = LogFactory.getLog(getClass());

  private final Map<String, P> propertyDefinitionMap = new TreeMap<String, P>(ComparableComparator.<String>getInstance());

  private final Set<L> listenerDefinitionSet = new TreeSet<L>(CustomListenerDefinitionComparator.INSTANCE);

  private final String className;
  private final String id;
  private String name;

  private String[] names;

  /**
   * Creates an instance of the AbstractBeanDefinition class initialized to the specified id and class name.
   * @param id a String value indication the unique identifier of the bean definition.
   * @param className the fully qualified class name of the bean.
   */
  public AbstractBeanDefinition(final String id, final String className) {
    Assert.notEmpty(id, "The id of the bean definition cannot be null or empty!");
    Assert.notEmpty(className, "The fully-qualified class name of the bean definition cannot be null or empty!");
    this.id = id;
    this.className = className;
  }

  /**
   * Gets the fully qualifed class name of the bean.
   * @return a String value indicating the fully qualified class name of the bean.
   */
  public final String getClassName() {
    return className;
  }

  /**
   * Gets the id uniquely identifying this bean.
   * @return a String value indicating the unique identifier of this bean.
   * @see BeanDefinition#getName()
   * @see BeanDefinition#getNames()
   */
  public final String getId() {
    return id;
  }

  /**
   * Gets the name of this bean.
   * @return a String value indicating the bean's name.
   * @see BeanDefinition#getId()
   * @see BeanDefinition#getNames()
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this bean.
   * @param name a String value indicating the bean's name.
   * @see AbstractBeanDefinition#getNames()
   */
  public void setName(final String name) {
    this.name = name;

    if (ObjectUtil.isNotNull(this.name)) {
      this.names = this.name.split(DEFAULT_NAME_DELIMITER);

      try {
        int index = 0;

        for (final String aname : this.names) {
          Assert.notEmpty(aname, "The name cannot be null or empty!");
          this.names[index++] = StringUtil.trim(aname);
        }
      }
      catch (IllegalArgumentException e) {
        this.name = null;
        this.names = null;
        throw e;
      }
    }
    else {
      this.names = null;
    }
  }

  /**
   * Gets all the names for this bean as specified in the bean definition.
   * @return a String array containing all the names for this bean in the bean definition.
   * @see BeanDefinition#getId()
   * @see BeanDefinition#getName()
   * @see BeanDefinition#setName(String)
   */
  public String[] getNames() {
    return ObjectUtil.<String[]>getDefaultValue(names, new String[0]);
  }

  /**
   * Object implementing this interface defines the accept method to allow the Visitor to perform it's operations
   * on this object by calling the visit method.  This Visitor will then determine if the operation should be
   * applied to this type of object depending on it's function.
   * @param visitor the Visitor with the operation to perform on this object.
   */
  public void accept(final Visitor visitor) {
    visitor.visit(this);

    for (final P propertyDefinition : propertyDefinitionMap.values()) {
      propertyDefinition.accept(visitor);
    }

    for (final L listenerDefinition : listenerDefinitionSet) {
      listenerDefinition.accept(visitor);
    }
  }

  /**
   * Adds the specified listener to this bean.
   * @param listener a ListenerDefinition describing the listener.
   * @return a boolean value indicating whether the listener was successfully added to this bean.
   * @see BeanDefinition#remove(ListenerDefinition)
   */
  public boolean add(final L listener) {
    Assert.notNull(listener, "The listener definition to add to this bean definition cannot be null!");
    Assert.state(listenerDefinitionSet.add(listener), "The listener class (" + listener.getClassName()
      + ") has already been registered on this bean definition (" + getId() + ")!");
    listener.setBeanDefinition(this);
    return true;
  }

  /**
   * Adds the specified property to this bean.
   * @param property a PropertyDefinition describing the property.
   * @return a boolean value indicating whether the property was successfully added to this bean.
   * @see BeanDefinition#remove(PropertyDefinition)
   */
  public boolean add(final P property) {
    Assert.notNull(property, "The property definition being added to this bean definition cannot be null!");
    Assert.state(!propertyDefinitionMap.containsKey(property.getName()), "The property (" + property.getName()
      + ") has already been defined on this bean definition (" + getId() + ")!");
    propertyDefinitionMap.put(property.getName(), property);
    property.setBeanDefinition(this);
    return true;
  }

  /**
   * Compares this bean definition to the specified bean definition for logical ordering.
   * @param beanDefinition the BeanDefinition object used in the relation comparison with this bean definition.
   * @return a numeric value indicating whether this bean definition is less than, equal to, or greater than the
   * specified bean definition.
   */
  public int compareTo(final BeanDefinition beanDefinition) {
    return getId().compareTo(beanDefinition.getId());
  }

  /**
   * Gets the specified ListenerDefinition from this bean by fully qualified class name of the listener.
   * @param className a String specifying the fully qualified class name of the listener.
   * @return a ListenerDefinition for the fully qualified class name of the listener.
   * @see BeanDefinition#getListeners()
   */
  public L getListenerDefinition(final String className) {
    for (final L listenerDefinition : listenerDefinitionSet) {
      if (ObjectUtil.equals(listenerDefinition.getClassName(), className)) {
        return listenerDefinition;
      }
    }

    return null;
  }

  /**
   * Gets a Set containing all the listeners on this bean.
   * @return a Set of ListenerDefinitions for all the listeners on this bean.
   * @see BeanDefinition#getListenerDefinition(String)
   */
  public Set<L> getListeners() {
    return Collections.unmodifiableSet(listenerDefinitionSet);
  }

  /**
   * Gets the specified PropertyDeclarations for this bean by property name.
   * @param propertyName a String specifying the name of the property.
   * @return a PropertyDeclaration for the specified name of the property.
   * @see BeanDefinition#getProperties()
   */
  public P getPropertyDefinition(final String propertyName) {
    return propertyDefinitionMap.get(propertyName);
  }

  /**
   * Gets a Set containing all the properties of this bean.
   * @return a Set of PropertyDeclarations for all the properties of this bean.
   * @see BeanDefinition#getPropertyDefinition(String)
   */
  public Set<P> getProperties() {
    return Collections.unmodifiableSet(new HashSet<P>(propertyDefinitionMap.values()));
  }

  /**
   * Removes the specified listener from this bean.
   * @param listener a ListenerDefinition describing the listener.
   * @return a boolean value indicating whether the listener was successfully removed from this bean.
   * @see BeanDefinition#add(ListenerDefinition)
   */
  public boolean remove(final L listener) {
    final boolean removed = listenerDefinitionSet.remove(listener);

    if (removed) {
      listener.setBeanDefinition(null);
    }

    return removed;
  }

  /**
   * Removes the specified property from this bean.
   * @param property a PropertyDefinition describing the property.
   * @return a boolean value indicating whether the property was successfully removed from this bean.
   * @see BeanDefinition#add(PropertyDefinition)
   */
  public boolean remove(final P property) {
    final boolean removed = ObjectUtil.isNotNull(propertyDefinitionMap.remove(
      ObjectUtil.isNull(property) ? null : property.getName()));

    if (removed) {
      property.setBeanDefinition(null);
    }

    return removed;
  }

  /**
   * Determines whether the specified object is equal to this BeanDefinition.
   * @param obj the Object used in the equality comparision with this BeanDefinition.
   * @return a boolean value indicating whether the specified Object and this BeanDefinition are equal.
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof BeanDefinition)) {
      return false;
    }

    final BeanDefinition that = (BeanDefinition) obj;

    return ObjectUtil.equals(getId(), that.getId());
  }

  /**
   * Computes the hash value of this BeanDefinition.
   * @return a integer value of the computed hash of this BeanDefinition.
   */
  @Override
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getId());
    return hashValue;
  }

  /**
   * Gets a String description of the internal state of this bean definition.
   * @return a String description of the internal state of this bean definition.
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{className = ");
    buffer.append(getClassName());
    buffer.append(", id = ").append(getId());
    buffer.append(", name = ").append(getName());
    buffer.append(", names = ").append(ArrayUtil.toString(getNames()));
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

  protected static final class CustomListenerDefinitionComparator implements Comparator<ListenerDefinition> {

    public static final CustomListenerDefinitionComparator INSTANCE = new CustomListenerDefinitionComparator();

    public int compare(final ListenerDefinition listener0, final ListenerDefinition listener1) {
      return ComparableComparator.<ListenerDefinition>getInstance().compare(listener0, listener1);
    }
  }

}
