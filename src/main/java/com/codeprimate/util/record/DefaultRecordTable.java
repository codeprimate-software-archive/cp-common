/*
 * DefaultRecordTable.java (c) 17 April 2002
 *
 * The DefaultRecordTable class is the default implementation of the
 * RecordTable interface.
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.1.20
 * @see com.cp.common.util.record.AbstractRecordTable
 * @see com.cp.common.util.record.Column
 * @see com.cp.common.util.IncompatibleRecordExeption
 * @see com.cp.common.util.IdentityComparator
 * @see com.cp.common.util.record.NonUniqueColumnValueException
 * @see com.cp.common.util.record.Record
 * @see com.cp.common.util.record.RecordAdapter
 * @see com.cp.common.util.record.RecordTable
 */

package com.codeprimate.util.record;

import com.cp.common.lang.support.MutableVisitor;
import com.cp.common.lang.Mutable;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ArrayUtil;
import com.cp.common.util.IdentityComparator;
import com.cp.common.util.Visitor;
import com.cp.common.util.record.AbstractRecord;
import com.cp.common.util.record.Column;
import com.cp.common.util.record.ColumnImpl;
import com.cp.common.util.record.IncompatibleRecordException;
import com.cp.common.util.record.NonUniqueColumnValueException;
import com.cp.common.util.record.Record;
import com.cp.common.util.record.RecordTable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;

public class DefaultRecordTable extends com.cp.common.util.record.AbstractRecordTable {

  // Logger object used to record the state, action and events of the RecordTable object.
  private static final Logger logger = Logger.getLogger(DefaultRecordTable.class);

  // This value represents the initial capacity (number of row or Records) to be stored
  // in this RecordTable.
  private static final int INITIAL_TABLE_CAPACITY = 1024;

  // An ordered collection of Columns in the record table.
  private final List<Column> columnList;

  // The record table data structure is a collection of rows (Records).
  private final List<com.cp.common.util.record.Record> recordList;

  // A Map of Column to hash values of the values in the column for all rows in this record table
  private final Map<Column, Set<Integer>> columnValueHashMap;

  // A Set of hash values computed from the rows in this record table.
  private final Set<com.cp.common.util.record.Record> rowSet;

  /**
   * Default constructor used to create an unitialized instance of the DefaultRecordTable class.
   */
  public DefaultRecordTable() {
    this(null);
  }

  /**
   * Creates an instance of the DefaultRecordTable class initialzed with the specified Columns
   * defining it's structre.
   * @param columns an array of Columns defining the structure of the new record table.
   */
  public DefaultRecordTable(final Column[] columns) {
    rowSet = new TreeSet<com.cp.common.util.record.Record>(IdentityComparator.getInstance());
    recordList = new ArrayList<com.cp.common.util.record.Record>(INITIAL_TABLE_CAPACITY);
    columnList = new ArrayList<Column>(ArrayUtil.length(columns));
    columnValueHashMap = new HashMap<Column, Set<Integer>>((int) (columnList.size() * 1.5));

    if (ArrayUtil.isNotEmpty(columns)) {
      for (Column column : columns) {
        column = new TableColumn(column);
        columnList.add(column);
        columnValueHashMap.put(column, null);
      }
    }
  }

  /**
   * Stores the hash value of the new value for the specified Column and removes any previous hash value
   * for the old value.
   * @param column a column in this record table.
   * @param oldValue the old value being replaced by the new value.
   * @param newValue the new value whose hash code will be stored to determine uniqueness.
   */
  private void addColumnValueHash(final com.cp.common.util.record.Column column, final Object oldValue, final Object newValue) {
    if (ObjectUtil.isNotNull(oldValue) || ObjectUtil.isNotNull(newValue)) {
      Set<Integer> columnValueHashSet = columnValueHashMap.get(column);

      if (ObjectUtil.isNull((columnValueHashSet))) {
        columnValueHashSet = new HashSet<Integer>((int) (rowCount() * 1.5));
        columnValueHashMap.put(column, columnValueHashSet);
      }

      if (ObjectUtil.isNotNull(oldValue)) {
        columnValueHashSet.remove(new Integer(oldValue.hashCode()));
      }

      if (ObjectUtil.isNotNull(newValue)) {
        columnValueHashSet.add(new Integer(newValue.hashCode()));
      }
    }
  }

