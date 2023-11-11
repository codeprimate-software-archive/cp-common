/*
 * Column.java (c) 5 November 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2005.9.16
 */

package com.cp.common.util.record;

import java.io.Serializable;
import java.util.Comparator;

public interface Column extends Serializable {

  /**
   * Returns a Comparator object that is used to compare values of the given type defined by this Column.
   * @return a Comparator instance for values of this Column type.
   */
  public Comparator getComparator();

  /**
   * Sets a custom Comarator object used to compare the various values for this Column, having the
   * defined type.
   * @param comparator the custom Comparator implementation used to compare values for this Column.
   */
  public void setComparator(Comparator comparator);

  /**
   * Returns the default value for this Column which is used for records in a table of records that
   * do not specify a value for this column.
   * @return the default value used when no value is provided for this column.
   */
  public Object getDefaultValue();

  /**
   * Sets the default value used when a record in a table of records does not specify a value for
   * this Column.
   * @param defaultValue an Object representing the default value used when no value is provided
   * for this Column.
   */
  public void setDefaultValue(Object defaultValue);

  /**
   * Returns a description of this Column and it's purpose.
   * @return a String describing the reason for this Column and the information that it holds.
   */
  public String getDescription();

  /**
   * Sets the description of this Column instance describing the meaning behind data in this column.
   * @param description the String description of this Column.
   */
  public void setDescription(String description);

  /**
   * Returns the presentation name of this Column used for display purposes.
   * @return a String representation of this Column to display.
   */
  public String getDisplayName();

  /**
   * Sets the presentation name of this Column instance that will be used for display purposes.
   * @param displayName the String presentation display name of this Column instance.
   */
  public void setDisplayName(String displayName);

  /**
   * Returns the proper name of the Column instance.
   * @return a String representing the proper name of this Column instance.
   */
  public String getName();

  /**
   * Determines whether null values are allowed in this Column.
   * @return a boolean value indicating whether null values are allowed.
   */
  public boolean isNullable();

  /**
   * Sets whether null values are allowed to be set as a value on this Column.
   * @param nullable is a boolean value indicating if null values are allowed to be set on this Column.
   * @see com.cp.common.util.record.RecordTable#setCellValue on the corresponding record table data structure which will throw a
   * NullPointerException if the column is non-nullable and the user attempts to set a null value on
   * this Column.
   */
  public void setNullable(boolean nullable);

  /**
   * Returns the data value size for this Column.  If the Column is of type character, than this method
   * returns the number of characters.  If this Column is a numerical type, than it returns the magnitude
   * of the value.
   * @return the size of the data value for the respective type.
   */
  public int getSize();

  /**
   * Sets the size of the column's data type value.
   * @param size a numerical value representing the size of the data value with respect to type.
   */
  public void setSize(int size);

  /**
   * Returns the type of data stored by this Column.
   * @return a Class object representing the type of data stored as values in this Column.
   */
  public Class getType();

  /**
   * Determines whether values kept this column are unique amongst records in the record table
   * data struture.
   * @return a boolean value indicating whether values of this column are unique.
   */
  public boolean isUnique();

  /**
   * Sets whether value in this column should be unique.
   * @param unique a boolean value indicating whether values of this Column are to be unique.
   */
  public void setUnique(boolean unique);

}
