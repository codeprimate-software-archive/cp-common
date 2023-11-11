/*
 * RecordTableResultSet.java (c) 12 January 2006
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.7.4
 * @see com.cp.common.sql.AbstractResultSet
 * @see com.cp.common.util.record.RecordTable
 */

package com.cp.common.sql;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import com.cp.common.lang.NumberUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.record.RecordTable;

public class RecordTableResultSet extends AbstractResultSet {

  private boolean closed = false;
  private boolean rowDeleted = false;
  private boolean rowInserted = false;
  private boolean rowUpdated = false;

  private int currentRow = -1;

  private Object lastColumnValueRead;

  private final RecordTable recordTable;

  private SQLWarning warnings = null;

  /**
   * Creates an instance of the RecordTableResultSet class representing and adapting the interface
   * of the specified RecordTable object.
   * @param recordTable the RecordTable object wrapped by this ResultSet instance.
   */
  public RecordTableResultSet(final RecordTable recordTable) {
    if (ObjectUtil.isNull(recordTable)) {
      log.warn("The RecordTable for this ResultSet cannot be null!");
      throw new NullPointerException("The RecordTable for this ResultSet cannot be null!");
    }
    this.recordTable = recordTable;
  }

  /**
   * Appends the specified SQL Warning to the beginning of the SQL Warning chain.
   * @param warning the SQL Warning object added to the beginning of the chain.
   */
  final void addWarning(final SQLWarning warning) {
    if (ObjectUtil.isNotNull(warnings)) {
      warning.setNextWarning(warnings);
    }
    warnings = warning;
  }

  /**
   * Retrieves the value for the column specified by column index for the current row in this ResultSet.
   * @param columnIndex the index of the column in the ResultSet for which the value should be returned.
   * @return the value of column at index for the current row in this ResultSet.
   * @throws SQLException if the value for the column at index in the current row of the ResultSet
   * cannot be read.
   */
  private <T> T getColumnValue(final int columnIndex) throws SQLException {
    try {
      verifyNotClosed();
      verifyValidRow();
      lastColumnValueRead = getRecordTable().getCellValue(getCurrentRow(), columnIndex);
      return (T) lastColumnValueRead;
    }
    catch (IndexOutOfBoundsException e) {
      log.error("(" + columnIndex + ") is not a valid column index in this ResultSet!", e);
      throw new SQLException("(" + columnIndex + ") is not a valid column index in this ResultSet!");
    }
  }

  /**
   * Retrieves the value for the column specified by name for the current row in this ResultSet.
   * @param columnName the nanme of the column in the ResultSet for which the value should be returned.
   * @return the value of column with name for the current row in this ResultSet.
   * @throws SQLException if the value for the column with name in the current row of the ResultSet
   * cannot be read.
   */
  private <T> T getColumnValue(final String columnName) throws SQLException {
    try {
      verifyNotClosed();
      verifyValidRow();
      lastColumnValueRead = getRecordTable().getCellValue(getCurrentRow(), getRecordTable().getColumn(columnName));
      return (T) lastColumnValueRead;
    }
    catch (IndexOutOfBoundsException e) {
      log.error("(" + columnName + ") is not a valid column in this ResultSet!", e);
      throw new SQLException("(" + columnName + ") is not a valid column in this ResultSet!");
    }
  }

  /**
   * Sets the value for the specified column at index for the current row in this ResultSet.
   * @param columnIndex the index of the column in the ResultSet where the value will be set.
   * @param value the value to set on the column at index in the current row of the ResultSet.
   * @throws SQLException if the value cannot be set on the column at index for the current row of the ResultSet.
   */
  private void setColumnValue(final int columnIndex, final Object value) throws SQLException {
    try {
      verifyNotClosed();
      verifyValidRow();
      getRecordTable().setCellValue(getCurrentRow(), columnIndex, value);
    }
    catch (IndexOutOfBoundsException e) {
      log.error("(" + columnIndex + ") is not a valid column index in this ResultSet!", e);
      throw new SQLException("(" + columnIndex + ") is not a valid column index in this ResultSet!");
    }
  }

  /**
   * Sets the value of the column specified by name in the current row of the ResultSet.
   * @param columnName the name of the column in the ResultSet where the value will be set.
   * @param value the value to set on the column with name in the current row of the ResultSet.
   * @throws SQLException if the value cannot be set on the column with name for the current row of the ResultSet.
   */
  private void setColumnValue(final String columnName, final Object value) throws SQLException {
    try {
      verifyNotClosed();
      verifyValidRow();
      getRecordTable().setCellValue(getCurrentRow(), getRecordTable().getColumn(columnName), value);
    }
    catch (IndexOutOfBoundsException e) {
      log.error("(" + columnName + ") is not a valid column in this ResultSet!", e);
      throw new SQLException("(" + columnName + ") is not a valid column in this ResultSet!");
    }
  }

  /**
   * Returns the row index of the current row in the ResultSet.
   * @return an integer value specifying the row index of the current row in the ResultSet.
   */
  private int getCurrentRow() {
    return currentRow;
  }

