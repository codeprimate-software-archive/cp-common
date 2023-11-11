/*
 * AbstractExtendedFossilDao.java (c) 16 December 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.12.23
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.dao.AbstractFossilDao
 * @see com.cp.common.dao.DataAccessException
 * @see com.cp.common.dao.ExtendedFossilDao
 */

package com.cp.common.dao;

import com.cp.common.beans.Bean;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractExtendedFossilDao<T extends Bean<I>, I extends Comparable<I>> 
  extends AbstractFossilDao<T> implements ExtendedFossilDao<T, I>
{

  /**
   * Loads or reads all Beans from the underlying data source represented by this DAO that match the criteria
   * indicated by the non-null properties of the specified bean object.
   * @param bean the bean object who's non-null properties specify the search criteria used to match existing Beans
   * in the underlying data source that will be loaded by this persistence operation.
   * @return a List of Beans read from the underlying data source that matched the criteria of the non-null
   * properties of the specified bean object.
   * @throws DataAccessException if the load all operation from the underlying data source represented by this DAO
   * fails.
   * @see com.cp.common.dao.ExtendedFossilDao#findAll(com.cp.common.beans.Bean)
   */
  public List<T> loadAll(T bean) throws DataAccessException {
    final List<T> beans = new LinkedList<T>();
    I currentId = null;

    try {
      for (final I id : findAll(bean)) {
        currentId = id;
        final T beanToLoad = (T) bean.getClass().newInstance();
        beanToLoad.setId(id);
        beans.add(load(beanToLoad));
      }
    }
    catch (DataAccessException e) {
      throw e;
    }
    catch (Exception e) {
      logger.error("Failed to load Bean having ID (" + currentId + ") from the data source!", e);
      throw new DataAccessException("Failed to load Bean having ID (" + currentId + ") from the data source!", e);
    }

    return beans;
  }

  /**
   * Removes or deletes all the specified bean objects from the underlying data source represented by this DAO.
   * @param beans the Collection of Beans being removed from the underlying data source.
   * @return a boolean value indicating whether the removal of the Bean Collection from the underlying data source was
   * successful or not.  Be aware that it is up to the particular DAO implemenations in deciding whether returning a
   * true or false value represents that all the Beans in the Collection were successfully removed or whether a
   * true or false value represents that the underlying data source was modified regardless of whether all the Beans
   * in the Collection were successfully removed or not.
   * @throws DataAccessException if the remove all operation from the underlying data source represented by this DAO
   * fails.
   * @see com.cp.common.dao.ExtendedFossilDao#saveAll(java.util.Collection)
   */
  public boolean removeAll(final Collection<T> beans) throws DataAccessException {
    boolean success = true;

    for (final T bean : beans) {
      success &= remove(bean);
    }

    return success;
  }

  /**
   * Saves or creates/updates individuall all the specified bean objects to the underlying data source represented
   * by this DAO.
   * @param beans the Collection of Beans being persisted (either inserted or updated) to the underlying data source.
   * @return the Collection of Beans after the save all operation completes.
   * @throws DataAccessException if the save all operation from the underlying data source represented by this DAO
   * fails.
   * @see com.cp.common.dao.ExtendedFossilDao#removeAll(java.util.Collection)
   */
  public List<T> saveAll(final Collection<T> beans) throws DataAccessException {
    final List<T> savedBeans = new LinkedList<T>();

    for (final T bean : beans) {
      savedBeans.add(save(bean));
    }

    return savedBeans;
  }

}