  /**
   * Returns the number of columns in this record table, specified as a integer value.
   * @return the number of columns in this record table.
   */
  public int columnCount() {
    return columnList.size();
  }

  /**
   * Returns an Iterator over the columns in this record table.
   * @return an Iterator to traverse the columns in this record table.
   */
  public Iterator<Column> columnIterator() {
    return new Iterator<Column>() {
      private final Iterator<Column> columnIterator = columnList.iterator();
      private Column currentColumn = null;

      public boolean hasNext() {
        return columnIterator.hasNext();
      }

      public Column next() {
        return (currentColumn = columnIterator.next());
      }

      public void remove() {
        validateMutable();

        for (Iterator<com.cp.common.util.record.Record> it = rowIterator(); it.hasNext(); ) {
          final TableRecord record = (TableRecord) it.next();
          record.removeColumn(currentColumn);
        }
        // NOTE: the column should only be removed from the record table if the
        // field was successfully removed from all rows (Records) in this record table.
        // In addition, the column index is needed by the TableRecord.removeColumn
        // method, therefore, the column cannot be removed from the table until
        // after the field has been removed from all rows in this table.
        columnIterator.remove();
        columnValueHashMap.remove(currentColumn);
      }
    };
  }

  /**
   * Determines whether this record table contains the specified Column object (whether the Column
   * is a column of this record table).
   * @param column the Column object being tested as a column of this record table.
   * @return a boolean value indication whether the specified Column is a column of this record table.
   */
  public boolean contains(final Column column) {
    return columnList.contains(column);
  }

  /**
   * Determines whether this record table contains the specified Record object (whether the Record
   * is a row in this record table).
   * @param record a Record to test for containment by this record table.
   * @return a boolean value indicating whether the Record object is contained, or is a row, of this
   * record table.
   */
  public boolean contains(final Record record) {
    return recordList.contains(record);
  }

  /**
   * This method perform a shallow-copy of this record table and creates a new record table instance
   * based on the contents of this record table.
   * @return a copy of this record table.
   * @see com.cp.common.lang.Copyable#copy
   */
  public Object copy() {
    final RecordTable recordTableCopy = new DefaultRecordTable(columnList.<com.cp.common.util.record.Column>toArray(new Column[columnList.size()]));
    recordTableCopy.addAll(this.recordList);
    return recordTableCopy;
  }

  /**
   * Returns the Column object at the specified column index in this record table.
   * @param columnIndex the index of the column in this record table for which a
   * Column object is returned.
   * @return a Column object for the column at index in this record table.
   */
  public Column getColumn(final int columnIndex) {
    if (logger.isDebugEnabled()) {
      logger.debug("columnIndex (" + columnIndex + ")");
    }

    validateColumnIndex(columnIndex);

    return columnList.get(columnIndex);
  }

  /**
   * Returns the index of the specified Column object within this record table.
   * @param column the Column object in this record table to return an index for.
   * @return the index of the Column object in this record table or a -1 if the column
   * does not exist in the record table.
   */
  public int getColumnIndex(final Column column) {
    if (logger.isDebugEnabled()) {
      logger.debug("column (" + column + ")");
    }
    return columnList.indexOf(column);
  }

  /**
   * Returns the row in this record table at index as a Record object.
   * @param rowIndex the index of the row in this record table to get a Record object for.
   * @return a Record object for the row at index in this record table.
   */
  public com.cp.common.util.record.Record getRow(final int rowIndex) {
    if (logger.isDebugEnabled()) {
      logger.debug("rowIndex (" + rowIndex + ")");
    }

    validateRowIndex(rowIndex);

    return recordList.get(rowIndex);
  }

