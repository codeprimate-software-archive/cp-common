/*
 * BusinessServiceAdaptor.java (c) 6 September 2009
 *
 * Copyright (c) 2003, Codeprimate
 * All Rights Reserved
 * @author jblum
 * @version 2009.9.6
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.biz.BusinessService
 * @see com.cp.common.util.event.EventSource
 */

package com.cp.common.biz;

import com.cp.common.beans.Bean;
import com.cp.common.util.event.EventSource;

public abstract class BusinessServiceAdaptor<T extends Bean, BUSINESS_PROCESS> extends EventSource implements BusinessService<T, BUSINESS_PROCESS> {

  /**
   * Loads the specified Bean from the underlying data source accessed using a DAO.
   * @param bean the specified Bean to load from the underlying data source.
   * @return the Bean loaded from the underlying data source.
   */
  public T load(final T bean) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Processes the specified Bean by identifying and invoking business logic and business rules.
   * @param bean the specified Bean to be processed against the business logic and business rules
   * implemented by this business service.
   * @param bizProcess an enumerated value indicating the type of business proces transformation
   * and rule application that the Bean is subject to.
   */
  public void process(final T bean, final BUSINESS_PROCESS bizProcess) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Removes the specified Bean from the underlying data source accessed using a DAO.
   * @param bean the specified Bean to remove from the underlying data source.
   * @return a boolean value indicating whether the specified Bean was removed from the underlying data source.
   */
  public boolean remove(final T bean) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Saves the specified Bean to the underlying data source accessed using a DAO.
   * @param bean the specified Bean to save to the underlying data source.
   * @return the Bean after it has been saved to the underlying data source using a DAO.
   */
  public T save(final T bean) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

}
