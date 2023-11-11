/*
 * MockResultSet.java (c) 8 August 2003
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.7.4
 * @see com.cp.common.sql.AbstractResultSet
 */

package com.cp.common.sql;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.cp.common.lang.Assert;
import com.cp.common.lang.NumberUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ArrayUtil;

public class MockResultSet extends AbstractResultSet {

  // Tracks the open and closed state of the ReultSet object.
  private boolean closed = false;
  private boolean rowUpdated = false;

  // Tracks the current row of the ResultSet object.
  private int currentRow = -1;

  // A Collection view of column names in this ResultSet.
  private final List<String> columnNameList;

  // Value of the last column read.
  private Object lastColumnValueRead = null;

  // Two-dimensional Object array containing the values of the ResultSet.
  private final Object[][] data;

  // Reference to SQL warnings generated through method calls on this ResultSet.
  private SQLWarning warnings = null;

  // A String array view of the column names in this ResultSet.
  private final String[] columnNames;

  /**
   * Creates an instance of the MockResultSet class initialized with the specified columns and data.
   * @param columnNames the names of the columns for the rows in this ResultSet.
   * @param data the tabular structure of data specifying the contents of this ResultSet.
   */
  public MockResultSet(final String[] columnNames, final Object[][] data) {
    Assert.isFalse(ArrayUtil.isEmpty(columnNames), "The column names of the result set cannot be null or empty!");
    Assert.notNull(data, "The data for the result set cannot be null!");
    this.columnNames = columnNames;
    this.data = data;
    columnNameList = Collections.unmodifiableList(Arrays.asList(columnNames));
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
   * Determines whether this ResultSet is closed.
   * @return a boolean value indicating whether this ResultSet is closed or open.
   */
  public boolean isClosed() {
    return closed;
  }

  /**
   * Returns the number of columns in this ResultSet.
   * @return an integer value of the number of columns in this ResultSet.
   */
  final int getColumnCount() throws SQLException {
    verifyNotClosed();
    return columnNames.length;
  }

  /**
   * Returns the column index for the specified column name in this ResultSet.
   * @param columnName the name of the column in this ResultSet for which the index will be returned.
   * @return a integer value indicating the index of the column with the specified name in this ResultSet.
   */
  final int getColumnIndex(final String columnName) throws SQLException {
    verifyNotClosed();
    return columnNameList.indexOf(columnName);
  }

  /**
   * Returns the name of the column in this ResultSet at the specified column index.
   * @param columnIndex the columnIndex of the column in the ResultSet to return the name for.
   * @return a String representation of the name of the column at the specified column index in this ResultSet.
   */
  final String getColumnName(final int columnIndex) throws SQLException {
    verifyNotClosed();
    return columnNames[columnIndex];
  }

  /**
   * Returns the value of the column specified by the column index for the current row of this ResultSet.
   * @param columnIndex the index of the column for which the data value should be read.
   * @return the value of the column at column index for the current row of the ResultSet.
   * @throws SQLException if the value of the column for the current row could not be read from this ResultSet.
   */
  private <T> T getColumnValue(final int columnIndex) throws SQLException {
    try {
      verifyNotClosed();
      verifyCurrentRow();
      lastColumnValueRead = data[getCurrentRow()][columnIndex];
      return (T) lastColumnValueRead;
    }
    catch (ArrayIndexOutOfBoundsException e) {
      log.error("(" + columnIndex + ") is not a valid column index in this ResultSet!", e);
      throw new SQLException("(" + columnIndex + ") is not a valid column index in this ResultSet!");
    }
  }

  /**
   * Returns the value of a column referenced by the column's name for the current row of the ResultSet.
   * @param columnName the name of the column that the value will be returned from the current row of the ResultSet.
   * @return the value of the column referred to by the the column name.
   * @throws SQLException if the vlaue of the column referred to by the column's name could not be read.
   */
  private <T> T getColumnValue(final String columnName) throws SQLException {
    try {
      return (T) getColumnValue(getColumnIndex(columnName));
    }
    catch (SQLException e) {
      log.error("(" + columnName + ") is not a valid column name in this ResultSet!", e);
      throw new SQLException("(" + columnName + ") is not a valid column name in this ResultSet!");
    }
  }

  /**
   * Sets the specified column referenced by the column's index in the ResultSet of the current row
   * to the specified value.
   * @param columnIndex the index of the column in the current row of the ResultSet to set a value.
   * @param value the Object value to set on the column at the specified index of the current row in the ResultSet.
   * @throws SQLException if the value of the column for the current row of the ResultSet could not be set.
   */
  private void setColumnValue(final int columnIndex, final Object value) throws SQLException {
    try {
      verifyNotClosed();
      verifyCurrentRow();
      data[getCurrentRow()][columnIndex] = value;
    }
    catch (ArrayIndexOutOfBoundsException e) {
      log.error("(" + columnIndex + ") is not a valid column index in this ResultSet!", e);
      throw new SQLException("(" + columnIndex + ") is not a valid column index in this ResultSet!");
    }
  }

  /**
   * Sets specified column reference by name in the ResultSet for the current row to the specified value.
   * @param columnName the name of the column for which the value will be set.
   * @param value the Object value being set on the column having the specified column name.
   * @throws SQLException if the column referred to by name could not be set.
   */
  private void setColumnValue(final String columnName, final Object value) throws SQLException {
    try {
      setColumnValue(getColumnIndex(columnName), value);
    }
    catch (SQLException e) {
      log.error("(" + columnName + ") is not a valid column name in this ResultSet!", e);
      throw new SQLException("(" + columnName + ") is not a valid column name in this ResultSet!");
    }
  }

  /**
   * Returns the an integer value specifying the current row of this ResultSet.
   * @return an interger value signifying the current row of this ResultSet.
   */
  private int getCurrentRow() {
    return currentRow;
  }

  /**
   * Sets the current row to the specified integer value (absolute positioning).
   * @param currentRow an integer value to set the current row of this ResultSet.
   */
  private void setCurrentRow(final int currentRow) {
    this.currentRow = Math.max(-1, Math.min(data.length, currentRow));
  }

  /**
   * Moves the position of the current row back one.
   */
  private void decrementCurrentRow() {
    setCurrentRow(currentRow - 1);
  }

  /**
   * Moves the position of the current row head one.
   */
  private void incrementCurrentRow() {
    setCurrentRow(currentRow + 1);
  }

  /**
   * Detemines whether this ResultSet has data or not.
   * @return a boolean value indicating whether this ResultSet contains data
   *         or not.
   */
  private boolean hasData() {
    return ArrayUtil.isNotEmpty(data);
  }

  /**
   * Verifies whether the specified columnIndex is a valid columnIndex index in this
   * ResultSet.
   * @param columnIndex the columnIndex to verify whether it is a valid index in this
   * ResultSet.
   */
  final void validateColumnIndex(final int columnIndex) {
    if (columnIndex < 1 || columnIndex > columnNames.length) {
      log.warn("(" + columnIndex + ") is not a valid column index in this ResultSet!");
      throw new ArrayIndexOutOfBoundsException("(" + columnIndex + ") is not a valid column index in this ResultSet!");
    }
  }

  /**
   * Determines whether the cursor to the current row is positioned on a
   * valid row within this ResultSet.
   * @throws SQLException if the cursor is not positioned on a valid row
   * in the ResultSet.
   */
  private void verifyCurrentRow() throws SQLException {
    if (isBeforeFirst() || isAfterLast()) {
      log.warn("The cursor is not positioned on a valid row in this ResultSet!");
      throw new SQLException("The cursor is not positioned on a valid row in this ResultSet!");
    }
  }

  /**
   * Determines whether the state of this ResultSet object is open or closed.
   * @throws SQLException if the state of this ResultSet object is closed.
   */
  private void verifyNotClosed() throws SQLException {
    if (closed) {
      log.warn("The result set has been closed!");
      throw new SQLException("The result set has been closed!");
    }
  }

  /**
   * Moves the cursor to the given row number in this ResultSet object.
   * <p/>
   * If the row number is positive, the cursor moves to the given row number with respect to the beginning of the
   * result set. The first row is row 1, the second is row 2, and so on.
   * <p/>
   * If the given row number is negative, the cursor moves to an absolute row position with respect to the end of the
   * result set. For example, calling the method absolute(-1) positions the cursor on the last row; calling the method
   * absolute(-2) moves the cursor to the next-to-last row, and so on.
   * <p/>
   * An attempt to position the cursor beyond the first/last row in the result set leaves the cursor before the first
   * row or after the last row.
   * <p/>
   * Note: Calling absolute(1) is the same as calling first(). Calling absolute(-1) is the same as calling last().
   * @param row the number of the row to which the cursor should move. A positive number indicates the row number
   * counting from the beginning of the result set; a negative number indicates the row number counting from the
   * end of the result set
   * @return true if the cursor is on the result set; false otherwise.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public boolean absolute(int row) throws SQLException {
    verifyNotClosed();
    final int origin = (NumberUtil.isGreaterThanEqualTo(0, row) ? 0 : data.length);
    row += (NumberUtil.isPositive(row) ? -1 : 0);
    setCurrentRow(origin + row);
    return !(isBeforeFirst() || isAfterLast());
  }

  /**
   * Moves the cursor to the end of this ResultSet object, just after the last row. This method has no effect if the
   * result set contains no rows.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public void afterLast() throws SQLException {
    verifyNotClosed();
    setCurrentRow(data.length);
  }

  /**
   * Moves the cursor to the front of this ResultSet object, just before the first row. This method has no effect if
   * the result set contains no rows.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public void beforeFirst() throws SQLException {
    verifyNotClosed();
    setCurrentRow(-1);
  }

  /**
   * Clears all warnings reported on this ResultSet object. After this method is called, the method getWarnings
   * returns null until a new warning is reported for this ResultSet object.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public void clearWarnings() throws SQLException {
    verifyNotClosed();
    warnings = null;
  }

  /**
   * Releases this ResultSet object's database and JDBC resources immediately instead of waiting for this to happen
   * when it is automatically closed.
   * <p/>
   * Note: A ResultSet object is automatically closed by the Statement object that generated it when that Statement
   * object is closed, re-executed, or is used to retrieve the next result from a sequence of multiple results. A
   * ResultSet object is also automatically closed when it is garbage collected.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public void close() throws SQLException {
    closed = true;
  }

  /**
   * Maps the given ResultSet column name to its ResultSet column index.
   * @param columnName the name of the column.
   * @return the column index of the given column name.
   * @throws SQLException if the ResultSet object does not contain columnName or an error occurs accessing
   * the underlying Object array.
   */
  public int findColumn(final String columnName) throws SQLException {
    verifyNotClosed();

    final int columnIndex = getColumnIndex(columnName);

    if (columnIndex == -1) {
      log.warn(columnName + " is not a valid column name in this ResultSet!");
      throw new SQLException(columnName + " is not a valid column name in this ResultSet!");
    }

    return columnIndex;
  }

  /**
   * Moves the cursor to the first row in this ResultSet object.
   * @return true if the cursor is on a valid row; false if there are no rows in the result set.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public boolean first() throws SQLException {
    verifyNotClosed();
    return !(isBeforeFirst() || isAfterLast());
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as an Array object
   * in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return an Array object representing the SQL ARRAY value in the specified column.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public Array getArray(final int columnIndex) throws SQLException {
    return getColumnValue(columnIndex - 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as an Array object
   * in the Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return an Array object representing the SQL ARRAY value in the specified column.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public Array getArray(final String columnName) throws SQLException {
    return getColumnValue(columnName);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a stream of
   * ASCII characters. The value can then be read in chunks from the stream. This method is particularly suitable
   * for retrieving large LONGVARCHAR values. The JDBC driver will do any necessary conversion from the database
   * format into ASCII.
   * <p/>
   * Note: All the data in the returned stream must be read prior to getting the value of any other column. The next
   * call to a getter method implicitly closes the stream. Also, a stream may return 0 when the method
   * InputStream.available is called whether there is data available or not.
   * @param columnIndex the index of the column in the ResultSet.
   * @return a Java input stream that delivers the database column value as a stream of one-byte ASCII characters;
   *         if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public InputStream getAsciiStream(final int columnIndex) throws SQLException {
    final Object columnValue = getColumnValue(columnIndex - 1);
    return (ObjectUtil.isNull(columnValue) ? null :
      new ByteArrayInputStream(columnValue.toString().getBytes()));
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a stream of
   * ASCII characters. The value can then be read in chunks from the stream. This method is particularly suitable
   * for retrieving large LONGVARCHAR values. The JDBC driver will do any necessary conversion from the database
   * format into ASCII.
   * <p/>
   * Note: All the data in the returned stream must be read prior to getting the value of any other column. The next
   * call to a getter method implicitly closes the stream. Also, a stream may return 0 when the method
   * InputStream.available is called whether there is data available or not.
   * @param columnName the name of the column in the ResultSet.
   * @return a Java input stream that delivers the database column value as a stream of one-byte ASCII characters;
   *         if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public InputStream getAsciiStream(final String columnName) throws SQLException {
    return getAsciiStream(getColumnIndex(columnName) + 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.sql.BigDecimal
   * in the Java programming language.
   * @param columnIndex the index of the column in this ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   * @deprecated
   */
  public BigDecimal getBigDecimal(final int columnIndex) throws SQLException {
    return getColumnValue(columnIndex - 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.sql.BigDecimal
   * in the Java programming language.
   * @param columnIndex the index of the column in this ResultSet.
   * @param scale the number of digits to the right of the decimal point.
   * @return the column value; if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   * @deprecated
   */
  public BigDecimal getBigDecimal(final int columnIndex, final int scale) throws SQLException {
    return getColumnValue(columnIndex - 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.sql.BigDecimal
   * in the Java programming language.
   * @param columnName the name of the column in this ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   * @deprecated
   */
  public BigDecimal getBigDecimal(final String columnName) throws SQLException {
    return getColumnValue(columnName);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.sql.BigDecimal
   * in the Java programming language.
   * @param columnName the name of the column in this ResultSet.
   * @param scale the number of digits to the right of the decimal point.
   * @return the column value; if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   * @deprecated
   */
  public BigDecimal getBigDecimal(final String columnName, final int scale) throws SQLException {
    return getColumnValue(columnName);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a Blob object
   * in the Java programming language
   * @param columnIndex the index of the column in the ResultSet.
   * @return a Blob object representing the SQL BLOB value in the specified column.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public Blob getBlob(final int columnIndex) throws SQLException {
    return getColumnValue(columnIndex - 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a Blob object
   * in the Java programming language
   * @param columnName the name of the column in the ResultSet.
   * @return a Blob object representing the SQL BLOB value in the specified column.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public Blob getBlob(final String columnName) throws SQLException {
    return getColumnValue(columnName);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a boolean
   * in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is false.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public boolean getBoolean(final int columnIndex) throws SQLException {
    final Boolean booleanValue = getColumnValue(columnIndex - 1);
    return booleanValue;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a boolean
   * in the Java programming language.
   * @param columnName the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is false.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public boolean getBoolean(final String columnName) throws SQLException {
    final Boolean booleanValue = getColumnValue(columnName);
    return booleanValue;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a byte
   * in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public byte getByte(final int columnIndex) throws SQLException {
    final Byte byteValue = getColumnValue(columnIndex - 1);
    return byteValue;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a byte
   * in the Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public byte getByte(final String columnName) throws SQLException {
    final Byte byteValue = getColumnValue(columnName);
    return byteValue;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a byte array
   * in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public byte[] getBytes(final String columnName) throws SQLException {
    final Object byteArray = getColumnValue(columnName);
    return (byte[]) byteArray;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.io.Reader
   * object.
   * @param columnIndex the index of the column in this ResultSet.
   * @return a  java.io.Reader object that contains the column value; if the value is SQL NULL, the value returned
   *         is null in the Java programming language.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public Reader getCharacterStream(final int columnIndex) throws SQLException {
    final Object columnValue = getColumnValue(columnIndex - 1);
    return (ObjectUtil.isNull(columnValue) ? null : new StringReader(columnValue.toString()));
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.io.Reader
   * object.
   * @param columnName the name of the column in this ResultSet.
   * @return a  java.io.Reader object that contains the column value; if the value is SQL NULL, the value returned
   *         is null in the Java programming language.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public Reader getCharacterStream(final String columnName) throws SQLException {
    return getCharacterStream(getColumnIndex(columnName) + 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a Clob object
   * in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return a Clob object representing the SQL CLOB value in the specified column.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public Clob getClob(final int columnIndex) throws SQLException {
    return getColumnValue(columnIndex - 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a Clob object
   * in the Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return a Clob object representing the SQL CLOB value in the specified column.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public Clob getClob(final String columnName) throws SQLException {
    return getColumnValue(columnName);
  }

  /**
   * Retrieves the concurrency mode of this ResultSet object. The concurrency used is determined by the Statement
   * object that created the result set.
   * @return the concurrency type, either ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public int getConcurrency() throws SQLException {
    return CONCUR_UPDATABLE;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.sql.Date object
   * in the Java programming language
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   *         in the Java programming language.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public Date getDate(final int columnIndex, final Calendar calendar) throws SQLException {
    Date date = getDate(columnIndex);

    if (ObjectUtil.isNotNull(date) && ObjectUtil.isNotNull(calendar)) {
      final Calendar newDate = Calendar.getInstance();
      newDate.setTime(date);
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   *         in the Java programming language.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public Date getDate(final String columnName, final Calendar calendar) throws SQLException {
    Date date = getDate(columnName);

    if (ObjectUtil.isNotNull(date) && ObjectUtil.isNotNull(calendar)) {
      final Calendar newDate = Calendar.getInstance();
      newDate.setTime(date);
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public double getDouble(final int columnIndex) throws SQLException {
    final Double doubleValue = getColumnValue(columnIndex - 1);
    return doubleValue;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a double
   * in the Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public double getDouble(final String columnName) throws SQLException {
    final Double doubleValue = getColumnValue(columnName);
    return doubleValue;
  }

  /**
   * Retrieves the fetch direction for this ResultSet object.
   * @return the current fetch direction for this ResultSet object.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public int getFetchDirection() throws SQLException {
    return FETCH_UNKNOWN;
  }

  /**
   * Retrieves the fetch size for this ResultSet object.
   * @return the current fetch size for this ResultSet object.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public int getFetchSize() throws SQLException {
    return data.length;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a float
   * in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public float getFloat(final int columnIndex) throws SQLException {
    final Float floatValue = getColumnValue(columnIndex - 1);
    return floatValue;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a float
   * in the Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public float getFloat(final String columnName) throws SQLException {
    final Float floatValue = getColumnValue(columnName);
    return floatValue;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a int
   * in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public int getInt(final int columnIndex) throws SQLException {
    final Integer integerValue = getColumnValue(columnIndex - 1);
    return integerValue;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a int
   * in the Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public int getInt(final String columnName) throws SQLException {
    final Integer integerValue = getColumnValue(columnName);
    return integerValue;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a long
   * in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public long getLong(final int columnIndex) throws SQLException {
    final Long longValue = getColumnValue(columnIndex - 1);
    return longValue;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a long
   * in the Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public long getLong(final String columnName) throws SQLException {
    final Long longValue = getColumnValue(columnName);
    return longValue;
  }

  /**
   * Retrieves the number, types and properties of this ResultSet object's columns.
   * @return the description of this ResultSet object's columns.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public ResultSetMetaData getMetaData() throws SQLException {
    return new MockResultSetMetaData(this);
  }

  /**
   * Gets the value of the designated column in the current row of this ResultSet object as an Object
   * in the Java programming language.
   * <p/>
   * This method will return the value of the given column as a Java object. The type of the Java object will be the
   * default Java object type corresponding to the column's SQL type, following the mapping for built-in types specified
   * in the JDBC specification. If the value is an SQL NULL, the driver returns a Java null.
   * <p/>
   * This method may also be used to read database-specific abstract data types. In the JDBC 2.0 API, the behavior of
   * method getObject is extended to materialize data of SQL user-defined types. When a column contains a structured or
   * distinct value, the behavior of this method is as if it were a call to: getObject(columnIndex,
   * this.getStatement().getConnection().getTypeMap()).
   * @param columnIndex the index of the column in the ResultSet.
   * @return a java.lang.Object holding the column value.
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public Object getObject(final int columnIndex, final Map map) throws SQLException {
    return getObject(columnIndex);
  }

  /**
   * Gets the value of the designated column in the current row of this ResultSet object as an Object
   * in the Java programming language.
   * <p/>
   * This method will return the value of the given column as a Java object. The type of the Java object will be the
   * default Java object type corresponding to the column's SQL type, following the mapping for built-in types specified
   * in the JDBC specification. If the value is an SQL NULL, the driver returns a Java null.
   * <p/>
   * This method may also be used to read database-specific abstract data types. In the JDBC 2.0 API, the behavior of
   * method getObject is extended to materialize data of SQL user-defined types. When a column contains a structured or
   * distinct value, the behavior of this method is as if it were a call to: getObject(columnIndex,
   * this.getStatement().getConnection().getTypeMap()).
   * @param columnName the name of the column in the ResultSet.
   * @return a java.lang.Object holding the column value.
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public Object getObject(final String columnName, final Map map) throws SQLException {
    return getObject(columnName);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a Ref object in the
   * Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return a Ref object representing an SQL REF value.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public Ref getRef(final int columnIndex) throws SQLException {
    return getColumnValue(columnIndex - 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a Ref object in the
   * Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return a Ref object representing an SQL REF value.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public Ref getRef(final String columnName) throws SQLException {
    return getColumnValue(columnName);
  }

  /**
   * Retrieves the current row number.
   * @return the current row number; 0 if there is no current row.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public int getRow() throws SQLException {
    verifyNotClosed();
    return getCurrentRow();
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a short in the
   * Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public short getShort(final int columnIndex) throws SQLException {
    final Short shortValue = getColumnValue(columnIndex - 1);
    return shortValue;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a short in the
   * Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is 0.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public short getShort(final String columnName) throws SQLException {
    final Short shortValue = getColumnValue(columnName);
    return shortValue;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a String in the
   * Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public String getString(final int columnIndex) throws SQLException {
    return getColumnValue(columnIndex - 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a String in the
   * Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public String getString(final String columnName) throws SQLException {
    return getColumnValue(columnName);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.sql.Time object
   * in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value; if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   *         Java programming language.
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   *         Java programming language.
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   *         in the Java programming language.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public Timestamp getTimestamp(final int columnIndex, final Calendar calendar) throws SQLException {
    Timestamp timestamp = getTimestamp(columnIndex);

    if (ObjectUtil.isNotNull(timestamp)) {
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   *         in the Java programming language.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public Timestamp getTimestamp(final String columnName, final Calendar calendar) throws SQLException {
    Timestamp timestamp = getTimestamp(columnName);

    if (ObjectUtil.isNotNull(timestamp)) {
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public int getType() throws SQLException {
    return ResultSet.TYPE_SCROLL_INSENSITIVE;
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.net.URL object
   * in the Java programming language.
   * @param columnIndex the index of the column in the ResultSet.
   * @return the column value as a java.net.URL object; if the value is SQL NULL, the value returned is null in the
   *         Java programming language.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public URL getURL(final int columnIndex) throws SQLException {
    verifyNotClosed();
    return (URL) getColumnValue(columnIndex - 1);
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.net.URL object
   * in the Java programming language.
   * @param columnName the name of the column in the ResultSet.
   * @return the column value as a java.net.URL object; if the value is SQL NULL, the value returned is null in the
   *         Java programming language.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public URL getURL(final String columnName) throws SQLException {
    verifyNotClosed();
    return (URL) getColumnValue(columnName);
  }

  /**
   * Retrieves the first warning reported by calls on this ResultSet object. Subsequent warnings on this ResultSet
   * object will be chained to the SQLWarning object that this method returns.
   * <p/>
   * The warning chain is automatically cleared each time a new row is read. This method may not be called on a
   * ResultSet object that has been closed; doing so will cause an SQLException to be thrown.
   * <p/>
   * Note: This warning chain only covers warnings caused by ResultSet methods. Any warning caused by Statement
   * methods (such as reading OUT parameters) will be chained on the Statement object.
   * @return the first SQLWarning object reported or null if there are none.
   * @throws SQLException if an error occurs accessing the underlying Object array or this method is called on a
   * closed ResultSet.
   */
  public SQLWarning getWarnings() throws SQLException {
    verifyNotClosed();
    return warnings;
  }

  /**
   * Retrieves whether the cursor is after the last row in this ResultSet object.
   * @return true if the cursor is after the last row; false if the cursor is at any other position or the result set
   *         contains no rows.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public boolean isAfterLast() throws SQLException {
    verifyNotClosed();
    return (!hasData() || (getCurrentRow() >= data.length));
  }

  /**
   * Retrieves whether the cursor is before the first row in this ResultSet object.
   * @return true if the cursor is before the first row; false if the cursor is at any other position or the result set
   *         contains no rows.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public boolean isBeforeFirst() throws SQLException {
    verifyNotClosed();
    return (!hasData() || (getCurrentRow() < 0));
  }

  /**
   * Retrieves whether the cursor is on the first row of this ResultSet object.
   * @return true if the cursor is on the first row; false otherwise.
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public boolean isLast() throws SQLException {
    verifyNotClosed();
    return (getCurrentRow() == (data.length - 1));
  }

  /**
   * Moves the cursor to the last row in this ResultSet object.
   * @return true if the cursor is on a valid row; false if there are no rows in the result set.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public boolean last() throws SQLException {
    verifyNotClosed();
    setCurrentRow(data.length - 1);
    return !(isBeforeFirst() || isAfterLast());
  }

  /**
   * Moves the cursor to the remembered cursor position, usually the current row. This method has no effect if the
   * cursor is not on the insert row.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public void moveToCurrentRow() throws SQLException {
    verifyNotClosed();
    // null implementation
  }

  /**
   * Moves the cursor down one row from its current position. A ResultSet cursor is initially positioned before the
   * first row; the first call to the method next makes the first row the current row; the second call makes the
   * second row the current row, and so on.
   * <p/>
   * If an input stream is open for the current row, a call to the method next will implicitly close it. A ResultSet
   * object's warning chain is cleared when a new row is read.
   * @return true if the new current row is valid; false if there are no more rows.
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * <p/>
   * Note: Calling the method relative(1) is identical to calling the method next() and calling the method relative(-1)
   * is identical to calling the method previous().
   * @param rows an int specifying the number of rows to move from the current row; a positive number moves the cursor
   * forward; a negative number moves the cursor backward.
   * @return true if the cursor is on a row; false otherwise.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public boolean relative(final int rows) throws SQLException {
    verifyNotClosed();
    setCurrentRow(getCurrentRow() + rows);
    return !(isBeforeFirst() || isAfterLast());
  }

  /**
   * Retrieves whether a row has been deleted. A deleted row may leave a visible "hole" in a result set. This method
   * can be used to detect holes in a result set. The value returned depends on whether or not this ResultSet object
   * can detect deletions.
   * @return true if a row was deleted and deletions are detected; false otherwise.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public boolean rowDeleted() throws SQLException {
    verifyNotClosed();
    return false;
  }

  /**
   * Retrieves whether the current row has had an insertion. The value returned depends on whether or not this
   * ResultSet object can detect visible inserts.
   * @return true if a row has had an insertion and insertions are detected; false otherwise.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public boolean rowInserted() throws SQLException {
    verifyNotClosed();
    return false;
  }

  /**
   * Retrieves whether the current row has been updated. The value returned depends on whether or not the result set
   * can detect updates.
   * @return true if both (1) the row has been visibly updated by the owner or another and (2) updates are detected.
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public void updateArray(final String columnName, final Array array) throws SQLException {
    setColumnValue(columnName, array);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a java.sql.BigDecimal value. The updater methods are used to update column
   * values in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param value the new BigDecimal value for the specified column.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public void updateBigDecimal(final int columnIndex, final BigDecimal value) throws SQLException {
    setColumnValue(columnIndex - 1, value);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a java.sql.BigDecimal value. The updater methods are used to update column
   * values in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param value the new BigDecimal value for the specified column.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public void updateBigDecimal(final String columnName, final BigDecimal value) throws SQLException {
    setColumnValue(columnName, value);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a java.sql.Blob value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param blob the new Blob value for the specified column.
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public void updateLong(final String columnName, final long value) throws SQLException {
    setColumnValue(columnName, value);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a null value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public void updateNull(final int columnIndex) throws SQLException {
    setColumnValue(columnIndex - 1, null);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a null value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public void updateNull(final String columnName) throws SQLException {
    setColumnValue(columnName, null);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a Object obj. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param obj the new Object obj for the specified column.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public void updateObject(final int columnIndex, final Object obj) throws SQLException {
    setColumnValue(columnIndex - 1, obj);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a Object obj. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param obj the new Object obj for the specified column.
   * @param scale for java.sql.Types.DECIMA or java.sql.Types.NUMERIC types, this is the number of digits after the
   * decimal point. For all other types this value will be ignored.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public void updateObject(final int columnIndex, final Object obj, final int scale) throws SQLException {
    try {
      final BigDecimal decimalValue = new BigDecimal(String.valueOf(obj));
      decimalValue.setScale(scale);
      setColumnValue(columnIndex - 1, decimalValue);
      rowUpdated = true;
    }
    catch (NumberFormatException e) {
      log.error("Failed to update column at index (" + columnIndex + ") for the current row (" + getCurrentRow()
        + ") of this ResultSet to the specified Object obj (" + obj + ")", e);
      throw new SQLException("Failed to update column at index (" + columnIndex + ") for the current row ("
        + getCurrentRow() + ") of this ResultSet to the specified Object obj (" + obj + ")");
    }
  }

  /**
   * Updates the designated column with a Object obj. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param obj the new Object obj for the specified column.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public void updateObject(final String columnName, final Object obj) throws SQLException {
    setColumnValue(columnName, obj);
    rowUpdated = true;
  }

  /**
   * Updates the designated column with a Object obj. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnName the name of the column in the ResultSet.
   * @param obj the new Object obj for the specified column.
   * @param scale for java.sql.Types.DECIMA or java.sql.Types.NUMERIC types, this is the number of digits after the
   * decimal point. For all other types this obj will be ignored.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public void updateObject(final String columnName, final Object obj, final int scale) throws SQLException {
    updateObject(getColumnIndex(columnName) + 1, obj, scale);
  }

  /**
   * Updates the designated column with a java.sql.Ref value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param ref the new Ref value for the specified column.
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
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
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public void updateTimestamp(final String columnName, final Timestamp timestamp) throws SQLException {
    setColumnValue(columnName, timestamp);
    rowUpdated = true;
  }

  /**
   * Reports whether the last column read had a value of SQL NULL. Note that you must first call one of the getter
   * methods on a column to try to read its value and then call the method wasNull to see if the value read was
   * SQL NULL
   * @return true if the last column value read was SQL NULL and false otherwise.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  public boolean wasNull() throws SQLException {
    verifyNotClosed();
    return ObjectUtil.isNull(lastColumnValueRead);
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
