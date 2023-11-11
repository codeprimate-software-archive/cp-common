/*
 * ColumnImpl.java (c) 17 November 2003
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 */

package com.cp.common.util.record;

import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.ObjectUtil;
import java.util.Comparator;
import org.apache.log4j.Logger;

public class ColumnImpl implements Column {

  private static final Logger logger = Logger.getLogger(ColumnImpl.class);

  protected static final boolean DEFAULT_NULLABLE = true;
  protected static final boolean DEFAULT_UNIQUE = false;

  private boolean nullable = DEFAULT_NULLABLE;
  private boolean unique = DEFAULT_UNIQUE;

  private int size;

  private Class type;

  private Comparator comparator;

  private Object defaultValue;

  private String description;
  private String displayName;
  private String name;

  /**
   * Creates an instance of the ColumImpl class to represent a column in a table, spreadsheet,
   * or other tabular data struture.
   * @param name the name of the column.
   * @param type the Class type of the values that are allowed in the column of the tabular
   * data structure.
   * @throws java.lang.NullPointerException if the name or type of the column is null.
   */
  public ColumnImpl(final String name, final Class type) {
    if (logger.isDebugEnabled()) {
      logger.debug("creating column (" + name + ") of type (" + type + ")");
    }
    setName(name);
    setType(type);
  }

