/*
 * RecordTable.java (c) 12 May 2002
 *
 * The RecordTable data structure mimics a database table of records, or a spreadsheet consisting
 * of rows and columns that describe a logical unit of data, the record.
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.11.29
 * @see java.lang.Cloneable
 * @see java.io.Serializable
 * @see java.util.Collection
 * @see com.cp.common.lang.Copyable
 * @see com.cp.common.lang.Mutable
 * @see com.cp.common.lang.Searchable
 * @see com.cp.common.lang.Sortable
 * @see com.cp.common.lang.Visitable
 * @see com.cp.common.util.record.Column
 * @see com.cp.common.util.record.AbstractRecordTable
 * @see com.cp.common.util.record.Record
 * @see com.cp.common.util.record.RecordAdapter
 * @see com.codeprimate.util.record.DefaultRecordTable
 */

package com.cp.common.util.record;

import com.cp.common.lang.Copyable;
import com.cp.common.lang.Mutable;
import com.cp.common.lang.Searchable;
import com.cp.common.lang.Sortable;
import com.cp.common.lang.Visitable;
import com.cp.common.util.record.*;
import com.cp.common.util.record.Column;
import com.cp.common.util.record.Record;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public interface RecordTable extends Cloneable, Collection<Record>, Copyable, Mutable, Searchable<Record>, Serializable, Sortable<Record>, Visitable {

  /**
   * Adds the specified Column as the last Column in the record table, thereby altering the structure of this
   * record table. Default values specified by the Column will be set for all rows in the record table.
   * @param column the Column being inserted.  The Column is appended as the last column in this record table.
   * @return a boolean value indicating whether the column was added successfully.
   */
  public boolean addColumn(Column column);

  /**
   * Adds the specified Record object (row) as the last row in this record table.
   * @param row the Record object added to this record table.
   * @return a boolean value indicating whether the row was added successfully.
   */
  public boolean addRow(Record row);

  /**
   * Returns the number of columns in this record table, specified as a integer value.
   * @return the number of columns in this record table.
   */
  public int columnCount();

  /**
   * Returns an Iterator over the columns in this record table.
   * @return an Iterator to traverse the columns in this record table.
   */
  public Iterator<Column> columnIterator();

  /**
   * Determines whether this record table contains the specified Column object (whether the Column
   * is a column of this record table).
   * @param column the Column object being tested as a column of this record table.
   * @return a boolean value indication whether the specified Column is a column of this record table.
   */
  public boolean contains(Column column);

  /**
   * Determines whether this record table contains the specified Record object (whether the Record
   * is a row in this record table).
   * @param record a Record to test for containment by this record table.
   * @return a boolean value indicating whether the Record object is contained, or is a row, of this
   * record table.
   */
  public boolean contains(Record record);

  /**
   * Returns the value of the specified cell at (rowIndex, columnIndex) in this record table.
   * @param rowIndex the index of the row.
   * @param columnIndex the index of the column
   * @return the value of the cell in this record table at (rowIndex, columnIndex).
   */
  public <T> T getCellValue(int rowIndex, int columnIndex);

  /**
   * Returns the value of the specified cell in the record table given it's rowIndex and Column.
   * @param rowIndex the index of the row.
   * @param column the Column in this record table.
   * @return the value of the cell in this record table at the specified row index and Column.
   */
  public <T> T getCellValue(int rowIndex, Column column);

  /**
   * Returns the Column object at the specified column index in this record table.
   * @param columnIndex the index of the column in this record table for which a
   * Column object is returned.
   * @return a Column object for the column at index in this record table.
   */
  public Column getColumn(int columnIndex);

  /**
   * Returns the Column object having the specified name in this record table.
   * @param columnName the name of the column in this record table.
   * @return the Column object having the specified column name.
   */
  public Column getColumn(String columnName);

  /**
   * Returns the index of the specified Column object within this record table.
   * @param column the Column object in this record table to return an index for.
   * @return the index of the Column object in this record table or a -1 if the column
   * does not exist in the record table.
   */
  public int getColumnIndex(Column column);

  /**
   * Returns the list of columns making up the structure of this record table.
   * @return the list of columns making up the structure of this record table.
   */
  public List<Column> getColumns();

  /**
   * Returns an appropriate Comparator to compare row values for the specified Column.
   * @param column the Column in which the Comparator is used to compare values of the various rows.
   * @param value the Object value who's type is used to lookup a Comparator appropriate for
   * comparing values of that type.
   * @return a Comparator object based on the Column, or the value if the Column does not specify a
   * Comparator object.
   */
  public Comparator getComparator(Column column, Object value);

  /**
   * Returns the first column in this record table as a Column instance.
   * @return a Column object representing the first column in this record table.
   */
  public Column getFirstColumn();

  /**
   * Returns the index of the first column in this record table, or a -1 if the record
   * table does not contain any columns.
   * @return an integer value specifying the index of the first column in this record table.
   */
  public int getFirstColumnIndex();

  /**
   * Returns the first row (Record) in this record table as a Record instance.
   * @return a Record representing the first row in this record table.
   */
  public Record getFirstRow();

  /**
   * Returns the index of the first row in this record table, or a -1 if the record
   * table does not contain any rows.
   * @return an integer value specifying the index of the first row in this record table.
   */
  public int getFirstRowIndex();

  /**
   * Returns the last column in this record table as a Column instance.
   * @return a Column object representing the last column in this record table.
   */
  public Column getLastColumn();


  /**
   * Returns the index of the last column in this record table, or a -1 if the record
   * table does not contain any columns.
   * @return an integer value specifying the index of the last column in this record table.
   */
  public int getLastColumnIndex();

  /**
   * Returns the last row (Record) in this record table as a Record instance.
   * @return a Record representing the last row in this record table.
   */
  public Record getLastRow();

  /**
   * Returns the index of the last row in this record table, or a -1 if the record
   * table does not contain any rows.
   * @return an integer value specifying the index of the last row in this record table.
   */
  public int getLastRowIndex();

  /**
   * Returns the row in this record table at index as a Record object.
   * @param rowIndex the index of the row in this record table to get a Record object for.
   * @return a Record object for the row at index in this record table.
   */
  public Record getRow(int rowIndex);

  /**
   * Returns the row index of the Record object in this record table.
   * @param row the Record object for which the row index is being returned.
   * @return an integer index of the row for the specified Record object or a -1
   * if the row is not contained in the record table.
   */
  public int getRowIndex(Record row);

  /**
   * Inserts the specified Column at the specified column index into this record table.
   * @param column the Column to insert into the record table.
   * @param columnIndex the column position in the record table in which to insert the Column.
   * @throws java.lang.IndexOutOfBoundsException if the columnIndex is less than 0 or greater than
   * the number of columns in this record table.
   * @return a boolean value indicating whether the column was inserted successfully.
   */
  public boolean insertColumn(Column column, int columnIndex);

  /**
   * Inserts the specified Record object at the specified row index into this record table.
   * @param record the Record object to insert into this record table.
   * @param rowIndex the index of the row in which to insert the Record.
   * @throws java.lang.IndexOutOfBoundsException if the rowIndex is less than 0 or greater than
   * the number of rows in this record table.
   * @return a boolean value indicating whether the row was inserted successfully.
   */
  public boolean insertRow(Record record, int rowIndex);

  /**
   * Registers the specified Comparator object for the specified class type.  During a comparison of Objects
   * of the specified type, the specified Comparator will be used to determine the relationship.
   * @param type the Class type of Objects for which the specified Comparator should be used.
   * @param comparator the Comparator instance used to the compare Objects of the specified type.
   */
  public void registerComparator(Class type, Comparator comparator);

  /**
   * Removes all rows from this record table.
   * @return a boolean value indicating whether the operation altered the record table.
   */
  public boolean removeAll();

  /**
   * Removes the column at the specified column index from this record table.
   * @param columnIndex the index of the column to remove from this record table.
   * @return the Column object removed from this record table.
   * @throws java.lang.IndexOutOfBoundsException if the column index is not a valid index to a column
   * in this record table.
   */
  public Column removeColumn(int columnIndex);

  /**
   * Removes the specified Column from this record table.
   * @param column the Column in this record table to remove.
   * @return the column index of the Column just removed from this record table, or -1 if the Column
   * was not a column in this record table.
   */
  public int removeColumn(com.cp.common.util.record.Column column);

  /**
   * Removes the record at the specified row index from this record table.
   * @param rowIndex an index in this record table of the row to remove.
   * @return a Record object of the row removed from this record table.
   */
  public Record removeRow(int rowIndex);

  /**
   * Removes the specified Record (row) from this record table.
   * @param record the row, specified as a Record, to remove from this record table.
   * @return the row index of the specified Record (row) in this record table, or a -1 if the specified
   * Record (row) does not exist in this record table.
   */
  public int removeRow(Record record);

  /**
   * Returns the number of rows (Records) in this record table.
   * @return a integer value of the number of rows contained in this record table.
   */
  public int rowCount();

  /**
   * Returns a row Iterator to traverse the rows in this record table.
   * @return an Iterator over the rows in this record table.
   */
  public Iterator<Record> rowIterator();

  /**
   * Sets the specified cell at (rowIndex, columnIndex) in this record table with the specified Object value.
   * @param rowIndex the index of the row.
   * @param columnIndex the index of the column.
   * @param value the Object value to set on the cell in this record table at the specied rowIndex and
   * columnIndex.
   * @throws java.lang.IndexOutOfBoundsException if the rowIndex or columnIndex is less than 0 or
   * greater than the number of rows or columns respectively.
   */
  public void setCellValue(int rowIndex, int columnIndex, Object value);

  /**
   * Sets the cell in this record table given the row index and Column with the given Object value.
   * @param rowIndex the index of the row.
   * @param column the Column in this record table.
   * @param value the Object value to set on the cell in this record table at the specied rowIndex and
   * columnIndex.
   * @throws java.lang.IndexOutOfBoundsException if the rowIndex or columnIndex is less than 0 or
   * greater than the number of rows or columns respectively.
   */
  public void setCellValue(int rowIndex, Column column, Object value);

  /**
   * Returns a tabular data structure of this record table containing all row and column values.
   * @return a two-dimensional Object array containing the values of this record table.
   */
  public Object[][] toTabular();

  /**
   * Returns a tabular data structure of this record table for the specified rows containing values
   * from all Columns.
   * @param rowIndices the specified rows to include in the tabular data structure.
   * @return a two-dimensional Object array of the values from this record table for the specified rows
   * and all Columns.
   */
  public Object[][] toTabular(int[] rowIndices);

  /**
   * Returns a tabular data structure of this record table for all rows containing only values from the
   * specified Columns.
   * @param columns the Columns of the tabular data structure.
   * @return a two-dimensional Object array of the values from this record table for the specified Columns.
   */
  public Object[][] toTabular(Column[] columns);

  /**
   * Returns a tabular data structure of this record table for the specified rows containing only values
   * from the specified Columns.
   * @param rowIndices the specified rows to include in the tabular data structure.
   * @param columns the Columns of the tabular data structure.
   * @return a two-dimensional Object array of the values from this record table for the specified rows
   * and Columns.
   */
  public Object[][] toTabular(int[] rowIndices, Column[] columns);

  /**
   * Unregisters the Comparator object for the specified class type.  If the class type was not mapped to a
   * Comparator object, then the method returns null.
   * @param type the class type for which the Comparator was registered.
   * @return a the Comparator registered with the specified class type.
   */
  public Comparator unregisterComparator(Class type);

}
