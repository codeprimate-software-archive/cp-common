/*
 * MockConnection.java (c) 13 October 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.10.13
 * @see com.cp.common.lang.Resettable
 * @see java.sql.Connection
 */

package com.cp.common.sql;

import com.cp.common.lang.Resettable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;

// TODO finish implementation
public class MockConnection implements Connection, Resettable {

  private boolean autoCommit = true;
  private boolean closed = false;
  private boolean commitCalled = false;
  private boolean readOnly = false;
  private boolean rollbackCalled = false;

  public void reset() {
    autoCommit = true;
    commitCalled = false;
    readOnly = false;
    rollbackCalled = false;
  }

  public boolean getAutoCommit() throws SQLException {
    return autoCommit;
  }

  public void setAutoCommit(final boolean autoCommit) throws SQLException {
    this.autoCommit = autoCommit;
  }

  public String getCatalog() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public void setCatalog(final String catalog) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  public boolean isClosed() throws SQLException {
    return closed;
  }

  public DatabaseMetaData getMetaData() throws SQLException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public boolean isReadOnly() throws SQLException {
    return readOnly;
  }

  public void setReadOnly(final boolean readOnly) throws SQLException {
    this.readOnly = readOnly;
  }

  public void close() throws SQLException {
    closed = true;
  }

  public void commit() throws SQLException {
    commitCalled = true;
  }

  public Statement createStatement() throws SQLException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public String nativeSQL(String sql) throws SQLException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public CallableStatement prepareCall(String sql) throws SQLException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public PreparedStatement prepareStatement(String sql) throws SQLException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public void rollback() throws SQLException {
    rollbackCalled = true;
  }

  public void setTransactionIsolation(int level) throws SQLException {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public int getTransactionIsolation() throws SQLException {
    return 0;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public SQLWarning getWarnings() throws SQLException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public void clearWarnings() throws SQLException {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public Map<String, Class<?>> getTypeMap() throws SQLException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public void setHoldability(int holdability) throws SQLException {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public int getHoldability() throws SQLException {
    return 0;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Savepoint setSavepoint() throws SQLException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Savepoint setSavepoint(String name) throws SQLException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public void rollback(Savepoint savepoint) throws SQLException {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public PreparedStatement prepareStatement(String sql, int columnIndexes[]) throws SQLException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public PreparedStatement prepareStatement(String sql, String columnNames[]) throws SQLException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

}
