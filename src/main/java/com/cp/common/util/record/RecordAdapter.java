/*
 * RecordAdapter.java (c) 18 September 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.11.17
 * @see com.cp.common.util.record.AbstractRecord
 * @see com.cp.common.util.record.Column
 * @see com.cp.common.util.record.Record
 * @see com.codeprimate.util.record.DefaultRecord
 */

package com.cp.common.util.record;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.record.*;
import com.cp.common.util.record.AbstractRecordFactory;
import com.cp.common.util.record.Column;
import com.cp.common.util.record.ColumnImpl;
import com.cp.common.util.record.Record;
import com.cp.common.util.CollectionUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.apache.log4j.Logger;

public final class RecordAdapter extends com.cp.common.util.record.AbstractRecord<Column> {

  private static final Logger logger = Logger.getLogger(RecordAdapter.class);

  private final List<Column> columnList;

  private final Record<String> record;

  /**
   * Creates an instance of the RecordAdapter class to adapt a Record<String> object into
   * an instance of the Record<Column> interface.
   * @param record the Record<String> object to adapt into the Record<Column> interface.
   */
  public RecordAdapter(final Record<String> record) {
    this(record, null);
  }

  /**
   * Creates an instance of the RecordAdapter class to adapt a Record<String> object into an instance
   * of the Record<Column> interface.  If the columns are specified, then the columns are taken to
   * correspond to the fields of the given Record, and are used to adapt (translate) the interface of
   * the Record<String> object.  If the number of columns does not match the number of fields in the
   * Record, or the Record does not contain a field with the corresponding Column name, then an
   * IllegalArgumentException is thrown.
   * @param record the Record<String> object to adapt as an instance of the Record<Column> interface.
   * @param columns the List of Columns corresponding to the fields of the Record<String> object.
   * @throws java.lang.IllegalArgumentException if the number of columns does not equal to the number
   * of fields in the Record, or the Record does not contain a field corresponding to the name of a
   * Column.
   */
  public RecordAdapter(final Record<String> record, final List<Column> columns) {
    if (ObjectUtil.isNull(record)) {
      logger.warn("The underlying Record<String> object to this adapter cannot be null!");
      throw new NullPointerException("The underlying Record<String> object to this adapter cannot be null!");
    }

    this.record = record;

    if (CollectionUtil.isEmpty(columns)) {
      this.columnList = new ArrayList<Column>(record.size());

      for (int fieldIndex = 0, size = record.size(); fieldIndex < size; fieldIndex++) {
        this.columnList.add(getColumnInstance(record.getField(fieldIndex)));
      }
    }
    else {
      this.columnList = new ArrayList<Column>(columns);

      if (this.columnList.size() != record.size()) {
        logger.warn("The number of columns in the List does not match the number of fields in the Record!");
        throw new IllegalArgumentException("The number of columns in the List does not match the number of fields in the Record!");
      }

      for (Column column : this.columnList) {
        if (!record.containsField(column.getName())) {
          logger.warn("The Record does not contain field (" + column.getName() + ")!");
          throw new IllegalArgumentException("The Record does not contain field (" + column.getName() + ")!");
        }
      }
    }
  }

  /**
   * Adds the specified field to the Record object initialized with the specified value.
   * @param column the field to add to this Record.
   * @param defaultValue the default value for the new field.
   * @return a boolean value indicating whether the specified field was added to this Record.
   * @throws java.lang.IllegalStateException if the Column could not be added to the list of
   * columns after the field was added to the Record.
   */
  public boolean addField(final Column column, final Object defaultValue) {
    if (record.addField(column.getName(), defaultValue)) {
      if (!columnList.add(column)) {
        logger.warn("Failed to add column (" + column + ") to the list of columns!");
        throw new IllegalStateException("Failed to add column (" + column + ") to the list of columns!");
      }
      return true;
    }
    return false;
  }

  /**
   * Determines whether this Record contains the specified field.
   * @param column the field being determined for containment by this Record.
   * @return a boolean value indicating if this Record contains the specified field.
   */
  public boolean containsField(final Column column) {
    return columnList.contains(column);
  }

  /**
   * Implementation of the copy method (same as clone operation) upholding the Records contract with
   * the Copyable interface.
   * @return a copy of this Record as an instance of the Record interface.
   * @see com.cp.common.lang.Copyable#copy
   */
  public Object copy() {
    return AbstractRecordFactory.getInstance().getRecordInstance(record);
  }

  /**
   * Creates an instance of the Column interface with the given name.  The default Column type
   * is Object.
   * @param fieldName the name of the column.
   * @return an instance of the Column interface initialized using the field name as it's column
   * name.
   */
  private Column getColumnInstance(final String fieldName) {
    return new ImmutableColumn(fieldName);
  }

