/*
 * RecordTableResultSetMetaData.java (c) 12 January 2006
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.7.4
 * @see com.cp.common.sql.AbstractResultSetMetaData
 */

package com.cp.common.sql;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.record.Column;
import java.math.BigDecimal;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecordTableResultSetMetaData extends AbstractResultSetMetaData {

  private static final Map<Class, Integer> CLASS_TO_SQLTYPE_MAP = new HashMap<Class, Integer>();
  private static final Map<Class, String> CLASS_TO_SQLTYPENAME_MAP = new HashMap<Class, String>();

  static {
    CLASS_TO_SQLTYPE_MAP.put(Boolean.class, Types.BOOLEAN);
    CLASS_TO_SQLTYPE_MAP.put(Byte.class, Types.TINYINT);
    CLASS_TO_SQLTYPE_MAP.put(Calendar.class, Types.TIMESTAMP);
    CLASS_TO_SQLTYPE_MAP.put(Character.class, Types.CHAR);
    CLASS_TO_SQLTYPE_MAP.put(Date.class, Types.DATE);
    CLASS_TO_SQLTYPE_MAP.put(Double.class, Types.DOUBLE);
    CLASS_TO_SQLTYPE_MAP.put(Float.class, Types.FLOAT);
    CLASS_TO_SQLTYPE_MAP.put(Integer.class, Types.INTEGER);
    CLASS_TO_SQLTYPE_MAP.put(Long.class, Types.NUMERIC);
    CLASS_TO_SQLTYPE_MAP.put(Object.class, Types.JAVA_OBJECT);
    CLASS_TO_SQLTYPE_MAP.put(Short.class, Types.SMALLINT);
    CLASS_TO_SQLTYPE_MAP.put(String.class, Types.VARCHAR);
    CLASS_TO_SQLTYPENAME_MAP.put(Boolean.class, "BOOLEAN");
    CLASS_TO_SQLTYPENAME_MAP.put(Byte.class, "NUMBER");
    CLASS_TO_SQLTYPENAME_MAP.put(Calendar.class, "TIMESTAMP");
    CLASS_TO_SQLTYPENAME_MAP.put(Character.class, "CHAR");
    CLASS_TO_SQLTYPENAME_MAP.put(Date.class, "DATE");
    CLASS_TO_SQLTYPENAME_MAP.put(Double.class, "NUMBER");
    CLASS_TO_SQLTYPENAME_MAP.put(Float.class, "NUMBER");
    CLASS_TO_SQLTYPENAME_MAP.put(Integer.class, "NUMBER");
    CLASS_TO_SQLTYPENAME_MAP.put(Long.class, "NUMBER");
    CLASS_TO_SQLTYPENAME_MAP.put(Object.class, "JAVA_OBJECT");
    CLASS_TO_SQLTYPENAME_MAP.put(Short.class, "NUMBER");
    CLASS_TO_SQLTYPENAME_MAP.put(String.class, "VARCHAR");
  }

  private final RecordTableResultSet resultSet;

  /**
   * Creates an instance of the RecordTableResultSetMetaData class to represent and provide meta-data
   * for the specified RecordTableResultSet object.
   * @param resultSet the RecordTableResultSet object for which meta-data is provided.
   */
  public RecordTableResultSetMetaData(final RecordTableResultSet resultSet) {
    if (ObjectUtil.isNull(resultSet)) {
      log.warn("The ResultSet used to determine meta data cannot be null!");
      throw new NullPointerException("The ResultSet used to determine meta data cannot be null!");
    }
    this.resultSet = resultSet;
  }

  /**
   * Gets the designated column's table's catalog name.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the name of the catalog for the table in which the given column appears or "" if not applicable.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public String getCatalogName(final int columnIndex) throws SQLException {
    return "";
  }

  /**
   * Returns the fully-qualified name of the Java class whose instances are manufactured if the method
   * ResultSet.getObject is called to retrieve a value from the column. ResultSet.getObject may return a subclass of
   * the class returned by this method.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the fully-qualified name of the class in the Java programming language that would be used by the method
   * ResultSet.getObject to retrieve the value in the specified column. This is the class name used for custom mapping.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public String getColumnClassName(final int columnIndex) throws SQLException {
    try {
      return resultSet.getRecordTable().getColumn(columnIndex - 1).getType().getName();
    }
    catch (IndexOutOfBoundsException e) {
      log.error("(" + columnIndex + ") is not a valid column index in this ResultSet!", e);
      throw new SQLException("(" + columnIndex + ") is not a valid column index in this ResultSet!");
    }
  }

  /**
   * Returns the number of columns in this ResultSet object.
   * @return the number of columns.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public int getColumnCount() throws SQLException {
    return resultSet.getRecordTable().columnCount();
  }

  /**
   * Indicates the designated column's normal maximum width in characters.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the normal maximum number of characters allowed as the width of the designated column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public int getColumnDisplaySize(final int columnIndex) throws SQLException {
    try {
      return resultSet.getRecordTable().getColumn(columnIndex - 1).getSize();
    }
    catch (IndexOutOfBoundsException e) {
      log.error("(" + columnIndex + ") is not a valid column index in this ResultSet!", e);
      throw new SQLException("(" + columnIndex + ") is not a valid column index in this ResultSet!");
    }
  }

  /**
   * Gets the designated column's suggested title for use in printouts and displays.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the suggested column title.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public String getColumnLabel(final int columnIndex) throws SQLException {
    try {
      return resultSet.getRecordTable().getColumn(columnIndex - 1).getDisplayName();
    }
    catch (IndexOutOfBoundsException e) {
      log.error("(" + columnIndex + ") is not a valid column index in this ResultSet!", e);
      throw new SQLException("(" + columnIndex + ") is not a valid column index in this ResultSet!");
    }
  }

  /**
   * Get the designated column's name.
   * @param columnIndex the index of the column in the ResultSet.
   * @return column name.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public String getColumnName(final int columnIndex) throws SQLException {
    try {
      return resultSet.getRecordTable().getColumn(columnIndex - 1).getName();
    }
    catch (IndexOutOfBoundsException e) {
      log.error("(" + columnIndex + ") is not a valid column index in this ResultSet!", e);
      throw new SQLException("(" + columnIndex + ") is not a valid column index in this ResultSet!");
    }
  }

  /**
   * Retrieves the designated column's SQL type.
   * @param columnIndex the index of the column in the ResultSet.
   * @return SQL type from java.sql.Types.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public int getColumnType(final int columnIndex) throws SQLException {
    try {
      final Class columnType = resultSet.getRecordTable().getColumn(columnIndex - 1).getType();

      for (Class type : CLASS_TO_SQLTYPE_MAP.keySet()) {
        if (type.isAssignableFrom(columnType)) {
          return CLASS_TO_SQLTYPE_MAP.get(type);
        }
      }

      return Types.JAVA_OBJECT;
    }
    catch (IndexOutOfBoundsException e) {
      log.error("(" + columnIndex + ") is not a valid column index in this ResultSet!", e);
      throw new SQLException("(" + columnIndex + ") is not a valid column index in this ResultSet!");
    }
  }

  /**
   * Retrieves the designated column's database-specific type name.
   * @param columnIndex the index of the column in the ResultSet.
   * @return type name used by the database. If the column type is a user-defined type, then a fully-qualified
   * type name is returned.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public String getColumnTypeName(final int columnIndex) throws SQLException {
    try {
      final Class columnType = resultSet.getRecordTable().getColumn(columnIndex - 1).getType();

      for (Class type : CLASS_TO_SQLTYPENAME_MAP.keySet()) {
        if (type.isAssignableFrom(columnType)) {
          return CLASS_TO_SQLTYPENAME_MAP.get(type);
        }
      }

      return "JAVA_OBJECT";
    }
    catch (IndexOutOfBoundsException e) {
      log.error("(" + columnIndex + ") is not a valid column index in this ResultSet!", e);
      throw new SQLException("(" + columnIndex + ") is not a valid column index in this ResultSet!");
    }
  }

  /**
   * Get the designated column's table's schema.
   * @param columnIndex the index of the column in the ResultSet.
   * @return schema name or "" if not applicable.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public String getSchemaName(final int columnIndex) throws SQLException {
    return "";
  }

  /**
   * Gets the designated column's table name.
   * @param columnIndex the index of the column in the ResultSet.
   * @return table name or "" if not applicable.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public String getTableName(final int columnIndex) throws SQLException {
    return "";
  }

  /**
   * Indicates whether the designated column is automatically numbered, thus read-only.
   * @param column the first column is 1, the second is 2, ...
   * @return true if so; false otherwise.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean isAutoIncrement(final int column) throws SQLException {
    return false;
  }

  /**
   * Indicates whether a column's case matters.
   * @param columnIndex the index of the column in the ResultSet.
   * @return true if so; false otherwise.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean isCaseSensitive(final int columnIndex) throws SQLException {
    try {
      final Column column = resultSet.getRecordTable().getColumn(columnIndex - 1);
      return  (String.class.equals(column.getType()) || Character.class.equals(column.getType()));
    }
    catch (IndexOutOfBoundsException e) {
      log.error("(" + columnIndex + ") is not a valid column index in this ResultSet!", e);
      throw new SQLException("(" + columnIndex + ") is not a valid column index in this ResultSet!");
    }
  }

  /**
   * Indicates whether the designated column is a cash value.
   * @param columnIndex the index of the column in the ResultSet.
   * @return true if so; false otherwise.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean isCurrency(final int columnIndex) throws SQLException {
    try {
      final Column column = resultSet.getRecordTable().getColumn(columnIndex - 1);
      return BigDecimal.class.equals(column.getType());
    }
    catch (IndexOutOfBoundsException e) {
      log.error("(" + columnIndex + ") is not a valid column index in this ResultSet!", e);
      throw new SQLException("(" + columnIndex + ") is not a valid column index in this ResultSet!");
    }
  }

  /**
   * Indicates whether a write on the designated column will definitely succeed.
   * @param column the first column is 1, the second is 2, ...
   * @return true if so; false otherwise.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean isDefinitelyWritable(final int column) throws SQLException {
    return resultSet.getRecordTable().isMutable();
  }

  /**
   * Indicates the nullability of values in the designated column.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the nullability status of the given column; one of columnNoNulls, columnNullable or columnNullableUnknown.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public int isNullable(final int columnIndex) throws SQLException {
    try {
      final boolean nullable = resultSet.getRecordTable().getColumn(columnIndex - 1).isNullable();
      return (nullable ? ResultSetMetaData.columnNullable : ResultSetMetaData.columnNoNulls);
    }
    catch (IndexOutOfBoundsException e) {
      log.error("(" + columnIndex + ") is not a valid column index in this ResultSet!", e);
      throw new SQLException("(" + columnIndex + ") is not a valid column index in this ResultSet!");
    }
  }

  /**
   * Indicates whether the designated column is definitely not writable.
   * @param column the first column is 1, the second is 2, ...
   * @return true if so; false otherwise.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean isReadOnly(final int column) throws SQLException {
    return !resultSet.getRecordTable().isMutable();
  }

  /**
   * Indicates whether the designated column can be used in a where clause.
   * @param column the first column is 1, the second is 2, ...
   * @return true if so; false otherwise.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean isSearchable(final int column) throws SQLException {
    return true;
  }

  /**
   * Indicates whether values in the designated column are signed numbers.
   * @param columnIndex the index of the column in the ResultSet.
   * @return true if so; false otherwise.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean isSigned(final int columnIndex) throws SQLException {
    try {
      final Class columnType = resultSet.getRecordTable().getColumn(columnIndex - 1).getType();
      return Number.class.isAssignableFrom(columnType);
    }
    catch (IndexOutOfBoundsException e) {
      log.error("(" + columnIndex + ") is not a valid column index in this ResultSet!", e);
      throw new SQLException("(" + columnIndex + ") is not a valid column index in this ResultSet!");
    }
  }

  @Override
  public boolean isWrapperFor(final Class<?> iface) throws SQLException {
    return false;
  }

  /**
   * Indicates whether it is possible for a write on the designated column to succeed.
   * @param column the first column is 1, the second is 2, ...
   * @return true if so; false otherwise.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean isWritable(final int column) throws SQLException {
    return resultSet.getRecordTable().isMutable();
  }

  @Override
  public <T> T unwrap(final Class<T> iface) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

}
