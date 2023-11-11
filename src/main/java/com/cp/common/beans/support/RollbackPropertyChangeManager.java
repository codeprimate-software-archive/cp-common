/*
 * RollbackPropertyChangeManager.java (c) 14 January 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.29
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.beans.util.BeanUtil
 * @see com.cp.common.beans.support.AbstractPropertyChangeManager
 * @see java.beans.PropertyChangeEvent
 */

package com.cp.common.beans.support;

import com.cp.common.beans.Bean;
import com.cp.common.beans.util.BeanUtil;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class RollbackPropertyChangeManager extends AbstractPropertyChangeManager {

  private final Map<String, Object> propertyChangeMap = new TreeMap<String, Object>();

  /**
   * Creates an instance of the RollbackPropertyChangeManager class initialized with the specified bean!
   * @param bean the Bean object who's properties are managed by this PropertyChangeManager.
   */
  public RollbackPropertyChangeManager(final Bean bean) {
    super(bean);
  }

  /**
   * Determines whether the specified property is being tracked and managed by this PropertyChangeManager.
   * @param propertyName a String value specifying the name of the property determined for containment.
   * @return a boolean value indicating whether this PropertyChangeManager is tracking and managing the
   * property identified by name.
   */
  public boolean contains(final String propertyName) {
    return propertyChangeMap.containsKey(propertyName);
  }

  /**
   * Performs a commit operation as defined by this PropertyChangeManager.  The RollbackPropertyChangeManager on commit
   * will clear the cache of properties that have changed on the bean.
   * @param bean the bean object this PropertyChangeManager is listening for property changes to.
   * @return a boolean value of true if the commit operation was successful.
   * @see RollbackPropertyChangeManager#doRollback(com.cp.common.beans.Bean)
   */
  public boolean doCommit(final Bean bean) {
    Assert.same(getBean(), bean, "The bean managed by this PropertyChangeManager is not the same as the bean argument to the doCommit call!");
    propertyChangeMap.clear();
    return true;
  }

  /**
   * Performs a rollback operation as defined by this PropertyChangeManager.  The RollbackPropertyChangeManager on
   * rollback revert the changed properties of the bean to their original value and clear the cache of properties
   * that have changed on the bean.
   * @param bean the bean object this PropertyChangeManager is listening for property changes to.
   * @return a boolean value of true if the rollback operation was successful.
   * @see RollbackPropertyChangeManager#doCommit(com.cp.common.beans.Bean)
   */
  public boolean doRollback(final Bean bean) {
    Assert.same(getBean(), bean, "The bean managed by this PropertyChangeManager is not the same as the bean argument to the doRollback call!");

    try {
      if (hasModifiedProperties()) {
        BeanUtil.setPropertyValues(bean, propertyChangeMap);
        propertyChangeMap.clear();
      }

      return true;
    }
    catch (Exception e) {
      logger.error("Rollback failed!  The bean (" + bean.getClass().getName() + ") is in an inconsistent state!", e);
      throw new IllegalStateException("Rollback failed!  The bean (" + bean.getClass().getName()
        + ") is in an inconsistent state!", e);
    }
  }

  /**
   * Returns the set of modified properties by name on the Bean.
   * @return a Set of String values specifying the names of the changed bean properties.
   */
  public Set<String> getModifiedProperties() {
    return Collections.unmodifiableSet(propertyChangeMap.keySet());
  }

  /**
   * Determines whether the bean has any modified properties.
   * @return a boolean value indicating if the bean has any modified properties.
   */
  public boolean hasModifiedProperties() {
    return !propertyChangeMap.isEmpty();
  }

  /**
   * Handler method for the property change event.
   * @param event the PropertyChangeEvent object describing the property change on the Bean.
   */
  public void propertyChange(final PropertyChangeEvent event) {
    Assert.same(getBean(), event.getSource(), "The event source is not the same object as the bean managed by this PropertyChangeManager!");

    if (!contains(event.getPropertyName()) && !(ObjectUtil.equals(event.getOldValue(), event.getNewValue()))) {
      propertyChangeMap.put(event.getPropertyName(), event.getOldValue());
    }
  }

}