  /**
   * Sets the row index of the current row in the ResultSet.
   * @param currentRow an integer value specifying the row index of the current row in the ResultSet.
   */
  private void setCurrentRow(final int currentRow) {
    this.currentRow = Math.max(-1, Math.min(getRecordTable().rowCount(), currentRow));
  }

  /**
   * Positions the current row on the previous row.
   */
  private void decrementCurrentRow() {
    setCurrentRow(currentRow - 1);
  }

  /**
   * Positions the current row on the next row.
   */
  private void incrementCurrentRow() {
    setCurrentRow(currentRow + 1);
  }

  /**
   * Returns the underlying RecordTable object represented and adapted by this ResultSet class.
   * @return the underlying RecordTable object supporting the ResultSet implementation.
   */
  final RecordTable getRecordTable() {
    return recordTable;
  }

  /**
   * Verifies that the close operation has not been called on the ResultSet object.
   * @throws SQLException if the ResultSet has been closed!
   */
  private void verifyNotClosed() throws SQLException {
    if (closed) {
      log.warn("The ResultSet is closed!");
      throw new SQLException("The ResultSet is closed!");
    }
  }

  /**
   * Validates that the cursor (current row index) is positioned on a valid row in the ResultSet.
   * @throws SQLException if the cursor is not positioned on a valid row in the ResultSet.
   */
  private void verifyValidRow() throws SQLException {
    if (isBeforeFirst() || isAfterLast()) {
      log.warn("The cursor is not positioned on a valid row in the ResultSet!");
      throw new SQLException("The cursor is not positioned on a valid row in the ResultSet!");
    }
  }

  /**
   * Moves the cursor to the given row number in this ResultSet object.
   *
   * If the row number is positive, the cursor moves to the given row number with respect to the beginning of the
   * result set. The first row is row 1, the second is row 2, and so on.
   *
   * If the given row number is negative, the cursor moves to an absolute row position with respect to the end of the
   * result set. For example, calling the method absolute(-1) positions the cursor on the last row; calling the method
   * absolute(-2) moves the cursor to the next-to-last row, and so on.
   *
   * An attempt to position the cursor beyond the first/last row in the result set leaves the cursor before the first
   * row or after the last row.
   *
   * Note: Calling absolute(1) is the same as calling first(). Calling absolute(-1) is the same as calling last().
   * @param row the number of the row to which the cursor should move. A positive number indicates the row number
   * counting from the beginning of the result set; a negative number indicates the row number counting from the
   * end of the result set
   * @return true if the cursor is on the result set; false otherwise.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean absolute(int row) throws SQLException {
    verifyNotClosed();
    final int origin = (NumberUtil.isGreaterThanEqualTo(0, row) ? 0 : getRecordTable().rowCount());
    row += (NumberUtil.isPositive(row) ? -1 : 0);
    setCurrentRow(origin + row);
    return (!isBeforeFirst() && !isAfterLast());
  }

  /**
   * Moves the cursor to the end of this ResultSet object, just after the last row. This method has no effect if the
   * result set contains no rows.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void afterLast() throws SQLException {
    verifyNotClosed();
    setCurrentRow(getRecordTable().rowCount());
  }

  /**
   * Moves the cursor to the front of this ResultSet object, just before the first row. This method has no effect if
   * the result set contains no rows.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void beforeFirst() throws SQLException {
    verifyNotClosed();
    setCurrentRow(-1);
  }

  /**
   * Clears all warnings reported on this ResultSet object. After this method is called, the method getWarnings
   * returns null until a new warning is reported for this ResultSet object.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void clearWarnings() throws SQLException {
    verifyNotClosed();
    warnings = null;
  }

  /**
   * Releases this ResultSet object's database and JDBC resources immediately instead of waiting for this to happen
   * when it is automatically closed.
   *
   * Note: A ResultSet object is automatically closed by the Statement object that generated it when that Statement
   * object is closed, re-executed, or is used to retrieve the next result from a sequence of multiple results. A
   * ResultSet object is also automatically closed when it is garbage collected.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void close() throws SQLException {
    closed = true;
  }

  /**
   * Deletes the current row from this ResultSet object and from the underlying database. This method cannot be
   * called when the cursor is on the insert row.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void deleteRow() throws SQLException {
    verifyNotClosed();
    verifyValidRow();
    getRecordTable().removeRow(getCurrentRow());
    rowDeleted = true;
  }

  /**
   * Maps the given ResultSet column name to its ResultSet column index.
   * @param columnName the name of the column.
   * @return the column index of the given column name.
   * @throws SQLException if the ResultSet object does not contain columnName or an error occurs accessing
   * the record table.
   */
  public int findColumn(final String columnName) throws SQLException {
    verifyNotClosed();
    final com.cp.common.util.record.Column column = getRecordTable().getColumn(columnName);
    if (ObjectUtil.isNotNull(column)) {
      return getRecordTable().getColumnIndex(column);
    }
    throw new SQLException("The ResultSet does not contain column (" + columnName + ")!");
  }

