/*
 * AbstractDataSource.java (c) 6 September 2009
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author jblum
 * @version 2010.7.4
 * @see javax.sql.DataSource
 */

package com.cp.common.sql;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractDataSource implements DataSource {

  protected final Log log = LogFactory.getLog(getClass());

  private int loginTimeout = 0;

  /**
   * Gets the maximum time in seconds that this data source can wait while attempting to connect to a database.
   * A value of zero means that the timeout is the default system timeout if there is one; otherwise, it means
   * that there is no timeout.  When a DataSource object is created, the login timeout is initially zero.
   * @return the data source login time limit.
   * @throws SQLException if a database access error occurs.
   */
  @Override
  public int getLoginTimeout() throws SQLException {
    return loginTimeout;
  }

  /**
   * Sets the maximum time in seconds that this data source will wait while attempting to connect to a database.
   * A value of zero specifies that the timeout is the default system timeout if there is one; otherwise, it specifies
   * that there is no timeout. When a DataSource object is created, the login timeout is initially zero.
   * @param seconds the data source login time limit.
   * @throws SQLException if a database access error occurs.
   */
  @Override
  public void setLoginTimeout(final int seconds) throws SQLException {
    this.loginTimeout = seconds;
  }

  /**
   * Retrieves the log writer for this DataSource object.  The log writer is a character output stream to which all
   * logging and tracing messages for this data source will be printed.  This includes messages printed by the methods
   * of this object, messages printed by methods of other objects manufactured by this object, and so on.   Messages
   * printed to a data source specific log writer are not printed to the log writer associated with the
   * java.sql.Drivermanager class.  When a DataSource object is created, the log writer is initially null;
   * in other words, the default is for logging to be disabled.
   * @return the log writer for this data source or null if logging is disabled.
   * @throws SQLException if a database access error occurs
   */
  @Override
  public PrintWriter getLogWriter() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Sets the log writer for this DataSource object to the given java.io.PrintWriter object.  The log writer is a
   * character output stream to which all logging and tracing messages for this data source will be printed.  This
   * includes messages printed by the methods of this object, messages printed by methods of other objects manufactured
   * by this object, and so on. Messages printed to a data source- specific log writer are not printed to the
   * log writer associated with the java.sql.Drivermanager class.  When a DataSource object is created the log writer
   * is initially null; in other words, the default is for logging to be disabled.
   * @param out the new log writer; to disable logging, set to null.
   * @throws SQLException if a database access error occurs.
   */
  @Override
  public void setLogWriter(final PrintWriter out) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Returns true if this either implements the interface argument or is directly or indirectly a wrapper for an object
   * that does. Returns false otherwise. If this implements the interface then return true, else if this is a wrapper
   * then return the result of recursively calling isWrapperFor on the wrapped object. If this does not implement the
   * interface and is not a wrapper, return false. This method should be implemented as a low-cost operation compared
   * to unwrap  so that callers can use this method to avoid expensive unwrap calls that may fail. If this method
   * returns true then calling unwrap with the same argument should succeed.
   * @param iface a Class defining an interface.
   * @return true if this implements the interface or directly or indirectly wraps an object that does.
   * @throws SQLException if an error occurs while determining whether this is a wrapper for an object with the
   * given interface.
   */
  @Override
  public boolean isWrapperFor(final Class<?> iface) throws SQLException {
    return false;
  }

  /**
   * Returns an object that implements the given interface to allow access to non-standard methods, or standard methods
   * not exposed by the proxy. If the receiver implements the interface then the result is the receiver or a proxy for
   * the receiver. If the receiver is a wrapper and the wrapped object implements the interface then the result is
   * the wrapped object or a proxy for the wrapped object. Otherwise return the the result of calling unwrap recursively
   * on the wrapped object or a proxy for that result. If the receiver is not a wrapper and does not implement the
   * interface, then an SQLException is thrown.
   * @param iface a Class defining an interface that the result must implement.
   * @return an object that implements the interface. May be a proxy for the actual implementing object.
   * @throws SQLException if no object found that implements the interface.
   */
  @Override
  public <T> T unwrap(final Class<T> iface) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * @inheritDoc
   */
  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    throw new UnsupportedOperationException("Not Implemented!");
  }
}
