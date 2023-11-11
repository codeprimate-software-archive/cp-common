/*
 * AbstractPropertyChangeListener.java (c) 26 December 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.8.7
 * @see com.cp.common.beans.event.AbstractListener
 * @see java.beans.PropertyChangeListener
 */

package com.cp.common.beans.event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class AbstractPropertyChangeListener extends AbstractListener implements PropertyChangeListener {

  /**
   * Creates an instance of the AbstractPropertyChangeListener class.
   */
  public AbstractPropertyChangeListener() {
  }

  /**
   * Creates an instance of the AbstractPropertyChangeListener class initialized with an object reference to the
   * annotated bean object.  The object reference is used to introspect the bean in order to read annotations
   * used by this listener to handle event processing.
   * @param annotatedBean the annotated bean object.
   */
  public AbstractPropertyChangeListener(final Object annotatedBean) {
    super(annotatedBean);
  }

  /**
   * Creates an instance of the AbstractPropertyChangeListener class initialized with the specified property name.
   * This listener will only handle property change events from the registered bean for the specified property.
   * @param propertyName the name of the property that this listener will handle and process property change events for.
   */
  public AbstractPropertyChangeListener(final String propertyName) {
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
  public AbstractPropertyChangeListener(final Object annotatedBean, final String propertyName) {
    super(annotatedBean, propertyName);
  }

  /**
   * Method called to handle/process the PropertyChangeEvent.
   * @param event the PropertyChangeEvent object containing information about property change of the registered bean.
   */
  protected abstract void handle(PropertyChangeEvent event);

  /**
   * Process all property change events of properties on the registered bean for which this listener is interested.
   * This method delegates to the handle method if the isInterested method returns true for the specified property name
   * of the event object.
   * @param event the PropertyChangeEvent object containing information about property change of the registered bean.
   */
  public void propertyChange(final PropertyChangeEvent event) {
    if (isInterested(event.getPropertyName())) {
      handle(event);
    }
  }

}
