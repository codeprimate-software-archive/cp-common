/*
 * ExtendedFossilDao.java (c) 16 December 2008
 *
 * The ExtendedFossilDao (Data Access Object) interface models the basic CRUD (Create, Read, Update and Delete)
 * persistence operations on a Collection of Bean objects.
 * 
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.12.23
 * @see com.cp.common.bean.Bean
 * @see com.cp.common.dao.DataAccessException
 * @see com.cp.common.dao.FossilDao
 */

package com.cp.common.dao;

import com.cp.common.beans.Bean;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ExtendedFossilDao<T extends Bean<I>, I extends Comparable<I>> extends FossilDao<T> {

  /**
   * Finds all the Beans in the underlying data source represented by this DAO that match the criteria indicated
   * by the non-null properties of the specified bean object.
   * @param bean the bean object who's non-null properties specify the search criteria used to match existing Beans
   * in the underlying data source.
   * @return a Set of unique identifiers of all the Beans in the underlying data source that match the criteria
   * specified by the non-null properties in the specified bean object.
   * @throws DataAccessException if the find all operation to the underlying data source represented by this DAO fails.
   * @see FossilDao#find(com.cp.common.beans.Bean)
   */
  public Set<I> findAll(T bean) throws DataAccessException;

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
  public List<T> loadAll(T bean) throws DataAccessException;

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
  public boolean removeAll(Collection<T> beans) throws DataAccessException;

  /**
   * Saves or creates/updates individuall all the specified bean objects to the underlying data source represented
   * by this DAO.
   * @param beans the Collection of Beans being persisted (either inserted or updated) to the underlying data source.
   * @return the Collection of Beans after the save all operation completes.
   * @throws DataAccessException if the save all operation from the underlying data source represented by this DAO
   * fails.
   * @see com.cp.common.dao.ExtendedFossilDao#removeAll(java.util.Collection)
   */
  public List<T> saveAll(Collection<T> beans) throws DataAccessException;

}
