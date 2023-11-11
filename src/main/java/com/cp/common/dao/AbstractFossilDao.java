/*
 * AbstractFossilDao.java (c) 16 December 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.12.17
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.dao.DataAccessException
 * @see com.cp.common.dao.FossilDao
 */

package com.cp.common.dao;

import com.cp.common.beans.Bean;
import com.cp.common.lang.ObjectUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractFossilDao<T extends Bean> implements FossilDao<T> {

  protected final Log logger = LogFactory.getLog(getClass());

  /**
   * Finds all the Beans in the underlying data source represented by this DAO that match the criteria indicated
   * by the non-null properties of the specified bean object.
   * Note, that this implementation of find is potentially a very costly operation depending upon the underlying
   * data source.
   * @param bean the bean object who's non-null properties specify the search criteria used to match existing Beans
   * in the underlying data source.
   * @return a Set of unique identifiers of all the Beans in the underlying data source that match the criteria
   * specified by the non-null properties in the specified bean object.
   * @throws DataAccessException if the find all operation to the underlying data source represented by this DAO fails.
   * @see FossilDao#find(com.cp.common.beans.Bean)
   */
  public boolean find(final T bean) throws DataAccessException {
    try {
      return ObjectUtil.isNotNull(load(bean));
    }
    catch (DataAccessException e) {
      return false;
    }
  }

}
