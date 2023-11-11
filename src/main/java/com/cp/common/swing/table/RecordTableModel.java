/*
 * RecordTableModel.java (c) 17 April 2002
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @auther John J. Blum
 * @version 2006.10.24
 * @see javax.swing.table.TableModel
 */

package com.cp.common.swing.table;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.record.RecordTable;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;

public class RecordTableModel extends AbstractTableModel {

  private static final Logger logger = Logger.getLogger(RecordTableModel.class);

  private final RecordTable recordTable;

  /**
   * Creates an instance of the RecordTableModel class initialized to the specified RecordTable object.
   * The instance of the this TableModel will be used to view the contents of the RecordTable object in
   * an JTable Swing GUI component.
   */
  public RecordTableModel(final RecordTable recordTable) {
    if (ObjectUtil.isNull(recordTable)) {
      logger.warn("The RecordTable object cannot be null!");
      throw new NullPointerException("The RecordTable object cannot be null!");
    }
    this.recordTable = recordTable;
  }

  /**
   * Returns the object type of the column indexed by the integer value parameter.
   * @param columnIndex an integer value index of the column within the table.
   * @return a Class object specifying the type of data contained in the column indexed by columnIndex.
   */
  public Class getColumnClass(final int columnIndex) {
    if (logger.isDebugEnabled()) {
      logger.debug("columnIndex = " + columnIndex);
    }
    return recordTable.getColumn(columnIndex).getType();
  }

  /**
   * Returns the number of columns in the table.
   * @return an integer value specifying the number of columns in the table.
   */
  public int getColumnCount() {
    return recordTable.columnCount();
  }

  /**
   * Returns the name of the column at columnIndex within this table.
   * @param columnIndex is an integer value specifying the index of the column in this table.
   * @return a String value specifying the name of the column at the given columnIndex within this table.
   */
  public String getColumnName(final int columnIndex) {
    if (logger.isDebugEnabled()) {
      logger.debug("columnIndex = " + columnIndex);
    }
    return recordTable.getColumn(columnIndex).getDisplayName();
  }

  /**
   * Accessor method for subclasses to obtain a reference to the underlying RecordTable object.
   * @return the instance of the underlying RecordTable object.
   */
  protected final RecordTable getRecordTable() {
    return recordTable;
  }

  /**
   * Returns the number of rows in the table.
   * @return an integer value indicating the number of rows in this table.
   */
  public int getRowCount() {
    return recordTable.rowCount();
  }

  /**
   * Returns the Object value at the specified cell (rowIndex, column) in this table.
   * @param rowIndex the rowIndex index in this table.
   * @param columnIndex the column index in this table.
   * @return the Object value at the specified cell (rowIndex, column) in this table.
   */
  public Object getValueAt(final int rowIndex, final int columnIndex) {
    if (logger.isDebugEnabled()) {
      logger.debug("rowIndex = " + rowIndex);
      logger.debug("columnIndex = " + columnIndex);
    }
    return recordTable.getCellValue(rowIndex, columnIndex);
  }

  /**
   * Returns whether the information in the specified cell (rowIndex, column) of this table can be modified.
   * @param rowIndex the rowIndex index in this table.
   * @param columnIndex the column index in this table.
   * @return a boolean indicating whether the user can modify the value at the specified cell (rowIndex, column)
   * in the table.
   */
  public boolean isCellEditable(final int rowIndex, final int columnIndex) {
    if (logger.isDebugEnabled()) {
      logger.debug("rowIndex = " + rowIndex);
      logger.debug("columnIndex = " + columnIndex);
    }
    return recordTable.isMutable();
  }

  /**
   * Sets the corresponding cell (rowIndex, columnIndex) to the specified Object value.
   * @param value the Object value to set the corresponding cell to.
   * @param rowIndex the row index in this table.
   * @param columnIndex the column index in this table.
   */
  public void setValueAt(final Object value, final int rowIndex, final int columnIndex) {
    if (logger.isDebugEnabled()) {
      logger.debug("value (" + value + ")");
      logger.debug("rowIndex = " + rowIndex);
      logger.debug("columnIndex = " + columnIndex);
    }
    recordTable.setCellValue(rowIndex, columnIndex, value);
  }

}