  /**
   * Returns the row index of the Record object in this record table.
   * @param row the Record object for which the row index is being returned.
   * @return an integer index of the row for the specified Record object or a -1
   * if the row is not contained in the record table.
   */
  public int getRowIndex(final com.cp.common.util.record.Record row) {
    if (logger.isDebugEnabled()) {
      logger.debug("row (" + row + ")");
    }
    return recordList.indexOf(row);
  }

  /**
   * Inserts the specified Column at the specified column index into this record table.
   * @param column the Column to insert into the record table.
   * @param columnIndex the column position in the record table in which to insert the Column.
   * @throws java.lang.IndexOutOfBoundsException if the columnIndex is less than 0 or greater than
   * the number of columns in this record table.
   * @return a boolean value indicating whether the column was inserted successfully.
   */
  public boolean insertColumn(final com.cp.common.util.record.Column column, final int columnIndex) {
    validateMutable();

    // validate column index
    if (columnIndex < 0 || columnIndex > columnCount()) {
      logger.warn(columnIndex + " is not a valid column index!  The column index must be between 0 and "
        + columnCount() + " inclusive!");
      throw new IndexOutOfBoundsException(columnIndex + " is not a valid column index!  The column index must be between 0 and "
        + columnCount() + " inclusive!");
    }

    // the column must be added first so that the index can be obtained in the call to TableRecord.addColumn.
    columnList.add(columnIndex, new TableColumn(column));

    for (Iterator it = rowIterator(); it.hasNext(); ) {
      final TableRecord record = (TableRecord) it.next();
      record.addColumn(column, null);
    }

    return true;
  }

  /**
   * Inserts the specified Record object at the specified row index into this record table.
   * @param record the Record object to insert into this record table.
   * @param rowIndex the index of the row in which to insert the Record.
   * @throws java.lang.IndexOutOfBoundsException if the rowIndex is less than 0 or greater than
   * the number of rows in this record table.
   * @return a boolean value indicating whether the row was inserted successfully.
   */
  public boolean insertRow(final com.cp.common.util.record.Record record, final int rowIndex) {
    validateMutable();

    // validate row index
    if (rowIndex < 0 || rowIndex > rowCount()) {
      logger.warn(rowIndex + " is not a valid row index!  The row index must be between 0 and "
        + rowCount() + " inclusive!");
      throw new IndexOutOfBoundsException(rowIndex + " is not a valid row index!  The row index must be between 0 and "
        + rowCount() + " inclusive!");
    }

    final TableRecord tableRecord = new TableRecord(record);

    recordList.add(rowIndex, tableRecord);
    rowSet.add(tableRecord);

    return true;
  }

  /**
   * Returns the number of rows (Records) in this record table.
   * @return a integer value of the number of rows contained in this record table.
   */
  public int rowCount() {
    return recordList.size();
  }

  /**
   * Returns a row Iterator to traverse the rows in this record table.
   * @return an Iterator over the rows in this record table.
   */
  public Iterator<Record> rowIterator() {
    return new Iterator<com.cp.common.util.record.Record>() {
      private final Iterator<Record> rowIterator = recordList.iterator();
      private Record currentRow = null;

      public boolean hasNext() {
        return rowIterator.hasNext();
      }

      public com.cp.common.util.record.Record next() {
        return (currentRow = rowIterator.next());
      }

      public void remove() {
        validateMutable();
        rowIterator.remove();
        rowSet.remove(currentRow);

        for (Iterator<Column> it = columnIterator(); it.hasNext(); ) {
          final Column column = it.next();

          if (logger.isDebugEnabled()) {
            logger.debug("column (" + column + ")");
          }

          try {
            final Object value = currentRow.getValue(column);

            if (logger.isDebugEnabled()) {
              logger.debug("value (" + value + ")");
            }

            // the null check is necessary; see addColumnValueHash
            if (ObjectUtil.isNotNull(columnValueHashMap.get(column))) {
              columnValueHashMap.get(column).remove(new Integer(ObjectUtil.hashCode(value)));
            }
          }
          catch (NoSuchFieldException ignore) {
            logger.error(ignore.getMessage(), ignore);
          }
        }
      }
    };
  }

