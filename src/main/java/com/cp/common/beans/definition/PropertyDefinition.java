/*
 * PropertyDefinition.java (c) 16 December 2007
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

public interface PropertyDefinition extends Comparable<PropertyDefinition>, Visitable {

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
   * Gets the fully qualifed class name of the property's type.
   * @return a String value indicating the fully qualified class name of the property's type.
   */
  public String getClassName();

  /**
   * Gets the name of this property.
   * @return a String value indicating the property's name.
   */
  public String getName();

}
