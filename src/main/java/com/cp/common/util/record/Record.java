/*
 * Record.java (c) 17 April 2002
 *
 * A Record is an ordered collection of fields mapped to values that form the structure
 * of the record.  The usual meaning of the term record originates from the database
 * record, which is a group of related information (fields) corresponding to a single
 * database table.  The notion of record, as defined by this interface, also applies to
 * records in the context of SQL and database queries.  A Record, for example, can be
 * derived from a <code>java.sql.ResultSet</code> object.  However, we overload the
 * definition of Record by meaning any group of related information (fields) regardless
 * of origin.  Therefore, a Record could be derived from a query (by way of the
 * <code>java.sql.ResultSet</code> object), which could be composed of information from
 * multiple tables via a join.
 * <br>
 * The Record interface extends the java.util.Map interface in the Java Platform API.
 * To distinguish the Record interface from the java.util.Map interface, it should be
 * emphasized that fields/keys for a Record are ordered according to the order given,
 * either through calling the addField method repeatedly, or through the structure
 * defined by some other data struture (another Map for instance).  If the data structure
 * is another java.util.Map object, then the order is determined by the set of keys returned
 * from a call to the keySet method.  If the data structure is a
 * <code>java.sql.ResultSet</code> object, then the order of the fields is determined by the
 * query (SQL) that generated the <code>java.sql.ResultSet</code> object.
 *
 * Copyright (c) 2002, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.11.17
 * @see java.lang.Cloneable
 * @see java.lang.Comparable
 * @see java.io.Serializable
 * @see java.util.Map
 * @see com.cp.common.lang.Copyable
 * @see com.cp.common.lang.Mutable
 * @see com.cp.common.util.record.AbstractRecord
 * @see com.cp.common.util.record.RecordAdapter
 * @see com.cp.common.util.record.RecordTable
 * @see com.cp.common.util.Visitable
 * @see com.codeprimate.util.record.DefaultRecord
 */

package com.cp.common.util.record;

import com.cp.common.lang.Copyable;
import com.cp.common.lang.Mutable;
import com.cp.common.lang.Visitable;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

public interface Record<K> extends Cloneable, Comparable<Record>, Copyable, Map<K, Object>, Mutable, Serializable, Visitable {

  /**
   * Adds the specified field to the this Record, thus modifying it's structure.  The field value
   * defaults to null.
   * @param field the field to add to this Record.
   * @return a boolean value indicating whether the specified field was added to this Record.
   */
  public boolean addField(K field);

  /**
   * Adds the specified field to the Record object initialized with the specified value.
   * @param field the field to add to this Record.
   * @param defaultValue the default value for the new field.
   * @return a boolean value indicating whether the specified field was added to this Record.
   */
  public boolean addField(K field, Object defaultValue);

  /**
   * Determines whether this Record contains the specified field.
   * @param field the field being determined for containment by this Record.
   * @return a boolean value indicating if this Record contains the specified field.
   */
  public boolean containsField(K field);

