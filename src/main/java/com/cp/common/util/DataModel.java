/*
 * DataModel.java (c) 17 July 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.1.7
 */

package com.cp.common.util;

import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import javax.swing.event.ChangeListener;

public interface DataModel {

  /**
   * Registers the specified ChangeListener object with this DataModel to listen for change events to the
   * DateModel's properties.
   * @param listener the ChangeListener registering interest in property changes of this DataModel.
   */
  public void addChangeListener(ChangeListener listener);

  /**
   * Registers the specified PropertyChangeListener with this DataModel.  If the listener object is null,
   * then the method silently returns.
   * @param listener a PropertyChangeListener used to monitor changes on the properties of this DataModel.
   */
  public void addPropertyChangeListener(PropertyChangeListener listener);

  /**
   * Registers the specified PropertyChangeListener with this DataModel for the specified property.  If the
   * listener object is null, then the method silently returns.
   * @param propertyName is the name of the property for which the listener is registered.
   * @param listener a PropertyChangeListener used to monitor changes on the properties of this DataModel.
   */
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

  /**
   * Registers the specified VetoableChangeListener with this DataModel.  If the listener object is null,
   * then the method silently returns.
   * @param listener a VetoableChangeListener used to constrain the changes to the values of the properties
   * of this DataModel.
   */
  public void addVetoableChangeListener(VetoableChangeListener listener);

  /**
   * Registers the specified VetoableChangeListener with this DataModel for the specified property.  If the listener object is null,
   * then the method silently returns.
   * @param propertyName the name of the property for which the listener is registered.
   * @param listener a VetoableChangeListener used to constrain the changes to the values of the properties
   * of this DataModel.
   */
  public void addVetoableChangeListener(String propertyName, VetoableChangeListener listener);

  /**
   * Returns the data value represented by this model.
   * @return an Object value containing the data represented by this data model.
   */
  public Object getValue();

  /**
   * Set the data value to be represented by this model.
   * @param value an Object value specifying the data to be represented by this data model.
   */
  public void setValue(Object value);

  /**
   * Unregisteres the specified ChangeListener from receiving property change events from this DataModel object.
   * @param listener the ChangeListener being unregistered with this DataModel to no longer receive property
   * change events.
   */
  public void removeChangeListener(ChangeListener listener);

  /**
   * Unregisters the specified PropertyChangeListener from this DataModel.  If the listener object is null,
   * then the method silently returns.
   * @param listener a PropertyChangeListener used to monitor changes on the properties of this DataModel.
   */
  public void removePropertyChangeListener(PropertyChangeListener listener);

  /**
   * Unregisters the specified PropertyChangeListener from this DataModel for the specified property.  If the
   * listener object is null, then the method silently returns.
   * @param propertyName the name of the property for which the listener is registered.
   * @param listener a PropertyChangeListener used to monitor changes on the properties of this DataModel.
   */
  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

  /**
   * Unregisters the specified VetoableChangeListener from this DataModel.  If the listener object is null,
   * then the method silently returns.
   * @param listener a VetoableChangeListener used to constrain the changes to the values of the properties
   * on this DataModel.
   */
  public void removeVetoableChangeListener(VetoableChangeListener listener);

  /**
   * Unregisters the specified VetoableChangeListener from this DataModel for the specified property.  If the
   * listener object is null, then the method silently returns.
   * @param propertyName is the name of the property for which the listener is registered.
   * @param listener a VetoableChangeListener used to constrain the changes to the values of the properties
   * on this DataModel.
   */
  public void removeVetoableChangeListener(String propertyName, VetoableChangeListener listener);

}
