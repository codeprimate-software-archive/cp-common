/*
 * AbstractResultSetMetaData.java (c) 04 July 2010
 *
 * Copyright (c) 2010, Codeprimate LLC
 * All Rights Reserved
 * @author jblum
 * @version 2010.07.04
 * @see java.sql.ResultSetMetaData
 */

package com.cp.common.sql;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractResultSetMetaData implements ResultSetMetaData {

  protected final Log log = LogFactory.getLog(getClass());

  @Override public int getColumnCount() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean isAutoIncrement(final int column) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Indicates whether a column's case matters.
   * @param column the first column is 1, the second is 2, ...
   * @return true if so; false otherwise.
   * @throws SQLException if a database access error occurs.
   */
  @Override public boolean isCaseSensitive(final int column) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Indicates whether the designated column can be used in a where clause.
   * @param column the first column is 1, the second is 2, ...
   * @return true if so; false otherwise.
   * @throws SQLException if a database access error occurs.
   */
  @Override public boolean isSearchable(final int column) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Indicates whether the designated column is a cash value.
   * @param column the first column is 1, the second is 2, ...
   * @return true if so; false otherwise.
   * @throws SQLException if a database access error occurs.
   */
  @Override public boolean isCurrency(final int column) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public int isNullable(final int column) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Indicates whether values in the designated column are signed numbers.
   * @param column the first column is 1, the second is 2, ...
   * @return true if so; false otherwise.
   * @throws SQLException if a database access error occurs.
   */
  @Override public boolean isSigned(final int column) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public int getColumnDisplaySize(final int column) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public String getColumnLabel(final int column) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public String getColumnName(final int column) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public String getSchemaName(final int column) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Get the designated column's number of decimal digits.
   * @param column the first column is 1, the second is 2, ...
   * @return precision.
   * @throws SQLException if a database access error occurs.
   */
  @Override public int getPrecision(final int column) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Gets the designated column's number of digits to right of the decimal point.
   * @param column the first column is 1, the second is 2, ...
   * @return scale.
   * @throws SQLException if a database access error occurs.
   */
  @Override public int getScale(final int column) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public String getTableName(final int column) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public String getCatalogName(final int column) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public int getColumnType(final int column) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public String getColumnTypeName(final int column) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean isReadOnly(final int column) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean isWritable(final int column) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean isDefinitelyWritable(final int column) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public String getColumnClassName(final int column) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public <T> T unwrap(final Class<T> iface) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean isWrapperFor(final Class<?> iface) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

}
