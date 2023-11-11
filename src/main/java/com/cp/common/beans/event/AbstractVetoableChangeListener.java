/*
 * AbstractVetoableChangeListener.java (c) 30 January 2005
 *
 * This class was created to accomodate the limitation that not all property change event sources may provide
 * functionality to register an event handler to apply only to a specified property.  One example of such a event
 * source is the JComponent class in the javax.swing package, which extends the java.awt.Component class.  The
 * JComponent class does not provide a method to register a VetoableChangeListener for a specified property of
 * the JComponent class.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.8.7
 * @see com.cp.common.beans.event.AbstractListener
 * @see java.beans.VetoableChangeListener
 */

package com.cp.common.beans.event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

public abstract class AbstractVetoableChangeListener extends AbstractListener implements VetoableChangeListener {

  /**
   * Creates an instance of the AbstractVetoableChangeListener class.
   */
  public AbstractVetoableChangeListener() {
  }

  /**
   * Creates an instance of the AbstractVetoableChangeListener class initialized with an object reference to the
   * annotated bean object.  The object reference is used to introspect the bean in order to read annotations
   * used by this listener to handle event processing.
   * @param annotatedBean the annotated bean object.
   */
  public AbstractVetoableChangeListener(final Object annotatedBean) {
    super(annotatedBean);
  }

  /**
   * Creates an instance of the AbstractVetoableChangeListener class initialized with the specified property name.
   * This listener will only handle property change events from the registered bean for the specified property.
   * @param propertyName the name of the property that this listener will handle and process property change events for.
   */
  public AbstractVetoableChangeListener(final String propertyName) {
    super(propertyName);
  }

  /**
   * Creates an instance of the AbstractPropertyChangeListener class initialized with an object reference to the
   * annotated bean object as well as the specifed property name for which property change events will be handled.
   * The object reference is used to introspect the bean in order to read annotations used by this listener to handle
   * event processing.  This listener will only handle property change events from the registered bean for the
   * specified property.
   * @param annotatedBean the annotated bean object.
   * @param propertyName the name of the property that this listener will handle and process property change events for.
   */
  public AbstractVetoableChangeListener(final Object annotatedBean, final String propertyName) {
    super(annotatedBean, propertyName);
  }

  /**
   * Method called to handle/process the PropertyChangeEvent.
   * @param event the PropertyChangeEvent object containing information about property change of the registered bean.
   * @throws PropertyVetoException if the property change event is vetoed by this listener.
   */
  protected abstract void handle(PropertyChangeEvent event) throws PropertyVetoException;

  /**
   * The vetoableChange method of this listener is triggered duing a property change event for a constrained property
   * on the event source for which this listener is registered.  If the listener's propertyName property is null,
   * then this listener handles all property change events from the event source that notifies this listener.
   * @param event the Event object used to describe the property change event that occurred.
   * @throws PropertyVetoException if the property change violates the contraints imposed upon the property.
   */
  public void vetoableChange(final PropertyChangeEvent event) throws PropertyVetoException {
    if (isInterested(event.getPropertyName())) {
      handle(event);
    }
  }

}
