/*
 * AbstractRecordTable.java (c) 17 April 2002
 *
 * The AbstractRecordTable class extends the AbstractCollection interface and implements
 * the RecordTable interface. To implement the AbstractRecordTable class, a subclass must
 * define the following methods:
 *
 * columnIterator():Iterator
 * addColumn(:Column, columnIndex:int)
 * addRow(:Record, rowIndex:int)
 * rowIterator():Iterator
 * Sortable.set(object:Object, index:int)
 *
 * Copyright (c) 2002, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see com.cp.common.util.record.Column
 * @see com.cp.common.util.InvalidColunValueException
 * @see com.cp.common.util.InvalidColunValueSizeException
 * @see com.cp.common.util.InvalidColunValueTypeException
 * @see com.cp.common.util.record.NonUniqueColumnValueException
 * @see com.cp.common.util.record.NullColumnValueException
 * @see com.cp.common.util.record.Record
 * @see com.cp.common.util.record.RecordAdapter
 * @see com.cp.common.util.record.RecordTable
 * @see com.codeprimate.util.record.DefaultRecordTable
 * @see java.util.AbstractCollection
 */

package com.cp.common.util.record;

import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.Mutable;
import com.cp.common.lang.ObjectImmutableException;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.Visitor;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractRecordTable extends AbstractCollection<Record> implements RecordTable {

  // State variable used to toggle the record table's read-only or modifiable state.
  private boolean mutable = Mutable.MUTABLE;

  // Logger object used to track the state, actions and events of the AbstractRecordTable instance.
  protected final Log log = LogFactory.getLog(getClass());

  // Map object used to track registered Comparators based on value types.
  private Map<Class, Comparator> comparatorMap = new HashMap<Class, Comparator>();

  /**
   * Creates an instance of the AbstractRecordTable class.
   */
  public AbstractRecordTable() {
  }

  /**
   * The Object implementing this interface defines the accept method to allow the Visitor
   * to perform it's operations on this object by calling the visit method.  This Visitor
   * will then determine if the operation should be applied to this type of object pending
   * it's function.
   * @param visitor the Visitor with the operation to perform on this object.
   * @see com.cp.common.lang.Visitable#accept
   */
  public void accept(final Visitor visitor) {
    visitor.visit(this);

    for (Iterator<Record> it = rowIterator(); it.hasNext(); ) {
      it.next().accept(visitor);
    }
  }

  /**
   * Ensures that this collection contains the specified element.  It is expected that the
   * Object parameter implements the Record interface.
   * @param record the Record object to add to the collection of Records in this RecordTable.
   * @return a boolean value inidicating whether the Record object was succcessfully added
   * to this RecordTable object.
   * @see java.util.Collection#add
   */
  public final boolean add(final Record record) {
    if (log.isDebugEnabled()) {
      log.debug("adding row (Record) (" + record + ")");
    }
    return addRow(record);
  }

  /**
   * Adds all of the elements in the specified collection to this RecordTable object.  It is
   * expected that the collection contain Objects that implement the Record interface.
   * @param c the collection of Record objects to add to this RecordTable.
   * @return a boolean value if the RecordTable changed as a result of this call.
   * @see java.util.Collection#addAll
   */
  public final boolean addAll(final Collection<? extends Record> c) {
    return super.addAll(c);
  }

  /**
   * Adds the specified Column as the last Column in the record table, thereby altering the structure of this
   * record table. Default values specified by the Column will be set for all rows in the record table.
   * @param column the Column being inserted.  The Column is appended as the last column in this record table.
   * @return a boolean value indicating whether the column was added successfully.
   */
  public boolean addColumn(final Column column) {
    if (log.isDebugEnabled()) {
      log.debug("adding column (" + column + ")");
    }
    return insertColumn(column, columnCount());
  }

  /**
   * Adds the specified Record object (row) as the last row in this record table.
   * @param row the Record object added to this record table.
   * @return a boolean value indicating whether the row was added successfully.
   */
  public boolean addRow(final Record row) {
    if (log.isDebugEnabled()) {
      log.debug("adding row (" + row + ")");
    }
    return insertRow(row, rowCount());
  }

  /**
   * Removes all of the Records (rows) from this record table.  The struture, or Columns, of the
   * record table are left intact.
   * @see java.util.Collection#clear
   */
  public final void clear() {
    super.clear();
  }

  /**
   * Constructs a clone of this RecordTable object implemented using a shallow-copy.  The cloned
   * RecordTable will structure and set the column and cell values to refer to the same content
   * as the original RecordTable object.  The implementation of this method calls the copy method
   * of the Copyable interface.
   * @return a clone of this RecordTable object.
   * @throws CloneNotSupportedException if the clone operation is not supported.
   * @see java.lang.Cloneable
   * @see com.cp.common.util.record.AbstractRecordTable#copy
   */
  public final Object clone() throws CloneNotSupportedException {
    return copy();
  }

  /**
   * Returns the number of columns in this record table, specified as a integer value.
   * @return the number of columns in this record table.
   */
  public int columnCount() {
    return getColumns().size();
  }

  /**
   * Determines whether this record table contains the specified Column object (whether the Column
   * is a column of this record table).
   * @param column the Column object being tested as a column of this record table.
   * @return a boolean value indication whether the specified Column is a column of this record table.
   */
  public boolean contains(final Column column) {
    if (log.isDebugEnabled()) {
      log.debug("contains column (" + column + ")");
    }
    return getColumns().contains(column);
  }

  /**
   * Returns true if this RecordTable contains the specified object parameter that implements
   * the Record interface..
   * @param obj the object implementing the Record interface and being tested for containment
   * by this RecordTable object.
   * @return a boolean value if the object parameter (Record) is contained by this RecordTable
   * object, false otherwise.
   * @see java.util.Collection#contains
   */
  public final boolean contains(final Object obj) {
    return super.contains(obj);
  }

  /**
   * Determines whether this record table contains the specified Record object (whether the Record
   * is a row in this record table).
   * @param record a Record to test for containment by this record table.
   * @return a boolean value indicating whether the Record object is contained, or is a row, of this
   * record table.
   */
  public boolean contains(final Record record) {
    if (log.isDebugEnabled()) {
      log.debug("contains record (" + record + ")");
    }

    for (Iterator<Record> it = rowIterator(); it.hasNext(); ) {
      if (it.next().equals(record)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns true if this RecordTable contains all of the elements in the specified collection.
   * @param c the collection of objects being tested for containment by this RecordTable object.
   * @return true if the RecordTable contains all the objects in the collection, false otherwise.
   * @see java.util.Collection#containsAll
   */
  public final boolean containsAll(final Collection c) {
    return super.containsAll(c);
  }

  /**
   * Implementation of the Searchable.get(I) and Sortable.get(I) methods.  This method returns
   * the row, or Record object, at index in this record table.
   * @param index the row index in this record table.
   * @return the Record object signifying the row at index in this record table.
   * @see com.cp.common.lang.Searchable#get
   * @see com.cp.common.lang.Sortable#get
   */
  public Record get(final int index) {
    if (log.isDebugEnabled()) {
      log.debug("index (" + index + ")");
    }
    return getRow(index);
  }

  /**
   * Returns the value of the specified cell in the record table, identified at the following
   * row index and column index.
   * WARNING: this method makes no effort to guard against modification of the value for the
   * specified column and current row if the record table is immutable.  In addition, the method
   * does not make defensive copies of the value to ensure uniqueness amongst values in a column
   * that constrains values to be unique.  It is left to the implementations or caller to ensure
   * the integrity of the record table.
   * @param rowIndex the index of the row.
   * @param columnIndex the index of the column
   * @return the value of the cell in this record table at (rowIndex, columnIndex).
   */
  public <T> T getCellValue(final int rowIndex, final int columnIndex) {
    if (log.isDebugEnabled()) {
      log.debug("returning value of table cell (" + rowIndex + ", " + columnIndex + ")");
    }

    validateRowIndex(rowIndex);
    validateColumnIndex(columnIndex);

    final Record record = getRow(rowIndex);

    if (log.isDebugEnabled()) {
      log.debug("record (" + record + ")");
    }

    return (T) record.getValue(columnIndex);
  }

  /**
   * Returns the value of the specified cell in the record table given it's rowIndex and Column.
   * @param rowIndex the index of the row.
   * @param column the Column in this record table.
   * @return the value of the cell in this record table at the specified row index and Column.
   */
  public <T> T getCellValue(final int rowIndex, final com.cp.common.util.record.Column column) {
    return (T) getCellValue(rowIndex, getColumnIndex(column));
  }

  /**
   * Returns the Column object at the specified column index in this record table.
   * @param columnIndex the index of the column in this record table for which a
   * Column object is returned.
   * @return a Column object for the column at index in this record table.
   */
  public Column getColumn(final int columnIndex) {
    if (log.isDebugEnabled()) {
      log.debug("columnIndex (" + columnIndex + ")");
    }

    validateColumnIndex(columnIndex);

    return getColumns().get(columnIndex);
  }

  /**
   * Returns the Column object having the specified name in this record table.
   * @param columnName the name of the column in this record table.
   * @return the Column object having the specified column name.
   */
  public Column getColumn(final String columnName) {
    if (log.isDebugEnabled()) {
      log.debug("columnName (" + columnName + ")");
    }

    for (Iterator<Column> it = columnIterator(); it.hasNext(); ) {
      final Column column = it.next();
      if (ObjectUtil.equals(column.getName(), columnName)) {
        return column;
      }
    }

    return null;
  }

  /**
   * Returns the index of the specified Column object within this record table.
   * @param column the Column object in this record table to return an index for.
   * @return the index of the Column object in this record table or a -1 if the column
   * does not exist in the record table.
   */
  public int getColumnIndex(final Column column) {
    if (log.isDebugEnabled()) {
      log.debug("column (" + column + ")");
    }
    // TODO: refactor implementation to lookup Columns by name and not by the Column.equals method.
    // NOTE: Columns in the record table are unique by name.
    return getColumns().indexOf(column);
  }

  /**
   * Returns the list of columns making up the structure of this record table.
   * @return the list of columns making up the structure of this record table.
   */
  public List<Column> getColumns() {
    final List<Column> columns = new LinkedList<Column>();

    for (Iterator<Column> it = columnIterator(); it.hasNext(); ) {
      columns.add(it.next());
    }

    return java.util.Collections.<Column>unmodifiableList(columns);
  }

  /**
   * Determines the value for the specified Column for this row in the RecordTable by checking the
   * nullable property on the Column.
   * @param column the Column in this RecordTable for which the value is being inserted.
   * @param value the value being inserted into this RecordTable at the specified Column.
   * @return the actual Object value to be inserted into the specified column of this RecordTable.
   */
  protected static Object getColumnValue(final Column column, final Object value) {
    if (ObjectUtil.isNull(value) && !column.isUnique()) {
      return column.getDefaultValue();
    }

    return value;
  }

  /**
   * Returns an appropriate Comparator to compare row values for the specified Column.
   * @param column the Column in which the Comparator is used to compare values of the various rows.
   * @param value the Object value who's type is used to lookup a Comparator appropriate for
   * comparing values of that type.
   * @return a Comparator object based on the Column, or the value if the Column does not specify a
   * Comparator object.
   */
  public Comparator getComparator(final Column column, final Object value) {
    Comparator comparator = column.getComparator();

    if (ObjectUtil.isNull(comparator)) {
      final Class valueType = (ObjectUtil.isNull(value) ? null : value.getClass());

      if (log.isDebugEnabled()) {
        log.debug("valueType (" + valueType + ")");
      }

      comparator = comparatorMap.get(valueType);

      if (ObjectUtil.isNull(comparator) && ObjectUtil.isNotNull(valueType)) {
        for (Class type : comparatorMap.keySet()) {
          if (type.isAssignableFrom(valueType)) {
            return comparatorMap.get(type);
          }
        }
      }
    }

    return comparator;
  }

  /**
   * Returns the first column in this record table as a Column instance.
   * @return a Column object representing the first column in this record table.
   */
  public Column getFirstColumn() {
    return (columnCount() > 0 ? getColumn(0) : null);
  }

  /**
   * Returns the index of the first column in this record table, or a -1 if the record
   * table does not contain any columns.
   * @return an integer value specifying the index of the first column in this record table.
   */
  public int getFirstColumnIndex() {
    return (columnCount() > 0 ? 0 : -1);
  }

  /**
   * Returns the first row (Record) in this record table as a Record instance.
   * @return a Record representing the first row in this record table.
   */
  public com.cp.common.util.record.Record getFirstRow() {
    return (isEmpty() ? null : getRow(0));
  }

  /**
   * Returns the index of the first row in this record table, or a -1 if the record
   * table does not contain any rows.
   * @return an integer value specifying the index of the first row in this record table.
   */
  public int getFirstRowIndex() {
    return (isEmpty() ? -1 : 0);
  }

  /**
   * Returns the last column in this record table as a Column instance.
   * @return a Column object representing the last column in this record table.
   */
  public Column getLastColumn() {
    return (columnCount() > 0 ? getColumn(columnCount() - 1) : null);
  }

  /**
   * Returns the index of the last column in this record table, or a -1 if the record
   * table does not contain any columns.
   * @return an integer value specifying the index of the last column in this record table.
   */
  public int getLastColumnIndex() {
    return (columnCount() - 1);
  }

  /**
   * Returns the last row (Record) in this record table as a Record instance.
   * @return a Record representing the last row in this record table.
   */
  public Record getLastRow() {
    return (isEmpty() ? null : getRow(rowCount() - 1));
  }

  /**
   * Returns the index of the last row in this record table, or a -1 if the record
   * table does not contain any rows.
   * @return an integer value specifying the index of the last row in this record table.
   */
  public int getLastRowIndex() {
    return (rowCount() - 1);
  }

  /**
   * Returns the row in this record table at index as a Record object.
   * @param rowIndex the index of the row in this record table to get a Record object for.
   * @return a Record object for the row at index in this record table.
   */
  public Record getRow(final int rowIndex) {
    if (log.isDebugEnabled()) {
      log.debug("rowIndex (" + rowIndex + ")");
    }

    validateRowIndex(rowIndex);

    Record row = null;
    int currentRowIndex = 0;

    for (Iterator<Record> it = rowIterator(); it.hasNext(); currentRowIndex++) {
      row = it.next();
      if (currentRowIndex == rowIndex) {
        break;
      }
    }

    return row;
  }

  /**
   * Returns the row index of the Record object in this record table.
   * @param row the Record object for which the row index is being returned.
   * @return an integer index of the row for the specified Record object or a -1
   * if the row is not contained in the record table.
   */
  public int getRowIndex(final Record row) {
    if (log.isDebugEnabled()) {
      log.debug("row (" + row + ")");
    }

    int rowIndex = 0;

    for (Iterator<Record> it = rowIterator(); it.hasNext(); rowIndex++) {
      if (ObjectUtil.equals(it.next(), row)) {
        return rowIndex;
      }
    }

    return -1;
  }

  /**
   * Returns true if this RecordTable contains no Records.
   * @return a boolean value indicating if the RecordTable contains Records or not.
   * @see java.util.Collection#isEmpty
   */
  public final boolean isEmpty() {
    return super.isEmpty();
  }

  /**
   * Determines whether the record table is modifiable or read-only.
   * @return boolean value indicating whether this record table is modifiable or read-only.
   * @see com.cp.common.lang.Mutable#isMutable
   */
  public boolean isMutable() {
    return mutable;
  }

  /**
   * Returns an iterator over the Records contained in this RecordTable.
   * @return a Iterator object that traverses the Records contained by this RecordTable.
   * @see java.util.Collection#iterator()
   */
  public final Iterator<Record> iterator() {
    return rowIterator();
  }

  /**
   * Registers the specified Comparator object for the specified class type.  During a comparison of Objects
   * of the specified type, the specified Comparator will be used to determine the relationship.
   * @param type the Class type of Objects for which the specified Comparator should be used.
   * @param comparator the Comparator instance used to the compare Objects of the specified type.
   */
  public void registerComparator(final Class type, final Comparator comparator) {
    if (ObjectUtil.isNull(type)) {
      log.warn("Cannot register a Comparator with a NULL type!");
      throw new NullPointerException("Cannot register a Comparator with a NULL type!");
    }
    comparatorMap.put(type, comparator);
  }

  /**
   * Removes the specified Record from this record table if present.
   * @param obj the object element (Record) to remove from this RecordTable.
   * @return a boolean value indicating if the Record was removed from this record table.
   * @see java.util.Collection#remove
   */
  public final boolean remove(Object obj) {
    return super.remove(obj);
  }

  /**
   * Removes all rows from this record table.
   * @return a boolean value indicating whether the operation altered the record table.
   */
  public boolean removeAll() {
    for (Iterator it = rowIterator(); it.hasNext(); ) {
      it.next();
      it.remove();
    }
    return true;
  }

  /**
   * Removes from this RecordTable all of its elements that are contained in the specified collection.
   * This method only removes those elements in the Collection that exist in this record table
   * (obviously, it cannot remove what is not there).
   * @param c specifies the collection of elements to be removed from this RecordTable.
   * @return a boolean value indicating whether the RecordTable object changed as a result of the
   * removeAll operation.
   * @see java.util.Collection#removeAll
   */
  public final boolean removeAll(final Collection c) {
    return super.removeAll(c);
  }

  /**
   * Removes the Column at the specified column index from the record table.  Note, this is the responsibility
   * of the implementing class of AbstractRecordTable for maintaining the integrity between the number
   * of fields in the Records of the table with the number of columns in the record table.
   * @param columnIndex the index of the column to remove from this record table.
   * @return the Column object removed from this record table.
   * @throws java.lang.IndexOutOfBoundsException if the column index is not a valid index to a column
   * in this record table.
   */
  public Column removeColumn(final int columnIndex) {
    if (log.isDebugEnabled()) {
      log.debug("removing column @ index (" + columnIndex + ")");
    }

    validateColumnIndex(columnIndex);

    int currentColumnIndex = 0;
    Column column = null;

    for (Iterator<Column> it = columnIterator(); it.hasNext(); currentColumnIndex++) {
      column = it.next();
      if (currentColumnIndex == columnIndex) {
        // be wary of infinite recursion; The Iterator.remove method of Column Iterator
        // should not be implemented in terms of removeColumn(columnIndex:int) or in terms of removeColumn(:Column)
        it.remove();
        break;
      }
    }

    return column;
  }

  /**
   * Removes the specified Column from this record table.
   * @param column the Column in this record table to remove.
   * @return the column index of the Column just removed from this record table, or -1 if the Column
   * was not a column in this record table.
   */
  public int removeColumn(final Column column) {
    final int columnIndex = getColumnIndex(column);
    removeColumn(columnIndex);
    return columnIndex;
  }

  /**
   * Removes the record at the specified row index from this record table.
   * @param rowIndex an index in this record table of the row to remove.
   * @return a Record object of the row removed from this record table.
   */
  public Record removeRow(final int rowIndex) {
    if (log.isDebugEnabled()) {
      log.debug("removing row @ index (" + rowIndex + ")");
    }

    validateRowIndex(rowIndex);

    int currentRowIndex = 0;
    Record record = null;

    for (Iterator<Record> it = rowIterator(); it.hasNext(); currentRowIndex++) {
      record = it.next();
      if (currentRowIndex == rowIndex) {
        it.remove();
        break;
      }
    }

    return record;
  }

  /**
   * Removes the specified Record (row) from this record table.
   * @param record the row, specified as a Record, to remove from this record table.
   * @return the row index of the specified Record (row) in this record table, or a -1 if the specified
   * Record (row) does not exist in this record table.
   */
  public int removeRow(final Record record) {
    final int rowIndex = getRowIndex(record);
    removeRow(rowIndex);
    return rowIndex;
  }

  /**
   * Retains only the elements in this Collection that are contained in this record table.
   * @param c the specified collection of elements to retain in this RecordTable where all
   * other elements are to be removed.
   * @return a boolean value indicating whether the RecordTable object changed as a result
   * of the retainAll operation.
   * @see java.util.Collection#retainAll
   */
  public final boolean retainAll(final Collection c) {
    return super.retainAll(c);
  }

  /**
   * Returns the number of rows (Records) in this record table.
   * @return a integer value of the number of rows contained in this record table.
   */
  public int rowCount() {
    int rowCount = 0;
    for (Iterator it = rowIterator(); it.hasNext(); rowCount++) {
      it.next();
    }
    return rowCount;
  }

  /**
   * Sets the specified cell at (rowIndex, columnIndex) in this record table with the specified Object value.
   * @param rowIndex the index of the row.
   * @param columnIndex the index of the column.
   * @param value the Object value to set on the cell in this record table at the specied rowIndex and
   * columnIndex.
   * @throws java.lang.IndexOutOfBoundsException if the rowIndex or columnIndex is less than 0 or
   * greater than the number of rows or columns respectively.
   */
  public void setCellValue(final int rowIndex, final int columnIndex, Object value) {
    if (log.isDebugEnabled()) {
      log.debug("setting value (" + value + ") of table cell (" + rowIndex + ", " + columnIndex + ")");
    }

    validateRowIndex(rowIndex);
    validateColumnIndex(columnIndex);

    final Column column = getColumn(columnIndex);

    value = getColumnValue(column, value);
    validateColumnValue(column, value);
    getRow(rowIndex).setValue(columnIndex, value);
  }

  /**
   * Sets the cell in this record table given the row index and Column with the given Object value.
   * @param rowIndex the index of the row.
   * @param column the Column in this record table.
   * @param value the Object value to set on the cell in this record table at the specied rowIndex and
   * columnIndex.
   * @throws java.lang.IndexOutOfBoundsException if the rowIndex or columnIndex is less than 0 or
   * greater than the number of rows or columns respectively.
   */
  public void setCellValue(final int rowIndex, final Column column, final Object value) {
    setCellValue(rowIndex, getColumnIndex(column), value);
  }

  /**
   * Sets the state of the mutable propertty of this RecordTable object indicating whether
   * this object is modifiable or read-only.
   * @param mutable a boolean value indicating the state of the mutable property of this
   * RecordTable object.
   * @see com.cp.common.lang.Mutable#setMutable
   */
  public void setMutable(final boolean mutable) {
    if (log.isDebugEnabled()) {
      log.debug("mutable (" + mutable + ")");
    }
    this.mutable = mutable;
  }

  /**
   * Returns the number of rows in the RecordTable data structure.
   * @return a integer value indicating the number of rows, or Records, in this record table.
   * @see java.util.Collection#size
   */
  public final int size() {
    return rowCount();
  }

  /**
   * Returns an array containing all of the Records in this record table.
   * @return an array of Records contained in this record table.
   * @see java.util.Collection#toArray
   */
  public final Object[] toArray() {
    return super.toArray();
  }

  /**
   * Returns an array containing all of the Records in this RecordTable object; the runtime type
   * of the returned array is that of the specified array.
   * @param a the array into which the Records of the RecordTable are to be stored, if it is big enough;
   * otherwise, a new array of the same runtime type is allocated for this purpose.
   * @return an array of Records, of the specified type, contained by this record table.
   * @see java.util.Collection#toArray
   */
  public final Object[] toArray(final Object[] a) {
    return super.toArray(a);
  }

  /**
   * Returns a String representation of the record table and it's contents.
   * @return a String representation of this record table.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{columns [");

    for (Iterator it = columnIterator(); it.hasNext(); ) {
      buffer.append(it.next().toString());
      buffer.append(it.hasNext() ? ", " : "");
    }

    buffer.append("] records [");

    for (Iterator it = rowIterator(); it.hasNext(); ) {
      buffer.append(it.next().toString());
      buffer.append(it.hasNext() ? ", " : "");
    }

    buffer.append("]}:").append(getClass().getName());

    return buffer.toString();
  }

  /**
   * Returns a tabular data structure of this record table containing all row and column values.
   * @return a two-dimensional Object array containing the values of this record table.
   */
  public Object[][] toTabular() {
    return toTabular(getColumns().<Column>toArray(new Column[columnCount()]));
  }

  /**
   * Returns a tabular data structure of this record table for the specified rows containing values
   * from all Columns.
   * @param rowIndices the specified rows to include in the tabular data structure.
   * @return a two-dimensional Object array of the values from this record table for the specified rows
   * and all Columns.
   */
  public Object[][] toTabular(final int[] rowIndices) {
    return toTabular(rowIndices, getColumns().<com.cp.common.util.record.Column>toArray(new Column[columnCount()]));
  }

  /**
   * Returns a tabular data structure of this record table for all rows containing only values from the
   * specified Columns.
   * @param columns the Columns of the tabular data structure.
   * @return a two-dimensional Object array of the values from this record table for the specified Columns.
   */
  public Object[][] toTabular(final Column[] columns) {
    final int[] rowIndices = new int[rowCount()];

    for (int index = 0; index < rowIndices.length; index++) {
      rowIndices[index] = index;
    }

    return toTabular(rowIndices, columns);
  }

  /**
   * Returns a tabular data structure of this record table for the specified rows containing only values
   * from the specified Columns.
   * @param rowIndices the specified rows to include in the tabular data structure.
   * @param columns the Columns of the tabular data structure.
   * @return a two-dimensional Object array of the values from this record table for the specified rows
   * and Columns.
   */
  public Object[][] toTabular(final int[] rowIndices, final Column[] columns) {
    if (ObjectUtil.isNull(rowIndices)) {
      log.warn("The row indices of the record table to include as rows in the tabular data structure cannot be null!");
      throw new NullPointerException("The row indices of the record table to include as rows in the tabular data structure cannot be null!");
    }

    if (ObjectUtil.isNull(columns)) {
      log.warn("The columns of the record table to include as values in the tabular data structure cannot be null!");
      throw new NullPointerException("The columns of the record table to include as values in the tabular data structure cannot be null!");
    }

    final Object[][] data = new Object[rowIndices.length][columns.length];

    for (int rowIndex = 0; rowIndex < data.length; rowIndex++) {
      for (int columnIndex = 0; columnIndex < data[rowIndex].length; columnIndex++) {
        data[rowIndex][columnIndex] = getCellValue(rowIndices[rowIndex], columns[columnIndex]);
      }
    }

    return data;
  }

  /**
   * Unregisters the Comparator object for the specified class type.  If the class type was not mapped
   * to a Comparator object, then the method returns null.
   * @param type the class type for which the Comparator was registered.
   * @return a the Comparator registered with the specified class type.
   */
  public Comparator unregisterComparator(final Class type) {
    if (log.isDebugEnabled()) {
      log.debug("unregistering Comparator for type (" + type + ")");
    }
    return comparatorMap.remove(type);
  }

  /**
   * Verifies that the specified column index is bound within the number of columns contained by this
   * record table.
   * @param columnIndex the index of a column in this record table.
   * @throws java.lang.IndexOutOfBoundsException if the specified column index is not within the range
   * of columns contained by this record table.
   */
  protected void validateColumnIndex(final int columnIndex) {
    if (log.isDebugEnabled()) {
      log.debug("validating columnIndex (" + columnIndex + ")");
    }

    if (columnIndex < 0 || columnIndex >= columnCount()) {
      log.warn(columnIndex + " is not between 0 inclusive and " + columnCount());
      throw new IndexOutOfBoundsException(columnIndex + " is not between 0 inclusive and " + columnCount());
    }
  }

  /**
   * Verifies the value being set on the column in this record table for the current row follows the
   * constraints implied by the RecordTable object implementation. NOTE: the order of precedence of
   * the validation is as follows: check for null, check type, check uniqueness, check size.
   * @param column the column in this record table.
   * @param value the value to be set on the colunn for the current row in the record table.
   */
  protected void validateColumnValue(final Column column, Object value) {
    if (log.isDebugEnabled()) {
      log.debug("validating value (" + value + ") for column (" + column + ")");
    }

    validateColumnValueType(column, value);
    value = validateColumnValueNullity(column, value);
    validateColumnValueUniqueness(column, value);
    validateColumnValueSize(column, value);
  }

  /**
   * Verifies that the value for column conforms to the nullity constraint of the Column in this
   * record table.
   * @param column the column of this record table used to verify nullity.
   * @param value the Object value who's nullity is verified with the specified Column.
   * @return the value transformed by the nullity check.
   * @throws com.cp.common.util.record.NullColumnValueException if the column is non-nullable and both
   * the value and default value for the column are null.
   */
  protected Object validateColumnValueNullity(final Column column, Object value) {
    if (log.isDebugEnabled()) {
      log.debug("value (" + value + ")");
      log.debug("verifying value nullity for column (" + column + ")");
    }

    value = getColumnValue(column, value);

    if (!column.isNullable() && ObjectUtil.isNull(value)) {
      log.warn("The value for column (" + column + ") cannot be null!");
      throw new NullColumnValueException("The value for column (" + column + ") cannot be null!");
    }

    return value;
  }

  /**
   * Verifies that value for column is constrained by the maximum size defined by the Column in this
   * record table.  The method constrains the size of the value by the toString representation of the
   * Object.  If the Object value is null, then an empty String is used when comparing to the Column's
   * designated size.
   * @param column the column of the record table used to verify size.
   * @param value the Object value whose size (length) is constrained to that of the column.
   * @throws com.cp.common.util.record.InvalidColumnValueSizeException if the value's size is not constrained
   * within size of the column!
   */
  protected void validateColumnValueSize(final Column column, final Object value) {
    if (log.isDebugEnabled()) {
      log.debug("validating value size (" + value + ") for column (" + column + ")");
    }

    final String valueString = StringUtil.toString(value);
    if (valueString.length() > (column.getSize() > 0 ? column.getSize() : Integer.MAX_VALUE)) {
      log.warn("The value length (" + valueString.length() + ") exceeds the maximum size (" + column.getSize()
        + ") for column (" + column + ")!");
      throw new InvalidColumnValueSizeException("The value length (" + valueString.length() + ") exceeds the maximum size ("
          + column.getSize() + ") for column (" + column + ")!");
    }
  }

  /**
   * Verifies that the value for column has an appropriate type assignable to the type of the specified
   * Column.
   * @param column the column of the record table used to verify type.
   * @param value the Object value being type checked.
   * @throws com.cp.common.util.record.InvalidColumnValueTypeException if the value's type does not match the
   * Column's type.
   */
  protected void validateColumnValueType(final Column column, final Object value) {
    if (log.isDebugEnabled()) {
      log.debug("validating value type (" + ClassUtil.getClassName(value) + ") for column (" + column + ")");
    }

    if (ObjectUtil.isNotNull(value)) {
      if (!column.getType().isAssignableFrom(value.getClass())) {
        log.warn("Expected value of type (" + column.getType().getName() + ") for column (" + column
          + ").  The value type was (" + value.getClass().getName() + ")");
        throw new InvalidColumnValueTypeException("Expected value of type (" + column.getType().getName() + ") for column ("
          + column + ").  The value type was (" + value.getClass().getName() + ")");
      }
    }
  }

  /**
   * Verifies that the value for column is unique if the Column's unique property enforces uniqueness
   * amongst other column values for all rows in the record table.
   * @param column the column of the record table used to verify uniqueness.
   * @param value the value being determined for uniqueness within this column.
   * @throws com.cp.common.util.record.NonUniqueColumnValueException if the value for a unique Column is not
   * unique.
   */
  protected void validateColumnValueUniqueness(final com.cp.common.util.record.Column column, final Object value) {
    if (log.isDebugEnabled()) {
      log.debug("validating value uniqueness (" + value + ") for column (" + column + ")");
    }

    if (column.isUnique() && ObjectUtil.isNotNull(value)) {
      for (int rowIndex = rowCount(); --rowIndex >= 0; ) {
        if (ObjectUtil.equals(value, getCellValue(rowIndex, column))) {
          log.warn("Value (" + value + ") is not unique for column (" + column + ")!");
          throw new com.cp.common.util.record.NonUniqueColumnValueException("Value (" + value + ") is not unique for column (" + column + ")!");
        }
      }
    }
  }

  /**
   * Verifies that this record table is mutable before performing operations that modify the record table.
   * @throws com.cp.common.lang.ObjectImmutableException if this record table is immutable.
   */
  protected void validateMutable() {
    if (!isMutable()) {
      log.warn("This record table is immutable!");
      throw new ObjectImmutableException("This record table is immutable!");
    }
  }

  /**
   * Verifies that the specified row index is bound within the number of rows contained by
   * this record table.
   * @param rowIndex the index of a row in this record table.
   * @throws java.lang.IndexOutOfBoundsException if the specified row index is not within
   * the range of rows contained by this record table.
   */
  protected void validateRowIndex(final int rowIndex) {
    if (log.isDebugEnabled()) {
      log.debug("validating rowIndex (" + rowIndex + ")");
    }

    if (rowIndex < 0 || rowIndex >= rowCount()) {
      log.warn(rowIndex + " is not between 0 inclusive and " + rowCount());
      throw new IndexOutOfBoundsException(rowIndex + " is not between 0 inclusive and " + rowCount());
    }
  }

}
