/*
 * PropertyChangeManager.java (c) 14 January 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.11
 * @see com.cp.common.beans.Bean
 * @see java.beans.PropertyChangeListener
 * @see java.io.Serializable
 */

package com.cp.common.beans.support;

import com.cp.common.beans.Bean;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Set;

public interface PropertyChangeManager extends PropertyChangeListener, Serializable {

  /**
   * Determines whether the specified property is being tracked and managed by this PropertyChangeManager.
   * @param propertyName a String value specifying the name of the property determined for containment.
   * @return a boolean value indicating whether this PropertyChangeManager is tracking and managing the
   * property identified by name.
   */
  public boolean contains(String propertyName);

  /**
   * Performs a commit on this PropertyChangeManager as defined by this PropertyChangeManager, which may be nothing
   * more than clearing the properties that have changed on the Bean for which this PropertyChangeManager is managing.
   * @param bean the Bean who's properties have changed as well as the Bean that this PropertyChangeManager is
   * listening to.
   * @return a boolean value of true if the commit operation is successful.
   */
  public boolean doCommit(Bean bean);

  /**
   * Performs a rollback on this PropertyChangeManager as defined by this PropertyChangeManager, which may be nothing
   * more than clearing the properties that have changed on the Bean for which this PropertyChangeManager is managing.
   * @param bean the Bean who's properties have changed as well as the Bean that this PropertyChangeManager is
   * listening to.
   * @return a boolean value of true if the rollback operation is successful.
   */
  public boolean doRollback(Bean bean);

  /**
   * Returns the set of modified properties of the Bean by name managed by this PropertyChangeManager.
   * @return a Set of String values specifying the names of the changed Bean properties.
   */
  public Set<String> getModifiedProperties();

  /**
   * Determines whether the Bean has any modified properties.
   * @return a boolean value indicating if the Bean has any modified properties.
   */
  public boolean hasModifiedProperties();

}
