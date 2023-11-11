/*
 * ResultSetUtil.java (c) 6 August 2003
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see com.cp.common.util.AbstractRecordFactory
 * @see com.cp.common.util.Column
 * @see com.cp.common.util.Record
 * @see com.cp.common.util.RecordTable
 * @see java.sql.ResultSet
 * @see java.sql.ResultSetMetaData
 * @see java.sql.Types
 */

package com.cp.common.sql;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.record.AbstractRecordFactory;
import com.cp.common.util.record.Column;
import com.cp.common.util.record.ColumnImpl;
import com.cp.common.util.record.Record;
import com.cp.common.util.record.RecordAdapter;
import com.cp.common.util.record.RecordTable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class ResultSetUtil {

  private static final Log logger = LogFactory.getLog(ResultSetUtil.class);

  /**
   * Private default constructor to enforce non-instantiability.
   */
  private ResultSetUtil() {
  }

  /**
   * Closed the specified ResultSet object.
   * @param rs the ResultSet object to close.
   */
  public static void closeResultSet(final ResultSet rs) {
    if (ObjectUtil.isNotNull(rs)) {
      try {
        rs.close();
      }
      catch (SQLException e) {
        logger.error("Failed to close JDBC result set!", e);
      }
    }
  }

  /**
   * Determines the Class type for the specified column class name.
   * @param columnClassName the Java Class name representing the type of values of the specified column.
   * @return a Java Class object representing the type of values contained in the specified column.
   * @see ResultSetUtil#getColumnClass(java.sql.ResultSet, int)
   */
  private static Class getColumnClass(final String columnClassName) {
    try {
      return ClassUtil.loadClass(columnClassName);
    }
    catch (ClassNotFoundException e) {
      return Object.class;
    }
  }

  /**
   * Convenience method to the the Java Class type of the column specified at index in the ResultSet.
   * @param rs the ResultSet object used to get the column's Java Class type at the specified index.
   * @param columnIndex the index of the column in the ResultSet.
   * @return a Java Class type for values in the ResultSet at the specified column index.
   * @throws SQLException if the Java Class type for the values of the column specified by index cannot be determined.
   * @see ResultSetUtil#getColumnClass(String)
   */
  public static Class getColumnClass(final ResultSet rs, final int columnIndex) throws SQLException {
    Assert.notNull(rs, "The result set cannot be null!");
    return getColumnClass(rs.getMetaData().getColumnClassName(columnIndex));
  }

  /**
   * Returns the number of columns in the ResultSet.
   * @param rs the ResultSet object used to determine the number of columns.
   * @return a integer value specifying the number of columns in the ResultSet.
   * @throws SQLException if the number of columns in the ResultSet cannot be determined.
   */
  public static int getColumnCount(final ResultSet rs) throws SQLException {
    Assert.notNull(rs, "The result set cannot be null!");
    return rs.getMetaData().getColumnCount();
  }

  /**
   * Constructs a column description using the database catalog name, schema name and table name.
   * @param catalogName the name of the database catalog.
   * @param schemaName the name of the database schema.
   * @param tableName the name of the database table.
   * @return a String description of the column using the catalog, schema and table name.
   */
  private static String getColumnDescription(final String catalogName, final String schemaName, final String tableName) {
    final StringBuffer buffer = new StringBuffer(StringUtil.toString(catalogName));
    buffer.append(".");
    buffer.append(StringUtil.toString(schemaName));
    buffer.append(".");
    buffer.append(StringUtil.toString(tableName));
    return buffer.toString();
  }

  /**
   * Gets in index of the column specified by name in the ResultSet.
   * @param rs the ResultSet object used to return the index of the column specified by name.
   * @param columnName the name of the specified column in the ResultSet.
   * @return a integer value specifying the index of the column in the ResultSet.
   * @throws SQLException if the ResultSet does not contain a column with the specified name,
   * or the names of the columns in the ResultSet cannot be retrieved!
   */
  public static int getColumnIndex(final ResultSet rs, final String columnName) throws SQLException {
    Assert.notNull(rs, "The result set cannot be null!");

    final List<String> columnNames = getColumnNames(rs);
    final int columnIndex = columnNames.indexOf(columnName);

    if (columnIndex > -1) {
      return (columnIndex + 1);
    }
    else {
      logger.warn("(" + columnName + ") is not the name of a column in the ResultSet!");
      throw new SQLException("(" + columnName + ") is not the name of a column in the ResultSet!");
    }
  }

  /**
   * Convenience method to get the name of the column at the specified index in the ResultSet.
   * @param rs the ResultSet object to retrieve the name of the column at the specified index.
   * @param columnIndex the index of the column in the ResultSet.
   * @return a String value specifying the name of the column at the specified index in the ResultSet.
   * @throws SQLException if the name of the column in the ResultSet cannot be retrieved for the specified index.
   */
  public static String getColumnName(final ResultSet rs, final int columnIndex) throws SQLException {
    Assert.notNull(rs, "The result set cannot be null!");
    return rs.getMetaData().getColumnName(columnIndex);
  }

  /**
   * Gets the name of the columns in the ResultSet object.
   * @param rs the ResultSet object to obtain column names for.
   * @return a List Collection object containing the names of the columns in the ResultSet.
   * @throws NullPointerException if the ResultSet is null.
   * @throws SQLException if the names of the columns cannot be retrieved from the ResultSet.
   */
  public static List<String> getColumnNames(final ResultSet rs) throws SQLException {
    Assert.notNull(rs, "The result set cannot be null!");

    final ResultSetMetaData metaData = rs.getMetaData();
    final int columnCount = metaData.getColumnCount();
    final List<String> columnNameList = new ArrayList<String>(columnCount);

    for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
      columnNameList.add(metaData.getColumnName(columnIndex));
    }

    return columnNameList;
  }

  /**
   * Determines whether the specified SQL type for nullable signifies whether the column can contain null values.
   * @param nullable a SQL type specifying nullity of the column.
   * @return a boolean value indicating whether the column is nullable or not (can contain null values).
   */
  private static boolean isColumnNullable(final int nullable) {
    return (nullable == ResultSetMetaData.columnNullable || nullable == ResultSetMetaData.columnNullableUnknown);
  }

  /**
   * Convenience method to get the SQL type of the column in the ResultSet specified by index.
   * @param rs the ResultSet object used to retrieve the SQL type of the specified column at index.
   * @param columnIndex the index of the column in the ResultSet.
   * @return a SQL type of the column specified by index in the ResultSet.
   * @throws SQLException if the SQL type of the column in the ResultSet specified by index cannot be determined.
   * @see java.sql.Types
   */
  public static int getColumnType(final ResultSet rs, final int columnIndex) throws SQLException {
    Assert.notNull(rs, "The result set cannot be null!");
    return rs.getMetaData().getColumnType(columnIndex);
  }

  /**
   * Returns a Map object mapping DB field to their corresponding values for one row in a particular ResultSet.
   * @param row the row of the ResultSet in which to generate the map.
   * @return a Map object containing the field value pairs for one row of the ResultSet.
   * @throws SQLException if a problem occurred while accessing the ResultSet object.
   * @throws IllegalStateException if the ResultSet object is not positioned on a valid row.
   * @see ResultSetUtil#getRecordTableFromResultSet(java.sql.ResultSet)
   */
  public static Map getMapFromResultSetRow(final ResultSet row) throws SQLException {
    Assert.state((!row.isBeforeFirst() && !row.isAfterLast()), "The result set is not positioned on a row!");

    final Map<String, Object> resultSetRow = new TreeMap<String, Object>();
    final ResultSetMetaData metaData = row.getMetaData();

    for (int columnIndex = 1, columnCount = metaData.getColumnCount(); columnIndex <= columnCount; columnIndex++) {
      final String columnName = metaData.getColumnName(columnIndex);
      final Object columnValue = row.getObject(columnName);

      if (logger.isDebugEnabled()) {
        logger.debug("column name (" + columnName + ")");
        logger.debug("column value (" + columnValue + ")");
      }

      resultSetRow.put(columnName, columnValue);
    }

    return resultSetRow;
  }

  /**
   * Creates a RecordTable object from the specified ResultSet object.  The contents of the ResultSet including
   * meta-data are used to create and initialize the RecordTable instance.
   * @param rs the ResultSet object used to populate an instance of the RecordTable class.
   * @return a RecordTable object containing the contents of the specified ResultSet object.
   * @throws SQLException if a problem occurs while reading the contents of the ResultSet object.
   * @see ResultSetUtil#getMapFromResultSetRow(java.sql.ResultSet)
   */
  public static RecordTable getRecordTableFromResultSet(final ResultSet rs) throws SQLException {
    final ResultSetMetaData metaData = rs.getMetaData();

    final Column[] columns = new Column[metaData.getColumnCount()];

    for (int columnIndex = 0; columnIndex < columns.length; columnIndex++) {
      final int column = (columnIndex + 1);

      columns[columnIndex] = new ColumnImpl(metaData.getColumnName(column),
        getColumnClass(metaData.getColumnClassName(column)));
      columns[columnIndex].setDescription(getColumnDescription(metaData.getCatalogName(column),
        metaData.getSchemaName(column), metaData.getTableName(column)));
      columns[columnIndex].setDisplayName(metaData.getColumnLabel(column));
      //columns[columnIndex].setDefaultValue();
      columns[columnIndex].setNullable(isColumnNullable(metaData.isNullable(column)));
      columns[columnIndex].setSize(metaData.getColumnDisplaySize(column));
      //columns[columnIndex].setUnique();
    }

    final RecordTable recordTable = AbstractRecordFactory.getInstance().getRecordTableInstance(columns);

    while (rs.next()) {
      final Record record = AbstractRecordFactory.getInstance().getRecordInstance();

      for (int columnIndex = 0; columnIndex < columns.length; columnIndex++) {
        record.addField(columns[columnIndex].getName(), rs.getObject(columnIndex + 1));
      }

      recordTable.addRow(new RecordAdapter(record, Arrays.<Column>asList(columns)));
    }

    return recordTable;
  }

  /**
   * Gets the value in the current row for the specified column of the ResultSet.
   * @param rs the ResultSet object to retrieve the value from.
   * @param columnIndex the index of the column in the ResultSet to retrieve the value for.
   * @return a Object value for the specified column in the current row of the ResultSet.
   * @throws SQLException if the value for the specified column in the current row of the ResultSet cannot be retrieved.
   * @see ResultSetUtil#getResultSetValue(java.sql.ResultSet, String)
   */
  public static Object getResultSetValue(final ResultSet rs, final int columnIndex) throws SQLException {
    final int sqlType = getColumnType(rs, columnIndex);

    switch (sqlType) {
      case Types.ARRAY:
        return rs.getArray(columnIndex);
      case Types.BIGINT:
        return rs.getBigDecimal(columnIndex);
      case Types.BINARY:
        return rs.getBinaryStream(columnIndex);
      case Types.BIT:
        return rs.getByte(columnIndex);
      case Types.BLOB:
        return rs.getBlob(columnIndex);
      case Types.BOOLEAN:
        return rs.getBoolean(columnIndex);
      case Types.CHAR:
        return rs.getString(columnIndex);
      case Types.CLOB:
        return rs.getClob(columnIndex);
      case Types.DATE:
        return rs.getDate(columnIndex);
      case Types.DECIMAL:
        return rs.getBigDecimal(columnIndex);
      case Types.DOUBLE:
        return rs.getDouble(columnIndex);
      case Types.FLOAT:
        return rs.getFloat(columnIndex);
      case Types.INTEGER:
        return rs.getInt(columnIndex);
      case Types.JAVA_OBJECT:
        return rs.getObject(columnIndex);
      case Types.LONGVARBINARY:
        return rs.getBinaryStream(columnIndex);
      case Types.LONGVARCHAR:
        return rs.getString(columnIndex);
      case Types.NUMERIC:
        return rs.getBigDecimal(columnIndex);
      case Types.OTHER:
        return rs.getObject(columnIndex);
      case Types.REAL:
        return rs.getBigDecimal(columnIndex);
      case Types.REF:
        return rs.getRef(columnIndex);
      case Types.SMALLINT:
        return rs.getShort(columnIndex);
      case Types.STRUCT:
        return rs.getObject(columnIndex);
      case Types.TIME:
        return rs.getTime(columnIndex);
      case Types.TIMESTAMP:
        return rs.getTimestamp(columnIndex);
      case Types.TINYINT:
        return rs.getByte(columnIndex);
      case Types.VARBINARY:
        return rs.getBinaryStream(columnIndex);
      case Types.VARCHAR:
        return rs.getString(columnIndex);
      default:
        return rs.getObject(columnIndex);
    }
  }

  /**
   * Gets the value in the current row for the specified column of the ResultSet.
   * @param rs the ResultSet object to retrieve the value from.
   * @param columnName the name of the column in the ResultSet to retrieve the value for.
   * @return a Object value for the specified column in the current row of the ResultSet.
   * @throws SQLException if the value for the specified column in the current row of the ResultSet cannot be retrieved.
   * @see ResultSetUtil#getResultSetValue(java.sql.ResultSet, int)
   */
  public static Object getResultSetValue(final ResultSet rs, final String columnName) throws SQLException {
    return getResultSetValue(rs, getColumnIndex(rs, columnName));
  }

}
