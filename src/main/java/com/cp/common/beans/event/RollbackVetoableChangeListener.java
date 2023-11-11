/*
 * RollbackVetoableChangeListener.java (c) 2 January 2007
 *
 * The RollbackVetoableChangeListener effectively prevents changes to a bean's properties if rollback
 * has been called on the bean and the bean is set to throw an exception on rollback.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.25
 * @see com.cp.common.beans.event.AbstractVetoableChangeListener
 */

package com.cp.common.beans.event;

import com.cp.common.beans.Bean;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

public final class RollbackVetoableChangeListener extends AbstractVetoableChangeListener {

  public static final RollbackVetoableChangeListener INSTANCE = new RollbackVetoableChangeListener();

  /**
   * Default private constructor to enforce non-instantiability.
   */
  private RollbackVetoableChangeListener() {
  }

  /**
   * Handle processes the property change event by first determining whether rollback has been called
   * on the source bean and whether to veto the property change based on the throwExceptionOnSetWithRollback
   * bean property being set.
   * @param event a PropertyChangeEvent object encapsulating information pertaining to the property change event.
   * @throws PropertyVetoException if the bean has been rolled back and the throwExceptionOnSetWithRollback property
   * has been set to true.
   */
  protected void handle(final PropertyChangeEvent event) throws PropertyVetoException {
    final Bean beanObject = (Bean) event.getSource();

    if (beanObject.isRollbackCalled() && beanObject.isThrowExceptionOnRollback()) {
      logger.warn("Rollback has been called on bean (" + beanObject.getClass().getName()
        + "); unable to set property (" + event.getPropertyName() + ")!");
      throw new PropertyVetoException("Rollback has been called on bean (" + beanObject.getClass().getName()
        + "); unable to set property (" + event.getPropertyName() + ")!", event);
    }
  }

}