  /**
   * Implementation of the Sortable.set(:Object, I) method.  This method guarantees that a
   * malicious caller cannot add new rows to this record table that violate the constraints
   * the record table.
   * @param record the Record object from this record table being set in a new location
   * according to the sort.
   * @param rowIndex the row index to set the Record at in this record table.
   * @throws java.lang.IllegalArgumentException if the record table did not previously contain
   * this Record (row).
   */
  public void set(final com.cp.common.util.record.Record record, final int rowIndex) throws Exception {
    validateRowIndex(rowIndex);

    if (!rowSet.contains(record)) {
      logger.warn("The record (" + record + ") is NOT contained in this record table!");
      throw new IllegalArgumentException("The record (" + record + ") is NOT contained in this record table!");
    }

    recordList.set(rowIndex, record);
  }

  /**
   * Verifies that the Column being added to this record table is valid.
   * @param column the Column object being validated before adding to this record table.
   * @throws java.lang.IllegalArgumentException if the column already exists.
   * @throws java.lang.IllegalStateException if the column enforces a unique constraint, or
   * cannot be NULL and has no default value.
   * @throws java.lang.NullPointerException if the column is null.
   * @throws java.lang.IllegalArgumentException if the colun already exist in this record table.
   * @throws java.lang.IllegalStateException if the column is not nullable, the record table is
   * not empty, and the the column is either unique or the default column value is null.
   */
  private void validateColumn(final Column column) {
    if (logger.isDebugEnabled()) {
      logger.debug("validating column (" + column + ")");
    }

    if (ObjectUtil.isNull(column)) {
      logger.warn("Cannot add a null column to this record table!");
      throw new NullPointerException("Cannot add a null column to this record table!");
    }

    if (ObjectUtil.isNotNull(getColumn(column.getName()))) {
      logger.warn("The column (" + column + ") already exists in this record table!");
      throw new IllegalArgumentException("The column (" + column + ") already exists in this record table!");
    }

    if (!column.isNullable() && !isEmpty()) {
      if (column.isUnique() || ObjectUtil.isNull(column.getDefaultValue())) {
        logger.warn("Cannot add a column (" + column + ") with a non-null constraint to a record table with rows!");
        throw new IllegalStateException("Cannot add a column (" + column + ") with a non-null constraint to a record table with rows!");
      }
    }
  }

  /**
   * Determines whether the specified value, if not null, is unique amongst the values in the column
   * for all rows in the record table.  The Column's state must inidicate that all row values for the
   * column must contain unique values.
   * @param column the Column in the RecordTable enforcing values to be unqiue.
   * @param value the value being validated for uniqueness amongst the values of the Column for all rows
   * in this record table.
   * @throws java.lang.IllegalArgumentException if the value being added to a unique column is not
   * unique amongst values of the column for all rows in the record table.
   */
  protected void validateColumnValueUniqueness(final com.cp.common.util.record.Column column, final Object value) {
    if (logger.isDebugEnabled()) {
      logger.debug("validating value uniqueness (" + value + ") for column (" + column + ")");
    }

    if (column.isUnique() && ObjectUtil.isNotNull(value)) {
      final Set<Integer> columnValueHashSet = columnValueHashMap.get(column);
      if (ObjectUtil.isNotNull(columnValueHashSet)) {
        if (columnValueHashSet.contains(new Integer(value.hashCode()))) {
          logger.warn("(" + value + ") is not a unique value for column (" + column + ")!");
          throw new NonUniqueColumnValueException("(" + value + ") is not a unique value for column (" + column + ")!");
        }
      }
    }
  }

