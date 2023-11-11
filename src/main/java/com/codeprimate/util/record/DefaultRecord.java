/*
 * DefaultRecord.java (c) 17 April 2002
 *
 * This is the default implementation of the Record interface which extends the AbstractRecord class.
 * Note, that in some instances, the default method implementations in the AbstractRecord class are
 * overridden to provide a more efficient implemenation based on the internal data structure used.
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.11.17
 * @see com.cp.common.util.record.AbstractRecord;
 * @see com.cp.common.util.record.Record;
 * @see com.cp.common.util.record.RecordAdapter
 */

package com.codeprimate.util.record;

import com.cp.common.lang.StringUtil;
import com.cp.common.util.record.AbstractRecord;
import com.cp.common.util.record.Record;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class DefaultRecord extends AbstractRecord<String> {

  // TODO: implement some of java.util.Map methods for optimization using the internal data structure.

  private static final Logger logger = Logger.getLogger(DefaultRecord.class);

  private static final int INITIAL_CAPACITY = 53;
  private static final float LOAD_FACTOR = 0.75f;

  private final List<String> fieldList;

  private final Map<String, Object> record;

  /**
   * Constructs an instance of the DefaultRecord class using the default constructor for this class.
   */
  public DefaultRecord() {
    fieldList = new ArrayList<String>(INITIAL_CAPACITY);
    record = new HashMap<String, Object>(INITIAL_CAPACITY, LOAD_FACTOR);
  }

  /**
   * Copy constructor for the DefaultRecord class.
   * @param record the Record object whose contents is being copied to the new instance of this
   * DefaultRecord class.
   */
  public DefaultRecord(final com.cp.common.util.record.Record record) {
    fieldList = new ArrayList<String>(Math.max(record.size(), INITIAL_CAPACITY));
    this.record = new HashMap<String, Object>(Math.max(record.size(), INITIAL_CAPACITY), LOAD_FACTOR);

    // Copy the record contents to this DefaultRecord.
    for (Iterator it = record.entrySet().iterator(); it.hasNext(); ) {
      final Map.Entry recordEntry = (Map.Entry) it.next();
      fieldList.add(recordEntry.getKey().toString());
      this.record.put(recordEntry.getKey().toString(), recordEntry.getValue());
    }
  }

  /**
   * Adds the specified field to the Record object initialized with the specified value.
   * @param field the field to add to this Record.
   * @param defaultValue the default value for the new field.
   * @return a boolean value indicating whether the specified field was added to this Record.
   */
  public boolean addField(final String field, final Object defaultValue) {
    if (StringUtil.isEmpty(field)) {
      logger.warn("(" + field + ") is not a valid field!");
      throw new IllegalArgumentException("(" + field + ") is not a valid field!");
    }

    verifyMutable();

    if (!containsField(field)) {
      if (logger.isDebugEnabled()) {
        logger.debug("adding field (" + field + ") with default value (" + defaultValue + ")");
      }
      fieldList.add(field);
      record.put(field, defaultValue);
      incModCount();
    }
    else {
      logger.warn(field + " already exists!");
    }

    return true;
  }

  /**
   * Determines whether this Record contains the specified field.
   * @param field the field being determined for containment by this Record.
   * @return a boolean value indicating if this Record contains the specified field.
   */
  public boolean containsField(final String field) {
    if (logger.isDebugEnabled()) {
      logger.debug("contains field (" + field + ")");
    }
    return fieldList.contains(field);
  }

  /**
   * Implementation of the copy method (same as clone operation) upholding the Records contract with
   * the Copyable interface.
   * @return a copy of this Record as an instance of the Record interface.
   * @see com.cp.common.lang.Copyable#copy
   */
  public Object copy() {
    return new DefaultRecord(this);
  }

  /**
   * Gets the field at the specified index.
   * @param fieldIndex the integer index of the field in the Record.
   * @return the field in the Record at the specified index.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public String getField(final int fieldIndex) {
    if (logger.isDebugEnabled()) {
      logger.debug("getting field @ index (" + fieldIndex + ")");
    }

    validateFieldIndex(fieldIndex);

    return fieldList.get(fieldIndex);
  }

  /**
   * Gets the field index for the specified field in this Record.
   * @param field the field to obtain the index of.
   * @return the integer index in this Record for the specified field.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public int getFieldIndex(final String field) throws NoSuchFieldException {
    if (logger.isDebugEnabled()) {
      logger.debug("getting field index for field (" + field + ")");
    }

    validateField(field);

    return fieldList.indexOf(field);
  }

  /**
   * Gets the value of the specified field in this Record.
   * @param field the field to return a value for.
   * @return the Object value of the specified field in this Record.
   * @throws java.lang.NoSuchFieldException if the specified field does not exist in this Record.
   */
  public <T> T getValue(final String field) throws NoSuchFieldException {
    if (logger.isDebugEnabled()) {
      logger.debug("getting value for field (" + field + ")");
    }

    validateField(field);

    return (T) record.get(field);
  }

  /**
   * Removes the specified field from this Record and returns it's value.
   * @param field the field to remove from this Record.
   * @return the Object value of the removed field.
   * @throws java.lang.NoSuchFieldException if the specified field does not exist in this Record.
   */
  public Object removeField(final String field) throws NoSuchFieldException {
    if (logger.isDebugEnabled()) {
      logger.debug("removing field (" + field + ")");
    }

    verifyMutable();
    validateField(field);

    Object value = null;

    if (fieldList.remove(field)) {
      value = record.remove(field);
    }
    else {
      logger.warn("failed to remove field (" + field + ")!");
    }

    incModCount();

    return value;
  }

  /**
   * Sets the value of the specified field in this Record to the specified Object value.
   * @param field the field to set the value of.
   * @param value the Object value to set on the specified field in this Record.
   * @return the original Object value of the specified field in this Record.
   * @throws java.lang.NoSuchFieldException if the specified field does not exist in this Record.
   */
  public Object setValue(final String field, final Object value) throws NoSuchFieldException {
    if (logger.isDebugEnabled()) {
      logger.debug("setting field (" + field + ") with value (" + value + ")");
    }

    verifyMutable();
    validateField(field);

    final Object oldValue = record.get(field);

    if (logger.isDebugEnabled()) {
      logger.debug("old value (" + oldValue + ")");
    }

    // Set the record's field with caller's value.
    record.put(field, value);
    incModCount();

    return oldValue;
  }

  /**
   * Gets the number of fields in this Record.
   * @return and integer value of the number of fields in this Record.
   */
  public int size() {
    return record.size();
  }

  /**
   * Verifies that the specified field is contained by this Record and is a valid
   * field.
   * @param field the String field (key) mapping to a value in this Record object.
   * @throws NoSuchFieldException if the field does not exist in this Record.
   */
  private void validateField(final String field) throws NoSuchFieldException {
    if (logger.isDebugEnabled()) {
      logger.debug("validating field (" + field + ")");
    }

    if (!containsField(field)) {
      logger.warn(field + " is not a field in this Record!");
      throw new NoSuchFieldException(field + " is not a field in this Record!");
    }
  }

}
