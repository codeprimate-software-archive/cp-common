/*
 * AbstractBean.java (c) 17 October 2004
 *
 * This class is a representation of a business domain object of the application's model.  The business domain object
 * is commonly referred to as a domain object of the domain model.
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see com.cp.common.beans.AbstractBeanHistory
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.beans.BeanHistory
 * @see com.cp.common.beans.DefaultProcess
 * @see com.cp.common.beans.DefaultUser
 * @see com.cp.common.beans.Process
 * @see com.cp.common.beans.User
 * @see com.cp.common.beans.annotation.BoundedDate
 * @see com.cp.common.beans.annotation.BoundedLength
 * @see com.cp.common.beans.annotation.BoundedNumber
 * @see com.cp.common.beans.annotation.Default
 * @see com.cp.common.beans.annotation.Required
 * @see com.cp.common.beans.event.BoundedDateVetoableChangeListener
 * @see com.cp.common.beans.event.BoundedLengthVetoableChangeListener
 * @see com.cp.common.beans.event.BoundedNumberVetoableChangeListener
 * @see com.cp.common.beans.event.LoggingPropertyChangeListener
 * @see com.cp.common.beans.event.ObjectImmutableVetoableChangeListener
 * @see com.cp.common.beans.event.RequiredVetoableChangeListener
 * @see com.cp.common.beans.event.RollbackVetoableChangeListener
 * @see com.cp.common.beans.event.SetDefaultValuePropertyChangeListener
 * @see com.cp.common.beans.support.DefaultPropertyChangeManager
 * @see com.cp.common.beans.support.PropertyChangeManager
 * @see com.cp.common.beans.util.BeanUtil
 * @see com.cp.common.enums.AbstractEnum
 * @see com.cp.common.enums.Enum
 * @see com.cp.common.util.Visitor
 * @see java.beans.PropertyChangeEvent
 * @see java.beans.PropertyChangeListener
 * @see java.beans.PropertyChangeSupport
 * @see java.beans.PropertyVetoException
 * @see java.beans.VetoableChangeListener
 * @see java.beans.VetoableChangeSupport
 * @see javax.swing.event.ChangeEvent
 * @see javax.swing.event.ChangeListener
 * @see javax.swing.event.EventListenerList
 */

package com.cp.common.beans;

import com.cp.common.beans.annotation.Required;
import com.cp.common.beans.event.BoundedDateVetoableChangeListener;
import com.cp.common.beans.event.BoundedLengthVetoableChangeListener;
import com.cp.common.beans.event.BoundedNumberVetoableChangeListener;
import com.cp.common.beans.event.LoggingPropertyChangeListener;
import com.cp.common.beans.event.ObjectImmutableVetoableChangeListener;
import com.cp.common.beans.event.RequiredVetoableChangeListener;
import com.cp.common.beans.event.RollbackVetoableChangeListener;
import com.cp.common.beans.event.SetDefaultValuePropertyChangeListener;
import com.cp.common.beans.support.DefaultPropertyChangeManager;
import com.cp.common.beans.support.PropertyChangeManager;
import com.cp.common.beans.util.BeanUtil;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.Mutable;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.CollectionUtil;
import com.cp.common.util.DateUtil;
import com.cp.common.util.Visitor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;

public abstract class AbstractBean<T extends Comparable<T>> implements Bean<T> {

  private boolean eventDispatchEnabled = true;
  private boolean mutable = Mutable.MUTABLE;
  private boolean rollbackCalled = false;
  private boolean throwExceptionOnRollback = false;

  private T id;

  private BeanHistory beanHistory;

  private Calendar createdDateTime;
  private Calendar lastModifiedDateTime;
  private Calendar modifiedDateTime;

  private ChangeEvent changeEvent;

  private final EventListenerList changeListenerList;

  protected transient final Log logger = LogFactory.getLog(getClass());

  private Process creatingProcess;
  private Process modifyingProcess;

  private final PropertyChangeManager propertyChangeManager;

  private final PropertyChangeSupport propertyChangeSupport;

  private User createdBy;
  private User lastModifiedBy;
  private User modifiedBy;

  private final VetoableChangeSupport vetoableChangeSupport;

