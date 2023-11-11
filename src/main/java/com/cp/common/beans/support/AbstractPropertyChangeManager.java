/*
 * AbstractPropertyChangeManager.java (c) 17 June 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.26
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.beans.support.PropertyChangeManager
 */

package com.cp.common.beans.support;

import com.cp.common.beans.Bean;
import com.cp.common.lang.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractPropertyChangeManager implements PropertyChangeManager {

  protected final Log logger = LogFactory.getLog(getClass());

  private final Bean bean;

  /**
   * Creates an instance of the AbstractPropertyChangeManager class initilized with the specified bean.
   * @param bean the bean object who's property events are managed by this PropertyChangeManager.
   */
  public AbstractPropertyChangeManager(final Bean bean) {
    Assert.notNull(bean, "The bean managed by this PropertyChangeManager cannot be null!");
    this.bean = bean;
  }

  /**
   * Gets the bean Object who's properties are managed by this PropertyChangeManager.
   * @return a Object referencing the bean that this PropertyChangeManager manages.
   */
  protected final Bean getBean() {
    return bean;
  }

}
