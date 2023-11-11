/*
 * AbstractResultSet.java (c) 04 July 2010
 *
 * Copyright (c) 2010, Codeprimate LLC
 * All Rights Reserved
 * @author jblum
 * @version 2010.07.04
 * @see java.sql.ResultSet
 */

package com.cp.common.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractResultSet implements ResultSet {

  protected final Log log = LogFactory.getLog(getClass());

  @Override public boolean next() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void close() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean wasNull() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public String getString(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean getBoolean(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public byte getByte(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public short getShort(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public int getInt(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public long getLong(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public float getFloat(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public double getDouble(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Deprecated @Override public BigDecimal getBigDecimal(final int columnIndex, final int scale) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public byte[] getBytes(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Date getDate(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Time getTime(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Timestamp getTimestamp(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a stream of
   * ASCII characters. The value can then be read in chunks from the stream. This method is particularly suitable
   * for retrieving large LONGVARCHAR values. The JDBC driver will do any necessary conversion from the database
   * format into ASCII.
   *
   * Note: All the data in the returned stream must be read prior to getting the value of any other column. The next
   * call to a getter method implicitly closes the stream. Also, a stream may return 0 when the method
   * InputStream.available is called whether there is data available or not.
   * @param columnIndex the index of the column in this ResultSet.
   * @return a Java input stream that delivers the database column value as a stream of one-byte ASCII characters;
   * if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the record table.
   * @throws UnsupportedOperationException the method is not implemented!
   */
  @Override public InputStream getAsciiStream(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Deprecated. use getCharacterStream in place of getUnicodeStream
   * Retrieves the value of the designated column in the current row of this ResultSet object as as a stream of
   * two-byte Unicode characters. The first byte is the high byte; the second byte is the low byte. The value can then
   * be read in chunks from the stream. This method is particularly suitable for retrieving large LONGVARCHAR values.
   * The JDBC driver will do any necessary conversion from the database format into Unicode.
   *
   * Note: All the data in the returned stream must be read prior to getting the value of any other column. The next
   * call to a getter method implicitly closes the stream. Also, a stream may return 0 when the method
   * InputStream.available is called, whether there is data available or not.
   * @param columnIndex the index of the column in the ResultSet.
   * @return a Java input stream that delivers the database column value as a stream of two-byte Unicode characters;
   * if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the record table.
   * @throws UnsupportedOperationException the method is not implemented!
   */
  @Deprecated @Override public InputStream getUnicodeStream(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a binary stream of
   * uninterpreted bytes. The value can then be read in chunks from the stream. This method is particularly suitable
   * for retrieving large LONGVARBINARY values.
   *
   * Note: All the data in the returned stream must be read prior to getting the value of any other column. The next
   * call to a getter method implicitly closes the stream. Also, a stream may return 0 when the method
   * InputStream.available is called whether there is data available or not
   * @param columnIndex the index of the column in this ResultSet.
   * @return a Java input stream that delivers the database column value as a stream of uninterpreted bytes; if the
   * value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the record table.
   * @throws UnsupportedOperationException the method is not implemented!
   */
  @Override public InputStream getBinaryStream(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public String getString(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean getBoolean(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public byte getByte(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public short getShort(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public int getInt(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public long getLong(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public float getFloat(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public double getDouble(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Deprecated @Override public BigDecimal getBigDecimal(final String columnLabel, final int scale) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public byte[] getBytes(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Date getDate(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Time getTime(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Timestamp getTimestamp(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a stream of
   * ASCII characters. The value can then be read in chunks from the stream. This method is particularly suitable
   * for retrieving large LONGVARCHAR values. The JDBC driver will do any necessary conversion from the database
   * format into ASCII.
   *
   * Note: All the data in the returned stream must be read prior to getting the value of any other column. The next
   * call to a getter method implicitly closes the stream. Also, a stream may return 0 when the method
   * InputStream.available is called whether there is data available or not.
   * @param columnLabel the name of the column in this ResultSet.
   * @return a Java input stream that delivers the database column value as a stream of one-byte ASCII characters;
   * if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the record table.
   * @throws UnsupportedOperationException the method is not implemented!
   */
  @Override public InputStream getAsciiStream(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Deprecated. use getCharacterStream in place of getUnicodeStream
   * Retrieves the value of the designated column in the current row of this ResultSet object as as a stream of
   * two-byte Unicode characters. The first byte is the high byte; the second byte is the low byte. The value can then
   * be read in chunks from the stream. This method is particularly suitable for retrieving large LONGVARCHAR values.
   * The JDBC driver will do any necessary conversion from the database format into Unicode.
   *
   * Note: All the data in the returned stream must be read prior to getting the value of any other column. The next
   * call to a getter method implicitly closes the stream. Also, a stream may return 0 when the method
   * InputStream.available is called, whether there is data available or not.
   * @param columnLabel the name of the column in the ResultSet.
   * @return a Java input stream that delivers the database column value as a stream of two-byte Unicode characters;
   * if the value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the record table.
   * @throws UnsupportedOperationException the method is not implemented!
   */
  @Deprecated @Override public InputStream getUnicodeStream(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a binary stream of
   * uninterpreted bytes. The value can then be read in chunks from the stream. This method is particularly suitable
   * for retrieving large LONGVARBINARY values.
   *
   * Note: All the data in the returned stream must be read prior to getting the value of any other column. The next
   * call to a getter method implicitly closes the stream. Also, a stream may return 0 when the method
   * InputStream.available is called whether there is data available or not
   * @param columnLabel the name of the column in this ResultSet.
   * @return a Java input stream that delivers the database column value as a stream of uninterpreted bytes; if the
   * value is SQL NULL, the value returned is null.
   * @throws SQLException if an error occurs accessing the record table.
   * @throws UnsupportedOperationException the method is not implemented!
   */
  @Override public InputStream getBinaryStream(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public SQLWarning getWarnings() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void clearWarnings() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Retrieves the name of the SQL cursor used by this ResultSet object.
   *
   * In SQL, a result table is retrieved through a cursor that is named. The current row of a result set can be updated
   * or deleted using a positioned update/delete statement that references the cursor name. To insure that the cursor
   * has the proper isolation level to support update, the cursor's SELECT statement should be of the form
   * SELECT FOR UPDATE. If FOR UPDATE is omitted, the positioned updates may fail.
   *
   * The JDBC API supports this SQL feature by providing the name of the SQL cursor used by a ResultSet object.
   * The current row of a ResultSet object is also the current row of this SQL cursor.
   * @return the SQL name for this ResultSet object's cursor.
   * @throws SQLException if an error occurs accessing the record table.
   * @throws UnsupportedOperationException the method is not implemented!
   */
  @Override public String getCursorName() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public ResultSetMetaData getMetaData() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Object getObject(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Object getObject(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public int findColumn(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.io.Reader object.
   * @param columnIndex the index of the column in the ResultSet.
   * @return a java.io.Reader object that contains the column value; if the value is SQL NULL, the value returned
   * is null in the Java programming language.
   * @throws SQLException if an error occurs accessing the record table.
   * @throws UnsupportedOperationException the method is not implemented!
   */
  @Override public Reader getCharacterStream(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Retrieves the value of the designated column in the current row of this ResultSet object as a java.io.Reader object.
   * @param columnLabel the name of the column in the ResultSet.
   * @return a java.io.Reader object that contains the column value; if the value is SQL NULL, the value returned
   * is null in the Java programming language.
   * @throws SQLException if an error occurs accessing the record table.
   * @throws UnsupportedOperationException the method is not implemented!
   */
  @Override public Reader getCharacterStream(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public BigDecimal getBigDecimal(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public BigDecimal getBigDecimal(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean isBeforeFirst() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean isAfterLast() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean isFirst() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean isLast() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void beforeFirst() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void afterLast() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean first() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean last() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public int getRow() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean absolute(final int row) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean relative(final int rows) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean previous() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Gives a hint as to the direction in which the rows in this ResultSet object will be processed. The initial value is
   * determined by the Statement object that produced this ResultSet object. The fetch direction may be changed at any
   * time.
   * @param direction an int specifying the suggested fetch direction; one of ResultSet.FETCH_FORWARD,
   * ResultSet.FETCH_REVERSE, or ResultSet.FETCH_UNKNOWN.
   * @throws SQLException if an error occurs accessing the record table.
   * @throws UnsupportedOperationException the method is not implemented!
   */
  @Override public void setFetchDirection(final int direction) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public int getFetchDirection() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Gives the JDBC driver a hint as to the number of rows that should be fetched from the database when more rows are
   * needed for this ResultSet object. If the fetch size specified is zero, the JDBC driver ignores the value and is
   * free to make its own best guess as to what the fetch size should be. The default value is set by the Statement
   * object that created the result set. The fetch size may be changed at any time.
   * @param rows the number of rows to fetch.
   * @throws SQLException if an error occurs accessing the record table or the condition
   * 0 <= rows <= Statement.getMaxRows() is not satisfied.
   * @throws UnsupportedOperationException the method is not implemented!
   */
  @Override public void setFetchSize(final int rows) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public int getFetchSize() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public int getType() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public int getConcurrency() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean rowUpdated() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean rowInserted() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean rowDeleted() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateNull(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateBoolean(final int columnIndex, final boolean x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateByte(final int columnIndex, final byte x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateShort(final int columnIndex, final short x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateInt(final int columnIndex, final int x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateLong(final int columnIndex, final long x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateFloat(final int columnIndex, final float x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateDouble(final int columnIndex, final double x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateBigDecimal(final int columnIndex, final BigDecimal x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateString(final int columnIndex, final String x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateBytes(final int columnIndex, final byte[] x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateDate(final int columnIndex, final Date x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateTime(final int columnIndex, final Time x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateTimestamp(final int columnIndex, final Timestamp x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Updates the designated column with an ascii stream value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param x the new ascii stream value for the specified column.
   * @param length the length of the stream.
   * @throws SQLException if an error occurs accessing the record table.
   * @throws UnsupportedOperationException the method is not implemented!
   */
  @Override public void updateAsciiStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Updates the designated column with a binary stream value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param x the new binary stream value for the specified column.
   * @param length  the length of the stream.
   * @throws SQLException if an error occurs accessing the record table.
   * @throws UnsupportedOperationException the method is not implemented!
   */
  @Override public void updateBinaryStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Updates the designated column with a character stream value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnIndex the index of the column in the ResultSet.
   * @param x the new character stream value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   * @throws UnsupportedOperationException the method is not implemented!
   */
  @Override public void updateCharacterStream(final int columnIndex, final Reader x, final int length) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateObject(final int columnIndex, final Object x, final int scaleOrLength) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateObject(final int columnIndex, final Object x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateNull(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateBoolean(final String columnLabel, final boolean x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateByte(final String columnLabel, final byte x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateShort(final String columnLabel, final short x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateInt(final String columnLabel, final int x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateLong(final String columnLabel, final long x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateFloat(final String columnLabel, final float x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateDouble(final String columnLabel, final double x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateBigDecimal(final String columnLabel, final BigDecimal x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateString(final String columnLabel, final String x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateBytes(final String columnLabel, final byte[] x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateDate(final String columnLabel, final Date x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateTime(final String columnLabel, final Time x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateTimestamp(final String columnLabel, final Timestamp x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Updates the designated column with an ascii stream value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnLabel the name of the column in the ResultSet.
   * @param x the new ascii stream value for the specified column.
   * @param length the length of the stream.
   * @throws SQLException if an error occurs accessing the record table.
   * @throws UnsupportedOperationException the method is not implemented!
   */
  @Override public void updateAsciiStream(final String columnLabel, final InputStream x, final int length) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Updates the designated column with a binary stream value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnLabel the name of the column in the ResultSet.
   * @param x the new binary stream value for the specified column.
   * @param length the length of the stream.
   * @throws SQLException if an error occurs accessing the record table.
   * @throws UnsupportedOperationException the method is not implemented!
   */
  @Override public void updateBinaryStream(final String columnLabel, final InputStream x, final int length) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Updates the designated column with a character stream value. The updater methods are used to update column values
   * in the current row. The updater methods directly update the underlying record table.
   * @param columnLabel the name of the column in the ResultSet.
   * @param reader the new character stream value for the specified column.
   * @throws SQLException if an error occurs accessing the record table.
   * @throws UnsupportedOperationException the method is not implemented!
   */
  @Override public void updateCharacterStream(final String columnLabel, final Reader reader, final int length) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateObject(final String columnLabel, final Object x, final int scaleOrLength) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateObject(final String columnLabel, final Object x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Inserts the contents of the insert row into this ResultSet object and into the database. The cursor must be on the
   * insert row when this method is called.
   * @throws SQLException if an error occurs accessing the record table, if this method is called when the cursor is
   * not on the insert row, or if not all of non-nullable columns in the insert row have been given a value
   * @throws UnsupportedOperationException the method is not implemented.
   */
  @Override public void insertRow() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Updates the underlying database with the new contents of the current row of this ResultSet object. This method
   * cannot be called when the cursor is on the insert row.
   * @throws SQLException if an error occurs accessing the record table.
   * @throws UnsupportedOperationException the method is not implemented!
   */
  @Override public void updateRow() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Deletes the current row from this ResultSet object and from the underlying database. This method cannot be called
   * when the cursor is on the insert row.
   * @throws SQLException if an error occurs accessing the underlying Object array.
   */
  @Override public void deleteRow() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Refreshes the current row with its most recent value in the database. This method cannot be called when the cursor
   * is on the insert row.
   *
   * The refreshRow method provides a way for an application to explicitly tell the JDBC driver to refetch a row(s) from
   * the database. An application may want to call refreshRow when caching or prefetching is being done by the JDBC
   * driver to fetch the latest value of a row from the database. The JDBC driver may actually refresh multiple rows at
   * once if the fetch size is greater than one.
   *
   * All values are refetched subject to the transaction isolation level and cursor sensitivity. If refreshRow is called
   * after calling an updater method, but before calling the method updateRow, then the updates made to the row are
   * lost. Calling the method refreshRow frequently will likely slow performance.
   * @throws SQLException if an error occurs accessing the record table.
   * @throws UnsupportedOperationException the method is not implemented!
   */
  @Override public void refreshRow() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Cancels the updates made to the current row in this ResultSet object. This method may be called after calling
   * an updater method(s) and before calling the method updateRow to roll back the updates made to a row. If no updates
   * have been made or updateRow has already been called, this method has no effect.
   * @throws SQLException if an error occurs accessing the record table.
   * @throws UnsupportedOperationException the method is not implemented!
   */
  @Override public void cancelRowUpdates() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Moves the cursor to the insert row. The current cursor position is remembered while the cursor is positioned on the
   * insert row. The insert row is a special row associated with an updatable result set. It is essentially a buffer
   * where a new row may be constructed by calling the updater methods prior to inserting the row into the result set.
   * Only the updater, getter, and insertRow methods may be called when the cursor is on the insert row. All of the
   * columns in a result set must be given a value each time this method is called before calling insertRow. An updater
   * method must be called before a getter method can be called on a column value.
   * @throws SQLException if an error occurs accessing the record table.
   * @throws UnsupportedOperationException the method is not implemented!
   */
  @Override public void moveToInsertRow() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void moveToCurrentRow() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  /**
   * Retrieves the Statement object that produced this ResultSet object. If the result set was generated some other way,
   * such as by a DatabaseMetaData method, this method returns null.
   * @return the Statment object that produced this ResultSet object or null if the result set was produced some other way.
   * @throws SQLException if an error occurs accessing the record table.
   */
  @Override public Statement getStatement() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Object getObject(final int columnIndex, final Map<String, Class<?>> map) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Ref getRef(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Blob getBlob(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Clob getClob(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Array getArray(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Object getObject(final String columnLabel, final Map<String, Class<?>> map) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Ref getRef(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Blob getBlob(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Clob getClob(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Array getArray(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Date getDate(final int columnIndex, final Calendar cal) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Date getDate(final String columnLabel, final Calendar cal) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Time getTime(final int columnIndex, final Calendar cal) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Time getTime(final String columnLabel, final Calendar cal) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Timestamp getTimestamp(final int columnIndex, final Calendar cal) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Timestamp getTimestamp(final String columnLabel, final Calendar cal) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public URL getURL(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public URL getURL(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateRef(final int columnIndex, final Ref x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateRef(final String columnLabel, final Ref x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateBlob(final int columnIndex, final Blob x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateBlob(final String columnLabel, final Blob x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateClob(final int columnIndex, final Clob x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateClob(final String columnLabel, final Clob x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateArray(final int columnIndex, final Array x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateArray(final String columnLabel, final Array x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public RowId getRowId(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public RowId getRowId(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateRowId(final int columnIndex, final RowId x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateRowId(final String columnLabel, final RowId x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public int getHoldability() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean isClosed() throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateNString(final int columnIndex, final String nString) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateNString(final String columnLabel, final String nString) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateNClob(final int columnIndex, final NClob nClob) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateNClob(final String columnLabel, final NClob nClob) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public NClob getNClob(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public NClob getNClob(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public SQLXML getSQLXML(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public SQLXML getSQLXML(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateSQLXML(final int columnIndex, final SQLXML xmlObject) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateSQLXML(final String columnLabel, final SQLXML xmlObject) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public String getNString(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public String getNString(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Reader getNCharacterStream(final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public Reader getNCharacterStream(final String columnLabel) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateNCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateNCharacterStream(final String columnLabel, final Reader reader, final long length) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateAsciiStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateBinaryStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateAsciiStream(final String columnLabel, final InputStream x, final long length) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateBinaryStream(final String columnLabel, final InputStream x, final long length) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateCharacterStream(final String columnLabel, final Reader reader, final long length) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateBlob(final int columnIndex, final InputStream inputStream, final long length) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateBlob(final String columnLabel, final InputStream inputStream, final long length) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateClob(final int columnIndex, final Reader reader, final long length) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateClob(final String columnLabel, final Reader reader, final long length) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateNClob(final int columnIndex, final Reader reader, final long length) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateNClob(final String columnLabel, final Reader reader, final long length) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateNCharacterStream(final int columnIndex, final Reader x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateNCharacterStream(final String columnLabel, final Reader reader) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateAsciiStream(final int columnIndex, final InputStream x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateBinaryStream(final int columnIndex, final InputStream x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateCharacterStream(final int columnIndex, final Reader x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateAsciiStream(final String columnLabel, final InputStream x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateBinaryStream(final String columnLabel, final InputStream x) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateCharacterStream(final String columnLabel, final Reader reader) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateBlob(final int columnIndex, final InputStream inputStream) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateBlob(final String columnLabel, final InputStream inputStream) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateClob(final int columnIndex, final Reader reader) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateClob(final String columnLabel, final Reader reader) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateNClob(final int columnIndex, final Reader reader) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public void updateNClob(final String columnLabel, final Reader reader) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public <T> T unwrap(final Class<T> iface) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  @Override public boolean isWrapperFor(final Class<?> iface) throws SQLException {
    throw new UnsupportedOperationException("Not Implemented!");
  }

}
