/*
 * Bean.java (c) 10 July 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.1.26
 * @see com.cp.common.beans.AbstractBean
 * @see com.cp.common.beans.BeanHistory
 * @see com.cp.common.enums.Enum
 * @see com.cp.common.lang.Auditable
 * @see com.cp.common.lang.Copyable
 * @see com.cp.common.lang.Identifiable
 * @see com.cp.common.lang.Mutable
 * @see com.cp.common.lang.Resettable
 * @see com.cp.common.lang.Visitable
 * @see java.lang.Cloneable
 * @see java.lang.Comparable
 * @see java.io.Serializable
 */

package com.cp.common.beans;

import com.cp.common.lang.Auditable;
import com.cp.common.lang.Copyable;
import com.cp.common.lang.Identifiable;
import com.cp.common.lang.Mutable;
import com.cp.common.lang.Resettable;
import com.cp.common.lang.Visitable;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.io.Serializable;
import java.util.Set;
import javax.swing.event.ChangeListener;

public interface Bean<T extends Comparable<T>> extends Auditable, Cloneable, Copyable, Identifiable<T>, Mutable, Resettable, Serializable, Visitable {

  public static final String DEFAULT_DATE_FORMAT_PATTERN = "MM/dd/yyyy hh:mm:ss.SS a";

  /**
   * Registers the specified ChangeListener object with this Bean to listen for change events to the
   * Bean's properties.
   * @param listener the ChangeListener registering interest in property changes of this Bean.
   */
  public void addChangeListener(final ChangeListener listener);

  /**
   * Registers an event listener to listen to property changes on this bean.
   * @param listener the PropertyChangeListener registering to listen for property changes on this bean.
   */
  public void addPropertyChangeListener(PropertyChangeListener listener);

  /**
   * Registers an event listener to listen for changes to the specified property on this bean.
   * @param propertyName the name of the property on this bean in which the listener is interested in listening
   * for changes of the property.
   * @param listener the PropertyChangeListener registering to listen for changes on the specified property of
   * this bean.
   */
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

  /**
   * Registers an event listener that vetos changes to properties on this bean that violate constraints imposed on the
   * bean's properties values.
   * @param listener the VetoableChangeListener being registered with this bean.
   */
  public void addVetoableChangeListener(VetoableChangeListener listener);

  /**
   * Registers an event listener that vetos changes to the specified property on this bean that violate constraints
   * imposed on the specified property's value.
   * @param propertyName the String name of the property.
   * @param listener the VetoableChangeListener being registered with this bean for the specified property.
   */
  public void addVetoableChangeListener(String propertyName, VetoableChangeListener listener);

  /**
   * Makes permanent, any changes that were made to the values of the properties on this bean.
   * @see Bean#rollback()
   */
  public void commit();

  /**
   * Gets the bean history to which this bean belongs.
   * @return an BeanHistory object for which this bean belongs.
   */
  public BeanHistory getBeanHistory();

  /**
   * Sets the bean history object containing this bean.
   * @param beanHistory the BeanHistory object containing this bean.
   */
  public void setBeanHistory(BeanHistory beanHistory);

  /**
   * Returns a Set of property names that have been modified on this bean.
   * @return a Set object containing property names of the properties of this bean that have been modified.
   */
  public Set<String> getModifiedProperties();

  /**
   * Determines whether rollback has been called on this bean.
   * @return a boolean value indicating if rollback has been called on this bean.
   * @see Bean#rollback()
   */
  public boolean isRollbackCalled();

  /**
   * Determines whether an RuntimeException will be thrown on Bean setters when rollback has already been called.
   * @return a boolean value indicating whether Bean setters will throw a RuntimeException when rollback
   * has already been called.
   * @see Bean#rollback()
   */
  public boolean isThrowExceptionOnRollback();

  /**
   * Unregisteres the specified ChangeListener from receiving property change events from this Bean.
   * @param listener the ChangeListener being unregistered with this Bean so that it no longer receives
   * property change events.
   */
  public void removeChangeListener(final ChangeListener listener);

  /**
   * Unregisters an event listener listening to property changes on this bean.
   * @param listener the PropertyChangeListener removing itself from notification of property changes on this bean.
   */
  public void removePropertyChangeListener(PropertyChangeListener listener);

  /**
   * Unregisters an event listener listening for changes to the specified property on this bean.
   * @param propertyName the name of the property on this bean in which the listener is interested in listening
   * for changes of the property.
   * @param listener the PropertyChangeListener removing itself from notification of changes for the specified
   * property of this bean.
   */
  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

  /**
   * Unregisters an event listener that vetos changes to properties on this bean violating constraints imposed on the
   * bean's property values.
   * @param listener the VetoableChangeListener being removed from notification of property changes with this bean.
   */
  public void removeVetoableChangeListener(VetoableChangeListener listener);

  /**
   * Unregisters an event listener vetoing changes to the specified property on this bean which violate constraints
   * imposed on the specified property's value.
   * @param propertyName the String name of the property.
   * @param listener the VetoableChangeListener being removed from property change notification with this bean
   * for the specified property.
   */
  public void removeVetoableChangeListener(String propertyName, VetoableChangeListener listener);

  /**
   * Undoes any changes to the properties of this bean.
   * @see Bean#commit()
   */
  public void rollback();

}
