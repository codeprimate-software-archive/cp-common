/*
 * AbstractBeanFactory.java (c) 18 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.1.8
 * @see com.cp.common.beans.factory.BeanFactory
 * @see com.cp.common.context.config.Config
 * @see com.cp.common.lang.factory.AbstractObjectFactory
 */

package com.cp.common.beans.factory;

import com.cp.common.context.config.Config;
import com.cp.common.lang.factory.AbstractObjectFactory;

public abstract class AbstractBeanFactory extends AbstractObjectFactory implements BeanFactory {

  /**
   * Creates an instance of the AbstractBeanFactory class initialized with the specified Config object.
   * @param config a Config object containing configuration information for this factory.
   */
  public AbstractBeanFactory(final Config config) {
    super(config);
  }

}