  /**
   * Determines whether the specified Record is compatible in size, columns/fields and value types
   * with this record table.  Other constraints are also enforced as specified in the
   * validateColumnValue method.
   * @param record the Record object tested for compatibility with this record table.
   * @see DefaultRecordTable#validateColumnValue
   * @throws com.cp.common.util.record.IncompatibleRecordException if the Record argument is not compatible
   * with this record table.
   */
  private void validateRecordCompatibility(final com.cp.common.util.record.Record record) {
    // if the Record is NULL, then the Record is not compatible with this record table
    if (ObjectUtil.isNull(record)) {
      logger.warn("The record argument cannot be null!");
      throw new IncompatibleRecordException("The record argument cannot be null!");
    }

    // if the number of fields in the Record does not equal the number of columns in this record table,
    // then the Record is not compatible with this record table
    if (record.size() != columnCount()) {
      logger.warn("The number of record fields does not match the number of columns in the record table!");
      throw new com.cp.common.util.record.IncompatibleRecordException("The number of record fields does not match the number of columns in the record table!");
    }

    // verify that all columns in this record table are fields in the record, and that the corresponding
    // value types for the fields match the column types of this record table
    for (Iterator<Column> it = columnIterator(); it.hasNext(); ) {
      final com.cp.common.util.record.Column column = it.next();
      if (!record.containsField(column)) {
        logger.warn("The record argument does not contain field (" + column + ")!");
        throw new com.cp.common.util.record.IncompatibleRecordException("The record argument does not contain field (" + column + ")!");
      }
      try {
        validateColumnValue(column, record.getValue(column));
      }
      catch (NoSuchFieldException ignore) {
        logger.error(ignore);
      }
    }
  }

  /**
   * The TableColumn class is used by the DefaultRecordTable class to represent columns/fields
   * of the record table.
   */
  private final class TableColumn extends ColumnImpl implements Mutable {

    /**
     * Creates an instance of the TableColumn class.
     * @param name the name of this column in the record table.
     * @param type the declared type of the Column, classifying all row values.
     */
    public TableColumn(final String name, final Class type) {
      super(name, type);
    }

    /**
     * Copy constructor used to create an instance of the TableColumn class used by this record table
     * to represent columns.
     * @param column the Column object on which this TableColumn is based (copied).
     */
    public TableColumn(final Column column) {
      this(column.getName(), column.getType());
      validateColumn(column);
      setDefaultValue(column.getDefaultValue());
      setDescription(column.getDescription());
      setDisplayName(column.getDisplayName());
      setNullable(column.isNullable());
      setSize(column.getSize());
      setUnique(column.isUnique());
    }

    /**
     * Determines whether this TableColumn instance can be modified or is read-only.  The mutable
     * state of this Column is delegated to the enclosing record table.
     * @return a boolean value indicating if the TableColumn is read-only or modifiable.
     */
    public boolean isMutable() {
      return DefaultRecordTable.this.isMutable();
    }

    /**
     * Sets the default value used when a record in a table of records does not specify a value for
     * this Column.
     * @param defaultValue an Object representing the default value used when no value is provided
     * for this Column.
     */
    public void setDefaultValue(final Object defaultValue) {
      validateMutable();
      super.setDefaultValue(defaultValue);
    }

    /**
     * Sets the description of this Column instance describing the meaning behind data in this column.
     * @param description the String description of this Column.
     */
    public void setDescription(final String description) {
      validateMutable();
      super.setDescription(description);
    }

    /**
     * Sets the presentation name of this Column instance that will be used for display purposes.
     * @param displayName the String presentation display name of this Column instance.
     */
    public void setDisplayName(final String displayName) {
      validateMutable();
      super.setDisplayName(displayName);
    }

