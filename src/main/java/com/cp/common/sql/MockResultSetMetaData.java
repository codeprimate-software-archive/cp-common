/*
 * MockResultSetMetaData.java (c) 26 October 2003
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.7.4
 * @see com.cp.common.sql.AbstractResultSetMetaData
 */

package com.cp.common.sql;

import com.cp.common.lang.ObjectUtil;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

public class MockResultSetMetaData extends AbstractResultSetMetaData {

  private final MockResultSet resultSet;

  /**
   * Creates an instance of the MockResultSetMetaData class to represent meta data information
   * for the specified ResultSet object.
   * @param resultSet the ResultSet object used to determine meta data.
   */
  public MockResultSetMetaData(final MockResultSet resultSet) {
    if (ObjectUtil.isNull(resultSet)) {
      log.warn("The ResultSet object used to determine meta data cannot be null!");
      throw new NullPointerException("The ResultSet object used to determine meta data cannot be null!");
    }
    this.resultSet = resultSet;
  }

  /**
   * Gets the designated column's table's catalog name.
   * @param column the first column is 1, the second is 2, ...
   * @return the name of the catalog for the table in which the given column appears or "" if not applicable.
   * @throws SQLException if a database access error occurs.
   */
  public String getCatalogName(final int column) throws SQLException {
    return "";
  }

  /**
   * Returns the fully-qualified name of the Java class whose instances are manufactured if the method
   * ResultSet.getObject is called to retrieve a value from the column. ResultSet.getObject may return a subclass
   * of the class returned by this method.
   * @param column the first column is 1, the second is 2, ...
   * @return the fully-qualified name of the class in the Java programming language that would be used by the method
   * ResultSet.getObject to retrieve the value in the specified column. This is the class name used for custom mapping.
   * @throws SQLException if a database access error occurs.
   */
  public String getColumnClassName(final int column) throws SQLException {
    return Object.class.getName();
  }

  /**
   * Returns the number of columns in this ResultSet object.
   * @return the number of columns.
   * @throws SQLException if a database access error occurs.
   */
  public int getColumnCount() throws SQLException {
    return resultSet.getColumnCount();
  }

  /**
   * Indicates the designated column's normal maximum width in characters.
   * @param column the first column is 1, the second is 2, ...
   * @return the normal maximum number of characters allowed as the width of the designated column.
   * @throws SQLException if a database access error occurs.
   */
  public int getColumnDisplaySize(final int column) throws SQLException {
    return Integer.MAX_VALUE;
  }

  /**
   * Gets the designated column's suggested title for use in printouts and displays.
   * @param column the first column is 1, the second is 2, ...
   * @return the suggested column title.
   * @throws SQLException if a database access error occurs.
   */
  public String getColumnLabel(final int column) throws SQLException {
    try {
      resultSet.validateColumnIndex(column);
      return resultSet.getColumnName(column - 1);
    }
    catch (ArrayIndexOutOfBoundsException e) {
      log.error(column + " is not a valid column in the ResultSet!", e);
      throw new SQLException(column + " is not a valid column in the ResultSet!");
    }
  }

  /**
   * Get the designated column's name.
   * @param column the first column is 1, the second is 2, ...
   * @return column name.
   * @throws SQLException if a database access error occurs.
   */
  public String getColumnName(final int column) throws SQLException {
    try {
      resultSet.validateColumnIndex(column);
      return resultSet.getColumnName(column - 1);
    }
    catch (ArrayIndexOutOfBoundsException e) {
      log.error(column + " is not a valid column in the ResultSet!", e);
      throw new SQLException(column + " is not a valid column in the ResultSet!");
    }
  }

  /**
   * Retrieves the designated column's SQL type.
   * @param column the first column is 1, the second is 2, ...
   * @return SQL type from java.sql.Types.
   * @throws SQLException if a database access error occurs.
   */
  public int getColumnType(final int column) throws SQLException {
    return Types.JAVA_OBJECT;
  }

  /**
   * Retrieves the designated column's database-specific type name.
   * @param column the first column is 1, the second is 2, ...
   * @return type name used by the database. If the column type is a user-defined type, then a fully-qualified
   * type name is returned.
   * @throws SQLException if a database access error occurs.
   */
  public String getColumnTypeName(final int column) throws SQLException {
    return "JAVA_OBJECT";
  }

  /**
   * Get the designated column's table's schema.
   * @param column the first column is 1, the second is 2, ...
   * @return schema name or "" if not applicable.
   * @throws SQLException if a database access error occurs.
   */
  public String getSchemaName(final int column) throws SQLException {
    return "";
  }

  /**
   * Gets the designated column's table name.
   * @param column the first column is 1, the second is 2, ...
   * @return table name or "" if not applicable.
   * @throws SQLException if a database access error occurs.
   */
  public String getTableName(final int column) throws SQLException {
    return "";
  }

  /**
   * Indicates whether the designated column is automatically numbered, thus read-only.
   * @param column the first column is 1, the second is 2, ...
   * @return true if so; false otherwise.
   * @throws SQLException if a database access error occurs.
   */
  public boolean isAutoIncrement(final int column) throws SQLException {
    return false;
  }

  /**
   * Indicates whether a write on the designated column will definitely succeed.
   * @param column the first column is 1, the second is 2, ...
   * @return true if so; false otherwise.
   * @throws SQLException if a database access error occurs.
   */
  public boolean isDefinitelyWritable(final int column) throws SQLException {
    return true;
  }

  /**
   * Indicates the nullability of values in the designated column.
   * @param column the first column is 1, the second is 2, ...
   * @return the nullability status of the given column; one of columnNoNulls, columnNullable or columnNullableUnknown.
   * @throws SQLException if a database access error occurs.
   */
  public int isNullable(final int column) throws SQLException {
    return ResultSetMetaData.columnNullable;
  }

  /**
   * Indicates whether the designated column is definitely not writable.
   * @param column the first column is 1, the second is 2, ...
   * @return true if so; false otherwise.
   * @throws SQLException if a database access error occurs.
   */
  public boolean isReadOnly(final int column) throws SQLException {
    return false;
  }

  @Override
  public boolean isWrapperFor(final Class<?> iface) throws SQLException {
    return false;
  }

  /**
   * Indicates whether it is possible for a write on the designated column to succeed.
   * @param column the first column is 1, the second is 2, ...
   * @return true if so; false otherwise.
   * @throws SQLException if a database access error occurs.
   */
  public boolean isWritable(final int column) throws SQLException {
    return true;
  }

  @Override
  public <T> T unwrap(final Class<T> iface) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

}
