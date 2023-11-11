/*
 * ConnectionFactory.java (c) 4 March 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.10.11
 * @see java.sql.Connection
 */

package com.cp.common.sql;

import com.cp.common.util.SystemException;
import java.sql.Connection;

public interface ConnectionFactory {

  /**
   * Opens a JDBC Connection to the underlying data source.
   * @return a JDBC Connection object to the data source.
   * @throws SystemException if the JDBC Connection cannot be opened!
   * @see ConnectionFactory#closeConnection(java.sql.Connection)
   */
  public Connection openConnection() throws SystemException;

  /**
   * Closes the specified JDBC Connection to the data source.
   * @param connection the JDBC Connection to the data source to close.
   * @throws SystemException if the JDBC Connection cannot be closed!
   * @see ConnectionFactory#openConnection()
   */
  public void closeConnection(Connection connection) throws SystemException;

}