    /**
     * The setMutable method is not supported by the TableColumn class.  Mutability can only be determined
     * by the enclosing record table.
     * @param mutable a boolean value indicating whether this TableColumn is modifiable or read-only.
     */
    public void setMutable(final boolean mutable) {
      throw new UnsupportedOperationException("Operation Not Supported!");
    }

    /**
     * Sets whether null values are allowed to be set as a value on this Column.
     * @param nullable is a boolean value indicating if null values are allowed to be set on this Column.
     * @see RecordTable#setCellValue on the corresponding record table data structure which will throw a
     * NullPointerException if the column is non-nullable and the user attempts to set a null value on
     * this Column.
     */
    public void setNullable(final boolean nullable) {
      validateMutable();

      if (!nullable && contains(this)) {
        int rowIndex = 0;
        for (Iterator<com.cp.common.util.record.Record> it = rowIterator(); it.hasNext(); rowIndex++) {
          try {
            final com.cp.common.util.record.Record record = it.next();
            Object value = record.getValue(this);
            if (ObjectUtil.isNull(value)) {
              value = getColumnValue(this, null);
              if (ObjectUtil.isNotNull(value)) {
                record.setValue(this, value);
              }
              else {
                logger.warn("Unable to set column (" + getName() + ") to non-nullable since their exist row @ index (" + rowIndex
                  + ") who's value for this column in the record table is null!");
                throw new IllegalStateException("Unable to set column (" + getName() + ") to non-nullable since their exist a row @ index ("
                  + rowIndex + ") who's value for this column in the record table is null!");
              }
            }
          }
          catch (NoSuchFieldException e) {
            logger.error(e.getMessage(), e);
          }
        }
      }

      super.setNullable(nullable);
    }

    /**
     * Sets the size of the column's data type value.
     * @param size a numerical value representing the size of the data value with respect to type.
     */
    public void setSize(final int size) {
      validateMutable();
      super.setSize(size);
    }

    /**
     * Sets whether value in this column should be unique.
     * @param unique a boolean value indicating whether values of this Column are to be unique.
     */
    public void setUnique(final boolean unique) {
      validateMutable();

      if (unique && contains(this)) {
        final Set columnValueSet = new HashSet();
        final int columnIndex = getColumnIndex(this);

        for (Iterator<com.cp.common.util.record.Record> it = rowIterator(); it.hasNext(); ) {
          final com.cp.common.util.record.Record record = it.next();
          final Object columnValue = record.getValue(columnIndex);

          if (ObjectUtil.isNotNull(columnValue)) {
            if (columnValueSet.contains(columnValue)) {
              logger.warn("Unable to set column (" + getName()
                + ") as unique since rows in this record table have duplicate values for this column!");
              throw new IllegalStateException("Unable to set column (" + getName()
                + ") as unique since rows in this record table have duplicate values for this column!");
            }
            else {
              columnValueSet.add(columnValue);
            }
          }
        }
      }

      super.setUnique(unique);
    }
  }

  /**
   * The TableRecord class is used by the DefaultRecordTable class to represent records, or rows
   * in this record table.
   */
  private final class TableRecord extends AbstractRecord<Column> {

    private final List rowData;

    /**
     * Default constructor for the TableRecord class.
     */
    public TableRecord() {
      rowData = new ArrayList((int)(columnCount() * 1.5));
    }

    /**
     * Copy constructor used to create instance of the TableRecord class based on an existing Record object.
     * @param record the Record object to copy and add to this record table.
     */
    public TableRecord(final Record record) {
      validateRecordCompatibility(record);
      rowData = new ArrayList(columnCount());

      for (Iterator<Column> it = columnIterator(); it.hasNext(); ) {
        final Column column = it.next();
        try {
          final Object value = getColumnValue(column, record.getValue(column));
          // we have already validated the column's value in validateRecordCompatibility!
          addColumnValueHash(column, null, value);
          rowData.add(value);
        }
        catch (NoSuchFieldException ignore) {
          // if implemented properly, the validateRecordCompatibility method should have prevented this
          // Exception from occurring.  However, log the Exception just in case.
          logger.error("The record does not contain field (" + column + ")!", ignore);
        }
      }
    }

