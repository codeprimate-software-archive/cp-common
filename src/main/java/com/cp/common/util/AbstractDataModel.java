/*
 * AbstractDataModel (c) 12 September 2004
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.8.30
 * @see com.cp.common.beans.CosntraintViolationException
 * @see com.cp.common.util.DataModel
 * @see java.beans.PropertyChangeEvent
 * @see java.beans.PropertyChangeListener
 * @see java.beans.PropertyChangeSupport
 * @see java.beans.VetoableChangeListener
 * @see java.beans.VetoableChangeSupport
 * @see javax.swing.event.ChangeListener
 * @see javax.swing.event.EventListenerList
 */

package com.cp.common.util;

import com.cp.common.beans.ConstraintViolationException;
import com.cp.common.lang.ObjectUtil;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractDataModel implements DataModel {

  protected final Log logger = LogFactory.getLog(getClass());

  private EventListenerList changeListenerList = new EventListenerList();

  private PropertyChangeSupport propertyChangeListeners = new PropertyChangeSupport(this);

  private VetoableChangeSupport vetoableChangeListeners = new VetoableChangeSupport(this);

  /**
   * Registers the specified ChangeListener object with this DataModel to listen for change events to the
   * DateModel's properties.
   * @param listener the ChangeListener registering interest in property changes of this DataModel.
   */
  public void addChangeListener(final ChangeListener listener) {
    if (ObjectUtil.isNotNull(listener)) {
      if (logger.isDebugEnabled()) {
        logger.debug("adding ChangeListener (" + listener.getClass() + ")");
      }
      changeListenerList.add(ChangeListener.class, listener);
    }
  }

  /**
   * Gets the ChangeEvent implementation.
   * @return a implementation of the ChangeEvent class.
   */
  protected ChangeEvent getChangeEventImpl() {
    return new ChangeEvent(this);
  }

  /**
   * Notifies interested ChangeListeners that a change occurred to one of the properties of this DataModel.
   */
  protected void fireChangeEvent() {
    fireChangeEvent(getChangeEventImpl());
  }

  /**
   * Notifies interested ChangeListeners that a change occurred to one of the properties of this DataModel.
   * @param event the ChangeEvent object capturing the change event details sent to the registered ChangeListeners.
   */
  protected void fireChangeEvent(ChangeEvent event) {
    if (ObjectUtil.isNull(event)) {
      event = getChangeEventImpl();
    }

    final Object[] listenerList = this.changeListenerList.getListenerList();

    for (int index = listenerList.length - 2; index >= 0; index--) {
      if (listenerList[index] == ChangeListener.class) {
        ((ChangeListener) listenerList[index + 1]).stateChanged(event);
      }
    }
  }

  /**
   * Unregisteres the specified ChangeListener from receiving property change events from this DataModel object.
   * @param listener the ChangeListener being unregistered with this DataModel to no longer receive property
   * change events.
   */
  public void removeChangeListener(final ChangeListener listener) {
    if (ObjectUtil.isNotNull(listener)) {
      if (logger.isDebugEnabled()) {
        logger.debug("removing ChangeListener (" + listener.getClass() + ")");
      }
      changeListenerList.remove(ChangeListener.class, listener);
    }
  }

  /**
   * Registers the specified PropertyChangeListener with this DataModel.  If the listener object is null,
   * then the method silently returns.
   * @param listener a PropertyChangeListener used to monitor changes on the properties of this DataModel.
   */
  public void addPropertyChangeListener(final PropertyChangeListener listener) {
    if (ObjectUtil.isNotNull(listener)) {
      if (logger.isDebugEnabled()) {
        logger.debug("adding PropertyChangeListener (" + listener.getClass() + ")");
      }
      propertyChangeListeners.addPropertyChangeListener(listener);
    }
  }

  /**
   * Registers the specified PropertyChangeListener with this DataModel for the specified property.  If the
   * listener object is null, then the method silently returns.
   * @param propertyName is the name of the property for which the listener is registered.
   * @param listener a PropertyChangeListener used to monitor changes on the properties of this DataModel.
   */
  public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
    if (ObjectUtil.isNotNull(listener)) {
      if (logger.isDebugEnabled()) {
        logger.debug("adding PropertyChangeListener (" + listener.getClass() + ") for property (" + propertyName + ")");
      }
      propertyChangeListeners.addPropertyChangeListener(propertyName, listener);
    }
  }

  /**
   * Notifies PropertyChangeListeners of the change to the specified property value recorded in the instance of the
   * PropertyChangeEvent.
   * @param event a PropertyChangeEvent noting the new and old values of the property that changed on this DataModel.
   */
  protected void firePropertyChange(final PropertyChangeEvent event) {
    propertyChangeListeners.firePropertyChange(event);
  }

  /**
   * Unregisters the specified PropertyChangeListener from this DataModel.  If the listener object is null,
   * then the method silently returns.
   * @param listener a PropertyChangeListener used to monitor changes on the properties of this DataModel.
   */
  public void removePropertyChangeListener(final PropertyChangeListener listener) {
    if (ObjectUtil.isNotNull(listener)) {
      if (logger.isDebugEnabled()) {
        logger.debug("removing PropertyChangeListener (" + listener.getClass() + ")");
      }
      propertyChangeListeners.removePropertyChangeListener(listener);
    }
  }

  /**
   * Unregisters the specified PropertyChangeListener from this DataModel for the specified property.  If the
   * listener object is null, then the method silently returns.
   * @param propertyName the name of the property for which the listener is registered.
   * @param listener a PropertyChangeListener used to monitor changes on the properties of this DataModel.
   */
  public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
    if (ObjectUtil.isNotNull(listener)) {
      if (logger.isDebugEnabled()) {
        logger.debug("removing PropertyChangeListener (" + listener.getClass() + ") for property (" + propertyName + ")");
      }
      propertyChangeListeners.removePropertyChangeListener(propertyName, listener);
    }
  }

  /**
   * Registers the specified VetoableChangeListener with this DataModel.  If the listener object is null,
   * then the method silently returns.
   * @param listener a VetoableChangeListener used to constrain the changes to the values of the properties
   * of this DataModel.
   */
  public void addVetoableChangeListener(final VetoableChangeListener listener) {
    if (ObjectUtil.isNotNull(listener)) {
      if (logger.isDebugEnabled()) {
        logger.debug("adding VetoableChangeListener (" + listener.getClass() + ")");
      }
      vetoableChangeListeners.addVetoableChangeListener(listener);
    }
  }

  /**
   * Registers the specified VetoableChangeListener with this DataModel for the specified property.  If the listener object is null,
   * then the method silently returns.
   * @param propertyName the name of the property for which the listener is registered.
   * @param listener a VetoableChangeListener used to constrain the changes to the values of the properties
   * of this DataModel.
   */
  public void addVetoableChangeListener(final String propertyName, final VetoableChangeListener listener) {
    if (ObjectUtil.isNotNull(listener)) {
      if (logger.isDebugEnabled()) {
        logger.debug("adding VetoableChangeListener (" + listener.getClass() + ") for property (" + propertyName + ")");
      }
      vetoableChangeListeners.addVetoableChangeListener(propertyName, listener);
    }
  }

  /**
   * Notifies VetobleChangeListeners that the specified property of this DataModel, recorded in the specified
   * PropertyChangeEvent, is about to change.  This gives an registered VetoableChangeListener a chance to veto, or
   * reject, the change based on constraints to the property's value.
   * @param event a PropertyChangeEvent noting the new and old values of the property that changed on the source object.
   * @throws java.beans.PropertyVetoException if the changed to the specified property value on the source object violates any
   * constraints imposed by the VetoableChangeListeners registered with this object.
   */
  protected void fireVetoableChange(final PropertyChangeEvent event) throws PropertyVetoException {
    vetoableChangeListeners.fireVetoableChange(event);
  }

  /**
   * Unregisters the specified VetoableChangeListener from this DataModel.  If the listener object is null,
   * then the method silently returns.
   * @param listener a VetoableChangeListener used to constrain the changes to the values of the properties
   * on this DataModel.
   */
  public void removeVetoableChangeListener(final VetoableChangeListener listener) {
    if (ObjectUtil.isNotNull(listener)) {
      if (logger.isDebugEnabled()) {
        logger.debug("removing VetoableChangeListener (" + listener + ")");
      }
      vetoableChangeListeners.removeVetoableChangeListener(listener);
    }
  }

  /**
   * Unregisters the specified VetoableChangeListener from this DataModel for the specified property.  If the
   * listener object is null, then the method silently returns.
   * @param propertyName is the name of the property for which the listener is registered.
   * @param listener a VetoableChangeListener used to constrain the changes to the values of the properties
   * on this DataModel.
   */
  public void removeVetoableChangeListener(final String propertyName, final VetoableChangeListener listener) {
    if (ObjectUtil.isNotNull(listener)) {
      if (logger.isDebugEnabled()) {
        logger.debug("removing VetoableChangeListener (" + listener.getClass() + ") for property (" + propertyName + ")");
      }
      vetoableChangeListeners.removeVetoableChangeListener(propertyName, listener);
    }
  }

  /**
   * Convenience method to call the fireVetoableChange, firePropertyChange and fireChangeEvent methods on this DataModel.
   * @param propertyName a String value specifying the name of the property that changed.
   * @param oldValue an Object value indicating the original value of the property that changed.
   * @param newValue an Object value specifying the new value of the property that changed.
   * @param callbackHandler a StateChangeCallbackHandler object used to change the state of the DataModel.
   * @throws java.beans.PropertyVetoException if the property change is constrained by any of the VetoableChangeListeners
   * registered with this DataModel.
   * @see AbstractDataModel#notifyListeners(java.beans.PropertyChangeEvent, com.cp.common.util.AbstractDataModel.StateChangeCallbackHandler)
   * @see AbstractDataModel#processChange(String, Object, Object, com.cp.common.util.AbstractDataModel.StateChangeCallbackHandler)
   */
  protected void notifyListener(final String propertyName, final Object oldValue, final Object newValue, final StateChangeCallbackHandler callbackHandler) 
    throws PropertyVetoException
  {
    notifyListeners(new PropertyChangeEvent(this, propertyName, oldValue, newValue), callbackHandler);
  }

  /**
   * Convenience method to call the fireVetoableChange, firePropertyChange and fireChangeEvent methods on this DataModel.
   * @param event the PropertyChangeEvent object used to notify listeners of the property change event.
   * @param callbackHandler a StateChangeCallbackHandler object used to change the state of the DataModel.
   * @throws java.beans.PropertyVetoException if the property change is constrained by any of the VetoableChangeListeners
   * registered with this DataModel.
   * @see AbstractDataModel#notifyListener(String, Object, Object, com.cp.common.util.AbstractDataModel.StateChangeCallbackHandler)
   * @see AbstractDataModel#processChange(String, Object, Object, com.cp.common.util.AbstractDataModel.StateChangeCallbackHandler)
   */
  protected void notifyListeners(final PropertyChangeEvent event, final StateChangeCallbackHandler callbackHandler)
    throws PropertyVetoException
  {
    fireVetoableChange(event);
    callbackHandler.changeState();
    firePropertyChange(event);
    fireChangeEvent();
  }

  /**
   * Process the property change from old value to new value for the property identified by name.
   * @param propertyName a String value specifying the name of the property that changed.
   * @param oldValue an Object value indicating the original value of the property that changed.
   * @param newValue an Object value specifying the new value of the property that changed.
   * @param callbackHandler a StateChangeCallbackHandler object used to change the state of the DataModel.
   * @see AbstractDataModel#notifyListener(String, Object, Object, com.cp.common.util.AbstractDataModel.StateChangeCallbackHandler)
   * @see AbstractDataModel#notifyListeners(java.beans.PropertyChangeEvent, com.cp.common.util.AbstractDataModel.StateChangeCallbackHandler)
   */
  protected void processChange(final String propertyName,
                               final Object oldValue,
                               final Object newValue,
                               final StateChangeCallbackHandler callbackHandler)
  {
    try {
      notifyListener(propertyName, oldValue, newValue, callbackHandler);
    }
    catch (PropertyVetoException e) {
      logger.warn(e.getMessage());
      throw new ConstraintViolationException(e.getMessage());
    }
  }

  /**
   * Interface used by the AbstractDataModel to correctly implement the notifyListeners method by notifying
   * VetoableChangeListeners before setting the appropriate state, and then setting state before notifying
   * PropertyChangeListeners and ChangeListeners of the state change.
   */
  protected interface StateChangeCallbackHandler {
    public void changeState();
  }

}