  /**
   * Gets the value of the field at index as a Boolean.
   * @param fieldIndex the index of the field in the Record for which to return the value for.
   * @return the specified field value as a Boolean.
   * @throws java.lang.ClassCastException if the value of the field is not of type Boolean.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Boolean getBooleanValue(int fieldIndex);

  /**
   * Gets the value of the specified field as a Boolean.
   * @param field the field to return a Boolean value for.
   * @return the field value as a Boolean.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public Boolean getBooleanValue(K field) throws NoSuchFieldException;

  /**
   * Gets the value of the field at index as a Byte.
   * @param fieldIndex the index of the field in the Record in which to return the value for.
   * @return the specified field value as a Byte.
   * @throws java.lang.ClassCastException if the value of the field is not of type Byte.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Byte getByteValue(int fieldIndex);

  /**
   * Gets the value of the specified field as a Byte.
   * @param field the field to return a Byte value for.
   * @return the field value as a Byte.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public Byte getByteValue(K field) throws NoSuchFieldException;

  /**
   * Gets the value of the field at index as a Calendar.
   * @param fieldIndex the index of the field in the Record in which to return the value for.
   * @return the specified field value as a Calendar.
   * @throws java.lang.ClassCastException if the value of the field is not of type Calendar.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Calendar getCalendarValue(int fieldIndex);

  /**
   * Gets the value of the specified field as a Calendar.
   * @param field the field to return a Calendar value for.
   * @return the field value as a Calendar.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public Calendar getCalendarValue(K field) throws NoSuchFieldException;

  /**
   * Gets the value of the field at index as a Character.
   * @param fieldIndex the index of the field in the Record in which to return the value for.
   * @return the specified field value as a Character.
   * @throws java.lang.ClassCastException if the value of the field is not of type Character.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Character getCharacterValue(int fieldIndex);

  /**
   * Gets the value of the specified field as a Character.
   * @param field the field to return a Character value for.
   * @return the field value as a Character.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public Character getCharacterValue(K field) throws NoSuchFieldException;

  /**
   * Gets the value of the field at index as a Date.
   * @param fieldIndex the index of the field in the Record in which to return the value for.
   * @return the specified field value as a Date.
   * @throws java.lang.ClassCastException if the value of the field is not of type Date.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Date getDateValue(int fieldIndex);

  /**
   * Gets the value of the specified field as a Date.
   * @param field the field to return a Date value for.
   * @return the field value as a Date.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public Date getDateValue(K field) throws NoSuchFieldException;

  /**
   * Gets the value of the field at index as a Double.
   * @param fieldIndex the index of the field in the Record in which to return the value for.
   * @return the specified field value as a Double.
   * @throws java.lang.ClassCastException if the value of the field is not of type Double.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Double getDoubleValue(int fieldIndex);

  /**
   * Gets the value of the specified field as a Double.
   * @param field the field to return a Double value for.
   * @return the field value as a Double.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public Double getDoubleValue(K field) throws NoSuchFieldException;

  /**
   * Gets the field at the specified index.
   * @param fieldIndex the integer index of the field in the Record.
   * @return the field in the Record at the specified index.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public K getField(int fieldIndex);

  /**
   * Gets the field index for the specified field in this Record.
   * @param field the field to obtain the index of.
   * @return the integer index in this Record for the specified field.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public int getFieldIndex(K field) throws NoSuchFieldException;

  /**
   * Gets the value of the field at index as a Float.
   * @param fieldIndex the index of the field in the Record in which to return the value for.
   * @return the specified field value as a Float.
   * @throws java.lang.ClassCastException if the value of the field is not of type Float.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Float getFloatValue(int fieldIndex);

  /**
   * Gets the value of the specified field as a Float.
   * @param field the field to return a Float value for.
   * @return the field value as a Float.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public Float getFloatValue(K field) throws NoSuchFieldException;

  /**
   * Gets the value of the field at index as a Integer.
   * @param fieldIndex the index of the field in the Record in which to return the value for.
   * @return the specified field value as a Integer.
   * @throws java.lang.ClassCastException if the value of the field is not of type Integer.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Integer getIntegerValue(int fieldIndex);

  /**
   * Gets the value of the specified field as a Integer.
   * @param field the field to return a Integer value for.
   * @return the field value as a Integer.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public Integer getIntegerValue(K field) throws NoSuchFieldException;

  /**
   * Gets the value of the field at index as a Long.
   * @param fieldIndex the index of the field in the Record in which to return the value for.
   * @return the specified field value as a Long.
   * @throws java.lang.ClassCastException if the value of the field is not of type Long.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Long getLongValue(int fieldIndex);

  /**
   * Gets the value of the specified field as a Long.
   * @param field the field to return a Long value for.
   * @return the field value as a Long.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public Long getLongValue(K field) throws NoSuchFieldException;

  /**
   * Gets the value of the field at index as a Short.
   * @param fieldIndex the index of the field in the Record in which to return the value for.
   * @return the specified field value as a Short.
   * @throws java.lang.ClassCastException if the value of the field is not of type Short.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Short getShortValue(int fieldIndex);

  /**
   * Gets the value of the specified field as a Short.
   * @param field the field to return a Short value for.
   * @return the field value as a Short.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public Short getShortValue(K field) throws NoSuchFieldException;

  /**
   * Gets the value of the field at index as a String.
   * @param fieldIndex the index of the field in the Record in which to return the value for.
   * @return the specified field value as a String.
   * @throws java.lang.ClassCastException if the value of the field is not of type String.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public String getStringValue(int fieldIndex);

  /**
   * Gets the value of the specified field as a String.
   * @param field the field to return a String value for.
   * @return the field value as a String.
   * @throws java.lang.NoSuchFieldException if the field does not exist in this Record.
   */
  public String getStringValue(K field) throws NoSuchFieldException;

  /**
   * Gets the value of the field at the specified index in this Record.
   * @param fieldIndex the index of the field in the Record in which to return the value.
   * @return the specified field value.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public <T> T getValue(int fieldIndex);

  /**
   * Gets the value of the specified field in this Record.
   * @param field the field to return a value for.
   * @return the Object value of the specified field in this Record.
   * @throws java.lang.NoSuchFieldException if the specified field does not exist in this Record.
   */
  public <T> T getValue(K field) throws NoSuchFieldException;

  /**
   * Registers the specified Comparator object for the specified class type.  Thus, during
   * a comparison of Objects of the specified type, the associated Comparator to this type
   * will be used to determine the relationship.
   * @param type the Class type of the Objects for which the specified Comparator should be used.
   * @param comparator the Comparator instance used to the compare Objects of the specified type.
   */
  public void registerComparator(Class type, Comparator comparator);

  /**
   * Removes the field at index in this Record and returns it's value.
   * @param fieldIndex the integer index of the field to remove from this Record.
   * @return the value of the field being removed from this Record.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Object removeField(int fieldIndex);

  /**
   * Removes the specified field from this Record and returns it's value.
   * @param field the field to remove from this Record.
   * @return the Object value of the removed field.
   * @throws java.lang.NoSuchFieldException if the specified field does not exist in this Record.
   */
  public Object removeField(K field) throws NoSuchFieldException;

  /**
   * Sets the value of the field at index in this Record to the specified Object value.
   * @param fieldIndex the index of the field in this Record.
   * @param value the Object value to set on the specified field.
   * @return the original value of the field at index in this Record.
   * @throws java.lang.IndexOutOfBoundsException if the field index is less than zero or more than
   * the number of fields in this Record.
   */
  public Object setValue(int fieldIndex, Object value);

  /**
   * Sets the value of the specified field in this Record to the specified Object value.
   * @param field the field to set the value of.
   * @param value the Object value to set on the specified field in this Record.
   * @return the original Object value of the specified field in this Record.
   * @throws java.lang.NoSuchFieldException if the specified field does not exist in this Record.
   */
  public Object setValue(K field, Object value) throws NoSuchFieldException;

  /**
   * Gets the number of fields in this Record.
   * @return and integer value of the number of fields in this Record.
   */
  public int size();

  /**
   * Unregisters the Comparator object for the specified class type.  If the class type
   * was not mapped to a Comparator object, then the method returns null.
   * @param type the class type for which the Comparator was registered.
   * @return the Comparator registered with the specified class type.
   */
  public Object unregisterComparator(Class type);

}