  /**
   * Creates an instance of the AbstractBean class.  This class is an abstraction and foundation for
   * business objects, or domain objects of an application's domain model.
   */
  public AbstractBean() {
    changeEvent = new ChangeEvent(this);
    changeListenerList = new EventListenerList();
    propertyChangeManager = getPropertyChangeManagerImpl();
    propertyChangeSupport = new PropertyChangeSupport(this);
    vetoableChangeSupport = new VetoableChangeSupport(this);
    addPropertyChangeListener(propertyChangeManager);
    addPropertyChangeListener(new LoggingPropertyChangeListener(this, getLogLevel()));
    addPropertyChangeListener(new SetDefaultValuePropertyChangeListener(this));
    addVetoableChangeListener(new BoundedDateVetoableChangeListener(this));
    addVetoableChangeListener(new BoundedLengthVetoableChangeListener(this));
    addVetoableChangeListener(new BoundedNumberVetoableChangeListener(this));
    addVetoableChangeListener(ObjectImmutableVetoableChangeListener.INSTANCE);
    addVetoableChangeListener(new RequiredVetoableChangeListener(this));
    addVetoableChangeListener(RollbackVetoableChangeListener.INSTANCE);
  }

  /**
   * Creates an instance of the AbstractBean class initialized with the specified ID.  This class is an abstraction
   * and foundation for business objects, or domain objects of an application's domain model.
   * This constructor sets up state and creates PropertyChangeListener and VetoableChangeListener support
   * objects as well as creating a default instance of the PropertyChangeManager (a PropertyChangeListener)
   * class to record and manage property changes.  In addition, this class also registers some default
   * PropertyChangeListener and VetoableChangeListener classes to handle such things as logging, bounded number
   * properties, immutability, required properties and rollback behavior.
   * @param id the unique identifier of this Bean object.
   */
  public AbstractBean(final T id) {
    this();
    setId(id);
  }

  /**
   * Gets the bean history to which this bean belongs.
   * @return an BeanHistory object for which this bean belongs.
   */
  public BeanHistory getBeanHistory() {
    return beanHistory;
  }

  /**
   * Sets the bean history object containing this bean.
   * @param beanHistory the BeanHistory object containing this bean.
   */
  public void setBeanHistory(final BeanHistory beanHistory) {
    Assert.state(ObjectUtil.isNull(beanHistory) || ObjectUtil.isSame(this.beanHistory, beanHistory)
      || (ObjectUtil.isNull(this.beanHistory) && ObjectUtil.isNotNull(beanHistory)),
      "This bean (" + getClass().getName() + ") identified by id (" + getId() + ") already belongs to a bean history object!");
    this.beanHistory = beanHistory;
  }

  /**
   * Returns the user responsible for the creation of this Object.
   * @return a User object referring to the user that created this Object.
   */
  public User getCreatedBy() {
    return createdBy;
  }