    /**
     * The interface used by Visitor objects to traverse the object structure and
     * perform its designated operation on this object.  This method implements the
     * Visitor design pattern allowing new operations to be performed on this object
     * without having to modify the object's interface (contractual agreement).
     * NOTE: this overridden accept method accepts all Visitor objects except the
     * MutableVisitor!  The act of setting the mutable property does not apply to
     * the TableRecord implementation of Record.  The mutable property is only kept
     * on the enclosing RecordTable.
     * @param visitor the Visitor object used to invoke some operation on this Object.
     */
    public void accept(Visitor visitor) {
      if (!(visitor instanceof MutableVisitor)) {
        super.accept(visitor);
      }
    }

    /**
     * Adds the specified field to the Record object initialized with the specified value.
     * @param field the field to add to this Record.
     * @param defaultValue the default value for the new field.
     * @return a boolean value indicating whether the specified field was added to this Record.
     */
    public boolean addField(final Column field, final Object defaultValue) {
      throw new UnsupportedOperationException("Operation Not Allowed!");
    }

    /**
     * Internal method used by this RecordTable object to add the specified Column to this
     * row of the record table.
     * @param column the specified Column to add to this row of the record table.
     * @param value the value for the Column to set on this row in the record table.
     */
    private void addColumn(final Column column, Object value) {
      validateMutable();
      value = getColumnValue(column, value);
      rowData.add(getColumnIndex(column), value);
    }

    /**
     * Creates an exact duplicate of the object whose class implements this interface.
     * Look to the class's implementation to determine whether the copy is "deep" or "shallow".
     * @return a java.lang.Object clone of this object who's class implements this interface.
     */
    public Object copy() {
      return new com.codeprimate.util.record.DefaultRecord(this);
    }

    /**
     * Returns the Comparator object registered for the specified type of the Object parameter.
     * @param value the Object value who's type is used to return the registered Comparator object.
     * @return a Comparator object for the specified type of the Object parameter or null if a
     * Comparator has not been registered for the specified Object's type.
     */
    protected Comparator getComparator(final Object value) {
      return DefaultRecordTable.this.getComparator(getColumn(rowData.indexOf(value)), value);
    }

    /**
     * Gets the field at the specified index.
     * @param fieldIndex the integer index of the field in the Record.
     * @return the field in the Record at the specified index.
     * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
     * the number of fields in this Record.
     */
    public com.cp.common.util.record.Column getField(final int fieldIndex) {
      if (logger.isDebugEnabled()) {
        logger.debug("fieldIndex (" + fieldIndex + ")");
      }

      validateColumnIndex(fieldIndex);

      return getColumn(fieldIndex);
    }

    /**
     * Gets the field index for the specified field in this Record.
     * @param field the field to obtain the index of.
     * @return the integer index in this Record for the specified field.
     * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
     */
    public int getFieldIndex(final com.cp.common.util.record.Column field) throws NoSuchFieldException {
      if (logger.isDebugEnabled()) {
        logger.debug("getting index for field (" + field + ")");
      }
      return getColumnIndex(field);
    }

    /**
     * Gets the value of the specified field in this Record.
     * @param field the field to return a value for.
     * @return the Object value of the specified field in this Record.
     * @throws java.lang.NoSuchFieldException if the specified field does not exist in this Record.
     */
    public <T> T getValue(final Column field) throws NoSuchFieldException {
      final int columnIndex = getColumnIndex(field);

      if (logger.isDebugEnabled()) {
        logger.debug("column index (" + columnIndex + ") for field (" + field + ")");
      }

      if (columnIndex < 0) {
        logger.warn("(" + field + ") is not a valid column in this Record!");
        throw new NoSuchFieldException("(" + field + ") is not a valid column in this Record!");
      }

      return (T) rowData.get(columnIndex);
    }

