/*
 * DefaultPropertyChangeManager.java (c) 14 January 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.29
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.beans.support.AbstractPropertyChangeManager
 * @see java.beans.PropertyChangeEvent
 */

package com.cp.common.beans.support;

import com.cp.common.beans.Bean;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class DefaultPropertyChangeManager extends AbstractPropertyChangeManager {

  private final Set<String> propertyNameSet = new TreeSet<String>();

  /**
   * Creates an instance of the DefaultPropertyChangeManager class initialized with the specified bean object!
   * @param bean the Object who's properties are managed by this PropertyChangeManager.
   */
  public DefaultPropertyChangeManager(final Bean bean) {
    super(bean);
  }

  /**
   * Determines whether the specified property is being tracked and managed by this PropertyChangeManager.
   * @param propertyName a String value specifying the name of the property determined for containment.
   * @return a boolean value indicating whether this PropertyChangeManager is tracking and managing the
   * property identified by name.
   */
  public boolean contains(final String propertyName) {
    return propertyNameSet.contains(propertyName);
  }

  /**
   * Performs a commit on this PropertyChangeManager as defined by this PropertyChangeManager, which may be nothing
   * more than clearing the properties that have changed on the Bean for which this PropertyChangeManager is managing.
   * @param bean the Bean who's properties have changed as well as the Bean that this PropertyChangeManager is
   * listening to.
   * @return a boolean value of true if the commit operation is successful.
   */
  public boolean doCommit(final Bean bean) {
    Assert.same(getBean(), bean, "The bean managed by this PropertyChangeManager is not the same as the bean argument to the doCommit call!");
    propertyNameSet.clear();
    return true;
  }

  /**
   * Performs a rollback on this PropertyChangeManager as defined by this PropertyChangeManager, which may be nothing
   * more than clearing the properties that have changed on the Bean for which this PropertyChangeManager is managing.
   * @param bean the Bean who's properties have changed as well as the Bean that this PropertyChangeManager is
   * listening to.
   * @return a boolean value of true if the rollback operation is successful.
   */
  public boolean doRollback(final Bean bean) {
    Assert.same(getBean(), bean, "The bean managed by this PropertyChangeManager is not the same as the bean argument to the doRollback call!");
    propertyNameSet.clear();
    return true;
  }

  /**
   * Returns the set of modified properties of the Bean by name managed by this PropertyChangeManager.
   * @return a Set of String values specifying the names of the changed Bean properties.
   */
  public Set<String> getModifiedProperties() {
    return Collections.unmodifiableSet(propertyNameSet);
  }

  /**
   * Determines whether the Bean has any modified properties.
   * @return a boolean value indicating if the Bean has any modified properties.
   */
  public boolean hasModifiedProperties() {
    return !propertyNameSet.isEmpty();
  }

  /**
   * Handler method for the property change event.
   * @param event the PropertyChangeEvent object describing the property change on the Bean.
   */
  public void propertyChange(final PropertyChangeEvent event) {
    Assert.same(getBean(), event.getSource(), "The event source is not the same object as the bean managed by this PropertyChangeManager!");

    if (!contains(event.getPropertyName()) && !ObjectUtil.equals(event.getOldValue(), event.getNewValue())) {
      propertyNameSet.add(event.getPropertyName());
    }
  }

}
