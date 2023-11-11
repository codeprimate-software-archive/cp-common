/*
 * AbstractPropertyDefinition.java (c) 6 January 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.8.6
 * @see com.cp.common.beans.definition.BeanDefinition
 * @see com.cp.common.beans.definition.PropertyDefintion
 */

package com.cp.common.beans.definition;

import com.cp.common.beans.definition.support.BeanDefinitionsUtil;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.Visitor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractPropertyDefinition implements PropertyDefinition {

  protected final Log logger = LogFactory.getLog(getClass());

  private BeanDefinition beanDefinition;

  private final String className;
  private final String propertyName;

  /**
   * Creates an instance of the AbstractPropertyDefinition initialized with the specified property name
   * and fully qualified class name.
   * @param propertyName a String value specifying the name of the property.
   * @param className a String value specifying the fully qualified class name of the property's type.
   */
  public AbstractPropertyDefinition(final String propertyName, final String className) {
    Assert.notEmpty(propertyName, "The name of the property cannot be null or empty!");
    Assert.notEmpty(className, "The fully-qualified class name of the property type cannot be null or empty!");
    this.propertyName = propertyName;
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
   * Gets the fully qualifed class name of the property's type.
   * @return a String value indicating the fully qualified class name of the property's type.
   */
  public final String getClassName() {
    return className;
  }

  /**
   * Gets the name of this property.
   * @return a String value indicating the property's name.
   */
  public final String getName() {
    return propertyName;
  }

  /**
   * Object implementing this interface defines the accept method to allow the Visitor to perform it's operations
   * on this object by calling the visit method.  This Visitor will then determine if the operation should be
   * applied to this type of object depending on it's function.
   * @param visitor the Visitor with the operation to perform on this object.
   */
  public void accept(final Visitor visitor) {
    visitor.visit(this);
  }

  /**
   * Compares this PropertyDefinition to the specified PropertyDefinition argument for logical ordering.
   * @param propertyDefinition the PropertyDefinition argument being compared with this PropertyDefinition.
   * @return a integer value indicating whether the PropertyDefinition argument comes before, after
   * or is logically equivalent to this PropertyDefinition.
   */
  public int compareTo(final PropertyDefinition propertyDefinition) {
    return getName().compareTo(propertyDefinition.getName());
  }

  /**
   * Determines whether the specified object is equal to this PropertyDeclaration.
   * @param obj the Object used in the equality comparision with this PropertyDeclaration.
   * @return a boolean value indicating whether the specified Object and this PropertyDeclaration are equal.
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof PropertyDefinition)) {
      return false;
    }

    final PropertyDefinition that = (PropertyDefinition) obj;

    return ObjectUtil.equals(getName(), that.getName());
  }

  /**
   * Computes the hash value of this PropertyDeclaration.
   * @return a integer value of the computed hash of this PropertyDeclaration.
   */
  @Override
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getName());
    return hashValue;
  }

  /**
   * Gets a String containing the internal state of this PropertyDefinition.
   * @return a String value containing the internal state of this PropertyDefinition.
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{beanDefinition = ");
    buffer.append(BeanDefinitionsUtil.toString(getBeanDefinition()));
    buffer.append(", className = ").append(getClassName());
    buffer.append(", name = ").append(getName());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