  /**
   * Gets the field at the specified index.
   * @param fieldIndex the integer index of the field in the Record.
   * @return the field in the Record at the specified index.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Column getField(final int fieldIndex) {
    return columnList.get(fieldIndex);
  }

  /**
   * Gets the field index for the specified field in this Record.
   * @param column the field to obtain the index of.
   * @return the integer index in this Record for the specified field.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public int getFieldIndex(final Column column) throws NoSuchFieldException {
    return columnList.indexOf(column);
  }

  /**
   * Gets the value of the specified field in this Record.
   * @param column the field to return a value for.
   * @return the Object value of the specified field in this Record.
   * @throws java.lang.NoSuchFieldException if the specified field does not exist in this Record.
   */
  public <T> T getValue(final Column column) throws NoSuchFieldException {
    return (T) record.getValue(column.getName());
  }

  /**
   * Removes the specified field from this Record and returns it's value.
   * @param column the field to remove from this Record.
   * @return the Object value of the removed field.
   * @throws java.lang.IllegalStateException of the Record's fields and list of columns are inconsistent.
   * @throws java.lang.NoSuchFieldException if the specified field does not exist in this Record.
   */
  public Object removeField(final Column column) throws NoSuchFieldException {
    if (columnList.remove(column)) {
      try {
        return record.removeField(column.getName());
      }
      catch (NoSuchFieldException e) {
        if (!columnList.add(column)) {
          logger.warn("The Record and list of columns are out-of-sync for Column (" + column + ")!");
          throw new IllegalStateException("The Record and list of columns are out-of-sync for Column (" + column + ")!");
        }
        throw e;
      }
    }
    else {
      logger.warn("Failed to remove uknown column (" + column + ") from this Record!");
      throw new NoSuchFieldException("Failed to remove uknown column (" + column + ") from this Record!");
    }
  }

  /**
   * Sets the value of the specified field in this Record to the specified Object value.
   * @param column the field to set the value of.
   * @param value the Object value to set on the specified field in this Record.
   * @return the original Object value of the specified field in this Record.
   * @throws java.lang.NoSuchFieldException if the specified field does not exist in this Record.
   */
  public Object setValue(final Column column, final Object value) throws NoSuchFieldException {
    return record.setValue(column.getName(), value);
  }

  /**
   * Gets the number of fields in this Record.
   * @return and integer value of the number of fields in this Record.
   */
  public int size() {
    return record.size();
  }

  /**
   * Returns a String representation of the wrapped Record<String> object.
   * @return a String representation of the wrapped Record<String> object.
   */
  public String toString() {
    return record.toString();
  }

  private final class ImmutableColumn extends ColumnImpl {

    /**
     * Creates an instance of the ImmutableColumn class, an instance of the Column
     * interface.
     * @param name the name of this Column.
     */
    public ImmutableColumn(final String name) {
      super(name, Object.class);
    }

    /**
     * Sets a custom Comarator object used to compare the various values for this Column, having the
     * defined type.
     * @param comparator the custom Comparator implementation used to compare values for this Column.
     */
    public void setComparator(Comparator comparator) {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    /**
     * Sets the default value used when a record in a table of records does not specify a value for
     * this Column.
     * @param defaultValue an Object representing the default value used when no value is provided
     * for this Column.
     */
    public void setDefaultValue(final Object defaultValue) {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    /**
     * Sets the description of this Column instance describing the meaning behind data in this column.
     * @param description the String description of this Column.
     */
    public void setDescription(final String description) {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    /**
     * Sets the presentation name of this Column instance that will be used for display purposes.
     * @param displayName the String presentation display name of this Column instance.
     */
    public void setDisplayName(final String displayName) {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    /**
     * Sets whether null values are allowed to be set as a value on this Column.
     * @param nullable is a boolean value indicating if null values are allowed to be set on this Column.
     * @see com.cp.common.util.record.RecordTable#setCellValue on the corresponding record table data structure which will throw a
     * NullPointerException if the column is non-nullable and the user attempts to set a null value on
     * this Column.
     */
    public void setNullable(final boolean nullable) {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    /**
     * Sets the size of the column's data type value.
     * @param size a numerical value representing the size of the data value with respect to type.
     */
    public void setSize(final int size) {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    /**
     * Sets whether value in this column should be unique.
     * @param unique a boolean value indicating whether values of this Column are to be unique.
     */
    public void setUnique(boolean unique) {
      throw new UnsupportedOperationException("Not Implemented!");
    }
  }

}