    /**
     * Determines whether this Record object can be modified, or is read-only.  Note, that
     * this Record object delegates to the enclosing RecordTable data structure.  A TableRecord
     * object does not have mutable property, and hence does not support row locks; the information
     * is maintained by the RecordTable and is an all or nothing property on the table.
     * @return a boolean value indicating whether this Record object can be modified or not.
     */
    public final boolean isMutable() {
      return DefaultRecordTable.this.isMutable();
    }

    /**
     * Registers the specified Comparator object for the specified class type.  Thus, during
     * a comparison of Objects of the specified type, the associated Comparator to this type
     * will be used to determine the relationship.
     * @param type the Class type of the Objects for which the specified Comparator should be used.
     * @param comparator the Comparator instance used to the compare Objects of the specified type.
     */
    public void registerComparator(final Class type, final Comparator comparator) {
      DefaultRecordTable.this.registerComparator(type, comparator);
    }

    /**
     * Removes the specified field from this Record and returns it's value.
     * @param field the field to remove from this Record.
     * @return the Object value of the removed field.
     * @throws java.lang.NoSuchFieldException if the specified field does not exist in this Record.
     */
    public final Object removeField(final Column field) throws NoSuchFieldException {
      throw new UnsupportedOperationException("Operation Not Allowed!");
    }

    /**
     * Removes the specified Column from this row in the RecordTable.
     * @param column the Column to be removed from this row of the RecordTable.
     */
    private void removeColumn(final com.cp.common.util.record.Column column) {
      validateMutable();
      rowData.remove(getColumnIndex(column));
    }

    /**
     * Sets whether this Record object can be modified or not.
     * @param mutable a boolean property indicating if this Record object can be modfied,
     * or is read-only.
     * @throws UnsupportedOperationException as the TableRecord implementation of Record
     * does not support the concept of the mutable property or row locks.  The constraint
     * of only being able to read or the ability to write a row is kept and maintained by
     * the RecordTable itself.  There is not concept of row locks in this RecordTable
     * implemenation.
     */
    public final void setMutable(final boolean mutable) {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    /**
     * Sets the value of the specified field in this Record to the specified Object value.
     * @param field the field to set the value of.
     * @param value the Object value to set on the specified field in this Record.
     * @return the original Object value of the specified field in this Record.
     * @throws java.lang.NoSuchFieldException if the specified field does not exist in this Record.
     */
    public Object setValue(final Column field, final Object value) throws NoSuchFieldException {
      if (logger.isDebugEnabled()) {
        logger.debug("setting column (" + field + ") value (" + value + ")");
      }

      validateMutable();

      final int columnIndex = getColumnIndex(field);

      if (logger.isDebugEnabled()) {
        logger.debug("column index (" + columnIndex + ")");
      }

      if (columnIndex < 0) {
        logger.warn("(" + field + ") is not a valid column in this Record!");
        throw new NoSuchFieldException("(" + field + ") is not a valid column in this Record!");
      }

      final Object columnValue = getColumnValue(field, value);

      if (logger.isDebugEnabled()) {
        logger.debug("column value (" + value + ")");
      }

      validateColumnValue(field, value);
      addColumnValueHash(field, rowData.get(columnIndex), columnValue);

      return rowData.set(columnIndex, columnValue);
    }

    /**
     * Gets the number of fields in this Record.
     * @return and integer value of the number of fields in this Record.
     */
    public int size() {
      return rowData.size();
    }

    /**
     * Unregisters the Comparator object for the specified class type.  If the class type
     * was not mapped to a Comparator object, then the method returns null.
     * @param type the class type for which the Comparator was registered.
     * @return the Comparator registered with the specified class type.
     */
    public Object unregisterComparator(final Class type) {
      return DefaultRecordTable.this.unregisterComparator(type);
    }
  }

}