  /**
   * Moves the cursor to the first row in this ResultSet object.
   * @return true if the cursor is on a valid row; false if there are no rows in the result set.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean first() throws SQLException {
    verifyNotClosed();
    setCurrentRow(0);
    return (!isBeforeFirst() && !isAfterLast());
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as an Array object
   * in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return an Array object representing the SQL ARRAY value in the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Array getArray(final int columnIndex) throws SQLException {
    return getColumnValue(columnIndex - 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as an Array object
   * in the Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return an Array object representing the SQL ARRAY value in the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Array getArray(final String columnName) throws SQLException {
    return getColumnValue(columnName);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.math.BigDecimal
   * with full precision.
   * @param columnIndex the index of the column in this ResultSet.
   * @return the column value (full precision); if the value is SQL NULL, the value returned is null in the
   * Java programming language.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public BigDecimal getBigDecimal(final int columnIndex) throws SQLException {
    return getColumnValue(columnIndex - 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.math.BigDecimal
   * with full precision.
   * @param columnIndex the index of the column in this ResultSet.
   * @param scale the number of digits to the right of the decimal point.
   * @return the column value (full precision); if the value is SQL NULL, the value returned is null in the
   * Java programming language.
   * @throws SQLException if an error occurs accessing the record table.
   * @deprecated use getBigDecimal(columnIndex:int) instead!
   */
  @Deprecated public BigDecimal getBigDecimal(final int columnIndex, final int scale) throws SQLException {
    return getColumnValue(columnIndex - 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.math.BigDecimal
   * with full precision.
   * @param columnName the name of the column in this ResultSet.
   * @return the column value (full precision); if the value is SQL NULL, the value returned is null in the
   * Java programming language.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public BigDecimal getBigDecimal(final String columnName) throws SQLException {
    return getColumnValue(columnName);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.math.BigDecimal
   * with full precision.
   * @param columnName the name of the column in this ResultSet.
   * @param scale the number of digits to the right of the decimal point.
   * @return the column value (full precision); if the value is SQL NULL, the value returned is null in the
   * Java programming language.
   * @throws SQLException if an error occurs accessing the record table.
   * @deprecated use getBigDecimal(columnName:String) instead!
   */
  @Deprecated public BigDecimal getBigDecimal(final String columnName, final int scale) throws SQLException {
    return getColumnValue(columnName);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a Blob object
   * in the Java programming language
   * @param columnIndex the index of the column in the ResultSet.
   * @return a Blob object representing the SQL BLOB value in the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Blob getBlob(final int columnIndex) throws SQLException {
    return getColumnValue(columnIndex - 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a Blob object
   * in the Java programming language
   * @param columnName the name of the column in the ResultSet.
   * @return a Blob object representing the SQL BLOB value in the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Blob getBlob(final String columnName) throws SQLException {
    return getColumnValue(columnName);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a boolean
   * in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is false.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean getBoolean(final int columnIndex) throws SQLException {
    final Boolean condition = getColumnValue(columnIndex - 1);
    return condition;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a boolean
   * in the Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is false.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean getBoolean(final String columnName) throws SQLException {
    final Boolean condition = getColumnValue(columnName);
    return condition;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a byte
   * in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public byte getByte(final int columnIndex) throws SQLException {
    final Byte value = getColumnValue(columnIndex - 1);
    return value;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a byte
   * in the Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public byte getByte(final String columnName) throws SQLException {
    final Byte value = getColumnValue(columnName);
    return value;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a byte array
   * in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public byte[] getBytes(final int columnIndex) throws SQLException {
    final Object byteArray = getColumnValue(columnIndex - 1);
    return (byte[]) byteArray;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a byte array
   * in the Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public byte[] getBytes(final String columnName) throws SQLException {
    final Object byteArray = getColumnValue(columnName);
    return (byte[]) byteArray;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a Clob object
   * in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return a Clob object representing the SQL CLOB value in the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Clob getClob(final int columnIndex) throws SQLException {
    return getColumnValue(columnIndex - 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a Clob object
   * in the Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return a Clob object representing the SQL CLOB value in the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Clob getClob(final String columnName) throws SQLException {
    return getColumnValue(columnName);
  }

  /**
   * Retrieves the concurrency mode of this ResultSet object. The concurrency used is determined by the Statement
   * object that created the result set
   * @return the concurrency type, ResultSet.CONCUR_UPDATABLE.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public int getConcurrency() throws SQLException {
    return ResultSet.CONCUR_UPDATABLE;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.sql.Date object
   * in the Java programming language
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Date getDate(final int columnIndex) throws SQLException {
    return getColumnValue(columnIndex - 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.sql.Date object
   * in the Java programming language. This method uses the given calendar to construct an appropriate millisecond
   * value for the date if the underlying database does not store timezone information.
   * @param columnIndex the index of the column in the ResultSet.
   * @param calendar the java.util.Calendar object to use in constructing the date.
   * @return the column value as a java.sql.Date object; if the value is SQL NULL, the value returned is null
   * in the Java programming language.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Date getDate(final int columnIndex, final Calendar calendar) throws SQLException {
    Date date = getDate(columnIndex);

    if (ObjectUtil.isNotNull(date) && ObjectUtil.isNotNull(calendar)) {
      final Calendar newDate = Calendar.getInstance();
      newDate.setTimeInMillis(date.getTime());
      newDate.setTimeZone(calendar.getTimeZone());
      date = new Date(newDate.getTimeInMillis());
    }

    return date;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.sql.Date object
   * in the Java programming language
   * @param columnName the name of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Date getDate(final String columnName) throws SQLException {
    return getColumnValue(columnName);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.sql.Date object
   * in the Java programming language. This method uses the given calendar to construct an appropriate millisecond
   * value for the date if the underlying database does not store timezone information.
   * @param columnName the name of the column in the ResultSet.
   * @param calendar the java.util.Calendar object to use in constructing the date.
   * @return the column value as a java.sql.Date object; if the value is SQL NULL, the value returned is null
   * in the Java programming language.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Date getDate(final String columnName, final Calendar calendar) throws SQLException {
    Date date = getDate(columnName);

    if (ObjectUtil.isNotNull(date) && ObjectUtil.isNotNull(calendar)) {
      final Calendar newDate = Calendar.getInstance();
      newDate.setTimeInMillis(date.getTime());
      newDate.setTimeZone(calendar.getTimeZone());
      date = new Date(newDate.getTimeInMillis());
    }

    return date;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a double
   * in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public double getDouble(final int columnIndex) throws SQLException {
    final Double value = getColumnValue(columnIndex - 1);
    return value;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a double
   * in the Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public double getDouble(final String columnName) throws SQLException {
    final Double value = getColumnValue(columnName);
    return value;
  }

  /**
   * Retrieves the fetch direction for this ResultSet object.
   * @return the current fetch direction for this ResultSet object, ResultSet.FETCH_UNKNOWN.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public int getFetchDirection() throws SQLException {
    return ResultSet.FETCH_UNKNOWN;
  }

  /**
   * Retrieves the fetch size for this ResultSet object.
   * @return the current fetch size for this ResultSet object, which equals the size of the record table.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public int getFetchSize() throws SQLException {
    return getRecordTable().rowCount();
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a float
   * in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public float getFloat(final int columnIndex) throws SQLException {
    final Float value = getColumnValue(columnIndex - 1);
    return value;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a float
   * in the Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public float getFloat(final String columnName) throws SQLException {
    final Float value = getColumnValue(columnName);
    return value;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as an int
   * in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public int getInt(final int columnIndex) throws SQLException {
    final Integer value = getColumnValue(columnIndex - 1);
    return value;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as an int
   * in the Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public int getInt(final String columnName) throws SQLException {
    final Integer value = getColumnValue(columnName);
    return value;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as an long
   * in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public long getLong(final int columnIndex) throws SQLException {
    final Long value = getColumnValue(columnIndex - 1);
    return value;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as an long
   * in the Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public long getLong(final String columnName) throws SQLException {
    final Long value = getColumnValue(columnName);
    return value;
  }

  /**
   * Retrieves the number, types and properties of this ResultSet object's columns.
   * @return the description of this ResultSet object's columns.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public ResultSetMetaData getMetaData() throws SQLException {
    return new RecordTableResultSetMetaData(this);
  }

  /**
   * Gets the value of the designated column in the current row of this ResultSet object as an Object
   * in the Java programming language.
   *
   * This method will return the value of the given column as a Java object. The type of the Java object will be the
   * default Java object type corresponding to the column's SQL type, following the mapping for built-in types specified
   * in the JDBC specification. If the value is an SQL NULL, the driver returns a Java null.
   *
   * This method may also be used to read database-specific abstract data types. In the JDBC 2.0 API, the behavior of
   * method getObject is extended to materialize data of SQL user-defined types. When a column contains a structured or
   * distinct value, the behavior of this method is as if it were a call to: getObject(columnIndex,
   * this.getStatement().getConnection().getTypeMap()).
   * @param columnIndex the index of the column in the ResultSet.
   * @return a java.lang.Object holding the column value.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Object getObject(final int columnIndex) throws SQLException {
    return getColumnValue(columnIndex - 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as an Object in the
   * Java programming language. If the value is an SQL NULL, the driver returns a Java null. This method uses the
   * given Map object for the custom mapping of the SQL structured or distinct type that is being retrieved.
   * @param columnIndex the index of the column in the ResultSet.
   * @param map a java.util.Map object that contains the mapping from SQL type names to classes in the
   * Java programming language.
   * @return an Object in the Java programming language representing the SQL value.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Object getObject(final int columnIndex, final Map<String, Class<?>> map) throws SQLException {
    return getColumnValue(columnIndex - 1);
  }

  /**
   * Gets the value of the designated column in the current row of this ResultSet object as an Object
   * in the Java programming language.
   *
   * This method will return the value of the given column as a Java object. The type of the Java object will be the
   * default Java object type corresponding to the column's SQL type, following the mapping for built-in types specified
   * in the JDBC specification. If the value is an SQL NULL, the driver returns a Java null.
   *
   * This method may also be used to read database-specific abstract data types. In the JDBC 2.0 API, the behavior of
   * method getObject is extended to materialize data of SQL user-defined types. When a column contains a structured or
   * distinct value, the behavior of this method is as if it were a call to: getObject(columnIndex,
   * this.getStatement().getConnection().getTypeMap()).
   * @param columnName the name of the column in the ResultSet.
   * @return a java.lang.Object holding the column value.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Object getObject(final String columnName) throws SQLException {
    return getColumnValue(columnName);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as an Object in the
   * Java programming language. If the value is an SQL NULL, the driver returns a Java null. This method uses the
   * given Map object for the custom mapping of the SQL structured or distinct type that is being retrieved.
   * @param columnName the name of the column in the ResultSet.
   * @param map a java.util.Map object that contains the mapping from SQL type names to classes in the
   * Java programming language.
   * @return an Object in the Java programming language representing the SQL value.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Object getObject(final String columnName, final Map<String, Class<?>> map) throws SQLException {
    return getColumnValue(columnName);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a Ref object in the
   * Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return a Ref object representing an SQL REF value.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Ref getRef(final int columnIndex) throws SQLException {
    return getColumnValue(columnIndex - 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a Ref object in the
   * Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return a Ref object representing an SQL REF value.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Ref getRef(final String columnName) throws SQLException {
    return getColumnValue(columnName);
  }

  /**
   * Retrieves the current row number. The first row is number 1, the second number 2, and so on.
   * @return the current row number; 0 if there is no current row.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public int getRow() throws SQLException {
    return (getCurrentRow() + 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a short in the
   * Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public short getShort(final int columnIndex) throws SQLException {
    final Short value = getColumnValue(columnIndex - 1);
    return value;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a short in the
   * Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public short getShort(final String columnName) throws SQLException {
    final Short value = getColumnValue(columnName);
    return value;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a String in the
   * Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public String getString(final int columnIndex) throws SQLException {
    return getColumnValue(columnIndex - 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a String in the
   * Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public String getString(final String columnName) throws SQLException {
    return getColumnValue(columnName);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.sql.Time object
   * in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Time getTime(final int columnIndex) throws SQLException {
    return getColumnValue(columnIndex - 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.sql.Time object
   * in the Java programming language. This method uses the given calendar to construct an appropriate millisecond value
   * for the time if the underlying database does not store timezone information
   * @param columnIndex the index of the column in the ResultSet.
   * @param calendar the java.util.Calendar object to use in constructing the time.
   * @return the column value as a java.sql.Time object; if the value is SQL NULL, the value returned is null in the
   * Java programming language.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Time getTime(final int columnIndex, final Calendar calendar) throws SQLException {
    Time time = getTime(columnIndex);

    if (ObjectUtil.isNotNull(time) && ObjectUtil.isNotNull(calendar)) {
      final Calendar newTime = Calendar.getInstance();
      newTime.setTimeInMillis(time.getTime());
      newTime.setTimeZone(calendar.getTimeZone());
      time = new Time(newTime.getTimeInMillis());
    }

    return time;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.sql.Time object
   * in the Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Time getTime(final String columnName) throws SQLException {
    return getColumnValue(columnName);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.sql.Time object
   * in the Java programming language. This method uses the given calendar to construct an appropriate millisecond value
   * for the time if the underlying database does not store timezone information
   * @param columnName the name of the column in the ResultSet.
   * @param calendar the java.util.Calendar object to use in constructing the time.
   * @return the column value as a java.sql.Time object; if the value is SQL NULL, the value returned is null in the
   * Java programming language.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Time getTime(final String columnName, final Calendar calendar) throws SQLException {
    Time time = getTime(columnName);

    if (ObjectUtil.isNotNull(time) && ObjectUtil.isNotNull(calendar)) {
      final Calendar newTime = Calendar.getInstance();
      newTime.setTimeInMillis(time.getTime());
      newTime.setTimeZone(calendar.getTimeZone());
      time = new Time(newTime.getTimeInMillis());
    }

    return time;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.sql.Timestamp
   * object in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Timestamp getTimestamp(final int columnIndex) throws SQLException {
    return getColumnValue(columnIndex - 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.sql.Timestamp
   * object in the Java programming language. This method uses the given calendar to construct an appropriate
   * millisecond value for the timestamp if the underlying database does not store timezone information.
   * @param columnIndex the index of the column in the ResultSet.
   * @param calendar the java.util.Calendar object to use in constructing the timestamp.
   * @return the column value as a java.sql.Timestamp object; if the value is SQL NULL, the value returned is null
   * in the Java programming language.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Timestamp getTimestamp(final int columnIndex, final Calendar calendar) throws SQLException {
    Timestamp timestamp = getTimestamp(columnIndex);

    if (ObjectUtil.isNotNull(timestamp) && ObjectUtil.isNotNull(calendar)) {
      final Calendar newTimestamp = Calendar.getInstance();
      newTimestamp.setTimeInMillis(timestamp.getTime());
      newTimestamp.setTimeZone(calendar.getTimeZone());
      timestamp = new Timestamp(newTimestamp.getTimeInMillis());
    }

    return timestamp;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.sql.Timestamp
   * object in the Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Timestamp getTimestamp(final String columnName) throws SQLException {
    return getColumnValue(columnName);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.sql.Timestamp
   * object in the Java programming language. This method uses the given calendar to construct an appropriate
   * millisecond value for the timestamp if the underlying database does not store timezone information.
   * @param columnName the name of the column in the ResultSet.
   * @param calendar the java.util.Calendar object to use in constructing the timestamp.
   * @return the column value as a java.sql.Timestamp object; if the value is SQL NULL, the value returned is null
   * in the Java programming language.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public Timestamp getTimestamp(final String columnName, final Calendar calendar) throws SQLException {
    Timestamp timestamp = getTimestamp(columnName);

    if (ObjectUtil.isNotNull(timestamp) && ObjectUtil.isNotNull(calendar)) {
      final Calendar newTimestamp = Calendar.getInstance();
      newTimestamp.setTimeInMillis(timestamp.getTime());
      newTimestamp.setTimeZone(calendar.getTimeZone());
      timestamp = new Timestamp(newTimestamp.getTimeInMillis());
    }

    return timestamp;
  }

  /**
   * Retrieves the type of this ResultSet object. The type is determined by the Statement object that created the
   * result set.
   * @return ResultSet.TYPE_SCROLL_INSENSITIVE.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public int getType() throws SQLException {
    return ResultSet.TYPE_SCROLL_INSENSITIVE;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.net.URL object
   * in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value as a java.net.URL object; if the value is SQL NULL, the value returned is null in the
   * Java programming language.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public URL getURL(final int columnIndex) throws SQLException {
    return getColumnValue(columnIndex - 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.net.URL object
   * in the Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return the column value as a java.net.URL object; if the value is SQL NULL, the value returned is null in the
   * Java programming language.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public URL getURL(final String columnName) throws SQLException {
    return getColumnValue(columnName);
  }

  /**
   * Retrieves the first warning reported by calls on this ResultSet object. Subsequent warnings on this ResultSet
   * object will be chained to the SQLWarning object that this method returns.
   *
   * The warning chain is automatically cleared each time a new row is read. This method may not be called on a
   * ResultSet object that has been closed; doing so will cause an SQLException to be thrown.
   *
   * Note: This warning chain only covers warnings caused by ResultSet methods. Any warning caused by Statement
   * methods (such as reading OUT parameters) will be chained on the Statement object.
   * @return the first SQLWarning object reported or null if there are none.
   * @throws SQLException if an error occurs accessing the record table or if this method is called on a
   * closed ResultSet.
   */
  public SQLWarning getWarnings() throws SQLException {
    verifyNotClosed();
    return warnings;
  }

  /**
   * Retrieves whether the cursor is after the last row in this ResultSet object.
   * @return true if the cursor is after the last row; false if the cursor is at any other position or the result set
   * contains no rows.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean isAfterLast() throws SQLException {
    verifyNotClosed();
    return (getCurrentRow() >= getRecordTable().rowCount());
  }

  /**
   * Retrieves whether the cursor is before the first row in this ResultSet object.
   * @return true if the cursor is before the first row; false if the cursor is at any other position or the result set
   * contains no rows.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean isBeforeFirst() throws SQLException {
    verifyNotClosed();
    return (getCurrentRow() < 0);
  }

  /**
   * Retrieves whether the cursor is on the first row of this ResultSet object.
   * @return true if the cursor is on the first row; false otherwise.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean isFirst() throws SQLException {
    verifyNotClosed();
    return (getCurrentRow() == 0);
  }

  /**
   * Retrieves whether the cursor is on the last row of this ResultSet object.
   * Note: Calling the method isLast may be expensive because the JDBC driver might need to fetch ahead one row in
   * order to determine whether the current row is the last row in the result set.
   * @return true if the cursor is on the last row; false otherwise.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean isLast() throws SQLException {
    verifyNotClosed();
    return (getCurrentRow() == (getRecordTable().rowCount() - 1));
  }

  /**
   * Moves the cursor to the last row in this ResultSet object.
   * @return true if the cursor is on a valid row; false if there are no rows in the result set.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean last() throws SQLException {
    verifyNotClosed();
    setCurrentRow(getRecordTable().rowCount() - 1);
    return (!isBeforeFirst() && !isAfterLast());
  }

  /**
   * Moves the cursor to the remembered cursor position, usually the current row. This method has no effect if the
   * cursor is not on the insert row.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void moveToCurrentRow() throws SQLException {
    verifyNotClosed();
    // null implementation
  }

  /**
   * Moves the cursor down one row from its current position. A ResultSet cursor is initially positioned before the
   * first row; the first call to the method next makes the first row the current row; the second call makes the
   * second row the current row, and so on.
   * If an input stream is open for the current row, a call to the method next will implicitly close it. A ResultSet
   * object's warning chain is cleared when a new row is read.
   * @return true if the new current row is valid; false if there are no more rows.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean next() throws SQLException {
    verifyNotClosed();
    incrementCurrentRow();
    clearWarnings();
    return !isAfterLast();
  }

  /**
   * Moves the cursor to the previous row in this ResultSet object.
   * @return true if the cursor is on a valid row; false if it is off the result set.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean previous() throws SQLException {
    verifyNotClosed();
    decrementCurrentRow();
    return !isBeforeFirst();
  }

  /**
   * Moves the cursor a relative number of rows, either positive or negative. Attempting to move beyond the first/last
   * row in the result set positions the cursor before/after the the first/last row. Calling relative(0) is valid, but
   * does not change the cursor position.
   *
   * Note: Calling the method relative(1) is identical to calling the method next() and calling the method relative(-1)
   * is identical to calling the method previous().
   * @param rows an int specifying the number of rows to move from the current row; a positive number moves the
   * cursor forward; a negative number moves the cursor backward.
   * @return true if the cursor is on a row; false otherwise.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean relative(final int rows) throws SQLException {
    verifyNotClosed();
    setCurrentRow(getCurrentRow() + rows);
    return (!isBeforeFirst() && !isAfterLast());
  }

  /**
   * Retrieves whether a row has been deleted. A deleted row may leave a visible "hole" in a result set. This method
   * can be used to detect holes in a result set. The value returned depends on whether or not this ResultSet object
   * can detect deletions.
   * @return true if a row was deleted and deletions are detected; false otherwise.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean rowDeleted() throws SQLException {
    verifyNotClosed();
    return rowDeleted;
  }

  /**
   * Retrieves whether the current row has had an insertion. The value returned depends on whether or not this
   * ResultSet object can detect visible inserts.
   * @return true if a row has had an insertion and insertions are detected; false otherwise.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean rowInserted() throws SQLException {
    verifyNotClosed();
    return rowInserted;
  }

  /**
   * Retrieves whether the current row has been updated. The value returned depends on whether or not the result set
   * can detect updates.
   * @return true if both (1) the row has been visibly updated by the owner or another and (2) updates are detected.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean rowUpdated() throws SQLException {
    verifyNotClosed();
    return rowUpdated;
  }

  /**
   * Updates the designated column with a java.sql.Array value. The updater methods are used to update column values in
   * the current row or the insert row.  The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param array the new Array value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateArray(final int columnIndex, final Array array) throws SQLException {
    setColumnValue(columnIndex - 1, array);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a java.sql.Array value. The updater methods are used to update column values in
   * the current row or the insert row.  The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param array the new Array value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateArray(final String columnName, final Array array) throws SQLException {
    setColumnValue(columnName, array);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a java.sql.BigDecimal value. The updater methods are used to update column
   * values in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param decimalValue the new BigDecimal value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateBigDecimal(final int columnIndex, final BigDecimal decimalValue) throws SQLException {
    setColumnValue(columnIndex - 1, decimalValue);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a java.sql.BigDecimal value. The updater methods are used to update column
   * values in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param decimalValue the new BigDecimal value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateBigDecimal(final String columnName, final BigDecimal decimalValue) throws SQLException {
    setColumnValue(columnName, decimalValue);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a java.sql.Blob value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param blob the new Blob value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateBlob(final int columnIndex, final Blob blob) throws SQLException {
    setColumnValue(columnIndex - 1, blob);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a java.sql.Blob value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param blob the new Blob value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateBlob(final String columnName, final Blob blob) throws SQLException {
    setColumnValue(columnName, blob);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a boolean value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param condition the new boolean value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateBoolean(final int columnIndex, final boolean condition) throws SQLException {
    setColumnValue(columnIndex - 1, condition);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a boolean value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param condition the new boolean value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateBoolean(final String columnName, final boolean condition) throws SQLException {
    setColumnValue(columnName, condition);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a byte value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param value the new byte value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateByte(final int columnIndex, final byte value) throws SQLException {
    setColumnValue(columnIndex - 1, value);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a byte value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param value the new byte value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateByte(final String columnName, final byte value) throws SQLException {
    setColumnValue(columnName, value);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a byte array value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param value the new byte array value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateBytes(final int columnIndex, final byte[] value) throws SQLException {
    setColumnValue(columnIndex - 1, value);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a byte array value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param value the new byte array value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateBytes(final String columnName, final byte[] value) throws SQLException {
    setColumnValue(columnName, value);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a java.sql.Clob value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param clob the new Clob value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateClob(final int columnIndex, final Clob clob) throws SQLException {
    setColumnValue(columnIndex - 1, clob);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a java.sql.Clob value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param clob the new Clob value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateClob(final String columnName, final Clob clob) throws SQLException {
    setColumnValue(columnName, clob);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a Date value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param date the new Date value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateDate(final int columnIndex, final Date date) throws SQLException {
    setColumnValue(columnIndex - 1, date);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a Date value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param date the new Date value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateDate(final String columnName, final Date date) throws SQLException {
    setColumnValue(columnName, date);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a double value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param value the new double value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateDouble(final int columnIndex, final double value) throws SQLException {
    setColumnValue(columnIndex - 1, value);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a double value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param value the new double value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateDouble(final String columnName, final double value) throws SQLException {
    setColumnValue(columnName, value);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a float value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param value the new float value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateFloat(final int columnIndex, final float value) throws SQLException {
    setColumnValue(columnIndex - 1, value);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a float value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param value the new float value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateFloat(final String columnName, final float value) throws SQLException {
    setColumnValue(columnName, value);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a int value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param value the new int value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateInt(final int columnIndex, final int value) throws SQLException {
    setColumnValue(columnIndex - 1, value);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a int value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param value the new int value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateInt(final String columnName, final int value) throws SQLException {
    setColumnValue(columnName, value);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a long value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param value the new long value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateLong(final int columnIndex, final long value) throws SQLException {
    setColumnValue(columnIndex - 1, value);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a long value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param value the new long value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateLong(final String columnName, final long value) throws SQLException {
    setColumnValue(columnName, value);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a null value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateNull(final int columnIndex) throws SQLException {
    setColumnValue(columnIndex - 1, null);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a null value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateNull(final String columnName) throws SQLException {
    setColumnValue(columnName, null);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a Object value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param obj the new Object value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateObject(final int columnIndex, final Object obj) throws SQLException {
    setColumnValue(columnIndex - 1, obj);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a Object value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param obj the new Object value for the specified column.
   * @param scale for java.sql.Types.DECIMA or java.sql.Types.NUMERIC types, this is the number of digits after the
   * decimal point. For all other types this value will be ignored.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateObject(final int columnIndex, final Object obj, final int scale) throws SQLException {
    setColumnValue(columnIndex - 1, obj);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a Object value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param obj the new Object value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateObject(final String columnName, final Object obj) throws SQLException {
    setColumnValue(columnName, obj);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a Object value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param obj the new Object value for the specified column.
   * @param scale for java.sql.Types.DECIMA or java.sql.Types.NUMERIC types, this is the number of digits after the
   * decimal point. For all other types this value will be ignored.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateObject(final String columnName, final Object obj, final int scale) throws SQLException {
    setColumnValue(columnName, obj);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a java.sql.Ref value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param ref the new Ref value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateRef(final int columnIndex, final Ref ref) throws SQLException {
    setColumnValue(columnIndex - 1, ref);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a java.sql.Ref value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param ref the new Ref value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateRef(final String columnName, final Ref ref) throws SQLException {
    setColumnValue(columnName, ref);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a short value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param value the new short value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateShort(final int columnIndex, final short value) throws SQLException {
    setColumnValue(columnIndex - 1, value);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a short value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param value the new short value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateShort(final String columnName, final short value) throws SQLException {
    setColumnValue(columnName, value);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a String value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param value the new String value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateString(final int columnIndex, final String value) throws SQLException {
    setColumnValue(columnIndex - 1, value);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a String value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param value the new String value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateString(final String columnName, final String value) throws SQLException {
    setColumnValue(columnName, value);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a java.sql.Time value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param time the new Time value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateTime(final int columnIndex, final Time time) throws SQLException {
    setColumnValue(columnIndex - 1, time);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a java.sql.Time value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param time the new Time value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateTime(final String columnName, final Time time) throws SQLException {
    setColumnValue(columnName, time);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a java.sql.Timestamp value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param timestamp the new Timestamp value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateTimestamp(final int columnIndex, final Timestamp timestamp) throws SQLException {
    setColumnValue(columnIndex - 1, timestamp);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a java.sql.Timestamp value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param timestamp the new Timestamp value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public void updateTimestamp(final String columnName, final Timestamp timestamp) throws SQLException {
    setColumnValue(columnName, timestamp);
    rowUpdated = true;
  }

  /**
   * Reports whether the last column read had a value of SQL NULL. Note that you must first call one of the
   * getter methods on a column to try to read its value and then call the method wasNull to see if the
   * value read was SQL NULL.
   * @return true if the last column value read was SQL NULL and false otherwise.
   * @throws SQLException if an error occurs accessing the record table.
   */
  public boolean wasNull() throws SQLException {
    verifyNotClosed();
    return (ObjectUtil.isNull(lastColumnValueRead));
  }

  /**
   * @inheritDoc
   */
  @Override
  public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented");
  }

  /**
   * @inheritDoc
   */
  @Override
  public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented");
  }
}
