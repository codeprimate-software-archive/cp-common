/*
 * FossilDao.java (c) 7 December 2008
 *
 * The FossilDao (Data Access Object) interface models the basic CRUD (Create, Read, Update and Delete)
 * persistence operations on a single Bean object.  This DAO interface is independent of data source that the
 * specific implementation accesses and can be used and interchanged seamlessly to access a different data source
 * or different type of data source.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.12.23
 * @see com.cp.common.bean.Bean
 * @see com.cp.common.dao.DataAccessException
 */

package com.cp.common.dao;

import com.cp.common.beans.Bean;

public interface FossilDao<T extends Bean> {

  /**
   * Determines whether the specified bean object exists in the data source represented by this DAO.
   * @param bean the bean object being checked for existence in the data source represented by this DAO.
   * @return a boolean value indicating whether the specified bean object exists in the data source represented
   * by this DAO.
   * @throws DataAccessException if the find operation for the specified bean in the underlying data source
   * represented by this DAO fails.
   */
  public boolean find(T bean) throws DataAccessException;

  /**
   * Loads or reads all the information persisted about the specified bean object from the data source represented
   * by this DAO.  Be mindful that the read operation can be shallow or deep depending upon the DAO implementation
   * for the underlying data source.  If the specified bean is a heavy weight object with many composed objects, the
   * DAO implementation is free to lazy load the bean with only partial information.  Other DAO implementations may
   * load the entire object graph of the bean.  It really depends upon the context in which the bean object is loaded.
   * @param bean the bean object being read from the underlying data source represented by this DAO.
   * @return the bean object loaded with persisted information for the bean.  The read bean may be fully
   * or partially loaded based on the bean's composition or the context in which the bean is being loaded.
   * @throws DataAccessException if the read operation of the specified bean object from the underlying data source
   * represented by this DAO fails.
   */
  public T load(T bean) throws DataAccessException;

  /**
   * Removes or deletes the specified bean object from the underlying data source represented by this DAO.  Again, be
   * mindful whether this DAO implementation performs a cascading delete or deletes only the target bean.  If the DAO
   * implementation only deletes the target bean, certain referential integrity constraints may be violated and the
   * delete operation may fail.
   * @param bean the bean object targeted for deletion from the underlying data source represented by this DAO.
   * @return a boolean value indicating whether the delete operation for the specified bean object from the data source
   * was successful, meaning whether the data source was modified as a result of this operation.  If the caller
   * attempted to remove a non-existing bean, then the remove operation could return false instead of thrown a variant
   * of DataAccessException.
   * @throws DataAccessException if the delete operation of the specified bean object from the underlying data source
   * represented by this DAO fails.
   * @see FossilDao#save(com.cp.common.beans.Bean)
   */
  public boolean remove(T bean) throws DataAccessException;

  /**
   * Saves or creates/updates the specified bean object to the underlying data source represented by this DAO.
   * It is suggested that the save operation be intelligent enough to determine whether the bean object already exists
   * in the underlying data source.  If the bean object is new, then the DAO implementation will perform an insert into
   * the underlying data source; if the bean object had been previously persisted and has been modified, the DAO
   * implementation will update information for the specified bean object to the underlying data source.
   *
   * A particular DAO implementation may choose to use the find operation to determine whether the bean object has been
   * previously persisted to the underlying data source.  Or, alternatively, the DAO implementation can use properties
   * of the Bean interface to determmine whether the Bean has been previously persisted, which is represented by the
   * new and modified properties respectively.
   *
   * In addition, the particular DAO implementation may choose to forgo an update operation if the specified bean object
   * has not been modified when the save operation is called.
   *
   * Finally, a particular DAO implementation may choose not to persist the entire objects graph of the specified Bean
   * and only create/update the target Bean itself.  While this is not suggested or that useful, it is entirely up to
   * the particular DAO implementation.
   *
   * @param bean the bean object being persisted to the underlying data source represented by this DAO.
   * @return the specified bean object after the Bean has been persisted to the underlying data source.
   * @throws DataAccessException if the create or update operation of the specified bean object to the underlying
   * data source represented by this DAO fails.
   * @see FossilDao#remove(com.cp.common.beans.Bean)
   * @see com.cp.common.beans.Bean#isNew()
   * @see com.cp.common.beans.Bean#isModified()
   */
  public T save(T bean) throws DataAccessException;

}