  /**
   * Determines whether this Column is equal to some other Object.
   * @param obj the Object being tested for equality with this Column.
   * @return a boolean value indicating whether this Column and the Object parameter are equal.
   */
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof Column)) {
      return false;
    }

    final com.cp.common.util.record.Column that = (Column) obj;

    return ObjectUtil.equals(getDefaultValue(), that.getDefaultValue())
      && ObjectUtil.equals(getDescription(), that.getDescription())
      && ObjectUtil.equals(getDisplayName(), that.getDisplayName())
      && ObjectUtil.equals(getName(), that.getName())
      && (isNullable() == that.isNullable())
      && (getSize() == that.getSize())
      && ObjectUtil.equals(getType(), that.getType())
      && (isUnique() == that.isUnique());
  }

  /**
   * Returns a Comparator object that is used to compare values of the given type defined by this Column.
   * @return a Comparator instance for values of this Column type.
   */
  public Comparator getComparator() {
    return comparator;
  }

  /**
   * Sets a custom Comarator object used to compare the various values for this Column, having the
   * defined type.
   * @param comparator the custom Comparator implementation used to compare values for this Column.
   */
  public void setComparator(final Comparator comparator) {
    if (logger.isDebugEnabled()) {
      logger.debug("comparator (" + comparator + ")");
    }
    this.comparator = comparator;
  }

  /**
   * Returns the default value for this Column which is used for records in a table of records that
   * do not specify a value for this column.
   * @return the default value used when no value is provided for this column.
   */
  public Object getDefaultValue() {
    return defaultValue;
  }

  /**
   * Sets the default value used when a record in a table of records does not specify a value for
   * this Column.
   * @param defaultValue an Object representing the default value used when no value is provided
   * for this Column.
   * @throws java.lang.IllegalArgumentException if the defaultValue's type is not the correct type
   * of value for this column.
   */
  public void setDefaultValue(final Object defaultValue) {
    if (logger.isDebugEnabled()) {
      logger.debug("defaultValue (" + defaultValue + ")");
    }

    validateType(defaultValue);
    validateSize(defaultValue); // TODO: think of how to enforce the size constraint for all types
    this.defaultValue = defaultValue;
  }

  /**
   * Returns a description of this Column and it's purpose.
   * @return a String describing the reason for this Column and the information that it holds.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of this Column instance describing the meaning behind data in this column.
   * @param description the String description of this Column.
   */
  public void setDescription(final String description) {
    if (logger.isDebugEnabled()) {
      logger.debug("description (" + description + ")");
    }
    this.description = description;
  }

  /**
   * Returns the presentation name of this Column used for display purposes.
   * @return a String representation of this Column to display.
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Sets the presentation name of this Column instance that will be used for display purposes.
   * @param displayName the String presentation display name of this Column instance.
   */
  public void setDisplayName(final String displayName) {
    if (logger.isDebugEnabled()) {
      logger.debug("displayName (" + displayName + ")");
    }
    this.displayName = displayName;
  }

  /**
   * Computes the hash value of this Column.
   * @return a integer value for the computed hash value of this Column.
   */
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getDefaultValue());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getDescription());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getDisplayName());
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getName());
    hashValue = 37 * hashValue + (isNullable() ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode());
    hashValue = 37 * hashValue + (new Integer(getSize())).hashCode();
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getType());
    hashValue = 37 * hashValue + (isUnique() ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode());
    return hashValue;
  }

  /**
   * Returns the proper name of the Column instance.
   * @return a String representing the proper name of this Column instance.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the Column instance.
   * @param name the name of the Column.
   * @throws java.lang.NullPointerException if the name of the Column is null.
   */
  private void setName(final String name) {
    if (logger.isDebugEnabled()) {
      logger.debug("name (" + name + ")");
    }
    if (ObjectUtil.isNull(name)) {
      logger.warn("The name of the Column cannot be null!");
      throw new NullPointerException("The name of the Column cannot be null!");
    }
    this.name = name;
  }

  /**
   * Determines whether null values are allowed in this Column.
   * @return a boolean value indicating whether null values are allowed.
   */
  public boolean isNullable() {
    return nullable;
  }

  /**
   * Sets whether null values are allowed to be set as a value on this Column.
   * @param nullable is a boolean value indicating if null values are allowed to be set on this Column.
   * @see com.cp.common.util.record.RecordTable#setCellValue on the corresponding record table data structure which will throw a
   * NullPointerException if the column is non-nullable and the user attempts to set a null value on
   * this Column.
   */
  public void setNullable(final boolean nullable) {
    if (logger.isDebugEnabled()) {
      logger.debug("nullable (" + nullable + ")");
    }
    this.nullable = nullable;
  }

  /**
   * Returns the data value size for this Column.  If the Column is of type character, than this method
   * returns the number of characters.  If this Column is a numerical type, than it returns the magnitude
   * of the value.
   * @return the size of the data value for the respective type.
   */
  public int getSize() {
    return size;
  }

  /**
   * Sets the size of the column's data type value.
   * @param size a numerical value representing the size of the data value with respect to type.
   */
  public void setSize(final int size) {
    if (logger.isDebugEnabled()) {
      logger.debug("size (" + size + ")");
    }
    this.size = size;
  }

  /**
   * Returns the type of data stored by this Column.
   * @return a Class object representing the type of data stored as values in this Column.
   */
  public Class getType() {
    return type;
  }

  /**
   * Sets the type of the Column.  The type specifies the kind of values the Column will contain.
   * @param type the Class type of values in this Column.
   * @throws java.lang.NullPointerException if the type is null.
   */
  private void setType(final Class type) {
    if (logger.isDebugEnabled()) {
      logger.debug("type (" + type + ")");
    }
    if (ObjectUtil.isNull(type)) {
      logger.warn("The type of the Column cannot be null!");
      throw new NullPointerException("The type of the Column cannot be null!");
    }
    this.type = type;
  }

  /**
   * Determines whether values kept this column are unique amongst records in the record table
   * data struture.
   * @return a boolean value indicating whether values of this column are unique.
   */
  public boolean isUnique() {
    return unique;
  }

  /**
   * Sets whether value in this column should be unique.
   * @param unique a boolean value indicating whether values of this Column are to be unique.
   */
  public void setUnique(final boolean unique) {
    if (logger.isDebugEnabled()) {
      logger.debug("unique (" + unique + ")");
    }
    this.unique = unique;
  }

  /**
   * Returns a String representation of this Column.
   * @return a String representation of this Column.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{comparator = ");
    buffer.append(getComparator());
    buffer.append(", defaultValue = ").append(getDefaultValue());
    buffer.append(", description = ").append(getDescription());
    buffer.append(", displayName= ").append(getDisplayName());
    buffer.append(", name = ").append(getName());
    buffer.append(", nullable = ").append(isNullable());
    buffer.append(", size = ").append(getSize());
    buffer.append(", type = ").append(getType());
    buffer.append(", unique = ").append(isUnique());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

  /**
   * Verifies the size of value is less than or equal to the maximum size
   * defined by this column.
   * @param value the value used when a null value is specified as
   * the value for this column.
   * @see com.cp.common.util.record.ColumnImpl#getSize
   */
  private void validateSize(final Object value) {
    if (value instanceof String) {
      final String valueString = (String) value;
      if (valueString.length() > (getSize() > 0 ? getSize() : Integer.MAX_VALUE)) {
        logger.warn("The String length (" + valueString.length() + ") of the value is greater than the maximum size ("
          + getSize() + ") allowed by this Column (" + getName() + ")!");
        throw new IllegalArgumentException("The String length (" + valueString.length()
          + ") of the value is greater than the maximum size (" + getSize() + ") allowed by this Column ("
          + getName() + ")!");
      }
    }
  }

  /**
   * Verfies that the value type is assignable to this Column's type.
   * @param value the value who's type is being determined for compatiability with this Column's type.
   * @see com.cp.common.util.record.ColumnImpl#getType
   */
  private void validateType(final Object value) {
    if (ObjectUtil.isNotNull(value) && !getType().isAssignableFrom(value.getClass())) {
      logger.warn("The default value type (" + ClassUtil.getClassName(value) + " is not valid!  Expected type of ("
        + getType().getName() + ")");
      throw new IllegalArgumentException("The default value type (" + ClassUtil.getClassName(value)
        + " is not valid!  Expected type of (" + getType().getName() + ")");
    }
  }

}
