/*
 * AbstractBusinessService.java (c) 7 December 2008
 *
 * Copyright (c) 2003, Codeprimate
 * All Rights Reserved
 * @author John Blum
 * @version 2010.1.18
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.biz.BusinessService
 * @see com.cp.common.dao.FossilDao
 * @see com.cp.common.util.ApplicationException
 * @see com.cp.common.util.SystemException
 * @see com.cp.common.util.event.EventSource
 */

package com.cp.common.biz;

import com.cp.common.beans.Bean;
import com.cp.common.dao.FossilDao;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ApplicationException;
import com.cp.common.util.SystemException;
import com.cp.common.util.event.EventSource;

public abstract class AbstractBusinessService<T extends Bean, BUSINESS_PROCESS> extends EventSource implements BusinessService<T, BUSINESS_PROCESS> {

  private FossilDao<T> fossilDao;

  /**
   * Gets the Data Access Object (DAO) used for accessing the underlying data source for information retrieval
   * and storage.
   * @return an implemenatation of the FossilDao interface for accessing the underlying data source.
   */
  protected FossilDao<T> getDao() {
    Assert.state(ObjectUtil.isNotNull(fossilDao), "The DAO for this business handler has not been properly initialized!");
    return fossilDao;
  }

  /**
   * Sets the Data Access Object (DAO) used for accessing the underlying data source for information retrieval
   * and storage.
   * @param dao an implemenatation of the FossilDao interface for accessing the underlying data source.
   */
  public void setDao(final FossilDao<T> dao) {
    Assert.notNull(dao, "The DAO for this business handler cannot be null!");
    this.fossilDao = dao;
  }

  /**
   * Loads the specified Bean from the underlying data source accessed using a DAO.
   * @param bean the specified Bean to load from the underlying data source.
   * @return the Bean loaded from the underlying data source.
   * @throws ApplicationException if the business service process violates a business rule.
   * @throws SystemException if the system/application is unable to process the transaction
   * due to external conditions.
   */
  public T load(final T bean) throws ApplicationException, SystemException {
    return getDao().load(bean);
  }

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
  public void process(final T bean, final BUSINESS_PROCESS bizProcess) throws ApplicationException, SystemException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Removes the specified Bean from the underlying data source accessed using a DAO.
   * @param bean the specified Bean to remove from the underlying data source.
   * @return a boolean value indicating whether the specified Bean was removed from the underlying data source.
   * @throws ApplicationException if the business service process violates a business rule.
   * @throws SystemException if the system/application is unable to process the transaction
   * due to external conditions.
   */
  public boolean remove(final T bean) throws ApplicationException, SystemException {
    return getDao().remove(bean);
  }

  /**
   * Saves the specified Bean to the underlying data source accessed using a DAO.
   * @param bean the specified Bean to save to the underlying data source.
   * @return the Bean after it has been saved to the underlying data source using a DAO.
   * @throws ApplicationException if the business service process violates a business rule.
   * @throws SystemException if the system/application is unable to process the transaction
   * due to external conditions.
   */
  public T save(final T bean) throws ApplicationException, SystemException {
    return getDao().save(bean);
  }

}
