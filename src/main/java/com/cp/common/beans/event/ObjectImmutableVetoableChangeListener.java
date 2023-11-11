/*
 * ObjectImmutableVetoableChangeListener.java (c) 10 July 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.23
 * @see com.cp.common.beans.event.AbstractVetoableChangeListener
 */

package com.cp.common.beans.event;

import com.cp.common.lang.Mutable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

public final class ObjectImmutableVetoableChangeListener extends AbstractVetoableChangeListener {

  public static final ObjectImmutableVetoableChangeListener INSTANCE = new ObjectImmutableVetoableChangeListener();

  /**
   * Private default constructor to prevent instantiation of a singleton class.
   */
  private ObjectImmutableVetoableChangeListener() {
  }

  /**
   * Method called to handle the PropertyChangeEvent.
   * @param event the Event object used to describe the property change event that occurred.
   * @throws PropertyVetoException if the property change violates the contraints imposed upon the property.
   */
  protected void handle(final PropertyChangeEvent event) throws PropertyVetoException {
    if (event.getSource() instanceof Mutable) {
      final Mutable mutableObject = (Mutable) event.getSource();

      if (logger.isDebugEnabled()) {
        logger.debug("The class (" + mutableObject.getClass().getName() + ") implements the Mutable interface!");
      }

      if (!mutableObject.isMutable()) {
        logger.warn("The object (" + mutableObject.getClass().getName() + ") is immutable!");
        throw new PropertyVetoException("The object (" + mutableObject.getClass().getName() + ") is immutable!", event);
      }
    }
  }

}
