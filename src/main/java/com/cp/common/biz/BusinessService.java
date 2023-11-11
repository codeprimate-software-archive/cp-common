/*
 * BusinessService.java (c) 7 December 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2010.1.17
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.util.ApplicationException
 * @see com.cp.common.util.SystemException
 */

package com.cp.common.biz;

import com.cp.common.beans.Bean;
import com.cp.common.util.ApplicationException;
import com.cp.common.util.SystemException;

public interface BusinessService<T extends Bean, BUSINESS_PROCESS> {

  /**
   * Loads the specified Bean from the underlying data source accessed using a DAO.
   * @param bean the specified Bean to load from the underlying data source.
   * @return the Bean loaded from the underlying data source.
   * @throws ApplicationException if the business service process violates a business rule.
   * @throws SystemException if the system/application is unable to process the transaction
   * due to external conditions.
   */
  public T load(T bean) throws ApplicationException, SystemException;

  /**
   * Processes the specified Bean by identifying and invoking business logic and business rules.
   * @param bean the specified Bean to be processed against the business logic and business rules
   * implemented by this business service.
   * @param bizProcess an enumerated value indicating the type of business proces transformation
   * and rule application that the Bean is subject to.
   * @throws ApplicationException if the business service process violates a business rule.
   * @throws SystemException if the system/application is unable to process the transaction
   * due to external conditions.
   */
  public void process(T bean, BUSINESS_PROCESS bizProcess) throws ApplicationException, SystemException;

  /**
   * Removes the specified Bean from the underlying data source accessed using a DAO.
   * @param bean the specified Bean to remove from the underlying data source.
   * @return a boolean value indicating whether the specified Bean was removed from the underlying data source.
   * @throws ApplicationException if the business service process violates a business rule.
   * @throws SystemException if the system/application is unable to process the transaction
   * due to external conditions.
   */
  public boolean remove(T bean) throws ApplicationException, SystemException;

  /**
   * Saves the specified Bean to the underlying data source accessed using a DAO.
   * @param bean the specified Bean to save to the underlying data source.
   * @return the Bean after it has been saved to the underlying data source using a DAO.
   * @throws ApplicationException if the business service process violates a business rule.
   * @throws SystemException if the system/application is unable to process the transaction
   * due to external conditions.
   */
  public T save(T bean) throws ApplicationException, SystemException;

}