  /**
   * Set the user responsible for creating this Object.
   * @param createdBy a User object referring to the user that created this Object.
   */
  @Required
  public void setCreatedBy(final User createdBy) {
    final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
      public void changeState() {
        AbstractBean.this.createdBy = createdBy;
      }
    };
    processChange("createdBy", this.createdBy, createdBy, callbackHandler);
  }

  /**
   * Returns the date/time when this Object was created.
   * @return a Calendar object specifying when this Object was created.
   */
  public Calendar getCreatedDateTime() {
    return DateUtil.copy(createdDateTime);
  }

  /**
   * Set the date/time when this Object was created.
   * @param createdDateTime a Calendar object specifying the date/time when this Object was created.
   */
  @Required
  public void setCreatedDateTime(final Calendar createdDateTime) {
    final Calendar createdDateTimeCopy = DateUtil.copy(createdDateTime);
    final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
      public void changeState() {
        AbstractBean.this.createdDateTime = createdDateTimeCopy;
      }
    };
    processChange("createdDateTime", getCreatedDateTime(), createdDateTime, callbackHandler);
  }

  /**
   * Returns the process responsible for creating this Object.
   * @return a Process object specifying the process that created this Object.
   */
  public Process getCreatingProcess() {
    return creatingProcess;
  }

  /**
   * Set the creatingProcess responsible for creating this Object.
   * @param creatingProcess a Process object specifying the creatingProcess that craeted this Object.
   */
  public void setCreatingProcess(final Process creatingProcess) {
    final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
      public void changeState() {
        AbstractBean.this.creatingProcess = creatingProcess;
      }
    };
    processChange("creatingProcess", this.creatingProcess, creatingProcess, callbackHandler);
  }

  /**
   * Determines whether listeners should be notified of property changes when the bean's setter (mutator) methods
   * are called.
   * @return a boolean value indicating whether listeners will notified of property changes.
   */
  protected boolean isEventDispatchEnabled() {
    return eventDispatchEnabled;
  }

  /**
   * Sets whether listeners will be notified of property changes when the bean's setter (mutator) methods are called.
   * @param eventDispatchEnabled is a boolean value indicating whether listeners will be notified of property changes
   * when the bean's setter (mutator) methods are called.
   */
  protected void setEventDispatchEnabled(final boolean eventDispatchEnabled) {
    if (!eventDispatchEnabled && logger.isWarnEnabled()) {
      logger.warn("disabling event dispatch for bean (" + getClass().getName() + ") having id (" + getId() + ")!");
    }
    this.eventDispatchEnabled = eventDispatchEnabled;
  }

  /**
   * Returns the unique identifier for this Bean uniquely identify Objects of this type..
   * @return an Object of type I that specifies the unique identifier of this Bean.
   */
  public final T getId() {
    return id;
  }

  /**
   * Sets the identifier uniquely identifying this Bean.
   * @param id an Object of type I whose value uniquely identifies Beans of this Object type.
   */
  public final void setId(final T id) {
    final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
      public void changeState() {
        AbstractBean.this.id = id;
      }
    };
    processChange("id", this.id, id, callbackHandler);
  }

  /**
   * Returns the last user reponsible for modifying the state of this Object.
   * @return a User object referring to the last user that modified the state of this Object.
   * @see AbstractBean#getModifiedBy()
   */
  public final User getLastModifiedBy() {
    return lastModifiedBy;
  }

  /**
   * Sets the last User who modified this Bean's property values.
   * NOTE this is a private internal method used by the commit method to record the User if different
   * than the current User.
   * @param lastModifiedBy a User object referring to the last User to modify this Bean's state (properties).
   * @see AbstractBean#setModifiedBy
   */
  private void setLastModifiedBy(final User lastModifiedBy) {
    if (logger.isDebugEnabled()) {
      logger.debug("last modified by (" + lastModifiedBy + ")");
    }
    this.lastModifiedBy = lastModifiedBy;
  }

  /**
   * Returns the last date/time when this Object's state was modified.
   * @return a Calendar object specifying the last date/time this Object's state was modified.
   * @see AbstractBean#getModifiedDateTime()
   */
  public final Calendar getLastModifiedDateTime() {
    return DateUtil.copy(lastModifiedDateTime);
  }

  /**
   * Sets a Calendar object specifying the last date and time when this Bean was modified.
   * NOTE this is a private  internal method used by the commit method to record the previous date/time
   * if different than the current date/time.
   * @param lastModifiedDateTime a Calendar object specifying the date and time when the Bean was last modified.
   * @see AbstractBean#setModifiedDateTime
   */
  private void setLastModifiedDateTime(final Calendar lastModifiedDateTime) {
    if (logger.isDebugEnabled()) {
      logger.debug("last modified date time (" + DateUtil.toString(lastModifiedDateTime) + ")");
    }
    this.lastModifiedDateTime = DateUtil.copy(lastModifiedDateTime);
  }

  /**
   * Returns the default log level of this Bean.  This method is intended to be overridden by subclasses
   * to change the logging level for this bean.
   * @return a Level object specifying the default log level for this bean.
   */
  protected Level getLogLevel() {
    return Level.DEBUG;
  }

  /**
   * Determines whether this Object's state has changed, or been modified.
   * @return a boolean value indicating if this Object's state has changed, or been modified.
   * @see AbstractBean#isNew()
   * @see AbstractBean#isModified(String)
   * @see PropertyChangeManager#hasModifiedProperties()
   */
  public boolean isModified() {
    return getPropertyChangeManager().hasModifiedProperties();
  }

  /**
   * Determines whether the property specified by name of this Bean has been modified.
   * @param propertyName the String name of the property.
   * @return a boolean value indicating if the property specified by name on this Bean has been modified.
   * @see AbstractBean#isNew()
   * @see AbstractBean#isModified()
   */
  public boolean isModified(final String propertyName) {
    return getPropertyChangeManager().contains(propertyName);
  }

  /**
   * Returns the user responsible for modifying this Object's state (properties).
   * @return a User object referring to the user responsible for modifying this Object's state (properties).
   */
  public User getModifiedBy() {
    return modifiedBy;
  }

  /**
   * Set the user responsible for modifying this Object's state (properties).
   * @param modifiedBy a User object referring to the user responsible for modifying this Object's state (properties).
   */
  @Required
  public void setModifiedBy(final User modifiedBy) {
    final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
      public void changeState() {
        AbstractBean.this.modifiedBy = modifiedBy;
      }
    };
    processChange("modifiedBy", this.modifiedBy, modifiedBy, callbackHandler);
  }

  /**
   * Returns the date/time when this Object's state was modified.
   * @return a Calendar object specifying the date/time when this Object's state was modified.
   */
  public Calendar getModifiedDateTime() {
    return DateUtil.copy(modifiedDateTime);
  }

  /**
   * Set the date/time when this Object's state was modified.
   * @param modifiedDateTime a Calendar object specifying the date/time when this Object's state was modified.
   */
  @Required
  public void setModifiedDateTime(final Calendar modifiedDateTime) {
    final Calendar modifiedDateTimeCopy = DateUtil.copy(modifiedDateTime);
    final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
      public void changeState() {
        AbstractBean.this.modifiedDateTime = modifiedDateTimeCopy;
      }
    };
    processChange("modifiedDateTime", getModifiedDateTime(), modifiedDateTime, callbackHandler);
  }

  /**
   * Returns a Set of property names that have been modified on this bean.
   * @return a Set object containing property names of the properties of this bean that have been modified.
   * @see PropertyChangeManager#getModifiedProperties()
   */
  public Set<String> getModifiedProperties() {
    return getPropertyChangeManager().getModifiedProperties();
  }

  /**
   * Returns the process responsible for modifying this Object's state (properties).
   * @return a Process object specifying the process state modified this Object's state (properties).
   */
  public Process getModifyingProcess() {
    return modifyingProcess;
  }

  /**
   * Sets the process responsible for modifying this Object's state (properties).
   * @param modifyingProcess a Process object specifying the process that modified this Object's state (properties).
   */
  public void setModifyingProcess(final Process modifyingProcess) {
    final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
      public void changeState() {
        AbstractBean.this.modifyingProcess = modifyingProcess;
      }
    };
    processChange("modifyingProcess", this.modifyingProcess, modifyingProcess, callbackHandler);
  }

  /**
   * Determines whether the specified bean is modifiable or not.
   * @return a boolean value indicating whehter this bean can be modified or not.
   */
  public boolean isMutable() {
    return mutable;
  }

  /**
   * Sets whether the specified bean can be modified, or edited.
   * @param mutable a boolean value indicating whether the bean is allowed to be modified or not.
   */
  public void setMutable(final boolean mutable) {
    this.mutable = mutable;
  }

  /**
   * Determines whether this Object is new, has not been persisted to a data store.
   * @return a boolean value indicating whether this Object is new.
   * @see AbstractBean#isModified()
   */
  public boolean isNew() {
    return ObjectUtil.isNull(getId());
  }

  /**
   * Returns the PropertyChangeManager, which is a PropertyChangeListener object, that can be used to track and manage
   * changes to properties, commit changes, and rollback or undo changes to the properties on this bean.
   * The PropertyChangeManager class can used by VetoableChangeListeners to undo property changes when one property
   * violates constraints imposed by a VetoableChangeListener class.
   * @return a the PropertyChangeManager class that records and manages property changes on this bean.
   */
  protected PropertyChangeManager getPropertyChangeManager() {
    return propertyChangeManager;
  }

  /**
   * Returns the implementing PropertyChangeManager class.  This class will record and manage all properties
   * that have been modified on this bean.
   * @return the PropertyChangeManager implementation defined by this bean.
   * @see DefaultPropertyChangeManager
   */
  protected PropertyChangeManager getPropertyChangeManagerImpl() {
    return new DefaultPropertyChangeManager(this);
  }

  /**
   * Determines whether rollback has been called on this bean.
   * @return a boolean value indicating if rollback has been called on this bean.
   */
  public boolean isRollbackCalled() {
    return rollbackCalled;
  }

  /**
   * Sets the rollbackCalled property on this bean accordingly.  The rollbackCalled property is used to indicate
   * that rollback has been called on this bean.
   * @param rollbackCalled a boolean value indicating the state of the
   */
  private void setRollbackCalled(final boolean rollbackCalled) {
    this.rollbackCalled = rollbackCalled;
  }

  /**
   * Determines whether an RuntimeException will be thrown on Bean setters when rollback has already been called.
   * @return a boolean value indicating whether Bean setters will throw a RuntimeException when rollback
   * has already been called.
   */
  public boolean isThrowExceptionOnRollback() {
    return throwExceptionOnRollback;
  }

  /**
   * Sets whether an RuntimeException will be thrown on Bean setters when rollback has already been called.
   * @param throwExceptionOnRollback a boolean value indicating whether Bean setters will throw a
   * RuntimeException when rollback has already been called.
   */
  protected void setThrowExceptionOnRollback(final boolean throwExceptionOnRollback) {
    this.throwExceptionOnRollback = throwExceptionOnRollback;
  }

  /**
   * Visits this Visitable object performing an operation defined by the implementing Visitor class on this
   * Visitable object.
   * @param visitor the Visitor visiting this Visitable object, performing the defined operation.
   */
  public void accept(final Visitor visitor) {
    visitor.visit(this);
  }

  /**
   * Registers the specified ChangeListener object with this Bean to listen for change events to the
   * Bean's properties.
   * @param listener the ChangeListener registering interest in property changes of this Bean.
   * @see AbstractBean#removeChangeListener(javax.swing.event.ChangeListener)
   */
  public void addChangeListener(final ChangeListener listener) {
    if (ObjectUtil.isNotNull(listener)) {
      if (logger.isDebugEnabled()) {
        logger.debug("adding change listener (" + listener.getClass().getName() + ")");
      }
      changeListenerList.add(ChangeListener.class, listener);
    }
  }

  /**
   * Registers an event listener to listen to property changes on this bean.
   * @param listener the PropertyChangeListener registering to listen for property changes on this bean.
   * @see AbstractBean#addPropertyChangeListener(String, java.beans.PropertyChangeListener)
   * @see AbstractBean#removePropertyChangeListener(java.beans.PropertyChangeListener)
   */
  public void addPropertyChangeListener(final PropertyChangeListener listener) {
    if (logger.isDebugEnabled()) {
      logger.debug("adding property change listener (" + ClassUtil.getClassName(listener) + ")");
    }
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  /**
   * Registers an event listener to listen for changes to the specified property on this bean.
   * @param propertyName the name of the property on this bean in which the listener is interested in listening
   * for changes of the property.
   * @param listener the PropertyChangeListener registering to listen for changes on the specified property of
   * this bean.
   * @see AbstractBean#addPropertyChangeListener(java.beans.PropertyChangeListener)
   * @see AbstractBean#removePropertyChangeListener(String, java.beans.PropertyChangeListener)
   */
  public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
    if (logger.isDebugEnabled()) {
      logger.debug("adding property change listener (" + ClassUtil.getClassName(listener)
        + ") for property (" + propertyName + ")");
    }
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * Registers an event listener that vetos changes to properties on this bean that violate constraints imposed on the
   * bean's properties values.
   * @param listener the VetoableChangeListener being registered with this bean.
   * @see AbstractBean#addVetoableChangeListener(String, java.beans.VetoableChangeListener)
   * @see AbstractBean#removeVetoableChangeListener(java.beans.VetoableChangeListener)
   */
  public void addVetoableChangeListener(final VetoableChangeListener listener) {
    if (logger.isDebugEnabled()) {
      logger.debug("adding vetoable change listener (" + ClassUtil.getClassName(listener) + ")");
    }
    vetoableChangeSupport.addVetoableChangeListener(listener);
  }

  /**
   * Registers an event listener that vetos changes to the specified property on this bean that violate constraints
   * imposed on the specified property's value.
   * @param listener the VetoableChangeListener being registered with this bean for the specified property.
   * @see AbstractBean#addVetoableChangeListener(java.beans.VetoableChangeListener)
   * @see AbstractBean#removeVetoableChangeListener(String, java.beans.VetoableChangeListener)
   */
  public void addVetoableChangeListener(final String propertyName, final VetoableChangeListener listener) {
    if (logger.isDebugEnabled()) {
      logger.debug("adding vetoable change listener (" + ClassUtil.getClassName(listener)
        + ") for property (" + propertyName + ")");
    }
    vetoableChangeSupport.addVetoableChangeListener(propertyName, listener);
  }

  /**
   * Creates and returns a copy of this object.
   * @return a clone of this instance.
   * @throws CloneNotSupportedException if the object's class does not support the Cloneable interface.
   * Subclasses that override the clone method can also throw this exception to indicate that an instance
   * cannot be cloned.
   * @see AbstractBean#copy()
   */
  @Override
  public final Object clone() throws CloneNotSupportedException {
    return copy();
  }

  /**
   * Makes permanent, any changes that were made to the values of the properties on this bean.
   */
  public void commit() {
    getPropertyChangeManager().doCommit(this);
    setLastModifiedBy(getModifiedBy());
    setLastModifiedDateTime(getModifiedDateTime());
  }

  /**
   * Creates an exact copy of this Bean object except the id and auditable properties are set to null.  This method
   * performs a shallow copy of this Bean object copying references to composed objects to the copied Bean.
   * @return an exact copy of this Bean.
   * @see AbstractBean#clone()
   */
  public Object copy() {
    try {
      final Bean beanCopy = getClass().newInstance();

      final Map<String, Object> propertyValueMap = BeanUtil.getPropertyValues(this);
      propertyValueMap.remove("class");
      propertyValueMap.remove("createdBy");
      propertyValueMap.remove("createdDateTime");
      propertyValueMap.remove("creatingProcess");
      propertyValueMap.remove("lastModifiedBy");
      propertyValueMap.remove("lastModifiedDateTime");
      propertyValueMap.remove("modified");
      propertyValueMap.remove("modifiedBy");
      propertyValueMap.remove("modifiedDateTime");
      propertyValueMap.remove("modifiedProperties");
      propertyValueMap.remove("modifyingProcess");
      propertyValueMap.remove("mutable");
      propertyValueMap.remove("new");
      propertyValueMap.remove("rollbackCalled");
      propertyValueMap.remove("throwExceptionOnRollback");

      BeanUtil.setPropertyValues(beanCopy, propertyValueMap);
      beanCopy.setId(null);
      beanCopy.setMutable(Mutable.MUTABLE);

      return beanCopy;
    }
    catch (Exception e) {
      logger.error("Failed to copy an instance of Bean (" + getClass().getName() + ")!", e);
      throw new BeanInstantiationException("Failed to copy an instance of Bean (" + getClass().getName() + ")!", e);
    }
  }

  /**
   * Notifies interested ChangeListeners that a change occurred to one of the properties of this Bean.
   */
  protected final void notifyChangeListeners() {
    if (isModified()) {
      final Object[] listenerList = changeListenerList.getListenerList();
      for (int index = listenerList.length - 2; index >= 0; index -= 2) {
        if (listenerList[index] == ChangeListener.class) {
          ((ChangeListener) listenerList[index + 1]).stateChanged(changeEvent);
        }
      }
    }
  }

  /**
   * Notifies VetoableChangeListeners, changes state, and notifies PropertyChangesListeners in that order, of
   * the property change, specified by propertyName, on this bean.
   * @param propertyName the name of the property that is changing.
   * @param oldValue the property's value before the change..
   * @param newValue the property's value after the change.
   * @param callbackHandler a StateChangeCallbackHandler object that will be called when the state of the object
   * is updated.
   * @throws PropertyVetoException if the new value for the specified property with propertyName violates any
   * constraints imposed by a VetoableChangeListener registered with this Bean.
   * @see AbstractBean#notifyListeners(java.beans.PropertyChangeEvent, com.cp.common.beans.AbstractBean.StateChangeCallbackHandler)
   */
  protected final void notifyListeners(final String propertyName,
                                       final Object oldValue,
                                       final Object newValue,
                                       final StateChangeCallbackHandler callbackHandler)
    throws PropertyVetoException
  {
    notifyListeners(new PropertyChangeEvent(this, propertyName, oldValue, newValue), callbackHandler);
  }

  /**
   * Notifies VetoableChangeListeners, changes state, and notifies the PropertyChangesListeners in that order, of
   * a property change on this bean.
   * @param event the PropertyChangeEvent recording the property, old value and new value that is sent to
   * registered listeners.
   * @param callbackHandler a StateChangeCallbackHandler object used to change the state of the Bean.
   * @throws PropertyVetoException if the new value for the specified property with propertyName violates any
   * constraints imposed by a VetoableChangeListener registered with this Bean.
   * @see AbstractBean#isEventDispatchEnabled()
   * @see AbstractBean#notifyChangeListeners()
   * @see AbstractBean#notifyPropertyListeners(java.beans.PropertyChangeEvent)
   * @see AbstractBean#notifyVetoListeners(java.beans.PropertyChangeEvent)
   * @see AbstractBean.StateChangeCallbackHandler#changeState()
   */
  protected final void notifyListeners(final PropertyChangeEvent event, final StateChangeCallbackHandler callbackHandler)
    throws PropertyVetoException
  {
    if (logger.isDebugEnabled()) {
      logger.debug("event dispatch enabled (" + isEventDispatchEnabled() + ")");
      logger.debug("property change event (" + event + ")");
    }

    if (isEventDispatchEnabled()) {
      notifyVetoListeners(event);
    }

    callbackHandler.changeState();

    if (isEventDispatchEnabled()) {
      notifyPropertyListeners(event);
      notifyChangeListeners();
    }
  }

  /**
   * Notifies all property change listeners interested in changes to the specified property of the state change.
   * The method also notifies all change listeners of the state change in this Bean if the Bean has indeed been
   * modified.
   * @param propertyName the name of the property that is changing.
   * @param oldValue the property's value before the change..
   * @param newValue the property's value after the change.
   * @see AbstractBean#notifyPropertyListeners(java.beans.PropertyChangeEvent)
   */
  protected final void notifyPropertyListeners(final String propertyName, final Object oldValue, final Object newValue) {
    notifyPropertyListeners(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
  }

  /**
   * Notifies all property change listeners of the specified property change event. The method also notifies
   * all change listeners of the state change in this Bean if the Bean has indeed been modified.
   * @param event the PropertyChangeEvent recording the property, old value and new value that is sent to
   * registered listeners.
   * @see AbstractBean#notifyPropertyListeners(String, Object, Object)
   */
  protected final void notifyPropertyListeners(final PropertyChangeEvent event) {
    if (logger.isDebugEnabled()) {
      logger.debug("property change event (" + event + ")");
    }
    propertyChangeSupport.firePropertyChange(event);
  }

  /**
   * Notifies all vetoable change listeners interested in changes to the specified property of the state change.
   * @param propertyName the name of the property that is changing.
   * @param oldValue the property's value before the change..
   * @param newValue the property's value after the change.
   * @throws PropertyVetoException if the new value for the specified property with propertyName violates any
   * constraints imposed by a VetoableChangeListener registered with this Bean.
   * @see AbstractBean#notifyVetoListeners(java.beans.PropertyChangeEvent)
   */
  protected final void notifyVetoListeners(final String propertyName, final Object oldValue, final Object newValue)
    throws PropertyVetoException
  {
    notifyVetoListeners(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
  }

  /**
   * Notifies all vetoable change listeners of the specified property change event.
   * @param event the PropertyChangeEvent recording the property, old value and new value that is sent to
   * registered listeners.
   * @throws PropertyVetoException if the new value for the specified property with propertyName violates any
   * constraints imposed by a VetoableChangeListener registered with this Bean.
   * @see AbstractBean#notifyVetoListeners(String, Object, Object)
   */
  protected final void notifyVetoListeners(final PropertyChangeEvent event) throws PropertyVetoException {
    if (logger.isDebugEnabled()) {
      logger.debug("property change event (" + event + ")");
    }
    vetoableChangeSupport.fireVetoableChange(event);
  }

  /**
   * A convenience method used by subclasses of the AbstractBean class to notify vetoable change and property change
   * listeners of the property change on this bean.  This method handles the PropertyVetoException and throws
   * a RuntimeException (IllegalArgumentException) instead.
   * @param propertyName the name of the property that is changing.
   * @param oldValue the property's value before the change.
   * @param newValue the property's value after the change.
   * @param callbackHandler a StateChangeCallbackHandler object used to change the state of the Bean.
   * @throws IllegalArgumentException if the new value for the specified property with propertyName causes a
   * PropertyVetoException from one of the VetoableChangeListeners to occurr.
   * @see AbstractBean#notifyListeners(String, Object, Object, com.cp.common.beans.AbstractBean.StateChangeCallbackHandler)
   */
  protected final void processChange(final String propertyName,
                                     final Object oldValue,
                                     final Object newValue,
                                     final StateChangeCallbackHandler callbackHandler)
  {
    try {
      notifyListeners(propertyName, oldValue, newValue, callbackHandler);
    }
    catch (PropertyVetoException e) {
      logger.error("The new value (" + newValue + ") for property (" + propertyName + ") of bean ("
        + getClass().getName() + ") is not valid!", e);
      throw new ConstraintViolationException(e.getMessage());
    }
  }

  /**
   * Unregisteres the specified ChangeListener from receiving property change events from this Bean.
   * @param listener the ChangeListener being unregistered with this Bean so that it no longer receives
   * property change events.
   * @see AbstractBean#addChangeListener(javax.swing.event.ChangeListener)
   */
  public void removeChangeListener(final ChangeListener listener) {
    if (ObjectUtil.isNotNull(listener)) {
      if (logger.isDebugEnabled()) {
        logger.debug("removing change listener (" + listener.getClass().getName() + ")");
      }
      changeListenerList.remove(ChangeListener.class, listener);
    }
  }

  /**
   * Unregisters an event listener listening to property changes on this bean.
   * @param listener the PropertyChangeListener removing itself from notification of property changes on this bean.
   * @see AbstractBean#addPropertyChangeListener(java.beans.PropertyChangeListener)
   * @see AbstractBean#removePropertyChangeListener(String, java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(final PropertyChangeListener listener) {
    if (logger.isDebugEnabled()) {
      logger.debug("removing property change listener (" + ClassUtil.getClassName(listener) + ")");
    }
    propertyChangeSupport.removePropertyChangeListener(listener);
  }

  /**
   * Unregisters an event listener listening for changes to the specified property on this bean.
   * @param propertyName the name of the property on this bean in which the listener is interested in listening
   * for changes of the property.
   * @param listener the PropertyChangeListener removing itself from notification of changes for the specified
   * property of this bean.
   * @see AbstractBean#addPropertyChangeListener(String, java.beans.PropertyChangeListener)
   * @see AbstractBean#removePropertyChangeListener(java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
    if (logger.isDebugEnabled()) {
      logger.debug("removing property change listener (" + ClassUtil.getClassName(listener)
        + ") for property (" + propertyName + ")");
    }
    propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
  }

  /**
   * Unregisters an event listener that vetos changes to properties on this bean violating constraints imposed on the
   * bean's property values.
   * @param listener the VetoableChangeListener being removed from notification of property changes with this bean.
   * @see AbstractBean#addVetoableChangeListener(java.beans.VetoableChangeListener)
   * @see AbstractBean#removeVetoableChangeListener(String, java.beans.VetoableChangeListener)
   */
  public void removeVetoableChangeListener(final VetoableChangeListener listener) {
    if (logger.isDebugEnabled()) {
      logger.debug("remove vetoable change listener (" + ClassUtil.getClassName(listener) + ")");
    }
    vetoableChangeSupport.removeVetoableChangeListener(listener);
  }

  /**
   * Unregisters an event listener vetoing changes to the specified property on this bean which violate constraints
   * imposed on the specified property's value.
   * @param listener the VetoableChangeListener being removed from property change notification with this bean
   * for the specified property.
   * @see AbstractBean#addVetoableChangeListener(String, java.beans.VetoableChangeListener)
   * @see AbstractBean#removeVetoableChangeListener(java.beans.VetoableChangeListener)
   */
  public void removeVetoableChangeListener(final String propertyName, final VetoableChangeListener listener) {
    if (logger.isDebugEnabled()) {
      logger.debug("removing vetoable change listener (" + ClassUtil.getClassName(listener)
        + ") for property (" + propertyName + ")");
    }
    vetoableChangeSupport.removeVetoableChangeListener(propertyName, listener);
  }

  /**
   * Resets the internal state of this bean.  Specifically, this reset method will reset the state of the rollback flag.
   */
  public void reset() {
    setRollbackCalled(false);
  }

  /**
   * Undoes any changes to the properties of this bean.
   */
  public void rollback() {
    try {
      setRollbackCalled(true);
      setEventDispatchEnabled(false);
      getPropertyChangeManager().doRollback(this);
    }
    finally {
      setEventDispatchEnabled(true);
    }
  }

  /**
   * Default implementation of the toString method returning a String respresentation of this Bean
   * thereby externalizing the state of the Bean.
   * @return a String representation of the Bean's internal state.
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{");

    try {
      final Map<String, Object> propertyValueMap = BeanUtil.getPropertyValues(this);
      boolean condition = false;

      // remove all Object properties
      propertyValueMap.remove("class");

      for (final String propertyName : propertyValueMap.keySet()) {
        final Object propertyValue = propertyValueMap.get(propertyName);

        if (condition) {
          buffer.append(", ");
        }

        buffer.append(propertyName);
        buffer.append(" = ");
        buffer.append(BeanUtil.toString(propertyValue));
        condition = true;
      }
    }
    catch (Exception e) {
      logger.warn(e.getMessage());
      buffer.append("createdBy = ").append(getCreatedBy());
      buffer.append(", createdDateTime = ").append(DateUtil.toString(getCreatedDateTime(), DEFAULT_DATE_FORMAT_PATTERN));
      buffer.append(", creatingProcess = ").append(getCreatingProcess());
      buffer.append(", id = ").append(getId());
      buffer.append(", lastModifiedBy = ").append(getLastModifiedBy());
      buffer.append(", lastModifiedDateTime = ").append(DateUtil.toString(getLastModifiedDateTime(), DEFAULT_DATE_FORMAT_PATTERN));
      buffer.append(", modified = ").append(isModified());
      buffer.append(", modifiedBy = ").append(getModifiedBy());
      buffer.append(", modifiedDateTime = ").append(DateUtil.toString(getModifiedDateTime(), DEFAULT_DATE_FORMAT_PATTERN));
      buffer.append(", modifiedProperties = ").append(CollectionUtil.toString(getModifiedProperties()));
      buffer.append(", modifyingProcess = ").append(getModifyingProcess());
      buffer.append(", mutable = ").append(isMutable());
      buffer.append(", new = ").append(isNew());
      buffer.append(", propertyChangeManagerImpl = ").append(ClassUtil.getClassName(getPropertyChangeManager()));
      buffer.append(", rollbackCalled = ").append(isRollbackCalled());
      buffer.append(", throwExceptionOnRollback = ").append(isThrowExceptionOnRollback());
    }

    buffer.append("}:").append(getClass().getName());

    return buffer.toString();
  }

  /**
   * Interface used by the AbstractBean to correctly implement the notifyListeners method by notifing VetoableChangeListeners
   * before setting the appropriate state, and then setting state before notifying PropertyChangeListeners and ChangeListeners
   * of the state change.
   */
  protected interface StateChangeCallbackHandler {

    public void changeState();

  }

}
