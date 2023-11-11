/*
 * CPRecordFactory.java (c) 4 March 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.8.14
 */

package com.codeprimate.util.record;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.record.AbstractRecordFactory;
import com.cp.common.util.record.Column;
import com.cp.common.util.record.Record;
import com.cp.common.util.record.RecordTable;
import org.apache.log4j.Logger;

public class CPRecordFactory extends AbstractRecordFactory {

  private static final Logger logger = Logger.getLogger(CPRecordFactory.class);

  /**
   * Returns a Code Primate instance of the Record interface.
   * @return a Record interface implementation.
   */
  public Record getRecordInstance() {
    return new com.codeprimate.util.record.DefaultRecord();
  }

  /**
   * Returns a Code Primate instance of the Record interface based on an existing
   * Record object.
   * @param record the Record instance on which the newly created Record object
   * is based.
   * @return a Record interface implementation initialized with the specified
   * Record object.
   */
  public Record getRecordInstance(final Record record) {
    return new com.codeprimate.util.record.DefaultRecord(record);
  }

  /**
   * Returns a Code Primate instance of the RecordTable interface.
   * @return a RecordTable interface implementation.
   */
  public com.cp.common.util.record.RecordTable getRecordTableInstance() {
    return new DefaultRecordTable();
  }

  /**
   * Returns a Code Primate instance of the RecordTable interface initialized
   * to the specified structure, columns.
   * @param columns the array of columns constituting the structure of the
   * RecordTable instance.
   * @return a RecordTable interface implemenation initialized with the
   * specified Column structure.
   */
  public com.cp.common.util.record.RecordTable getRecordTableInstance(final Column[] columns) {
    return new com.codeprimate.util.record.DefaultRecordTable(columns);
  }

  /**
   * Returns a service provider instance of the RecordTable interface based on an
   * existing RecordTable object.
   * @param recordTable the RecordTable object on which the newly created RecordTable
   * object is based.
   * @return a service provider implementation of the RecordTable interface.
   */
  public RecordTable getRecordTableInstance(final com.cp.common.util.record.RecordTable recordTable) {
    if (ObjectUtil.isNull(recordTable)) {
      logger.warn("The RecordTable parameter cannot be null!");
      throw new NullPointerException("The RecordTable parameter cannot be null!");
    }
    return (RecordTable) recordTable.copy();
  }

}
