/*
 * RecordComparator.java (c) 3 December 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.1.16
 * @see java.util.Comparator
 * @see com.cp.common.util.ComparableComparator
 */

package com.cp.common.util.record;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ArrayUtil;
import com.cp.common.util.ComparableComparator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RecordComparator implements Comparator<Record> {

  protected final Log logger = LogFactory.getLog(getClass());

  private final Column[] columns;

  private final Map<Class, Comparator> classComparatorMap;

  /**
   * Creates an instance of the RecordComparator class used to sort records contained in a Collection
   * data struture where the order is determined by the columns in the Column array.
   * @param columns an array of columns used to order records in a Collection.
   */
  public RecordComparator(final Column[] columns) {
    Assert.isFalse(ArrayUtil.isEmpty(columns), "The columns cannot be null or empty!");

    if (logger.isDebugEnabled()) {
      logger.debug("columns (" + ArrayUtil.toString(columns) + ")");
    }

    this.columns = columns;
    classComparatorMap = new HashMap<Class, Comparator>();
  }

  /**
   * Compares two Record objects to determine their order based on the Columns in the array
   * passed to the RecordComparator constructor.
   * @param record1 the first Record object used in the comparison.
   * @param record2 the second Record object being compared to the first Record object to determine order.
   * @return a negative integer value if the first Record object is less than the second Record object,
   * a positive integer value if the first Record object is greater than the second Record object, and 0
   * if the two Record objects are equal based on the given columns.
   */
  public int compare(final Record record1, final Record record2) {
    for (final Column column : columns) {
      if (logger.isDebugEnabled()) {
        logger.debug("column (" + column + ")");
      }

      try {
        final Object recordValue1 = record1.getValue(column.getName());
        final Object recordValue2 = record2.getValue(column.getName());

        if (logger.isDebugEnabled()) {
          logger.debug("recordValue1 (" + recordValue1 + ")");
          logger.debug("recordValue2 (" + recordValue2 + ")");
        }

        final int compareValue = getComparator(column).compare(recordValue1, recordValue2);

        if (logger.isDebugEnabled()) {
          logger.debug("compareValue (" + compareValue + ")");
        }

        if (compareValue != 0) {
          return compareValue;
        }
      }
      catch (NoSuchFieldException ignore) {
        logger.warn(column.getName() + " is not a valid column in the record!");
        throw new IllegalArgumentException(column.getName() + " is not a valid column in the record!");
      }
    }

    return 0;
  }

  /**
   * Returns a custom Comparator based on the Column's class type or the default Comparator if the caller
   * did not define a custom Comparator based on the type of values stored in the Column.
   * @param column the Column whose type is used to obtain a custom comparator.
   * @return a Comparator for values base on the Column's type or the default Comparator if a Comparator
   * has not been specified.
   */
  private Comparator getComparator(final Column column) {
    final Comparator comparator = classComparatorMap.get(column.getType());

    if (logger.isDebugEnabled()) {
      logger.debug("Returning custom Comparator (" + comparator + ") for Column (" + column + ").");
    }

    return (ObjectUtil.isNull(comparator) ? new ComparableComparator() : comparator);
  }

  /**
   * Registers a custom Comparator for a specific type of data value.
   * @param type the Class type of the data value for which a custom Comparator will be registered.
   * @param comparator the Comparator to associate with data value of the specified class type.
   * @throws java.lang.NullPointerException if the type is null.
   */
  public void registerComparator(final Class type, final Comparator comparator) {
    Assert.notNull(type, "The type cannot be null!");

    if (logger.isDebugEnabled()) {
      logger.debug("Registering Comparator (" + comparator + ") for Class type (" + type + ")");
    }

    classComparatorMap.put(type, comparator);
  }

  /**
   * Returns a String representation of this Comparator.
   * @return a String representation of this Comparator.
   */
  public String toString() {
    return getClass().getName();
  }

  /**
   * Removes the Comparator association for the specified Class type.
   * @param type the Class type for which a custom Comparator had previously been associated.
   * @return the Comparator registered to the specified Class type.
   */
  public Comparator unregistorComparator(final Class type) {
    if (logger.isDebugEnabled()) {
      logger.debug("Unregistering Comparator for Class type (" + type + ")");
    }

    return classComparatorMap.remove(type);
  }

}
